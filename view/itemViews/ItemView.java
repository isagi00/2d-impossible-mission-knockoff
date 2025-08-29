package view.itemViews;

import model.inventoryrelated.Item;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * abstract class of an item view, it renders an {@link Item}, displayed in the {@link InventoryView}.
 */
public abstract class ItemView {
    /**
     * item that this view is trying to render.
     */
    Item item;

    /**view of the item that will appear in {@link InventoryView}.
     * it takes the {@link Item} model and creates its view component
     * @param item item to render
     */
    public ItemView(Item item) {
        this.item = item;
    }

    /**
     * empty constructor for more flexibility when creating the item view
     */
    public ItemView(){}

    /**constructor that takes the item sprite to render
     * @param itemSprite sprite of the item
     */
    public ItemView(BufferedImage itemSprite){
    }


    /**
     * loads the sprites of the item
     */
    public void loadSprites() {}

    /**renders the item in the inventory
     * @param g2d swing's graphics 2d instance that allows rendering
     * @param x x coordinate of the item to draw
     * @param y y coordinate of the item to draw
     * @param width  width of the item to draw
     * @param height height of the item to draw
     */
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {}


}
