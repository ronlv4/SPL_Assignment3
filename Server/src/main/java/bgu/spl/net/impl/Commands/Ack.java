package bgu.spl.net.impl.Commands;

import java.io.Serializable;

public class Ack implements BaseCommand{
    @Override
    public void addArgument(byte[] arg) {

    }

    @Override
    public Serializable execute(Object arg) {
        return null;
    }
}
