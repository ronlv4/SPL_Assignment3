package bgu.spl.net.impl.Commands;

public interface ClientToServerCommand<T> extends BaseCommand<T> {

    ServerToClientCommand<T> execute(T arg, int connectionId);

    void decode(byte[] commandBytes);

}
