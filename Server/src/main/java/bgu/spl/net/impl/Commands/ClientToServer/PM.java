package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.bidi.BGSService;

import java.util.Arrays;

import static bgu.spl.net.utils.Helpers.indexOf;

public class PM implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    private String userName;
    private String content;
    private String date;

    @Override
    public boolean execute(BGSService service, int connectionId) {
       return service.SendPM(connectionId, this);

    }

    @Override
    public void decode(byte[] commandBytes) {
        int next = indexOf(commandBytes, ((byte) 0), 2);
        userName = new String(Arrays.copyOfRange(commandBytes, 2, next));
        int prev = next + 1;
        next = indexOf(commandBytes, ((byte) 0), prev);
        content = new String(Arrays.copyOfRange(commandBytes, prev, next));
        prev = next + 1;
        next = indexOf(commandBytes, ((byte) 0), prev);
        date = new String(Arrays.copyOfRange(commandBytes, prev, next));
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public static short getOpCode(){
        return 6;
    }
}
