import java.io.IOException;

/**
 * Created by Cata on 7/8/2016.
 */
public class Game {
    public static void main(String args[])
    {
        Server sr=new Server();
        try {
            sr.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
