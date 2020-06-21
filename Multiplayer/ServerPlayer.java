package Reversi.Multiplayer;

import Reversi.Menu;
import Reversi.Multiplayer.Server.*;
import Reversi.Enums.*;
import Reversi.Singleplayer.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerPlayer extends Game implements MessageWaiter{
	Socket s;
	OutputStream os;
	InputStream is;
	Handler myHandler;
	Field myColor;
	JFrame frame;
	boolean myTurn;
	
	public ServerPlayer(JFrame frame, int port){
		super(frame);
		try{
			this.s = new Socket("127.0.0.1", port);
			this.myHandler = new Handler(s, this);
			this.infoOGrze = "Oczekiwanie na przeciwnika...";
		} catch (IOException ex) {
			Logger.getLogger(ServerPlayer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	Field opposite(){
		if(myColor==Field.BLACK)
			return Field.WHITE;
		else
			return Field.BLACK;
	}

	@Override
	public void getMessage(String message){
		if(message=="BLACK"){
			myColor=Field.BLACK;
			myTurn = true;
			System.out.println("Jestem CZARNY");
		}
		else if(message=="WHITE"){
			myColor=Field.WHITE;
			myTurn = false;
			System.out.println("Jestem BIA£Y");
		}
		else if(message=="Can't move") {
			immobilityBlocker++;
			if(immobilityBlocker>=2){
				String outcome = null;
				Winner settled = checkWhoPrevailed();
				if(settled==Winner.BLACK && myColor == Field.BLACK||settled==Winner.WHITE && myColor == Field.WHITE)
					outcome = "Wygra³eœ";
				else if(settled==Winner.DRAW)
					outcome = "Remis";
				else outcome = "Przegra³eœ";
				JFrame extFrame = new JFrame();
				extFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				extFrame.setBounds(10,10, 246, 289);
				extFrame.setResizable(false);
				Menu summary = new Menu(extFrame);
				extFrame.add(summary);
				extFrame.setVisible(true);
				frame.setVisible(false);
				frame.dispose();}
			else myTurn = true;
		}
		else if(message=="Limit graczy przekroczony"){
			JFrame extFrame = new JFrame();
			extFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			extFrame.setBounds(10,10, 246, 289);
			extFrame.setResizable(false);
			Menu summary = new Menu(extFrame);
			extFrame.add(summary);
			extFrame.setVisible(true);
			frame.setVisible(false);
			frame.dispose();
		}
		else{
			String newX = message.substring(0,0);
			String newY = message.substring(2,2);
			int x = Integer.parseInt(newX);
			int y = Integer.parseInt(newY);
			plansza[x][y] = opposite();
			myTurn = true;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(myTurn){
			boolean movePossible = false, blocked = false;
			if(canIMove(myColor,opposite())){
				int x, y;
				immobilityBlocker = 0;
				x = (int) e.getX();
				y = (int) e.getY();
				if(y>=20 && x<240 && y<260) {
					x = Math.floorDiv(x, 30);
					y = Math.floorDiv(y-20, 30);
					if(plansza[x][y]==Field.EMPTY){
						boolean[] directions = new boolean[8];
						if(replacePossible(turn,opponent,x,y,directions)){
							plansza[x][y] = turn;
							myHandler.getWriter().println(""+x+"-"+y);
							replace(turn,opponent,x,y,directions);
							infoOGrze = "Czekaj na przeciwnika...";
						}
						else {
							infoOGrze = "Ten ruch nie jest mo¿liwy!";
							repaint();
						}
					}
					else {
						infoOGrze = "Miejsce zajête!";
						repaint();
					}
				}
				else
				{
					infoOGrze = "Klikn¹³eœ poza planszê";
					repaint();
				}
			}
			else
			{
				infoOGrze = "Brak mo¿liwych ruchów!";
				immobilityBlocker++;
				myHandler.getWriter().println("Can't move");
			}
			myTurn = false;
		}
		else infoOGrze = "Jeszcze nie...";
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}