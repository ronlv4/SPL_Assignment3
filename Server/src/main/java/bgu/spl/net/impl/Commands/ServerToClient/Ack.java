package bgu.spl.net.impl.Commands.ServerToClient;

import bgu.spl.net.impl.BGSServer.Objects.UserStats;
import bgu.spl.net.impl.Commands.ClientToServer.Follow;
import bgu.spl.net.impl.Commands.ClientToServer.LogStat;
import bgu.spl.net.impl.Commands.ClientToServer.Stat;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static bgu.spl.net.utils.Helpers.shortToBytes;

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
    public byte[] encode(byte delimiter) {
        switch (messageOpCode){
            case 4:
                return encodeFollow(delimiter);
            case 7:
                return encodeLogStat(delimiter);
            case 8:
                return encodeStat(delimiter);
        }
        byte[] byteResponse = new byte[5];
        byte[] byteAckOpCode = shortToBytes(getOpCode());
        byte[] byteMessageOpCode = shortToBytes(messageOpCode);
        for (int i = 0; i < 2; i++) {
            byteResponse[i] = byteAckOpCode[i];
            byteResponse[i + 2] = byteMessageOpCode[i];
        }
        byteResponse[4] = delimiter;
        return byteResponse;
    }

    private byte[] encodeFollow(byte delimiter) {
        Follow followCommand = ((Follow) optional);
        byte[] ackOpCode = shortToBytes(Ack.getOpCode());
        byte[] followOpCode = shortToBytes(Follow.getOpcode());
        byte[] userNameByte = followCommand.getUserName().getBytes(StandardCharsets.UTF_8);
        byte[] byteResponse = new byte[6 + userNameByte.length];
        for (int i = 0; i < 2; i++){
            byteResponse[i] = ackOpCode[i];
            byteResponse[i+2] = followOpCode[i];
        }
        for (int i = 0; i < userNameByte.length; i++){
            byteResponse[i + 4] = userNameByte[i];
        }
        byteResponse[byteResponse.length - 2] = '\0';
        byteResponse[byteResponse.length - 1] = (delimiter);
        return byteResponse;
    }

    private byte[] encodeLogStat(byte delimiter) {
        return encodeStatLogStat((short) 7, delimiter);
    }

    private byte[] encodeStat(byte delimiter) {
        return encodeStatLogStat((short) 8, delimiter);
    }

    private byte[] encodeStatLogStat(short opCode, byte delimiter) {
        UserStats userStats = (UserStats) optional;
        byte[] byteResponse = new byte[13];
        byte[] ackOpCode = shortToBytes(Ack.getOpCode());
        byte[] logStatOpCode = shortToBytes(opCode);
        byte[] age = shortToBytes(userStats.getAge());
        byte[] numPosts = shortToBytes(userStats.getPosts());
        byte[] numFollowers = shortToBytes(userStats.getFollowers());
        byte[] numFollowing = shortToBytes(userStats.getFollowing());
        for (int i = 0; i < 2; i++) {
            byteResponse[i] = ackOpCode[i];
            byteResponse[i + 2] = logStatOpCode[i];
            byteResponse[i + 4] = age[i];
            byteResponse[i + 6] = numPosts[i];
            byteResponse[i + 8] = numFollowers[i];
            byteResponse[i + 10] = numFollowing[i];
        }
        byteResponse[12] = delimiter;
        return byteResponse;
    }

    public static short getOpCode(){
        return 10;
    }
}
