package model.tiles;

import java.awt.image.BufferedImage;

/**
 * model.
 * should store tile data
 * shoould implement tile rules
 * should provide tile accessors
 */
public class Tile {
    public BufferedImage image;
    public boolean collision = false;
    public int width;
    public int height;

}
