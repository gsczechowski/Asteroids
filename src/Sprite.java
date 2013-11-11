import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * Sprite contains support for velocity and animations for simple horizontal 2D tilesets only.
 * @author Brad
 *
 */
public class Sprite extends Visual {
	protected Vector _velocity;
	protected double _maxVelocity;
	protected int _frameWidth;
	protected int _frameHeight;
	protected int _maxFrame;
	protected int _frame = 0;
	protected boolean _isAnimated;
	protected boolean _loop;
	protected int _FPS;
	protected final long _nano = 1000000000;
	protected long _elapsedTime = 0;
	
	public Sprite(String filepath) {
		super(filepath);
		try {
			loadImage();
			_frameWidth = _bufImage.getWidth();
			_frameHeight = _bufImage.getHeight();
		} catch (IOException e) {System.out.println("Error opening image file");}
		_loop = false;
		_isAnimated = false;
		_FPS = 1;
		_frame = 0;
		_maxFrame = 0;
		calculateBounds();
		_velocity = new Vector();
	}
	public Sprite(String filepath, int frameWidth, int FPS, boolean looping) {
		super(filepath);
		try {
			loadImage();
			_frameHeight = _bufImage.getHeight();
			_maxFrame = _bufImage.getWidth() / frameWidth - 1;
		}catch (IOException e) {System.out.println("Error opening image file");}
		_loop = looping;
		_isAnimated = true;
		_FPS = FPS;
		_frameWidth = frameWidth;
		calculateBounds();
		_velocity = new Vector();
	}
	
	public void setMaxVelocity(double max) {
		_maxVelocity = max;
	}
	public void setVelocity(Vector v) {
		_velocity = v;
	}
	public Dimension getScaledImageSize() {
		if (_bufImage != null) {
			return new Dimension((int)(_frameWidth  * _scaleFactor),(int)( _bufImage.getHeight() * _scaleFactor));
		}else {
			return new Dimension(-1,-1);
			} 
	}
	protected void updateAnim(long elapsedNanoTime) {
		_elapsedTime += elapsedNanoTime;
		while (_elapsedTime > _nano / _FPS) {
			_frame++;
			_elapsedTime -=_nano/_FPS;
			if (_frame > _maxFrame){
				if (_loop) {
					_frame = _frame - _maxFrame;
				}else {
					_frame = _maxFrame;
				}
			}
		}
	}
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize) {
		updateAnim(elapsedNanoTime);
	}
	protected void checkScreenBounds(Dimension screenSize) {
		if (_coords.x < 0) {
			_coords.x = screenSize.width;
		}
		else if (_coords.x > screenSize.width) {
			_coords.x = 0;
		}
		
		if (_coords.y < 0) {
			_coords.y = screenSize.height;
		}
		else if (_coords.y > screenSize.height){
			_coords.y = 0;
		}
	}
	public void draw(Graphics2D canvas) {
		AffineTransform atrans = new AffineTransform();
		atrans.rotate(Math.toRadians(_rotation), _coords.x, _coords.y);
		Dimension iSize = getScaledImageSize();
		atrans.translate(_coords.x-iSize.width / 2, _coords.y-iSize.height / 2);
		atrans.scale(_scaleFactor,_scaleFactor);
		//canvas.drawImage(_bufImage, atrans, null);
		canvas.drawImage(_bufImage.getSubimage(_frameWidth * _frame, 0, _frameWidth, _frameHeight), atrans, null);
	}
	public void calculateBounds() {
		if (_bufImage != null) {
			// Both the width and the height are the same
			// Calculates half the diagonal of the rectangle, this is the bounding circle diameter
			_bounds.width = (int)(_scaleFactor * (Math.max(_frameWidth, _frameHeight) ));
			_bounds.height = _bounds.width;
		}
	}
}
