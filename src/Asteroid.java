import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;


public class Asteroid extends Sprite {
	private int _size; // Smallest size is 0, largest is 2
	private static final double _MAXVEL = 2.0;
	/**
	 * Generates a new asteroid.
	 * @param size
	 * The size of the asteroid should range from 0 to 2, 2 being the largest.
	 * @param coord
	 * The coordinate parameter controls where the asteroid spawns.
	 * @param direction
	 * The direction should be the angle of velocity the asteroid should spawn with.
	 * The asteroid object will automatically randomly generate its velocity.
	 */
	public Asteroid (int size, Vector coord, double direction) {
		// Generate random asteroid between 1 and 3
		super("asteroid" + (new Random().nextInt(3) + 1));
		_coords = coord;
		_size = size;
		this.setScale(((double)_size+ 1)/3.0);
		_velocity = Vector.unitAtAngle(direction).scalarMul(new Random().nextDouble() * (_MAXVEL- 0.5) +0.5);
	}
	public void split() {
		Game.resources.delete(this);
		if (_size > 0) {
			//Generate two more asteroids if necessary
			Game.resources.addAsteroid(this._coords, new Random().nextDouble() * 359.0, _size - 1);
			Game.resources.addAsteroid(this._coords, new Random().nextDouble() * 359.0, _size - 1);
		}
	}
	
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize) {
		offsetCoords(_velocity.x, _velocity.y);
		this.setRotation(this.getRotation() + 2);
		checkScreenBounds(screenSize);
	}

	public void draw(Graphics2D canvas) {
		if (!Game.settings.primitiveRendering()) {
			AffineTransform atrans = new AffineTransform();
			if (Game.settings.rotationEnabled()) {
				atrans.rotate(Math.toRadians(_rotation), _coords.x, _coords.y);
			}
			Dimension iSize = getScaledImageSize();
			atrans.translate(_coords.x-iSize.width / 2, _coords.y-iSize.height / 2);
			atrans.scale(_scaleFactor,_scaleFactor);
			canvas.drawImage(_bufImage.getSubimage(_frameWidth * _frame, 0, _frameWidth, _frameHeight), atrans, null);
		}else {
			this.drawBounds(canvas); 
		}
	}
}
