package view.itemViews;

import model.inventoryrelated.Item;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ItemView {
    Item item;

    public ItemView(Item item) {
        this.item = item;
    }

    public ItemView(){}

    public ItemView(BufferedImage itemSprite){

    }


    public void loadSprites() {}

    public void draw(Graphics2D g2d, int x, int y, int width, int height) {}


}
