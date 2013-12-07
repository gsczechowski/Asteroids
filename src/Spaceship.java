import java.awt.Dimension;

public class Spaceship extends Sprite {
	// Identification for player numbers and enemy spacecraft
	private int _ID;
	protected int lives;
	private boolean _lastShootState;
	protected boolean player;
	public Spaceship(String filepath, int ID) {
		super(filepath);
		_ID = ID;
		_lastShootState = false;
		lives = 3;
		player = true;
	}
	public int getID() {
		return _ID;
	}
	
	public boolean isPlayer(){
		return player;
	}
	
	public int getLives(){
		return lives;
	}
	
	public void loseLife(){
		lives--;
	}
	
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize) {
		if (input.pressed("p" + _ID + "up")) {
			_velocity = _velocity.add(Vector.unitAtAngle(_rotation).scalarMul(.075));
			_velocity.clamp(_maxVelocity);
		}
		if (input.pressed("p" + _ID + "down")) {
			_velocity = _velocity.sub(Vector.unitAtAngle(_rotation).scalarMul(.075));
			_velocity.clamp(_maxVelocity);
		}
		if (input.pressed("p" + _ID + "right")) {
			_rotation += 2.0;
		}
		if (input.pressed("p" + _ID + "left")) {
			_rotation -= 2.0;
			//System.out.println(input.toString());
		}
		if (Game.settings.gravityEnabled()) {
			_velocity = Game.resources.gravity.enactGravity(_coords, _velocity, elapsedNanoTime);
		}
		offsetCoords(_velocity.x, _velocity.y);
		if (input.pressed("p" + _ID + "shoot") && _lastShootState == false) {
			_lastShootState = true;
			Game.resources.addBullet(this._coords, this._rotation, this._velocity.abs(), this);
		}
		else if(!input.pressed("p" + _ID + "shoot")) {
			_lastShootState = false;
		}
		checkScreenBounds(screenSize);
	}
	
	


}
