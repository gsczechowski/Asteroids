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
	
	private static final Font MENUFONT = new Font("Arial", Font.PLAIN, 18);
	private static final int MARGIN = 2;
	
	
	private String _name;
	private String _value;
	private int _setting;
	private MenuItem _prevItem;
	private MenuItem _nextItem;
	private Point _coords; // Bottom-left coordinate
	private boolean _highlighted;
	private boolean _pressed;
	
	public MenuItem() {
		_name = "";
		_value = "";
		_prevItem = null;
		_nextItem = null;
		_coords = new Point(0,0);
		_highlighted = false;
		_pressed = false;
	}
	
	public MenuItem(Point coords, MenuItem prevItem, MenuItem nextItem, int setting) {
		this();
		_prevItem = prevItem;
		_nextItem = nextItem;
		_setting = setting;
	}
	
	public void select() {
		_highlighted = true;
	}
	
	public void deselect() {
		_highlighted = false;
	}
	
	public MenuItem nextItem() {
		return _nextItem;
	}
	
	public MenuItem prevItem() {
		return _prevItem;
	}
	
	public void nextValue() {
		_value = Game.settings.next(_setting);
	}
	
	public void prevValue() {
		_value = Game.settings.prev(_setting);
	}
	
	public void initialize() {
		_name = Game.settings.getName(_setting);
		_value = Game.settings.getSetting(_setting);
	}
	
	public void draw(Graphics2D canvas) {
		Rectangle2D bounds = strBounds(_name, canvas);
		if (_pressed) {
			canvas.setColor(BGPRESSED);
		} else if (_highlighted) {
			canvas.setColor(BGSELECTED);
		} else {
			canvas.setColor(BGUNSELECTED);
		}
		canvas.fillRect(_coords.x, _coords.y, (int)bounds.getX() + 2 * MARGIN + 250, (int)bounds.getY() + 2 * MARGIN);
		if (_pressed) {
			canvas.setColor(FGPRESSED);
		} else if (_highlighted) {
			canvas.setColor(FGSELECTED);
		} else  {
			canvas.setColor(FGUNSELECTED);
		}
		canvas.drawString(_name + "     :     "+ _value, _coords.x + MARGIN, _coords.y - MARGIN);
		
	}
	
	private static Rectangle2D strBounds(String str, Graphics2D canvas) {
		return MENUFONT.getStringBounds(str, canvas.getFontRenderContext());
	}
}
