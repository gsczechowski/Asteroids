import java.awt.Dimension;
import java.awt.Graphics2D;


public class GravityObject extends Visual {

	private static final double G = 1200;
	public GravityObject(Dimension screenSize) {
		super("blackhole");
		try {
			this.loadImage();
		}catch (Exception e) {}
		_coords.x = screenSize.getWidth() / 2;
		_coords.y = screenSize.getHeight() / 2;
	}
	
	public void draw(Graphics2D canvas) {
		if (Game.settings.gravityObjVisible()){
			Dimension iSize = getScaledImageSize();
			canvas.drawImage(_bufImage, null, (int)(_coords.x - iSize.width/2), (int)(_coords.y-iSize.height/2));
		}
	}

	/**
	 * Enacts gravity based on the input coordinates, the input velocity vector, and the amount of time has passed. 
	 * @param coordinates
	 * Coordinates of the object being acted upon
	 * @param velocity
	 * Speed and direction of the object being acted upon
	 * @param nanoTime
	 * Amount of nano seconds that have passed since the last update
	 */
	public Vector enactGravity(Vector coordinates, Vector velocity, long nanoTime) {
		// Find a vector between two points
		Vector v = new Vector(this._coords.x - coordinates.x, this._coords.y - coordinates.y);
		// Get the length of this vector
		double abs = v.abs();
		double nt = nanoTime * 1e-9;
		// fg = vel + (G * v' * |v| * nT / 1E-9)
		Vector fg = velocity.add(v.unit().scalarMul(G).scalarMul(1/abs).scalarMul(nt));
		return fg;
		
	}
}
