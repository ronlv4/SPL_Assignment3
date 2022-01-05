package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;
import jdk.internal.net.http.common.ImmutableExtendedSSLSession;

public class Error implements ServerToClient<BGSService>, CommandWithArguments<BGSService> {

    private short messageOpCode;
    private String errorMessage;

    public Error(short messageOpCode) {
        this.messageOpCode = messageOpCode;
    }

    public Error(short messageOpCode, String errorMessage) {
        this.messageOpCode = messageOpCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public byte[] encode() {
        byte[] byteResponse = new byte[4];
        byte[] byteErrorOpCode = shortToBytes((short) 11);
        byte[] byteMessageOpCode = shortToBytes(messageOpCode);
        for (int i = 0; i < 2; i++) {
            byteResponse[i] = byteErrorOpCode[i];
            byteResponse[i + 2] = byteMessageOpCode[i];
        }
        return byteResponse;
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

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }
}
