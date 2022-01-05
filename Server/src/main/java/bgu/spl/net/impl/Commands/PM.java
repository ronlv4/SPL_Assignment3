package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

public class PM implements ClientToServer<BGSService>, CommandWithArguments<BGSService> {

    private String userName;
    private String content;
    private String date;

    @Override
    public ServerToClient<BGSService> execute(BGSService service, int connectionId) {
        service.SendPM(connectionId, this);

    }

    @Override
    public void addArgument(byte[] arg) {

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
