package bgu.spl.net.utils;

public class Helpers {

    public static int indexOf(byte[] commandBytes,byte ch, int from){
        for (int i = from; i < commandBytes.length; i++){
            if (commandBytes[i] == ch)
                return i;
        }
        return -1;
    }

}
