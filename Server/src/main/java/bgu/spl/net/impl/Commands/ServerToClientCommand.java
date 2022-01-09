package bgu.spl.net.impl.Commands;

public interface ServerToClientCommand<T> extends BaseCommand<T>{

    byte[] encode(byte delimiter);
}
