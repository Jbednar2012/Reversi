package Reversi.Multiplayer.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectorThread extends Thread {
	private ClientWaiter observer;
	private ServerSocket server;
	private boolean isReading = true;
	public ConnectorThread(ClientWaiter observer, ServerSocket server) {
		super();
		this.observer = observer;
		this.server = server;
	}
	@Override
	public void run() {
		while(isReading) {
			try {
				Socket client = server.accept();
				observer.addClient(client);
			} catch (IOException e) {
				stopReading();
			}
		}
	}
	public void stopReading() {
		this.isReading = false;
	}
}
