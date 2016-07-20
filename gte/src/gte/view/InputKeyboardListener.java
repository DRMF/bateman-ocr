package gte.view;

import gte.controller.Controller;
import gte.model.Model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by kns10 on 4/22/16.
 */
public class InputKeyboardListener implements KeyListener {

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

    public InputKeyboardListener(Model model, View view, Controller controller)
    {
        this.model = model;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println(keyCode);
        switch( keyCode ) {
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
