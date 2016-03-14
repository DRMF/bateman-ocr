package layout.view;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import layout.controller.Controller;
import layout.model.Model;
import layout.view.actions.ExitAction;
import layout.view.actions.LongRunningAction;
import layout.view.actions.OpenAction;
import layout.view.actions.ToggleBoxAction;
import layout.view.actions.ToggleImageAction;
import layout.view.actions.ZoomInAction;
import layout.view.actions.ZoomOutAction;
import layout.view.actions.ZoomPageAction;
import layout.view.actions.ZoomResetAction;
import layout.view.actions.ZoomWidthAction;

/**
 * User: Alan P. Sexton
 * Date: 21/06/13
 * Time: 13:42
 */
public class View extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Canvas canvas = null;
    private Model model = null;
    @SuppressWarnings("unused")
	private Controller controller;
    private JScrollPane canvasScrollPane;
    
    private boolean isImageDisplayEnabled;
    private boolean isBoxDisplayEnabled;
    private AbstractAction toggleImageAction;
    private AbstractAction toggleBoxAction;
    private AbstractAction zoomInAction;
    private AbstractAction zoomOutAction;
    private AbstractAction zoomResetAction;
    private AbstractAction zoomWidthAction;
    private AbstractAction zoomPageAction;
    
    private double scaleFactor;
    
    private double maxWidthScaleFactor;
    private double maxHeightScaleFactor;

    public View(Model modelObject, Controller controller)
    {
        super("Layout Analyser");
        this.model = modelObject;
        this.controller = controller;
        controller.addView(this);

        // We will use the default BorderLayout, with a scrolled panel in
        // the centre area, a tool bar in the NORTH area and a menu bar

        canvasScrollPane = new HVMouseWheelScrollPane(this);

        // The default when scrolling is very slow. Set the increment as follows:
        canvasScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        canvasScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        canvas = new Canvas(model, this, controller);
        canvasScrollPane.setViewportView(canvas);
        getContentPane().add(canvasScrollPane, BorderLayout.CENTER);

        // exitAction has to be final because we reference it from within
        // an inner class

        scaleFactor = 0.1;
        
        isImageDisplayEnabled = true;
        isBoxDisplayEnabled = false;
        
        final AbstractAction exitAction = new ExitAction(model, this, controller);
        AbstractAction openAction = new OpenAction(model, this, controller);
        AbstractAction longRunningAction = new LongRunningAction(model, this, controller);
        toggleImageAction = new ToggleImageAction(model, this, controller);
        toggleImageAction.setEnabled(false);
        toggleBoxAction = new ToggleBoxAction(model, this, controller);
        toggleBoxAction.setEnabled(false);
        zoomInAction = new ZoomInAction(model, this, controller);
        zoomInAction.setEnabled(false);
        zoomOutAction = new ZoomOutAction(model, this, controller);
        zoomOutAction.setEnabled(false);
        zoomResetAction = new ZoomResetAction(model, this, controller);
        zoomResetAction.setEnabled(false);
        zoomWidthAction = new ZoomWidthAction(model, this, controller);
        zoomWidthAction.setEnabled(false);
        zoomPageAction = new ZoomPageAction(model, this, controller);
        zoomPageAction.setEnabled(false);
        

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                exitAction.actionPerformed(null);
            }
        });
        
        addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				
			}

			@Override
			public void componentResized(ComponentEvent e) {
		    	double canvasScrollPaneWidth = (double)canvasScrollPane.getViewport().getSize().width;
		    	double canvasScrollPaneHeight= (double)canvasScrollPane.getViewport().getSize().height;
		    	double modelWidth = (double)model.getDimensions().width;
		    	double modelHeight = (double)model.getDimensions().height;
		    	int verticalScrollBarWidth = canvasScrollPane.getVerticalScrollBar().getPreferredSize().width;
		    	int horizontalScrollBarHeight = canvasScrollPane.getHorizontalScrollBar().getPreferredSize().height;
		    	
		    	maxWidthScaleFactor = canvasScrollPaneWidth / modelWidth;
		    	maxHeightScaleFactor = canvasScrollPaneHeight / modelHeight;
		    	
		    	//Take the size of the scroll bars into account
		    	if(maxWidthScaleFactor > maxHeightScaleFactor && maxHeightScaleFactor > 1)
		    		maxWidthScaleFactor = (canvasScrollPaneWidth - verticalScrollBarWidth) /  modelWidth;
		    	else if(maxWidthScaleFactor > maxHeightScaleFactor && maxWidthScaleFactor > 1)
		    		maxHeightScaleFactor = (canvasScrollPaneHeight- horizontalScrollBarHeight) / modelHeight;
			}

			@Override
			public void componentShown(ComponentEvent e) {
				
			}
        	
        });

        // Set up the menu bar
        JMenu fileMenu;
        fileMenu = new JMenu("File");
        fileMenu.add(openAction);
        fileMenu.add(longRunningAction);
        fileMenu.add(toggleImageAction);
        fileMenu.add(toggleBoxAction);
        fileMenu.addSeparator();
        fileMenu.add(zoomInAction);
        fileMenu.add(zoomOutAction);
        fileMenu.add(zoomResetAction);
        fileMenu.add(zoomWidthAction);
        fileMenu.add(zoomPageAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);

        JMenuBar menuBar;

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        // Set up the tool bar
        JToolBar toolBar;
        toolBar = new JToolBar();
        toolBar.setFloatable(true);
        toolBar.setRollover(true);
        toolBar.add(exitAction);
        toolBar.addSeparator();
        toolBar.add(openAction);
        toolBar.add(longRunningAction);
        toolBar.add(toggleImageAction);
        toolBar.add(toggleBoxAction);
        toolBar.addSeparator();
        toolBar.add(zoomInAction);
        toolBar.add(zoomOutAction);
        toolBar.add(zoomResetAction);
        toolBar.add(zoomWidthAction);
        toolBar.add(zoomPageAction);

        getContentPane().add(toolBar, BorderLayout.NORTH);

        pack();
        setBounds(0, 0, 700, 800);
    }

    public void adaptToNewImage()
    {
    	toggleImageAction.setEnabled(true);
    	toggleBoxAction.setEnabled(true);
    	zoomInAction.setEnabled(true);
    	zoomOutAction.setEnabled(true);
    	zoomResetAction.setEnabled(true);
    	zoomWidthAction.setEnabled(true);
    	zoomPageAction.setEnabled(true);
    	
    	double canvasScrollPaneWidth = (double)canvasScrollPane.getViewport().getSize().width;
    	double canvasScrollPaneHeight= (double)canvasScrollPane.getViewport().getSize().height;
    	double modelWidth = (double)model.getDimensions().width;
    	double modelHeight = (double)model.getDimensions().height;
    	int verticalScrollBarWidth = canvasScrollPane.getVerticalScrollBar().getPreferredSize().width;
    	int horizontalScrollBarHeight = canvasScrollPane.getHorizontalScrollBar().getPreferredSize().height;
    	
    	maxWidthScaleFactor = canvasScrollPaneWidth / modelWidth;
    	maxHeightScaleFactor = canvasScrollPaneHeight / modelHeight;
    	
    	if(maxWidthScaleFactor > maxHeightScaleFactor)
    		maxWidthScaleFactor = (canvasScrollPaneWidth - verticalScrollBarWidth) /  modelWidth;
    	else
    		maxHeightScaleFactor = (canvasScrollPaneHeight- horizontalScrollBarHeight) / modelHeight;
    	
        setCanvasSize();
        canvas.repaint();
    }


    /**
     * Adapt the settings for the ViewPort and scroll bars to the dimensions required.
     * This needs to be called anytime the image changes size.
     */
    protected void setCanvasSize()
    {
        canvas.setSize(canvas.getPreferredSize());

        // need this so that the scroll bars knows the size of the canvas that has to be scrolled over
        canvas.validate();
    }

    protected Canvas getCanvas()
    {
        return canvas;
    }

    protected JScrollPane getCanvasScrollPane()
    {
        return canvasScrollPane;
    }
    
    public boolean getIsImageDisplayEnabled(){
    	return isImageDisplayEnabled;
    }
    
    public boolean getIsBoxDisplayEnabled(){
    	return isBoxDisplayEnabled;
    }
    
    public void toggleImageDisplay(){
    	isImageDisplayEnabled = !isImageDisplayEnabled;
    	canvas.repaint();
    }
    
    public void toggleBoxDisplay(){
    	isBoxDisplayEnabled = !isBoxDisplayEnabled;
    	canvas.repaint();
    }
    
    public void setScale(double scale){
    	canvas.setScale(scale);
    	setCanvasSize();
    	canvas.repaint();
    	
    	//rounded because of inherent computing errors with doubles
    	if(Math.round((scale - scaleFactor) * 10000.0) / 10000.0 <= 0)
    		zoomOutAction.setEnabled(false);
    	else
    		zoomOutAction.setEnabled(true);
    	
    	if(scale == 1)
    		zoomResetAction.setEnabled(false);
    	else
    		zoomResetAction.setEnabled(true);
    	
    	if(scale == maxWidthScaleFactor)
    		zoomWidthAction.setEnabled(false);
    	else
    		zoomWidthAction.setEnabled(true);
    	
    	if((maxHeightScaleFactor > maxWidthScaleFactor && scale == maxWidthScaleFactor) || (maxHeightScaleFactor < maxWidthScaleFactor && scale == maxHeightScaleFactor))
    		zoomPageAction.setEnabled(false);
    	else
    		zoomPageAction.setEnabled(true);
    		
    	
    }
    
    public double getScale(){
    	return canvas.getScale();
    }
    
    public double getScaleFactor(){
    	return scaleFactor;
    }
    
    public double getMaxWidthScaleFactor(){
    	return maxWidthScaleFactor;
    }
    
    public double getMaxHeightScaleFactor(){
    	return maxHeightScaleFactor;
    }
}
