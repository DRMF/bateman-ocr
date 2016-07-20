package gte.view.actions;

import gte.controller.Controller;
import gte.model.Model;
import gte.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by kns10 on 3/15/16.
 */
public class ZoomWidth extends AbstractAction {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private Model model;
    private View view;
    private Controller controller;


    {
        putValue(NAME, "Zoom Out");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/gte/icons/zoomin.png")));
        putValue(SHORT_DESCRIPTION, "Zoom width");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 9"));
    }

    public ZoomWidth(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e)
    {
        view.setZoomLevel(view.getWidth()*1.0/model.getImage().getWidth());
    }
}
