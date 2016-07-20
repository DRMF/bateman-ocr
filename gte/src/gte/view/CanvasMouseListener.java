package gte.view;

import gte.controller.Controller;
import gte.model.Model;

import javax.swing.event.MouseInputListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * User: Alan P. Sexton
 * Date: 21/06/13
 * Time: 00:52
 */
class CanvasMouseListener implements MouseInputListener

{
    Model model;
    View view;
    Controller controller;

    int x1;
    int y1;
    int x2;
    int y2;
    boolean mouseDown = false;

    public CanvasMouseListener(Model model, View view, Controller controller)
    {
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    public void paint(Graphics g)
    {
        if (mouseDown)
        {
            Color col = g.getColor();
            g.setColor(Color.RED);
            if (x1 <= x2)
            {
                if (y1 <= y2)
                    g.drawRect(x1,y1, x2-x1, y2-y1);
                else
                    g.drawRect(x1, y2, x2 - x1, y1 - y2);
            }
            else
            {
                if (y1 <= y2)
                    g.drawRect(x2,y1, x1-x2, y2-y1);
                else
                    g.drawRect(x2,y2, x1-x2, y1-y2);
            }
            g.setColor(col);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        model.selectComponent(model.whichClicked(e.getPoint()));
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!model.isActive())
            return ;
        x1 = (int)(e.getX()/view.getZoomLevel());
        y1 = (int)(e.getY()/view.getZoomLevel());
        mouseDown = true;
        view.getCanvas().addMouseMotionListener(this);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        Rectangle r;
        if (!model.isActive())
            return ;
        view.getCanvas().removeMouseMotionListener(this);
        mouseDown = false;
        x2 = (int)(e.getX()/view.getZoomLevel());
        y2 = (int)(e.getY()/view.getZoomLevel());
        if (x1 <= x2)
        {
            if (y1 <= y2)
                r = new Rectangle(x1,y1,x2-x1,y2-y1);
            else
                r = new Rectangle(x1, y2, x2 - x1, y1 - y2);
        }
        else
        {
            if (y1 <= y2)
                r = new Rectangle(x2, y1, x1 - x2, y2 - y1);
            else
                r = new Rectangle(x2, y2, x1 - x2, y1 - y2);
        }
        if (e.getModifiers()!=0 && e.isShiftDown()) {
            model.selectComponents(model.allSelected(r));
        } else if (e.getModifiers()!=0 && e.isControlDown()) {
            model.toggleComponents(model.allSelected(r));
        } else {
            model.deselectAllComponents();
            model.selectComponents(model.allSelected(r));
        }

        controller.addRect(r);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        x2 = (int)(e.getX()/view.getZoomLevel());
        y2 = (int)(e.getY()/view.getZoomLevel());
        view.getCanvas().repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }
}
