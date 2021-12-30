package bgu.spl.net.impl.Commands;

import java.util.ArrayList;

public class Register implements BaseCommand {

    String userName;
    String password;
    String birthday;

    public Register() {
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
    public void execute() {

    }

}
