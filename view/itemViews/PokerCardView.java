package view.itemViews;

import model.inventoryrelated.PokerCard;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * view of a single instance of a {@link PokerCard}.
 * the constructor takes a single poker card Buffered Image
 */
public class PokerCardView extends ItemView{

    /**
     * the sprite of the poker card
     */
    private BufferedImage cardSprite;

    /**this object is the view of a single {@link PokerCard}.
     * takes a poker card sprite image as the parameter
     * @param pokerCardSprite a buffered image that represents the image of the poker card.
     */
    public PokerCardView(BufferedImage pokerCardSprite) {
        this.cardSprite = pokerCardSprite;
    }


    /**draws the {@link #cardSprite}
     * @param g2d    swing's graphics 2d instance that allows rendering
     * @param x      x coordinate of the item to draw
     * @param y      y coordinate of the item to draw
     * @param width  width of the item to draw
     * @param height height of the item to draw
     */
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.drawImage(cardSprite, x, y, width, height, null);
    }







}
