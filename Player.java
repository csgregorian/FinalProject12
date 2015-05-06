import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;


class Player {
	// Location
	int x, y;

	// Velocity
	int velx, vely;
	int maxvelx, maxvely;

	// Jump
	int jspeed;

	public Player(int startx, int starty) {
		x = startx;
		y = starty;

		velx = vely = 0;
		maxvelx = 2;
		maxvely = 5;

		jspeed = 2;
	}

	public void accelerate(int accelx) {
		velx += accelx;

		if (velx > 2) {
			velx = 2;
		} else if (velx < -2) {
			velx = -2;
		}
	}

	public void jump() {

	}

	public void drag() {
	}

	public void move() {
		x += velx;
		y += vely;
	}
}