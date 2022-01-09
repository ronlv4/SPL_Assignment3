package bgu.spl.net.impl.Commands.ServerToClient;

import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

import java.nio.charset.StandardCharsets;

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
    public byte[] encode(byte delimiter) {
        byte[] response = ("\u0000\u0009" + ((char) type) + postingUser + "\u0000" + content + "\u0000" + ((char) delimiter)).getBytes(StandardCharsets.UTF_8);
         return  response;
    }
}
