package bgu.spl.net.impl.Commands;

public interface ClientToServerCommand<T> extends BaseCommand<T> {

    boolean execute(T arg, int connectionId);

    void decode(byte[] commandBytes);

}
