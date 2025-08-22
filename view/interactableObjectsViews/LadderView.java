package view.interactableObjectsViews;

import model.interactableObjects.Ladder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LadderView {
    BufferedImage ladderImage;
    Ladder ladder;


    public LadderView(Ladder ladder ) {
        this.ladder = ladder;
        loadSprites();
    }



    public void loadSprites() {
        try{
            ladderImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/ladder/ladder.png"));
            System.out.println("ladder image(s) loaded");
        }catch(Exception e){
            System.out.println("ladder image(s) not loaded : ");
            e.printStackTrace();
        }
    }



    public void update() {
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(ladderImage, ladder.getX(), ladder.getY(), ladder.getWidth(), ladder.getHeight(), null);
    }


}
