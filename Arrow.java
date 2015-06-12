/* Arrow.java
 * Damaging arrow entities. */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class Arrow extends Rectangle implements Globals {

	// Velocity
	double velx = 0,
		   vely = 0;

	int direction;

	boolean alive = true;

	public Arrow(Player player, int dir) {
		/* Constructs the arrow one tile over in the given direction
		 * from the source tile.  Arrows are centred in the tile and
		 * have a size of 32*16 when horizontal and 16*32 when vertical */

		direction = dir;

		if (direction == RIGHT) {
			width = 32;
			height = 16;

			x = player.x + player.width;
			y = player.y;

			velx = player.arrowspeed;

		} else
		if (direction == LEFT) {
			width = 32;
			height = 16;

			x = player.x - width;
			y = player.y;


			velx = -player.arrowspeed;

		} else
		if (direction == DOWN) {
			width = 16;
			height = 32;

			x = player.x + player.width / 2 - width / 2;
			y = player.y + player.height;

			vely = player.arrowspeed;

		} else
		if (direction == UP) {
			width = 16;
			height = 32;

			x = player.x + player.width / 2 - width / 2;
			y = player.y - height;

			vely = -player.arrowspeed;
		}
	}

	public void move(Map map) {
		/* Changes position each frame */

		// Horizontal movement
		if (velx > 0) {
			// Moving right

			// Each pixel -> each block
			outer: {
				for (int i = 0; i < velx; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(this)) {
							// Arrow disappears on block touch
							alive = false;

							break outer;
						}
					}

					x++;
				}
			}
		} else
		if (velx < 0) {
			// Moving left

			// Checks each block for collision with the left side
			outer: {
				for (int i = 0; i < -velx; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(this)) {
							alive = false;

							break outer;
						}
					}

					x--;
				}
			}
		}

		// Looping horizontally
		if (x < 0) {
			x += 1280;
		} else
		if (x >= 1279) {
			x -= 1280;
		}

		// Vertical movement
		if (vely > 0) {
			outer: {
				for (int i = 0; i < vely; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(this)) {
							alive = false;

							break outer;
						}
					}

					y++;
				}
			}
		} else
		if (vely < 0) {
			outer: {
				for (int i = 0; i < -vely; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(this)) {
							alive = false;
							break outer;
						}
					}

					y--;
				}
			}
		}

		// Looping vertically
		if (y < 0) {
			y += 640;
		} else
		if (y >= 640) {
			y -= 640;
		}
	}

	public BufferedImage getSprite(TextureManager tex) {
		/* Returns texture */
		String template = "%s-%s";
		String name;

		// Different texture if double the speed
		if (Math.max(Math.abs(velx), Math.abs(vely)) == 32) {
			name = "SpeedA";
		} else {
			name = "A";
		}

		switch (direction) {
			case RIGHT:
				return tex.getTexture(String.format(template, name, "R"));
			case LEFT:
				return tex.getTexture(String.format(template, name, "L"));
			case DOWN:
				return tex.getTexture(String.format(template, name, "D"));
			case UP:
				return tex.getTexture(String.format(template, name, "U"));
			default:
				return null;
		}
	}
}