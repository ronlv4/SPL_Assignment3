package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

public class PM implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    private String userName;
    private String content;
    private String date;

    @Override
    public ServerToClientCommand<BGSService> execute(BGSService service, int connectionId) {
       return service.SendPM(connectionId, this);

    }

    @Override
    public void decode(byte[] commandBytes) {

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
