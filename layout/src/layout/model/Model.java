package layout.model;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private BufferedImage image = null;
    private List<Rectangle> rects = new ArrayList<Rectangle>();
    private List<Component> components = new ArrayList<Component>();
    private Component biggestBox = null;

    public Model()
    {
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
    	
    	reader.close();
    }

}
