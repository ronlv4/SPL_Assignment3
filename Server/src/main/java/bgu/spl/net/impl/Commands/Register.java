package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

public class Register implements ServerCommand<BGSService> {

    String userName;
    String password;
    String birthday;

    public Register() {
    }

    public Register(String userName, String password, String birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }

    @Override
    public ResponseCommand<BGSService> execute(BGSService service) {
        return service.registerUser(userName,password,birthday);
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

    public static short getOpCode(){
        return 1;
    }

}
