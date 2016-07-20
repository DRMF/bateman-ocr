package gte.view.actions;

import gte.controller.Controller;
import gte.model.Model;
import gte.view.View;
import gte.model.Component;
import gte.utils.ImageFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by kns10 on 2/22/16.
 */
public class ToggleAction extends AbstractAction {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SuppressWarnings("unused")
    private Model model;
    private View view;
    private Controller controller;
    private BufferedImage loadedImage;
    private boolean toggled;

    {
        putValue(NAME, "Toggle");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/gte/icons/toggle.png")));
        putValue(SHORT_DESCRIPTION, "Toggles between loaded image and generated image");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));

    }

    public ToggleAction(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        this.controller = controller;
        this.toggled = false;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (model.isActive()) {
            // make sure image is loaded
            if (this.toggled == false) {
                this.toggled = true;
                // DISPLAY THE INDIVIDUAL COMPONENTS
                // save the image for use later
                loadedImage = model.getImage();
                if (model.getComps().isEmpty()) {
                    // first time, load the csv file
                    String csvFile = "gte/data/sample_ccs/moments.csv";
                    BufferedReader br = null;
                    //Integer page = 1;
                    String line;
                    try {

                        br = new BufferedReader(new FileReader(csvFile));
                        br.readLine();
                        while ((line = br.readLine()) != null) {
                            int pagenum = view.getPageNum()+1;
                            if (Integer.parseInt(""+line.charAt(13))==pagenum) {
                                Component temp = new Component(line);
                                model.addComponent(temp);
                            }
                        }

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view, "Bad file path");
                        System.out.println(ex);
                    }
                }


            } else {
                // DISPLAY THE WHOLE IMAGE
                this.model.setImage(loadedImage);
                // clear our copy of the image to save memory
                this.loadedImage = null;
                this.toggled = false;
            }
        } else {
            JOptionPane.showMessageDialog(view, "No image is loaded!");
        }



        /*
        if (response == JOptionPane.YES_OPTION)
            controller.exit(0);
        */
    }
}
