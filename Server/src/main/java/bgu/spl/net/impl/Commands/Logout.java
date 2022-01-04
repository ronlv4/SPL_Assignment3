package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

public class Logout implements ClientToServer<BGSService> {

    public static short getOpCode(){
        return 3;
    }

    @Override
    public ServerToClient<BGSService> execute(BGSService service, int connectionId) {
        return service.logoutUser(connectionId);
    }
}
