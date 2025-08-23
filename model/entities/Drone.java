package model.entities;

import model.ScreenSettings;
import model.levels.LevelManager;

import java.awt.*;

public class Drone extends Entity{          //note that entity already extends observable
    //----------------------------------------------------------------------------------------//
    // DRONE STATES
    //----------------------------------------------------------------------------------------//
    private boolean isIdle = true;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private boolean isChasing = false;
    private boolean isDisabled = false;
    private boolean movingRight = true;
    private boolean wallInFront = false;
    //----------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------//
    //other models
    private Player player;
    private LevelManager levelManager;

    //patrol related
    private int patrolRange;
    private int currentPatrolPosition = 0;

    //drone constants
    private int SPEED = 2;
    private int ATTACK_RANGE = ScreenSettings.TILE_SIZE * 2;
    private int CHASE_RANGE = ScreenSettings.TILE_SIZE * 4;
    private final int MAX_VERTICAL_DISTANCE = ScreenSettings.TILE_SIZE; //chase the player only if its on the same platform


    //----------------------------------------------------------------------------------------//
    // CONSTRUCTOR
    //----------------------------------------------------------------------------------------//

    public Drone(int x, int y, Player player, LevelManager levelManager) {
        super(x, y, ScreenSettings.TILE_SIZE , ScreenSettings.TILE_SIZE, levelManager);

        this.patrolRange = ScreenSettings.TILE_SIZE * 10;
        this.player = player;
        this.levelManager = levelManager;
        setDirection("right");
    }



    //----------------------------------------------------------------------------------------//
    // MAIN UPDATE METHOD
    //----------------------------------------------------------------------------------------//

    public void update() {
        // if drone is disabled and player touches him make him cry anyways muhahaha
        if (!player.getGameOver()){     //if player is not dead
            if (checkCollisionWithPlayer()) {           //check collision with him
                System.out.println("setting game over to true on player. checkCollisionWithPlayer()");
                player.setGameOver(true);           // ded
            }
        }

        if (!isDisabled) {
            boolean sameLevel = Math.abs(player.getY() - getY()) <= MAX_VERTICAL_DISTANCE;      //check if drone and player are on the same platfrom (level)
            boolean playerInFront = false;

            if (sameLevel) {            //if they are on the same level
                //if drone facing right and player is on the right
                if (getDirection().equals("right") && player.getX() > getX()) {
                    int distance = player.getX() - getX();
                    if (distance <= CHASE_RANGE) {              //if he also is withing chase range, then chase him
                        playerInFront = true;
                    }
                }
                //same thing but for left facing drone
                else if (getDirection().equals("left") && player.getX() < getX()) {
                    int distance = getX() - player.getX();
                    if (distance <= CHASE_RANGE) {
                        playerInFront = true;
                    }
                }
            }
            if (playerInFront) {        //if the player is withing range chase
                chasePlayer();
                setChanged();
                notifyObservers("chasing player");
            } else {                    //if not just patrol normally
                isChasing = false;
                patrol();

                setChanged();
                notifyObservers("patrolling");
            }

        }


    }

    //----------------------------------------------------------------------------------------//
    // methods called in the main update method
    //----------------------------------------------------------------------------------------//


    private void patrol(){
        if (movingRight) {
            // Move right until we reach the patrol boundary
            if (currentPatrolPosition < patrolRange) {

                wallInFront = checkCollisionWithTile(getX() + SPEED, getY() - 1);
                //note that it is getY() - 1, , this way it checks slightly on top, so that it returns false.
                //if there is no -1, it will checkCollisionWithTile will return true because it checks the bottom tile(floor).

                //this checks the bottom right corner of the drone, and if there is not a tile while the drone is facing right, then it bounces back.
                //this help avoiding cases where the drones walk on air
                int bottomRightCornerX = getX() + getWidth();
                int bottomRightCornerY = getY() + getHeight();
                boolean tileOnBottomRight = true;
                if(!checkCollisionWithTile(bottomRightCornerX, bottomRightCornerY)){
                    tileOnBottomRight = false;
                }

                if (!wallInFront && getX() < ScreenSettings.SCREEN_WIDTH && checkCollisionWithTile(getX(), getY() + 1) && tileOnBottomRight) {  //check collision with tile to the right, if there is not, then move right
                    setX(getX() + SPEED);
                    setDirection("right");
                    isIdle = false;
                    isMoving = true;
                    currentPatrolPosition++;
                }
                else{       //if there is wall to the right, then go back to the left
                    movingRight = false;
                }
            }
            // reached right max patrol range, turn around
            else {
                movingRight = false;
            }
        }

        if(!movingRight) {
            // move left until the drone returns to the starting position
            if (currentPatrolPosition > 0) {
                wallInFront = checkCollisionWithTile(getX() - SPEED, getY() - 1);

                boolean tileOnBottomLeft = true;
                int bottomLeftCornerX = getX();
                int bottomLeftCornerY = getY() + getHeight();
                if(!checkCollisionWithTile(bottomLeftCornerX, bottomLeftCornerY)){
                    tileOnBottomLeft = false;
                }

                if(!wallInFront && getX() > 0 && checkCollisionWithTile(getX(), getY() + 1) && tileOnBottomLeft) {   //check collision with tile to the left, if there is not, then move left
                    setX(getX() - SPEED);
                    setDirection("left");
                    isIdle = false;
                    isMoving = true;
                    currentPatrolPosition--;
                }
                else{   //if there is wall to the left, then go back to the right
                    movingRight = true;
                }
            }
            // reached the starting point, turn right
            else {
                movingRight = true;
            }
        }
    }


    private void chasePlayer() {
        int chaseSpeed = (int) (SPEED * 1.5);
        if (getDirection().equals("right")) {       //if drone direction is right
            movingRight = true;
            int newX = getX() + chaseSpeed;
            int bottomRightCornerX = getX() + getWidth();
            int bottomRightCornerY = getY() + getHeight();
            boolean tileOnBottomRight = true;
            if(!checkCollisionWithTile(bottomRightCornerX, bottomRightCornerY)){
                tileOnBottomRight = false;
            }

            System.out.println("tile on bottom right " + tileOnBottomRight);
            if (!checkCollisionWithTile(newX, getY() - 1)  && getX() < ScreenSettings.SCREEN_WIDTH && tileOnBottomRight && movingRight) {
                //if there is no wall and floot under  AND it is lesser than the screen width, start chasing
                setX(newX);
                isChasing = true;
                isMoving = true;
                isIdle = false;
                currentPatrolPosition++;
            } else {            //if theres wall turn around and stop chasing
                setDirection("left");
                isChasing = false;
                isMoving = true;
                isIdle = false;
                movingRight = false;
                currentPatrolPosition--;
            }
        }
        if(!movingRight) { // direction is "left"
            int newX = getX() - chaseSpeed;
            boolean tileOnBottomLeft = true;
            int bottomLeftCornerX = getX();
            int bottomLeftCornerY = getY() + getHeight();
            if(!checkCollisionWithTile(bottomLeftCornerX, bottomLeftCornerY)){
                tileOnBottomLeft = false;
            }
            System.out.println("tile on bottom left " + tileOnBottomLeft);


            if (!checkCollisionWithTile(newX, getY() - 1) && getX() > 0 && tileOnBottomLeft) {
                //no wall && there is tile under && x is greater than 0 left side of screen -> chase
                setX(newX);
                isChasing = true;
                isMoving = true;
                isIdle = false;
                currentPatrolPosition--;
            } else {        //wall or the is no tile under -> turn around and stop chasing
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
        Rectangle droneHitbox = new Rectangle(getX() + 5, getY() + 15, getWidth() - 10, getHeight() - 15);
        Rectangle playerHitbox = new Rectangle(player.getX() + 10, player.getY() + 3, player.getWidth() - 20, player.getHeight());


//        g2d.drawRect(drone.getX() + 5, drone.getY() + 15, drone.getWidth() - 10, drone.getHeight() - 15);
//        g2d.drawRect(player.getX() + 10, player.getY() + 3, player.width - 20, player.height);


//        System.out.println("droen rect rect: " + droneHitbox);
//        System.out.println("player rect: " + playerHitbox);
//        System.out.println("intersects? " + droneHitbox.intersects(playerHitbox));

        return droneHitbox.intersects(playerHitbox);
    }


    public void deactivate(){       //used in room, to disable drone through computer.
        isDisabled = true;
    }















    public boolean getIsIdle() {
        return isIdle;
    }

    public int getPatrolRange() {
        return patrolRange;
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
    public boolean getIsChasing() {
        return isChasing;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }






}
