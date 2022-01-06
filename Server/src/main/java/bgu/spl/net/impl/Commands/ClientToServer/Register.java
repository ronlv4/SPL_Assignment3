package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.bidi.BGSService;

import java.util.Arrays;

import static bgu.spl.net.utils.Helpers.indexOf;

public class Register implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

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
    public boolean execute(BGSService service, int connectionId) {
        return service.registerUser(connectionId, userName, password, birthday);
    }


    @Override
    public void decode(byte[] commandBytes) {
        int prev = 2;
        int next = indexOf(commandBytes, ((byte) 0), prev);
        userName = new String(Arrays.copyOfRange(commandBytes, prev, next));
        prev = next + 1;
        next = indexOf(commandBytes, ((byte) 0), prev);
        password = new String(Arrays.copyOfRange(commandBytes, prev, next));
        prev = next + 1;
        birthday = new String(Arrays.copyOfRange(commandBytes, prev, commandBytes.length));
    }

    public static short getOpCode() {
        return 1;
    }


}
