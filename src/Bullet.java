import java.awt.Dimension;


public class Bullet extends Sprite {
	private int _owner;
	private double _distance = 0;
	private static final double _MAXDIST = 1920;
	public Bullet(Vector coords, double angle, double sourceSpeed, int owner) {
		super("images//bullet.png");
		this._coords = coords;
		this._rotation = angle;
		this._owner = owner;
		this._velocity = Vector.unitAtAngle(angle).scalarMul(4 + sourceSpeed);
	}
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize) {
		updateAnim(elapsedNanoTime);
		this.offsetCoords(_velocity.x , _velocity.y);
		_distance+= _velocity.abs();
		if (_distance > _MAXDIST) {
			Game.resources.delete(this);
		}
		checkScreenBounds(screenSize);
	}
	public int getOwner() {
		return _owner;
	}
}
