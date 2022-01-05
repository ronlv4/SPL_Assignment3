package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

public class Notification implements ServerToClient<BGSService>, CommandWithArguments<BGSService> {

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
    public void addArgument(byte[] arg) {

    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }
}
