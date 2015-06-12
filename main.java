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

// Imaging libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

// Removes KeyEvent prefix in front of keycodes
import static java.awt.event.KeyEvent.*;

// Standard library
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

// Music-specific imports
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;


public class main extends JFrame implements ActionListener, Globals {
	// Triggers an action 60 times per second
	Timer myTimer = new Timer(1000 / FPS, this);

	// Window size
	int sizex = 1280;
	int sizey = 740;

	// Main graphics object
	GamePanel game = new GamePanel(sizex, sizey);

	public main() {
		// Sets title
		super("Towerfall: Ascension");

		// Exits process on window close
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initializes JFX module
		JFXPanel jfx = new JFXPanel();
		// Plays music
		MediaPlayer mp = new MediaPlayer(
						 new Media(getClass()
						           .getResource("res/mus/ost.mp3")
						           .toExternalForm()));
		mp.play();

		// Sets window size
		// Actual size is 720, 20px are reserved for the title bar
		setSize(sizex, sizey);

		// Adds JPanel to JFrame
        add(game);

        // Starts timer object
        myTimer.start();

        // Shows window
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
					// Extra action only needs to be taken on GAME state
					game.gameTick();
					break;

				default:
					break;
			}


			// Draws graphics
			game.repaint();

			// THIS LINE IS MAGICAL DO NOT TOUCH
			// Fixes all lag problems
			// Found in the depths of SO
			Toolkit.getDefaultToolkit().sync();
		}
	}

	public static void main(String[] args) {
		main frame = new main();
	}
}
