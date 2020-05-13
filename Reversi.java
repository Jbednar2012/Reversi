import java.util.Scanner;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

enum Stan {EMPTY, BLACK, WHITE}
enum Winner {NOT_YET, DRAW, BLACK, WHITE}

class Field extends JPanel implements MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Stan turn, opponent;
	public String infoOGrze;
	Winner win;
	JFrame frame;
	int immobilityBlocker;
	Scanner wspolrzedne = new Scanner(System.in);
	Stan[][] plansza = new Stan[8][8];
	private BufferedImage blackImage, whiteImage, emptyImage, thisImage;
	
	public Field(JFrame frame){
		super();
		this.frame = frame;
		File black = new File("black.png");
		File white = new File("white.png");
		File empty = new File("empty.png");
		try {
				blackImage = ImageIO.read(black);
				whiteImage = ImageIO.read(white);
				emptyImage = ImageIO.read(empty);
		} catch (IOException e) {
			System.err.println("Blad odczytu obrazka");
			e.printStackTrace();
		}

		Dimension dimension = new Dimension(240, 260);
		setPreferredSize(dimension);
		for(int j=0;j<8;j++)
			for(int i=0;i<8;i++){
				if(i==3&&j==3||i==4&&j==4)
					plansza[i][j] = Stan.BLACK;
				else if(i==3&&j==4||i==4&&j==3)
					plansza[i][j] = Stan.WHITE;
				else plansza[i][j] = Stan.EMPTY;
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
				if(plansza[i][j]==Stan.BLACK)
					thisImage = blackImage;
				else if (plansza[i][j]==Stan.WHITE)
					thisImage = whiteImage;
				else thisImage = emptyImage;
				g2d.drawImage(thisImage, 30*i, 20+30*j, this);
			}
	}
	
	boolean replacePossible(Stan turn, Stan opponent, int x, int y, boolean[] directions)
	{
		if(plansza[x][y]!=Stan.EMPTY) return false;
		int i, j, possibility=0;
		check_right:	for(i=x+1;i<8;i++){
							j=y;
							if(plansza[i][j]==opponent)
								continue check_right;
							else if(plansza[i][j]==turn&&plansza[i-1][j]==opponent){
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
									else if(plansza[i][j]==turn&&plansza[i-1][j-1]==opponent){
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
						else if(plansza[i][j]==turn&&plansza[i][j-1]==opponent){
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
									else if(plansza[i][j]==turn&&plansza[i+1][j-1]==opponent){
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
						else if(plansza[i][j]==turn&&plansza[i+1][j]==opponent){
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
							else if(plansza[i][j]==turn&&plansza[i+1][j+1]==opponent){
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
						else if(plansza[i][j]==turn&&plansza[i][j+1]==opponent){
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
							else if(plansza[i][j]==turn&&plansza[i-1][j+1]==opponent){
								possibility+=1;
								directions[7] = true;
								break check_right_up;
							}
							else break check_right_up;
		}
		if(possibility>0) return true;
		else return false;
	}
	
	void replace(Stan turn, Stan opponent, int x, int y, boolean[] directions)
	{
		int i, j;
		if(directions[0]){
			i=x+1;
			j=y;
			do{
				plansza[i][j]=turn;
				i++;
			}while(plansza[i][j]==opponent);
		}
		if(directions[1]){
			i=x+1;
			j=y+1;
			do{
				plansza[i][j]=turn;
				i++; j++;
			}while(plansza[i][j]==opponent);
		}
		if(directions[2]){
			i=x;
			j=y+1;
			do{
				plansza[i][j]=turn;
				j++;
			}while(plansza[i][j]==opponent);
		}
		if(directions[3]){
			i=x-1;
			j=y+1;
			do{
				plansza[i][j]=turn;
				i--; j++;
			}while(plansza[i][j]==opponent);
		}
		if(directions[4]){
			i=x-1;
			j=y;
			do{
				plansza[i][j]=turn;
				i--;
			}while(plansza[i][j]==opponent);
		}
		if(directions[5]){
			i=x-1;
			j=y-1;
			do{
				plansza[i][j]=turn;
				i--; j--;
			}while(plansza[i][j]==opponent);
		}
		if(directions[6]){
			i=x;
			j=y-1;
			do{
				plansza[i][j]=turn;
				j--;;
			}while(plansza[i][j]==opponent);
		}
		if(directions[7])
		{
			i=x+1;
			j=y-1;
			do{
				plansza[i][j]=turn;
				i++; j--;
			}while(plansza[i][j]==opponent);
		}
	}
	
	boolean canIMove(Stan turn, Stan opponent)
	{
		boolean[] checker = new boolean[8];
		int possibilities = 0;
		for(int i=0;i<8;i++)
			for(int j=0; j<8; j++)
				if(replacePossible(turn,opponent,i,j,checker))
					possibilities++;
		if(possibilities>0) return true;
		else return false;
	}
	
	boolean immobility(){
		if(immobilityBlocker == 2)
			return true;
		else return false;
	}
	
	boolean fullyFilled()
	{
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				if(plansza[i][j]==Stan.EMPTY) return false;
		}
		return true;
	}
	
	Winner whoIsEliminated()
	{
		int black_counter = 0, white_counter = 0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				switch(plansza[i][j]){
					case BLACK: black_counter++; break;
					case WHITE: white_counter++; break;
					case EMPTY: ;
				}
		}
		if(black_counter==0) {
			infoOGrze = "Eliminacja - ";
			return Winner.BLACK;
		}
		if(white_counter==0) {
			infoOGrze = "Eliminacja - ";
			return Winner.WHITE;
		}
		return Winner.NOT_YET;
	}
	
	Winner checkWhoPrevailed(){
		int black_counter = 0, white_counter = 0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++)
				switch(plansza[i][j]){
					case BLACK: black_counter++; break;
					case WHITE: white_counter++;
					case EMPTY: ;
				}
		}
		infoOGrze = "C: "+black_counter+"  B: "+white_counter+" - ";
		if(black_counter>white_counter) return Winner.BLACK;
		else if(black_counter<white_counter) return Winner.WHITE;
		else return Winner.DRAW;
	}

	boolean Settled()
	{
		if(fullyFilled()||immobilityBlocker>1){
			win = checkWhoPrevailed();
			switch(win){
				case BLACK: infoOGrze += "CZARNY wygra³!"; break;
				case WHITE: infoOGrze += "BIA£Y wygra³!"; break;
				case DRAW: infoOGrze += "Mamy remis!"; break;
				case NOT_YET: return false;
			}
		}
		else{
			win = whoIsEliminated();
			switch(win){
				case BLACK: infoOGrze = "Bia³y wygra³!"; break;
				case WHITE: infoOGrze = "CZARNY wygra³!"; break;
				case DRAW: infoOGrze = "Niemo¿liwe!"; break;
				case NOT_YET: return false;
			}
		}
		System.out.println(infoOGrze);
		wspolrzedne.close();
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

	@Override
	public void mousePressed(MouseEvent e) {
		String who = "X";
		if(turn==Stan.BLACK) {
			opponent = Stan.WHITE;
			who = "CZARNY";
		}
		else if(turn==Stan.WHITE) {
			opponent = Stan.BLACK;
			who = "BIA£Y";
		}
		boolean movePossible = false;
		if(canIMove(turn,opponent)){
			int x, y;
			immobilityBlocker = 0;
			x = (int) e.getX();
			y = (int) e.getY();
			if(y>=20 && x<240 && y<260) {
				x = Math.floorDiv(x, 30);
				y = Math.floorDiv(y-20, 30);
				if(plansza[x][y]==Stan.EMPTY){
					boolean[] directions = new boolean[8];
					if(replacePossible(turn,opponent,x,y,directions)){
						movePossible = true;
						plansza[x][y] = turn;
						replace(turn,opponent,x,y,directions);
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
			immobilityBlocker++;
		}
		repaint();
		if(movePossible) {
			Stan tmp = turn;
			turn = opponent;
			opponent = tmp;
			if(turn == Stan.BLACK)
				infoOGrze = "CZARNY";
			else if(turn == Stan.WHITE)
				infoOGrze = "BIA£Y";
			if(Settled()) System.gc();
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}

class ServerGame
{
	
	void serverPlay(Field field, JFrame frame)
	{
	    frame.setVisible(true);
		System.out.println("Coming soon");
		System.gc();
	}
}

public class Reversi {
	static ServerGame multiplayer = new ServerGame();
	
	static void initFrame(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(10,10, 246, 289);
	    frame.setResizable(false);
	}
	
	public static void main(String[] args){
		int choice;
		Scanner begin = new Scanner(System.in);
		System.out.println("\nW którym trybie chcesz graæ?\n1 - \"Gor¹ce krzes³o\"\n2 - Gra przez serwer");
		choice = begin.nextInt();
		while(choice<1||choice>2){
			System.out.println("Nie ma takiej opcji\nSpróbuj ponownie");
			choice = begin.nextInt();
		}
		JFrame f = new JFrame("Reversi");
		initFrame(f);
		Field pole = new Field(f);
		if(choice == 1) {
		    f.add(pole);
			pole.turn = Stan.BLACK;
			pole.infoOGrze = "CZARNY";
		    f.setVisible(true);
		}
		else {
		    f.add(pole);
			multiplayer.serverPlay(pole, f);
		}
		begin.close();
	}
}