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

	//Constructors
	public ScreenManager() {
		_gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		_v = new Visual("C:\\Users\\Brad\\Documents\\Purdue\\Junior\\Eclipse\\Asteroids\\Asteroids\\bin\\test.png", Visual.Bounding.Rectangle);
		try {
			_v.loadImage();
			_v.setCoords(300,300);
			_v.setScale(.5);
			_v.calculateBounds();
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
		_gd.getFullScreenWindow().setBackground(Color.blue);
		Graphics2D canvas = (Graphics2D)getDblBuffer().getDrawGraphics();
		Dimension d = _gd.getFullScreenWindow().getSize();
		canvas.clearRect(0, 0, d.width, d.height);
		canvas.drawString(d.width + ", " + d.height, 500, 400);
		canvas.setBackground(Color.black);
		_v.draw(canvas, true);
		_v.setRotation(_v.getRotation()+ 15);
		_v.offsetCoords(3, 3);
		canvas.dispose();
		updateGraphics();
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
