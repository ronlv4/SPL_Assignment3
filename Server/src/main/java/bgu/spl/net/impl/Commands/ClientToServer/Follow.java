package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

import java.util.Arrays;

import static bgu.spl.net.utils.Helpers.indexOf;


public class Follow implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    private byte followUnfollow;
    private String userName;

    public Follow() {
    }

    public Follow(byte followUnfollow, String userName) {
        this.followUnfollow = followUnfollow;
        this.userName = userName;
    }

    @Override
    public ServerToClientCommand<BGSService> execute(BGSService service, int connectionId) {
        return service.followUser(connectionId, followUnfollow, userName);
    }

    @Override
    public void decode(byte[] commandBytes) {
        followUnfollow = commandBytes[2];
        int next = indexOf(commandBytes, ((byte) 0), 3);
        userName = new String(Arrays.copyOfRange(commandBytes, 3, next));
    }

    public static short getOpcode(){
        return 4;
    }

    public byte getFollowUnfollow(){
        return followUnfollow;
    }
    public String getUserName(){
        return userName;
    }
}
