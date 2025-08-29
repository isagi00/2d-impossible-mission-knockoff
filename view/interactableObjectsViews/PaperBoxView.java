package view.interactableObjectsViews;

import model.interactableObjects.PaperBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * view of the {@link PaperBox} model
 */
public class PaperBoxView {

    /**
     * closed paper box sprite
     */
    private static BufferedImage closedBoxImage;
    /**
     * opened paper box sprite
     */
    private static BufferedImage openedBoxImage;
    /**
     * {@link PaperBox} model instance
     */
    private final PaperBox paperBox;

    /**view of the {@link PaperBox} model.
     * loads the paper box sprites via {@link #loadSprites()}
     * @param paperBox {@link PaperBox} instance
     */
    public PaperBoxView(PaperBox paperBox) {
        this.paperBox = paperBox;

        loadSprites();
    }


    /**
     * loads the paper box sprites
     */
    public void loadSprites(){
        try{
            closedBoxImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/closedBox1.png"));
            openedBoxImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/openedBox1.png"));
            System.out.println("[PaperBoxView] loaded paper box sprites");
        }catch(IOException e){
            System.err.println("[PaperBoxView]IOException while loading box images:");
            e.printStackTrace();
        }
    }


    /**draws the paper box sprites at the {@link PaperBox} model specified location
     *@param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d) {
        BufferedImage image = paperBox.getIsOpened() ? openedBoxImage : closedBoxImage;
        if(image != null){
            g2d.drawImage(image, paperBox.getX(), paperBox.getY(), paperBox.getWidth(), paperBox.getHeight(), null);
        }
    }



}
