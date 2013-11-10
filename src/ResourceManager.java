import java.awt.Graphics2D;
import java.util.ArrayList;


public class ResourceManager {
	private Spaceship _player1;
	private Spaceship _player2;
	private boolean _multiplayerOn = false;
	private ArrayList<Asteroid> _asteroids;
	private ArrayList<Bullet> _bullets;
	
	public ResourceManager() {
		_player1 = new Spaceship("images//spaceship.png", 1);
		_player2 = new Spaceship("images//spaceship2.png", 2);
		_asteroids = new ArrayList<Asteroid> ();
		_bullets = new ArrayList<Bullet>();
	}
	public Spaceship getP1() {
		return _player1;
	}
	public Spaceship getP2() {
		return _player2;
	}
	
	public void addBullet(Vector coords, double angle, Spaceship owner) {
		Bullet newbullet = new Bullet(coords, angle, owner.getID());
		_bullets.add(newbullet);
	}
	public void deleteBullet(Bullet b) {
		_bullets.remove(b);
	}
	public void update(InputState input, long elapsedNanoTime) {
		_player1.update(input, elapsedNanoTime);
		if (_multiplayerOn) {_player2.update(input, elapsedNanoTime);}
		for (Bullet b: _bullets) {
			b.update(input,elapsedNanoTime);
		}
		for (Asteroid a: _asteroids) {
			a.update(input,  elapsedNanoTime);
		}
	}
	public void draw(Graphics2D canvas) {
		draw(canvas, false);
	}
	
	public void draw(Graphics2D canvas, boolean debugMode) {
		// Order of these calls influences ordering on the screen
		for (Bullet b: _bullets) {
			b.draw(canvas, debugMode);
		}
		_player1.draw(canvas, debugMode);
		if (_multiplayerOn) {_player2.draw(canvas, debugMode);}
		for (Asteroid a : _asteroids) {
			a.draw(canvas, debugMode);
		}
	}
}
