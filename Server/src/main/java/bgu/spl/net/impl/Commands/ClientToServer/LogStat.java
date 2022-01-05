package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

public class LogStat implements ClientToServerCommand<BGSService> {


    @Override
    public ServerToClientCommand<BGSService> execute(BGSService service, int connectionId) {
        return service.logstat(connectionId);
    }

    @Override
    public void decode(byte[] commandBytes) {

    }

    public static short getOpCode(){
        return 7;
    }
}
