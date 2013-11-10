
public class Bullet extends Sprite {
	private int _owner;
	public Bullet(Vector coords, double angle, int owner) {
		super("images//bullet.png");
		this._coords = coords;
		this._rotation = angle;
		this._owner = owner;
		this._velocity = Vector.unitAtAngle(angle).scalarMul(5);
	}
	public void update(InputState input, long elapsedNanoTime) {
		updateAnim(elapsedNanoTime);
		this.offsetCoords(_velocity.x , _velocity.y);
	}
	public int getOwner() {
		return _owner;
	}
}
