import java.util.HashMap;
public class InputState {
	private HashMap<String,Boolean> _keyCodes;
	
	public InputState(HashMap<String, Boolean> hm) {
		_keyCodes = new HashMap<String, Boolean>(hm);
	}
	
	public boolean pressed(String key) {
		return (boolean)_keyCodes.get(key);
	}
	
	@Override
	public String toString() {
		String s = "";
		for (String key: _keyCodes.keySet()) {
			s = s.concat(key + ": " + _keyCodes.get(key) + "\n");
		}
		return s;
	}

}
