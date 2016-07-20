package gte.view.actions;

import gte.controller.Controller;
import gte.model.Model;
import gte.utils.ImageFile;
import gte.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
/**
 * Created by kns10 on 2/26/16.
 */
public class PreviousPage extends AbstractAction {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private Model model;
    private View view;
    private Controller controller;


    {
        putValue(NAME, "Previous page");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/gte/icons/leftarrow.png")));
        putValue(SHORT_DESCRIPTION, "Previous page");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control ,"));
    }

    public PreviousPage(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e)
    {
        int temp = view.getPageNum();
        model.clearComps();
        try {
            if (view.getToggled()==true) {
                view.toggle();
            }
            model.loadImageWithNumber(model.getCurrentFile(), temp - 1);
            view.setPageNum(temp-1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "This is the first image!");
            System.out.println(ex);
        }
    }
}
