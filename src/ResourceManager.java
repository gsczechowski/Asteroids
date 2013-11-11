import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class ResourceManager {
	private Spaceship _player1;
	private Spaceship _player2;
	private boolean _multiplayerOn = false;
	private ArrayList<Asteroid> _asteroids;
	private ArrayList<Bullet> _bullets;
	private ArrayList<Sprite> _deletion;
	private ArrayList<Pair<Sprite,String>> _additions;
	
	public ResourceManager() {
		_player1 = new Spaceship("images//spaceship.png", 1);
		_player2 = new Spaceship("images//spaceship2.png", 2);
		_asteroids = new ArrayList<Asteroid> ();
		_bullets = new ArrayList<Bullet>();
		_deletion = new ArrayList<Sprite>();
		_additions = new ArrayList<Pair<Sprite,String>> ();
	}
	public Spaceship getP1() {
		return _player1;
	}
	public Spaceship getP2() {
		return _player2;
	}
	public void addBullet(Vector coords, double angle, double sourceSpeed, Spaceship owner) {
		_additions.add(new Pair<Sprite,String> (new Bullet(coords, angle,sourceSpeed, owner.getID()), "bullet"));
	}
	public void addAsteroid(Vector coords, double angle, int size) {
		_additions.add(new Pair<Sprite,String> (new Asteroid(size,coords,angle), "asteroid"));
	}
	public void delete(Sprite obj) {
		_deletion.add(obj);
	}
	private void finalizeChanges() {
		for (Sprite s: _deletion) {
			if (_asteroids.contains(s)) {
				_asteroids.remove(s);
			}
			else if (_bullets.contains(s)) {
				_bullets.remove(s);
			}
		}
		_deletion.clear();
		for (Pair<Sprite,String> p: _additions) {
			if (p.getSecond().equals("asteroid")) {
				_asteroids.add((Asteroid)p.getFirst());
			}
			else if (p.getSecond().equals("bullet")) {
				_bullets.add((Bullet) p.getFirst());
			}
		}
		_additions.clear();
	}
	public void update(InputState input, long elapsedNanoTime, Dimension screenSize) {
		_player1.update(input, elapsedNanoTime, screenSize);
		if (_multiplayerOn) {_player2.update(input, elapsedNanoTime, screenSize);}
		for (Bullet b: _bullets) {
			b.update(input,elapsedNanoTime, screenSize);
		}
		for (Asteroid a: _asteroids) {
			a.update(input,  elapsedNanoTime, screenSize);
		}
		// Don't handle collisions for elements that are already deleted
		finalizeChanges();
		handleCollisions();
	}
	
	private void handleCollisions() {
		// First check if any bullets collided with any asteroids
		for (Bullet b: _bullets) {
			for (Asteroid a: _asteroids) {
				if (b.collides(a)) {
					delete(b);
					a.split();
				}
			}
		}
		finalizeChanges();
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
