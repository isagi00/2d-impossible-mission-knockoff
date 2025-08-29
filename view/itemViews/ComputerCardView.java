package view.itemViews;

import model.inventoryrelated.ComputerCard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * view of the {@link ComputerCard} model, displayed in {@link InventoryView}
 */
public class ComputerCardView extends ItemView {

    /**
     * sprite of the computer card displayed in {@link InventoryView}
     */
    private static BufferedImage computerCardSprite;


    /**view of the {@link ComputerCard} model. renders the item sprite, used in {@link InventoryView}.
     * @param computerCard {@link ComputerCard} model
     */
    public ComputerCardView(ComputerCard computerCard) {
        super(computerCard);
        loadSprites();
    }

    /**
     * loads the computer card item sprite.
     */
    @Override
    public void loadSprites(){
        try{
            computerCardSprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/computerCard.png"));
//            System.out.println("[ComputerCardView]loaded computer card item sprite");
        }
        catch (Exception e) {
//            System.out.println("[ComputerCardView]failed to load computer card item sprite");
            e.printStackTrace();
        }
    }


    /**renders the computer card item image
     * @param g2d swing's graphics 2d instance that allows rendering
     * @param x x coordinate of the card item
     * @param y y coordinate of the card item
     * @param width width of the card item
     * @param height height of the card item
     */
    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        if (computerCardSprite != null){
            g2d.drawImage(computerCardSprite, x, y, width, height, null);
//            System.out.println("[ComputerCard]drawing computer card image");
        }


    }


}
