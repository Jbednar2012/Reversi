package Reversi.Multiplayer;

import Reversi.Enums.Field;

import java.io.Serializable;

public class ServerMove implements Serializable{
	private static final long serialVersionUID = 2L;
	int x;
	int y;
	
	ServerMove(Field who, int x, int y){
		if(who==Field.BLACK)
			this.player="BLACK";
		else if(who==Field.WHITE)
			this.player="WHITE";
		this.x=x;
		this.y=y;
	}
	
	@Override
	public String toString() {
		return ""+x+"x"+y+"y";
	}
}