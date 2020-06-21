package Reversi.Multiplayer;

import Reversi.Enums.*;
import Reversi.Multiplayer.Server.Server;

public class ReversiServer{
	
	public static void main(String[] args){
		Server rs = new Server(65500);
	}
}
