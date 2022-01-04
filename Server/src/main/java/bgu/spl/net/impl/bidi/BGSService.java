package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Objects.User;
import bgu.spl.net.impl.Commands.*;
import bgu.spl.net.impl.Commands.Error;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BGSService {

    private Map<Integer, User> usersByConId = new ConcurrentHashMap<>();
    private Map<String, User> usersByUserName = new ConcurrentHashMap<>();
    private Connections<BGSService> activeConnections = new ConnectionsImpl<>();

    public ServerToClient<BGSService> registerUser(int connectionId, String userName, String password, String birthday){
        if (usersByUserName.containsKey(userName))
            return new Error(Register.getOpCode());
        User userToAdd = new User(userName, password, birthday);
        usersByConId.put(connectionId,userToAdd);
        usersByUserName.put(userName, userToAdd);
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
        return ack;
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
        User user = usersByConId.get(connectionId);
        if (user == null)
            return new Error(Post.getOpCode());
    }

    public ServerToClient<BGSService> SendPM(int connectionId, String userName, String content, String time){

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