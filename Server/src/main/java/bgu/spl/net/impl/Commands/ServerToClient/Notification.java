package bgu.spl.net.impl.Commands.ServerToClient;

import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

public class Notification implements ServerToClientCommand<BGSService>, CommandWithArguments<BGSService> {

    private byte type;
    private String postingUser;
    private String content;


    public Notification() {
    }

    public Notification(byte type, String postingUser, String content) {
        this.type = type;
        this.postingUser = postingUser;
        this.content = content;
    }

    @Override
    public void decode(byte[] commandBytes) {

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
