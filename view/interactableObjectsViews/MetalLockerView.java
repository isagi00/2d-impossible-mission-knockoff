package view.interactableObjectsViews;

import model.interactableObjects.MetalLocker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MetalLockerView {
    BufferedImage lockerImage;
    MetalLocker metalLocker;


    public MetalLockerView(MetalLocker metalLocker) {
        this.metalLocker = metalLocker;

        loadSprites();
    }



    public void loadSprites(){
        try{
            lockerImage = ImageIO.read(getClass().getClassLoader().getResource("interactableObjects/lockers/metalLocker.png"));
            System.out.println("loaded metal locker image");
        }catch(Exception e){
            System.out.println("failed to load metal locker image");
            e.printStackTrace();
        }


    }


    public void draw(Graphics2D g2d) {  //called in gameview's draw interactive objects method
        BufferedImage image = lockerImage;
        g2d.drawImage(image, metalLocker.getX(),metalLocker.getY(), metalLocker.getWidth(), metalLocker.getHeight(), null);
    }





}
