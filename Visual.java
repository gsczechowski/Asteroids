import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class Visual {
	// Enumerations
	public static enum Bounding {
		Rectangle,
		Circle
	}
	
	//Member Declarations
	private String _imageFilepath;
	private BufferedImage _bufImage;
	private Point _coords;
	private double _scaleFactor;
	private Bounding _boundType;
	private Dimension _bounds;
	private int _rotation;
	
	// Constructors
	public Visual () {
		_imageFilepath = "";
		_bufImage = null;
		_coords = new Point(0,0);
		_scaleFactor = 1.0;
		_boundType = Bounding.Circle;
		_bounds = new Dimension(0,0);
	}
	
	public Visual(String filepath) {
		this();
		_imageFilepath = filepath;
		
	}
	
	public Visual (String filepath, Bounding bounds) {
		this(filepath);
		_boundType = bounds;
	}
	
	// Public Accessors/Setters
	public BufferedImage getImage() {
		return _bufImage;
	}
	public void setFilepath(String filepath) {
		_imageFilepath = filepath;
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
	
	public double getScale() {
		return _scaleFactor;
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
	public Dimension getScaledImageSize() {
		if (_bufImage != null) {
			return new Dimension((int)(_bufImage.getWidth() * _scaleFactor),(int)( _bufImage.getHeight() * _scaleFactor));
		}else {
			return new Dimension(-1,-1);
		}
	}
	public int getRotation() {
		return _rotation;
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
			if (_boundType == Bounding.Circle){
				// Both the width and the height are the same
				// Calculates half the diagonal of the rectangle, this is the bounding circle diameter
				_bounds.width = (int)(_scaleFactor * (_bufImage.getWidth() ^2 + _bufImage.getHeight() ^2) );
				_bounds.height = _bounds.width;
			}else if (_boundType == Bounding.Rectangle) {
				//Simply equal to the buffered image's width and height;
				_bounds.width = (int)(_scaleFactor * _bufImage.getWidth());
				_bounds.height = (int)(_scaleFactor * _bufImage.getHeight());
			}
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
			AffineTransform atrans = new AffineTransform();
			atrans.rotate(Math.toRadians(_rotation), _coords.x, _coords.y);
			canvas.setTransform(atrans);
			if (_boundType == Bounding.Circle){
				canvas.drawOval(_coords.x - _bounds.width / 2, _coords.y - _bounds.height / 2, _bounds.width, _bounds.height);;
			} else if (_boundType == Bounding.Rectangle) {
				canvas.drawRect(_coords.x - _bounds.width / 2, _coords.y - _bounds.height / 2, _bounds.width, _bounds.height);
			}
			atrans.setToIdentity();
			canvas.setTransform(atrans);
		}
	}
}
