package layout.model;

import java.awt.Rectangle;

public class Component {
	
	private Rectangle data;
	
	public Component(int x, int y, int w, int h)
	{
		data = new Rectangle(x, y, w, h);
	}
	
	public Component(Rectangle word) {
		data = new Rectangle((int)word.getX(), (int)word.getY(), (int)word.getWidth(), (int)word.getHeight());
	}

	public Rectangle getData()
	{
		return data;
	}
}
