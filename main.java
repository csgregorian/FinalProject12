/* Final Project
 * Grade 12
 * Christopher Gregorian
 * main.java
 * Creates a JFrame which handles action handling
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import static java.awt.event.KeyEvent.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;


public class main extends JFrame implements ActionListener, Globals {
	Timer myTimer = new Timer(1000 / FPS, this);

	int sizex = 1280;
	int sizey = 740;

	GamePanel game = new GamePanel(sizex, sizey);

	public main() {
		// Sets title
		super("Towerfall: Ascension");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(sizex, sizey);

        add(game);

        myTimer.start();

        setResizable(false);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		if (game != null) {
			switch (game.state) {
				case INTRO:
					break;
					
				case GAME:
					game.gameTick();
					break;

				case PLAYERMENU:
					break;

				case MAPMENU:
					break;

				case PAUSE:
					break;
			}


			game.repaint();
			// THIS LINE IS MAGICAL DO NOT TOUCH
			Toolkit.getDefaultToolkit().sync();
		}
	}

	public static void main(String[] args) {
		main frame = new main();
	}
}
