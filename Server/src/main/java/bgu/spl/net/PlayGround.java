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
        String str1 = "adasdas0asdsa";
        String[] str2= str1.split("0");
        Arrays.toString(str2);
    }
}
