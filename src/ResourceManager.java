import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;



import javax.imageio.ImageIO;


public class ResourceManager {
	private Spaceship _player1;
	private Spaceship _player2;
	private ArrayList<Spaceship> _autoShips = new ArrayList<Spaceship>();
	private boolean _multiplayerOn = false;
	private HashMap<String, BufferedImage> _images;
	private ArrayList<Asteroid> _asteroids;
	private ArrayList<Bullet> _bullets;
	private ArrayList<Explosion> _explosions;
	private ArrayList<Sprite> _deletion;
	private ArrayList<Pair<Sprite,String>> _additions;
	private ArrayList<MenuItem> _menuItems;
	private InputState _lastState;
	public GravityObject gravity;
	private Visual _title;
	private String status = "";
	private int pressCount = 0;
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
		_menuItems = new ArrayList<MenuItem> ();
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
		if (Game.settings.multiplayerEnabled()) {
			_player2.update(input, elapsedNanoTime, screenSize);
		}
		for (Spaceship s: _autoShips) {
			s.update(input, elapsedNanoTime, screenSize);
		}
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
		_lastState = input;
	}
	
	public void initSettings() {
		_menuItems.clear();
		int i = 0;
		for (i = 0; i< Game.settings.numSettings(); i++) {
			_menuItems.add(new MenuItem(new Point(10,200 + i * 50), i, MenuItem.MenuType.SETTING));
			_menuItems.get(i).initialize();
			_menuItems.get(i).setActions(MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.PREV_VALUE, MenuItem.MenuEvent.NEXT_VALUE,
					MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.NEXT_MENU);
		}
		_menuItems.add(new MenuItem(new Point(10,250 + i * 50), i, MenuItem.MenuType.BUTTON));
		_menuItems.get(i).setText("New Game");
		_menuItems.get(i).initialize();
		_menuItems.get(i).setActions(MenuItem.MenuEvent.PRESS, MenuItem.MenuEvent.UNPRESS, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.NEXT_MENU);
		i++;
		_menuItems.add(new MenuItem(new Point(10,250 + i * 50), i, MenuItem.MenuType.BUTTON));
		_menuItems.get(i).setText("Save Game");
		_menuItems.get(i).initialize();
		_menuItems.get(i).setActions(MenuItem.MenuEvent.PRESS, MenuItem.MenuEvent.UNPRESS, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE,
				MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.NEXT_MENU);
		i++;
		_menuItems.add(new MenuItem(new Point(10,250 + i * 50), i, MenuItem.MenuType.BUTTON));
		_menuItems.get(i).setText("Load Game");
		_menuItems.get(i).initialize();
		_menuItems.get(i).setActions(MenuItem.MenuEvent.PRESS, MenuItem.MenuEvent.UNPRESS, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE,
				MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.NEXT_MENU);
		i++;
		_menuItems.add(new MenuItem(new Point(10,250 + i * 50), i, MenuItem.MenuType.BUTTON));
		_menuItems.get(i).setText("Quit");
		_menuItems.get(i).initialize();
		_menuItems.get(i).setActions(MenuItem.MenuEvent.PRESS, MenuItem.MenuEvent.UNPRESS, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE,
				MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.NEXT_MENU);
		i++;
		_menuItems.add(new MenuItem(new Point(10,250 + i * 50), i, MenuItem.MenuType.TEXTINPUT));
		_menuItems.get(i).setActions(MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NEXT_MENU, 
				MenuItem.MenuEvent.PREV_MENU,MenuItem.MenuEvent.PREV_VALUE, MenuItem.MenuEvent.NEXT_VALUE);
		i++;
		_menuItems.add(new MenuItem(new Point(35,250 + (i-1) * 50), i, MenuItem.MenuType.TEXTINPUT));
		_menuItems.get(i).setActions(MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NEXT_MENU, 
				MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.PREV_VALUE, MenuItem.MenuEvent.NEXT_VALUE);
		i++;
		_menuItems.add(new MenuItem(new Point(60,250 + (i-2) * 50), i, MenuItem.MenuType.TEXTINPUT));
		_menuItems.get(i).setActions(MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NEXT_MENU, 
				MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.PREV_VALUE, MenuItem.MenuEvent.NEXT_VALUE);
		i++;
		
		_menuItems.get(0).setAdj(_menuItems.get(Game.settings.numSettings()+6), _menuItems.get(1));
		_menuItems.get(0).setActions(MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.PREV_VALUE, MenuItem.MenuEvent.NEXT_VALUE,
				MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.NEXT_MENU);
		
		_menuItems.get(Game.settings.numSettings()+6).setAdj(_menuItems.get(Game.settings.numSettings()+5), _menuItems.get(0));
		//_menuItems.get(Game.settings.numSettings()+3).setActions(MenuItem.MenuEvent.NONE, MenuItem.MenuEvent.PREV_VALUE, MenuItem.MenuEvent.NEXT_VALUE,
		//		MenuItem.MenuEvent.PREV_MENU, MenuItem.MenuEvent.NEXT_MENU);
		for (i = 1; i< Game.settings.numSettings() +6; i++) {
			_menuItems.get(i).setAdj(_menuItems.get(i-1), _menuItems.get(i+1));
		}
		_menuItems.get(0).select();
	}
	
	public void updateSettings(InputState input) {
		int selected = -1;
		for (int i = 0; i < Game.settings.numSettings() +7; i++) {
			if (_menuItems.get(i).buttonPressed) {
				if (_menuItems.get(i).toString() == "New Game") {
					// Start a new game
					if (pressCount == 0) status = "Press again to confirm new game. Unsaved progress will not be saved.";
					if (pressCount == 1) {
						newGame();
						status = "A new game has been started.";
					}
				} else if (_menuItems.get(i).toString() == "Save Game") {
					// Save the game
					if (pressCount == 0) status = "Press again to save game under name " + getName();
					if (pressCount == 1) {
						if (save(getName())) {
							status = "The game under name " + getName() + " has been saved successfully.";
							pressCount = 0;
						}else {
							status = "The game could not be saved.";
							pressCount = 0;
						}
					}
				} else if (_menuItems.get(i).toString() == "Load Game") {
					// Load a previous game
					if (pressCount == 0) status = "Press again to load the game for name " + getName() + ". Current progress will not be saved.";
					if (pressCount == 1) {
						if (load(getName())) {
							status = "The game under name " + getName() + " has been loaded successfully.";
							pressCount = 0;
						} else {
							status = "The game could not be loaded.";
							pressCount = 0;
						}
					}
				} else if (_menuItems.get(i).toString() == "Quit") {
					// Quit the game
					if (pressCount == 0) status = "Are you sure you want to quit? Press again to quit.";
					if (pressCount == 1) System.exit(0);
				}
				_menuItems.get(i).resetButton();
				pressCount ++;
			}
			if (_menuItems.get(i).selected()) {
				selected = i;
				break;
			}
		}
		if (selected == -1) {
			return;
		}
		if (input.pressed("p1down") && !_lastState.pressed("p1down")) {
			_menuItems.get(selected).unpress();
			_menuItems.get(selected).up();
			pressCount = 0;
			status = "";
			//_menuItems.get(selected).deselect();
			//_menuItems.get((selected+1) % Game.settings.numSettings()).select();
		}
		else if (input.pressed("p1up") && !_lastState.pressed("p1up")) {
			_menuItems.get(selected).unpress();
			_menuItems.get(selected).down();
			pressCount = 0;
			status = "";
		}
		else if (input.pressed("p1left") && !_lastState.pressed("p1left")) {
			_menuItems.get(selected).unpress();
			_menuItems.get(selected).left();
			//_menuItems.get(selected).prevValue();
		}
		else if (input.pressed("p1right") && !_lastState.pressed("p1right")) {
			_menuItems.get(selected).unpress();
			_menuItems.get(selected).right();
			//_menuItems.get(selected).nextValue();
		}
		else if (input.pressed("p1shoot")) {
			_menuItems.get(selected).press();
		}
		if (!input.pressed("p1shoot")) {
			_menuItems.get(selected).unpress();
		}
		_lastState = input;
	}
	public void drawSettings(Graphics2D canvas) {
		_title.draw(canvas);
		canvas.setColor(Color.white);
		canvas.setFont(new Font("Arial", Font.ROMAN_BASELINE, 72));
		canvas.drawString("P A U S E D", 10, 100);
		for (MenuItem m : _menuItems) {
			m.draw(canvas);
		}
		canvas.setColor(Color.white);
		canvas.setFont(new Font("Arial", Font.PLAIN, 24));
		canvas.drawString(status, 10, (int)Game.screen.getWindowSize().getHeight() - 50);
	}
	
	public String getName() {
		if (_menuItems != null) {
			int length = _menuItems.size();
			return new String("" + _menuItems.get(length - 3) + _menuItems.get(length - 2) + _menuItems.get(length -1));
		} else {
			return "";
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
				delete(b);
				Dimension area = Game.screen.getWindowSize();
				Vector newCoords = new Vector(Math.random() * area.width, Math.random() * area.height);
				while(!areaClear(newCoords)){
					newCoords = new Vector(Math.random() * area.width, Math.random() * area.height);
				}
				_player1.setCoords(newCoords);
				_player1.setVelocity(new Vector(0,0));
			}
			if (b.collides(_player2) && b.getOwner() != _player2.getID()) {
				_player2.loseLife();
				delete(b);
				Dimension area = Game.screen.getWindowSize();
				Vector newCoords = new Vector(Math.random() * area.width, Math.random() * area.height);
				while(!areaClear(newCoords)){
					newCoords = new Vector(Math.random() * area.width, Math.random() * area.height);
				}
				_player2.setCoords(newCoords);
				_player2.setVelocity(new Vector(0,0));
			}
			for(Spaceship s : _autoShips){
				if(b.collides(s) && b.getOwner() != s.getID()){
					s.loseLife();
					delete(b);
				}
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
				_player1.setVelocity(new Vector(0,0));
			}
			if(_player2.collides(a)) {
				_player2.loseLife();
				System.out.println("Player 2 loses life\n");
				a.split();
				Dimension area = Game.screen.getWindowSize();
				Vector newCoords = new Vector((int) (Math.random() * area.width), (int) (Math.random() * area.height));
				while(!areaClear(newCoords)){
					newCoords = new Vector((int) (Math.random() * area.width),(int) (Math.random() * area.height));
				}
				_player2.setCoords(newCoords);
				_player2.setVelocity(new Vector(0,0));
				
			}
		}
		if (Game.settings.gravityEnabled() && gravity.collides(_player1)) {
			_player1.loseLife();
			System.out.println("Player 1 loses life\n");				
			Dimension area = Game.screen.getWindowSize();
			Vector newCoords = new Vector((int) (Math.random() * area.width), (int) (Math.random() * area.height));
			while(!areaClear(newCoords)){
				newCoords = new Vector((int) (Math.random() * area.width),(int) (Math.random() * area.height));
			}
			_player1.setCoords(newCoords);
		}
		if(_player1.getLives() == 0){
			System.exit(0);
		}
		ArrayList<Spaceship> deletes = new ArrayList<Spaceship>();
		for(Spaceship s: _autoShips){
			if(s.getLives() == 0){
				deletes.add(s);
			}
		}
		for(Spaceship s: deletes){
			_autoShips.remove(s);
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
		if (Game.settings.debugEnabled()) {
			draw(canvas, true);
		}else{
			draw(canvas,false);
		}
	}
	
	public void draw(Graphics2D canvas, boolean debugMode) {
		// Order of these calls influences ordering on the screen
		gravity.draw(canvas,debugMode);
		for (Bullet b: _bullets) {
			b.draw(canvas, debugMode); 
		}
		_player1.draw(canvas, debugMode);
		if (Game.settings.multiplayerEnabled()) 
			_player2.draw(canvas,debugMode);
		for (Spaceship s: _autoShips) {
			s.draw(canvas, debugMode);
		}
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
			_images.put("spaceship3", ImageIO.read(new File("images//spaceship3.png").toURI().toURL()));
			_images.put("asteroid1", ImageIO.read(new File("images//asteroid1.png").toURI().toURL()));
			_images.put("asteroid2", ImageIO.read(new File("images//asteroid2.png").toURI().toURL()));
			_images.put("asteroid3", ImageIO.read(new File("images//asteroid3.png").toURI().toURL()));
			_images.put("bullet", ImageIO.read(new File("images//bullet.png").toURI().toURL()));
			_images.put("explosion320x240", ImageIO.read(new File("images//explosion320x240.png").toURI().toURL()));
			_images.put("blackhole", ImageIO.read(new File("images//blackhole.png").toURI().toURL()));
			_images.put("title", ImageIO.read(new File("images//title.png").toURI().toURL()));
			_images.put("alienship", ImageIO.read(new File("images//alienship.png").toURI().toURL()));
		} catch (Exception e) {
			System.out.println("Error loading image from file.");
		}
	}
	
	public void initializeShips() {
		_player1 = new Spaceship("spaceship1", 1);
		_player2 = new Spaceship("spaceship2", 2);
		_autoShips.add(new RogueShip("spaceship3", 3));
		_autoShips.add(new AlienShip("alienship", 4));
	}
	
	public void initGravity(Dimension screenSize) {
		gravity = new GravityObject(screenSize);
		_title = new Visual("title");
		_title.setCoords(960,780);
		_title.loadImage();
	}
	
	
	public boolean save(String fname){
		ArrayList<Spaceship> ships = new ArrayList<Spaceship>();
		ships.add(_player1);
		if(_multiplayerOn){
			ships.add(_player2);
		}
		if(_autoShips.size() != 0){
			ships.addAll(_autoShips);
		}
		try{
			File folder = new File("saves");
			if (!folder.exists()) {
				folder.mkdir();
			}
			FileOutputStream fileOut = new FileOutputStream("saves//" + fname + ".save");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(new Boolean(_multiplayerOn));
			out.writeObject(ships);
			out.writeObject(_bullets);
			out.writeObject(_asteroids);
			out.writeObject(Game.score);
			out.writeObject(Game.settings);
			out.close();
			fileOut.close();
			System.out.println("Saved to: saves//" + fname + ".save");
		}catch(IOException i){
			i.printStackTrace();
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean load(String fname){
		ArrayList<Spaceship> ships; 
		try{
			FileInputStream fileIn = new FileInputStream("saves//" + fname + ".save");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			_multiplayerOn = ((Boolean) in.readObject()).booleanValue();
			ships = (ArrayList<Spaceship>) in.readObject();
			for (Spaceship s : ships) {
				s.loadImage();
			}
			_bullets = (ArrayList<Bullet>) in.readObject();
			for (Bullet b : _bullets) {
				b.loadImage();
			}
			_asteroids = (ArrayList<Asteroid>) in.readObject();
			for (Asteroid a: _asteroids) {
				a.loadImage();
			}
			Game.score = (ScoreManager) in.readObject();
			Game.settings = (Settings) in.readObject();
			Game.score.makeTimer();
		}catch(IOException i){
			i.printStackTrace();
			return false;
		}catch(ClassNotFoundException c){
			System.out.println("Class not found");
			c.printStackTrace();
			return false;
		}
		_player1 = ships.get(0);
		ships.remove(_player1);
		if(_multiplayerOn){
			_player2 = ships.get(0);
			ships.remove(_player2);
		}
		_autoShips = ships;
		return true;
	}
	public void newGame() {
		_asteroids.clear();
		_bullets.clear();
		initializeShips();
		_player2.lives = Game.settings.numberLives();
		_player1.lives = Game.settings.numberLives();
		Game.resources.getP1().setCoords(300,300);
		Game.resources.getP1().setVelocity(new Vector(0,0));
		Game.resources.getP1().setMaxVelocity(5);
		Game.resources.getP2().setCoords(600,300);
		Game.resources.getP2().setVelocity(new Vector(0,0));
		Game.resources.getP2().setMaxVelocity(5);
		Game.score = new ScoreManager();
		Game.score.increaseLevel();
		for (int i = 1; i<Game.settings.startingLevel(); i++) {
			Game.score.increaseLevel();
		}
		newLevelShips();
		
	
	}
	
	public Spaceship getRandomPlayer() {
		if (Game.settings.multiplayerEnabled()) {
			if (Math.random() < 0.5) {
				return _player2;
			} 
		}
		return _player1;
	}
	
	public void newLevelShips(){
		_autoShips = new ArrayList<Spaceship>();
		_autoShips.add(new RogueShip("spaceship3", 3));
		_autoShips.add(new AlienShip("alienship", 4));
	}
}
