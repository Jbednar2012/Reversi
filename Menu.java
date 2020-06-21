package Reversi;

import Reversi.Singleplayer.*;
import Reversi.Multiplayer.*;
import Reversi.Enums.Field;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Menu extends JPanel implements ActionListener{
	private static final long serialVersionUID = 2L;
	private JButton singlePButton;
	private JButton multiPButton;
	private JButton exitButton;
	JFrame frame;

	public Menu(JFrame frame){
		this.frame = frame;
		singlePButton = new JButton("Graj");
		multiPButton = new JButton("Graj online");
		exitButton = new JButton("Wyjdü");
		singlePButton.addActionListener(this);
		multiPButton.addActionListener(this);
		exitButton.addActionListener(this);
		setPreferredSize(new Dimension(240,260));
		add(singlePButton);
		add(multiPButton);
		add(exitButton);
 	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.GREEN);
		Font font = g.getFont().deriveFont( 60.0f );
		g.setFont(font);
		g2d.drawString("Reversi", 17, 150);
	}
	
	void initFrame(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(10,10, 246, 289);
	    frame.setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent s) {
		Object source = s.getSource();
		if(source==singlePButton) {
			frame.setVisible(false);
			frame.dispose();
			System.gc();
		}
		if(source==multiPButton) {
			JFrame extFrame = new JFrame();
			initFrame(extFrame);
			ServerPlayer multi = new ServerPlayer(extFrame, 65500);
			extFrame.add(multi);
			frame.setVisible(false);
			extFrame.setVisible(true);
			frame.dispose();
		}
		if(source==exitButton) {
			frame.setVisible(false);
			frame.dispose();
			System.gc();
		}
	}
}