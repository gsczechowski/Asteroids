import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;


public class MenuItem {
	private static final Color BGUNSELECTED = Color.white;
	private static final Color BGSELECTED = Color.yellow;
	private static final Color BGPRESSED = Color.black;
	private static final Color FGUNSELECTED = Color.black;
	private static final Color FGSELECTED = Color.black;
	private static final Color FGPRESSED = Color.white;
	
	private static final Font MENUFONT = new Font("Arial", Font.PLAIN, 24);
	private static final int MARGIN = 2;
	
	private static final String[] ALPHABET= {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	public enum MenuEvent {
		NONE,
		PRESS,
		UNPRESS,
		NEXT_MENU,
		PREV_MENU,
		NEXT_VALUE,
		PREV_VALUE
	}
	
	public enum MenuType {
		SETTING,
		BUTTON,
		TEXTINPUT
	}
	
	private String _name;
	private String _value;
	private int _setting;
	private Point _coords; // Bottom-left coordinate
	private boolean _highlighted;
	private boolean _pressed;
	private String _text;
	private int _alphabetPtr;
	private MenuType _type;
	private MenuEvent _leftAction;
	private MenuEvent _rightAction;
	private MenuEvent _upAction;
	private MenuEvent _downAction;
	private MenuEvent _pressAction;
	private MenuEvent _unpressAction;
	private MenuItem _nextMenu;
	private MenuItem _prevMenu;
	
	public boolean buttonPressed;
	
	public MenuItem() {
		_name = "";
		_value = "";
		_coords = new Point(0,0);
		_highlighted = false;
		_pressed = false;
		_alphabetPtr = 0;
		buttonPressed = false;
	}
	
	public MenuItem(Point coords, int setting, MenuType type) {
		this();
		_coords = coords;
		_setting = setting;
		_type = type;
		if (type == MenuType.TEXTINPUT) {
			_value = "A";
		}
	}
	
	public void setAdj(MenuItem next, MenuItem prev) {
		_nextMenu = next;
		_prevMenu = prev;
	}
	
	public void setActions(MenuEvent press, MenuEvent unpress, MenuEvent left, MenuEvent right, MenuEvent up, MenuEvent down) {
		_leftAction = left;
		_rightAction = right;
		_upAction = up;
		_downAction = down;
		_pressAction = press;
		_unpressAction = unpress;
	}
	
	public void setText(String text) {
		_text = text;
	}
	
	public void select() {
		_highlighted = true;
	}
	
	public void deselect() {
		_highlighted = false;
	}
	public boolean selected() {
		return _highlighted;
	}
	
	public void left() {
		handleAction(_leftAction);
		//_value = Game.settings.next(_setting);
	}
	
	public void right() {
		handleAction(_rightAction);
		//_value = Game.settings.prev(_setting);
	}
	
	public void up() {
		handleAction(_upAction);
	}
	
	public void down() {
		handleAction(_downAction);
	}
	
	public void press() {
		handleAction(_pressAction);
	}
	
	public void unpress() {
		handleAction(_unpressAction);
	}
	private void handleAction(MenuEvent action) {
		if (action == MenuEvent.NEXT_MENU) {
			this.deselect();
			_nextMenu.select();
		} else if (action == MenuEvent.PREV_MENU){
			this.deselect();
			_prevMenu.select();
		} else if (action == MenuEvent.NEXT_VALUE){
			if (_type == MenuType.SETTING) _value = Game.settings.next(_setting);
			else if (_type == MenuType.TEXTINPUT) {
				_alphabetPtr = (_alphabetPtr + 1) % 26;
				_value = ALPHABET[_alphabetPtr];
			}
		} else if (action == MenuEvent.PREV_VALUE){
			if (_type == MenuType.SETTING) _value = Game.settings.prev(_setting);
			else if (_type == MenuType.TEXTINPUT) {
				_alphabetPtr = (_alphabetPtr - 1);
				if (_alphabetPtr < 0) {
					_alphabetPtr += 26;
				}
				_value = ALPHABET[_alphabetPtr];
			}
		} else if (action == MenuEvent.PRESS) {
			this._pressed = true;
		} else if (action == MenuEvent.UNPRESS){
			if (this._pressed == true) {
				buttonPressed = true;
			}
			this._pressed = false;
		}
		
	}
	
	public void initialize() {
		_name = Game.settings.getName(_setting);
		_value = Game.settings.getSetting(_setting);
	}
	
	public void draw(Graphics2D canvas) {
		if (_pressed) {
			canvas.setColor(BGPRESSED);
		} else if (_highlighted) {
			canvas.setColor(BGSELECTED);
		} else {
			canvas.setColor(BGUNSELECTED);
		}
		canvas.setFont(MENUFONT);
		if (_type == MenuType.SETTING){ 
			Rectangle2D bounds = strBounds(":     " +_value, canvas);
			canvas.fillRect(_coords.x, _coords.y - (int)bounds.getHeight() + 2* MARGIN, 
					(int)bounds.getWidth() + 2 * MARGIN + 250, (int)bounds.getHeight() + 2 * MARGIN);
			if (_pressed) {
				canvas.setColor(FGPRESSED);
			} else if (_highlighted) {
				canvas.setColor(FGSELECTED);
			} else  {
				canvas.setColor(FGUNSELECTED);
			}
			canvas.drawString(_name, _coords.x + MARGIN, _coords.y - MARGIN);
			canvas.drawString(":     " + _value, _coords.x + MARGIN + 250, _coords.y-MARGIN);
		} else if (_type == MenuType.BUTTON){
			Rectangle2D bounds = strBounds(_text, canvas);
			canvas.fillRect(_coords.x, _coords.y - (int)bounds.getHeight() + 2* MARGIN, 
					(int)bounds.getWidth() + 2 * MARGIN, (int)bounds.getHeight() + 2 * MARGIN);
			if (_pressed) {
				canvas.setColor(FGPRESSED);
			} else if (_highlighted) {
				canvas.setColor(FGSELECTED);
			} else  {
				canvas.setColor(FGUNSELECTED);
			}
			canvas.drawString(_text, _coords.x + MARGIN, _coords.y - MARGIN);
			
		} else if (_type == MenuType.TEXTINPUT){
			Rectangle2D bounds = strBounds(_value,canvas);
			canvas.fillRect(_coords.x, _coords.y - (int)bounds.getHeight() + 2* MARGIN, 
					(int)bounds.getWidth() + 2 * MARGIN, (int)bounds.getHeight() + 2 * MARGIN);
			if (_pressed) {
				canvas.setColor(FGPRESSED);
			} else if (_highlighted) {
				canvas.setColor(FGSELECTED);
			} else  {
				canvas.setColor(FGUNSELECTED);
			}
			canvas.drawString(_value, _coords.x + MARGIN, _coords.y - MARGIN);
		}
		
	}
	
	private static Rectangle2D strBounds(String str, Graphics2D canvas) {
		return MENUFONT.getStringBounds(str, canvas.getFontRenderContext());
	}
	
	public void resetButton() {
		buttonPressed = false;
	}
	
	@Override
	public String toString() {
		if (_type == MenuType.BUTTON) {
			return _text;
		}
		return _value;
	}
}
