import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;


class Player extends Rectangle {
	// Location
	int x, y;

	// Size
	int width, height;

	// Velocity
	double velx, vely;
	double maxvelx, maxvely;
	double startvelx, startvely;

	// Acceleration/Deceleration
	double accelx, decelx;

	// Jump/Gravity
	double jumpy, gravy;

	// Constants
	static boolean RIGHT = true, LEFT = false;

	public Player(int startx, int starty) {
		x = startx;
		y = starty;

		width = height = 32;

		velx = vely = 0;
		maxvelx = 5;
		maxvely = 5;
		startvelx = 1;
		startvely = 1;

		accelx = 0.2;
		decelx = 0.5;

		jumpy = -6;
		gravy = 0.3;
	}

	public void accelerate(boolean direction) {
		if (direction == RIGHT) {
			velx = Math.max(velx, 1);
			velx += accelx;
		} else {
			velx = Math.min(velx, -1);
			velx -= accelx;
		}

		if (velx > maxvelx) {
			velx = maxvelx;
		} else if (velx < -maxvelx) {
			velx = -maxvelx;
		}
	}

	public void drag() {
		if (velx > decelx) {
			velx -= decelx;
		} else if (velx < -decelx) {
			velx += decelx;
		} else {
			velx = 0;
		}
	}

	public void jump() {
		vely = jumpy;
	}

	public void fall() {
		vely += gravy;
	}

	public void move(Map map) {

		if (velx > 0) {
			// Falling
			outer:
			for (int i = 0; i < velx; i++) {
				for (Block b : map.blocks) {
					if (b.intersects(rectRight())) {
						velx = 0;
						break outer;
					}
				}

				x++;
			}
		} else if (velx < 0) {
			// Jumping
			outer:
			for (int i = 0; i < -velx; i++) {
				for (Block b : map.blocks) {
					if (b.intersects(rectLeft())) {
						velx = 0;
						break outer;
					}
				}

				x--;
			}
		}

		if (vely > 0) {
			// Falling
			outer:
			for (int i = 0; i < vely; i++) {
				for (Block b : map.blocks) {
					if (b.intersects(rectBottom())) {
						vely = 0;
						break outer;
					}
				}

				y++;
			}
		} else if (vely < 0) {
			// Jumping
			outer:
			for (int i = 0; i < -vely; i++) {
				for (Block b : map.blocks) {
					if (b.intersects(rectTop())) {
						vely = 0;
						break outer;
					}
				}

				y--;
			}
		}
	}

	public Rectangle rectRight() {
		return new Rectangle(x + 32, y + 2, 1, 28);
	}

	public Rectangle rectLeft() {
		return new Rectangle(x, y + 2, 1, 28);
	}



	public Rectangle rectTop() {
		return new Rectangle(x + 2, y, 28, 1);
	}

	public Rectangle rectBottom() {
		return new Rectangle(x + 2, y + 32, 28, 1);
	}


}