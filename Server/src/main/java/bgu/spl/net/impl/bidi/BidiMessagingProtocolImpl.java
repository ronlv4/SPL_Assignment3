package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<Serializable> {

    private boolean shouldTerminate = false;
    private T arg;

    public BidiMessagingProtocolImpl(T arg) {
        this.arg = arg;
    }

    @Override
    public void start(int connectionId, Connections<Serializable> connections) {

    }

    @Override
    public void process(Serializable message) {
        Serializable response = ((Command) message).execute(arg);

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
