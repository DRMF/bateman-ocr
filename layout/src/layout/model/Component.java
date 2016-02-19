package layout.model;

import java.awt.Rectangle;

public class Component {
	
	private Rectangle data;
	
	public Component(int x, int y, int w, int h)
	{
		data = new Rectangle(x, y, w, h);
	}
	
	public Rectangle getData()
	{
		return data;
	}
}
