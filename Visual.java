import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Visual {

	//Member Declarations
	private String _imageFilepath;
	private BufferedImage _bufImage;
	private Point _coords;
	private double _scaleFactor;
	private Dimension _bounds;
	private int _rotation;

	// Constructors
	public Visual () {
		_imageFilepath = "";
		_bufImage = null;
		_coords = new Point(0,0);
		_scaleFactor = 1.0;
		_bounds = new Dimension(0,0);
	}

	public Visual(String filepath) {
		this();
		_imageFilepath = filepath;

	}
	// Public Accessors/Setters
	public BufferedImage getImage() {
		return _bufImage;
	}
	public double getScale() {
		return _scaleFactor;
	}

	public Point getCoords() {
		return _coords;
	}

	public Dimension getBounds() {
		if (_bufImage != null) {
			return _bounds;
		}else{
			return new Dimension(-1,-1);
		}
	}

	public int getRotation() {
		return _rotation;
	}

	public Dimension getScaledImageSize() {
		if (_bufImage != null) {
			return new Dimension((int)(_bufImage.getWidth() * _scaleFactor),(int)( _bufImage.getHeight() * _scaleFactor));
		}else {
			return new Dimension(-1,-1);
		}
	}
	public void setCoords(Point coords) {
		_coords = coords;
	}
	public void setCoords(int x, int y) {
		_coords = new Point(x,y);
	}
	public void setScale(double scaleFactor) {
		_scaleFactor = scaleFactor;
		calculateBounds();
	}

	/**
	 * Sets the rotation value for rendering the Visual object. Clamps the values between 0 and 359.
	 * @param rotation
	 * Rotation in degrees.  0 degrees is right facing, 90 is up, 180 is left, and 270 is down.
	 */
	public void setRotation (int rotation) {
		_rotation = rotation;
		while (rotation < 0) {
			rotation += 360;
		}
		while (rotation > 359 ) {
			rotation -= 360;
		}
	}
	// Public Methods
	/**
	 * Offsets the current coordinates by both an x and y offset.
	 */
	public void offsetCoords(int x_offset, int y_offset) {
		_coords = new Point(_coords.x + x_offset, _coords.y + y_offset);
	}

	/**
	 * If the buffered image has been loaded into the Visual object, and the bounding
	 * type has been selected, then the bounding dimensions are changed based on the width and height
	 * of the scaled buffered image.
	 */
	public void calculateBounds() {
		if (_bufImage != null) {
			// Both the width and the height are the same
			// Calculates half the diagonal of the rectangle, this is the bounding circle diameter
			_bounds.width = (int)(_scaleFactor * (Math.max(_bufImage.getWidth(), _bufImage.getHeight())) );
			_bounds.height = _bounds.width;
		}
	}
	/**
	 * Loads the image at the currently held filepath into the Visual's buffered image.
	 * @throws IOException
	 * If there is an error loading the file, an IOException is thrown.
	 */
	public void loadImage() throws IOException {
		if (_imageFilepath != "") {
			try {
				_bufImage = ImageIO.read(new File(_imageFilepath).toURI().toURL());
				this.calculateBounds();
			}catch (Exception e) {throw e;}
		}
	}

	/**
	 * Draws the Visual object on the canvas taking into consideration its scale factor, rotation,
	 * and buffered image.
	 */
	public void draw(Graphics2D canvas) {
		AffineTransform atrans = new AffineTransform();
		atrans.rotate(Math.toRadians(_rotation), _coords.x, _coords.y);
		Dimension iSize = getScaledImageSize();
		atrans.translate(_coords.x-iSize.width / 2, _coords.y-iSize.height / 2);
		atrans.scale(_scaleFactor,_scaleFactor);
		canvas.drawImage(_bufImage, atrans, null);
	}
	public void draw(Graphics2D canvas, boolean debugMode) {
		draw(canvas);
		if (debugMode) {
			canvas.drawOval(_coords.x - _bounds.width / 2, _coords.y - _bounds.height / 2, _bounds.width, _bounds.height);
		}
	}
	public boolean collides(Visual obj) {
		// Perform basic circle collision detection
		Point o2coord = obj.getCoords();
		double dist = Math.sqrt(Math.pow(o2coord.x - this._coords.x, 2.0) + Math.pow(o2coord.y - this._coords.y, 2.0));
		if (dist < this._bounds.width/2 + obj.getBounds().width/2) {
			return true;
		} else {
			return false;
		}

	}
}
