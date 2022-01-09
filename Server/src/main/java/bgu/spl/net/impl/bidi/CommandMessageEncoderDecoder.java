package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.Commands.*;
import bgu.spl.net.impl.Commands.ClientToServer.*;

import java.util.Arrays;

public class CommandMessageEncoderDecoder implements MessageEncoderDecoder<BaseCommand<BGSService>> {
    private byte[] commandBytes = new byte[1 << 10];
    private int len = 0;
    private ClientToServerCommand<BGSService> command;


    public BaseCommand<BGSService> decodeNextByte(byte nextByte) {
        if (len == 2) {
            short opCode = decodeOpcode();
            command = createAction(opCode);
        }
        if (nextByte == ';') {
            command.decode(commandBytes);
            len = 0;
            return command;
        }
        pushByte(nextByte);
        return null;
    }

    @Override
    public byte[] encode(BaseCommand<BGSService> message) {
        return ((ServerToClientCommand<BGSService>) message).encode(((byte) ';'));
    }


    private ClientToServerCommand<BGSService> createAction(short opCode) {
        switch (opCode) {
            case 1: // Register
                return new Register();
            case 2: // Login
                return new Login();
            case 3: // Logout
                return new Logout();
            case 4: // Follow/Unfollow
                return new Follow();
            case 5: // Post
                return new Post();
            case 6: // PM
                return new PM();
            case 7: // Logged in States
                return new LogStat();
            case 8: // Stats
                return new Stat();
            case 12: // Block
                return new Block();
        }
        return null;
    }

    private short decodeOpcode() {
        short opcode = (short) ((commandBytes[0] & 0xff) << 8);
        opcode += (short) (commandBytes[1] & 0xff);
        return opcode;
    }

    private void pushByte(byte nextByte) {
        if (len >= commandBytes.length) {
            commandBytes = Arrays.copyOf(commandBytes, len * 2);
        }
        commandBytes[len++] = nextByte;
    }
}
