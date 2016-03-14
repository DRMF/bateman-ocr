package layout.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import layout.controller.Controller;
import layout.model.Model;
import layout.view.View;

public class ToggleBoxAction extends AbstractAction {
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
		putValue(NAME, "Toggle Box");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/layout/icons/togglebox.png")));
		putValue(SHORT_DESCRIPTION, "Toggles display of bounding boxes");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));

	}

	public ToggleBoxAction(Model model, View view, Controller controller)
	    {
	        this.view = view;
	        this.model = model;
	        this.controller = controller;
	    }

	public void actionPerformed(ActionEvent e) {
		view.toggleBoxDisplay();
	}
}
