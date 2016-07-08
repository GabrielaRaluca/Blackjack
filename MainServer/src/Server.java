
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {

    public static ServerSocket serverSocket;
    public static Socket clientSocket;
    private ExecutorService threadPool;
    public ArrayList<ClientThread> threads;
    public Player dealer;
    public static int count;
    public volatile int turn;
    public volatile boolean gameStarted;
    public Deck deck;

    public final int portNumber = 9999;





    public Server() {

        threadPool = Executors.newCachedThreadPool();
        turn = 0;
        count = 0;
        gameStarted = false;
        threads = new ArrayList<ClientThread>();
        dealer = new Player();
        deck = new Deck();

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }





    private void split_two_cards(){
        int i;
        System.out.println("Split the cards:");
        for(i = 0; i < count; i++)
            threads.get(i).getcard(deck);
        dealer.getcard(deck);
        for(i = 0; i < count; i++)
            threads.get(i).getcard(deck);
        dealer.getcard(deck);
     /*
      public synchronized void getcard(Deck object);
     */
    }




    public void start() throws IOException {
        long startTime = 0 , endTime;
        while (true) {
            try {
                System.out.println("Waiting...");
                clientSocket = serverSocket.accept();

                if (count != 0) {
                    endTime = System.currentTimeMillis();
                    if ((endTime - startTime) >= 10000) {
                        System.out.println("There are " + count + "players.");
                        break;
                    }
                }


                threads.add(new ClientThread());
                threadPool.execute(threads.get(threads.size() - 1));
                count++;


                System.out.println("Client " + count + " has connected");
                if (count == 1) {
                    startTime = System.currentTimeMillis();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        deck.shuffle();
        split_two_cards();


        gameStarted = true;


        while(turn < count)
        {
            if(threads.get(turn).finished)
            {
                turn++;
            }
        }


    }
}