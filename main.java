/* Final Project: Towerfall Ascension

 * A 2D platformer in which two players duke it out in a variety of
   PvP archery arenas.  Mechanics such as looping walls, powerups, and wallkicking
   add a creative and competitive element to the game.

 * P1 controls: Arrow keys to move, C to shoot, X to jump, Z to change direction.
   P2 controls: IJKL to move, D to shoot, S to jump, A to change direction.
   Two keyboards are recommended for a more comfortable experience.

 * Grade 12

 * Christopher Gregorian

 * main.java
 
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

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;



public class main extends JFrame implements ActionListener, Globals {
	Timer myTimer = new Timer(1000 / FPS, this);

	int sizex = 1280;
	int sizey = 740;

	GamePanel game = new GamePanel(sizex, sizey);

	public main() {
		// Sets title
		super("Towerfall: Ascension");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFXPanel jfx = new JFXPanel();
		MediaPlayer mp = new MediaPlayer(
						 new Media(getClass()
						           .getResource("res/mus/ost.mp3")
						           .toExternalForm()));
		mp.play();

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
				case PLAYERMENU:
				case MAPMENU:
				case PAUSE:
				case SCORE:
					break;

				case GAME:
					game.gameTick();
					break;

				default:
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
