/* GamePanel.java
 * 
 */

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

public class GamePanel extends JPanel implements KeyListener, Globals {
    // Window size
    int sizex, sizey;

    // Keys pressed
    boolean keys[] = new boolean[256];

    // Texture manager
    TextureManager tex = new TextureManager();

    // Players
    ArrayList<String> players = new ArrayList<String>();
    Player player1 = new Player("Link", "Bow", 7*32, 5*32),
           player2 = new Player("BlackLink", "IceBow", 34*32, 5*32);

    ArrayList<String> bows = new ArrayList<String>();

    int player1_cursor = 0;
    int bow1_cursor = 0;
    int player2_cursor = 1;
    int bow2_cursor = 0;

    // Maps
    HashMap<String, Map> maps = new HashMap<>();
    ArrayList<String> map_names = new ArrayList<>();
    Map map;
    int map_cursor = 0;

    // Entities
    ArrayList<Arrow> arrows = new ArrayList<>();
    ArrayList<Powerup> powerups = new ArrayList<>();


    // Game State
    int state = INTRO;
    int timer = 0;


    public GamePanel(int x, int y) {
        sizex = x; sizey = y;

        for (int i = 0; i < 256; i++)
            keys[i] = false;

        loadImages();

        loadMaps();
        map = maps.get("Forest");

        loadPlayers();

        // Event listeners
        addKeyListener(this);

        setFocusable(true);
    }

    public void resetMap(String mapname) {
        timer = 0;
        arrows.clear();
        powerups.clear();
        map = maps.get(mapname);   
    }

    public void resetPlayers(String name1, String name2, String bow1, String bow2) {
        player1 = new Player(name1, bow1, 7*32, 5*32);
        player2 = new Player(name2, bow2, 34*32, 5*32);
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
            System.err.println("res/img/player_list.txt not found");
            System.exit(0);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(0);
        }

        Scanner bow_list;

        try {
            bow_list = new Scanner(new File("res/img/bow_list.txt"));

            while (bow_list.hasNext()) {
                bows.add(bow_list.next());
            }
        } catch (FileNotFoundException e) {
            System.err.println("res/img/bow_list.txt not found");
            System.exit(0);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(0);
        }
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
                    state = PAUSE;
                    break;
                case VK_Q:
                    Block place = map.powerups.get(rng.nextInt(map.powerups.size()));
                    powerups.add(new Powerup(place.x, place.y, rng.nextInt(6)));
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
                        a = new Arrow(player1, DOWN);
                    } else
                    if (keys[VK_UP] && !keys[VK_DOWN]) {
                        a = new Arrow(player1, UP);
                    } else
                    if (keys[VK_LEFT] && !keys[VK_RIGHT]) {
                        a = new Arrow(player1, LEFT);
                    } else
                    if (keys[VK_RIGHT] && !keys[VK_LEFT]) {
                        a = new Arrow(player1, RIGHT);
                    } else
                    if (!(keys[VK_RIGHT] || keys[VK_LEFT] || keys[VK_DOWN] || keys[VK_UP])) {
                        a = new Arrow(player1, player1.last_input);
                    }

                    if (a != null && player1.arrows > 0) {
                        player1.shoot(a.direction);
                        arrows.add(a);
                    }

                    break;

                case VK_D:
                    if (keys[VK_K] && !keys[VK_I]) {
                        a = new Arrow(player2, DOWN);
                    } else
                    if (keys[VK_I] && !keys[VK_K]) {
                        a = new Arrow(player2, UP);
                    } else
                    if (keys[VK_J] && !keys[VK_L]) {
                        a = new Arrow(player2, LEFT);
                    } else
                    if (keys[VK_L] && !keys[VK_J]) {
                        a = new Arrow(player2, RIGHT);
                    } else
                    if (!(keys[VK_RIGHT] || keys[VK_LEFT] || keys[VK_DOWN] || keys[VK_UP])) {
                        a = new Arrow(player2, player2.last_input);
                    }
                    
                     if (a != null && player2.arrows > 0) {
                        player2.shoot(a.direction);
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

                case VK_DOWN:
                    bow1_cursor++;
                    bow1_cursor = bow1_cursor % bows.size();
                    break;

                case VK_UP:
                    bow1_cursor--;
                    bow1_cursor = (bow1_cursor + bows.size()) % bows.size();
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

                case VK_K:
                    bow2_cursor++;
                    bow2_cursor = bow2_cursor % bows.size();
                    break;

                case VK_I:
                    bow2_cursor--;
                    bow2_cursor = (bow2_cursor + bows.size()) % bows.size();
                    break;

                case VK_ENTER:
                    state = MAPMENU;
                    resetPlayers(players.get(player1_cursor), players.get(player2_cursor),
                                 bows.get(bow1_cursor), bows.get(player2_cursor));
                    break;

                case VK_ESCAPE:
                    // state = INTRO;
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
                case VK_ESCAPE:
                    state = PLAYERMENU;
                    break;
                default:
                    break;

            }
        } else
        if (state == PAUSE) {
            switch (code) {
                case VK_ENTER:
                    state = INTRO;
                    break;
                case VK_ESCAPE:
                    state = GAME;
                    break;
                default:
                    break;
            }
        } else
        if (state == INTRO) {
            switch (code) {
                case VK_ESCAPE:
                    System.exit(0);
                    break;
                case VK_ENTER:
                    state = PLAYERMENU;
                    break;
            }
        }
    }

    public void gameTick() {
        timer++;
        powerupCalc();
        playerCalc();
        arrowCalc();
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public void powerupCalc() {
        if (map.powerups.size() > 0 &&
            timer % (FPS * 5) == 1 &&
            rng.nextBoolean()) {
            Block place = map.powerups.get(rng.nextInt(map.powerups.size()));
            powerups.add(new Powerup(place.x, place.y, rng.nextInt(7)));
        }
        
        for (Powerup p : powerups) {
            p.tick();

            if (p.intersects(player1)) {
                player1.getPowerup(p.type);
                p.alive = false;
            }

            if (p.intersects(player2)) {
                player2.getPowerup(p.type);
                p.alive = false;
            }
        }

        powerups.removeIf(p -> !p.alive);
    }

    public void playerCalc() {
        // Player 1 Movement

        if (player2.powerup != TIME || timer % 2 == 0) {
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
        }

        if (player1.powerup != TIME || timer % 2 == 0) {
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
        
        player1.tick();
        player2.tick();
    }

    public void arrowCalc() {
        for (Arrow a : arrows) {
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
            case INTRO:
                paintIntro(g);
                break;

            case GAME:
                paintGame(g);
                break;

            case PAUSE:
                paintGame(g);
                paintPause(g);
                break;

            case PLAYERMENU:
                paintPlayerMenu(g);
                break;

            case MAPMENU:
                paintMapMenu(g);
                break;
        }
    }

    public void paintIntro(Graphics g) {
        g.drawImage(tex.getTexture("Background"), 0, 0, this);
    }

    public void paintGame(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, sizex, sizey);
        g.drawImage(tex.getTexture(map.name), 0, 0, 1280, 640, this);

        g.setColor(Color.black);
        // for (Block b : map.blocks) {
        //  fillRect(g, b);
        // }


        for (int x = -1280; x <= 1280; x += 1280) {
            for (int y = -640; y <= 640; y += 640) {
                g.drawImage(
                    player1.getSprite(tex),
                    player1.x + x, player1.y + y,
                    player1.width, player1.height, this
                );

                int bowx, bowy;
                switch (player1.last_input) {
                    case RIGHT:
                        bowx = player1.x + x + player1.width;
                        bowy = player1.y + y + player1.height;
                        break;

                    case LEFT:
                        bowx = player1.x + x - player1.width;
                        bowy = player1.y + y - player1.height;
                        break;

                    case UP:
                        


                }

                g.drawImage(
                    player2.getSprite(tex),
                    player2.x + x, player2.y + y,
                    player2.width, player2.height, this
                );
            }
        }

        g.setColor(Color.yellow);
        for (Powerup p : powerups) {
            g.drawImage(p.getSprite(tex), p.x, p.y, this);
            // fillRect(g, p);
        }

        g.setColor(Color.green);
        for (Arrow a : arrows) {
            g.drawImage(a.getSprite(tex), a.x, a.y, this);
        }

        g.drawImage(tex.getTexture("Overlay"), 0, 640, this);

        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        g.drawString("Player 1", 32, 690);
        g.drawString("Player 2", 1100, 690);

        g.setColor(Color.red);
        g.fillRect(32, 692, 32*player1.hp, 4);
        g.fillRect(1100, 692, 32*player2.hp, 4);

        g.drawImage(tex.getTexture("Overlay"), 0, 640, this);

        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Segoe UI", Font.PLAIN, 32));
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

        for (int i = 0; i < map_names.size(); i++) {
            g.drawImage(tex.getTexture(map_names.get(i)),
                       160 + ((i % 2) * 640),
                       80 + ((i / 2) * 320),
                       320,
                       160,
                       this);
        }
        g.drawImage(tex.getTexture("Overlay"), 0, 640, this);

    }

    public void paintPlayerMenu(Graphics g) {
        g.drawImage(tex.getTexture("Background"), 0, 0, this);

        String p1 = String.format("%s-D-0", players.get(player1_cursor));
        String p2 = String.format("%s-D-0", players.get(player2_cursor));

        String b1 = String.format("%s-D", bows.get(bow1_cursor));
        String b2 = String.format("%s-D", bows.get(bow2_cursor));


        g.drawImage(tex.getTexture(p1), 160, 160, 160, 160, this);
        g.drawImage(tex.getTexture(b1), 320, 160, 160, 160, this);

        g.drawImage(tex.getTexture(p2), 700, 160, 160, 160, this);
        g.drawImage(tex.getTexture(b2), 860, 160, 160, 160, this);

        g.drawImage(tex.getTexture("Overlay"), 0, 640, this);
    }

    public void paintPause(Graphics g) {
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(0, 0, 1280, 640);

        g.setColor(Color.white);
        g.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        g.drawString("Press ENTER to exit.", 300, 298); 
    }

    public void fillRect(Graphics g, Rectangle rect) {
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
}




















