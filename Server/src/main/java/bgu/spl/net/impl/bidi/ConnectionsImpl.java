package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImpl<T> implements Connections<T> {

    private Map<Integer, ConnectionHandler<T>> activeConnections = new ConcurrentHashMap<>();
    private AtomicInteger connectionId = new AtomicInteger(0);

    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler<T> handler = activeConnections.get(connectionId);
        if (handler == null)
            return false;
        try {
            handler.send(msg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void broadcast(T msg) {
        for (ConnectionHandler<T> handler : activeConnections.values()) {
            handler.send(msg);
        }

    }

    @Override
    public void disconnect(int connectionId) {
        ConnectionHandler<T> handler = activeConnections.get(connectionId);
        try {
            handler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        activeConnections.remove(connectionId);
    }

    public int addConnection(ConnectionHandler<T> handler) {
        int conId = connectionId.getAndIncrement();
        activeConnections.put(conId, handler);
        return conId;
    }

}
