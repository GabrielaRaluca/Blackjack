package server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private ExecutorService threadPool;

    private ArrayList<PlayerThread> threads;

    Deck deck;
    Player dealer;

    private static int count;
    private volatile int turn;
    private final int portNumber = 9999;
    private final int interval = 10000;

    private volatile boolean gameStarted;
    private volatile boolean gameOver;
    private volatile boolean  running;


    Date timeToRun;
    Timer timer;

    public Server()
    {
        threadPool = Executors.newCachedThreadPool();
        threads = new ArrayList<PlayerThread>();
        deck = new Deck();
        dealer = new Player();
        running = true;
        turn = 0; //initialize with 0 so that first player starts the game
        count = 0; //the number of players connected to the server
        gameStarted = false;
        gameOver = false;
        try
        {
            serverSocket = new ServerSocket(portNumber);
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (running) {
            try {
                System.out.println("Waiting for a player to connect...");
                clientSocket = serverSocket.accept();

                threads.add(new PlayerThread(clientSocket, this, count));
                threadPool.execute(threads.get(threads.size() - 1));

                count++;
                System.out.println("Player " + count + " has connected");

                if (threads.size() == 1) {
                    timeToRun = new Date(System.currentTimeMillis() + interval);
                    timer = new Timer();

                    timer.schedule(new TimerTask() {
                        public void run() {
                            running = false;
                            try {
                                serverSocket.close();
                            } catch (IOException e) {
                                System.out.println("Done waiting! No more clients can connect to the server");
                            }
                            timer.cancel();
                        }
                    }, timeToRun);
                }

            } catch (IOException e) {
                System.out.println("Done waiting! No more clients can connect to the server");
            }
        }

        hitFirstTwoCards();

        gameStarted = true;

        while (turn < count)
        {
            if (threads.get(turn).isFinished())
            {
                turn++;
            }
        }

        getCardsForDealer();

        gameOver = true;
    }
    private void getCardsForDealer()
    {
        Card card;
        while(this.dealer.getTotal() < 16)
        {
            card = this.deck.drawCard();
            this.dealer.addCard(card);
        }
    }

    public void hitFirstTwoCards() throws IOException
    {
        for(int i = 0; i < threads.size(); i++)
        {
            {
                Card card = deck.drawCard();
                threads.get(i).addCard(card);

                card = deck.drawCard();
                threads.get(i).addCard(card);

            }
        }

        Card card = deck.drawCard();
        dealer.addCard(card);
        card = deck.drawCard();
        dealer.addCard(card);

        for(int i = 0; i < threads.size(); i++)
        {
            threads.get(i).getOutput().writeObject("Dealer has " + dealer.getCards().get(0).toString());
            threads.get(i).getOutput().flush();

            threads.get(i).getOutput().writeObject(threads.get(i).getCards().get(0).toString());
            threads.get(i).getOutput().flush();

            threads.get(i).getOutput().writeObject(threads.get(i).getCards().get(1).toString());
            threads.get(i).getOutput().flush();

            threads.get(i).getOutput().writeObject("Your total is " + threads.get(i).getTotal());
            threads.get(i).getOutput().flush();
        }
    }
    public void close()
    {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ExecutorService getThreadPool()
    {
        return threadPool;
    }

    public ArrayList<PlayerThread> getThreads()
    {
        return this.threads;
    }

    public int getTurn()
    {
        return turn;
    }

    public boolean isGameStarted() { return gameStarted; }

    public boolean isGameOver() { return gameOver; }

    public Deck getDeck()
    {
        return deck;
    }

    public Player getDealer()
    {
        return dealer;
    }
}
