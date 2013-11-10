import java.util.Random;


public class Asteroid extends Sprite {
	public Asteroid () {
		// Generate random asteroid between 1 and 3
		super("images//asteroid" + new Random().nextInt(3) + 1);
	}
}
