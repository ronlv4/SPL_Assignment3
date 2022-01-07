package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.Commands.BaseCommand;
import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.ServerToClientCommand;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<BaseCommand<BGSService>>{

    private boolean shouldTerminate = false;
    private int connectionId;
    private Connections<BaseCommand<BGSService>> activeConnections;
    private BGSService bgsService;


    public BidiMessagingProtocolImpl(BGSService service){
        this.bgsService = service;
    }


    @Override
    public void start(int connectionId, Connections<BaseCommand<BGSService>> connections) {
        this.connectionId = connectionId;
        this.activeConnections = connections;
    }

    @Override
    public void process(BaseCommand<BGSService> message) {
        ((ClientToServerCommand<BGSService>)message).execute(bgsService, connectionId);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}

