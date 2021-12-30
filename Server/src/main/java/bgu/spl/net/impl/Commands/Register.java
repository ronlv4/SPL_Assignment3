package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;
import bgu.spl.net.impl.bidi.ConnectionsImpl;
import bgu.spl.net.impl.newsfeed.NewsFeed;

import java.io.Serializable;
import java.util.ArrayList;

public class Register implements BaseCommand<BGSService> {

    String userName;
    String password;
    String birthday;

    public Register(String userName, String password, String birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }

    @Override
    public void addArgument(byte[] arg) {
        if (userName == null)
            userName = new String(arg);
        else if (password == null)
            password = new String(arg);
        else
            birthday = new String(arg);
    }

    @Override
    public Serializable execute(BGSService bgsService) {
        return bgsService.registerUser(userName, password, birthday);
    }

}
