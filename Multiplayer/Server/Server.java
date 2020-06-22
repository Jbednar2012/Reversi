package Reversi.Multiplayer.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements ClientWaiter, MessageWaiter {
	
	private ArrayList<Handler> clients = new ArrayList<>();
	private ServerSocket server;
	private ConnectorThread connector;
	private boolean isWorking = true;
	String[] colors;
	boolean[] taken;
	int i=0;
	int turn=0;
	int closer = 0;
	
	public Server(int port) {
		try {
			server = new ServerSocket(port);
			colors = new String[2];
			colors[0] = "BLACK";
			colors[1] = "WHITE";
			taken = new boolean[2];
			taken[0] = false;
			taken[1] = false;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		connector = new ConnectorThread(this, server);
		connector.start();	
		
	}

	public void addClient(Socket client) {
		if(i>2){
			write(i,"Limit graczy przekroczony");
			return;
		}
		Handler newHandler = new Handler(client, this);
		newHandler.start();
		clients.add(newHandler);
		int a;
		for(a=0; a<2; a++){
			if(!taken[a]){
				write(i,colors[a]);
				taken[a]=true;
				break;
			}
		}
		i++;
		if(i >= 2) {
			clients.get(0).getWriter().println("ready");
			clients.get(1).getWriter().println("ready");
		} 
	}

	@Override
	public void getMessage(String message) {
		if(message.equals("enough")){
			for(Handler cl : clients) {
				cl.getWriter().println("stop");
				i--;
			}
			System.exit(0);
		}
		for(Handler cl : clients) {
			if(cl!=clients.get(turn))
				cl.getWriter().println(message);
		}
		turn++; turn%=2;
	}
	
	public void write(int clientId, String message) {
		clients.get(clientId).getWriter().println(message);
	}
}
