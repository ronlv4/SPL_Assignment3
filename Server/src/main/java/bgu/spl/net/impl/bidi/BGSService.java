package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Objects.User;
import bgu.spl.net.impl.BGSServer.Objects.UserStats;
import bgu.spl.net.impl.Commands.*;
import bgu.spl.net.impl.Commands.ClientToServer.*;
import bgu.spl.net.impl.Commands.ServerToClient.*;
import bgu.spl.net.impl.Commands.ServerToClient.Error;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;



public class BGSService {

    private Map<Integer, User> usersByConId = new ConcurrentHashMap<>();
    private Map<String, User> usersByUserName = new ConcurrentHashMap<>();
    private Connections<BaseCommand<BGSService>> activeConnections = new ConnectionsImpl<>();
    private Map<User, List<Post>> postDB = new ConcurrentHashMap<>();
    private Map<User, List<PM>> messagesDB = new ConcurrentHashMap<>();
    private Map<User, Queue<Notification>> usersNotifications = new ConcurrentHashMap<>();
    private Set<String> filteredWords = new HashSet<>(Arrays.asList("government", "area51"));


    public boolean registerUser(int connectionId, String userName, String password, String birthday) {
        if (usersByUserName.containsKey(userName))
            return activeConnections.send(connectionId,new Error(Register.getOpCode()));
        User userToAdd = new User(userName, password, birthday);
        usersByConId.put(connectionId, userToAdd);
        usersByUserName.put(userName, userToAdd);
        usersNotifications.put(userToAdd, new ConcurrentLinkedQueue<>());
        postDB.put(userToAdd, new LinkedList<>());
        messagesDB.put(userToAdd, new LinkedList<>());
        return activeConnections.send(connectionId, new Ack(Register.getOpCode()));
    }

    public boolean loginUser(int connectionId, String userName, String password, byte captcha) {
        Error err = new Error(Login.getOpcode());
        Ack ack = new Ack(Login.getOpcode());
        if (captcha == 0)
            return activeConnections.send(connectionId, err);
        User user = usersByConId.get(connectionId);
        if (user == null)
            return activeConnections.send(connectionId, err);
        if (!user.getUserName().equals(userName))
            return activeConnections.send(connectionId, err);
        if (user.isLoggedIn())
            return activeConnections.send(connectionId, err);
        if (!user.getPassword().equals(password))
            return activeConnections.send(connectionId, err);
        user.login();
        checkAndSendNotification(connectionId);
        return activeConnections.send(connectionId, ack);
    }

    private boolean checkAndSendNotification(int connectionId) {
        // assuming user exists
        Queue<Notification> notificationQueue = usersNotifications.get(usersByConId.get(connectionId));
        for (Notification notification : notificationQueue)
            try {
                activeConnections.send(connectionId, notification);
            } catch (Exception e) {
                return false;
            }
        return true;
    }

    public boolean logoutUser(int connectionId) {
        User user = usersByConId.get(connectionId);
        if (user == null)
            return activeConnections.send(connectionId, new Error(Logout.getOpCode()));
        user.logout();
        // TODO delete all references to user
        // TODO terminate connection
        return activeConnections.send(connectionId, new Ack(Logout.getOpCode()));
    }

    public boolean followUser(int connectionId,Follow thisCommand, byte followUnfollow, String userName) {
        User thisUser = usersByConId.get(connectionId);
        User userToFollowUnfollow = usersByUserName.get(userName);
        Error err = new Error(Follow.getOpcode());
        if (userToFollowUnfollow == null || thisUser == null)
            return activeConnections.send(connectionId, err );
        if (!thisUser.isLoggedIn())
            return activeConnections.send(connectionId, err);
        boolean isAlreadyFollowing = thisUser.isFollowing(userName);
        if (followUnfollow == 0 && isAlreadyFollowing)
            return activeConnections.send(connectionId, err);
        if (followUnfollow == 1 && (!isAlreadyFollowing))
            return activeConnections.send(connectionId, err);
        if (followUnfollow == 0) {
            thisUser.addFollower(userToFollowUnfollow);
            return activeConnections.send(connectionId, new Ack(Follow.getOpcode(), thisCommand));
        }
        thisUser.removeFollower(userToFollowUnfollow);
        return activeConnections.send(connectionId, new Ack(Follow.getOpcode(), thisCommand));
    }

    public boolean post(int connectionId, String content) {
        User poster = usersByConId.get(connectionId);
        Error err = new Error(Post.getOpCode());
        if (poster == null)
            return activeConnections.send(connectionId, err);
        if (!poster.isLoggedIn())
            return activeConnections.send(connectionId, err);
        Post post = new Post(poster, content);
        poster.addPost(post);
        List<String> tagList = post.getTagList();
        for (String userName : tagList) {
            User notifiedUser = usersByUserName.get(userName);
            if (notifiedUser != null) {
                Notification notification = new Notification(((byte) 1), poster.getUserName(), content);
                if (notifiedUser.isLoggedIn())
                    sendNotification(connectionId, notification);
                else
                    usersNotifications.get(notifiedUser).add(notification);
            }
        }
        return activeConnections.send(connectionId, new Ack(Post.getOpCode()));
    }

    private boolean sendNotification(int connectionId, Notification notification) {
        return activeConnections.send(connectionId, notification);
    }

    public boolean SendPM(int connectionId, PM message) {
        User sendingUser = usersByConId.get(connectionId);
        User receivingUser = usersByUserName.get(message.getUserName());
        Error err = new Error(PM.getOpCode());
        if (!sendingUser.isLoggedIn())
            return activeConnections.send(connectionId, err);
        if (receivingUser == null)
            return activeConnections.send(
                    connectionId,
                    new Error(PM.getOpCode(), "@" + message.getUserName() + " isn't applilcable for private messages." ));
        if (!sendingUser.isFollowing(message.getUserName()))
            return activeConnections.send(connectionId, err);
        for (String word : filteredWords)
            message.setContent(message.getContent().replace(word, "<filtered>"));
        messagesDB.get(sendingUser).add(message);
        return activeConnections.send(connectionId, new Ack(PM.getOpCode()));
    }

    public boolean logstat(int connectionId) {
        User user = usersByConId.get(connectionId);
        Error err = new Error(LogStat.getOpCode());
        if (user == null)
            return activeConnections.send(connectionId, err);
        if (!user.isLoggedIn())
            return activeConnections.send(connectionId, err);
        for (User userToLog: usersByUserName.values()){
            if (!userToLog.isLoggedIn())
                continue;
            if (userToLog.isBlocking(user))
                continue;
            try {
                activeConnections.send(connectionId, new Ack(LogStat.getOpCode(), new UserStats(userToLog)));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean stat(int connectionId, List<String> userNames) {
        User user = usersByConId.get(connectionId);
        Error err = new Error(LogStat.getOpCode());
        if (user == null)
            return activeConnections.send(connectionId, err);
        if (!user.isLoggedIn())
            return activeConnections.send(connectionId, err);
        for (String userNameToLog : userNames) {
            User userToLog = usersByUserName.get(userNameToLog);
            if (userToLog == null)
                return activeConnections.send(connectionId, err);
            if (userToLog.isBlocking(user) || user.isBlocking(userToLog))
                return activeConnections.send(connectionId, err);
            try {
                activeConnections.send(connectionId, new Ack(LogStat.getOpCode(), new UserStats(userToLog)));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean blockUser(int connectionId, String userName) {
        User userBlocking = usersByConId.get(connectionId);
        User userToBlock = usersByUserName.get(userName);
        Error err = new Error(Block.getOpCode());
        if (userBlocking == null)
            return activeConnections.send(connectionId, err);
        if (userToBlock == null)
            return activeConnections.send(connectionId, err);
        userBlocking.addToBlockingList(userToBlock);
        userToBlock.addToBlockersList(userBlocking);
        userToBlock.removeFollower(userBlocking);
        userBlocking.removeFollower(userToBlock);
        return activeConnections.send(connectionId, new Ack(Block.getOpCode()));
    }
}