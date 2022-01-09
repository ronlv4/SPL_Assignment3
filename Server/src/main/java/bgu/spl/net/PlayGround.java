package bgu.spl.net;

import bgu.spl.net.impl.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Supplier;

public class PlayGround {
    public static void main(String[] args) {
        byte type = '\u0001';
        String postingUser = "ron";
        String content = "wubwub";
        byte delimiter = ';';
        byte[] response = ("\u0000\u0009" + type + postingUser + "\u0000" + content + "\u0000" + ((char) delimiter)).getBytes(StandardCharsets.UTF_8);
        System.out.println("hello");
    }
}
