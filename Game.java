import java.awt.*;
import java.awt.event.KeyEvent;
public class Game {
	// Member declarations
	public static ScreenManager screen;
	public static InputManager input;
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
			for (int k = 0; k<1000; k++) {
			screen.showTestImage();
			try {
				Thread.sleep(20);
			} catch (Exception e) {}
			}

	}
	public static InputManager inputManager() {
		return input;
	}
	
	// Private (static) methods
	/**
	 * Initialization method for the static game instance.
	 */
	private static void init() {
		screen = new ScreenManager();
		screen.startFullscreen(DISPLAY_MODES[15]);
		
		input = new InputManager(screen.getWindow());
		input.bind(KeyEvent.VK_LEFT, "p1left");
		input.bind(KeyEvent.VK_RIGHT, "p1right");
		input.bind(KeyEvent.VK_UP, "p1up");
		input.bind(KeyEvent.VK_DOWN, "p1down");
	}
}
