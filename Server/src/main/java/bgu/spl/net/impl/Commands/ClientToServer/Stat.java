package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

import java.util.List;

public class Stat implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    List<String> userNames;

    @Override
    public ServerToClientCommand<BGSService> execute(BGSService service, int connectionId) {
        return service.stat(connectionId, userNames);
    }

    @Override
    public void decode(byte[] commandBytes) {

    }
}