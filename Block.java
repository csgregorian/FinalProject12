import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;

public class Block extends Rectangle implements Globals {

	public Block(int x, int y) {
		this.x = x;
		this.y = y;

		width = height = TILESIZE;
	}
}