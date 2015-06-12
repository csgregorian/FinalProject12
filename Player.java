/* Player.java
 * Represents the users character. */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;

public class Player extends Rectangle implements Globals {
	// Sprite identifiers
	String name;
	String bow;

	// Velocity
	private double velx = 0,
		vely = 0,
		maxvelx = 6,
		maxvely = 50,
    	startvelx = 3,
    	startvely = 1;

	// Acceleration/Deceleration
	private double accelx = 0.5,
		decelx = 1;

	// Jump/Gravity
	private double jumpy = -7,
		   gravy = 0.3;
	private int jumps = 2;

	// Collisions
	private boolean touch_right, touch_left, touch_up, touch_down;

	// Arrows
	int arrows = 5;
	int arrowspeed = 16;

	// Last input
	int last_input = RIGHT;

	// Health
	int hp = 4;
	boolean alive = true;

	// Powerups
	int powerup = 0;

	// Timers
	int powerup_timer = 0;
	int hurt_timer = 60;
	int shoot_timer = 0;

	public Player(String name, String bow, int startx, int starty) {
		this.name = name;
		this.bow = bow;

		x = startx;
		y = starty;

		// PLAYERSIZE is a constant inherited from Globals
		width = PLAYERSIZE;
		height = PLAYERSIZE;

		// Collisions default to false
		touch_right = touch_left = touch_up = touch_down = false;
	}

	public void tick() {
		/* Updates player each frame */

		// Decreases powerup lifetime
		if (powerup != NONE) {
			powerup_timer--;
			if (powerup_timer <= 0) {
				removePowerup();
			}
		}

		// Invincibility
		if (hurt_timer > 0) {
			hurt_timer--;
		}

		// Arrow capacity
		if (shoot_timer > 0) {
			shoot_timer--;
		} else {
			arrows = 5;
		}


		// Lose condition check
		if (hp <= 0) {
			alive = false;
		}
	}

	public void accelerate(int direction) {
		/* Changes velocity.
		 * Run when L/R directional keys are held down */

		// Sets speed to a minimum value
		if (direction == RIGHT) {
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
		/* Sudden vertical acceleration */

		if (powerup == FLY) {
			// No jump limit
		} else
		if (jumps == 0) {
			return;
		} else {
			jumps--;
		}

		vely = jumpy;

		// Wall-kicking: sudden jump in opposite collision direction
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
		/* Gravity effects */

		vely += gravy;
		if (vely > maxvely) {
			vely = maxvely;
		}
	}

	public void move(Map map) {
		/* Position calculation */

		// Horizontal movement
		if (velx > 0) {
			// Moving right

			// Each pixel of movement, checks each block for collision
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

							// Adds another jump
							jumps = Math.max(jumps, 1);

							break outer;
						}
					}

					// Moves right
					x++;

					// Looping if out of bounds
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

		// Vertical movement
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

	public void shoot(int direction) {
		/* Run upon creation of a new arrow */
		arrows--;
		last_input = direction;
		shoot_timer += 30;
	}

	public void hurt() {
		/* Run upon collision with an arrow */
		if (hurt_timer <= 0) {
			hp--;
			hurt_timer = 60;
		}

	}

	public void knockback(Arrow a) {
		/* The kinetic energy of the arrow is tranferred
		 * to the player in the form of velocity. */
		velx += a.velx;
		vely += a.vely;
	}

	public void getPowerup(int type) {
		/* Adds a buff to the player */

		removePowerup();

		// Applies appropriate passive effect
		switch (type) {
			case NONE:
				break;

			case SPEED:
				accelx *= 2;
				maxvelx *= 2;
				break;

			case JUMP:
				jumpy *= 2;
				gravy *= 2;
				break;

			case BULLET:
				arrowspeed *= 2;
				break;

			case FLY:
			case AMMO:
			case TIME:
			default:
				// Fallthrough: these are active effects
				break;

		}

		powerup_timer = 600;
		powerup = type;
	}

	public void removePowerup() {
		/* Reverses buff effects */

		switch (powerup) {
			case NONE:
				break;

			case SPEED:
				accelx /= 2;
				maxvelx /= 2;
				break;

			case JUMP:
				jumpy /= 2;
				gravy /= 2;
				break;

			case BULLET:
				arrowspeed /= 2;
				break;

			case FLY:
			case AMMO:
			case TIME:
			default:
				// Fallthrough
				break;
		}

		powerup_timer = 0;
		powerup = NONE;
	}

	/* Hitboxes for each side of the player */
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
		/* Returns the relevant texture based on the player state */

		// Flashes when invincible
		if (hurt_timer > 0 && (hurt_timer % 8 / 4) == 0) {
			return null;
		}

		String template = "%s-%s-%d";
		switch (last_input) {
			case RIGHT:
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

	public BufferedImage getBowSprite(TextureManager tex) {
		/* Returns the relevant bow texture */

		// Blank if no recent shot
		if (shoot_timer <= 0) {
			return null;
		}

		String template = "%s-%s";
		switch (last_input) {
			case RIGHT:
				return tex.getTexture(String.format(template, bow, "R"));
			case LEFT:
				return tex.getTexture(String.format(template, bow, "L"));
			case DOWN:
				return tex.getTexture(String.format(template, bow, "D"));
			case UP:
				return tex.getTexture(String.format(template, bow, "U"));
			default:
				return tex.getTexture(String.format(template, bow, "R"));
		}
	}
}





