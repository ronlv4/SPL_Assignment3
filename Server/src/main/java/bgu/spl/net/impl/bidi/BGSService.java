package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Objects.User;
import bgu.spl.net.impl.Commands.Ack;
import bgu.spl.net.impl.Commands.BaseCommand;
import bgu.spl.net.impl.Commands.Error;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BGSService {

    private Map<String, User> users = new ConcurrentHashMap<>();
    private Connections<BGSService> activeConnections = new ConnectionsImpl<>();

    public Serializable registerUser(String userName, String password, String birthday){
        if (users.containsKey(userName))
            return new Error();
        users.put(userName,new User(userName,password, birthday));
        return new Ack();
    }

}
