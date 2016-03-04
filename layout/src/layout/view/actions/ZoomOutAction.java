package layout.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import layout.controller.Controller;
import layout.model.Model;
import layout.view.View;

public class ZoomOutAction extends AbstractAction {
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
		putValue(NAME, "Zoom Out");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/layout/icons/togglebox.png")));
		putValue(SHORT_DESCRIPTION, "Zooms out from the canvas");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control -"));

	}

	public ZoomOutAction(Model model, View view, Controller controller)
	    {
	        this.view = view;
	        this.model = model;
	        this.controller = controller;
	    }

	public void actionPerformed(ActionEvent e) {
		view.setScale(view.getScale() - view.getScaleFactor());
	}
}
