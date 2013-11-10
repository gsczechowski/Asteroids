
public class Vector {
	public double x;
	public double y;
	public Vector() {
		x = 0;
		y = 0;
	}
	public Vector(double x_component, double y_component) {
		x = x_component;
		y = y_component;
	}
	public Vector add(Vector vec) {
		return new Vector(this.x + vec.x, this.y + vec.y);
	}
	public Vector sub(Vector vec) {
		return new Vector(this.x - vec.x, this.y - vec.y);
	}
	public double abs() {
		return Math.sqrt(Math.pow(x,2.0) +  Math.pow(y,2.0));
	}
	public Vector scalarMul(double s) {
		return new Vector(this.x * s, this.y * s);
	}
	/**
	 * Finds the angle at which the vector is pointing, relative to 0 degrees. (Straight right)
	 * @return
	 * The angle in degrees
	 */
	public double angle() {
		if (x == 0) {
			if (y > 0) {
				return 90.0;
			}
			if (y < 0) {
				return 270.0;
			}
			else return 0;
		}
		double a = Math.toDegrees(Math.atan(y/x));
		if (a < 0) {
			a += 360.0;
			if (x < 0) {
				a-= 180.0;
			}
		}
		else {
			if (y <0) {
				a+=180.0;
			}
		}
		return a;		
	}
	public void rotate(double degrees) {
		while (degrees < 0) { degrees+= 360.0;}
		while (degrees >= 360) {degrees -= 360.0;}
		Vector v = unitAtAngle(degrees).scalarMul(abs());
		x = v.x;
		y = v.y;
	}
	public void clamp(double maxAbs) {
		double abs = abs();
		if (abs <= maxAbs) {
			return;
		}
		Vector v= unitAtAngle(angle()).scalarMul(maxAbs);
		x = v.x;
		y = v.y;
	}
	public Vector unit() {
		return new Vector(this.x / abs(), this.y / abs());
	}
	public static Vector unitAtAngle(double degrees) {
		Vector v = new Vector();
		v.x = Math.cos(Math.toRadians(degrees));
		v.y = Math.sin(Math.toRadians(degrees));
		return v;
	}
	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}
}
