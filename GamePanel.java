import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.geom.*;
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
	TextureManager tex = new TextureManager();

	// Players
	ArrayList<String> players = new ArrayList<String>();
	Player player1 = new Player("Link", 7*32, 5*32),
		   player2 = new Player("BlackLink", 34*32, 5*32);

	int player1_cursor = 0;
	int player2_cursor = 1;

	// Maps
	HashMap<String, Map> maps = new HashMap<>();
	ArrayList<String> map_names = new ArrayList<>();
	Map map;
	int map_cursor = 0;

	// Arrows
	ArrayList<Arrow> arrows = new ArrayList<>();

	// Game State
	int state = PLAYERMENU;


	public GamePanel(int x, int y) {
		sizex = x; sizey = y;

		for (int i = 0; i < 256; i++)
			keys[i] = false;

		loadImages();

		loadMaps();
		map = maps.get("Forest");

		loadPlayers();

		// Event listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		setFocusable(true);
	}

	public void resetMap(String mapname) {
		arrows.clear();
		map = maps.get(mapname);

		
	}

	public void resetPlayers(String name1, String name2) {
		Player player1 = new Player(name1, 7*32, 5*32),
			   player2 = new Player(name2, 34*32, 5*32);
	}

	public void loadImages() {
		Scanner image_list;

		try {
			image_list = new Scanner(new File("res/img/image_list.txt"));

			while (image_list.hasNext()) {
				String img_name = image_list.next();
				String file_name = String.format("res/img/%s.png", img_name);
				tex.addTexture(img_name, file_name);
			}
		} catch (FileNotFoundException e) {
			System.err.println("res/img/images_list.txt not found");
			System.exit(0);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(0);
		}
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
			String file_name = String.format("res/maps/%s.png", map_name);
			System.out.println(file_name);
			tex.addTexture(map_name, file_name);

			Map map = new Map(map_name);
			maps.put(map_name, map);
		}
	}

	public void loadPlayers() {
		Scanner player_list;

		try {
			player_list = new Scanner(new File("res/img/player_list.txt"));

			while (player_list.hasNext()) {
				players.add(player_list.next());
			}
		} catch (FileNotFoundException e) {
			System.err.println("res/img/images_list.txt not found");
			System.exit(0);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(0);
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
		if (state == PLAYERMENU) {
			switch (code) {
				case VK_RIGHT:
					player1_cursor++;
					if (player1_cursor == player2_cursor) {
						player1_cursor++;
					}
					player1_cursor = player1_cursor % players.size();
					break;
				case VK_LEFT:
					player1_cursor--;
					if (player1_cursor == player2_cursor) {
						player1_cursor--;
					}
					player1_cursor = (player1_cursor + players.size()) % players.size();
					break;
				case VK_L:
					player2_cursor++;
					player2_cursor = player2_cursor % players.size();
					if (player2_cursor == player1_cursor) {
						player2_cursor++;
					}
					player2_cursor = player2_cursor % players.size();
					break;
				case VK_J:
					player2_cursor--;
					if (player2_cursor == player1_cursor) {
						player2_cursor--;
					}
					player2_cursor = (player2_cursor + players.size()) % players.size();
					break;
				case VK_ENTER:
					state = MAPMENU;
					resetPlayers(players.get(player1_cursor), players.get(player2_cursor));
					break;
				default:
					break;
			}
		} else
		if (state == MAPMENU) {
			switch (code) {
				case VK_DOWN:
				case VK_UP:
				case VK_K:
				case VK_I:
					map_cursor = (map_cursor + 2) % 4;
					break;
				case VK_RIGHT:
				case VK_L:
					map_cursor = (map_cursor + 1) % 4;
					break;
				case VK_LEFT:
				case VK_J:
					map_cursor = (map_cursor + 3) % 4;
					break;
				case VK_ENTER:
					state = GAME;
					resetMap(map_names.get(map_cursor));
					break;
				default:
					break;

			}
		}

	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}


	public void playerCalc() {
		// Time
		player1.tick();
		player2.tick();

		// Player 1 Movement
		if (keys[VK_RIGHT] == keys[VK_LEFT] ||
		    keys[VK_Z]) {
			// XNOR: both or neither are pressed
			player1.drag();
		} else if (keys[VK_RIGHT]) {
			player1.accelerate(RIGHT);
		} else if (keys[VK_LEFT]) {
			player1.accelerate(LEFT);
		}

		player1.move(map);
		player1.fall();

		if (keys[VK_L] == keys[VK_J] ||
		    keys[VK_A]) {
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

	public void paintComponent(Graphics g) {
		switch (state) {
			case GAME:
				paintGameBackground(g);
				paintGamePlayers(g);
				paintGameArrows(g);
				paintGameOverlay(g);
				break;

			case PLAYERMENU:
				paintPlayerMenu(g);
				paintPlayerOverlay(g);
				break;

			case MAPMENU:
				paintMapMenu(g);
				paintMapOverlay(g);
				break;
		}
	}

	public void paintGameBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, sizex, sizey);
		AffineTransform at = new AffineTransform();
		g.drawImage(tex.getTexture(map.name), 0, 0, this);

		g.setColor(Color.black);
		// for (Block b : map.blocks) {
		// 	fillRect(g, b);
		// }

	}

	public void paintGamePlayers(Graphics g) {
		g.drawImage(player1.getSprite(tex), player1.x, player1.y, this);
		g.drawImage(player1.getSprite(tex), player1.x-1280, player1.y, this);
		g.drawImage(player1.getSprite(tex), player1.x+1280, player1.y, this);
		g.drawImage(player1.getSprite(tex), player1.x, player1.y-640, this);
		g.drawImage(player1.getSprite(tex), player1.x, player1.y+640, this);

		g.drawImage(player2.getSprite(tex), player2.x, player2.y, this);
		g.drawImage(player2.getSprite(tex), player2.x-1280, player2.y, this);
		g.drawImage(player2.getSprite(tex), player2.x+1280, player2.y, this);
		g.drawImage(player2.getSprite(tex), player2.x, player2.y-640, this);
		g.drawImage(player2.getSprite(tex), player2.x, player2.y+640, this);
	}

	public void paintGameArrows(Graphics g) {
		g.setColor(Color.green);
		for (Arrow a : arrows) {
			g.drawImage(a.getSprite(tex), a.x, a.y, this);
		}
	}

	public void paintGameOverlay(Graphics g) {
		g.drawImage(tex.getTexture("Overlay"), 0, 640, this);

		g.setColor(new Color(200, 200, 200));
		g.setFont(new Font("Droid Sans", Font.PLAIN, 32));
		g.drawString("Player 1", 32, 690);
		g.drawString("Player 2", 1100, 690);

		g.setColor(Color.red);
		g.fillRect(32, 692, 32*player1.hp, 4);
		g.fillRect(1100, 692, 32*player2.hp, 4);
	}

	public void paintMapMenu(Graphics g) {
		g.drawImage(tex.getTexture("Background"), 0, 0, this);

		g.setColor(Color.white);
		g.fillRect(156 + (map_cursor % 2) * 640,
				   76 + (map_cursor / 2) * 320, 328, 168);

		g.setColor(Color.red);
		for (int i = 0; i < map_names.size(); i++) {
			g.drawImage(tex.getTexture(map_names.get(i)),
					   160 + ((i % 2) * 640),
					   80 + ((i / 2) * 320),
					   320,
					   160,
					   this);
		}
	}

	public void paintMapOverlay(Graphics g) {
		g.drawImage(tex.getTexture("Overlay"), 0, 640, this);
	}

	public void paintPlayerMenu(Graphics g) {
		g.drawImage(tex.getTexture("Background"), 0, 0, this);

		String p1 = String.format("%s-D-0", players.get(player1_cursor));
		String p2 = String.format("%s-D-0", players.get(player2_cursor));

		g.drawImage(tex.getTexture(p1), 160, 160, 320, 320, this);
		g.drawImage(tex.getTexture(p2), 700, 160, 320, 320, this);
	}

	public void paintPlayerOverlay(Graphics g) {
		g.drawImage(tex.getTexture("Overlay"), 0, 640, this);
	}

	public void fillRect(Graphics g, Rectangle rect) {
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
}


