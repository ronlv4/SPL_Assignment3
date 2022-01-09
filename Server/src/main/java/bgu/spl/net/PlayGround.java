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
        byte[] commandBytes = {'1', '\0', 48, 0, 127};
        byte followUnfollow = commandBytes[2];
    }
}
