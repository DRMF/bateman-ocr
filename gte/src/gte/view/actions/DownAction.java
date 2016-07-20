package gte.view.actions;

import gte.controller.Controller;
import gte.model.Model;
import gte.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by kns10 on 4/12/16.
 */
public class DownAction extends AbstractAction{
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private Model model;
    private View view;
    private Controller controller;

    public DownAction(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e)
    {
        model.addComponent(model.nextBelow());
        System.out.println("down");
    }
}
