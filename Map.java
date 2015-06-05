import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;
import java.util.ArrayList;

class Map implements Constants {
	// Constants
	final static int RIGHT = 0, LEFT = 1, DOWN = 2, UP = 3;
	final static int NONE = 0, SPEED = 1, JUMP = 2, BULLET = 3;


	ArrayList<Block> blocks;
	ArrayList<Powerup> powerups;
	String name;

	public Map(String name) {
		this.name = name;
		
		blocks = new ArrayList<Block>();
		String filename = String.format("res/maps/%s.txt", name);

		BufferedReader block_file;
		
		try {
			block_file = new BufferedReader(new FileReader(filename));

			for (int y = 0; y < TILEY; y++) {	
				for (int x = 0; x < TILEX; x++) {
					char c = (char)(block_file.read());
					if (c == '\n') {
						c = (char)(block_file.read());
					}
					if (c == 'O') {
						blocks.add(new Block(x * TILESIZE, y * TILESIZE));
					}
				}
			}



			

			block_file.close();

		} catch (FileNotFoundException e) {
			System.err.println(filename + " not found");
			System.exit(0);
		} catch (Exception e) {
			System.err.println(e);
			System.exit(0);
		}

	}
}