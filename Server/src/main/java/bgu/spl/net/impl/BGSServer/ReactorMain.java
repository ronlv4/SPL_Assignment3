package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Reactor;

import java.util.function.Supplier;

public class ReactorMain {

    public static void main(String[] args) {

        Reactor<String> reactorServer = new Reactor<>(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                () -> new RemoteCommandInvocationProtocol<Object>("Hello"),
                ObjectEncoderDecoder::new
        );

        Reactor<String> reactorServer = new Reactor<>(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                () -> new EchoProtocol(),
                LineMessageEncoderDecoder::new
        );
        reactorServer.serve();
    }
}
