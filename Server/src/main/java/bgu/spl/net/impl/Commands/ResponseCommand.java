package bgu.spl.net.impl.Commands;

public interface ResponseCommand<T> extends BaseCommand<T> {

    byte[] encode();
}
