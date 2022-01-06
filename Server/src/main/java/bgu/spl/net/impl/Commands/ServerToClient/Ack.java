package bgu.spl.net.impl.Commands.ServerToClient;

import bgu.spl.net.impl.BGSServer.Objects.UserStats;
import bgu.spl.net.impl.Commands.ClientToServer.Follow;
import bgu.spl.net.impl.Commands.ClientToServer.LogStat;
import bgu.spl.net.impl.Commands.ClientToServer.Stat;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

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
    public byte[] encode() {
        switch (messageOpCode){
            case 4:
                return encodeFollow();
            case 7:
                return encodeLogStat();
            case 8:
                return encodeStat();
        }
        byte[] byteResponse = new byte[4];
        byte[] byteErrorOpCode = shortToBytes((short) 10);
        byte[] byteMessageOpCode = shortToBytes(messageOpCode);
        for (int i = 0; i < 2; i++) {
            byteResponse[i] = byteErrorOpCode[i];
            byteResponse[i + 2] = byteMessageOpCode[i];
        }
        return byteResponse;
    }

    private byte[] encodeLogStat() {
        return encodeStatLogStat(LogStat.getOpCode());
    }

    private byte[] encodeStatLogStat(short opCode) {
        List<UserStats> userStats = (List<UserStats>) optional;
        byte[] byteResponse = new byte[12];
        byte[] ackOpCode = shortToBytes(Ack.getOpCode());
        byte[] logStatOpCode = shortToBytes(opCode);
        for (UserStats userStat : userStats) {
            byte[] age = shortToBytes(userStat.getAge());
            byte[] numPosts = shortToBytes(userStat.getPosts());
            byte[] numFollowers = shortToBytes(userStat.getFollowers());
            byte[] numFollowing = shortToBytes(userStat.getFollowing());
            for (int i = 0; i < 2; i++) {
                byteResponse[i] = ackOpCode[i];
                byteResponse[i + 2] = logStatOpCode[i];
                byteResponse[i + 4] = age[i];
                byteResponse[i + 6] = numPosts[i];
                byteResponse[i + 8] = numFollowers[i];
                byteResponse[i + 10] = numFollowing[i];
            }
        }
        return byteResponse;
    }

    public static short getOpCode(){
        return 10;
    }

    private byte[] encodeFollow() {
        byte[] byteResponse = new byte[4];
        byte[] ackOpCode = shortToBytes(Ack.getOpCode());
        byte[] followOpCode = shortToBytes(Follow.getOpcode());
        for (int i = 0; i < 2; i++){
            byteResponse[i] = ackOpCode[i];
            byteResponse[i+2] = followOpCode[i];
        }

        return new byte[0];
    }


//    private byte[] encodeStatLogStat(short opCode) {
//        List<UserStats> userStats= (List<UserStats>) optional;
//        byte[] byteResponse = new byte[12 * userStats.size()];
//        byte[] ackOpCode = shortToBytes(Ack.getOpCode());
//        byte[] logStatOpCode = shortToBytes(LogStat.getOpCode());
//        int userIndex = 0;
//        for (UserStats userStat: userStats){
//            byte[] age = shortToBytes(userStat.getAge());
//            byte[] numPosts = shortToBytes(userStat.getPosts());
//            byte[] numFollowers = shortToBytes(userStat.getFollowers());
//            byte[] numFollowing = shortToBytes(userStat.getFollowing());
//            for (int i = 0; i < 2; i++){
//                byteResponse[i + userIndex] = ackOpCode[i];
//                byteResponse[i + 2 + userIndex] = logStatOpCode[i + 2];
//                byteResponse[i + 4 + userIndex] = age[i + 4];
//                byteResponse[i + 6 + userIndex] = numPosts[i + 6];
//                byteResponse[i + 8 + userIndex] = numFollowers[i + 8];
//                byteResponse[i + 10 + userIndex] = numFollowing[i + 10];
//            }
//            userIndex += 12;
//        }
//        return byteResponse;
//    }

    private byte[] encodeStat() {
        return encodeStatLogStat(Stat.getOpCode());
    }
}
