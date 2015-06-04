/* Final Project
 * Grade 12
 * Christopher Gregorian
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


public class main extends JFrame implements ActionListener, Constants {
	int fps = 60;
	Timer myTimer = new Timer(16, this);

	int sizex = 1280;
	int sizey = 640;

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
				case GAME:
					game.playerCalc();
					game.arrowCalc();
					break;

				case MENU:
					break;

				case SCORE:
					break;

				case CREDITS:
					break;
				
			}


			game.repaint();
			// THIS LINE IS MAGICAL DO NOT TOUCH
			Toolkit.getDefaultToolkit().sync();
		}
	}

	public static void main(String[] args) {
		Rectangle r1 = new Rectangle(10, 10, 10, 10);
		Rectangle r2 = new Rectangle(0, 0, 10, 10);
		main frame = new main();
	}
}
