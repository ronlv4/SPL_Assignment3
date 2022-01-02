package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

import java.io.Serializable;

public class Ack implements ResponseCommand<BGSService> {

    private short messageOpCode;

    public Ack(short messageOpCode) {
        this.messageOpCode = messageOpCode;
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
}
