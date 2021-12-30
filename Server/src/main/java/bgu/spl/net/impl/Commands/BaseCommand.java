package bgu.spl.net.impl.Commands;

import java.io.Serializable;

public interface BaseCommand<T> extends Serializable {

//    public void addArgument(byte[] arg);

    Serializable execute(T arg);
}
