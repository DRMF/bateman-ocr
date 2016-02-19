package layout.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import layout.controller.Controller;
import layout.model.Model;
import layout.view.View;

public class ToggleAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private Model model;
	private View view;
	@SuppressWarnings("unused")
	private Controller controller;

	{
		putValue(NAME, "Toggle");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/layout/icons/exit.png")));
		putValue(SHORT_DESCRIPTION, "Toggles display of image");
		//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));

	}

	public ToggleAction(Model model, View view, Controller controller)
	    {
	        this.view = view;
	        this.model = model;
	        this.controller = controller;
	    }

	public void actionPerformed(ActionEvent e) {
		view.toggleDisplay();
	}
}
