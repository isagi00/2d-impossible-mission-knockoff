package model.entities;

import model.ScreenSettings;
import model.levels.LevelManager;
import view.AudioManager;

import java.awt.*;

/**
 * model of the dog {@link Entity}, it extends the {@link Entity} abstract class.
 * the dog is implemented in a way that is more dangerous than the {@link Drone}: it has a bigger hit box, it has a faster
 * base movement speed, bigger chase range, and keeps chasing the player if he tries to jump around it.
 *
 */
public class Dog extends Entity {
    //other models
    /**
     * reference to the {@link Player} instance
     */
    private final Player player;
    /**
     * reference to the {@link LevelManager} instance
     */
    private final LevelManager levelManager;

    //dog constants
    /**
     * base movement speed of the dog
     */
    private final int SPEED = 4;
    /**
     * max allowed chase range of the dog.
     */
    private final int CHASE_RANGE = ScreenSettings.TILE_SIZE * 5;
    /**
     * the maximum vertical distance that the dog can have between the player.
     * it is around 2 tiles tall, so the dog will continue to chase the player if he tries to jump around the dog
     */
    private final int MAX_VERTICAL_DISTANCE = ScreenSettings.TILE_SIZE * 2;
    /**
     * the allowed patrol range of the dog. the dog will not go past this range.
     */
    private final int PATROL_RANGE = ScreenSettings.TILE_SIZE * 10;
    /**
     * total time that the dog has to wait before changing direction / moving.
     */
    private int WAIT_TIME = 180;
    /**
     * the wait counter that helps track how much time there is left to wait.
     * it is set to {@link #WAIT_TIME} if the dog kills the player, finds a ledge or wall in front of him.
     */
    private int waitCounter = 0;
    /**
     * current patrol position of the dog.
     * if it is 0, then the dog has reached the maximum patrol range on the left side, so it will start to wait, then it turns around to the other side.
     * same thing happens when the dog has reached the max {@link #PATROL_RANGE}.
     */
    private int currentPatrolPosition = 0;

    /**
     * flag that indicates if the dog is idle or not.
     * it is set to true if the dog finds a wall in front of itself, if detects that it doesn't have ground ahead or if it kills the player.
     */
    //dog states
    private boolean isIdle = true;
    /**
     * flag that indicates if the dog is moving or not
     */
    private boolean isMoving = false;
    /**
     * flag that indicates if the dog is chasing the player or not.
     * the dog is implemented in a way that if it starts chasing the player, it will continue to chase
     * if the player tries to jump around the dog.
     */
    private boolean isChasing = false;
    /**
     * flag that indicates if the dog is disabled or not.
     * even when disabled, the dog still has a hit box, so if the player touches the dog, then it will kill him
     * the dog can be disabled by the player through the computer.
     */
    private boolean isDisabled = false;
    /**
     * flag that indicates if the dog is moving to the right
     */
    private boolean movingRight = true;
    /**
     * flag that indicates if there is a wall in front of the dog.
     * if this flag is set to true, then the dog will start waiting, then turning around into the other direction
     */
    private boolean wallInFront = false;
    /**
     * flag that indicates if there's ground ahead of the dog.
     * if this flag is false, then the dog will start waiting, then turning around into the other direction
     */
    private boolean groundAhead;
    /**
     * flag that indicates if the dog is waiting or not.
     * while this flag is true, the dog will display the idle animation.
     * it gets set to true when the dog finds a wall, or checks if there is not ground ahead, or after killing the player
     */
    private boolean isWaiting = false;

    //required for sound
    /**
     * flag that indicates if the dog was moving or not.
     * this was needed because otherwise multiple notifications were being sent to the {@link AudioManager}.
     */
    private boolean wasMoving = false;
    /**
     * last time the footstep sound being played
     */
    private long lastFootStepTime = 0;
    /**
     * interval between dog footstep sounds
     */
    private final long FOOTSTEP_SOUND_INTERVAL = 200; //200ms between footsteps


    /**default constructor of {@link Entity}.
     * the dog's starting position and sizes are defined here.
     * note that the dog is an observable (got it from the {@link Entity} abstract class, so that it is easier to send
     * notification to the {@link AudioManager} to play the dog footsteps.
     * @param x x coordinate of the dog
     * @param y y coordinate of the dog
     * @param player reference to the player instance. {@link Player}
     * @param levelManager reference to the level manager instance. {@link LevelManager}
     */
    public Dog(int x, int y, Player player, LevelManager levelManager) {
        super (x, y, ScreenSettings.TILE_SIZE * 2, ScreenSettings.TILE_SIZE * 2, levelManager);
        //models
        this.player = player;
        this.levelManager = levelManager;
        //dog constants
        setDirection("right");

        this.addObserver(AudioManager.getInstance());
    }

    /**
     * core dog logic.
     * handles waiting logic by decreasing the {@link #waitCounter} if the dog is in the state of {@link #isWaiting}.
     * handles the player collision via {@link #checkCollisionWithPlayer()}.
     * the chase logic is handled in a different method, {@link #chasePlayer()} and the patrol logic is handled in the {@link #patrol()} method.
     * handles the notification of what sound to play to the {@link AudioManager}
     */
    public void update(){
        //if the dog sees that there is no ground ahead or these is a wall, it will wait a bit then change direction
        if(isWaiting){
            waitCounter--;
            isIdle = true;
            if(waitCounter <= 0){   //if finished waiting
                isWaiting = false;
                isIdle = false;     //resume movement after waiting
            }
            return;         //pause the update method here when the dog is waiting
        }


        //collision check with player
        if (!player.getGameOver()) {
            if (checkCollisionWithPlayer()) {
                System.out.println("[Dog][update()]setting game over to true on player. checkCollisionWithPlayer()");
                player.setGameOver(true);
            }
        }


        if (!isDisabled){
            //check the dog and the player are on the same platform
            boolean sameLevel = Math.abs(player.getY() - getY()) <= MAX_VERTICAL_DISTANCE;
            //check the player is within in front of the dog
            boolean playerWithinChaseRange = false;
            if (sameLevel){
                int distance = Math.abs(player.getX() - getX());
                if (distance < CHASE_RANGE && !player.getGameOver()){
                    playerWithinChaseRange = true;
                    isChasing = true;
                }
                else if (player.getGameOver()){
                    startWaiting();
                    isMoving = false;
                    isChasing = false;
                    isIdle = true;      //???
                }
            }

            if (isChasing && playerWithinChaseRange){
                chasePlayer();
            }
            else{
                isChasing = false;
                patrol();
            }


            //sound logic: it is the same as the player's
            if ((isChasing || isMoving) && !isWaiting){
                long currentTime = System.currentTimeMillis();

                if (!wasMoving){        //play sound immediately if dog is moving
                    setChanged();
                    notifyObservers("dog moving");
                    clearChanged();
                    lastFootStepTime = currentTime;
                }
                else if (currentTime - lastFootStepTime >= FOOTSTEP_SOUND_INTERVAL){    //if dog moving for a while play sound at intervals
                    setChanged();
                    notifyObservers("dog moving");
                    clearChanged();
                    lastFootStepTime = currentTime;
                }
            }
            wasMoving = isMoving;
        }
        else {  //dog is disabled
            isMoving = false;
            isChasing = false;
            isIdle = false;
        }
    }


    /**checks the collision with the player.
     * both hit boxes are implemented as rectangles, and if they overlap, then returns true
     * @return true if the dog hit box intersects with the player hit box
     */
    private boolean checkCollisionWithPlayer(){
        Rectangle dogHitBox = new Rectangle(getX() + 20, getY() + 65, getWidth() - 40, getHeight() - 65);
        Rectangle playerHitbox = new Rectangle(player.getX() + 10, player.getY() + 3, player.getWidth() - 20, player.getHeight());
        return dogHitBox.intersects(playerHitbox);
    }

    /**
     * dog's player chasing logic.
     * the base speed when chasing is boosted. it checks if there's a wall in front or if there's no tile below it.
     * if there are so problems, the dog keeps chasing the player.
     * note that the dog is implemented as a more dangerous enemy than the {@link Drone}, so it will keep chasing the player if the player jumps
     * around it. if it kills the player, then it will start waiting a bit, before returning to patrol.
     * this was to implement a more 'real' effect to the dog.
     */
    private void chasePlayer(){
        int chaseSpeed = (int) (SPEED * 1.7);
        //sets the current dog direction when chasing, if the player jumped over it and is on the left then it direction will be right, left otherwise
        if ((player.getX() > getX() && getDirection().equals("left")) || (player.getX() < getX() && getDirection().equals("right"))) {
            setDirection(player.getX() > getX() ? "right" : "left");
            movingRight = player.getX() > getX();
        }

        //moving right
        if (getDirection().equals("right")){
            movingRight = true;
            int newX = getX() + chaseSpeed;

            //ground ahead check
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
            else if (wallInFront || newX > ScreenSettings.SCREEN_WIDTH - getWidth() || !groundAhead ) {
                startWaiting();     //when changin direction, wait for a little bit before turning
                setDirection("left");
                isChasing = false;
                isMoving = false;
                isIdle = true;
                movingRight = false;
                currentPatrolPosition --;
            }
        }
        else{  //moving left
            int newX = getX() - chaseSpeed;
            //check if there's gound ahead of the dog
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
                startWaiting();      //when stopping, wait for a little bit
                setDirection("right");
                isChasing = false;
                isMoving = false;
                isIdle = true;
                movingRight = true;
                currentPatrolPosition ++;
            }

        }
    }

    /**
     * dog's patrol logic.
     * it checks if there's a wall ahead and if there is a ground tile for it to go.
     * if not, or if the dog has reached the maximum patrol range, it will start waiting for a little bit before turning around
     * and going in the other direction.
     */
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
                    startWaiting();     //start waiting for a lil bit
                    movingRight = false;
                }
            }
            else{   //reached max patrol range
                startWaiting();         //wait for lil
                movingRight = false;
            }
        }
        else{
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
                    startWaiting();     //wait for a lil
                    isIdle = true;
                    movingRight = true;
                }
            }
            else{   //reached the starting point to the left
                startWaiting();     //wait
                isIdle = true;
                movingRight = true;
            }
        }
    }

    /**
     * makes the dog start waiting. it sets isIdle flag to true and starts the {@link #waitCounter} = {@link #WAIT_TIME}.
     */
    private void startWaiting(){
       isWaiting = true;
       waitCounter = WAIT_TIME;
       isIdle = true;
       isMoving = false;
    }


    /**
     * checks if a single point at the provided x and y coordinate is on a solid tile
     * @param x x coordinate to check
     * @param y y coordinate to check
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


    /**
     * @return the {@link #isIdle} flag
     */
    public boolean getIsIdle(){
        return isIdle;
    }

    /**
     * @return the {@link #isMoving} flag
     */
    public boolean getIsMoving(){
        return isMoving;
    }

    /**
     * @return the {@link #isDisabled} flag
     */
    public boolean getIsDisabled(){
        return isDisabled;
    }

    /**
     * @return the {@link #isChasing} flag
     */
    public boolean getIsChasing(){
        return isChasing;
    }

    /**
     * @return the {@link #wallInFront} flag
     */
    public boolean getWallInFront(){
        return wallInFront;
    }

    /**
     * @return the current patrol position. {@link #currentPatrolPosition}
     */
    public int getCurrentPatrolPosition(){
        return currentPatrolPosition;
    }

    /**
     * @return {@link #groundAhead } flag
     */
    public boolean getGroundAhead(){
        return groundAhead;
    }

    /**
     * @return {@link #WAIT_TIME}
     */
    public int getWaitTime(){
        return WAIT_TIME;
    }

    /**
     * deactivates the dog. it is used mainly in {@link model.levels.Room} deactivate all dogs method.
     */
    public void deactivate(){
        this.isDisabled = true;
    }

    /**
     * @return the {@link #waitCounter} field
     */
    public int getWaitCounter(){
        return waitCounter;
    }
}