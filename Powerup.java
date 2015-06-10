import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;

public class Powerup extends Rectangle implements Globals {
	int type;

	boolean alive = true;

	public Powerup(int startx, int starty, int type) {
		x = startx;
		y = starty;

		this.type = type;

		width = height = 32;
	}

	public BufferedImage getSprite(TextureManager tex) {
		return tex.getTexture("Powerup-" + Integer.toString(type));
	}
}