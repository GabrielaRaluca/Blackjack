package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class PlayerThread extends Player implements Runnable{

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private Server server;

    private String message;

    private volatile boolean finished;
    private volatile boolean busted;

    private int turn;

    public PlayerThread(Socket socket, Server server, int turn,String name)
    {
        super(name);
        this.socket = socket;
        this.server = server;
        this.turn = turn;
        this.finished = false;
        this.busted = false;
    }

    @Override
    public void run()
    {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            message = "";

            while(true)
            {
                if(server.getTurn() == this.turn && server.isGameStarted() && !finished)
                {
                	this.output.writeObject("Your total is " + this.getTotal());
                    this.output.writeObject("It is your turn! HIT or STAND?");
                    this.output.flush();
                    
                    message = (String)this.input.readObject();
                    System.out.println(message);

                    if(message.equals("STAND"))
                    {
                        this.output.writeObject("Please wait for the results...");
                        this.output.flush();
                        this.finished = true;
                    }
                    if(message.equals("HIT"))
                    {
                        hitCardAndCheckBust();
                    }
                }

                if(server.isGameOver())
                {
                    sendResults();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch(ClassNotFoundException cnfe)
        {
            cnfe.printStackTrace();
        }
        close();
    }

    private void sendResults()
    {
        if(!this.busted)
        {
            int dealersTotal = this.server.getDealer().getTotal();
            int clientsTotal = this.getTotal();
            try {
                this.output.writeObject("Dealer has " + dealersTotal + " points!");
                this.output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(dealersTotal > 21)
            {
            	try {
                    this.output.writeObject("WIN!");
                    this.output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(clientsTotal <= 21)
            {
                if(clientsTotal > dealersTotal && dealersTotal <= 21)
                {
                    try {
                        this.output.writeObject("WIN!");
                        this.output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(clientsTotal == dealersTotal)
                {
                    try {
                        this.output.writeObject("TIE!");
                        this.output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(dealersTotal <= 21)
                {
                    try {
                        this.output.writeObject("LOSE!");
                        this.output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                try {
                    this.output.writeObject("LOSE!");
                    this.output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void hitCardAndCheckBust()
    {
        synchronized(this)
        {
            Card card = this.server.getDeck().drawCard();
            this.addCard(card);
            try {
                this.output.writeObject(card.toString());
                this.output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(this.getTotal() > 21)
            {
                this.busted = true;
                this.finished = true;
                try {
                    this.output.writeObject("BUST!");
                    this.output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    this.output.writeObject("Your total is now " + this.getTotal());
                    this.output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void close()
    {
        try{
            input.close();
            output.close();
            socket.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public boolean isBusted() { return busted; }

    public boolean isFinished()
    {
        return finished;
    }

    public ObjectInputStream getInput()
    {
        return input;
    }

    public ObjectOutputStream getOutput()
    {
        return output;
    }
}
