package gte.view;

import gte.controller.Controller;
import gte.model.Model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by kns10 on 4/6/16.
 */
public class CanvasKeyboardListener implements KeyListener {

    Model model;
    View view;
    Controller controller;

    /*
    int x1;
    int y1;
    int x2;
    int y2;
    boolean mouseDown = false;
    */

    public CanvasKeyboardListener(Model model, View view, Controller controller)
    {
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                System.out.println("up");
                model.selectComponent(model.nextAbove());
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("up");
                model.selectComponent(model.nextBelow());
                break;
            case KeyEvent.VK_LEFT:
                System.out.println("up");
                model.selectComponent(model.nextLeft());
                break;
            case KeyEvent.VK_RIGHT :
                System.out.println("up");
                model.selectComponent(model.nextRight());
                break;
            case KeyEvent.VK_ENTER:
                System.out.println("y u do dish");
                view.getTextData();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e){

    }

    @Override
    public void keyTyped(KeyEvent e){

    }
}
