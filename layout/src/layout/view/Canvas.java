package layout.view;

import layout.controller.Controller;
import layout.model.Model;
import layout.model.Component;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Alan P. Sexton Date: 20/06/13 Time: 18:00
 */
class Canvas extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int minArea = 300;
	private static final int maxArea = 15000;
	private static final int minInnerWidth = 7;
	private static final int minOuterWidth = 20;
	private static final int minTableSide = 100;
	private Model model;
	private View view;
	private double scale;

	private CanvasMouseListener mouseListener;
	
	private List<Component> deleteThis;

	/**
	 * The default constructor should NEVER be called. It is made private so
	 * that no other class can create a Canvas except by initializing it
	 * properly (i.e. by calling the parameterized constructor)
	 */
	@SuppressWarnings("unused")
	private Canvas() {
	}

	/**
	 * Create a <code>Canvas</code> object initialized to the given
	 * <code>View</code> and <code>Model</code>
	 *
	 * @param view
	 *            The View object that encapsulates the whole GUI
	 * @param model
	 *            The Model object that encapsulates the (view-independent) data
	 *            of the application
	 */
	public Canvas(Model model, View view, Controller controller) {
		this.view = view;
		this.model = model;
		this.scale = 1;
		mouseListener = new CanvasMouseListener(model, view, controller);
		addMouseListener(mouseListener);
		deleteThis = new ArrayList<Component>();
	}

	/**
	 * The method that is called to paint the contents of this component
	 *
	 * @param g
	 *            The <code>Graphics</code> object used to do the actual drawing
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Using g or g2, draw on the full size "canvas":
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(scale, scale);
		
		if (view.getIsImageDisplayEnabled()) {
			//
			// The ViewPort is the part of the canvas that is displayed.
			// By scrolling the ViewPort, you move it across the full size
			// canvas,
			// showing only the ViewPort sized window of the canvas at any one
			// time.

			if (model.getImage() != null) {
				BufferedImage image = model.getImage();

				// Draw the display image on the full size canvas
				g2.drawImage(image, 0, 0, null);

			}
			
			List<Component> deleteThis = model.deleteThisMethod();
			for(Component c : deleteThis){
				g2.setColor(Color.MAGENTA);
				g2.draw(c.getData());
				g2.fillRect((int)c.getData().getX(), (int)c.getData().getY(), (int)c.getData().getWidth(), (int)c.getData().getHeight());
			}

			
		} else {
			g2.setColor(Color.WHITE);
			Dimension dim = model.getDimensions();
			g2.fillRect(0, 0, (int)dim.getWidth(), (int)dim.getHeight());
		}
		
		List<Component> components = model.getComponents();
		List<Rectangle> rects = model.getRects();
		List<Component> possibleStarts = model.getWords();
		
		g2.setColor(Color.BLUE);
		for(Component c : possibleStarts){
			g2.draw(c.getData());
		}
		
		if (view.getIsBoxDisplayEnabled() && !components.isEmpty()) {
			Color col = g2.getColor();
			for (Component component : components) {
				int biggestBoxLeft = (int) model.getBiggestBox().getData().getX();
				int biggestBoxRight = biggestBoxLeft + (int) model.getBiggestBox().getData().getWidth();
				int biggestBoxHeight = (int) model.getBiggestBox().getData().getHeight();
				int componentX = (int) component.getData().getX();
				
				boolean isWithinTable = (biggestBoxHeight > minTableSide && biggestBoxRight - biggestBoxLeft > minTableSide) ? (componentX > biggestBoxLeft && componentX < biggestBoxRight) : true;
				double area = component.getData().getWidth() * component.getData().getHeight();

				if(area < minArea || area > maxArea)
					g2.setColor(Color.RED);
				else if(area < maxArea)
					g2.setColor(Color.GREEN);

				if(component.getData().getWidth() < (isWithinTable ? minInnerWidth : minOuterWidth))
					g2.setColor(Color.RED);
				
				g2.draw(component.getData());
			}
			
			g2.setColor(Color.YELLOW);
			
			for(ArrayList<int[]> aai : model.getMathLines())
				for(Component c : drawLine(aai, Model.LineTypes.MATH))
					g2.draw(c.getData());
			
			g2.setColor(Color.ORANGE);
			
			for(ArrayList<int[]> aai : model.getWordParagraphs()){
				for(Component c : drawLine(aai, Model.LineTypes.WORD))
					g2.draw(c.getData());
			}
			
//			for(int[] ai : model.getMathLines()){
//				ArrayList<int[]> aai = new ArrayList<int[]>();
//				aai.add(ai);
//				g2.draw(drawParagraph(aai).getData());
//			}
			
			g2.setColor(Color.MAGENTA);
			
			for(Component c : deleteThis){
				g2.fill(c.getData());
			}
			
			g2.setColor(col);
			
			// In case there is some animation going on (e.g. mouse
			// dragging), call this to
			// paint the intermediate images
			mouseListener.paint(g);
			
			if (!rects.isEmpty()) {
				col = g2.getColor();
				g2.setColor(Color.BLUE);
				for (Rectangle rect : rects) {
					g2.draw(rect);
				}
				g2.setColor(col);
			}
		}
	}
	
	private ArrayList<Component> drawLine(ArrayList<int[]> keys, Model.LineTypes lineType)
	{
		int maxHeight = 0;
		int index = -1;
		
		ArrayList<Component> output = new ArrayList<Component>();
		
		if(lineType.equals(Model.LineTypes.MATH)){
			for(int i = 0; i < keys.size(); i++){
				if(keys.get(i)[1] - keys.get(i)[0] > maxHeight){
					index = i;
					maxHeight = keys.get(i)[1] - keys.get(i)[0];
				}
			}
		} else {
			index = 0;
			maxHeight = keys.get(0)[1] - keys.get(0)[0];
		}
		
		List<int[]> widths = new ArrayList<int[]>();
		List<Integer> primarySortedLineTypeIndices = new ArrayList<Integer>(model.getLineTypes().get(keys.get(index)).keySet()); 
		Collections.sort(primarySortedLineTypeIndices);
		
		for(int i = 0; i < primarySortedLineTypeIndices.size(); i++){
			int start = primarySortedLineTypeIndices.get(i);
			int end = i == primarySortedLineTypeIndices.size() - 1 ? model.getPartitionedPossibleStarts().get(keys.get(index)).size() - 1 : primarySortedLineTypeIndices.get(i + 1) - 1;
			
			Component startComponent = model.getPartitionedPossibleStarts().get(keys.get(index)).get(start);
			Component endComponent = model.getPartitionedPossibleStarts().get(keys.get(index)).get(end);
			
			int[] wordLettersKey = null;
			
			for(int[] ai : model.getWordLetters().keySet()){
				if(ai[0] == endComponent.getData().getX() && ai[1] == endComponent.getData().getY()){
					wordLettersKey = ai;
					break;
				}
			}
			
			int left = (int)startComponent.getData().getX();
			int right = (int)model.getWordBounds(model.getWordLetters().get(wordLettersKey)).getMaxX();
			int[] element = new int[] {left, right};
			
			widths.add(element);
			output.add(new Component(left, keys.get(index)[0], right - left, keys.get(index)[1] - keys.get(index)[0]));
		}
		
		for(int i = 0; i < keys.size(); i++){
			
			if(i == index)
				continue;
			
			int[] key = keys.get(i);
			
			List<Integer> sortedLineTypeIndices = new ArrayList<Integer>(model.getLineTypes().get(key).keySet()); 
			Collections.sort(sortedLineTypeIndices);
			
			for(int j = 0; j < sortedLineTypeIndices.size(); j++){
				if(model.getLineTypes().get(key).get(j) == null || !model.getLineTypes().get(key).get(j).equals(lineType))
					continue;
				
				int start = sortedLineTypeIndices.get(j);
				int end = j == sortedLineTypeIndices.size() - 1 ? model.getPartitionedPossibleStarts().get(key).size() - 1 : sortedLineTypeIndices.get(j + 1);
				
				Component startComponent = model.getPartitionedPossibleStarts().get(key).get(start);
				Component endComponent = model.getPartitionedPossibleStarts().get(key).get(end);
				
				int[] wordLettersKey = null;
				
				for(int[] ai : model.getWordLetters().keySet()){
					if(ai[0] == endComponent.getData().getX() && ai[1] == endComponent.getData().getY()){
						wordLettersKey = ai;
						break;
					}
				}
				
				int left = (int)startComponent.getData().getX();
				int right = (int)model.getWordBounds(model.getWordLetters().get(wordLettersKey)).getMaxX();
				
				int outputIndex = -1;
				
				for(int k = 0; k < widths.size(); k++){
					if(widths.get(k)[0] < left && widths.get(k)[1] > left || widths.get(k)[0] < right && widths.get(k)[1] > right){
						outputIndex = k;
						break;
					}
				}
				
				if(outputIndex != -1){
					Component primary = output.get(outputIndex);
					
					int outputLeft = (int)primary.getData().getX();
					int outputRight = (int)primary.getData().getMaxX();
					int outputTop = (int)primary.getData().getY();
					int outputBottom = (int)primary.getData().getMaxY();
					
		    		if(left < outputLeft)
		    			outputLeft = left;
		    		if(right > outputRight)
		    			outputRight = right;
		    		if(key[0] < outputTop)
		    			outputTop = key[0];
		    		if(key[1] > outputBottom)
		    			outputBottom = key[1];
		    		
		    		output.set(outputIndex, new Component(outputLeft, outputTop, outputRight - outputLeft, outputBottom - outputTop));
				}
			}
		}
		
		if(output.size() > 1){
			//System.out.println(output.get(0).getData().getX() + "\t" + output.get(1).getData().getX());
			//deleteThis.add(output.get(0));
			
			//deleteThis.add(output.get(1));
		}
		
		return output;
	}
	
	private Component drawParagraph(ArrayList<int[]> keys)
	{
		int top = (int)model.getDimensions().getHeight();
		int bottom = 0;
		int left = (int)model.getDimensions().getWidth();
		int right = 0;
		
		for(int[] key : keys){
    		List<Integer> sortedLineTypeIndices = new ArrayList<Integer>(model.getLineTypes().get(key).keySet()); 
    		Collections.sort(sortedLineTypeIndices);
    		
    		int start = sortedLineTypeIndices.get(0);
    		int end = model.getPartitionedPossibleStarts().get(key).size() - 1;
    		int[] wordLettersKey = null;
    		
    		Component first = model.getPartitionedPossibleStarts().get(key).get(start);
    		Component last = model.getPartitionedPossibleStarts().get(key).get(end);
    		
			for(int[] ai : model.getWordLetters().keySet()){
				if(ai[0] == last.getData().getX() && ai[1] == last.getData().getY()){
					wordLettersKey = ai;
					break;
				}
			}
    		
    		Rectangle lastWordBounds = model.getWordBounds(model.getWordLetters().get(wordLettersKey));
    		
    		if(first.getData().getX() < left)
    			left = (int)first.getData().getX();
    		if(lastWordBounds.getMaxX() > right)
    			right = (int)lastWordBounds.getMaxX();
    		if(key[0] < top)
    			top = key[0];
    		if(key[1] > bottom)
    			bottom = key[1];
		}
		
		return new Component(left, top, right - left, bottom - top);
	}

	public Dimension getPreferredSize() {
		if (model.isActive())
			return new Dimension((int)(model.getImage().getWidth() * scale), (int)(model.getImage().getHeight() * scale));
		return new Dimension(0, 0);
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getScale() {
		return scale;
	}

}
