package gte.view.actions;

import gte.controller.Controller;
import gte.model.Model;
import gte.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by kns10 on 3/2/16.
 */
public class ZoomIn extends AbstractAction{
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private Model model;
    private View view;
    private Controller controller;


    {
        putValue(NAME, "Zoom Out");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/gte/icons/zoomin.png")));
        putValue(SHORT_DESCRIPTION, "Zooms in");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control ="));
    }

    public ZoomIn(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e)
    {
        view.setZoomLevel(Math.min(view.getZoomLevel()+0.2,8));
    }
}
