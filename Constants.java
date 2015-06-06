public interface Constants {
	/* Shared global interface between all classes */

	// Tiles
	final static int TILESIZE = 32, TILEX = 40, TILEY = 20;

	// Directions
	final static int RIGHT = 0, LEFT = 1, DOWN = 2, UP = 3;

	// Powerups
	final static int NONE = 0, SPEED = 1, JUMP = 2, BULLET = 3;

	// Game states
	final static int GAME = 0, INTRO = 1, PLAYERMENU = 2, STAGEMENU = 3, GAME = 4, PAUSE = 5, SCORE = 6;
}