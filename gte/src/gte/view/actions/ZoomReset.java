package gte.view.actions;

import gte.controller.Controller;
import gte.model.Model;
import gte.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by kns10 on 3/2/16.
 */
public class ZoomReset extends AbstractAction
{
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private Model model;
    private View view;
    private Controller controller;


    {
        putValue(NAME, "Reset Zoom");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/gte/icons/zoomreset.png")));
        putValue(SHORT_DESCRIPTION, "Resets zoom level");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control 0"));
    }

    public ZoomReset(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e)
    {
        view.setZoomLevel(1);
    }
}
