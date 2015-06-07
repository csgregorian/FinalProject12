import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;

public class Powerup extends Rectangle implements Constants {
	int type;

	public Powerup(int x, int y, int type) {
		this.x = x;
		this.y = y;

		this.type = type;

		width = height = 32;
	}
}