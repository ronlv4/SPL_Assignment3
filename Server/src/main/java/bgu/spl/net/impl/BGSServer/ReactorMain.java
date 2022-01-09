
package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.Commands.BaseCommand;
import bgu.spl.net.impl.bidi.BGSService;
import bgu.spl.net.impl.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.bidi.CommandMessageEncoderDecoder;
import bgu.spl.net.impl.bidi.ConnectionsImpl;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {
        Connections<BaseCommand<BGSService>> activeConnections = new ConnectionsImpl<>();
        BGSService service = new BGSService(activeConnections);

        Server.reactor(
                Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                () -> new BidiMessagingProtocolImpl(service),
                CommandMessageEncoderDecoder::new,
                activeConnections
        ).serve();
    }
}
