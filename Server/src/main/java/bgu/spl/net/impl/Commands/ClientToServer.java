package bgu.spl.net.impl.Commands;

public interface ClientToServer<T> extends BaseCommand<T> {

    public ServerToClient<T> execute(T arg, int connectionId);
}
