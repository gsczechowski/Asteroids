import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class ResourceManager {
	private Spaceship _player1;
	private Spaceship _player2;
	private boolean _multiplayerOn = false;
	private HashMap<String, BufferedImage> _images;
	private ArrayList<Asteroid> _asteroids;
	private ArrayList<Bullet> _bullets;
	private ArrayList<Explosion> _explosions;
	private ArrayList<Sprite> _deletion;
	private ArrayList<Pair<Sprite,String>> _additions;
	public boolean sendLevelComplete = false;
	
	public ResourceManager() {
		initializeImages();
		//_player1 = new Spaceship("spaceship1", 1);
		//_player2 = new Spaceship("spaceship2", 2);
		_asteroids = new ArrayList<Asteroid> ();
		_bullets = new ArrayList<Bullet>();
		_deletion = new ArrayList<Sprite>();
		_additions = new ArrayList<Pair<Sprite,String>> ();
		_explosions = new ArrayList<Explosion> ();
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
	
	public void addExplosion(Vector coords) {
		_additions.add(new Pair<Sprite, String> (new Explosion(coords), "explosion"));
	}
	
	public void spawnAsteroid(Dimension screenSize) {
		int side = new Random().nextInt(4);
		if (side == 0) { // Generate asteroid on the left side of the screen
			addAsteroid(new Vector(1, new Random().nextInt(screenSize.height)), new Random().nextDouble() * 90 - 45, 2);
		}else if (side == 1) { // Generate an asteroid on the top of the screen
			addAsteroid(new Vector(new Random().nextInt(screenSize.width), 1), new Random().nextDouble() * 90 + 45, 2);
		}else if (side == 2) { // Generate an asteroid on the right side of the screen
			addAsteroid(new Vector(screenSize.width - 1, new Random().nextInt(screenSize.height)), 
					new Random().nextDouble() * 90 + 135, 2);
		}else {
			addAsteroid(new Vector(new Random().nextInt(screenSize.width), screenSize.height - 1), 
					new Random().nextDouble() * 90 + 215, 2);
		}
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
			else if (_explosions.contains(s)) {
				_explosions.remove(s);
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
			else if (p.getSecond().equals("explosion")) {
				_explosions.add((Explosion) p.getFirst());
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
		for (Explosion e: _explosions) {
			e.update(input, elapsedNanoTime, screenSize);
		}
		// Don't handle collisions for elements that are already deleted
		finalizeChanges();
		handleCollisions();
		if (sendLevelComplete && _asteroids.size() == 0) {
			sendLevelComplete = false;
			Game.score.increaseLevel();
		}
	}
	
	private void handleCollisions() {
		// First check if any bullets collided with any asteroids
		for (Bullet b: _bullets) {
			for (Asteroid a: _asteroids) {
				if (b.collides(a)) {
					addExplosion(a.getCoords());
					delete(b);
					a.split();
					Game.score.asteroidHit();
				}
			}
			//TODO: add ///////////////////////////////////////////////
			if(b.collides(_player1) && b.getOwner() != _player1.getID()){
				_player1.loseLife();
				System.out.println("Player 1 loses life\n");
				delete(b);
				Dimension area = Game.screen.getWindowSize();
				Vector newCoords = new Vector(Math.random() * area.width, Math.random() * area.height);
				while(!areaClear(newCoords)){
					newCoords = new Vector(Math.random() * area.width, Math.random() * area.height);
				}
				_player1.setCoords(newCoords);
			}
			///////////////////////////////////////////////////////////
		}
		//TODO: add//////////////////////////////////////////////////////
		for(Asteroid a: _asteroids){
			if(_player1.collides(a)){
				_player1.loseLife();
				System.out.println("Player 1 loses life\n");
				a.split();
				Dimension area = Game.screen.getWindowSize();
				Vector newCoords = new Vector((int) (Math.random() * area.width), (int) (Math.random() * area.height));
				while(!areaClear(newCoords)){
					newCoords = new Vector((int) (Math.random() * area.width),(int) (Math.random() * area.height));
				}
				_player1.setCoords(newCoords);
				
			}
		}
		if(_player1.getLives() == 0){
			System.exit(0);
		}
		////////////////////////////////////////////
		finalizeChanges();
	}
	
	private boolean areaClear(Vector coords){
		System.out.println("Trying: x:" + coords.x + " y:" + coords.y);
		Vector Coord;
		for(Bullet b : _bullets){
			Coord = b.getCoords();
			if(Math.abs(Coord.x - coords.x) < 50 || Math.abs(Coord.y- coords.y) < 50){
				return false;
			}	
		}
		for(Asteroid a : _asteroids){
			Coord = a.getCoords();
			if(Math.abs(Coord.x - coords.x) < 50 || Math.abs(Coord.y- coords.y) < 50){
				return false;
			}
		}
		//TODO:: check other ships
		return true;
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
		for (Explosion e: _explosions) {
			e.draw(canvas, debugMode);
		}
	}
	
	public BufferedImage getImage(String ref) {
		return _images.get(ref);
	}
	
	private void initializeImages() {
		_images = new HashMap<String, BufferedImage> ();
		try {
			_images.put("spaceship1", ImageIO.read(new File("images//spaceship.png").toURI().toURL()));
			_images.put("spaceship2", ImageIO.read(new File("images//spaceship2.png").toURI().toURL()));
			_images.put("asteroid1", ImageIO.read(new File("images//asteroid1.png").toURI().toURL()));
			_images.put("asteroid2", ImageIO.read(new File("images//asteroid2.png").toURI().toURL()));
			_images.put("asteroid3", ImageIO.read(new File("images//asteroid3.png").toURI().toURL()));
			_images.put("bullet", ImageIO.read(new File("images//bullet.png").toURI().toURL()));
			_images.put("explosion320x240", ImageIO.read(new File("images//explosion320x240.png").toURI().toURL()));
		} catch (Exception e) {}
	}
	
	public void initializeShips() {
		_player1 = new Spaceship("spaceship1", 1);
		_player2 = new Spaceship("spaceship2", 2);
	}
}
