package bgu.spl.net.impl.Commands.ClientToServer;

import bgu.spl.net.impl.BGSServer.Objects.User;
import bgu.spl.net.impl.Commands.ClientToServerCommand;
import bgu.spl.net.impl.Commands.CommandWithArguments;
import bgu.spl.net.impl.bidi.BGSService;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static bgu.spl.net.utils.Helpers.indexOf;

public class Post implements ClientToServerCommand<BGSService>, CommandWithArguments<BGSService> {

    private User userPosted;
    private String content;
    private List<String> tagList;

    public Post() {
    }

    public Post(String content) {
        this.content = content;
    }

    public Post(User userPosted, String content) {
        this.userPosted = userPosted;
        this.content = content;
        initializeTagList();
    }

    @Override
    public boolean execute(BGSService service, int connectionId) {
        return service.post(connectionId, content);
    }

    @Override
    public void decode(byte[] commandBytes) {
        int next = indexOf(commandBytes, ((byte) 0), 2);
        this.content = new String(Arrays.copyOfRange(commandBytes, 2, next));
    }

    private void initializeTagList(){ // assuming valid input
        int userNameStartIndex = content.indexOf('@');
        List<String> userNames = new LinkedList<>();
        while (userNameStartIndex != -1){
            int userNameEndIndex = content.indexOf(' ', userNameStartIndex);
            if (userNameEndIndex != -1) {
                userNames.add(content.substring(userNameStartIndex, userNameEndIndex));
            }
            else {
                userNames.add(content.substring(userNameStartIndex));
            }
            userNameStartIndex = content.indexOf('@', userNameEndIndex);
        }
        tagList = userNames;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public static short getOpCode(){
        return 5;
    }

}
