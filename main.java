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

import java.util.HashMap;


public class main extends JFrame implements ActionListener {
	Timer myTimer;
	GamePanel game;

	public main() {
		// Sets title
		super("Towerfall: Ascension");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int sizex = 1280;
		int sizey = 640;
		setSize(sizex, sizey);

		int fps = 100;
        myTimer = new Timer(1000/fps, this);
        myTimer.start();

        game = new GamePanel(sizex, sizey);
        add(game);

        setResizable(false);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		if (game != null)
			game.repaint();
	}

	public static void main(String[] args) {
		main frame = new main();
	}
}

class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	// Window size
	private int sizex, sizey;

	// Mouse location
	private int mx, my;
	private boolean click;

	// Keys pressed
	private boolean keys[] = new boolean[256];

	// Texture manager
	private TextureManager textures;

	// Players
	private Player player1, player2;

	public GamePanel(int x, int y) {
		sizex = x; sizey = y;

		mx = my = 0;
		click = false;

		for (int i = 0; i < 256; i++)
			keys[i] = false;

		textures = new TextureManager();

		// Event listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		setFocusable(true);
	}

	// Placeholder function overrides for event listeners
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		click = true;
		mx = e.getX();
		my = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		click = false;
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	public void mouseMoved(MouseEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, sizex, sizey);

		// Player 1 Movement
		if (keys[KeyEvent.VK_UP]) {
			
		}
	}
}





