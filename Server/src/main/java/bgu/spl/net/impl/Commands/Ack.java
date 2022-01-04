package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

import java.io.Serializable;

public class Ack implements ServerToClient<BGSService>, CommandWithArguments<BGSService> {

    private short messageOpCode;
    private Object optional;

    public Ack(short messageOpCode) {
        this.messageOpCode = messageOpCode;
    }

    public Ack(short messageOpCode, Object optional) {
        this.messageOpCode = messageOpCode;
        this.optional = optional;
    }

    @Override
    public byte[] encode() {
        byte[] byteResponse = new byte[4];
        byte[] byteErrorOpCode = shortToBytes((short) 10);
        byte[] byteMessageOpCode = shortToBytes(messageOpCode);
        for (int i = 0; i < 2; i++) {
            byteResponse[i] = byteErrorOpCode[i];
            byteResponse[i + 2] = byteMessageOpCode[i];
        }
        return byteResponse;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    @Override
    public void addArgument(byte[] arg) {
        messageOpCode = bytesToShort(arg);
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }
}
