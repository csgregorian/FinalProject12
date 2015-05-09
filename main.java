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
import java.util.ArrayList;
import java.util.Scanner;


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

	
        game = new GamePanel(sizex, sizey);
        add(game);

        int fps = 60;
        myTimer = new Timer(16, this);
        myTimer.start();

        setResizable(false);
        setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) {
		if (game != null) {
			game.playerMove();

			game.arrowMove();

			game.repaint();
			// THIS LINE IS MAGICAL DO NOT TOUCH
			Toolkit.getDefaultToolkit().sync();
		}
	}

	public static void main(String[] args) {
		main frame = new main();
	}
}

class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	// Window size
	public int sizex, sizey;

	// Mouse location
	public int mx, my;
	public boolean click;

	// Keys pressed
	public boolean keys[] = new boolean[256];

	// Texture manager
	public TextureManager textures;

	// Players
	public Player player1, player2;

	// Maps
	public HashMap<String, Map> maps;
	ArrayList<String> map_names;
	Map map;

	// Arrows
	public ArrayList<Arrow> arrows;
	public int arrowspeed;

	public GamePanel(int x, int y) {
		sizex = x; sizey = y;

		mx = my = 0;
		click = false;

		for (int i = 0; i < 256; i++)
			keys[i] = false;

		textures = new TextureManager();

		player1 = new Player(640, 320);
		player2 = new Player(700, 320);

		maps = new HashMap<String, Map>();
		map_names = new ArrayList<String>();
		loadMaps();
		map = maps.get("Stage1");

		arrows = new ArrayList<Arrow>();
		arrowspeed = 8;

		// Event listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		setFocusable(true);
	}

	public void loadMaps() {
		Scanner map_list;
		

		// Gets the map names from file

		try {
			map_list = new Scanner(new File("res/maps/map_list.txt"));

			while (map_list.hasNext()) {
				map_names.add(map_list.next());
			}

			map_list.close();

		} catch (FileNotFoundException e) {
			System.err.println("res/maps/map_list.txt not found");
			System.exit(0);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(0);
		}

		// Create the new map objects

		for (String map_name : map_names) {
			String filename = String.format("maps/%s.png", map_name);
			textures.addTexture(map_name, filename);

			Map map = new Map(map_name);
			maps.put(map_name, map);
		}
		
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
		int code = e.getKeyCode();

		keys[code] = true;

		if (code == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

		if (code == KeyEvent.VK_X) {
			if (player1.jumps > 0) {
				player1.jump();
			}
		} else
		if (code == KeyEvent.VK_S) {
			if (player2.jumps > 0) {
				player2.jump();
			}
		} else

		if (code == KeyEvent.VK_C) {
			Arrow a = null;
			int arrowspeed = 16;
			if (keys[KeyEvent.VK_DOWN] & !keys[KeyEvent.VK_UP]) {
				a = new Arrow(player1.x, player1.y, Arrow.DOWN, arrowspeed);
			} else
			if (keys[KeyEvent.VK_UP] & !keys[KeyEvent.VK_DOWN]) {
				a = new Arrow(player1.x, player1.y, Arrow.UP, arrowspeed);
			} else
			if (keys[KeyEvent.VK_LEFT] & !keys[KeyEvent.VK_RIGHT]) {
				a = new Arrow(player1.x, player1.y, Arrow.LEFT, arrowspeed);
			} else
			if (keys[KeyEvent.VK_RIGHT] & !keys[KeyEvent.VK_LEFT]) {
				a = new Arrow(player1.x, player1.y, Arrow.RIGHT, arrowspeed);
			}


			if (a != null) {
				arrows.add(a);
				System.out.println(arrows.size());
			}
		} else

		if (code == KeyEvent.VK_D) {
			Arrow a = null;

			if (keys[KeyEvent.VK_K] & !keys[KeyEvent.VK_I]) {
				a = new Arrow(player2.x, player2.y, Arrow.DOWN, arrowspeed);
			} else
			if (keys[KeyEvent.VK_I] & !keys[KeyEvent.VK_K]) {
				a = new Arrow(player2.x, player2.y, Arrow.UP, arrowspeed);
			} else
			if (keys[KeyEvent.VK_J] & !keys[KeyEvent.VK_L]) {
				a = new Arrow(player2.x, player2.y, Arrow.LEFT, arrowspeed);
			} else
			if (keys[KeyEvent.VK_L] & !keys[KeyEvent.VK_J]) {
				a = new Arrow(player2.x, player2.y, Arrow.RIGHT, arrowspeed);
			}


			if (a != null) {
				arrows.add(a);
			}
		}

	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public void paintComponent(Graphics g) {
		paintBackground(g);
		paintPlayers(g);
		paintArrows(g);
	}

	public void playerMove() {
		// Player 1 Movement
		if (keys[KeyEvent.VK_RIGHT] == keys[KeyEvent.VK_LEFT]) {
			// XNOR: both or neither are pressed
			player1.drag();
		} else if (keys[KeyEvent.VK_RIGHT]) {
			player1.accelerate(true);
		} else if (keys[KeyEvent.VK_LEFT]) {
			player1.accelerate(false);
		}

		player1.move(map);
		player1.fall();

		if (keys[KeyEvent.VK_L] == keys[KeyEvent.VK_J]) {
			// XNOR: both or neither are pressed
			player2.drag();
		} else if (keys[KeyEvent.VK_L]) {
			player2.accelerate(true);
		} else if (keys[KeyEvent.VK_J]) {
			player2.accelerate(false);
		}

		player2.move(map);
		player2.fall();
	}

	public void arrowMove() {
		for (Arrow a : arrows) {
			a.fall();
			a.move(map);
		}

		arrows.removeIf(a -> !a.alive);
	}

	public void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, sizex, sizey);
		g.drawImage(textures.getTexture(map.name), 0, 0, this);

		g.setColor(Color.black);
		for (Block b : map.blocks) {
			fillRect(g, b);
		}
	}

	public void paintPlayers(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(player1.x, player1.y, 32, 32);

		g.setColor(Color.blue);
		g.fillRect(player2.x, player2.y, 32, 32);
	}

	public void paintArrows(Graphics g) {
		g.setColor(Color.green);
		for (Arrow a : arrows) {
			fillRect(g, a);
		}
	}

	public void fillRect(Graphics g, Rectangle rect) {
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
}





