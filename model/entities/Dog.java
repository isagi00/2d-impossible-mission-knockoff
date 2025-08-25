package model.entities;

import model.ScreenSettings;
import model.levels.LevelManager;

import java.awt.*;

public class Dog extends Entity {
    //other models
    private Player player;
    private LevelManager levelManager;

    //dog constants
    private int SPEED = 4;
    private int CHASE_RANGE = ScreenSettings.TILE_SIZE * 3;
    private final int MAX_VERTICAL_DISTANCE = ScreenSettings.TILE_SIZE;
    private int PATROL_RANGE;
    private int currentPatrolPosition = 0;

    //dog states
    private boolean isIdle = true;
    private boolean isMoving = false;
    private boolean isChasing = false;
    private boolean isDisabled = false;
    private boolean movingRight = true;
    private boolean wallInFront = false;
    private boolean groundAhead;


    public Dog(int x, int y, Player player, LevelManager levelManager) {
        super (x, y, ScreenSettings.TILE_SIZE * 2, ScreenSettings.TILE_SIZE * 2, levelManager);
        //models
        this.player = player;
        this.levelManager = levelManager;
        //dog constants
        this.PATROL_RANGE = ScreenSettings.TILE_SIZE * 10;

        setDirection("right");
    }

    public void update(){
        if (!player.getGameOver()) {
            if (checkCollisionWithPlayer()) {
                System.out.println("[Dog][update()]setting game over to true on player. checkCollisionWithPlayer()");
                player.setGameOver(true);
            }
        }

        if (!isDisabled){
            boolean sameLevel = Math.abs(player.getY() - getY()) <= MAX_VERTICAL_DISTANCE;
            boolean playerWithinChaseRange = false;

            if (sameLevel){
                if (getDirection().equals("right") && player.getX() > getX()){
                    int distance = player.getX() - getX();
                    if (distance <= CHASE_RANGE){
                        playerWithinChaseRange = true;
                    }
                }
                else if (getDirection().equals("left") && player.getX() < getX()){
                    int distance = getX()- player.getX();
                    if (distance <= CHASE_RANGE){
                        playerWithinChaseRange = true;
                    }
                }
            }

            if (playerWithinChaseRange){
                chasePlayer();
                setChanged();
                notifyObservers("chasing player");
            }
            else{
                isChasing = false;

                patrol();
                setChanged();
                notifyObservers("patrolling");
            }
        }
    }



    private boolean checkCollisionWithPlayer(){
        Rectangle dogHitBox = new Rectangle(getX() + 10, getY() + 50, getWidth() - 20, getHeight() - 50);
        Rectangle playerHitbox = new Rectangle(player.getX() + 10, player.getY() + 3, player.getWidth() - 20, player.getHeight());
        return dogHitBox.intersects(playerHitbox);
    }

    private void chasePlayer(){
        int chaseSpeed = SPEED * 2;

        if (getDirection().equals("right")){
            movingRight = true;

            int newX = getX() + chaseSpeed;
            int groundCheckX = newX + getWidth() - 5;
            int groundCheckY = getY() + getHeight() + 1;
            groundAhead = isPointOnSolidTile(groundCheckX, groundCheckY);

            //check at the edge of where the dog will be
            //check at the tile boundary
            int wallCheckCol = (newX + getWidth()) / ScreenSettings.TILE_SIZE;  //-> gives the tile col
            int wallCheckX = wallCheckCol * ScreenSettings.TILE_SIZE;
            wallInFront = isPointOnSolidTile(wallCheckX, getY() + getHeight()/2);

//            System.out.println("[Dog][chasePlayer()]tile on bottom right " + groundAhead);

            if (!wallInFront && newX < ScreenSettings.SCREEN_WIDTH - getWidth() && groundAhead) {
                setX(newX);
                isChasing = true;
                isMoving = true;
                isIdle = false;
                currentPatrolPosition ++;
            }
            else{
                setDirection("left");
                isChasing = false;
                isMoving = true;
                isIdle = false;
                movingRight = false;
                currentPatrolPosition --;
            }
        }
        if (!movingRight){  //moving left
            int newX = getX() - chaseSpeed;

            int groundCheckX = newX + 5;
            int groundCheckY = getY() + getHeight() + 1;
            groundAhead = isPointOnSolidTile(groundCheckX, groundCheckY);

            //wall detection for left movement
            int wallCheckCol = (newX + getWidth() - 1) / ScreenSettings.TILE_SIZE;
            int wallCheckX = wallCheckCol * ScreenSettings.TILE_SIZE;
            wallInFront = isPointOnSolidTile(wallCheckX, getY() + getHeight()/2);

            if (!wallInFront && newX > 0 && groundAhead) {
                setX(newX);
                isChasing = true;
                isMoving = true;
                isIdle = false;
                currentPatrolPosition --;
            }
            else{
                setDirection("right");
                isChasing = false;
                isMoving = true;
                isIdle = false;
                movingRight = true;
                currentPatrolPosition ++;
            }

        }
    }

    private void patrol(){
        if (movingRight){
            if(currentPatrolPosition < PATROL_RANGE){
                int newX = getX() + SPEED;
                int groundCheckX = newX + getWidth() - 5;
                int groundCheckY = getY() + getHeight() + 1;
                groundAhead = isPointOnSolidTile(groundCheckX, groundCheckY);

                // wall detection
                int wallCheckCol = (newX + getWidth()) / ScreenSettings.TILE_SIZE;
                int wallCheckX = wallCheckCol * ScreenSettings.TILE_SIZE;
                wallInFront = isPointOnSolidTile(wallCheckX, getY() + getHeight()/2);

                if (!wallInFront && newX < ScreenSettings.SCREEN_WIDTH - getWidth() && groundAhead) {
                    setX(newX);
                    setDirection("right");
                    isIdle = false;
                    isMoving = true;
                    currentPatrolPosition++;
                }
                else{   //found a wall or edge
                    movingRight = false;
                }
            }
            else{   //reached max patrol range
                movingRight = false;
            }
        }
        if (!movingRight){
            if (currentPatrolPosition > 0){
                int newX = getX() - SPEED;

                int groundCheckX = newX + 5;
                int groundCheckY = getY() + getHeight() + 1;
                groundAhead = isPointOnSolidTile(groundCheckX, groundCheckY);

                //wall detection for left movement
                int wallCheckCol = (newX + getWidth() - 1) / ScreenSettings.TILE_SIZE;
                int wallCheckX = wallCheckCol * ScreenSettings.TILE_SIZE;
                wallInFront = isPointOnSolidTile(wallCheckX, getY() + getHeight()/2);


                if(!wallInFront && newX > 0 && groundAhead ){
                    setX(newX);
                    setDirection("left");
                    isIdle = false;
                    isMoving = true;
                    currentPatrolPosition --;
                }
                else{       //wall or edge
                    movingRight = true;
                }
            }
            else{   //reached the starting point to the left
                movingRight = true;
            }
        }
    }

    /**
     * checks if a single point is on a solid tile
     * @param x pixel x-coordinate to check
     * @param y pixel y-coordinate to check
     * @return true if the point is on a solid tile
     */
    private boolean isPointOnSolidTile(int x, int y) {
        //convert point to tile coordinates
        int col = x / ScreenSettings.TILE_SIZE;
        int row = y / ScreenSettings.TILE_SIZE;

        //check if within room bounds
        if (row < 0 || row >= ScreenSettings.MAX_SCREEN_ROW ||
                col < 0 || col >= ScreenSettings.MAX_SCREEN_COL) {
            return false; // Or true if you want boundaries to be solid
        }

        //get the tile at that position
        int tileNum = levelManager.getCurrentRoomData()[row][col];

        //check if it's a solid tile
        return tileNum > 0 && levelManager.getTile(tileNum).collision;
    }


    public boolean getIsIdle(){
        return isIdle;
    }

    public boolean getIsMoving(){
        return isMoving;
    }

    public boolean getIsDisabled(){
        return isDisabled;
    }

    public boolean getIsChasing(){
        return isChasing;
    }

    public boolean getWallInFront(){
        return wallInFront;
    }

    public int getCurrentPatrolPosition(){
        return currentPatrolPosition;
    }
    public boolean getGroundAhead(){
        return groundAhead;
    }

    public void deactivate(){
        this.isDisabled = true;
    }
}