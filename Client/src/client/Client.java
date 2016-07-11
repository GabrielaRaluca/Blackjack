package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {

    private String IP;
    private int port;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String message;
	private Scanner scanner;

     public Client(String IP, int port){

	this.IP = IP;
	this.port = port;
	scanner = new Scanner(System.in);

    }

    public void connectToServer() {
        System.out.println("Attempting connection...\n");
        try {
            socket = new Socket(IP, port);
            System.out.println("Connected to server\n");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setUpStreams() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Streams are up\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	

    public void start() {

	connectToServer();
	setUpStreams();
        // TODO Auto-generated method stub
	while( true ){

        try {
            message = (String) input.readObject();
            
            
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	 System.out.println(message);
	if (message.equals("WIN") || message.equals("LOSE") || message.equals("TIE") || message.equals ("BUST"))
		break;
	if (message.equals("IT IS YOUR TURN ! HIT OR STAND?")){

		String line = "";
		line = scanner.nextLine();
		sendMessage(line);
	}

	}
	close();
    }

    public void sendMessage(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void close() {
        try
        {   scanner.close();
            output.close();
            input.close();
            socket.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

  
}


