package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.Reactor;

public class ReactorMain {

    public static void main(String[] args) {
        Reactor<String> reactorServer = new Reactor<>(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                EchoProtocol::new,
                LineMessageEncoderDecoder::new);
        reactorServer.serve();
    }
}
