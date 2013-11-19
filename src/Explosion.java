import java.awt.Dimension;


public class Explosion extends Sprite {

	public Explosion(Vector coords) {
		super("images//explosion320x240.png", 320,60, false);
		this.setCoords(coords);
	}
	
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize) {
		updateAnim(10000000);
		if (_frame == _maxFrame) {
			Game.resources.delete(this);
		}
	}
	
}
