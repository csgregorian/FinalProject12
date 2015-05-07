import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;
import java.util.ArrayList;

class Map {
	ArrayList<Block> blocks;
	String name;

	public Map(String name) {
		this.name = name;
		
		blocks = new ArrayList<Block>();
		String filename = String.format("res/maps/%s.txt", name);

		BufferedReader block_file;
		
		try {
			block_file = new BufferedReader(new FileReader(filename));


			for (int y = 0; y < 20; y++) {	
				for (int x = 0; x < 32; x++) {
					char c = (char)(block_file.read());
					if (c == '\n') {
						block_file.read();
					} else if (c == '1') {
						blocks.add(new Block(x * 32, y * 32));
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