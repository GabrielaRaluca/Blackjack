import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Cata on 7/8/2016.
 */
public class PlayerThread extends Player implements Runnable {
    private static Server server;
    private Socket clientSocket;
    private ObjectInputStream in;
    public ObjectOutputStream out;

    String message;
    int turn;

    public PlayerThread(String name, int turn)
    {
        super(name);
        this.turn=turn;
        this.clientSocket=new Socket();
    }
    public PlayerThread(Socket clientSocket, Server server, int turn) {
        super("unnamed player");
        this.clientSocket = clientSocket;
        this.turn=turn;
        PlayerThread.server = server;
    }

    public void run() {
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            message = "";

            //Start the conversation
            while (true) {
                if (this.turn == server.turn) {
                    if (cards.size()<2) {
                        this.addCard(server.deck.drawCard());
                        out.writeObject(this+"/n");
                        out.flush();
                    }
                    else{
                        out.writeObject(this+"/n");
                        out.writeObject("It is your turn now! HIT or STAND?");
                        out.flush();


                        message = (String) in.readObject();
                        System.out.println("Client " + this.turn + ": " + message);


                        if (message.equals("STAND")) {
                            out.writeObject("STOP");
                            out.flush();
                        }
                        else if (message.equals("HIT")) {

                        }
                        else if (message.equals("STOP")) {
                            //finished = true;
                            break;
                        }
                    }
                }
                else {//for updating the real-time game
                    out.writeObject(server.getPlayerThread(server.turn));
                    out.flush();
                }
            }
            close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
    public void close()
    {
        try{
            in.close();
            out.close();
            clientSocket.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void setServer(Server server)
    {
        this.server=server;
    }


    public void hit() {//askCard();
    }

    public void stand(){
        this.receiving=false;
    }
    public void start() {
        this.receiving=true;
    }

}
