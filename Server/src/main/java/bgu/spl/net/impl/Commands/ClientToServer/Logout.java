package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

public class Logout implements ClientToServerCommand<BGSService> {

    public static short getOpCode(){
        return 3;
    }

    @Override
    public ServerToClientCommand<BGSService> execute(BGSService service, int connectionId) {
        return service.logoutUser(connectionId);
    }
}
