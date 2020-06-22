package Reversi.Multiplayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Reversi.Multiplayer.ServerPlayer;
import Reversi.Multiplayer.Server.MessageWaiter;
import Reversi.Enums.ReversiColor;

public class MultiGameOver extends JPanel implements ActionListener {
	private static final long serialVersionUID = 2L;
	private JButton againButton;
	private JButton quitButton;
	static String outcome;
	ServerPlayer player;
	JFrame frame;

	public MultiGameOver(JFrame frame, String outcome, ServerPlayer player) {
		this.frame = frame;
		this.outcome = outcome;
		this.player = player;
		againButton = new JButton("Jeszcze raz");
		quitButton = new JButton("Zakoñcz");
		againButton.addActionListener(this);
		quitButton.addActionListener(this);
		setPreferredSize(new Dimension(240, 260));
		add(againButton);
		add(quitButton);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.GREEN);
		Font scoreFont = g.getFont().deriveFont(30.0f);
		int pos = 22;
		if (outcome == "Remis!")
			pos = 40;
		if (outcome == "Wygra³eœ!")
			pos = 20;
		if (outcome == "Przegra³eœ!")
			pos = 10;
		g.setFont(scoreFont);
		g2d.drawString(outcome, pos, 90);
	}

	void initFrame(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10, 10, 246, 289);
		frame.setResizable(false);
	}

	void playAgain() {
		if(player.myHandler.running()){
			player.clearField();
			if(player.myColor == ReversiColor.BLACK){
				player.myColor = ReversiColor.WHITE;
				player.infoOGrze = "Teraz grasz BIA£YMI";
				player.myTurn = false;
			}
			else if(player.myColor == ReversiColor.WHITE){
				player.myColor = ReversiColor.BLACK;
				player.infoOGrze = "Teraz grasz CZARNYMI";
				player.myTurn = true;
			}
			player.frame.setVisible(true);
		}
		frame.setVisible(false);
		frame.dispose();
	}

	void quit() {
		player.myHandler.getWriter().println("enough");
		frame.setVisible(false);
		player.frame.dispose();
		frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == againButton) {
			playAgain();
		}
		if (source == quitButton) {
			quit();
		}

	}

}