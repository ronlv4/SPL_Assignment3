package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;
import static bgu.spl.net.utils.Helpers.indexOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Stat implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    List<String> userNames;

    @Override
    public ServerToClientCommand<BGSService> execute(BGSService service, int connectionId) {
        return service.stat(connectionId, userNames);
    }

    @Override
    public void decode(byte[] commandBytes) {
        userNames = new LinkedList<>();
        int prev = 2;
        int next = indexOf(commandBytes, ((byte) 124), prev );
        while (next != -1) {
            userNames.add(new String(Arrays.copyOfRange(commandBytes, prev, next)));
            prev = next + 1;
            next = indexOf(commandBytes, ((byte) 124), prev );
        }
        userNames.add(new String(Arrays.copyOfRange(commandBytes, prev, commandBytes.length - 1)));
    }
}