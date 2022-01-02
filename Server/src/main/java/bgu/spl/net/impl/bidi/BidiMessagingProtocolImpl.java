package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.Commands.BaseCommand;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<Serializable> {

    private boolean shouldTerminate = false;
    private int connectionId;
    private Connections<Serializable> activeConnections;
    private T arg;

    public BidiMessagingProtocolImpl(T arg) {
        this.arg = arg;
    }

    @Override
    public void start(int connectionId, Connections<Serializable> connections) {
        this.connectionId = connectionId;
        this.activeConnections = connections;
        System.out.println("initializing protocol");
        System.out.println("activeConnections: " + activeConnections);
        System.out.println("Thread name: " + Thread.currentThread().getName());
        System.out.println("protocol: " + this);
        System.out.println("");
    }

    @Override
    public void process(Serializable message) {
        System.out.println("processing message");
        System.out.println("activeConnections: " + activeConnections);
        System.out.println("Thread name: " + Thread.currentThread().getName());
        System.out.println("protocol: " + this);
        System.out.println("");
        Serializable response = ((BaseCommand) message).execute(arg);
        activeConnections.send(connectionId, response);
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
