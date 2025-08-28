package view.interactableObjectsViews;

import model.interactableObjects.Ladder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * view of the {@link Ladder} model
 */
public class LadderView {
    /**
     * ladder sprite image
     */
    private static BufferedImage ladderImage;
    /**
     * ladder instance that this view is rendering
     */
    private final Ladder ladder;


    /**view of the {@link Ladder} model
     * @param ladder ladder instance that this view is rendering
     */
    public LadderView(Ladder ladder) {
        this.ladder = ladder;
        loadSprites();
    }

    /**
     * loads the ladder sprites
     */
    public void loadSprites() {
        try{
            ladderImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/ladder/ladder.png"));
            System.out.println("[LadderView] ladder image loaded");
        }catch(Exception e){
            System.out.println("[LadderView] ladder image not loaded : ");
            e.printStackTrace();
        }
    }

    /**draws the ladder sprite at {@link Ladder} x and y position
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d) {
        g2d.drawImage(ladderImage, ladder.getX(), ladder.getY(), ladder.getWidth(), ladder.getHeight(), null);
    }


}
