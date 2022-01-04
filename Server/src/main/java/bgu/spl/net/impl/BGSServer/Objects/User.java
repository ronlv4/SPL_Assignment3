package bgu.spl.net.impl.BGSServer.Objects;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TransferQueue;

public class User {
    private String userName;
    private String password;
    private String birthday;
    private boolean loggedIn = false;
    private Set<User> following = new HashSet<>();

    public User(String userName, String password, String birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void login(){
        loggedIn = true;
    }

    public void logout(){
        loggedIn = false;
    }

    public boolean isFollowing(String userName){
        return following.stream().anyMatch(user -> user.getUserName().equals(userName));
    }

    public void addFollower(User user){
        following.add(user);
    }

    public void removeFollower(User user){
        following.remove(user);
    }
}
