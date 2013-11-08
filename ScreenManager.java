import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ScreenManager {
	// Member declarations
	private GraphicsDevice _gd;
	private Visual _v;
	private Visual _v2;

	//Constructors
	public ScreenManager() {
		_gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		_v = new Visual("C:\\Users\\Brad\\Documents\\Purdue\\Junior\\Eclipse\\Asteroids\\Asteroids\\bin\\spaceship.png");
		_v2 = new Visual("C:\\Users\\Brad\\Documents\\Purdue\\Junior\\Eclipse\\Asteroids\\Asteroids\\bin\\asteroid1.png");
		try {
			_v.loadImage();
			_v.setScale(.25);
			_v2.loadImage();
			_v2.setScale(.25);
			_v.setCoords(300,300);
			_v2.setCoords(900,300);
		} catch (Exception e) {
			System.out.println("Error opening file.");
		}

	}

	//Public get/set methods
	public Graphics2D getCanvas() {
		return (Graphics2D)getDblBuffer().getDrawGraphics();
	}
	public Dimension getWindowSize() {
		return _gd.getFullScreenWindow().getSize();
	}

	//Public methods
	/**
	 * Sets the screen to fullscreen mode based on the display mode provided.
	 * If fullscreen mode is already enabled, this method changes the display mode
	 * to the one provided.
	 * @param mode
	 * New display mode to use for the fullscreen window
	 */
	public void startFullscreen(DisplayMode mode) {
		final JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setBackground(Color.cyan);
		jf.setUndecorated(true);
		jf.setIgnoreRepaint(true);
		jf.setResizable(false);
		if (_gd != null) {
			_gd.setFullScreenWindow(jf);
		}
		jf.createBufferStrategy(2);
		changeMode(mode);
		jf.setSize(_gd.getFullScreenWindow().getSize());
		jf.setVisible(true);
		jf.repaint();
	}
	/**
	 * If the screen is in fullscreen mode, changeMode will attempt to
	 * find the first compatible mode and switch the display mode to this
	 * mode.  ChangeMode searches for display modes from the bottom down,
	 * so the ordering of the array will determine which mode is selected first.
	 * @param modes
	 * Array of modes that the fullscreen window has to choose from
	 */
	public void changeMode(DisplayMode[] modes) {
		if (_gd.isDisplayChangeSupported()) {
			DisplayMode [] compModes = _gd.getDisplayModes();
			for (int i = modes.length; i>=0; i--) {
				for (int k = 0; k < compModes.length; k++) {
					if (modesEqual(modes[i], compModes[k])) {
						_gd.setDisplayMode(modes[i]);
						return; 
					}					
				}
			}
		}
	}
	/**
	 * changeMode attempts to change the display mode to the mode specified.
	 * If it is unable to do so, the function returns.
	 * @param mode
	 * The new display mode to use for the fullscreen window
	 */
	public void changeMode(DisplayMode mode) {
		if (_gd.isDisplayChangeSupported()) {
			try {
				_gd.setDisplayMode(mode);
			}catch (Exception e) {}
		}
	}


	/**
	 * Call this method to update the double buffered graphics object and
	 * draw it to the screen.
	 */
	public void updateGraphics() {
		BufferStrategy bs = getDblBuffer();
		if (bs != null && !bs.contentsLost()) {
			bs.show();
		}
	}
	/* Delete this code after testing */
	public void showTestImage() {
		_gd.getFullScreenWindow().setBackground(Color.black);
		Graphics2D canvas = (Graphics2D)getDblBuffer().getDrawGraphics();
		Dimension d = _gd.getFullScreenWindow().getSize();
		canvas.clearRect(0, 0, d.width, d.height);
		canvas.setColor(Color.white);
		InputState is = Game.inputManager().getState();
		if (is.pressed("p1left")) {
			_v.offsetCoords(-5,  0);
			_v.setRotation(180);
		}
		else if (is.pressed("p1right")) {
			_v.offsetCoords(5, 0);
			_v.setRotation(0);
		}
		else if (is.pressed("p1up")) {
			_v.offsetCoords(0, -5);
			_v.setRotation(90);
		}
		else if (is.pressed("p1down")) {
			_v.offsetCoords(0, 5);
			_v.setRotation(270);
		}
		
		_v.draw(canvas);
		_v2.draw(canvas);
		if (_v.collides(_v2)) {
			canvas.drawString("COLLIDES", 500, 400);
			System.out.println("COLLISION");
		}
		_v.setRotation(_v.getRotation()+ 15);
		canvas.dispose();
		updateGraphics();
	}
	public Window getWindow() {
		return _gd.getFullScreenWindow();
	}

	//Private methods
	private BufferStrategy getDblBuffer() {
		Window win;
		if ((win = _gd.getFullScreenWindow()) != null) {
			return win.getBufferStrategy();	
		}
		return null;
	}
	private boolean modesEqual(DisplayMode m1, DisplayMode m2) {
		if (m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI  &&
				m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI &&
				m1.getBitDepth() != m2.getBitDepth()) {
			return false;
		}
		if (m1.getHeight() != m2.getHeight() ||
				m1.getWidth() != m2.getWidth()) {
			return false;
		}
		return true;
	}
}
