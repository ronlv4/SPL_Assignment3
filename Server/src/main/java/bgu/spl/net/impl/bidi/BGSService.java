package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Objects.Notification;
import bgu.spl.net.impl.BGSServer.Objects.User;
import bgu.spl.net.impl.Commands.*;
import bgu.spl.net.impl.Commands.Error;
import bgu.spl.net.impl.Commands.Post;

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


    public ServerToClient<BGSService> registerUser(int connectionId, String userName, String password, String birthday){
        if (usersByUserName.containsKey(userName))
            return new Error(Register.getOpCode());
        User userToAdd = new User(userName, password, birthday);
        usersByConId.put(connectionId,userToAdd);
        usersByUserName.put(userName, userToAdd);
        usersNotifications.put(userToAdd, new ConcurrentLinkedQueue<>());
        postDB.put(userToAdd, new LinkedList<>());
        messagesDB.put(userToAdd, new LinkedList<>());
        return new Ack(Register.getOpCode());
    }

    public ServerToClient<BGSService> loginUser(int connectionId, String userName, String password, byte captcha){
        Error err = new Error(Login.getOpcode());
        Ack ack = new Ack(Login.getOpcode());
        if (captcha == 0)
            return err;
        User user = usersByConId.get(connectionId);
        if (user == null)
            return err;
        if (!user.getUserName().equals(userName))
            return err;
        if (user.isLoggedIn())
            return err;
        if (!user.getPassword().equals(password))
            return err;
        user.login();
        checkAndSendNotification(connectionId);
        return ack;
    }

    private void checkAndSendNotification(int connectionId) {
        // assuming user exists
        Queue<Notification> notificationQueue = usersNotifications.get(usersByConId.get(connectionId));
        for (Notification notification: notificationQueue)
            activeConnections.send(connectionId, notification);
    }

    public ServerToClient<BGSService> logoutUser(int connectionId){
        User user = usersByConId.get(connectionId);
        if (user == null)
            return new Error(Logout.getOpCode());
        user.logout();
        return new Ack(Logout.getOpCode());
    }

    public ServerToClient<BGSService> followUser(int connectionId, byte followUnfollow, String userName){
        User thisUser = usersByConId.get(connectionId);
        User userToFollowUnfollow = usersByUserName.get(userName);
        Error err = new Error(Follow.getOpcode());
        if (userToFollowUnfollow == null || thisUser == null)
            return err;
        if (!thisUser.isLoggedIn())
            return err;
        boolean isAlreadyFollowing = thisUser.isFollowing(userName);
        if (followUnfollow == 0 && isAlreadyFollowing)
            return err;
        if (followUnfollow == 1 && (!isAlreadyFollowing))
            return err;
        if (followUnfollow == 0) {
            String[] optional = {"adasd","asdfdf"};
            thisUser.addFollower(userToFollowUnfollow);
            return new Ack(Follow.getOpcode(), new Follow(followUnfollow, userName)); //TODO Ack format is unclear here
        }
        thisUser.removeFollower(userToFollowUnfollow);
        return new Ack(Follow.getOpcode(), new Follow(followUnfollow, userName)); //TODO Ack format is unclear here
    }

    public ServerToClient<BGSService> post(int connectionId, String content){
        User poster = usersByConId.get(connectionId);
        Error err = new Error(Post.getOpCode());
        if (poster == null)
            return err;
        if (!poster.isLoggedIn())
            return err;
        Post post = new Post(poster, content);
        poster.addPost(post);
        List<String> tagList = post.getTagList();
        for (String userName: tagList){
            User notifiedUser = usersByUserName.get(userName);
            if (notifiedUser != null) {
                Notification notification = new Notification(((byte) 1), poster.getUserName(), content);
                if (notifiedUser.isLoggedIn())
                    sendNotification(connectionId, notification);
                else
                    usersNotifications.get(notifiedUser).add(notification);
            }
        }
        return new Ack(Post.getOpCode());
    }

    private void sendNotification(int connectionId, Notification notification) {
        activeConnections.send(connectionId, notification);
    }

    public ServerToClient<BGSService> SendPM(int connectionId, PM message){
        User sendingUser = usersByConId.get(connectionId);
        User receivingUser = usersByUserName.get(message.getUserName());
        Error err = new Error(PM.getOpCode());
        if (!sendingUser.isLoggedIn())
            return err;
        if (receivingUser == null)
            return new Error(PM.getOpCode(), "@" + message.getUserName() + " isn’t applicable for private messages.");
        if (!sendingUser.isFollowing(message.getUserName()))
            return err;
        for (String word: filteredWords)
            message.setContent(message.getContent().replace(word, "<filtered>"));
        messagesDB.get(sendingUser).add(message);
        return new Ack(PM.getOpCode());
    }

    public ServerToClient<BGSService> logstat(int connectionId){


    }

    public ServerToClient<BGSService> stat(int connectionId, List<String> userNames){

    }

    public void notify(int connectionId){

    }

    public ServerToClient<BGSService> block(int connectionId){

    }
    }