package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

import java.util.Arrays;

import static bgu.spl.net.utils.Helpers.indexOf;

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
        int prev = 2;
        int next = indexOf(commandBytes, ((byte) 0), prev);
        userName = new String(Arrays.copyOfRange(commandBytes, prev, next));
        prev = next + 1;
        next = indexOf(commandBytes, ((byte) 0), prev);
        password = new String(Arrays.copyOfRange(commandBytes, prev, next));
        captcha = commandBytes[next + 1];
    }
}
