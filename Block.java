import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;

class Block extends Rectangle implements Constants {

	public Block(int x, int y) {
		this.x = x;
		this.y = y;

		width = height = TILESIZE;
	}
}