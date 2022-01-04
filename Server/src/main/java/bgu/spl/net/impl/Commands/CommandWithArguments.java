package bgu.spl.net.impl.Commands;

public interface CommandWithArguments<T> extends BaseCommand<T> {

    void addArgument(byte[] arg);
}
