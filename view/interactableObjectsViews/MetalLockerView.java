package view.interactableObjectsViews;

import model.interactableObjects.MetalLocker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * view of the {@link MetalLocker} model
 */
public class MetalLockerView {
    /**
     * locker image
     */
    private static BufferedImage lockerImage;
    private final MetalLocker metalLocker;


    /**view of the {@link MetalLocker} model.
     * loads the sprite via {@link #loadSprites()}
     * @param metalLocker {@link MetalLocker} model instance that this view is rendering
     */
    public MetalLockerView(MetalLocker metalLocker) {
        this.metalLocker = metalLocker;

        loadSprites();
    }


    /**
     * loads the metal locker sprite
     */
    public void loadSprites(){
        try{
            lockerImage = ImageIO.read(getClass().getClassLoader().getResource("interactableObjects/lockers/metalLocker.png"));
            System.out.println("[MetalLockerView] loaded metal locker image");
        }catch(Exception e){
            System.out.println("[MetalLockerView]failed to load metal locker image");
            e.printStackTrace();
        }


    }


    /**draws the metal locker at {@link MetalLocker} position
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d) {  //called in gameview's draw interactive objects method
        BufferedImage image = lockerImage;
        g2d.drawImage(image, metalLocker.getX(),metalLocker.getY(), metalLocker.getWidth(), metalLocker.getHeight(), null);
    }





}
