package model.entities;

import model.ScreenSettings;
import model.levels.LevelManager;
import view.AudioManager;

import java.awt.*;

public class Drone extends Entity {
    //----------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------//
    //other models
    private Player player;
    private LevelManager levelManager;

    //drone constants
    private int SPEED = 2;
    private int ATTACK_RANGE = ScreenSettings.TILE_SIZE * 2;
    private int CHASE_RANGE = ScreenSettings.TILE_SIZE * 4;
    private final int MAX_VERTICAL_DISTANCE = ScreenSettings.TILE_SIZE; //chase the player only if its on the same platform
    private int PATROL_RANGE;
    private int currentPatrolPosition = 0;

    //drone states
    private boolean isIdle = true;
    private boolean isMoving = false;
    private boolean isChasing = false;
    private boolean isDisabled = false;
    private boolean movingRight = true;
    private boolean wallInFront = false;
    private boolean groundAhead = false;

    //required for sound
    private boolean wasMoving = false;
    private long lastFootStepTime = 0;
    private long FOOTSTEP_SOUND_INTERVAL = 230; //230ms between footsteps

    //----------------------------------------------------------------------------------------//
    // CONSTRUCTOR
    //----------------------------------------------------------------------------------------//

    public Drone(int x, int y, Player player, LevelManager levelManager) {
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE, levelManager);

        this.PATROL_RANGE = ScreenSettings.TILE_SIZE * 10;
        this.player = player;
        this.levelManager = levelManager;
        setDirection("right");

        this.addObserver(AudioManager.getInstance());
    }

    //----------------------------------------------------------------------------------------//
    // MAIN UPDATE METHOD
    //----------------------------------------------------------------------------------------//

    public void update() {
        // if drone is disabled and player touches him still kills the player
        if (!player.getGameOver()) {     //if player is not dead
            if (checkCollisionWithPlayer()) {           //check collision with him
                System.out.println("[Drone][update()]setting game over to true on player. checkCollisionWithPlayer()");
                player.setGameOver(true);           // ded
            }
        }

        if (!isDisabled) {
            boolean sameLevel = Math.abs(player.getY() - getY()) <= MAX_VERTICAL_DISTANCE;      //check if drone and player are on the same platform (level)
            boolean playerWithinChaseRange = false;

            if (sameLevel) {            //if they are on the same level
                //if drone facing right and player is on the right
                if (getDirection().equals("right") && player.getX() > getX()) {
                    int distance = player.getX() - getX();
                    if (distance <= CHASE_RANGE) {              //if he also is within chase range, then chase him
                        playerWithinChaseRange = true;
                    }
                }
                //same thing but for left facing drone
                else if (getDirection().equals("left") && player.getX() < getX()) {
                    int distance = getX() - player.getX();
                    if (distance <= CHASE_RANGE) {
                        playerWithinChaseRange = true;
                    }
                }
            }

            if (playerWithinChaseRange) {        //if the player is within range chase
                chasePlayer();
            }
            else {                    //if not just patrol normally
                isChasing = false;
                patrol();
            }

            //sound logic: same as the player
            if ((isChasing || isMoving) && !isIdle) {
                long currentTime = System.currentTimeMillis();
                if (!wasMoving){        //play sound once if started moving
                    setChanged();
                    notifyObservers("drone moving");
                    clearChanged();
                    lastFootStepTime = currentTime;
                }
                else if(currentTime - lastFootStepTime >= FOOTSTEP_SOUND_INTERVAL){     //play sound at interval if keeps moving
                    setChanged();
                    notifyObservers("drone moving");
                    clearChanged();
                    lastFootStepTime = currentTime;
                }
            }
            wasMoving = isMoving;

        }


    }

    //----------------------------------------------------------------------------------------//
    // methods called in the main update method
    //----------------------------------------------------------------------------------------//

    private void patrol() {
        if (movingRight) {
            if (currentPatrolPosition < PATROL_RANGE) {
                int newX = getX() + SPEED;
                int groundCheckX = newX + getWidth() - 5;
                int groundCheckY = getY() + getHeight() + 1;
                groundAhead = isPointOnSolidTile(groundCheckX, groundCheckY);

                // check at the tile boundary for walls
                int wallCheckCol = (newX + getWidth()) / ScreenSettings.TILE_SIZE;
                int wallCheckX = wallCheckCol * ScreenSettings.TILE_SIZE;
                wallInFront = isPointOnSolidTile(wallCheckX, getY() + getHeight()/2);

                if (!wallInFront &&
                        newX < ScreenSettings.SCREEN_WIDTH - getWidth() &&
                        groundAhead) {

                    setX(newX);
                    setDirection("right");
                    isIdle = false;
                    isMoving = true;
                    currentPatrolPosition++;
                } else {       //found a wall or edge
                    movingRight = false;
                }
            } else {   // reached right max patrol range, turn around
                movingRight = false;
            }
        } else {
            if (currentPatrolPosition > 0) {
                int newX = getX() - SPEED;
                int groundCheckX = newX + 5;
                int groundCheckY = getY() + getHeight() + 1;
                groundAhead = isPointOnSolidTile(groundCheckX, groundCheckY);

                // Check at the tile boundary for walls
                int wallCheckCol = (newX + getWidth() - 1) / ScreenSettings.TILE_SIZE;
                int wallCheckX = wallCheckCol * ScreenSettings.TILE_SIZE;
                wallInFront = isPointOnSolidTile(wallCheckX, getY() + getHeight()/2);

                if (!wallInFront &&
                        newX > 0 &&
                        groundAhead) {

                    setX(newX);
                    setDirection("left");
                    isIdle = false;
                    isMoving = true;
                    currentPatrolPosition--;
                } else {   //found a wall or edge
                    movingRight = true;
                }
            } else {   // reached the starting point, turn right
                movingRight = true;
            }
        }
    }

    private void chasePlayer() {
        int chaseSpeed = (int) (SPEED * 1.8);

        if (getDirection().equals("right")) {
            movingRight = true;
            int newX = getX() + chaseSpeed;

            // check ground ahead of new position
            int groundCheckX = newX + getWidth() - 5;
            int groundCheckY = getY() + getHeight() + 1;
            groundAhead = isPointOnSolidTile(groundCheckX, groundCheckY);

            // check wall at new position
            int wallCheckCol = (newX + getWidth()) / ScreenSettings.TILE_SIZE;
            int wallCheckX = wallCheckCol * ScreenSettings.TILE_SIZE;
            wallInFront = isPointOnSolidTile(wallCheckX, getY() + getHeight()/2);

            if (!wallInFront && newX < ScreenSettings.SCREEN_WIDTH - getWidth() && groundAhead) {   //no wall, gorund ahead and is on the left of the screen bounds
                setX(newX);
                isChasing = true;
                isMoving = true;
                isIdle = false;
                currentPatrolPosition++;
            } else {        //hit a wall or some other condition turned false
                setDirection("left");
                isChasing = false;
                isMoving = true;
                isIdle = false;
                movingRight = false;
                currentPatrolPosition--;
            }
        }
        else { // direction is "left"
            int newX = getX() - chaseSpeed;

            // check ground ahead of new position
            int groundCheckX = newX + 5;
            int groundCheckY = getY() + getHeight() + 1;
            groundAhead = isPointOnSolidTile(groundCheckX, groundCheckY);

            // check wall at new position
            int wallCheckCol = (newX + getWidth() - 1) / ScreenSettings.TILE_SIZE;
            int wallCheckX = wallCheckCol * ScreenSettings.TILE_SIZE;
            wallInFront = isPointOnSolidTile(wallCheckX, getY() + getHeight()/2);

            if (!wallInFront &&
                    newX > 0 &&
                    groundAhead) {

                setX(newX);
                isChasing = true;
                isMoving = true;
                isIdle = false;
                currentPatrolPosition--;
            } else {
                setDirection("right");
                isChasing = false;
                isMoving = true;
                isIdle = false;
                movingRight = true;
                currentPatrolPosition++;
            }
        }
    }

    private boolean checkCollisionWithPlayer() {
        Rectangle droneHitbox = new Rectangle(getX() + 10, getY() + 25, getWidth() - 20, getHeight() - 25);
        Rectangle playerHitbox = new Rectangle(player.getX() + 10, player.getY() + 3, player.getWidth() - 20, player.getHeight());
        return droneHitbox.intersects(playerHitbox);
    }

    /**
     * checks if a single point is on a solid tile
     * @param x Pixel x-coordinate to check
     * @param y Pixel y-coordinate to check
     * @return true if the point is on a solid tile
     */
    private boolean isPointOnSolidTile(int x, int y) {
        //convert point to tile coordinates
        int col = x / ScreenSettings.TILE_SIZE;
        int row = y / ScreenSettings.TILE_SIZE;

        //check if within room bounds
        if (row < 0 || row >= ScreenSettings.MAX_SCREEN_ROW ||
                col < 0 || col >= ScreenSettings.MAX_SCREEN_COL) {
            return false;
        }

        //get the tile at that position
        int tileNum = levelManager.getCurrentRoomData()[row][col];

        //check if it's a solid tile
        return tileNum > 0 && levelManager.getTile(tileNum).collision;
    }

    public void deactivate() {       //used in room, to disable drone through computer.
        isDisabled = true;
    }

    //----------------------------------------------------------------------------------------//
    // GETTERS
    //----------------------------------------------------------------------------------------//

    public boolean getIsIdle() {
        return isIdle;
    }

    public int getPATROL_RANGE() {
        return PATROL_RANGE;
    }

    public boolean getIsMoving() {
        return isMoving;
    }

    public int getCurrentPatrolPosition() {
        return currentPatrolPosition;
    }

    public boolean getWallInFront() {
        return wallInFront;
    }

    public boolean getGroundAhead() {
        return groundAhead;
    }

    public boolean getIsChasing() {
        return isChasing;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }
}