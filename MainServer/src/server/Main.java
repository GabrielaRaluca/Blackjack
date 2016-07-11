package server;

import java.io.IOException;

/**
 * Created by teo on 11.07.2016.
 */
public class Main {
    public static void main(String[] args){
        Server server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
