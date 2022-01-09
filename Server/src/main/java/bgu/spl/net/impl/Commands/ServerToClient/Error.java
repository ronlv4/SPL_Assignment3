package bgu.spl.net.impl.Commands.ServerToClient;

import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

import static bgu.spl.net.utils.Helpers.shortToBytes;

public class Error implements ServerToClientCommand<BGSService>, CommandWithArguments<BGSService> {

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
    public byte[] encode(byte delimiter) {
        byte[] byteResponse = new byte[5];
        byte[] byteErrorOpCode = shortToBytes((short) 11);
        byte[] byteMessageOpCode = shortToBytes(messageOpCode);
        for (int i = 0; i < 2; i++) {
            byteResponse[i] = byteErrorOpCode[i];
            byteResponse[i + 2] = byteMessageOpCode[i];
        }
        byteResponse[4] = delimiter;
        return byteResponse;
    }
}
