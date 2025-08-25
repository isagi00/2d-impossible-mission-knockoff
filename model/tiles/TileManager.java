package model.tiles;

import model.ScreenSettings;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * model.
 * should store averything about tiles data
 * implement tile rules
 * provide tile accessors
 *
 * should not know about view or input
 */
import static java.util.Objects.requireNonNull;

/**
 * model.
 */
public class TileManager {
    //----------------------------------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------------------------------//
    Tile[] tile;

    public TileManager() {

        tile = new Tile[101];    //creating 81 kind of tiles, if you need more, change this

        getTileImage();
    }

    public void getTileImage() {
        try {
            // 0 is empty tile.
            Tile emptyTile = new Tile();
                BufferedImage emptyTileImage = new BufferedImage(ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = emptyTileImage.createGraphics();

                //g2d.setColor(Color.BLACK); //black
                g2d.setColor(new Color(0,0,0,0)); //transparent
                g2d.fillRect(0, 0, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);
                g2d.dispose();
            tile[0] = emptyTile;
            tile[0].image = emptyTileImage;
            tile[0].collision = false;
            tile[0].width = ScreenSettings.TILE_SIZE;
            tile[0].height = ScreenSettings.TILE_SIZE;



            Tile backGroundMetal = new Tile();
            tile[3] = backGroundMetal;
            tile[3].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_03.png")));
            tile[3].collision = false;
            tile[3].width = ScreenSettings.TILE_SIZE;
            tile[3].height = ScreenSettings.TILE_SIZE;

            Tile metalTopLeft = new Tile();
            tile[4] = metalTopLeft;
            tile[4].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_04.png")));
            tile[4].collision = true;
            tile[4].width = ScreenSettings.TILE_SIZE;
            tile[4].height = ScreenSettings.TILE_SIZE;

            Tile metalTop = new Tile();
            tile[5] = metalTop;
            tile[5].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_05.png")));
            tile[5].collision = true;
            tile[5].width = ScreenSettings.TILE_SIZE;
            tile[5].height = ScreenSettings.TILE_SIZE;

            Tile metalTopRight = new Tile();
            tile[6] = metalTopRight;
            tile[6].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_06.png")));
            tile[6].collision = true;
            tile[6].width = ScreenSettings.TILE_SIZE;
            tile[6].height = ScreenSettings.TILE_SIZE;

            Tile deathTile = new Tile();
            tile[9] = deathTile;
            tile[9].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_09.png")));
            tile[9].collision = true;
            tile[9].width = ScreenSettings.TILE_SIZE;
            tile[9].height = ScreenSettings.TILE_SIZE;

            Tile metalLeft = new Tile();
            tile[13] = metalLeft;
            tile[13].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_13.png")));
            tile[13].collision = true;
            tile[13].width = ScreenSettings.TILE_SIZE;
            tile[13].height = ScreenSettings.TILE_SIZE;

            Tile metalRight = new Tile();
            tile[15] = metalRight;
            tile[15].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_15.png")));
            tile[15].collision = true;
            tile[15].width = ScreenSettings.TILE_SIZE;
            tile[15].height = ScreenSettings.TILE_SIZE;

            Tile metalBottomLeft = new Tile();
            tile[22] = metalBottomLeft;
            tile[22].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_22.png")));
            tile[22].collision = true;
            tile[22].width = ScreenSettings.TILE_SIZE;
            tile[22].height = ScreenSettings.TILE_SIZE;

            Tile metalBottomRight = new Tile();
            tile[24] = metalBottomRight;
            tile[24].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_24.png")));
            tile[24].collision = true;
            tile[24].width = ScreenSettings.TILE_SIZE;
            tile[24].height = ScreenSettings.TILE_SIZE;

            Tile metalBottom = new Tile();
            tile[23] = metalBottom;
            tile[23].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_23.png")));
            tile[23].collision = true;
            tile[23].width = ScreenSettings.TILE_SIZE;
            tile[23].height = ScreenSettings.TILE_SIZE;


            Tile fullMetal = new Tile();
            tile[25] = fullMetal;
            tile[25].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_25.png")));
            tile[25].collision = true;
            tile[25].width = ScreenSettings.TILE_SIZE;
            tile[25].height = ScreenSettings.TILE_SIZE;

            Tile metalPlatform = new Tile();
            tile[78] = metalPlatform;
            tile[78].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/IndustrialTile_78.png")));
            tile[78]. collision = true;
            tile[78].width = ScreenSettings.TILE_SIZE;
            tile[78].height = ScreenSettings.TILE_SIZE / 2;

            Tile checkpoint = new Tile();
            tile[99] = checkpoint;
            tile[99].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/checkpoint99.png")));
            tile[99].collision = false;
            tile[99].width = ScreenSettings.TILE_SIZE;
            tile[99].height = ScreenSettings.TILE_SIZE;

            Tile exit = new Tile();
            tile[100] = exit;
            tile[100].image = ImageIO.read(requireNonNull(getClass().getClassLoader().getResourceAsStream("tiles/exit100.png")));
            tile[100].collision = false;
            tile[100].width = ScreenSettings.TILE_SIZE * 2;
            tile[100].height = ScreenSettings.TILE_SIZE * 2;

        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void draw(Graphics2D g2d, int[][] roomData) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (row < ScreenSettings.MAX_SCREEN_ROW  ){
            int tileNum = roomData[row][col];
            g2d.drawImage(tile[tileNum].image, x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE, null);
            col++;  //next tile
            x += ScreenSettings.TILE_SIZE;    //go forward 48 pixels
            if (col == ScreenSettings.MAX_SCREEN_COL) {
                col = 0;
                x = 0;
                row++;
                y += ScreenSettings.TILE_SIZE;
            }
        }
    }

    public Tile getTile(int tileNum) {
        if (tileNum >= 0 && tileNum < tile.length) {
            return tile[tileNum];
        }
        return tile[0]; //empty tile as default
    }

}
