package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.Commands.ServerToClientCommand;
import bgu.spl.net.impl.bidi.BGSService;

public class Follow implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    private byte followUnfollow = -1;
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
        if (followUnfollow == -1)
            followUnfollow = commandBytes[0];
        else
            userName = new String(commandBytes);

    }

    public static short getOpcode(){
        return 4;
    }
}
