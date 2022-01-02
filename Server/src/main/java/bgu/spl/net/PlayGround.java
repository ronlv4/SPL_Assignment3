package bgu.spl.net;

import bgu.spl.net.impl.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.function.Supplier;

public class PlayGround {
    public static void main(String[] args) {
        Supplier<BidiMessagingProtocolImpl<String>> protocol = () -> new BidiMessagingProtocolImpl<>("hello");

        for (int i = 0; i < 10; i++) {
            System.out.println(protocol.get());
        }
    }
}
