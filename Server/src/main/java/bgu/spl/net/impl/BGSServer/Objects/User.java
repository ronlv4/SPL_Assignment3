package bgu.spl.net.impl.BGSServer.Objects;

import java.util.HashSet;
import java.util.Set;

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
}
