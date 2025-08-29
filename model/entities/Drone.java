package model.entities;

import model.ScreenSettings;
import model.levels.LevelManager;
import view.AudioManager;

import java.awt.*;

/**
 * model of the 'drone' enemy, extends the {@link Entity} abstract class.
 * a drone is the base enemy of the game, its hit box is small, chase range is little.
 * it will chase the player if near enough and in front of the drone, if not it will patrol a fixed
 * distance, {@link #PATROL_RANGE}.
 * the player is allowed to jump on top of the drone to avoid it.
 */
public class Drone extends Entity {

    //----------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------//
    /**
     *reference to the {@link Player} instance
     */
    //other models
    private final Player player;
    /**
     * reference to the {@link LevelManager} instance
     */
    private final LevelManager levelManager;

    /**
     * drone's base (patrol) speed.
     */
    //drone constants
    private final int SPEED = 2;
    /**
     * the chase range of the drone. if the player is within this distance, then the drone will start chasing.
     */
    private final int CHASE_RANGE = ScreenSettings.TILE_SIZE * 4;
    /**
     * max vertical distance allowed for the drone to detect the player.
     * if the player is on the same y range and within chase range of the drone, then the drone will start chasing.
     * this prevents drones from different level heights to chase the player.
     * it is being set low, allowing the drone do be jumped over by the player.
     */
    private final int MAX_VERTICAL_DISTANCE = ScreenSettings.TILE_SIZE; //chase the player only if its on the same platform
    /**
     * drone patrol range. it is the maximum distance that the drone can travel to the right before changing direction.
     */
    private final int PATROL_RANGE;
    /**
     * current patrol position. if it is 0, then the drone will change direction and move right,
     * if it is equals to the {@link #PATROL_RANGE}, then it will change direction and move left.
     */
    private int currentPatrolPosition = 0;
    //drone states
    /**
     * flag that indicates if the drone is idle or not.
     * note that it is not used a lot, since the drone behaviour is only: move left and right, and chase the player
     * if it is in front of the drone. it is never idle.
     */
    private boolean isIdle = true;
    /**
     * flag that indicates if the drone is moving or not
     */
    private boolean isMoving = false;
    /**
     * flag that indicates if the drone is chasing the player or not.
     * it gets reset once the player is not in front of the drone.
     * this way, the player has a degree of freedom to move around the level.
     * note that the drone is made to be a base level enemy, so it won't be that dangerous.
     */
    private boolean isChasing = false;
    /**
     * flag that indicates if the drone is disabled or not.
     * if it's not disabled, then the drone will chase or patrol depending on the situation.
     * even if disabled, the drone will still have a hit box that can kill the player.
     */
    private boolean isDisabled = false;
    /**
     * flag that indicates if the drone is moving in the right direction
     * helps with the drone movement logic.
     */
    private boolean movingRight = true;
    /**
     * flag that indicates if the drone has a wall in front.
     * if the drone has a wall ahead, it will turn around
     */
    private boolean wallInFront = false;
    /**
     * flag that indicates if the drone has a ground tile ahead.
     * if not, the drone will turn around.
     */
    private boolean groundAhead = false;


    //required for sound
    /**
     * checks if the drone was moving or not. this is done because otherwise multiple events would fire to the
     * {@link AudioManager}, instead of notifying it once
     */
    private boolean wasMoving = false;
    /**
     * last foot step time
     */
    private long lastFootStepTime = 0;
    /**
     * interval between footsteps to play
     */
    private final long FOOTSTEP_SOUND_INTERVAL = 230; //230ms between footsteps

    //----------------------------------------------------------------------------------------//
    // CONSTRUCTOR
    //----------------------------------------------------------------------------------------//

    /**drone extends the entity class, so base methods are already defined, such as getX(), get().
     * it is an observable for the {@link AudioManager}, so it knows what sound to display. the view counterpart of the drone, {@link view.entityViews.DroneView} handles the rendering
     * via getter methods instead of the observer pattern.
     * the {@link Entity} default constructor is used here.
     * @param x x coordinate of the drone
     * @param y y coordinate of the drone
     * @param player reference to the {@link Player} instance
     * @param levelManager reference to the {@link LevelManager} instance
     */
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

    /**
     * drone's core logic is contained here. handles the drone collision with the player via {@link #checkCollisionWithPlayer()}.
     * checks if the player is in front of the drone and on the same level as the drone, and if yes it will chase the player via {@link #chasePlayer()}.
     * if not the drone will patrol on a fixed range defined in the constructor: {@link #PATROL_RANGE} VIA {@link #patrol()}.
     */
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

    /**
     * drone's patrolling logic. if the drone is not chasing, then it will execute this behaviour as default.
     * uses base speed to move around. if the drone hits a wall or if there's no ground below it, it will change direction
     * it has a fixed patrol range {@link #PATROL_RANGE}, defined in the constructor. the drone won't patrol past this defined range.
     * uses {@link #movingRight} flag to control the direction in which the drone is moving.
     *
     */
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

                if (!wallInFront && newX < ScreenSettings.SCREEN_WIDTH - getWidth() && groundAhead) {

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

    /**
     * contains chase logic.
     * drone's base speed is boosted. checks if there's a wall and ground ahead of the drone.
     * if not, it will stop chasing and patrol normally. it uses {@link #movingRight} flag to move left or right
     */
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

            if (!wallInFront && newX > 0 && groundAhead) {

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

    /**representing the drone hit box and the player's hit box as rectangles, and checking if these rectangle intersect or not.
     * @return true if the drone hit box intersects with the player's hit box
     */
    private boolean checkCollisionWithPlayer() {
        Rectangle droneHitbox = new Rectangle(getX() + 10, getY() + 25, getWidth() - 20, getHeight() - 25);
        Rectangle playerHitbox = new Rectangle(player.getX() + 10, player.getY() + 3, player.getWidth() - 20, player.getHeight());
        return droneHitbox.intersects(playerHitbox);
    }

    /**
     * checks if a single point at the provided x and y is on a solid tile
     * @param x  x-coordinate to check
     * @param y y-coordinate to check
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

    /**
     * disabled the drone.
     * sets the {@link #isDisabled} flag to true, it is used mainly in {@link model.levels.Room},
     * for disabling drones in the room
     */
    public void deactivate() {       //used in room, to disable drone through computer.
        isDisabled = true;
    }

    //----------------------------------------------------------------------------------------//
    // GETTERS
    //----------------------------------------------------------------------------------------//

    /**
     * @return true if the drone is idle
     */
    public boolean getIsIdle() {
        return isIdle;
    }

    /**
     * @return the drone's patrol range
     */
    public int getPATROL_RANGE() {
        return PATROL_RANGE;
    }

    /**
     * @return if the drone is moving or not
     */
    public boolean getIsMoving() {
        return isMoving;
    }

    /**
     * @return the drone's current patrol position
     */
    public int getCurrentPatrolPosition() {
        return currentPatrolPosition;
    }

    /**
     * @return if the there's a wall in front of the drone or not
     */
    public boolean getWallInFront() {
        return wallInFront;
    }

    /**
     * @return if there's ground ahead of the drone or not
     */
    public boolean getGroundAhead() {
        return groundAhead;
    }

    /**
     * @return if the drone is chasing or not
     */
    public boolean getIsChasing() {
        return isChasing;
    }

    /**
     * @return if the drone is disabled or not
     */
    public boolean getIsDisabled() {
        return isDisabled;
    }
}