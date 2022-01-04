package bgu.spl.net.impl.Commands;

public interface ServerToClient<T> extends BaseCommand<T>{

    byte[] encode();
}
