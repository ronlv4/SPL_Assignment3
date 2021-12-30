package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.Commands.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

public class CommandMessageEncoderDecoder implements MessageEncoderDecoder<Serializable> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int lastArgumentPosition = 0;
    private BaseCommand command;

    @Override
    public BaseCommand decodeNextByte(byte nextByte) {
        if (len == 2) {
            short opCode = decodeOpcode();
            command = createAction(opCode);
            lastArgumentPosition = 2;
        }
        if (nextByte == ';')
            return command;
        pushByte(nextByte);
        return null;
    }

    @Override
    public byte[] encode(BaseCommand message) {
        return new byte[0];
    }
    private BaseCommand createAction(short opCode) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (opCode){
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
                return new LoggedStates();
            case 8: // Stats
                return new Stats();
            case 9: // Notification
                return new Notification();
            case 10: // Ack
            case 11: // Error
            case 12: // Block
        }
        //TODO complete implementation
        return null;
    }

    private short decodeOpcode() {
        short opcode = (short)((bytes[0] & 0xff) << 8);
        opcode += (short)(bytes[1] & 0xff);
        return opcode;
    }

    private BaseCommand popString() {
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        for (Byte curByte: bytes){
//            gener += (T)(curByte & 0xff);

        }
//        return command;
//        result = (T)Convert.ChangeType(bytes, typeof(T));
//        return result;
        return null;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        if (nextByte == '\0') {
            command.addArgument(Arrays.copyOfRange(bytes, lastArgumentPosition, len));
            lastArgumentPosition = len;
        }
        bytes[len++] = nextByte;
    }


}
