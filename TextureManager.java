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

	public void addTexture(String filename) {
		addTexture(filename, filename);
	}

	public boolean addTexture(String filename, String name) {
		filename = "resources/" + filename + ".png";

		BufferedImage image;
		try {
			image = ImageIO.read(new File(filename));
			textures.put(name, image);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public BufferedImage getTexture(String name) {
		return textures.get(name);
	}
}