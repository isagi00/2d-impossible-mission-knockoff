package model.entities;

import model.ScreenSettings;
import model.levels.LevelManager;

import java.util.Observable;

/**
 * model, abstract class of a game entity.
 * provides 2 constructors, one with required x, y, width, height and level manager parameters, and one empty constructor to allow
 * more flexible creation.
 * it extends the Observable class, so each entity has the utility methods to notify Observers. see {@link Observable}.
 */
public abstract class Entity extends Observable  {
    /**
     * x pixel coordinate of the entity
     */
    private int x;
    /**
     * y pixel coordinate of the entity
     */
    private int y;
    /**
     * base movement speed of the entity
     */
    private int speed;
    /**
     * direction of the entity
     */
    private String direction;
    /**
     * height in pixels of the entity
     */
    private int width;
    /**
     * height in pixels of the entity
     */
    private int height;
    /**
     * reference to the {@link LevelManager} model. it is used for the entity to interact with the
     * level.
     */
    private LevelManager levelManager;

    /**main entity constructor method, creates an 'entity' object with the provided values
     * @param x x coordinate of the entity
     * @param y y coordinate of the entity
     * @param width width of the entity
     * @param height height of the entity
     * @param levelManager level manager reference
     */
    public Entity(int x, int y, int width, int height, LevelManager levelManager) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.levelManager = levelManager;
    }


    /**
     * overloaded constructor: this constructor creates an 'entity' object without specifying
     * the x, y, width, height. use the setters to complete the object.
     */
    public Entity(){
    }

//----------------------------------------------------------------------------------------------------------------//
// COLLISION CHECKER
//----------------------------------------------------------------------------------------------------------------//
    /**checks if the entity is colliding against some tile.
     * each entity edge has 4 collision points, so it prevents the entity from slipping through tiles.
     * this number was chosen because it proved to be comfortable and enough for the collision detection.
     * it is divided in 4 other helper methods to check each edge:
     * {@link #checkTopEdgeCollision(int, int, int, int, int[][])},
     * {@link #checkBottomEdgeCollision(int, int, int, int, int[][])}
     * {@link #checkLeftEdgeCollision(int, int, int, int, int[][])}
     * {@link #checkRightEdgeCollision(int, int, int, int, int[][])}.
     * @param x entity's current x coordinate
     * @param y entity's current y coordinate
     * @return true if entity is colliding with some tile
     */
    protected boolean checkCollisionWithTile(int x, int y) {  //x and y represent the current position of the entity
        //get the array representing the current level
        int[][] roomData = levelManager.getCurrentRoomData();

        // calculate the real hit box positions
        // modifying this to tweak the hit box collision size
        int left = x ;                     //the top left, ex : x = 0
        int right = x + width;
        int top = y ;
        int bottom = y + height;

        // convert pixel coordinates into grid coordinates
        // allows to check which specific tiles the entity is interacting with
        // for example:
        // if x is 100, then dividing this by the tile size(48px) -> 2.08 -> entity's left edge is on tile column 2
        int LeftCol = left / ScreenSettings.TILE_SIZE;        // (x / 48)
        int RightCol = (right - 1)  / ScreenSettings.TILE_SIZE;      // (x + width) / 48 -> entity's right edge is on tile column ...
        int TopRow = top / ScreenSettings.TILE_SIZE;         // (y / 48)          -> entity's top edge is on tile row...
        int BottomRow = (bottom - 1) / ScreenSettings.TILE_SIZE;  // (y + height) / 48  -> entity's bottom edge is on tile row...

        //check tile collision along the entity edges
        final int collisionPointsForEachEdge = 4; // 4 points per edge, so it prevents the entity from slipping inside the tiles.

        return
                //top edge
                checkTopEdgeCollision(collisionPointsForEachEdge, left, right, TopRow, roomData) ||
                //bottom edge
                checkBottomEdgeCollision(collisionPointsForEachEdge, left, right, BottomRow, roomData) ||
                //left edge
                checkLeftEdgeCollision(collisionPointsForEachEdge, bottom, top, LeftCol, roomData) ||
                //right edge
                checkRightEdgeCollision(collisionPointsForEachEdge, bottom, top, RightCol, roomData);

    }

    /**checks the entity's top edge collision points. returns true if there's a collision.
     * @param collisionPointsForEachEdge number of collision points for this edge
     * @param left entity's leftmost point
     * @param right entity's rightmost point
     * @param entityTopRow entity's top row in grid coordinates
     * @param roomData current room's data
     * @return true if there's a collision with a tile
     */
    protected boolean checkTopEdgeCollision(int collisionPointsForEachEdge, int left, int right, int entityTopRow, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int width = right - left;     //width of the entity
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
            int entityTopCollisionPointCol = x / ScreenSettings.TILE_SIZE;       //convert pixel coordinate to the collision point's current column

            int tileNum = roomData[entityTopRow][entityTopCollisionPointCol];    //look up what tile type is at that grid position

            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {  //check if that tile collision is true or not
                return true;
            }

        }
        return false;
    }

    /**checks the entity's bottom edge collision points. returns true if there's a collision.
     * @param collisionPointsForEachEdge number of collision points for this edge
     * @param left entity's leftmost point
     * @param right entity's rightmost point
     * @param entityBottomRow entity's bottom row in grid coordinates
     * @param roomData current room's data
     * @return true if there's a collision with a tile
     */
    protected boolean checkBottomEdgeCollision(int collisionPointsForEachEdge, int left, int right, int entityBottomRow, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int width = right - left;     //width of the entity
            int totalIntervals = collisionPointsForEachEdge - 1;    //how many spaces between the points
            int currentInterval = i;            //the current point
            int distanceFromLeft = (width * currentInterval) / totalIntervals;  //distance from the left point

            int x = left + distanceFromLeft;        //add the left edge to the distances from the left

            //find the current collision point tile grid position
            int entityBottomCollisionPointCol = x / ScreenSettings.TILE_SIZE;       //convert pixel coordinate to the collision point's current column

            int tileNum = roomData[entityBottomRow][entityBottomCollisionPointCol];    //look up what tile type is at that grid position
            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {

                return true;
            }
        }
        return false;
    }

    /**checks the entity's left edge collision points. returns true if there's a collision.
     * @param collisionPointsForEachEdge number of collision points for this edge
     * @param bottom entity's bottommost point
     * @param top entity's topmost point
     * @param entityLeftCol entity's left col in grid coordinates
     * @param roomData current room's data
     * @return true if there's a collision with a tile
     */
    protected boolean checkLeftEdgeCollision(int collisionPointsForEachEdge, int bottom, int top, int entityLeftCol, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int height = bottom - top;
            int totalIntervals = collisionPointsForEachEdge - 1;
            int currentInterval = i;
            int distanceFromTop = (height * currentInterval) / totalIntervals;
            int y = top + distanceFromTop;

            int entityLeftCollisionPointRow = y / ScreenSettings.TILE_SIZE;

            int tileNum = roomData[entityLeftCollisionPointRow][entityLeftCol];
            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {

                return true;
            }
        }
        return false;
    }

    /**checks the entity's right edge collision points. returns true if there's a collision.
     * @param collisionPointsForEachEdge number of collision points for this edge
     * @param bottom entity's bottommost point
     * @param top entity's topmost point
     * @param entityRightCol entity's left col in grid coordinates
     * @param roomData current room's data
     * @return true if there's a collision with a tile
     */
    protected boolean checkRightEdgeCollision(int collisionPointsForEachEdge, int bottom, int top, int entityRightCol, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int height = bottom - top;
            int totalIntervals = collisionPointsForEachEdge - 1;
            int currentInterval = i;
            int distanceFromTop = (height * currentInterval) / totalIntervals;
            int y = top + distanceFromTop;

            int entityRightCollisionPointRow = y / ScreenSettings.TILE_SIZE;

            int tileNum = roomData[entityRightCollisionPointRow][entityRightCol];

            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {
                return true;
            }
        }
        return false;
    }


    /**
     * @return entity's current x pixel coordinate
     */
    public int getX() { return x; }

    /**
     * @return entity's current y pixel coordinate
     */
    public int getY() { return y; }

    /**
     * @return entity's current direction
     */
    public String getDirection() { return direction; }

    /**
     * @return entity's speed
     */
    public int getSpeed() { return speed; }

    /**
     * @return entity's width
     */
    public int getWidth() { return width; }

    /**
     * @return entity's height
     */
    public int getHeight() { return height; }

    /**sets the x of the object to the provided x coordinate.
     * if it is the same as the previous x coordinate, it won't set it.
     */
    public void setX(int x) {
        if (this.x != x) {
            this.x = x;
        }
    }

    /**sets the y of the object to the provided y coordinate.
     * if it is the same as the previous y coordinate, it won't set it.
     * @param y y pixel coordinate
     */
    public void setY(int y) {
        if (this.y != y) {
            this.y = y;
        }
    }

    /**sets the entity's speed to the specified speed
     * @param speed speed of the entity
     */
    public void setSpeed(int speed) {
        if (this.speed != speed) {
            this.speed = speed;
        }
    }

    /**sets the entity's direction to the provided direction
     * @param direction direction of the entity
     */
    public void setDirection(String direction) {
        if (this.direction != direction) {
            this.direction = direction;
        }
    }
    /**sets the entity's width to the provided parameter
     * @param width height of the entity
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**sets the entity's height to the provided parameter
     * @param height height of the entity
     */
    public void setHeight(int height) {
        this.height = height;
    }




}
