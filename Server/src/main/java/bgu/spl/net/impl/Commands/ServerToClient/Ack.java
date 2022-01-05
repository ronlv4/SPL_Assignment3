package bgu.spl.net.impl.Commands.ServerToClient;

import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

public class Ack implements ServerToClientCommand<BGSService>, CommandWithArguments<BGSService> {

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
    public void decode(byte[] commandBytes) {
        messageOpCode = bytesToShort(commandBytes);
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }
}
