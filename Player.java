import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;

public class Player extends Rectangle implements Globals {
	String name;

	// Velocity
	double velx = 0,
		   vely = 0,
		   maxvelx = 6,
		   maxvely = 50,
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
	int arrowspeed = 16;

	// Last input
	int last_input = -1;

	// Health
	int hp = 4;
	boolean alive = true;

	// Powerups
	int powerup = 0;
	int powerup_timer = 0;

	public Player(String name, int startx, int starty) {
		this.name = name;

		x = startx;
		y = starty;

		width = PLAYERSIZE;
		height = PLAYERSIZE;

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
		if (vely > maxvely) {
			vely = maxvely;
		}
	}

	public void move(Map map) {
		if (velx > 0) {
			// Moving right

			// Checks each block for collision with the right side
			outer: {
				for (int i = 0; i < velx; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(rectRight())) {
							// Hits block on right
							velx = 0;

							if (vely > 0.5) {
								vely -= 0.5;
							}

							touch_right = true;
							jumps = Math.max(jumps, 1);

							break outer;
						}
					}

					// Moves right
					x++;

					if (x < 0) {
						x += 1280;
					} else
					if (x >= 1279) {
						x -= 1280;
					}

				}

				// No collision on right
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
							// Hits block on left
							velx = 0;
							
							if (vely > 0.5) {
								vely -= 0.5;
							}

							touch_left = true;
							jumps = Math.max(jumps, 1);

							break outer;
						}
					}

					// Moves left
					x--;

					if (x < 0) {
						x += 1280;
					} else
					if (x >= 1279) {
						x -= 1280;
					}

					
				}

				// No collision on left
				touch_left = false;
				touch_right = false;
			}
		}

		// Horizontal loop adjustment
		

		if (vely > 0) {
			// Falling

			// Labeled block acts as a for/else construct: if the player never
			// collides with the ground, touch_down is set to true
			outer: {
				for (int i = 0; i < vely; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(rectBottom())) {
							// Hits block on bottom
							vely = 0;
							touch_down = true;
							jumps = 2;
							break outer;
						}
					}

					// Moves down
					y++;

					if (y < 0) {
						y += 640;
					} else
					if (y >= 640) {
						y -= 640;
					}

					
				}

				// No collision on bottom
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
							// Hits block on top
							vely = 0;
							touch_up = true;
							break outer;
						}
					}

					// Moves up
					y--;
					
					if (y < 0) {
						y += 640;
					} else
					if (y >= 640) {
						y -= 640;
					}

				}

				// No collision on top
				touch_up = false;
			}
		}
		

		
	}

	public void hurt() {
		hp--;
		if (hp <= 0) {
			alive = false;
		}
	}

	public void knockback(Arrow a) {
		velx += a.velx;
		vely += a.vely;
	}

	public void getPowerup(int type) {
		powerup_timer = 600;

		if (powerup != NONE) {
			removePowerup();
		}

		powerup = type;
		switch (powerup) {
			case NONE:
				break;

			case SPEED:
				maxvelx *= 2;
				break;

			case JUMP:
				jumpy *= 2;
				gravy *= 2;

			case BULLET:
				arrowspeed *= 2;
				break;

			case FLY:
				break;

			case AMMO:
				break;

			default:
				break;

		}
	}

	public void removePowerup() {
		switch (powerup) {
			case NONE:
				break;

			case SPEED:
				maxvelx /= 2;

			case JUMP:
				jumpy /= 2;
				gravy /= 2;

			case BULLET:
				arrowspeed /= 2;
				break;

			case FLY:
				break;

			case AMMO:
				break;

			default:
				break;
		}

		powerup = NONE;
	}

	public void checkPowerup() {
		System.out.println(powerup);
		powerup_timer--;

		if (powerup_timer == 0) {
			removePowerup();
		}
	}

	public Rectangle rectRight() {
		return new Rectangle(x + width, y, 1, height - 1);
	}

	public Rectangle rectLeft() {
		return new Rectangle(x-1, y + 1, 1, height - 2);
	}

	public Rectangle rectTop() {
		return new Rectangle(x, y, width, 1);
	}

	public Rectangle rectBottom() {
		return new Rectangle(x, y + height, width, 1);
	}

	public BufferedImage getSprite(TextureManager tex) {
		String template = "%s-%s-%d";
		switch (last_input) {
			case RIGHT:
				System.out.println(String.format(template, name, "R", (x % 1000) / 500));
				return tex.getTexture(String.format(template, name, "R", (x % 100) / 50));
			case LEFT:
				return tex.getTexture(String.format(template, name, "L", (x % 100) / 50));
			case DOWN:
				return tex.getTexture(String.format(template, name, "D", (x % 100) / 50));
			case UP:
				return tex.getTexture(String.format(template, name, "U", (x % 100) / 50));
			default:
				return tex.getTexture(String.format(template, name, "R", (x % 100) / 50));
		}
	}
}