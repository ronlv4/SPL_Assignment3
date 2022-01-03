package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

public class Logout implements ServerCommand<BGSService> {

    @Override
    public ResponseCommand<BGSService> execute(BGSService service) {
        serve
    }

    @Override
    public void addArgument(byte[] arg) {

    }

    public static short getOpCode(){
        return 3;
    }
}
