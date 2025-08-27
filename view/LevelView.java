package view;

import model.ScreenSettings;
import model.levels.LevelManager;
import model.levels.Tile;
import model.levels.TileManager;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class LevelView implements Observer {
    private LevelManager levelManager;
    private TileManager tileManager;

    private int[][] roomData;
    private Tile[] tiles;

    public LevelView(LevelManager levelManager, TileManager tileManager) {
        this.levelManager = levelManager;
        this.tileManager = tileManager;
        this.roomData = levelManager.getCurrentRoomData();  //initialize the room data when creating the LevelView instance
        this.tiles = tileManager.getTiles();    //the tiles never change
    }




    public void draw(Graphics2D g2d) {
        int x = 0;
        int y = 0;
        //iterate over the room data matrix
        for (int row = 0; row < ScreenSettings.MAX_SCREEN_ROW; row ++){ //for each row
            for (int col = 0; col < ScreenSettings.MAX_SCREEN_COL; col ++){     //for each col
                int tileNum = roomData[row][col];       //get the tile number from the room data
                g2d.drawImage(tiles[tileNum].image, x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE, null);    //draw the tile image at x and y position
                x += ScreenSettings.TILE_SIZE;  //go forward on the x-axis when finished drawing the tile
            }
            x = 0;  //finished row, reset the x-axis
            y += ScreenSettings.TILE_SIZE;  //move forward on the y axis
        }

    }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LevelManager) {
            switch((String) (arg)){
                case "level changed":
                    this.roomData = levelManager.getCurrentRoomData();      //update the room data when level changes
            }
        }


    }
}
