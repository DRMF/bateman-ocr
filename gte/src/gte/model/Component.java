package gte.model;

import gte.utils.ImageFile;
import gte.utils.UnsupportedImageTypeException;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kns10 on 2/24/16.
 */
public class Component {

    private String src_image;
    private int page;
    private int page_width;
    private int page_height;
    private String cc_image;
    private String image_basePath = "gte/data/sample_ccs/page-";
    private Rectangle imageCoords;

    private String[] everythingElse;

    private BufferedImage heldImage;
    private BufferedImage normalImage;
    private boolean isSelected;

    private String associatedType;
    private String associatedWord;


    public Component(int x, int y, int w, int h, BufferedImage b) {
        imageCoords = new Rectangle(x, y, w, h);
        heldImage = b;
        normalImage = deepCopy(b);
        isSelected = false;
        associatedWord = "";
    }

    public void setAssociatedType(String s) {
        associatedType = s;
    }

    public static BufferedImage tintImage(BufferedImage loadImg, int red, int green, int blue, int alpha) {
        Graphics g = loadImg.getGraphics();
        g.setColor(new Color(red, green, blue, alpha));
        g.fillRect(0, 0, loadImg.getWidth(), loadImg.getHeight());
        g.dispose();
        return loadImg;
    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public BufferedImage colorImage(BufferedImage loadImg, int red, int green, int blue) {
        BufferedImage img = new BufferedImage(loadImg.getWidth(), loadImg.getHeight(),
                BufferedImage.TRANSLUCENT);
        Graphics2D graphics = img.createGraphics();
        Color newColor = new Color(red, green, blue, 0 /* alpha needs to be zero */);
        graphics.setXORMode(newColor);
        graphics.drawImage(loadImg, null, 0, 0);
        graphics.dispose();
        return img;
    }

    public void select() {
        System.out.println(isSelected);
        if (!isSelected) {
            normalImage = deepCopy(heldImage);
            setSelected(true);

            heldImage = tintImage(heldImage, 0, 0, 100, 100);
        }
    }

    public void deselect() {
        if (isSelected) {
            setSelected(false);
            heldImage = deepCopy(normalImage);
        }
    }

    public boolean getSelected() {
        return this.isSelected;
    }

    public void toggleSelected() {

        if (!isSelected) {
            //normalImage = heldImage;
            //heldImage = tintImage(heldImage, 0, 0, 100, 100);
            normalImage = deepCopy(heldImage);
            setSelected(true);

            heldImage = tintImage(heldImage, 0, 0, 100, 100);

        } else {
            //heldImage = tintImage(heldImage, 255, 255, 155, 100);
            setSelected(false);
            heldImage = deepCopy(normalImage);

        }
    }

    public void setSelected(boolean b) {
        if (b) {
            isSelected = true;
        } else {
            isSelected = false;
        }
    }

    public BufferedImage getHeldImage() {
        return heldImage;
    }

    public String toString() {
        return this.getImageCoords().toString();
    }

    public Rectangle getImageCoords() {
        return imageCoords;
    }

    public boolean contains(Point p) {
        return this.imageCoords.contains(p);
    }

    public int getPage() {
        return page;
    }
    public Component(String s) {
        String[] current = s.split(",");
        src_image = current[0].replace("\"","");
        page = Integer.parseInt(current[1]);
        page_width  = Integer.parseInt(current[2]);
        page_height  = Integer.parseInt(current[3]);
        cc_image  = current[4].replace("\"","");
        imageCoords = new Rectangle(Integer.parseInt(current[5]),
                Integer.parseInt(current[6]),
                Integer.parseInt(current[7]),
                Integer.parseInt(current[8]));
        everythingElse = new String[current.length-9];
        System.arraycopy(current, 9, everythingElse, 0, current.length-9);
        File img = new File(image_basePath+String.format("%04d", page)+"/"+cc_image);

        try {
            heldImage = new BufferedImage(page_width,page_height, BufferedImage.TYPE_INT_ARGB);
            heldImage = ImageIO.read(img);

        } catch (Exception ex) {
            System.out.println("error reading file "+image_basePath+String.format("%04d", page)+"/"+cc_image);
            System.out.println(ex);
        }

        isSelected = false;


    }

}
