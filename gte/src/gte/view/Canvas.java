package gte.view;

import gte.controller.Controller;
import gte.model.Model;
import gte.model.Component;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * User: Alan P. Sexton
 * Date: 20/06/13
 * Time: 18:00
 */
class Canvas extends JPanel
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Model model;
    @SuppressWarnings("unused")
	private View view;

    private CanvasMouseListener mouseListener;
    private CanvasKeyboardListener keyboardListener;
    private boolean hasFocus = false;

    /**
     * The default constructor should NEVER be called. It is made private so that no other class can create a
     * Canvas except by initializing it properly (i.e. by calling the parameterized constructor)
     */
    @SuppressWarnings("unused")
	private Canvas()
    {
    }

    /**
     * Create a <code>Canvas</code> object initialized to the given <code>View</code> and <code>Model</code>
     *
     * @param view  The View object that encapsulates the whole GUI
     * @param model The Model object that encapsulates the (view-independent) data of the application
     */
    public Canvas(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        mouseListener = new CanvasMouseListener(model, view, controller);
        keyboardListener = new CanvasKeyboardListener(model, view, controller);
        addMouseListener(mouseListener);
        addKeyListener(keyboardListener);
    }


    /**
     * The method that is called to paint the contents of this component
     *
     * @param g The <code>Graphics</code> object used to do the actual drawing
     */
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);


        // Using g or g2, draw on the full size "canvas":
        Graphics2D g2 = (Graphics2D) g;
        //
        // The ViewPort is the part of the canvas that is displayed.
        // By scrolling the ViewPort, you move it across the full size canvas,
        // showing only the ViewPort sized window of the canvas at any one time.
        if (view.getToggled()) {
            List<Rectangle> rects = model.getRects();
            g2.scale(view.getZoomLevel(), view.getZoomLevel());
            //g2.setColor(Color.WHITE);
            for (Component c : model.getComps()) {
                g2.drawImage(c.getHeldImage(), c.getImageCoords().x, c.getImageCoords().y, null);
            }
            if (!rects.isEmpty()) {
                Color col = g2.getColor();
                g2.setColor(Color.BLUE);
                for (Rectangle rect : rects) {
                    g2.draw(rect);
                }
                g2.setColor(col);
            }
            // In case there is some animation going on (e.g. mouse dragging), call this to
            // paint the intermediate images
            this.updateUI();
            mouseListener.paint(g);
        }
        else if (model.getImage() != null)
        {
            BufferedImage image = model.getImage();
            List<Rectangle> rects = model.getRects();

            // Draw the display image on the full size canvas
            g2.scale(view.getZoomLevel(), view.getZoomLevel());
            g2.drawImage(image, 0, 0, null);

            if (!rects.isEmpty()) {
                Color col = g2.getColor();
                g2.setColor(Color.BLUE);
                for (Rectangle rect : rects) {
                    g2.draw(rect);
                }
                g2.setColor(col);
            }
            this.updateUI();
            // In case there is some animation going on (e.g. mouse dragging), call this to
            // paint the intermediate images
            mouseListener.paint(g);
        }
    }


    public Dimension getPreferredSize()
    {
        if (model.isActive())
            return new Dimension((int)(model.getImage().getWidth()*view.getZoomLevel()),
                    (int)(model.getImage().getHeight()*view.getZoomLevel()));
        return new Dimension(0, 0);
    }

}
