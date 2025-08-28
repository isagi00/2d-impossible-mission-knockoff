package view.interactableObjectsViews;

import model.interactableObjects.WoodLocker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * view of the {@link WoodLocker} model
 */
public class WoodLockerView {

    /**
     * wood locker image
     */
    private static BufferedImage lockerImage;
    /**
     * {@link WoodLocker} instance
     */
    private final WoodLocker woodLocker;

    /**view of the {@link WoodLocker} model, loads the wood locker sprite via {@link #loadSprites()}
     * @param woodLocker {@link WoodLocker} instance
     */
    public WoodLockerView(WoodLocker woodLocker) {
        this.woodLocker = woodLocker;
        loadSprites();
    }


    /**
     * loads the wood locker sprites
     */
    private void loadSprites() {
        try{
            lockerImage = ImageIO.read(getClass().getClassLoader().getResource("interactableObjects/lockers/woodLocker.png"));
            System.out.println("[WoodLocker] loaded wood locker image");
        }
        catch(Exception e){
            System.out.println("[WoodLocker] failed to load wood locker image");
            e.printStackTrace();
        }
    }


    /**draws the wood locker at {@link WoodLocker} specified location
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d) {
        BufferedImage image = lockerImage;
        g2d.drawImage(image,woodLocker.getX(), woodLocker.getY(), woodLocker.getWidth(), woodLocker.getHeight(),null);
    }









}
