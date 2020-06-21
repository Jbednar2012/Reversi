package Reversi.Multiplayer.Server;

import java.net.Socket;

public interface ClientWaiter {
	void addClient(Socket client);
}
