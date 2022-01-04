package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

import java.io.Serializable;

public class Login implements ClientToServer<BGSService>, CommandWithArguments<BGSService> {

    private String userName;
    private String password;
    private byte captcha;


    @Override
    public ServerToClient<BGSService> execute(BGSService service, int connectionId) {
        return service.loginUser(connectionId, userName, password, captcha);
    }

    public static short getOpcode(){
        return 2;
    }

    @Override
    public void addArgument(byte[] arg) {
        if (userName == null)
            userName = new String(arg);
        else if (password == null)
            password = new String(arg);
        else
            captcha = arg[0];
    }
}
