package Reversi.Singleplayer;

import Reversi.Enums.ReversiColor;
import Reversi.Menu;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SingleGameOver extends JPanel implements ActionListener{
	private static final long serialVersionUID = 2L;
	private JButton againButton;
	private JButton quitButton;
	static int blackWins;
	static int whiteWins;
	static int draws;
	static String score;
	JFrame frame;

	public SingleGameOver(JFrame frame){
		this.frame = frame;
		againButton = new JButton("Jeszcze raz");
		quitButton = new JButton("Zakoñcz");
		againButton.addActionListener(this);
		quitButton.addActionListener(this);
		setPreferredSize(new Dimension(240,260));
		add(againButton);
 		add(quitButton);
	}
	
	@Override
 	public void paint(Graphics g) {
 		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.GREEN);
		Font scoreFont = g.getFont().deriveFont(30.0f);
		Font summaryFont = g.getFont().deriveFont(15.0f);
		int pos = 22;
		if(score=="Mamy remis!")
			pos = 35;
		if(score=="BIA£Y wygra³!")
			pos = 30;
		if(score=="CZARNY wygra³!")
			pos = 8;
	    g.setFont(scoreFont);
		g2d.drawString(score, pos, 90);
		g.setFont(summaryFont);
		String blackWinsDisplay = "Czarny:\t"+blackWins;
		String whiteWinsDisplay = "Bia³y:\t"+whiteWins;
		String drawsDisplay = "Remis:\t"+draws;
		String blackPieces = "Cz: "+Game.blackCounter;
		String whitePieces = "B: "+Game.whiteCounter;
		g2d.drawString(blackWinsDisplay, 50, 120);
		g2d.drawString(whiteWinsDisplay, 50, 140);
		g2d.drawString(drawsDisplay, 50, 160);
		g2d.drawString(blackPieces, 50, 50);
		g2d.drawString(whitePieces, 150, 50);
 	}
 	
	void initFrame(JFrame frame) {
 		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10,10, 246, 289);
	    frame.setResizable(false);
	}
	
	void playAgain() {
		JFrame extFrame = new JFrame();
		initFrame(extFrame);
		Game again = new Game(extFrame);
		again.turns = ReversiColor.BLACK;
		again.infoOGrze = "CZARNY";
		extFrame.add(again);
		extFrame.setVisible(true);
		frame.setVisible(false);
		frame.dispose();
	}
	
	void quit() {
		blackWins = 0; whiteWins = 0; draws = 0;
		JFrame extFrame = new JFrame();
		initFrame(extFrame);
		Menu quit = new Menu(extFrame);
		extFrame.add(quit);
		extFrame.setVisible(true);
		frame.setVisible(false);
		frame.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source==againButton) {
			playAgain();
		}
		if(source==quitButton) {
			quit();
		}
		
		
	}
	
}