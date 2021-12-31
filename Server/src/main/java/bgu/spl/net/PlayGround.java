package bgu.spl.net;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PlayGround {
    public static void main(String[] args) {

        try {
            int i = 1/0;
        }catch (ArithmeticException e){
            System.out.println("Got this division by 0 exception");
        }
        System.out.println("outside try scope");

    }
}
