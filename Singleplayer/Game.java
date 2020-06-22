package Reversi.Singleplayer;

import Reversi.Singleplayer.SingleGameOver;
import Reversi.Enums.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel implements MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public ReversiColor turns, opponent;
	public String infoOGrze;
	static int blackCounter;
	static int whiteCounter;
	Winner win;
	JFrame frame;
	public int immobilityBlocker;
	public ReversiColor[][] plansza = new ReversiColor[8][8];
	private BufferedImage blackImage, whiteImage, emptyImage, thisImage;
	
	public Game(JFrame frame){
		super();
		this.frame = frame;
		File black = new File("./Reversi/Images/Black.png");
		File white = new File("./Reversi/Images/White.png");
		File empty = new File("./Reversi/Images/Empty.png");
		try {
				blackImage = ImageIO.read(black);
				whiteImage = ImageIO.read(white);
				emptyImage = ImageIO.read(empty);
		} catch (IOException e) {
			System.err.println("B³¹d odczytu obrazka");
			e.printStackTrace();
		}

		Dimension dimension = new Dimension(240, 260);
		setPreferredSize(dimension);
		for(int j=0;j<8;j++)
			for(int i=0;i<8;i++){
				if(i==3&&j==3||i==4&&j==4)
					plansza[i][j] = ReversiColor.BLACK;
				else if(i==3&&j==4||i==4&&j==3)
					plansza[i][j] = ReversiColor.WHITE;
				else plansza[i][j] = ReversiColor.EMPTY;
			}
		win = Winner.NOT_YET;
		immobilityBlocker = 0;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paint(Graphics g)
	{
		g.clearRect(0,0,240,260);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.GREEN);
		g2d.drawString(infoOGrze,0,19);
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++){
				if(plansza[i][j]==ReversiColor.BLACK)
					thisImage = blackImage;
				else if (plansza[i][j]==ReversiColor.WHITE)
					thisImage = whiteImage;
				else thisImage = emptyImage;
				g2d.drawImage(thisImage, 30*i, 20+30*j, this);
			}
	}
	
	public boolean replacePossible(ReversiColor turns, ReversiColor opponent, int x, int y, boolean[] directions)
	{
		if(plansza[x][y]!=ReversiColor.EMPTY) return false;
		int i, j, possibility=0;
		check_right:	for(i=x+1;i<8;i++){
							j=y;
							if(plansza[i][j]==opponent)
								continue check_right;
							else if(plansza[i][j]==turns&&plansza[i-1][j]==opponent){
								possibility+=1;
								directions[0] = true;
								break check_right;
							}
							else break check_right;
		}
		check_right_down:	for(i=x+1;i<8;i++){
								j=y+(i-x);
								if(j<8)
									if(plansza[i][j]==opponent){
										continue check_right_down;
									}
									else if(plansza[i][j]==turns&&plansza[i-1][j-1]==opponent){
										possibility+=1;
										directions[1] = true;
										break check_right_down;
									}
							else break check_right_down;
		}
		check_down:	for(j=y+1;j<8;j++){
						i=x;
						if(plansza[i][j]==opponent)
							continue check_down;
						else if(plansza[i][j]==turns&&plansza[i][j-1]==opponent){
							possibility+=1;
							directions[2] = true;
							break check_down;
						}
							else break check_down;
		}
		check_left_down:	for(i=x-1;i>=0;i--){
								j=y+(x-i);
								if(j<8)
									if(plansza[i][j]==opponent){
										continue check_left_down;
									}
									else if(plansza[i][j]==turns&&plansza[i+1][j-1]==opponent){
										possibility+=1;
										directions[3] = true;
										break check_left_down;
									}
							else break check_left_down;
		}
		check_left: for(i=x-1;i>=0;i--){
						j=y;
						if(plansza[i][j]==opponent)
							continue check_left;
						else if(plansza[i][j]==turns&&plansza[i+1][j]==opponent){
							possibility+=1;
							directions[4] = true;
							break check_left;
						}
						else break check_left;
		}
		check_left_up: 	for(i=x-1;i>=0;i--){
							j=y-(x-i);
							if(j>=0)
								if(plansza[i][j]==opponent){
									continue check_left_up;
								}
							else if(plansza[i][j]==turns&&plansza[i+1][j+1]==opponent){
								possibility+=1;
								directions[5] = true;
								break check_left_up;
							}
							else break check_left_up;
		}
		check_up: 	for(j=y-1;j>=0;j--){
						i=x;
						if(plansza[i][j]==opponent)
							continue check_up;
						else if(plansza[i][j]==turns&&plansza[i][j+1]==opponent){
							possibility+=1;
							directions[6] = true;
							break check_up;
						}
						else break check_up;
		}
		check_right_up: for(i=x+1;i<8;i++){
							j=y-(i-x);
							if(j>=0)
							if(plansza[i][j]==opponent){
								continue check_right_up;
							}
							else if(plansza[i][j]==turns&&plansza[i-1][j+1]==opponent){
								possibility+=1;
								directions[7] = true;
								break check_right_up;
							}
							else break check_right_up;
		}
		if(possibility>0) return true;
		else return false;
	}
	
	public void replace(ReversiColor turns, ReversiColor opponent, int x, int y, boolean[] directions)
	{
		int i, j;
		if(directions[0]){
			i=x+1;
			j=y;
			do{
				plansza[i][j]=turns;
				i++;
			}while(plansza[i][j]==opponent);
		}
		if(directions[1]){
			i=x+1;
			j=y+1;
			do{
				plansza[i][j]=turns;
				i++; j++;
			}while(plansza[i][j]==opponent);
		}
		if(directions[2]){
			i=x;
			j=y+1;
			do{
				plansza[i][j]=turns;
				j++;
			}while(plansza[i][j]==opponent);
		}
		if(directions[3]){
			i=x-1;
			j=y+1;
			do{
				plansza[i][j]=turns;
				i--; j++;
			}while(plansza[i][j]==opponent);
		}
		if(directions[4]){
			i=x-1;
			j=y;
			do{
				plansza[i][j]=turns;
				i--;
			}while(plansza[i][j]==opponent);
		}
		if(directions[5]){
			i=x-1;
			j=y-1;
			do{
				plansza[i][j]=turns;
				i--; j--;
			}while(plansza[i][j]==opponent);
		}
		if(directions[6]){
			i=x;
			j=y-1;
			do{
				plansza[i][j]=turns;
				j--;;
			}while(plansza[i][j]==opponent);
		}
		if(directions[7])
		{
			i=x+1;
			j=y-1;
			do{
				plansza[i][j]=turns;
				i++; j--;
			}while(plansza[i][j]==opponent);
		}
	}
	
	public boolean canIMove(ReversiColor turns, ReversiColor opponent)
	{
		boolean[] checker = new boolean[8];
		int possibilities = 0;
		for(int i=0;i<8;i++)
			for(int j=0; j<8; j++)
				if(replacePossible(turns,opponent,i,j,checker))
					possibilities++;
		if(possibilities>0) return true;
		else return false;
	}
	
	boolean immobility(){
		if(immobilityBlocker == 2)
			return true;
		else return false;
	}
	
	public boolean fullyFilled()
	{
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				if(plansza[i][j]==ReversiColor.EMPTY) return false;
		}
		return true;
	}
	
	Winner whoIsEliminated()
	{
		blackCounter = 0;
		whiteCounter = 0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				switch(plansza[i][j]){
					case BLACK: blackCounter++; break;
					case WHITE: whiteCounter++; break;
					case EMPTY: ;
				}
		}
		if(blackCounter==0 && whiteCounter==0) return Winner.DRAW;
		if(blackCounter==0) return Winner.BLACK;
		if(whiteCounter==0) return Winner.WHITE;
		return Winner.NOT_YET;
	}
	
	public Winner checkWhoPrevailed(){
		blackCounter = 0;
		whiteCounter = 0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				switch(plansza[i][j]){
					case BLACK: blackCounter++; break;
					case WHITE: whiteCounter++;
					case EMPTY: ;
				}
		}
		if(blackCounter>whiteCounter) return Winner.BLACK;
		else if(blackCounter<whiteCounter) return Winner.WHITE;
		else return Winner.DRAW;
	}

	public boolean Settled()
	{
		if(fullyFilled()||immobilityBlocker>1){
			win = checkWhoPrevailed();
			switch(win){
				case BLACK: SingleGameOver.score="CZARNY wygra³!";
							SingleGameOver.blackWins++;
							break;
				case WHITE: SingleGameOver.score="BIA£Y wygra³!";
							SingleGameOver.whiteWins++;
							break;
				case DRAW: 	SingleGameOver.score="Mamy remis!";
							SingleGameOver.draws++;
							break;
				case NOT_YET: return false;
			}
		}
		else{
			win = whoIsEliminated();
			switch(win){
				case BLACK: SingleGameOver.score="BIA£Y wygra³!";
							SingleGameOver.whiteWins++;
							break;
				case WHITE: SingleGameOver.score="CZARNY wygra³!";
							SingleGameOver.blackWins++;
							break;
				case DRAW: 	SingleGameOver.score="Plansza pusta!";
							break;
				case NOT_YET: return false;
			}
		}
		return true;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	void endTurn() {
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++) {
				if(plansza[i][j]==ReversiColor.BLACK)
					blackCounter++;
				if(plansza[i][j]==ReversiColor.WHITE)
					whiteCounter++;
			}
		ReversiColor tmp = turns;
		turns = opponent;
		opponent = tmp;
		if(turns == ReversiColor.BLACK)
			infoOGrze = "CZARNY";
		else if(turns == ReversiColor.WHITE)
			infoOGrze = "BIA£Y";
		if(Settled()) {
			JFrame extFrame = new JFrame();
			extFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			extFrame.setBounds(10,10, 246, 289);
			extFrame.setResizable(false);
			SingleGameOver summary = new SingleGameOver(extFrame);
			extFrame.add(summary);
			extFrame.setVisible(true);
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		String who = "X";
		if(turns==ReversiColor.BLACK) {
			opponent = ReversiColor.WHITE;
			who = "CZARNY";
		}
		else if(turns==ReversiColor.WHITE) {
			opponent = ReversiColor.BLACK;
			who = "BIA£Y";
		}
		boolean movePossible = false, blocked = false;
		if(canIMove(turns,opponent)){
			int x, y;
			immobilityBlocker = 0;
			x = (int) e.getX();
			y = (int) e.getY();
			if(y>=20 && x<240 && y<260) {
				x = Math.floorDiv(x, 30);
				y = Math.floorDiv(y-20, 30);
				if(plansza[x][y]==ReversiColor.EMPTY){
					boolean[] directions = new boolean[8];
					if(replacePossible(turns,opponent,x,y,directions)){
						movePossible = true;
						plansza[x][y] = turns;
						replace(turns,opponent,x,y,directions);
					}
					else {
						infoOGrze = who + " - Ten ruch nie jest mo¿liwy!";
						repaint();
					}
				}
				else {
					infoOGrze = who + " - Miejsce zajête!";
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
			infoOGrze = who + " - Brak mo¿liwych ruchów!";
			blocked = true;
		}
		repaint();
		if(movePossible) {
			endTurn();
		}
		if(blocked) {
			immobilityBlocker++;
			endTurn();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}