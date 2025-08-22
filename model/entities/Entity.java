package model.entities;

import model.ScreenSettings;
import model.levels.LevelManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Observable;

public abstract class Entity extends Observable  {
    private int x;
    private int y;
    private int speed;
    private String direction;
    private int width;
    private int height;
    private LevelManager levelManager;

    public Entity(int x, int y, int width, int height, LevelManager levelManager) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.levelManager = levelManager;
    }


    public Entity(){
    }


    //----------------------------------------------------------------------------------------------------------------//
// COLLISION CHECKER
//----------------------------------------------------------------------------------------------------------------//
    //called in various methods, to check collision with tiles
    //check player collision with tiles (fuck you, collision logic)
    protected boolean checkCollisionWithTile(int x, int y) {  //x and playerY represent the current position of the player
        //get the array representing the current level
        int[][] roomData = levelManager.getCurrentRoomData();

        // calculate the hitbox positions
        // modifying this foe each hitbox
        int left = x ;                     //the top left, ex : x = 0
        int right = x + width;
        int top = y ;
        int bottom = y + height;

        // convert pixel coordinates into grid coordinates
        // allows to check which specific tiles the player is interacting with
        // for example:
        // if x is 100, then dividing this by the tilesize(48px) -> 2.08 -> player's left edge is on tile column 2
        int LeftCol = left / ScreenSettings.TILE_SIZE;        // (x / 48)
        int RightCol = (right - 1)  / ScreenSettings.TILE_SIZE;      // (x + width) / 48 -> players right edge is on tile column ...
        int TopRow = top / ScreenSettings.TILE_SIZE;         // (y / 48)          -> player's top edge is on tile row...
        int BottomRow = (bottom - 1) / ScreenSettings.TILE_SIZE;  // (y + height) / 48  -> player's bottom edge is on tile row...


//        //temporary solution, treating the room bounds as solid for now
//        //prevent array index out of bound exception... valid indices are 0 - 31, so if the player accesses the right bound it causes array out of bounds...
//        if (playerLeftCol < 0 || playerRightCol >= ScreenSettings.MAX_SCREEN_COL || playerTopRow < 0 || playerBottomRow >= ScreenSettings.MAX_SCREEN_ROW) {
//            return true;
//        }
//
//
//        //check tile collision along the player edges
        final int collisionPointsForEachEdge = 4; // 4 points per edge, so it prevents the player from slipping inside the tiles
        return
                //there will be 4 collision points for each player edge:
                //top edge
                checkTopEdgeCollision(collisionPointsForEachEdge, left, right, TopRow, roomData) ||
                        //bottom edge
                        checkBottomEdgeCollision(collisionPointsForEachEdge, left, right, BottomRow, roomData) ||
                        //left edge
                        checkLeftEdgeCollision(collisionPointsForEachEdge, bottom, top, LeftCol, roomData) ||
                        //right edge
                        checkRightEdgeCollision(collisionPointsForEachEdge, bottom, top, RightCol, roomData);

    }

    //called in checkCollisionWithTile()
    //TOP COLLISION
    protected boolean checkTopEdgeCollision(int collisionPointsForEachEdge, int left, int right, int playerTopRow, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int width = right - left;     //width of the player
            int totalIntervals = collisionPointsForEachEdge - 1;    //how many spaces between the points
            int currentInterval = i;            //the current point
            int distanceFromLeft = (width * currentInterval) / totalIntervals;  //distance from the left point
            /**
             * i = 0:   (100 * 0) / 3 = 0 (first is just 0, the left point is just the left point)
             * i = 1:   (100 * 1) / 3= 33 (distance is 33pixels from the left
             * i = 2:   (100 * 2) / 3 = 66 px (distance is 66 pixels from the left
             * i = 3:   (100 * 3) / 3 = 100px distance is 100 pixels, so it is the right edge
             */
            int x = left + distanceFromLeft;        //add the left edge to the distances from the left

            //find the current collision point tile grid position
            int playerTopCollisionPointCol = x / ScreenSettings.TILE_SIZE;       //convert pixel coordinate to the collision point's current column

            int tileNum = roomData[playerTopRow][playerTopCollisionPointCol];    //look up what tile type is at that grid position

            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {  //check if that tile collision is true or not
                return true;
            }

        }
        return false;
    }

    //called in checkCollisionWithTile()
    //BOTTOM COLLISION
    protected boolean checkBottomEdgeCollision(int collisionPointsForEachEdge, int left, int right, int playerBottomRow, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int width = right - left;     //width of the player
            int totalIntervals = collisionPointsForEachEdge - 1;    //how many spaces between the points
            int currentInterval = i;            //the current point
            int distanceFromLeft = (width * currentInterval) / totalIntervals;  //distance from the left point

            int x = left + distanceFromLeft;        //add the left edge to the distances from the left

            //find the current collision point tile grid position
            int playerBottomCollisionPointCol = x / ScreenSettings.TILE_SIZE;       //convert pixel coordinate to the collision point's current column

            int tileNum = roomData[playerBottomRow][playerBottomCollisionPointCol];    //look up what tile type is at that grid position
            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {

                return true;
            }
        }
        return false;
    }

    //called in checkCollisionWithTile()
    //LEFT COLLISION
    protected boolean checkLeftEdgeCollision(int collisionPointsForEachEdge, int bottom, int top, int playerLeftCol, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int height = bottom - top;
            int totalIntervals = collisionPointsForEachEdge - 1;
            int currentInterval = i;
            int distanceFromTop = (height * currentInterval) / totalIntervals;
            int y = top + distanceFromTop;

            int playerLeftCollisionPointRow = y / ScreenSettings.TILE_SIZE;

            int tileNum = roomData[playerLeftCollisionPointRow][playerLeftCol];
            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {

                return true;
            }
        }
        return false;
    }

    //called in checkCollisionWithTile()
    //RIGHT COLLISION
    protected boolean checkRightEdgeCollision(int collisionPointsForEachEdge, int bottom, int top, int playerRightCol, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int height = bottom - top;
            int totalIntervals = collisionPointsForEachEdge - 1;
            int currentInterval = i;
            int distanceFromTop = (height * currentInterval) / totalIntervals;
            int y = top + distanceFromTop;

            int playerRightCollisionPointRow = y / ScreenSettings.TILE_SIZE;

            int tileNum = roomData[playerRightCollisionPointRow][playerRightCol];

            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {
                return true;
            }
        }
        return false;
    }







    public int getX() { return x; }
    public int getY() { return y; }
    public String getDirection() { return direction; }
    public int getSpeed() { return speed; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }


    public void setX(int x) {
        if (this.x != x) {  //prevents redundant notifications
            this.x = x;

            setChanged();
            notifyObservers("x changed");
        }
    }

    public void setY(int y) {
        if (this.y != y) {
            this.y = y;

            setChanged();
            notifyObservers("y changed");
        }

    }

    public void setSpeed(int speed) {
        if (this.speed != speed) {
            this.speed = speed;

            setChanged();
            notifyObservers("speed changed");
        }

    }
    public void setDirection(String direction) {
        if (this.direction != direction) {
            this.direction = direction;

            setChanged();
            notifyObservers("direction changed");
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }




}
