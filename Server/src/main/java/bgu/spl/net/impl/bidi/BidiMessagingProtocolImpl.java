package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {

    private boolean shouldTerminate = false;

    @Override
    public void start(int connectionId, Connections<T> connections) {

    }

    @Override
    public void process(T message) {
        if (message instanceof String){
            String[] args = null;
            short opCode = extractCommand((String) message, args);


        }
        //TODO: what if message is not instance of String?

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    private short extractCommand(String message, String[] args){
        short opCode = (short)message.charAt(0);
        return 3;

    }
}
