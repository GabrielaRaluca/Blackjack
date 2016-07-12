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



    private volatile boolean gameOver;

    private final int portNumber = 9999;





    public Server() {

        threadPool = Executors.newCachedThreadPool();
        turn = 0;
        count = 0;
        gameStarted = false;
        threads = new ArrayList<PlayerThread>();
        dealer = new Player("Dealer");
        deck = new Deck();
        running = true;
        gameOver = false;


        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    private int getMinim(){
        int minim = 22;
        for(int i = 0;i < count; i++)
            if(minim > threads.get(i).getTotal())
                minim = threads.get(i).getTotal();
        return minim;
    }




   private void showCards(){
       for(int i = 0; i < count; i++)
       {
           try {
               threads.get(i).getOutput().writeObject("Dealer has: \n" + dealer.getCards().get(0).toString());
               threads.get(i).getOutput().flush();
               threads.get(i).getOutput().writeObject("Your cards are: " + threads.get(i).getCards().get(0) + " " + threads.get(i).getCards().get(1));
               threads.get(i).getOutput().flush();

               for(int j = 0; j < count; j++)
               {
                   if(i != j)
                   {
                       threads.get(i).getOutput().writeObject(threads.get(j).toString());
                       threads.get(i).getOutput().flush();
                   }
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }


    private void split_two_cards(){
        int i;
        Card card;
        System.out.println("Split the cards:");
        for(i = 0; i < count; i++)
        {   card = deck.drawCard();
            threads.get(i).addCard(card);
            card = deck.drawCard();
            threads.get(i).addCard(card);
        }
        card = deck.drawCard();
        dealer.addCard(card);
        card = deck.drawCard();
        dealer.addCard(card);


    }




    public void start() throws IOException {

        while (running) {
            try {
                System.out.println("Waiting...");
                clientSocket = serverSocket.accept();


                threads.add(new PlayerThread(clientSocket,this,count,"Player " + (count + 1)));
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
        showCards();
        gameStarted = true;


        while(turn < count)
        {
            if(threads.get(turn).isFinished())
            {
                getResults();
                turn++;

            }
        }

        getCardsForDealer();
        gameOver = true;

    }

    private void getResults()
    {
        for( int i = 0 ; i < count;i++)
            if(i != turn && !threads.get(i).isBusted())
                try {
                    threads.get(i).getOutput().writeObject(threads.get(turn).toString());
                    threads.get(i).getOutput().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        Card card;
        while(dealer.getTotal() < getMinim())
        {
            card = deck.drawCard();
            dealer.addCard(card);
        }
    }
    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}