import java.awt.Dimension;


public class RogueShip extends Spaceship {

	
	public RogueShip(String filepath, int ID){
		super(filepath, ID);
		loadImage();
		_coords = new Vector(600, 100);
		lives = 1;
		player = false;
		System.out.println(_velocity);
		_maxVelocity = 4;
	}
	
	@Override
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize){
		int random = (int) (Math.random() * 60);
		if(random < 5){
			setRotation(_rotation += Math.random() * 5);
		}else if(random > 55){
			setRotation(_rotation -= Math.random() * 5);
		}
		offsetCoords(_velocity.x, _velocity.y);
		random = (int) (Math.random() * 20);
		if(random < 10){
			//System.out.println("Speeding up");
			_velocity = _velocity.add(Vector.unitAtAngle(_rotation).scalarMul(.075));
			_velocity.clamp(_maxVelocity);
		}else if(random > 13){
			_velocity = _velocity.sub(Vector.unitAtAngle(_rotation).scalarMul(.075));
			_velocity.clamp(_maxVelocity);
		}
		random = (int) (Math.random() * 100);
		if(random < 2){
			Game.resources.addBullet(this._coords, this._rotation, this._velocity.abs(), this);
		}
		checkScreenBounds(screenSize);
	}
}
