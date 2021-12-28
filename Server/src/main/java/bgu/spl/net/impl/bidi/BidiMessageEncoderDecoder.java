package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BidiMessageEncoderDecoder<T> implements MessageEncoderDecoder<T> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public T decodeNextByte(byte nextByte) {
        if (len == 2)
            return decodeCommand();
        if (nextByte == ';')
            return popString();
        pushByte(nextByte);
        return null;
    }

    @Override
    public byte[] encode(T message) {
        return new byte[0];
    }

    private T popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        T gener;
        for (Byte curByte: bytes){
            gener += (T)(curByte & 0xff);

        }
        return command;
        result = (T)Convert.ChangeType(bytes, typeof(T));
        return result;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    public short decodeCommand(){
        short command = (short)((bytes[0] & 0xff) << 8);
        command += (short)(bytes[1] & 0xff);
        return command;
    }
}
