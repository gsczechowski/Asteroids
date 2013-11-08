import java.util.HashMap;
public class InputState {
	private HashMap<String,Boolean> _keyCodes;
	
	public InputState(HashMap<String, Boolean> hm) {
		_keyCodes = new HashMap<String, Boolean>(hm);
	}
	
	public boolean pressed(String key) {
		return (boolean)_keyCodes.get(key);
	}

}
