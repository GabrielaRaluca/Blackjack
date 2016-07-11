package server;

import server.Deck;
import server.Player;
import server.PlayerThread;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private ExecutorService threadPool;
    private ArrayList<PlayerThread> threads;
    private Player dealer;
    private static int count;
    private Timer timer;
    private Date timeToRun;



    private volatile int turn;
    private volatile boolean gameStarted;
    private Deck deck;
    private volatile boolean running;

    private final int portNumber = 9999;





    public Server() {

        threadPool = Executors.newCachedThreadPool();
        turn = 0;
        count = 0;
        gameStarted = false;
        threads = new ArrayList<PlayerThread>();
        dealer = new Player();
        deck = new Deck();
        running = true;


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
      public synchronized void getcard(server.Deck object);
     */
    }




    public void start() throws IOException {

        while (running) {
            try {
                System.out.println("Waiting...");
                clientSocket = serverSocket.accept();









                threads.add(new PlayerThread());
                threadPool.execute(threads.get(threads.size() - 1));
                count++;

                if (count == 1) {

                    int interval = 10000; // 10 sec
                    timeToRun = new Date(System.currentTimeMillis() + interval);
                    timer = new Timer();

                    timer.schedule(new TimerTask() {
                        public void run() {
                            // Task here ...
                            running = false;

                            try {
                                serverSocket.close();
                            } catch (IOException e) {
                                System.out.println("No more clients can connect!");
                            }
                            timer.cancel();
                        }
                    }, timeToRun);

                }



                System.out.println("Client " + count + " has connected");


            } catch (IOException e) {
               System.out.println("No more clients can connect!");
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

        getCardsForDealer();

    }

    public int getTurn() {
        return turn;
    }

    public Deck getDeck() {
        return deck;
    }

    public Player getDealer() {
        return dealer;
    }

    public void getCardsForDealer(){
        while(dealer.total() < 16)
        {
            //adauga carti dealer si calculeaza totalul
        }
    }
}