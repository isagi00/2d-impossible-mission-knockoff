package model.levels;

import java.awt.image.BufferedImage;

/**
 * model, stores tile data : it is kept simple because the heavy work is done in {@link TileManager}.
 * represents a single Tile object in the game.
 *
 */
public class Tile {
    /**
     * tile sprite image
     */
    public BufferedImage image;
    /**
     * tile collision
     */
    public boolean collision = false;
    /**
     * tile width
     */
    public int width;
    /**
     * tile height
     */
    public int height;
}
