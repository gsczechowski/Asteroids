import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Visual {

	//Member Declarations
	protected String _imageFilepath;
	protected BufferedImage _bufImage;
	protected Vector _coords;
	protected double _scaleFactor;
	protected Dimension _bounds;
	protected double _rotation;

	// Constructors

	public Visual(String filepath) {
		_imageFilepath = "";
		_bufImage = null;
		_coords = new Vector(0,0);
		_scaleFactor = 1.0;
		_bounds = new Dimension(0,0);
		_imageFilepath = filepath;

	}
	// Public Accessors/Setters
	public BufferedImage getImage() {
		return _bufImage;
	}
	public double getScale() {
		return _scaleFactor;
	}

	public Vector getCoords() {
		return _coords;
	}

	public Dimension getBounds() {
		if (_bufImage != null) {
			return _bounds;
		}else{
			return new Dimension(-1,-1);
		}
	}

	public double getRotation() {
		return _rotation;
	}

	public Dimension getScaledImageSize() {
		if (_bufImage != null) {
			return new Dimension((int)(_bufImage.getWidth() * _scaleFactor),(int)( _bufImage.getHeight() * _scaleFactor));
		}else {
			return new Dimension(-1,-1);
		}
	}
	public void setCoords(Vector coords) {
		_coords = coords;
	}
	public void setCoords(double x, double y) {
		_coords = new Vector(x,y);
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
	public void setRotation (double rotation) {
		_rotation = rotation;
		while (rotation < 0) {
			rotation += 360;
		}
		while (rotation >= 360 ) {
			rotation -= 360;
		}
	}
	// Public Methods
	/**
	 * Offsets the current coordinates by both an x and y offset.
	 */
	public void offsetCoords(double x_offset, double y_offset) {
		_coords = _coords.add(new Vector(x_offset,y_offset));
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
	public void loadImage() {
		/*if (_imageFilepath != "") {
			try {
				_bufImage = ImageIO.read(new File(_imageFilepath).toURI().toURL());
				this.calculateBounds();
			}catch (Exception e) {throw e;}
		}*/
		_bufImage = Game.resources.getImage(_imageFilepath);
		if (_bufImage == null) {
			System.out.println("Error fetching image from resource bin - " + _imageFilepath);
			return;
		}
		this.calculateBounds();

	}

	/**
	 * Draws the Visual object on the canvas taking into consideration its scale factor, rotation,
	 * and buffered image.
	 */
	public void draw(Graphics2D canvas) {
		if (!Game.settings.primitiveRendering()) {
			AffineTransform atrans = new AffineTransform();
			atrans.rotate(Math.toRadians(_rotation), _coords.x, _coords.y);
			Dimension iSize = getScaledImageSize();
			atrans.translate(_coords.x-iSize.width / 2, _coords.y-iSize.height / 2);
			atrans.scale(_scaleFactor,_scaleFactor);
			canvas.drawImage(_bufImage, atrans, null);
		} else {
			drawBounds(canvas);
		}
	}
	public void draw(Graphics2D canvas, boolean debugMode) {
		draw(canvas);
		if (debugMode) {
			drawBounds(canvas);
		}
	}
	
	public void drawBounds(Graphics2D canvas) {
		canvas.drawOval((int)(_coords.x - _bounds.width / 2), (int)(_coords.y - _bounds.height / 2), _bounds.width, _bounds.height);
	}
	public boolean collides(Visual obj) {
		// Perform basic circle collision detection
		Vector o2coord = obj.getCoords();
		double dist = Math.sqrt(Math.pow(o2coord.x - this._coords.x, 2.0) + Math.pow(o2coord.y - this._coords.y, 2.0));
		if (dist < this._bounds.width/2 + obj.getBounds().width/2) {
			return true;
		} else {
			return false;
		}

	}
}
