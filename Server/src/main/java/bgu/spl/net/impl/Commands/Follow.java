package bgu.spl.net.impl.Commands;

import bgu.spl.net.impl.bidi.BGSService;

public class Follow implements ClientToServer<BGSService>, CommandWithArguments<BGSService> {

    private byte followUnfollow = -1;
    private String userName;

    public Follow() {
    }

    public Follow(byte followUnfollow, String userName) {
        this.followUnfollow = followUnfollow;
        this.userName = userName;
    }

    @Override
    public ServerToClient<BGSService> execute(BGSService service, int connectionId) {
        return service.followUser(connectionId, followUnfollow, userName);
    }

    @Override
    public void addArgument(byte[] arg) {
        if (followUnfollow == -1)
            followUnfollow = arg[0];
        else
            userName = new String(arg);

    }

    public static short getOpcode(){
        return 4;
    }
}
