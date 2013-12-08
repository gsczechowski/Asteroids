import java.awt.DisplayMode;
import java.io.Serializable;


public class Settings implements Serializable{

	private boolean windowed; //0
	private boolean rotation; //1
	private boolean primitive; //2 primitive rendering
	private int numLives = 3; //3
	private int level = 1; //4
	private boolean gravity; //5 gravity enable
	private boolean gravityVisible; //6 visibility of gravity
	private boolean debug; //7 debug mode
	private boolean multiplayer; //8 use two player ships
	
	
	public String getName(int num){
		switch(num){
		case 0: 
			return "Windowed Mode";
		case 1:
			return "Rotation Enabled";
		case 2:
			return "Primitive Rendering";
		case 3:
			return "Number of Lives";
		case 4:
			return "Starting Level";
		case 5:
			return "Gravity Enabled";
		case 6:
			return "Gravity Visible";
		case 7:
			return "Debug Mode";
		case 8:
			return "Multiplayer Mode";
		}
		return "Error";
	}
	
	public String getSetting(int num){
		switch(num){
		case 0:
			if(windowed)
				return "Enabled";
			else
				return "Disabled";
		case 1:
			if(rotation)
				return "Enabled";
			else
				return "Disabled";
		case 2:
			if(primitive)
				return "Enabled";
			else
				return "Disabled";
		case 3:
			if(numLives > 0)
				return Integer.toString(numLives);
			return "Unlimited";
		case 4:
			return Integer.toString(level);
		case 5:
			if(gravity)
				return "Enabled";
			else
				return "Disabled";
		case 6:
			if(gravityVisible)
				return "Enabled";
			else
				return "Disabled";
		case 7:
			if (debug)
				return "Enabled";
			else
				return "Disabled";
		case 8:
			if (multiplayer) 
				return "Enabled";
			else
				return "Disabled";
		}
		return "Error";
	}
	
	public String next(int num){
		switch(num){
		case 0:
			windowed = !windowed;
			break;
		case 1:
			rotation = !rotation;
			break;
		case 2:
			primitive = !primitive;
			break;
		case 3:
			numLives++;
			break;
		case 4:
			level++;
			break;
		case 5:
			gravity = !gravity;
			break;
		case 6:
			gravityVisible = !gravityVisible;
			break;
		case 7:
			debug = !debug;
			break;
		case 8:
			multiplayer = !multiplayer;
			break;
		}
		return getSetting(num);
	}
	
	public String prev(int num){
		switch(num){
		case 0:
			windowed = !windowed;
			break;
		case 1:
			rotation = !rotation;
			break;
		case 2:
			primitive = !primitive;
			break;
		case 3:
			if(numLives > 0)
				numLives--;
			break;
		case 4:
			if(level > 1)
				level--;
			break;
		case 5:
			gravity = !gravity;
			break;
		case 6:
			gravityVisible = !gravityVisible;
			break;
		case 7:
			debug = !debug;
			break;
		case 8:
			multiplayer = !multiplayer;
		}
		return getSetting(num);
	}
	
	public void save(){
		//Game.save();
	}
	
	public int numSettings(){
		return 9;
	}
	
	public boolean windowedMode() {
		return windowed;
	}
	
	public boolean rotationEnabled() {
		return rotation;
	}
	
	public boolean primitiveRendering() {
		return primitive;
	}
	
	public int startingLevel() {
		return level;
	}
	
	public int numberLives() {
		return numLives;
	}
	
	public boolean gravityEnabled() {
		return gravity;
	}
	public boolean gravityObjVisible() {
		return gravityVisible;
	}
	
	public boolean debugEnabled() {
		return debug;
	}
	
	public boolean multiplayerEnabled() {
		return multiplayer;
	}
}
