package view.itemViews;

import model.inventoryrelated.PokerCard;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PokerCardView extends ItemView{

    BufferedImage cardSprite;

    public PokerCardView(BufferedImage pokerCardSprite) {
        this.cardSprite = pokerCardSprite;
    }


    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.drawImage(cardSprite, x, y, width, height, null);
    }







}
