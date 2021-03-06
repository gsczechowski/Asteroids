import java.awt.*;
import java.awt.event.KeyEvent;
public class Game {
	// Member declarations
	public static ScreenManager screen;
	public static InputManager input;
	public static ResourceManager resources;
	public static ScoreManager score;
	public static Settings settings;
		// These display modes were adapted from the most common
		// screen modes per the Steam hardware configuration survey
	public static final DisplayMode[] DISPLAY_MODES = {
		new DisplayMode(640, 480, 16, 0),
		new DisplayMode(640, 480, 32, 0),
		new DisplayMode(800, 600, 16, 0),
		new DisplayMode(800, 600, 32, 0),
		new DisplayMode(1024, 768, 16, 0),
		new DisplayMode(1024, 768, 32, 0),
		new DisplayMode(1280, 800, 16, 0),
		new DisplayMode(1280, 800, 32, 0),
		new DisplayMode(1280, 1024, 16, 0),
		new DisplayMode(1280, 1024, 32, 0),
		new DisplayMode(1366, 768, 16, 0),
		new DisplayMode(1366, 768, 32, 0),
		new DisplayMode(1600, 900, 16, 0),
		new DisplayMode(1600, 900, 32, 0),
		new DisplayMode(1920, 1080, 16, 0),
		new DisplayMode(1920, 1080, 32, 0)	
	};
	// Public (static) methods
	/**
	 * Initializes the game and starts the game.
	 */
	public static void run() {
		init();
		//This section simply tests the different display configurations.
		//Delete this code after testing.
			while(true) {
				screen.showTestImage();
				try {
					Thread.sleep(10);
				} catch (Exception e) {}
			}

	}
	// Private (static) methods
	/**
	 * Initialization method for the static game instance.
	 */
	private static void init() {
		settings = new Settings();
		resources = new ResourceManager();
		resources.initializeShips();
		screen = new ScreenManager();
		screen.startFullscreen(DISPLAY_MODES[15]);
		resources.initGravity(screen.getWindowSize());
		
		input = new InputManager(screen.getWindow());
		//common controls
		input.bind(KeyEvent.VK_ESCAPE, "esc");
		//player 1 controls
		input.bind(KeyEvent.VK_A, "p1left");
		input.bind(KeyEvent.VK_D, "p1right");
		input.bind(KeyEvent.VK_W, "p1up");
		input.bind(KeyEvent.VK_S, "p1down");
		input.bind(KeyEvent.VK_SPACE, "p1shoot");
		
		//player 2 controls
		input.bind(KeyEvent.VK_UP, "p2up");
		input.bind(KeyEvent.VK_DOWN, "p2down");
		input.bind(KeyEvent.VK_LEFT, "p2left");
		input.bind(KeyEvent.VK_RIGHT, "p2right");
		input.bind(KeyEvent.VK_ENTER, "p2shoot");
		
		score = new ScoreManager();
		score.increaseLevel();
		
	}
}
