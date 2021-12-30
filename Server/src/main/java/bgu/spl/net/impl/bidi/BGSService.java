package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Objects.User;
import bgu.spl.net.impl.Commands.Ack;
import bgu.spl.net.impl.Commands.BaseCommand;
import bgu.spl.net.impl.Commands.Error;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BGSService {

    private Map<String, User> users;

    public Serializable registerUser(String userName, String password, String birthday){
        if (users.containsKey(userName))
            return new Error();
        users.put(userName,new User(userName,password, birthday));
        return new Ack();
    }

}
