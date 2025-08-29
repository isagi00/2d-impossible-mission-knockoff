package model.levels;

import model.ScreenSettings;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static java.util.Objects.requireNonNull;

/**
 * model, initializes all the various tiles information in the {@link #tiles}  array.
 */
public class TileManager {
    /**
     * array of {@link Tile}s
     */
    private final Tile[] tiles;

    /**
     * initialized the empty array {@link tiles} into an array of size 101, and initializes the tiles information
     */
    public TileManager() {
        tiles = new Tile[101];    //creating 81 kind of tiles, if you need more, change this
        initializeTiles();
    }


    /**
     * initializes the {@link tiles} array with new {@link Tile}s.
     * sets each tile's image(sprite), collision, width and height
     */
    private void initializeTiles() {
        try {
            // 0 is empty tile.
            Tile emptyTile = new Tile();
                BufferedImage emptyTileImage = new BufferedImage(ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = emptyTileImage.createGraphics();

                //g2d.setColor(Color.BLACK); //black
                g2d.setColor(new Color(0,0,0,0)); //transparent
                g2d.fillRect(0, 0, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);
                g2d.dispose();
            tiles[0] = emptyTile;
            tiles[0].image = emptyTileImage;
            tiles[0].collision = false;
            tiles[0].width = ScreenSettings.TILE_SIZE;
            tiles[0].height = ScreenSettings.TILE_SIZE;

            Tile backGroundMetal = new Tile();
            tiles[3] = backGroundMetal;
            tiles[3].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_03.png")));
            tiles[3].collision = false;
            tiles[3].width = ScreenSettings.TILE_SIZE;
            tiles[3].height = ScreenSettings.TILE_SIZE;

            Tile metalTopLeft = new Tile();
            tiles[4] = metalTopLeft;
            tiles[4].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_04.png")));
            tiles[4].collision = true;
            tiles[4].width = ScreenSettings.TILE_SIZE;
            tiles[4].height = ScreenSettings.TILE_SIZE;

            Tile metalTop = new Tile();
            tiles[5] = metalTop;
            tiles[5].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_05.png")));
            tiles[5].collision = true;
            tiles[5].width = ScreenSettings.TILE_SIZE;
            tiles[5].height = ScreenSettings.TILE_SIZE;

            Tile metalTopRight = new Tile();
            tiles[6] = metalTopRight;
            tiles[6].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_06.png")));
            tiles[6].collision = true;
            tiles[6].width = ScreenSettings.TILE_SIZE;
            tiles[6].height = ScreenSettings.TILE_SIZE;

            Tile deathTile = new Tile();
            tiles[9] = deathTile;
            tiles[9].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_09.png")));
            tiles[9].collision = true;
            tiles[9].width = ScreenSettings.TILE_SIZE;
            tiles[9].height = ScreenSettings.TILE_SIZE;

            Tile metalLeft = new Tile();
            tiles[13] = metalLeft;
            tiles[13].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_13.png")));
            tiles[13].collision = true;
            tiles[13].width = ScreenSettings.TILE_SIZE;
            tiles[13].height = ScreenSettings.TILE_SIZE;

            Tile metalRight = new Tile();
            tiles[15] = metalRight;
            tiles[15].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_15.png")));
            tiles[15].collision = true;
            tiles[15].width = ScreenSettings.TILE_SIZE;
            tiles[15].height = ScreenSettings.TILE_SIZE;

            Tile metalBottomLeft = new Tile();
            tiles[22] = metalBottomLeft;
            tiles[22].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_22.png")));
            tiles[22].collision = true;
            tiles[22].width = ScreenSettings.TILE_SIZE;
            tiles[22].height = ScreenSettings.TILE_SIZE;

            Tile metalBottomRight = new Tile();
            tiles[24] = metalBottomRight;
            tiles[24].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_24.png")));
            tiles[24].collision = true;
            tiles[24].width = ScreenSettings.TILE_SIZE;
            tiles[24].height = ScreenSettings.TILE_SIZE;

            Tile metalBottom = new Tile();
            tiles[23] = metalBottom;
            tiles[23].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_23.png")));
            tiles[23].collision = true;
            tiles[23].width = ScreenSettings.TILE_SIZE;
            tiles[23].height = ScreenSettings.TILE_SIZE;


            Tile fullMetal = new Tile();
            tiles[25] = fullMetal;
            tiles[25].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_25.png")));
            tiles[25].collision = true;
            tiles[25].width = ScreenSettings.TILE_SIZE;
            tiles[25].height = ScreenSettings.TILE_SIZE;

            Tile metalPlatform = new Tile();
            tiles[78] = metalPlatform;
            tiles[78].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_78.png")));
            tiles[78]. collision = true;
            tiles[78].width = ScreenSettings.TILE_SIZE;
            tiles[78].height = ScreenSettings.TILE_SIZE / 2;

            Tile checkpoint = new Tile();
            tiles[99] = checkpoint;
            tiles[99].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/checkpoint99.png")));
            tiles[99].collision = false;
            tiles[99].width = ScreenSettings.TILE_SIZE;
            tiles[99].height = ScreenSettings.TILE_SIZE;

            Tile exit = new Tile();
            tiles[100] = exit;
            tiles[100].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/exit100.png")));
            tiles[100].collision = false;
            tiles[100].width = ScreenSettings.TILE_SIZE * 2;
            tiles[100].height = ScreenSettings.TILE_SIZE * 2;

        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param tileNum identification number of the tile
     * @return a tile in the {@link tiles} array, empty tile (tiles[0]) as default
     */
    public Tile getTile(int tileNum) {
        if (tileNum >= 0 && tileNum < tiles.length) {
            return tiles[tileNum];
        }
        return tiles[0]; //empty tile as default
    }

    /**
     * @return the {@link tiles} array
     */
    public Tile[] getTiles() {
        return tiles;
    }

}
