import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class SpinShip extends Spaceship {
	
	private int cycles = 0;
	
	public SpinShip(String filepath, int ID) {
		super(filepath, ID);
		lives = 1;
		player = false;
	}
	
	@Override
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize){
		_rotation += 20;
		cycles++;
		if(cycles > 5){
			cycles = 0;
			Game.resources.addBullet(this._coords, this._rotation, this._velocity.abs(), this);
		}
		checkScreenBounds(screenSize);
	}

}
