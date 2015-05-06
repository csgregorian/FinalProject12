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
	private long now=0,cnt=0,st=0;

	public main() {
		// Sets title
		super("Towerfall: Ascension");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int sizex = 1280;
		int sizey = 640;
		setSize(sizex, sizey);

		

        game = new GamePanel(sizex, sizey);
        add(game);

        int fps = 60;
        myTimer = new Timer(1000/60, this);
        myTimer.start();
        st = System.currentTimeMillis();
        setResizable(false);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		if (game != null){
			cnt += 1;
			game.playerMove();
			game.repaintz();
			//System.out.println(cnt *(1000/60)+","+(System.currentTimeMillis()-st));
		}
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
	// private boolean keys[] = new boolean[256];

	// Texture manager
	private TextureManager textures;

	// Players
	private Player player1, player2;

	// Testing
	private int counter = 0;

	public GamePanel(int x, int y) {
		sizex = x; sizey = y;

		mx = my = 0;
		click = false;

		// for (int i = 0; i < 256; i++)
		// 	keys[i] = false;

		textures = new TextureManager();

		player1 = new Player(640, 320);
		player2 = new Player(700, 320);

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
		// click = true;
		// mx = e.getX();
		// my = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		// click = false;
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mouseDragged(MouseEvent e) {
		// mx = e.getX();
		// my = e.getY();
	}

	public void mouseMoved(MouseEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		// keys[e.getKeyCode()] = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player1.accelerate(1);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player1.accelerate(-1);
		}
	}

	public void keyReleased(KeyEvent e) {
		// keys[e.getKeyCode()] = false;
	}

	public void paintComponent(Graphics g) {
		// paintBackground(g);

		// g.setColor(Color.red);
		paintPlayers(g);
		System.out.println(++counter);
	}
	public void repaintz() {
		Graphics g = getGraphics();
		if(g!=null){
			paintBackground(g);

		// g.setColor(Color.red);
			paintPlayers(g);
		}
		else{
			System.out.println(++counter);	
		}

		counter++;
		//System.out.println(++counter);
	}

	public void playerMove() {
		// Player 1 Movement
		// if (keys[KeyEvent.VK_RIGHT]) {
		// 	player1.accelerate(1);
		// }
		// if (keys[KeyEvent.VK_LEFT]) {
		// 	player1.accelerate(-1);
		// }

		player1.move();
	}

	public void paintBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, sizex, sizey);
	}

	public void paintPlayers(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(player1.x, player1.y, 32, 32);

		g.setColor(Color.blue);
		g.fillRect(player2.x, player2.y, 32, 32);
	}
}





