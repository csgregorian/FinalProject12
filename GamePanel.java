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

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, Constants {
	// Window size
	int sizex, sizey;

	// Mouse location
	int mx = 0,
	    my = 0;
	boolean click = false;

	// Keys pressed
	boolean keys[] = new boolean[256];

	// Texture manager
	TextureManager textures = new TextureManager();

	// Players
	Player player1 = new Player(7*32, 5*32),
		   player2 = new Player(34*32, 5*32);

	// Maps
	HashMap<String, Map> maps = new HashMap<>();
	ArrayList<String> map_names = new ArrayList<>();
	Map map;

	// Arrows
	ArrayList<Arrow> arrows = new ArrayList<>();

	// Game State
	int state = GAME;


	public GamePanel(int x, int y) {
		sizex = x; sizey = y;

		for (int i = 0; i < 256; i++)
			keys[i] = false;

		loadMaps();
		map = maps.get("Island");

		// Event listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		setFocusable(true);
	}

	public void reset(String mapname) {
		arrows.clear();
		map = maps.get(mapname);

		Player player1 = new Player(7*32, 5*32),
			   player2 = new Player(34*32, 5*32);

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
			String filename = String.format("res/maps/%s.png", map_name);
			System.out.println(filename);
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

		if (state == GAME) {
			Arrow a = null;

			switch (code) {
				case VK_ESCAPE:
					System.exit(0);
					break;

				case VK_X:
					player1.jump();
					break;
				case VK_S:
					player2.jump();
					break;

				case VK_RIGHT:
					player1.last_input = RIGHT;
					break;
				case VK_LEFT:
					player1.last_input = LEFT;
					break;
				case VK_DOWN:
					player1.last_input = DOWN;
					break;
				case VK_UP:
					player1.last_input = UP;
					break;

				case VK_L:
					player2.last_input = RIGHT;
					break;
				case VK_J:
					player2.last_input = LEFT;
					break;
				case VK_K:
					player2.last_input = DOWN;
					break;
				case VK_I:
					player2.last_input = UP;
					break;

				case VK_C:
					if (keys[VK_DOWN] && !keys[VK_UP]) {
						a = new Arrow(player1.x, player1.y, Arrow.DOWN, player1.arrowspeed);
					} else
					if (keys[VK_UP] && !keys[VK_DOWN]) {
						a = new Arrow(player1.x, player1.y, Arrow.UP, player1.arrowspeed);
					} else
					if (keys[VK_LEFT] && !keys[VK_RIGHT]) {
						a = new Arrow(player1.x, player1.y, Arrow.LEFT, player1.arrowspeed);
					} else
					if (keys[VK_RIGHT] && !keys[VK_LEFT]) {
						a = new Arrow(player1.x, player1.y, Arrow.RIGHT, player1.arrowspeed);
					} else
					if (!(keys[VK_RIGHT] || keys[VK_LEFT] || keys[VK_DOWN] || keys[VK_UP])) {
						a = new Arrow(player1.x, player1.y, player1.last_input, player1.arrowspeed);
					}

					if (a != null) {
						arrows.add(a);
					}
					break;

				case VK_D:
					if (keys[VK_K] && !keys[VK_I]) {
						a = new Arrow(player2.x, player2.y, Arrow.DOWN, player2.arrowspeed);
					} else
					if (keys[VK_I] && !keys[VK_K]) {
						a = new Arrow(player2.x, player2.y, Arrow.UP, player2.arrowspeed);
					} else
					if (keys[VK_J] && !keys[VK_L]) {
						a = new Arrow(player2.x, player2.y, Arrow.LEFT, player2.arrowspeed);
					} else
					if (keys[VK_L] && !keys[VK_J]) {
						a = new Arrow(player2.x, player2.y, Arrow.RIGHT, player2.arrowspeed);
					} else
					if (!(keys[VK_RIGHT] || keys[VK_LEFT] || keys[VK_DOWN] || keys[VK_UP])) {
						a = new Arrow(player2.x, player2.y, player2.last_input, player2.arrowspeed);
					}
					
					if (a != null) {
						arrows.add(a);
					}
					break;

				default:
					break;
			}
		} else
		if (state == MENU) {
			switch (code) {
				case VK_DOWN:

				case VK_UP:

				case VK_RIGHT:

				case VK_LEFT:

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

	public void playerCalc() {
		// Time
		player1.tick();
		player2.tick();

		// Player 1 Movement
		if (keys[VK_RIGHT] == keys[VK_LEFT] ||
		    keys[VK_]) {
			// XNOR: both or neither are pressed
			player1.drag();
		} else if (keys[VK_RIGHT]) {
			player1.accelerate(RIGHT);
		} else if (keys[VK_LEFT]) {
			player1.accelerate(LEFT);
		}

		player1.move(map);
		player1.fall();

		if (keys[VK_L] == keys[VK_J]) {
			// XNOR: both or neither are pressed
			player2.drag();
		} else if (keys[VK_L]) {
			player2.accelerate(RIGHT);
		} else if (keys[VK_J]) {
			player2.accelerate(LEFT);
		}

		player2.move(map);
		player2.fall();
	}

	public void arrowCalc() {
		for (Arrow a : arrows) {
			a.fall();
			a.move(map);

			if (a.intersects(player1)) {
				player1.hurt();
				player1.knockback(a);
				a.alive = false;
			}
			
			if (a.intersects(player2)) {
				player2.hurt();
				player2.knockback(a);
				a.alive = false;
			}
		}

		arrows.removeIf(a -> !a.alive);
	}

	public void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, sizex, sizey);
		g.drawImage(textures.getTexture(map.name), 0, 0, this);

		g.setColor(Color.black);
		// for (Block b : map.blocks) {
		// 	fillRect(g, b);
		// }

	}

	public void paintPlayers(Graphics g) {
		g.setColor(Color.green);
		fillRect(g, player1);
		g.fillRect(player1.x+1280, player1.y, 32, 32);
		g.fillRect(player1.x-1280, player1.y, 32, 32);
		g.fillRect(player1.x, player1.y-640, 32, 32);
		g.fillRect(player1.x, player1.y+640, 32, 32);


		g.setColor(Color.blue);
		fillRect(g, player2);
		g.fillRect(player2.x+1280, player2.y, 32, 32);
		g.fillRect(player2.x-1280, player2.y, 32, 32);
		g.fillRect(player2.x, player2.y-640, 32, 32);
		g.fillRect(player2.x, player2.y+640, 32, 32);

		g.setColor(Color.red);
		g.fillRect(player1.x, player1.y, player1.hp*8, 4);
		g.fillRect(player2.x, player2.y, player2.hp*8, 4);
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


