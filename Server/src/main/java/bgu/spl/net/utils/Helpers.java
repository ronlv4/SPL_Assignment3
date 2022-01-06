package bgu.spl.net.utils;

public class Helpers {

    public static int indexOf(byte[] commandBytes,byte ch, int from){
        for (int i = from; i < commandBytes.length; i++){
            if (commandBytes[i] == ch)
                return i;
        }
        return -1;
    }

    public static byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    public static short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

}
