package Reversi.Multiplayer;

import Reversi.Menu;
import Reversi.Enums.ReversiColor;
import Reversi.Enums.Winner;
import Reversi.Multiplayer.Server.Handler;
import Reversi.Multiplayer.Server.MessageWaiter;
import Reversi.Singleplayer.Game;

import java.awt.event.*;

import java.io.IOException;

import javax.swing.JFrame;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerPlayer extends Game implements MessageWaiter{
	Socket s;
	Handler myHandler;
	ReversiColor myColor;
	JFrame frame;
	boolean myTurn;
	
	public ServerPlayer(JFrame frame, int port){
		super(frame);
		this.frame = frame;
		try{
			this.s = new Socket("localhost", port);
			this.myHandler = new Handler(s, this);
			myHandler.start();
			this.infoOGrze = "Oczekiwanie na przeciwnika...";
		} catch (IOException ex) {
			Logger.getLogger(ServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	ReversiColor opposite(){
		if(myColor==ReversiColor.BLACK)
			return ReversiColor.WHITE;
		else
			return ReversiColor.BLACK;
	}
	
	void clearField() {
		for(int j=0;j<8;j++)
			for(int i=0;i<8;i++){
				if(i==3&&j==3||i==4&&j==4)
					plansza[i][j] = ReversiColor.BLACK;
				else if(i==3&&j==4||i==4&&j==3)
					plansza[i][j] = ReversiColor.WHITE;
				else plansza[i][j] = ReversiColor.EMPTY;
			}
	}
	
	void returnToMenu() {
		JFrame extFrame = new JFrame();
		extFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		extFrame.setBounds(10, 10, 246, 289);
		extFrame.setResizable(false);
		Menu summary = new Menu(extFrame);
		extFrame.add(summary);
		extFrame.setVisible(true);
		if(frame != null) {
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	@Override
	public void getMessage(String message){
		if(message.equals("BLACK")){
			myColor=ReversiColor.BLACK;
			infoOGrze = "Grasz jako CZARNY";
			myTurn = true;
			
		}
		else if(message.equals("WHITE")){
			myColor=ReversiColor.WHITE;
			infoOGrze = "Grasz jako BIA£Y";
			myTurn = false;
		}
		else if(message.equals("Can't move")) {
			immobilityBlocker++;
			if(immobilityBlocker>1){
				endGame();
			}
			else {
				infoOGrze = "Przeciwnik by³ zablokowany, twój ruch";
				repaint();
				myTurn = true;
			}
		}
		else if(message.equals("Limit graczy przekroczony")){
			endGame();
		}
		else if(message.equals("ready")){
			startGame();
		}
		else if(message.equals("stop")){
			System.out.println("Serwer zakoñczy³ dzia³anie");
			myHandler.stopReading();
			returnToMenu();
		}
		else{
			int i=0;
			while(i<message.length()){
				String fieldReader = ""+message.charAt(i);
				int field = Integer.parseInt(fieldReader);
				int y = i%8;
				double potX = Math.floor(i/8);
				int x = (int)potX;
				switch(field){
					case 0: plansza[x][y]=ReversiColor.EMPTY; break;
					case 1: plansza[x][y]=ReversiColor.BLACK; break;
					case 2: plansza[x][y]=ReversiColor.WHITE; break;
				}
				i++;
			}
			immobilityBlocker = 0;
			if(Settled())
				endGame();
			else {
				infoOGrze = "Twoja kolej";
				repaint();
				myTurn = true;
			}
		}
		
	}
	
	private void startGame() {
		
	}
	
	private void endGame() {
		String outcome = null;
		Winner settled = checkWhoPrevailed();
		if(settled==Winner.BLACK && myColor == ReversiColor.BLACK
			|| settled==Winner.WHITE && myColor == ReversiColor.WHITE)
			outcome = "Wygra³eœ";
		else if(settled==Winner.DRAW)
			outcome = "Remis";
		else outcome = "Przegra³eœ";
		JFrame extFrame = new JFrame();
		extFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		extFrame.setBounds(10, 10, 246, 289);
		extFrame.setResizable(false);
		MultiGameOver summary = new MultiGameOver(extFrame, outcome, this);
		extFrame.add(summary);
		extFrame.setVisible(true);
		frame.setVisible(false);
	}
	
	private void sendField() {
		String numericField = "";
		for(int i=0; i<8; i++)
			for(int j=0; j<8; j++){
				if(plansza[i][j]==ReversiColor.BLACK)
					numericField += "1";
				else if (plansza[i][j]==ReversiColor.WHITE)
					numericField += "2";
				else numericField += "0";
			}
		myHandler.getWriter().println(numericField);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(!myTurn){
			infoOGrze = "Jeszcze nie...";
			repaint();
			return;
		}
		if(canIMove(myColor,opposite())){
			int x, y;
			immobilityBlocker = 0;
			x = (int) e.getX();
			y = (int) e.getY();
			if(y>=20 && x<240 && y<260) {
				x = Math.floorDiv(x, 30);
				y = Math.floorDiv(y-20, 30);
				if(plansza[x][y]==ReversiColor.EMPTY){
					boolean[] directions = new boolean[8];
					if(replacePossible(myColor,opposite(),x,y,directions)){
						plansza[x][y] = myColor;
						replace(myColor,opposite(),x,y,directions);
						infoOGrze = "Czekaj na przeciwnika...";
						myTurn = false;
						sendField();
					}
					else {
						infoOGrze = "Ten ruch nie jest mo¿liwy!";
					}
				}
				else {
					infoOGrze = "Miejsce zajête!";
				}
			}
			else
			{
				infoOGrze = "Klikn¹³eœ poza planszê";
			}
		}
		else
		{
			infoOGrze = "Brak mo¿liwych ruchów!";
			myHandler.getWriter().println("Can't move");
			myTurn = false;
			immobilityBlocker++;
		}
		repaint();
		if(Settled())
			endGame();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}