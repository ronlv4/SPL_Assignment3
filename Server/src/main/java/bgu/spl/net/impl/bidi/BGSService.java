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

    public BGSService(Connections<BaseCommand<BGSService>> activeConnections) {
        this.activeConnections = activeConnections;
    }

    private Map<Integer, User> connectedUsers = new ConcurrentHashMap<>();
    private Map<String, User> usersByUserName = new ConcurrentHashMap<>();
    private Connections<BaseCommand<BGSService>> activeConnections;
    private Map<User, List<Post>> postDB = new ConcurrentHashMap<>();
    private Map<User, List<PM>> messagesDB = new ConcurrentHashMap<>();
    private Map<User, Queue<Notification>> usersNotifications = new ConcurrentHashMap<>();
    private Set<String> filteredWords = new HashSet<>(Arrays.asList("government", "area51"));


    public boolean registerUser(int connectionId, String userName, String password, String birthday) {
        if (usersByUserName.containsKey(userName))
            return activeConnections.send(connectionId,new Error(Register.getOpCode()));
        User userToAdd = new User(userName, password, birthday);
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
        User user = usersByUserName.get(userName);
        if (user == null)
            return activeConnections.send(connectionId, err);
        if (user.isLoggedIn())
            return activeConnections.send(connectionId, err);
        if (!user.getPassword().equals(password))
            return activeConnections.send(connectionId, err);
        user.login(connectionId);
        connectedUsers.put(connectionId, user);
        checkAndSendNotification(connectionId);
        return activeConnections.send(connectionId, ack);
    }

    private void checkAndSendNotification(int connectionId) {
        // assuming user exists
        Queue<Notification> notificationQueue = usersNotifications.get(connectedUsers.get(connectionId));
        for (Notification notification : notificationQueue)
            try {
                activeConnections.send(connectionId, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public boolean logoutUser(int connectionId) {
        User user = connectedUsers.get(connectionId);
        if (user == null)
            return activeConnections.send(connectionId, new Error(Logout.getOpCode())); // this client is not connected to any user
        user.logout();
        connectedUsers.remove(connectionId);
        if (activeConnections.send(connectionId, new Ack(Logout.getOpCode()))) {
            activeConnections.disconnect(connectionId);
            return true;
        }
        return false;
        // TODO delete all references to user
        // TODO terminate connection
    }

    public boolean followUser(int connectionId,Follow thisCommand, byte followUnfollow, String userName) {
        User thisUser = connectedUsers.get(connectionId);
        Error err = new Error(Follow.getOpcode());
        if (thisUser == null)
            return activeConnections.send(connectionId, err);
        User userToFollowUnfollow = usersByUserName.get(userName);
        if (userToFollowUnfollow == null)
            return activeConnections.send(connectionId, err);
        boolean isAlreadyFollowing = thisUser.isFollowing(userName);
        if (followUnfollow == 0 && isAlreadyFollowing)
            return activeConnections.send(connectionId, err);
        if (followUnfollow == 1 && (!isAlreadyFollowing))
            return activeConnections.send(connectionId, err);
        if (followUnfollow == 0) {
            thisUser.addFollowing(userToFollowUnfollow);
            userToFollowUnfollow.addFollower(thisUser);
            return activeConnections.send(connectionId, new Ack(Follow.getOpcode(), thisCommand));
        }
        thisUser.removeFollowing(userToFollowUnfollow);
        userToFollowUnfollow.removeFollower(thisUser);
        return activeConnections.send(connectionId, new Ack(Follow.getOpcode(), thisCommand));
    }

    public boolean post(int connectionId, String content) {
        User poster = connectedUsers.get(connectionId);
        Error err = new Error(Post.getOpCode());
        if (poster == null)
            return activeConnections.send(connectionId, err);
        Post post = new Post(poster, content);
        poster.addPost(post);
        List<String> tagList = post.getTagList();
        for (String userName : tagList) {
            User notifiedUser = usersByUserName.get(userName);
            if (notifiedUser != null) {
                Notification notification = new Notification(((byte) 1), poster.getUserName(), content);
                sendOrStoreNotification(notifiedUser, notification);
            }
        }
        for (User user: poster.getFollowers()){
            Notification notification = new Notification(((byte) 1), poster.getUserName(), content);
            sendOrStoreNotification(user, notification);
        }

        return activeConnections.send(connectionId, new Ack(Post.getOpCode()));
    }

    /**
     * if notified user is logged in then send him a notification otherwise store this notification in his queue
     * @param notifiedUser
     * @param notification
     */
    private void sendOrStoreNotification(User notifiedUser, Notification notification) {
        if (notifiedUser.isLoggedIn())
            sendNotification(notifiedUser.getConnectionId(), notification);
        else
            usersNotifications.get(notifiedUser).add(notification);
    }

    private boolean sendNotification(int connectionId, Notification notification) {
        return activeConnections.send(connectionId, notification);
    }

    public boolean SendPM(int connectionId, PM message) {
        User sendingUser = connectedUsers.get(connectionId);
        Error err = new Error(PM.getOpCode());
        if (sendingUser == null)
            return activeConnections.send(connectionId, err);
        User receivingUser = usersByUserName.get(message.getUserName());
        if (receivingUser == null)
            return activeConnections.send(
                    connectionId,
                    new Error(PM.getOpCode(), "@" + message.getUserName() + " isn't applilcable for private messages." ));
        if (!sendingUser.isFollowing(receivingUser.getUserName()))
            return activeConnections.send(connectionId, err);
        for (String word : filteredWords)
            message.setContent(message.getContent().replace(word, "<filtered>"));
        messagesDB.get(sendingUser).add(message);
        return activeConnections.send(connectionId, new Ack(PM.getOpCode()));
    }

    public boolean logstat(int connectionId) {
        User user = connectedUsers.get(connectionId);
        Error err = new Error(LogStat.getOpCode());
        if (user == null)
            return activeConnections.send(connectionId, err);
        for (User userToLog: connectedUsers.values()){
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
        User user = connectedUsers.get(connectionId);
        Error err = new Error(LogStat.getOpCode());
        if (user == null)
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
        User userBlocking = connectedUsers.get(connectionId);
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