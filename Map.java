/* Map.java
 * The stage and its associated powerup/collision data. */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;
import java.util.ArrayList;

public class Map implements Globals {

	// Collision
	ArrayList<Block> blocks = new ArrayList<>();
	
	// Powerup spawn locations
	ArrayList<Block> powerups = new ArrayList<>();

	// Texture
	String name;

	public Map(String name) {
		/* Opens the text file and converts to blocks and powerups */

		this.name = name;
		String filename = String.format("res/maps/%s.txt", name);

		BufferedReader block_file;
		
		try {
			block_file = new BufferedReader(new FileReader(filename));

			// Checks each column and row
			for (int y = 0; y < TILEY; y++) {	
				for (int x = 0; x < TILEX; x++) {
					char c = (char)(block_file.read());

					if (c == '\n') {
						c = (char)(block_file.read());
						// Flushes buffer and gets new character
					}

					if (c == 'O') {
						blocks.add(new Block(x * TILESIZE, y * TILESIZE));
					} else
					if (c == '_') {
						powerups.add(new Block(x * TILESIZE, y * TILESIZE));
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