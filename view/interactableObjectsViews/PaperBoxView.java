package view.interactableObjectsViews;

import model.interactableObjects.PaperBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class PaperBoxView implements Observer {

    private BufferedImage closedBoxImage;
    private BufferedImage openedBoxImage;
    PaperBox paperBox;

    public PaperBoxView(PaperBox paperBox) {
        this.paperBox = paperBox;

        paperBox.addObserver(this);

        loadSprites();
    }



    public void loadSprites(){
        try{
            closedBoxImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/closedBox1.png"));
            openedBoxImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/openedBox1.png"));

        }catch(IOException e){
            System.err.println("IOException while loading box images:");
            e.printStackTrace();
        }
    }


    public void draw(Graphics2D g2d) {

        BufferedImage image = paperBox.getIsOpened() ? openedBoxImage : closedBoxImage;
        if(image != null){

            g2d.drawImage(image, paperBox.getX(), paperBox.getY(), paperBox.getWidth(), paperBox.getHeight(), null);
        }else{
            //if there is no image
            g2d.setColor(paperBox.getIsOpened() ? Color.YELLOW : Color.GRAY);
            g2d.fillRect(paperBox.getX(), paperBox.getY(), paperBox.getWidth(), paperBox.getHeight());
        }
    }


    //TRIGGERED AUTOMATICALLY WHEN THE BOX1 CHANGES STATE
    @Override
    public void update(Observable o, Object arg) {
        //  verify the notification is for this box
        if (o != paperBox) return;



    }



}
