package bgu.spl.net.impl.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.Commands.*;
import com.sun.jdi.PrimitiveValue;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class CommandMessageEncoderDecoder implements MessageEncoderDecoder<BaseCommand<BGSService>> {
    private byte[] commandBytes = new byte[1 << 10];
    private int len = 0;
    private int lastArgumentPosition = 0;
    private BaseCommand<BGSService> command;


    public BaseCommand<BGSService> decodeNextByte(byte nextByte) {
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
    public byte[] encode(BaseCommand<BGSService> message) {
        return ((ServerToClient<BGSService>)message).encode();
    }


    private BaseCommand<BGSService> createAction(short opCode) {
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
//            case 6: // PM
//                return new PM();
//            case 7: // Logged in States
//                return new LoggedStates();
//            case 8: // Stats
//                return new Stats();
//            case 9: // Notification
//                return new Notification();
//            case 10: // Ack
//            case 11: // Error
//            case 12: // Block
        }
        //TODO complete implementation
        return null;
    }
    private short decodeOpcode() {
        short opcode = (short)((commandBytes[0] & 0xff) << 8);
        opcode += (short)(commandBytes[1] & 0xff);
        return opcode;
    }

//    private BaseCommand popString() {
//        String result = new String(commandBytes, 0, len, StandardCharsets.UTF_8);
//        len = 0;
//        for (Byte curByte: commandBytes){
//            gener += (T)(curByte & 0xff);
//        return command;
//        result = (T)Convert.ChangeType(bytes, typeof(T));
//        return result;
//        return null;
//    }

    private void pushByte(byte nextByte) {
        if (len >= commandBytes.length) {
            commandBytes = Arrays.copyOf(commandBytes, len * 2);
        }
        if (nextByte == '\0') {
            ((CommandWithArguments<BGSService>)command).addArgument(Arrays.copyOfRange(commandBytes, lastArgumentPosition, len));
            lastArgumentPosition = len;
        }
        commandBytes[len++] = nextByte;
    }
//    @Override
//    public byte[] encode(Serializable message) {
//        return serializeObject(message);

//    }
//    private byte[] serializeObject(Serializable message) {
//        try {
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//
//            //placeholder for the object size
//            for (int i = 0; i < 4; i++) {
//                bytes.write(0);
//            }
//
//            ObjectOutput out = new ObjectOutputStream(bytes);
//            out.writeObject(message);
//            out.flush();
//            byte[] result = bytes.toByteArray();
//
//            //now write the object size
//            ByteBuffer.wrap(result).putInt(result.length - 4);
//            return result;
//
//        } catch (Exception ex) {
//            throw new IllegalArgumentException("cannot serialize object", ex);
//        }

//    }
//    private Serializable deserializeCommand() {
//        try {
//            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(commandBytes));
//            Serializable serializableObject = (Serializable) in.readObject();
//            System.out.println(serializableObject);
//            return serializableObject;
////            return (Serializable) in.readObject();
//        } catch (Exception ex) {
//            throw new IllegalArgumentException("cannot desrialize object", ex);

//        }

//    }


//    @Override
//    public Serializable decodeNextByte(byte nextByte) {
//        if (commandBytes == null) { //indicates that we are still reading the length
//            lengthBuffer.put(nextByte);
//            if (!lengthBuffer.hasRemaining()) { //we read 4 bytes and therefore can take the length
//                lengthBuffer.flip();
//                commandBytes = new byte[lengthBuffer.getInt()];
//                commandBytesIndex = 0;
//                lengthBuffer.clear();
//            }
//        } else {
//            commandBytes[commandBytesIndex] = nextByte;
//            if (++commandBytesIndex == commandBytes.length) {
//                Serializable result = deserializeCommand();
//                commandBytes = null;
//                return result;
//            }
//        }
//
//        return null;
//    }

}
