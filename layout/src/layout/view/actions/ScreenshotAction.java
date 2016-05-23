package layout.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import layout.controller.Controller;
import layout.model.Model;
import layout.view.View;

public class ScreenshotAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private Model model;
	private View view;
	@SuppressWarnings("unused")
	private Controller controller;
	
	private JFileChooser directoryChooser;

	{
		putValue(NAME, "Screenshot");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/layout/icons/screenshot.png")));
		putValue(SHORT_DESCRIPTION, "Splits up page into word and math section pictures.");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

	}

	public ScreenshotAction(Model model, View view, Controller controller)
	    {
	        this.view = view;
	        this.model = model;
	        this.controller = controller;
	    }

	public void actionPerformed(ActionEvent e) {
        if (directoryChooser == null)
        {
            directoryChooser = new JFileChooser(".");
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            directoryChooser.setAcceptAllFileFilterUsed(false);
        }
        directoryChooser.setDialogTitle("Choose a directory to place screenshots in");
        int result = directoryChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION)
		{
			view.takeScreenshots(directoryChooser.getCurrentDirectory().getAbsolutePath() + "/" + directoryChooser.getSelectedFile().getName());
		}
	
	}
}
