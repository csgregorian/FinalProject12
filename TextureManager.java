import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;

class TextureManager {
	private HashMap<String, BufferedImage> textures;

	public TextureManager() {
		textures = new HashMap();
	}

	public void addTexture(String name, String filename) {
		filename = "res/" + filename;

		BufferedImage image;
		try {
			image = ImageIO.read(new File(filename));
			textures.put(name, image);
		} catch (Exception e) {
			System.err.print(e);
			System.exit(0);
		}
	}

	public BufferedImage getTexture(String name) {
		return textures.get(name);
	}
}