/* Arrow.java
 *

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

		} else {
			System.err.println("Invalid direction");
		}
	}

	public void move(Map map) {
		if (velx > 0) {
			// Moving right

			// Checks each block for collision with the right side
			outer: {
				for (int i = 0; i < velx; i++) {
					for (Block b : map.blocks) {
						if (b.intersects(this)) {
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
			// Jumping
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

		if (y < 0) {
			y += 640;
		} else
		if (y >= 640) {
			y -= 640;
		}
	}

	public BufferedImage getSprite(TextureManager tex) {
		String template = "%s-%s";
		String name;
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