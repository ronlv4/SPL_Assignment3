package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

public class Post implements ClientToServer<BGSService>,CommandWithArguments<BGSService> {

    private String content;

    public Post() {
    }

    public Post(String content) {
        this.content = content;
    }

    @Override
    public ServerToClient<BGSService> execute(BGSService service, int connectionId) {
        return service.post(connectionId, content);
    }

    @Override
    public void addArgument(byte[] arg) {
        content = new String(arg);
    }

    public static short getOpCode(){
        return 5;
    }
}
