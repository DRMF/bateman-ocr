package layout.model;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.utils.ImageFile;
import layout.utils.UnsupportedImageTypeException;

/**
 * User: Alan P. Sexton
 * Date: 20/06/13
 * Time: 23:36
 */

/**
 * The <code>Model</code> class manages the data of the application
 * <p/>
 * Other than possibly a draw method, which draws a representation of the object on a graphics context, and
 * possibly a toString method, which generates a <code>String</code> representation of the object, it should
 * not know about the user interface
 */
public class Model
{
	public enum LineTypes{
		WORD, MATH
	}
	
    private BufferedImage image = null;
    private List<Rectangle> rects = new ArrayList<Rectangle>();
    private List<Component> components = new ArrayList<Component>();
    private List<Component> words = new ArrayList<Component>();
    private List<Component> deleteThis = new ArrayList<Component>();
    private Component biggestBox = null;
    
    private int minXDistForWord = -2;
    private int maxXDistForWord = 15;
    
    private Map<int[], ArrayList<Component>> sortedWordComponents;
    private Map<int[], ArrayList<Component>> partitionedPossibleStarts;
    private Map<int[], ArrayList<Component>> wordLetters;
    private Map<int[], Map<Integer, LineTypes>> lineTypes;
    
    private List<ArrayList<int[]>> wordParagraphs;
    private List<ArrayList<int[]>> mathLines;
    
    private Map<LineTypes, ArrayList<Component>> finalBounds;

    public Model()
    {
    }
    
    public Map<LineTypes, ArrayList<Component>> getFinalBounds()
    {
    	return finalBounds;
    }
    
    public List<ArrayList<int[]>> getWordParagraphs()
    {
    	return wordParagraphs;
    }
    
    public List<ArrayList<int[]>> getMathLines()
    {
    	return mathLines;
    }
    
    public Map<int[], ArrayList<Component>> getWordLetters()
    {
    	return wordLetters;
    }
    
    public Map<int[], ArrayList<Component>> getPartitionedPossibleStarts()
    {
    	return partitionedPossibleStarts;
    }
    
    public Map<int[], Map<Integer, LineTypes>> getLineTypes()
    {
    	return lineTypes;
    }
    
    public List<Component> deleteThisMethod()
    {
    	return deleteThis;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public List<Rectangle> getRects()
    {
        return rects;
    }
    
    public List<Component> getComponents()
    {
    	return components;
    }
    
    public List<Component> getWords()
    {
    	return words;
    }
    
    public Component getBiggestBox()
    {
    	return biggestBox;
    }

    /**
     * Sets or replaces the current image in the <code>Model</code>
     *
     * @param bi the image to set in the <code>Model</code>
     */
    public void setImage(BufferedImage bi)
    {
        image = bi;
        rects.clear();
    }

    public Dimension getDimensions()
    {
        if (image != null)
            return new Dimension(image.getWidth(), image.getHeight());
        else
            return new Dimension(0,0);
    }

    /**
     * Adds a new <code>Rectangle</code> to the <code>Model</code>
     *
     * @param rect the <code>Rectangle</code> to add to the <code>Model</code>
     */
    public void addRect(Rectangle rect)
    {
        rects.add(rect);

    }

    /**
     * Tests if the model is active, i.e. whether it currently has an image
     *
     * @return <code>true</code> if the model has an image, false otherwise
     */
    public boolean isActive()
    {
        return image != null;
    }

    public void loadImage(File file)
            throws IOException, UnsupportedImageTypeException
    {
        ImageFile newImageFile = new ImageFile(file);
        int numImages = newImageFile.getNumImages();
        if (numImages == 0)
            throw new IOException("Image file contains no images");
        BufferedImage bi = newImageFile.getBufferedImage(0);
        setImage(bi);
    }
    
    public void loadCSV(File file)
    		throws IOException, UnsupportedImageTypeException
    {
    	String imagePath = file.getPath();
    	String CSVPath = imagePath.substring(0, imagePath.lastIndexOf('.')) + ".csv";
    	BufferedReader reader = new BufferedReader(new FileReader(CSVPath));
    	
    	components.clear();
    	words.clear();
    	String line = reader.readLine();
    	int maxArea = 0;
    	while ((line = reader.readLine()) != null){
    		String[] cols = line.split(",");
    		
    		int x = Integer.parseInt(cols[5]);
    		int y = Integer.parseInt(cols[6]);
    		int w = Integer.parseInt(cols[7]);
    		int h = Integer.parseInt(cols[8]);
    		
    		if(w * h > maxArea){
    			maxArea = w * h;
    			biggestBox = new Component(x, y, w, h);
    		}
    		
    		components.add(new Component(x, y, w, h));
    	}
    	
    	finalBounds = new HashMap<LineTypes, ArrayList<Component>>();
    	
    	reader.close();
    	detectWords();
    	detectWordLines();
    	combineWordParagraphs();
    	
    	finalBounds.put(LineTypes.MATH, new ArrayList<Component>());
		for(ArrayList<int[]> aai : getMathLines())
			finalBounds.get(LineTypes.MATH).addAll(findFinalBoundsOfType(aai, LineTypes.MATH));

		finalBounds.put(LineTypes.WORD, new ArrayList<Component>());
		for(ArrayList<int[]> aai : getWordParagraphs())
			finalBounds.get(LineTypes.WORD).addAll(findFinalBoundsOfType(aai, LineTypes.WORD));
    }
    
    public void detectWords()
    {
    	List<Component> listCopy = new ArrayList<Component>();
    	sortedWordComponents = new HashMap<int[], ArrayList<Component>>();
    	partitionedPossibleStarts = new HashMap<int[], ArrayList<Component>>();
    	wordLetters = new HashMap<int[], ArrayList<Component>>();
    	for(Component c : components){
    		listCopy.add(c);
    	}
    	
    	for(int i = 0; i < listCopy.size(); i++){
    		Component out = listCopy.get(i);
    		
    		if(!isLetter(out)){
    			continue;
    		}
    		
    		boolean dictionaryHasHeight = false;
			double objectY = out.getData().getY();
			double objectHeight = out.getData().getHeight();
    		
    		for(int[] heightRange : sortedWordComponents.keySet()){
    			if((objectY >= heightRange[0] && objectY <= heightRange[1]) || (heightRange[0] >= objectY && heightRange[1] <= (objectY + objectHeight))){
    				dictionaryHasHeight = true;
    				sortedWordComponents.get(heightRange).add(out);
    				
    				int[] newHeightRange = new int[] {objectY < heightRange[0] ? (int)objectY : heightRange[0], objectY + objectHeight > heightRange[1] ? (int)(objectY + objectHeight) : heightRange[1]}; 
  					sortedWordComponents.put(newHeightRange, sortedWordComponents.get(heightRange));
  					sortedWordComponents.remove(heightRange);
  					partitionedPossibleStarts.put(newHeightRange, partitionedPossibleStarts.get(heightRange));
  					partitionedPossibleStarts.remove(heightRange);
  					
  					checkIsPossibleStart(out, partitionedPossibleStarts, newHeightRange);
  		    		
    				break;
    			}
    		}
    		
    		if(!dictionaryHasHeight){
    			int[] key = new int[] {(int)objectY, (int)(objectY + objectHeight)};
    			sortedWordComponents.put(key, new ArrayList<Component>());
    			sortedWordComponents.get(key).add(out);
    			partitionedPossibleStarts.put(key, new ArrayList<Component>());
        		
    			checkIsPossibleStart(out, partitionedPossibleStarts, key);
    		}
    	}
    	
    	for(ArrayList<Component> ac : partitionedPossibleStarts.values()){
	    	for(Component c : ac){
	    		Rectangle word = new Rectangle(c.getData());
	    		int[] key = null;
	    		int[] wordLettersKey = new int[] {(int)c.getData().getX(), (int)c.getData().getY()};
	    		double objectY = c.getData().getY();
	    		double objectHeight = c.getData().getHeight();
	    		
	    		for(int[] heightRange : sortedWordComponents.keySet()){
	    			if((objectY >= heightRange[0] && objectY <= heightRange[1]) || (heightRange[0] >= objectY && (heightRange[1] <= (objectY + objectHeight)))){
	    				key = heightRange;
	    				break;
	    			}
	    		}
	    		
	    		wordLetters.put(wordLettersKey, new ArrayList<Component>());
	    		wordLetters.get(wordLettersKey).add(c);
	    		
	    		for(int i = 0; i < sortedWordComponents.get(key).size(); i++){
	    			Component right = sortedWordComponents.get(key).get(i);
	    			
	    			double dx = right.getData().getX() - (word.getMaxX());
	    			if(dx < maxXDistForWord && dx >= minXDistForWord){
	    				
	    				wordLetters.get(wordLettersKey).add(right);
	    				
	    				double newRight = right.getData().getMaxX(); 
	    				double newTop = word.getY() > right.getData().getY() ? right.getData().getY() : word.getY();
	    				double newBottom = word.getY() + word.getHeight() < right.getData().getY() + right.getData().getHeight() ? right.getData().getY() + right.getData().getHeight() : word.getY() + word.getHeight();
	    				
	    				word = new Rectangle((int)word.getX(), (int)newTop, (int)(newRight - word.getX()), (int)(newBottom - newTop));
	    				i = 0;
	    				
	    			}
	    		}
	    		
	    		words.add(new Component(word));
	    	}
    	}

    }
    
    private void detectWordLines()
    {
    	lineTypes = new HashMap<int[], Map<Integer, LineTypes>>();
    	for(int[] key : partitionedPossibleStarts.keySet()){
			lineTypes.put(key, new HashMap<Integer, LineTypes>());
			
    		for(int i = 0; i < partitionedPossibleStarts.get(key).size()-1; i++){
    			int smallestIndex = i;
    			for(int j = i; j < partitionedPossibleStarts.get(key).size(); j++){
    				if(partitionedPossibleStarts.get(key).get(j).getData().getX() < partitionedPossibleStarts.get(key).get(smallestIndex).getData().getX())
    					smallestIndex = j;
    			}
    			
    			if(!(smallestIndex == i)){
    				Component temp = partitionedPossibleStarts.get(key).get(smallestIndex);
    				partitionedPossibleStarts.get(key).set(smallestIndex, partitionedPossibleStarts.get(key).get(i));
    				partitionedPossibleStarts.get(key).set(i, temp);
    			}
    		}
    		
    		double meanWidth = 0;
    		for(int i = 0; i < partitionedPossibleStarts.get(key).size(); i++){
    			meanWidth = (meanWidth * i + partitionedPossibleStarts.get(key).get(i).getData().getWidth()) / (i + 1);
    			
    			if(i == 0){
    				lineTypes.get(key).put(i, null);
    				continue;
    			}
    			
    			Rectangle previousWord = null;
    			
    			double currentWordX = partitionedPossibleStarts.get(key).get(i).getData().getX();
    			for(int[] ai : wordLetters.keySet()){
    				if(ai[0] == (int)partitionedPossibleStarts.get(key).get(i-1).getData().getX() && ai[1] == (int)partitionedPossibleStarts.get(key).get(i-1).getData().getY())
    					previousWord = getWordBounds(wordLetters.get(ai));
    			}
    			
    			if(currentWordX - (previousWord.getMaxX()) > meanWidth * 6){
    				lineTypes.get(key).put(i, null);
    			}
    			
    		}
    		
    		List<Integer> sortedLineTypeIndices = new ArrayList<Integer>(lineTypes.get(key).keySet()); 
    		Collections.sort(sortedLineTypeIndices);
    		
    		
    		for(int i = 0; i < sortedLineTypeIndices.size(); i++){
    			int start = sortedLineTypeIndices.get(i);
    			int end = i == sortedLineTypeIndices.size() - 1 ? partitionedPossibleStarts.get(key).size() : sortedLineTypeIndices.get(i + 1);
    			int numLikelyWords = 0;
    			
        		int right = 0;
        		int left = (int)partitionedPossibleStarts.get(key).get(start).getData().getX();
    			
    			for(int j = start; j < end; j++){
		
	    			Component c = partitionedPossibleStarts.get(key).get(j);
	    			
	    			int[] wordLettersKey = null;
	    			
	    			for(int[] ai : wordLetters.keySet()){
	    				if(ai[0] == c.getData().getX() && ai[1] == c.getData().getY()){
	    					wordLettersKey = ai;
	    					break;
	    				}
	    			}
	    			
	    			Rectangle possibleWordBounds = getWordBounds(wordLetters.get(wordLettersKey));
	    			
	    			if(isWord(wordLetters.get(wordLettersKey))){
	    				boolean wordInWord = false;
	    				for(Component compare : partitionedPossibleStarts.get(key)){
	    					if(compare.equals(c))
	    						continue;	
	    					
	    					if(possibleWordBounds.intersects(compare.getData())){
	    						wordInWord = true;
	    						break;
	    					}
	    				}
	    				
	    				if(!wordInWord){
	    					//deleteThis.add(c);
	    					numLikelyWords++;
	    				}else
	    					numLikelyWords++;
	    			}
	    			
	    			if(possibleWordBounds != null && possibleWordBounds.getMaxX() > right)
	    				right = (int)(possibleWordBounds.getMaxX());
	    			
	    			if(c.getData().getX() < left)
	    				left = (int)c.getData().getX();
	    		}
	    		
	    		if((numLikelyWords >= 4 && numLikelyWords * 2 > end - start) || numLikelyWords >= 1 && numLikelyWords * 5 > end - start){
	    			lineTypes.get(key).put(start, LineTypes.WORD);
	    		} else {
	    			lineTypes.get(key).put(start, LineTypes.MATH);
	    		}
    		}
    	}
    	
    }
    
    private void combineWordParagraphs()
    {
    	wordParagraphs = new ArrayList<ArrayList<int[]>>();
    	mathLines = new ArrayList<ArrayList<int[]>>();
    	boolean previousIsWordLine = false;
    	boolean previousIsMathLine = false;
    	int[] previousKey = null;
    	
    	List<int[]> sortedLineTypeKeys = new ArrayList<int[]>();
    	
    	for(int[] key : lineTypes.keySet()){
    		if(sortedLineTypeKeys.size() == 0)
    			sortedLineTypeKeys.add(key);
    		
    		for(int i = 0; i < sortedLineTypeKeys.size(); i++){
    			if(key[0] < sortedLineTypeKeys.get(i)[0]){
    				sortedLineTypeKeys.add(i, key);
    				break;
    			}
    			
    			if(i == sortedLineTypeKeys.size() - 1){
    				sortedLineTypeKeys.add(key);
    				break;
    			}
    		}
    	}
    	
    	for(int i = 0; i < sortedLineTypeKeys.size(); i++){
    		
    		int[] key = sortedLineTypeKeys.get(i);
    		
    		if(lineTypes.get(key).containsValue(LineTypes.MATH)){
    			int primarySize = previousKey == null ? 0 : key[1] - key[0] > previousKey[1] - previousKey[0] ? key[1] - key[0] : previousKey[1] - previousKey[0];
    			deleteThis.add(new Component(30, key[0], 50, key[1] - key[0]));
    			getWordBounds(sortedWordComponents.get(key));
    			if(previousIsWordLine && key[0] - previousKey[1] < (previousKey[1] - previousKey[0]) / 5){
    				wordParagraphs.get(wordParagraphs.size() - 1).add(key);
    				deleteThis.add(new Component(100, key[0], 100, key[1] - key[0]));
    				previousIsMathLine = false;
    			} else if(previousIsMathLine && key[0] - previousKey[1] < primarySize / 7 && Math.abs((key[1] - key[0]) - (previousKey[1] - previousKey[0])) > primarySize / 2){
    				mathLines.get(mathLines.size() - 1).add(key);
    			} else {
    				mathLines.add(new ArrayList<int[]>());
    				mathLines.get(mathLines.size() - 1).add(key);
    				previousIsWordLine = false;
    				previousIsMathLine = true;
    			}
    		}
    		
    		if(lineTypes.get(key).containsValue(LineTypes.WORD)){
    			//deleteThis.add(new Component(100, key[0], 100, key[1] - key[0]));
    			if(previousIsWordLine && wordParagraphs.size() > 0 && key[0] - previousKey[1] < (key[1] - key[0]) / 3){
    				deleteThis.add(new Component(50, key[0], 100, key[1] - key[0]));
    				wordParagraphs.get(wordParagraphs.size() - 1).add(key);
    				previousIsMathLine = false;
    			} else {
    				wordParagraphs.add(new ArrayList<int[]>());
    				wordParagraphs.get(wordParagraphs.size() - 1).add(key);
    				previousIsWordLine = true;
    				previousIsMathLine = false;
    			}
    		}
    		
    		previousKey = key;
    	}
    	
    }
    
	private ArrayList<Component> findFinalBoundsOfType(ArrayList<int[]> keys, LineTypes lineType)
	{
		int maxHeight = 0;
		int index = -1;
		
		ArrayList<Component> output = new ArrayList<Component>();
		
		if(lineType.equals(LineTypes.MATH)){
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
		List<Integer> primarySortedLineTypeIndices = new ArrayList<Integer>(getLineTypes().get(keys.get(index)).keySet()); 
		Collections.sort(primarySortedLineTypeIndices);
		
		for(int i = 0; i < primarySortedLineTypeIndices.size(); i++){
			if(getLineTypes().get(keys.get(index)).get(i) == null)// || !getLineTypes().get(keys.get(index)).get(i).equals(lineType))
				continue;
			
			int start = primarySortedLineTypeIndices.get(i);
			int end = i == primarySortedLineTypeIndices.size() - 1 ? getPartitionedPossibleStarts().get(keys.get(index)).size() - 1 : primarySortedLineTypeIndices.get(i + 1) - 1;
			
			Component startComponent = getPartitionedPossibleStarts().get(keys.get(index)).get(start);
			Component endComponent = getPartitionedPossibleStarts().get(keys.get(index)).get(end);
			
			int[] wordLettersKey = null;
			
			for(int[] ai : getWordLetters().keySet()){
				if(ai[0] == endComponent.getData().getX() && ai[1] == endComponent.getData().getY()){
					wordLettersKey = ai;
					break;
				}
			}
			
			int left = (int)startComponent.getData().getX();
			int right = (int)getWordBounds(getWordLetters().get(wordLettersKey)).getMaxX();
			int[] element = new int[] {left, right};
			
			widths.add(element);
			output.add(new Component(left, keys.get(index)[0], right - left, keys.get(index)[1] - keys.get(index)[0]));
		}
		
		for(int i = 0; i < keys.size(); i++){
			
			if(i == index)
				continue;
			
			int[] key = keys.get(i);
			
			List<Integer> sortedLineTypeIndices = new ArrayList<Integer>(getLineTypes().get(key).keySet()); 
			Collections.sort(sortedLineTypeIndices);
			
			for(int j = 0; j < sortedLineTypeIndices.size(); j++){
				if(getLineTypes().get(key).get(j) == null)// || !getLineTypes().get(key).get(j).equals(lineType))
					continue;
				
				int start = sortedLineTypeIndices.get(j);
				int end = j == sortedLineTypeIndices.size() - 1 ? getPartitionedPossibleStarts().get(key).size() - 1 : sortedLineTypeIndices.get(j + 1);
				
				Component startComponent = getPartitionedPossibleStarts().get(key).get(start);
				Component endComponent = getPartitionedPossibleStarts().get(key).get(end);
				
				int[] wordLettersKey = null;
				
				for(int[] ai : getWordLetters().keySet()){
					if(ai[0] == endComponent.getData().getX() && ai[1] == endComponent.getData().getY()){
						wordLettersKey = ai;
						break;
					}
				}
				
				int left = (int)startComponent.getData().getX();
				int right = (int)getWordBounds(getWordLetters().get(wordLettersKey)).getMaxX();
				
				for(int k = 0; k < widths.size(); k++){
					Component primary = output.get(k);
					
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
		    		
		    		output.set(k, new Component(outputLeft, outputTop, outputRight - outputLeft, outputBottom - outputTop));
				}
			}
		}
		
		return output;
	}
    
    private void checkIsPossibleStart(Component out, Map<int[], ArrayList<Component>> sortedPossibleStarts, int[] heightRange)
    {
    	Iterator<Component> it = components.iterator();
    	boolean isPossibleStart = true;
    	
  		while(it.hasNext()){
  			Component in = it.next();
  			if(!isLetter(in))
  				continue;
  			
  			double dx = out.getData().getX() - (in.getData().getMaxX());
  			double dy = (out.getData().getY() + out.getData().getHeight()) - (in.getData().getY() + in.getData().getHeight());
  			if(dx >= minXDistForWord && dx <= maxXDistForWord && Math.abs(dy) <= 25){
  				isPossibleStart = false;
  				break;
  			}
  		}
  		
  		
  		if(isPossibleStart && isLetter(out)){
  			sortedPossibleStarts.get(heightRange).add(out);
  		}
    }
    
    private boolean isLetter(Component box){
    	return ((box.getData().getWidth() / box.getData().getHeight() > .2) & (box.getData().getWidth() < 500)  || (box.getData().getHeight() < 85) && (box.getData().getHeight() > 15)) && (box.getData().getWidth() > maxXDistForWord - 2);
    }
    
    public Rectangle getWordBounds(List<Component> letters)
    {
    	double top = letters.get(0).getData().getY();
    	double right = letters.get(0).getData().getMaxX();
    	double bottom = letters.get(0).getData().getY() + letters.get(0).getData().getHeight();
    	double left = letters.get(0).getData().getX();
    	
    	for(Component c : letters){
    		if(c.getData().getY() < top)
    			top = c.getData().getY();
    		
    		if(c.getData().getMaxX() > right)
    			right = c.getData().getMaxX();
    		
    		if(c.getData().getY() + c.getData().getHeight() > bottom)
    			bottom = c.getData().getY() + c.getData().getHeight();
    		
    		if(c.getData().getX() < left)
    			left = c.getData().getX();
    	}

    	return new Rectangle((int)left, (int)top, (int)(right-left), (int)(bottom-top));
    	
    }
    
    private boolean isWord(List<Component> letters)
    {
    	double meanTop = 0;
    	double meanBottom = 0;
    	double stdTop = 0;
    	double stdBottom = 0;
    	double maxPositionVariance = 6;
    	int maxNumLetterPartition = 0;
    	
    	Map<Double, ArrayList<Component>> letterPartitions = new HashMap<Double, ArrayList<Component>>();
    	double heightThreshold = letters.get(0).getData().getHeight() * .2;
    
    	if(letters.size() <= 2)
    		return false;
    	
    	for(Component c : letters){
    		meanTop += c.getData().getY();
    		meanBottom += c.getData().getY() + c.getData().getHeight();
    		
    		boolean isInExistingPartition = false;
    		for(double d : letterPartitions.keySet()){
    			if(Math.abs(c.getData().getY() - d) < heightThreshold){
    				letterPartitions.get(d).add(c);
    				isInExistingPartition = true;
    			}
    		}
    		
    		if(!isInExistingPartition){
    			letterPartitions.put(c.getData().getY(), new ArrayList<Component>());
    		}
    	}
    	
    	for(ArrayList<Component> ac : letterPartitions.values()){
    		if(ac.size() > maxNumLetterPartition)
    			maxNumLetterPartition = ac.size();
    	}
    	
    	meanTop /= letters.size();
    	meanBottom /= letters.size();
    	
    	for(Component c : letters){
    		stdTop += Math.pow(c.getData().getY() - meanTop, 2);
    		stdBottom += Math.pow(c.getData().getY() + c.getData().getHeight() - meanBottom, 2);
    	}
    	
    	stdTop /= letters.size();
    	stdBottom /= letters.size();
    	
    	return stdTop < maxPositionVariance || stdBottom < maxPositionVariance || maxNumLetterPartition > letters.size() / 2;
    }

}
