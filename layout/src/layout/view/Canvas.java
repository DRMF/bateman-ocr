package layout.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import layout.controller.Controller;
import layout.model.Component;
import layout.model.Model;

/**
 * User: Alan P. Sexton Date: 20/06/13 Time: 18:00
 */
class Canvas extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Model model;
	private View view;
	private double scale;

	private CanvasMouseListener mouseListener;

	/**
	 * The default constructor should NEVER be called. It is made private so
	 * that no other class can create a Canvas except by initializing it
	 * properly (i.e. by calling the parameterized constructor)
	 */
	@SuppressWarnings("unused")
	private Canvas() {
	}

	/**
	 * Create a <code>Canvas</code> object initialized to the given
	 * <code>View</code> and <code>Model</code>
	 *
	 * @param view
	 *            The View object that encapsulates the whole GUI
	 * @param model
	 *            The Model object that encapsulates the (view-independent) data
	 *            of the application
	 */
	public Canvas(Model model, View view, Controller controller) {
		this.view = view;
		this.model = model;
		this.scale = 1;
		mouseListener = new CanvasMouseListener(model, view, controller);
		addMouseListener(mouseListener);
	}

	/**
	 * The method that is called to paint the contents of this component
	 *
	 * @param g
	 *            The <code>Graphics</code> object used to do the actual drawing
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Using g or g2, draw on the full size "canvas":
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		
		if (view.getIsImageDisplayEnabled()) {
			//
			// The ViewPort is the part of the canvas that is displayed.
			// By scrolling the ViewPort, you move it across the full size
			// canvas,
			// showing only the ViewPort sized window of the canvas at any one
			// time.

			if (model.getImage() != null) {
				BufferedImage image = model.getImage();

				// Draw the display image on the full size canvas
				g2.drawImage(image, 0, 0, null);

			}

			
		} else {
			g2.setColor(Color.WHITE);
			Dimension dim = model.getDimensions();
			g2.fillRect(0, 0, (int)dim.getWidth(), (int)dim.getHeight());
		}
		
		List<Component> components = model.getComponents();
		List<Rectangle> rects = model.getRects();
		
		if (view.getIsBoxDisplayEnabled() && !components.isEmpty()) {
			Color col = g2.getColor();
			for (Component component : components) {
				g2.setColor(Color.BLUE);
				
				g2.draw(component.getData());
			}
			
			g2.setColor(Color.GREEN);
			
			for(Component c : model.getFinalBounds().get(Model.LineTypes.MATH))
				g2.draw(c.getData());
			
			g2.setColor(Color.RED);
			
			for(Component c : model.getFinalBounds().get(Model.LineTypes.WORD))
				g2.draw(c.getData());
			
			g2.setColor(col);
			
			// In case there is some animation going on (e.g. mouse
			// dragging), call this to
			// paint the intermediate images
			mouseListener.paint(g);
			
			if (!rects.isEmpty()) {
				col = g2.getColor();
				g2.setColor(Color.BLUE);
				for (Rectangle rect : rects) {
					g2.draw(rect);
				}
				g2.setColor(col);
			}
		}
	}

	public Dimension getPreferredSize() {
		if (model.isActive())
			return new Dimension((int)(model.getImage().getWidth() * scale), (int)(model.getImage().getHeight() * scale));
		return new Dimension(0, 0);
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getScale() {
		return scale;
	}

}
