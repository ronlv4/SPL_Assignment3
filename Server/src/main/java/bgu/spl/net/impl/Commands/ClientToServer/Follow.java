package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.bidi.BGSService;

import java.util.Arrays;

import static bgu.spl.net.utils.Helpers.indexOf;


public class Follow implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    private byte followUnfollow;
    private String userName;

    public Follow() {
    }

    @Override
    public boolean execute(BGSService service, int connectionId) {
        return service.followUser(connectionId,this, followUnfollow, userName);
    }

    @Override
    public void decode(byte[] commandBytes) {
        followUnfollow = commandBytes[2];
        int next = indexOf(commandBytes, ((byte) 0), 2);
        userName = new String(Arrays.copyOfRange(commandBytes, 2, next));
    }

    public byte getFollowUnfollow() {
        return followUnfollow;
    }

    public String getUserName() {
        return userName;
    }

    public static short getOpcode(){
        return 4;
    }
}
