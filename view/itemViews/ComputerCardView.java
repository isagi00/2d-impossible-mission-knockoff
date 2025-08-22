package view.itemViews;

import model.inventoryrelated.ComputerCard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ComputerCardView extends ItemView {

    BufferedImage computerCardSprite;


    public ComputerCardView(ComputerCard computerCard) {
        super(computerCard);

        loadSprites();
        if (computerCardSprite == null) {
//            System.err.println("COMPUTER CARD SPRITE FAILED TO LOAD!");
        }

    }

    @Override
    public void loadSprites(){
        try{
            computerCardSprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/computerCard.png"));
//            System.out.println("loaded computer card item sprite");
        } catch (Exception e) {
//            System.out.println("failed to load computer card item sprite");
            e.printStackTrace();
        }
    }



    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        if (computerCardSprite != null){
            g2d.drawImage(computerCardSprite, x, y, width, height, null);
//            System.out.println("drawing computer card image");
        }
        else{
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x, y, width, height);
//            System.out.println("drawing computer card fallback image");
        }


    }


}
