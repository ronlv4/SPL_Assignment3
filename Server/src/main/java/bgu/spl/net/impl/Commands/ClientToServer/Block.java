package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;
import static bgu.spl.net.utils.Helpers.indexOf;


import java.util.Arrays;

public class Block implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    String userName;

    @Override
    public ServerToClientCommand<BGSService> execute(BGSService service, int connectionId) {
        return service.blockUser(connectionId, userName);
    }

    @Override
    public void decode(byte[] commandBytes) {
        userName = new String(Arrays.copyOfRange(commandBytes, 2, indexOf(commandBytes, (byte) 0, 2)));
    }

    public static short getOpCode(){
        return 12;
    }
}
