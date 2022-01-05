package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

public class Login implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    private String userName;
    private String password;
    private byte captcha;


    @Override
    public ServerToClientCommand<BGSService> execute(BGSService service, int connectionId) {
        return service.loginUser(connectionId, userName, password, captcha);
    }

    public static short getOpcode(){
        return 2;
    }

    @Override
    public void decode(byte[] commandBytes) {
        if (userName == null)
            userName = new String(commandBytes);
        else if (password == null)
            password = new String(commandBytes);
        else
            captcha = commandBytes[0];
    }
}
