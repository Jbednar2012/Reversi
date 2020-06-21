package Reversi.Multiplayer.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements ClientWaiter, MessageWaiter {
	
	private ArrayList<Handler> clients = new ArrayList<>();
	private ServerSocket server;
	private ConnectorThread connector;
	String colors[];
	int i=0;
	int turn=1;
	int closer = 0;
	
	public Server(int port) {
		try {
			server = new ServerSocket(port);
			colors = new String[2];
			colors[0] = "BLACK";
			colors[1] = "WHITE";
		} catch (IOException e) {
			System.exit(-1);
		}
		connector = new ConnectorThread(this, server);
		connector.start();	
		
	}

	public void addClient(Socket client) {
		if(i<2){
			Handler newHandler = new Handler(client, this);
			newHandler.start();
			clients.add(newHandler);
			System.out.println("Dodano");
			write(i,colors[i]);
			System.out.println("Przyznano kolor");
			i++;
		}
		else
			clients.get(i).getWriter().println("Limit graczy przekroczony");
	}

	@Override
	public void getMessage(String message) {
		if(message=="enough"){
			closer++;
			if(closer>=2)
				System.exit(0);
		}
	}
	
	public void write(int clientId, String message) {
		clients.get(clientId).getWriter().println(message);
	}
}
