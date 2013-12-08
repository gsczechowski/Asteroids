import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;
public class ScoreManager implements Serializable{
	private int _level;
	private int _numAsteroids;
	private long _score;
	private transient Timer _asteroidTimer = null;
	
	public ScoreManager() {
		this.reset();
	}
	
	public void reset() {
		// Reset the level to 1, the score to 0
		_level = 0;
		_score = 0;
		if (_asteroidTimer != null) {
			_asteroidTimer.cancel();
			_asteroidTimer.purge();
		}
		makeTimer();
		
	}
	public void makeTimer() {
		_asteroidTimer = new Timer();
	}
	
	public void asteroidHit() {
		_score += 5;
	}
	
	public void increaseLevel() {
		_score += _level * 100;
		_level ++;
		_numAsteroids = (int)(Math.pow(_level, 1.5) * (.5) + 3);
		// Spawn the first asteroid
		spawnAsteroid();
		Game.resources.newLevelShips();
		
	}
	
	public int getLevel() {
		return _level;
	}
	
	public void spawnAsteroid() {
		if (_numAsteroids > 0) {
			_numAsteroids --;
			Game.resources.spawnAsteroid(Game.screen.getWindowSize());
			_asteroidTimer.schedule(new Task(), new Random().nextInt(5000) + 5000);
		} else {
			// We are out of asteroids, so wait until we get the signal for another level
			Game.resources.sendLevelComplete = true;
		}
		
	}
	public class Task extends TimerTask {

		@Override
		public void run() {
			spawnAsteroid();
			
		}
	}
	
	public void draw(Graphics2D canvas) {
		// Just draw the score in the top left-hand corner of the screen
		canvas.setFont(new Font("Arial", Font.PLAIN, 24));
		canvas.drawString("Score: " + _score + "               Level: " + _level + "               Num Asteroids Left: " + _numAsteroids, 10, 30);
	}

}
