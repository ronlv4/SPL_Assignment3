package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.bidi.BGSService;
import bgu.spl.net.impl.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.bidi.CommandMessageEncoderDecoder;
import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

import java.io.Serializable;
import java.util.function.Supplier;

public class ReactorMain {

    public static void main(String[] args) {

        BGSService service = new BGSService();

        Server.reactor(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                () -> new BidiMessagingProtocolImpl<BGSService>(service),
                CommandMessageEncoderDecoder::new
        ).serve();

//        Reactor<Serializable> reactorServer = new Reactor<>(
//                Integer.parseInt(args[1]),
//                Integer.parseInt(args[0]),
//                () -> new RemoteCommandInvocationProtocol<Object>("Hello"),
//                ObjectEncoderDecoder::new
//        );

//        Reactor<String> reactorServer = new Reactor<>(
//                Integer.parseInt(args[1]),
//                Integer.parseInt(args[0]),
//                () -> new EchoProtocol(),
//                LineMessageEncoderDecoder::new
//        );

//        Server.reactor(
//                Runtime.getRuntime().availableProcessors(),
//                7777, //port
//                () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
//                ObjectEncoderDecoder::new //message encoder decoder factory
//        ).serve();


//        reactorServer.serve();
    }
}
