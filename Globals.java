/* Globals.java
 * Shared global interface between all classes */

import java.util.Random;

public interface Globals {
	// RNG
	Random rng = new Random();

	// Framerate
	final static int FPS = 60;

	// Player
	final static int PLAYERSIZE = 48;

	// Tiles
	final static int TILESIZE = 32, TILEX = 40, TILEY = 20;

	// Directions
	final static int RIGHT = 0, LEFT = 1, DOWN = 2, UP = 3;

	// Powerups
	final static int NONE = 0, SPEED = 1, JUMP = 2, BULLET = 3, FLY = 4, AMMO = 5, TIME = 6;

    // Game states
	final static int INTRO = 0, PLAYERMENU = 1, MAPMENU = 2, GAME = 3, PAUSE = 4, SCORE = 5;
}