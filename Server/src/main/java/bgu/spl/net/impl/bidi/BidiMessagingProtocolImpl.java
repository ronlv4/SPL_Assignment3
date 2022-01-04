package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.Commands.BaseCommand;
import bgu.spl.net.impl.Commands.ClientToServer;
import bgu.spl.net.impl.Commands.ServerToClient;

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
        ServerToClient<BGSService> response = ((ClientToServer<BGSService>)message).execute(bgsService, connectionId);
        activeConnections.send(connectionId, response);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}

