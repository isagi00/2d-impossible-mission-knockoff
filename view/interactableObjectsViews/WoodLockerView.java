package view.interactableObjectsViews;

import model.interactableObjects.WoodLocker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WoodLockerView {

    private BufferedImage lockerImage;
    private WoodLocker woodLocker;

    public WoodLockerView(WoodLocker woodLocker) {
        this.woodLocker = woodLocker;
        loadSprites();
    }




    private void loadSprites() {
        try{
            lockerImage = ImageIO.read(getClass().getClassLoader().getResource("interactableObjects/lockers/woodLocker.png"));
            System.out.println("loaded wood locker image");

        }catch(Exception e){
            System.out.println("failed to load wood locker image");
            e.printStackTrace();
        }
    }



    public void draw(Graphics2D g2d) {
        BufferedImage image = lockerImage;
        g2d.drawImage(image,woodLocker.getX(), woodLocker.getY(), woodLocker.getWidth(), woodLocker.getHeight(),null);
    }









}
