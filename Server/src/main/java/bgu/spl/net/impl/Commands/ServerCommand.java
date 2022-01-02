package bgu.spl.net.impl.Commands;

public interface ServerCommand<T> extends BaseCommand<T>{

    ResponseCommand<T> execute(T arg);

    void addArgument(byte[] arg);

}
