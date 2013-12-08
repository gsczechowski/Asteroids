import java.awt.Dimension;
import java.lang.NullPointerException;


public class AlienShip extends Spaceship {
	
	private Spaceship target;
	private int _cycles = 0;
	private int bullets = 4;
	private boolean reloading = false;
	private long startReload;
	private long lastFire;
	
	public AlienShip(String filepath, int ID) {
		super(filepath, ID);
		loadImage();
		_coords = new Vector(800, 800);
		lives = 3;
		player = false;
		//System.out.println(_velocity);
		_maxVelocity = 5;
		//target = Game.resources.getRandomPlayer();
	}
	
	@Override
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize){
		if(target == null){
			target = Game.resources.getRandomPlayer();
		}
		double angle = new Vector(target.getCoords().x - this._coords.x, target.getCoords().y - this._coords.y).angle();	
		if(angle < 0){
			angle += 360;
		}
		if(angle == 0){
			System.out.println("Lock");
		}else{
			if(_rotation < angle){
				setRotation(_rotation += (int) Game.score.getLevel() * 0.5 + 1);
			}else if(_rotation  > angle){
				setRotation(_rotation -= (int) Game.score.getLevel() * 0.5 + 1);
			}
		}
		int random = (int) (Math.random() * 6.0);
		_velocity = _velocity.add(Vector.unitAtAngle(_rotation).scalarMul(.075));
		_velocity.clamp(_maxVelocity);
		if(random == 1){
			_velocity = _velocity.add(Vector.unitAtAngle(_rotation).scalarMul(.075));
			_velocity.clamp(_maxVelocity);
		}else if(random == 2){
			_velocity = _velocity.sub(Vector.unitAtAngle(_rotation).scalarMul(.075));
			_velocity.clamp(_maxVelocity);
		}
		offsetCoords(_velocity.x, _velocity.y);
		checkScreenBounds(screenSize);

		//bullet firing
		if(Math.abs(angle - _rotation) < 10){
			if(bullets == -1 && System.currentTimeMillis() - startReload > (10000/((int) Game.score.getLevel() * 0.25 + 1))){
				bullets = 4;
			}
			if(bullets > 0 && System.currentTimeMillis() - lastFire > 1000/((int) Game.score.getLevel() * 0.25 + 1)){
				Game.resources.addBullet(this._coords, this._rotation, this._velocity.abs(), this);
				bullets--;
				lastFire = System.currentTimeMillis();
			}
			if(bullets == 0){
				bullets--;
				startReload = System.currentTimeMillis();
			}
		}
	}
}
