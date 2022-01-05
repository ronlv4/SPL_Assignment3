package bgu.spl.net.impl.Commands;

public interface CommandWithArguments<T> extends BaseCommand<T> {

    void decode(byte[] commandBytes);
}
