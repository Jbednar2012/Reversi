package Reversi.Multiplayer.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler extends Thread {
	
	private MessageWaiter observer;
	private Socket client;
	private boolean isRunning = true;
	private BufferedReader in;
	private PrintWriter out;
	
	public Handler(Socket client, MessageWaiter observer) {
		this.observer = observer;
		this.client = client;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			stopReading();
		}
		try {
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			stopReading();
		}

	}
	@Override
	public void run() {
		while(isRunning) {
			try {
				String message = in.readLine();
				observer.getMessage(message);
			} catch (IOException e) {
				stopReading();
			}
		}
	}
	public void stopReading() {
		isRunning = false;
		try {
			client.close();
		} catch (IOException e) {
			System.exit(-1);
		}
	}
	
	public PrintWriter getWriter() {
		return out;
	}
	
	public boolean running() {
		return isRunning;
	}
}
