import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;


class Player extends Rectangle {
	// Velocity
	double velx = 0,
		   vely = 0,
		   maxvelx = 6,
		   maxvely = 5,
	       startvelx = 3,
	       startvely = 1;

	// Acceleration/Deceleration
	double accelx = 0.5,
		   decelx = 1;

	// Jump/Gravity
	double jumpy = -7,
		   gravy = 0.3;
	int jumps = 2;

	// Collisions
	boolean touch_right, touch_left, touch_up, touch_down;

	// Arrows
	int arrows = 8;

	// Last input
	int last_input = -1;

	// Health
	int hp = 4;
	boolean alive = true;

	// Constants
	final static int RIGHT = 0, LEFT = 1, DOWN = 2, UP = 3;

	public Player(int startx, int starty) {
		x = startx;
		y = starty;

		width = 32;
		height = 32;

		touch_right = touch_left = touch_up = touch_down = false;
	}

	public void accelerate(int direction) {
		/* Changes velocity.
		 * Run when L/R directional keys are held down */

		// Sets speed to a minimum value
		if (direction == RIGHT) {
			// If the player is in the air, halve their acceleration

			velx = Math.max(velx, startvelx);
			velx += accelx;


		} else if (direction == LEFT) {
			velx = Math.min(velx, -startvelx);
			velx -= accelx;
		}

		// Caps speed at max and min values
		if (velx > maxvelx) {
			velx = maxvelx;
		} else if (velx < -maxvelx) {
			velx = -maxvelx;
		}
	}

	public void drag() {
		/* Reduces velocity 
		 * Run when the player is not accelerating */

		// Decreases velocity
		if (velx > decelx) {
			// Moving right
			velx -= decelx;
		} else if (velx < -decelx) {
			// Moving left
			velx += decelx;
		} else {
			velx = 0;
		}
	}

	public void jump() {
		if (jumps == 0) {
			return;
		}

		jumps--;
		vely = jumpy;

		if (!touch_down) {
			if (touch_right) {
				x--;
				velx = -10;
			} else
			if (touch_left) {
				x++;
				velx = 10;
			}
		}
	}

	public void fall() {
		vely += gravy;
	}

	public void move(Map map) {
		if (velx > 0) {
			// Moving right

			// Checks each block for collision with the right side
			outer: {
				for (int i = 0; i < velx; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(rectRight())) {
							// Stops player immediately on collision
							velx = 0;

							if (vely > 0.5) {
								vely -= 0.5;
							}

							touch_right = true;
							jumps = Math.max(jumps, 1);

							break outer;
						}
					}

					x++;
				}

				touch_right = false;
				touch_left = false;
			}
		} else
		if (velx < 0) {
			// Moving left

			// Checks each block for collision with the left side
			outer: {
				for (int i = 0; i < -velx; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(rectLeft())) {
							velx = 0;
							
							if (vely > 0.5) {
								vely -= 0.5;
							}

							touch_left = true;
							jumps = Math.max(jumps, 1);

							break outer;
						}
					}

					x--;
				}

				touch_left = false;
				touch_right = false;
			}
		}

		if (x < 0) {
			x += 1280;
		} else
		if (x >= 1279) {
			x -= 1280;
		}

		if (vely > 0) {
			// Falling

			// Labeled block acts as a for/else construct: if the player never
			// collides with the ground, touch_down is set to true
			outer: {
				for (int i = 0; i < vely; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(rectBottom())) {
							vely = 0;
							touch_down = true;
							jumps = 2;
							break outer;
						}
					}

					y++;
				}

				touch_down = false;
				jumps = Math.min(jumps, 1);
			}
		} else
		if (vely < 0) {
			// Jumping
			outer: {
				for (int i = 0; i < -vely; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(rectTop())) {
							vely = 0;
							touch_up = true;
							break outer;
						}
					}

					y--;
				}

				touch_up = false;
			}
		}
		

		if (y < 0) {
			y += 640;
		} else
		if (y >= 640) {
			y -= 640;
		}
	}

	public void hurt() {
		hp--;
		if (hp <= 0) {
			alive = false;
		}
	}

	public Rectangle rectRight() {
		return new Rectangle(x + 32, y + 1, 1, 30);
	}

	public Rectangle rectLeft() {
		return new Rectangle(x-1, y + 1, 1, 30);
	}

	public Rectangle rectTop() {
		return new Rectangle(x, y, 32, 1);
	}

	public Rectangle rectBottom() {
		return new Rectangle(x + 1, y + 32, 30, 1);
	}
}