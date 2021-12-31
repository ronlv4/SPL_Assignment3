package bgu.spl.net.impl.Commands;

import java.io.Serializable;

public class Login implements BaseCommand{

    private String userName;
    private String password;
    private short captcha;


    @Override
    public Serializable execute(Object arg) {
        return null;
    }
}
