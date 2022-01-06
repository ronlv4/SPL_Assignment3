package bgu.spl.net.impl.Commands.ServerToClient;

import bgu.spl.net.impl.BGSServer.Objects.UserStats;
import bgu.spl.net.impl.Commands.ClientToServer.Follow;
import bgu.spl.net.impl.Commands.ClientToServer.LogStat;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

import java.util.LinkedList;
import java.util.List;

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
        byte[] byteACKOpCode = shortToBytes((short) 10);
        byte[] byteMessageOpCode = shortToBytes(messageOpCode);
        if(optional!=null){
            if(optional instanceof Follow){
                Follow cur = (Follow) optional;
                String userName = cur.getUserName();
                byte[] stringToBytes = userName.getBytes();
                int size = 5+stringToBytes.length;
                byte[] output = new byte[size];
                for (int i = 0; i < 2; i++) {
                    output[i] = byteACKOpCode[i];
                    output[i + 2] = byteMessageOpCode[i];
                }
                for (int i = 0; i<size; i++){
                    output[i+4] = stringToBytes[i];
                }
                output[size-1] = 0;
                return output;
            }
            else{
                LinkedList<UserStats> cur = (LinkedList<UserStats>)optional;
                byte[] output = new byte[12];
                while(!cur.isEmpty()){
                    UserStats user = cur.pop();
                    for (int i = 0; i < 2; i++) {
                        output[i] = byteACKOpCode[i];
                        output[i + 2] = byteMessageOpCode[i];
                        byte[] age = shortToBytes(user.getAge());
                        output[i + 4] = age[i];
                        byte[] followers = shortToBytes(user.getFollowers());
                        output[i + 6] = followers[i];
                        byte[] following = shortToBytes(user.getFollowing());
                        output[i + 8] = following[i];
                        byte[] posts = shortToBytes(user.getPosts());
                        output[i + 10] = posts[i];
                    }
                }
                return output;
            }
        }
        else{
            byte[] output = new byte[4];
            for (int i = 0; i < 2; i++) {
                output[i] = byteACKOpCode[i];
                output[i + 2] = byteMessageOpCode[i];
            }
            return output;
        }
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    @Override
    public void decode(byte[] commandBytes) {//why?

        messageOpCode = bytesToShort(commandBytes);
    }

    public short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }
}
