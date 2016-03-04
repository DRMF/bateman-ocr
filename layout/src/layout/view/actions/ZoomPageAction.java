package layout.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import layout.controller.Controller;
import layout.model.Model;
import layout.view.View;

public class ZoomPageAction extends AbstractAction {
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
		putValue(NAME, "Zoom Width");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/layout/icons/togglebox.png")));
		putValue(SHORT_DESCRIPTION, "Resets the zoom of the canvas");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control +"));

	}

	public ZoomPageAction(Model model, View view, Controller controller)
	    {
	        this.view = view;
	        this.model = model;
	        this.controller = controller;
	    }

	public void actionPerformed(ActionEvent e) {
		double scale = 1;
		if(view.getMaxHeightScaleFactor() > view.getMaxWidthScaleFactor())
			scale = view.getMaxWidthScaleFactor();
		else
			scale = view.getMaxHeightScaleFactor();
		
		view.setScale(scale);
	}
}
