package Reversi;

import Reversi.Singleplayer.*;
import Reversi.Multiplayer.ServerMove;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Reversi {
	
	static void initFrame(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(10,10, 246, 289);
	    frame.setResizable(false);
	}
	
	public static void main(String[] args){
		JFrame f = new JFrame("Reversi");
		initFrame(f);
		Menu menu = new Menu(f);
		f.add(menu);
		f.setVisible(true);
	}
}