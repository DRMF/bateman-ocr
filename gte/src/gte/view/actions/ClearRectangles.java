package gte.view.actions;

import gte.controller.Controller;
import gte.model.Model;
import gte.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by kns10 on 3/29/16.
 */
public class ClearRectangles extends AbstractAction {
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private Model model;
    private View view;
    private Controller controller;


    {
        putValue(NAME, "Clear Rectangles");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/gte/icons/rightarrow.png")));
        putValue(SHORT_DESCRIPTION, "Erases rectangles.");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control ."));
    }

    public ClearRectangles(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e)
    {
        model.clearRects();
    }
}
