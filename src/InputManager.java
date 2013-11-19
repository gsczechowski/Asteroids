import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;


public class InputManager implements KeyListener{
	private HashMap<String, Boolean> _keyStates;
	private String [] _bindings;
	private Component _comp;
	public InputManager(Component comp) {
		_comp = comp;
		_comp.addKeyListener(this);
		_bindings = new String[600];
		for (int i = 0; i < 600; i++) {
			_bindings[i] = "";
		}
		_keyStates = new HashMap<String, Boolean>();
	}
	public void bind(int keyCode, String binding) {
		if (_bindings != null && keyCode >= 0 && keyCode < 600) {
			_bindings[keyCode] = binding;
			_keyStates.put(binding, false);	
		}
	}
	public void unbind(int keyCode) {
		if (_bindings != null && keyCode >= 0 && keyCode < 600) {
			String oldbinding = _bindings[keyCode];
			_bindings[keyCode] = "";
			for (int i = 0; i < 600; i++ ) {
				if (_bindings[keyCode].equals(oldbinding) ) {
					return;
				}
			}
			_keyStates.remove(oldbinding);
		}
	}
	public String getName(int keyCode) {
		if (keyCode >= 0 && keyCode < 600) {
			return _bindings[keyCode];
		}
		return "";
	}
	
	// Inherited from KeyListener
	public void keyPressed(KeyEvent k) {
		if (k.getKeyCode() == KeyEvent.VK_SPACE) {
			System.out.println("Pressed Space");
		}
		String key = _bindings[k.getKeyCode()];
		if (_keyStates.containsKey(key)){
			_keyStates.put(key, true);
		}
	}
	
	// Inherited from KeyListener
	public void keyTyped(KeyEvent k) {
		k.consume();
	}
	
	// Inherited from KeyListener
	public void keyReleased(KeyEvent k) {
		String key = _bindings[k.getKeyCode()];
		if (_keyStates.containsKey(key)){
			_keyStates.put(key,false);
		}
	}
	
	public InputState getState() {
		HashMap<String, Boolean> retmap = new HashMap<String, Boolean>();
		for (String s :_keyStates.keySet()) {
			if (_keyStates.get(s) != null && _keyStates.get(s) == true) {
				retmap.put(s, true);
			}else{
				retmap.put(s, false);
			}
		}
		return new InputState(retmap);
	}

}
