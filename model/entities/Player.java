package model.entities;

import model.ScoreTracker;
import model.ScreenSettings;
import model.interactableObjects.*;
import model.inventoryrelated.ComputerCard;
import model.inventoryrelated.Inventory;
import model.inventoryrelated.Item;
import model.levels.LevelManager;
import model.levels.Room;
import view.AudioManager;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * model.
 * should:
 * store player data
 * implement player rules
 * provide data accessors
 *
 * should not:
 * should not know about the views or input
 */
public class Player extends Entity {
    //----------------------------------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------------------------------//
    //other models
    private LevelManager levelManager;
    private Room currentRoom;
    public int width = (int)(ScreenSettings.TILE_SIZE * 1);
    public int height = (int)(ScreenSettings.TILE_SIZE * 1);
    //movement status bools
    private boolean isMoving = false;
    private boolean isOnGround = false;
    private boolean isJumping = false;
    //interaction related
    private boolean interactionCompleted = false;
    private int interactionHoldTime = 0;
    private final int SEARCH_COMPLETE_TIME = 180;    //3 seconds at 60 fps
    private boolean showingInteractionPrompt = false;
    private InteractableObject currentInteractable = null;
    private final int MAX_ALLOWED_OBJECT_DISTANCE = 40; //50 pixels near
    //jump related
    private int verticalSpeed = 0;
    private int GRAVITY = 1;
    private int MAX_JUMP_SPEED = -17;
    //ladder climbing related
    private boolean isClimbing = false;
    private Ladder currentLadder = null;
    private static final int CLIMB_SPEED = 4;
    //inventory
    public Inventory inventory;
    //gameover
    private boolean gameOver;
    private int lastCheckpointX = 0;
    private int lastCheckpointY = 0;
    //extraction
    private boolean extracted = false;
    //score tracker
    private ScoreTracker scoreTracker;

    //sound related
    private boolean wasGameOver = false; //otherwise the audio replays multiple times
    private boolean wasMoving = false;
    private long lastFootStepTime = 0;
    private long FOOTSTEP_SOUND_INTERVAL = 250; //250 ms between footsteps


    //----------------------------------------------------------------------------------------------------------------//
    // PLAYER INITIALIZATION
    //----------------------------------------------------------------------------------------------------------------//
    //constructor
    public Player() {
        setDefaultValues();
        this.inventory = new Inventory(5);
        this.scoreTracker = new ScoreTracker();

        this.addObserver(scoreTracker);
        this.addObserver(AudioManager.getInstance());
    }

    //called in Player() constructor
    //sets player default values
    private void setDefaultValues() {
        //spawn location
        setX(100);
        setY( height + ScreenSettings.TILE_SIZE);
        setWidth(ScreenSettings.TILE_SIZE);
        setHeight(ScreenSettings.TILE_SIZE);
        setSpeed(6);
        setDirection("right");
        isOnGround = false;
        isJumping = false;
        gameOver = false;
    }





//----------------------------------------------------------------------------------------------------------------//
// UPDATE METHOD
//----------------------------------------------------------------------------------------------------------------//

    // method is called in GameController run() method
    // method is called 60 times per second
    public void update(boolean upPressed, boolean downPressed, boolean leftPressed, boolean rightPressed,
                       boolean spacePressed, boolean ePressed,
                       boolean upArrowPressed, boolean downArrowPressed, boolean enterPressed) {

        if (this.currentRoom != levelManager.getCurrentRoom()) {    //if the current player's current room doesnt match the level manager's current room
            this.currentRoom = levelManager.getCurrentRoom();   //then get the current room from level manager
        }

        int playerX = getX();
        int playerY = getY();
        int speed = getSpeed();
        int playerWidth = getWidth();
        int playerHeight = getHeight();

        //player movement, gravity, climbing, interactions logic
        if (!gameOver) {                //if player is alive
            showingInteractionPrompt = updateCurrentInteractable();


            //computer menu navigation (if the menu is visible, the player won't be able to move
            if (currentInteractable instanceof Computer computer && computer.getIsMenuVisible()){
//                System.out.println("current interactable: " + currentInteractable);
//                System.out.println("DEBUG: Menu is visible, selected option: " + computer.getSelectedMenuOption());
//                System.out.println("computer .getismenuvisible()" + computer.getIsMenuVisible());
                if (upArrowPressed) {
                    computer.navigateMenuUp();
                    System.out.println("menu up : player update()");
                }
                if (downArrowPressed) {
                    computer.navigateMenuDown();
                    System.out.println("menu down : player update()");
                }
                if (enterPressed) {
                    computer.selectMenuOption(this);
                    computer.cancelMenu();  //after selecting an option close the menu
                }
                return; //return here early, so all the movement logic gets skipped
            }




            //apply gravity to the player
            verticalSpeed += GRAVITY;
            String lastDirection = getDirection();
            if(!checkCollisionWithTile(playerX, playerY + verticalSpeed)) {     //if there is no collision underneath the player
                setY(playerY + verticalSpeed);
                if (verticalSpeed > 0){     //vertical speed increasing means falling down
                    isOnGround = false;
                    isMoving = false;
                    setDirection("down");
                }
                else if (verticalSpeed < 0){    //only way to increase vertical speed is to jump
                    isOnGround = false;
                    isJumping = true;
                    isMoving = false;
                    setDirection("up");
                }
            }
            else{               //hit the ground / there is collision
                if(verticalSpeed > 0){          //vertical speed is positive, so if player was falling
                    isOnGround = true;          //then hit the ground
                    isJumping = false;
                    isMoving = false;
                    verticalSpeed = 0;          //reset the vertical speed
                    setY(playerY);
                    setDirection(lastDirection);
                }
                else if (verticalSpeed < 0){    //if the vertical speed was negative then the player was going up and hit a ceiling
                    verticalSpeed = 0;
                    isMoving = false;
                    setY(getY() + 1);       //bring the player slightly under the tile
                    setDirection(lastDirection);
                }
            }


            //ladder movement
            isClimbing = false;
            if (isNearLadder()) {        //check if player is near ladder
//                System.out.println("current ladder : " + currentLadder);
                int climbSpeed = CLIMB_SPEED;
                boolean canMoveUp = true;
                boolean canMoveDown = true;
                int ladderY = currentLadder.getY();
                int ladderHeight = currentLadder.getHeight();
                if (playerY <= ladderY - playerHeight && checkCollisionWithTile(playerX, playerY)) {      //check if the player is already at the top of the ladder
                    canMoveUp = false;          //if hes already at the top player cannot go more up
                }
                if (playerY + playerHeight >= ladderY + ladderHeight && checkCollisionWithTile(playerX, playerY)) {       //check if player is at the bottom of the ladder && there is a tile
                    canMoveDown = false;        //then he cant move down anymore
                }
                if (upPressed && canMoveUp && !checkCollisionWithTile(playerX, playerY - climbSpeed)) {
                    setY(playerY - climbSpeed);
                    setDirection("up");
                    isClimbing = true;
                    isOnGround = false;
                    isJumping = false;
                    isMoving = false;
                    verticalSpeed = 0;          //dont apply any kind of vertical speed when climbing, otherwise it shoots the player to the ground
                    if(upPressed && checkCollisionWithTile(playerX, playerY - climbSpeed ) && (leftPressed || rightPressed)) {
                        isClimbing = true;
                        isMoving = false;
                    }
                }
                else if (downPressed && canMoveDown && !checkCollisionWithTile(playerX, playerY + climbSpeed)) {
                    setY(playerY + climbSpeed);
                    setDirection("down");
                    isClimbing = true;
                    isOnGround = false;
                    isJumping = false;
                    isMoving = false;
                    verticalSpeed = 0;
                }
                else if (upPressed && checkCollisionWithTile(playerX, playerY - climbSpeed)) {
                    //edge case: if player is trying to climb a ladder but theres is tile on top of him, just reset his position back to the original one
                    //and keep the 'is climbing' state. this prevents funky visuals (was toggling between is moving and is climbing states really fast)
                    setY(playerY);
                    setDirection("up");
                    isClimbing = true;
                    isMoving = false;
                    verticalSpeed = 0;  //very important, otherwise the gravity keeps stacking on the vertical speed, and teleports the player to the ground otherwise.
                }

                else {                  //if the player is neither climbing up or down, then reset the flag.
                    isClimbing = false;
                }
            }


            //horizontal movement
            isMoving = false;
            if (leftPressed && !checkCollisionWithTile(playerX - speed, playerY)) {         //left is pressed and there is no tile on the left
                setX(playerX - speed);
                setDirection("left");
                if (isJumping || isClimbing) {
                    isMoving = false;
                }
                else{
                    isMoving = true;
                }
            }
            else if (rightPressed && !checkCollisionWithTile(playerX + speed, playerY)) {   //right is pressed and there is no tile on the right
                setX(playerX + speed);
                setDirection("right");
                if (isJumping || isClimbing) {
                    isMoving = false;
                }
                else{
                    isMoving = true;
                }
            }
            //horizontal movement sound logic
            if(isMoving && !gameOver) {
                long currentTime = System.currentTimeMillis();

                if (!wasMoving){    //if player just started moving then play the sound right away
                    setChanged();
                    notifyObservers("player moved");
                    clearChanged();
                    lastFootStepTime = currentTime;
                }
                else if (currentTime - lastFootStepTime >= FOOTSTEP_SOUND_INTERVAL){    //if the player has been moving for a while, then play the sound every 200 ms
                    setChanged();
                    notifyObservers("player moved");
                    clearChanged();
                    lastFootStepTime = currentTime;
                }
            }
            wasMoving = isMoving;



            //player jump
            if(spacePressed && isOnGround) {
                isJumping = true;
                isOnGround = false;
                isMoving = false;
                verticalSpeed = MAX_JUMP_SPEED;

                setChanged();
                notifyObservers("player jumped");
                clearChanged();
            }

            //interactable object handling (player interaction)
            showingInteractionPrompt = updateCurrentInteractable();      //check if player is near interactable object
            if (showingInteractionPrompt && currentInteractable != null) {
                currentInteractable.startInteraction(ePressed);     //start the interaction
                if (currentInteractable.isInteractionCompleted()) {
                    interact();                                     //actually interact with the object
                }
                else{
                    interactionCompleted = false;
                }
            }

        }

        //player reset handling
        if(gameOver && !wasGameOver) {      //if player dies, then reset to last checkpoint after waiting 2 seconds
            setChanged();
            notifyObservers("player died");
            clearChanged();

            isMoving = false;
            isOnGround = false;
            isJumping = false;
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(()->{
//                System.out.println("resetting to checkpoint...");
                resetToCheckpoint();
                scheduler.shutdown();
            }, 2, TimeUnit.SECONDS);
        }
        wasGameOver = gameOver;

        if(extracted){
            setChanged();
            notifyObservers("player extracted");
            clearChanged();
        }
    }

//----------------------------------------------------------------------------------------------------------------//
// METHODS CALLED IN UPDATE (PLAYER MOVEMENT, GRAVITY, INTERACTION) (LOGIC)
// ----------------------------------------------------------------------------------------------------------------//



    //called in update()
    //called to check if player is near ladder
    private boolean isNearLadder() {
        List<InteractableObject> objects = currentRoom.getInteractiveObjects();
        for (InteractableObject obj : objects) {
            if (obj instanceof Ladder ladder) {
//                System.out.println("ladder : " + ladder);
                boolean horizontalBounds = getX() + width >= ladder.getX() && getX() <= ladder.getX() + ladder.getWidth();

                int playerBottom = getY() + height;
                int ladderBottom =ladder.getY() + ladder.getHeight();
                boolean verticalBounds = playerBottom >= ladder.getY() - CLIMB_SPEED - 5 && getY() <= ladderBottom + 5;

                if(horizontalBounds && verticalBounds){
                    currentLadder = ladder;
                    return true;
                }
            }
        }
        return false;
    }



    /**compute if player is near to an object or not. this is the logic that determines whether the player is near an object.
     * called in updateCurrentInteractable()
     * @param obj   current interactable object
     * @return true if the player is near enough, false otherwise
     */
    private boolean isWithinDistance(InteractableObject obj){
        //get player and object centers
        int playerCenterX = getX() + width/2;
        int playerCenterY = getY() + height/2;
        int objectCenterX = obj.getX() + obj.getWidth()/2;
        int objectCenterY = obj.getY() + obj.getHeight()/2;
        //find the difference in x and y coordinates
        int distanceX = playerCenterX - objectCenterX;
        int distanceY = playerCenterY - objectCenterY;
        //pythagorean theorem : straight line distance between 2 points = sqrt(dx^2 + dy^2) -> then check with the allowed max distance to an object
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY) < MAX_ALLOWED_OBJECT_DISTANCE;
    }

    /**
     * check if player is near interactable object, sets the currentInteractable field if it is near.
     * called in update() method
     * @return true if isWithinDistance returns true and sets the currentInteractable, false if isWithinDistance returns false, and sets the currentInteractable to false
     */
    private boolean updateCurrentInteractable() {
        if (inventory.isInventoryFull() && !checkPlayerHasCard()){  //if the inventory is actually full from cards, dont display the interaction prompt
            return false;
        }

        List<InteractableObject> objects = currentRoom.getInteractiveObjects();
        // if there's an active computer menu, keep it as current interactable
        for (InteractableObject obj : objects) {
            if (obj instanceof Computer computer && computer.getIsMenuVisible()) {
                currentInteractable = computer;
                return true;
            }
        }
        // regular proximity check for other objects
        for (InteractableObject obj : objects) {                //iterate over all interactable objects
            //if the computer card is taken, then dont count it as an interactable object anymore
            if (obj instanceof Card card && card.getIsTaken()) {
                continue;
            }
            //if it is a ladder, dont count as interactable object -> initially set it as an 'interactable object' because it is different from a 'tile', and it is simpler to add in the level with the level editor
            if (obj instanceof Ladder ladder) {
                continue;
            }
            //show the computer interaction prompt only if the player has the computer card in his inventory
            if (obj instanceof Computer computer) {
                if (checkPlayerHasCard()) {         //check if player has card in inventory
                    if (isWithinDistance(obj)) {
                        currentInteractable = computer;
                        return true;
                    }
                }
                continue;
            }
            //check if the object is within distance the inventory is not full
            if (isWithinDistance(obj) && (!inventory.isInventoryFull() || (inventory.isInventoryFull() && checkPlayerHasCard()) ) ) {
                //return true if: object is within distance and if the inventory is not full OR it is full but there is a card (dont consider it full)
                currentInteractable = obj;
                return true;
            }
        }
        currentInteractable = null;
        return false;
    }


    private void interact(){
        if ( currentInteractable instanceof Computer && checkPlayerHasCard()){
            ComputerCard card = inventory.getComputerCard();
            inventory.removeComputerCard(card);
        }
        //interact with the object
        if( currentInteractable != null ) {
            currentInteractable.interact(this); //player INITIATES THE INTERACTION, object interacts with player
            //COMMAND PATTERN
            /**
             * the player calls interact on the object
             * the objects executes its own interact logic
             */
            if(currentInteractable.isInteractionCompleted() && !interactionCompleted) {
                interactionCompleted = true;
                if (currentInteractable instanceof PaperBox) {
                    setChanged();
                    notifyObservers("paper box opened");
                    System.out.println("notifiying observers paper box opened");
                }
                else if (currentInteractable instanceof RedBox) {
                    setChanged();
                    notifyObservers("red box opened");
                    System.out.println("notifiying observers red box opened");
                }
                else if (currentInteractable instanceof MetalLocker){
                    setChanged();
                    notifyObservers("metal locker opened");
                    System.out.println("notifiying observers metal locker opened");
                }
                else if (currentInteractable instanceof WoodLocker){
                    setChanged();
                    notifyObservers("wood locker opened");
                    System.out.println("notifiying observers wood locker opened");
                }
            }
        }
    }

    private void resetToCheckpoint(){
        if (lastCheckpointX != 0 && lastCheckpointY != 0 && gameOver) {
            setX(lastCheckpointX);
            setY(lastCheckpointY - 40);
        }
        gameOver = false;   //reset the flag
    }

    private boolean checkPlayerHasCard(){
        boolean hasCard = false;
        for ( Item item : inventory.getItems() ) {
            if (item instanceof ComputerCard) {
                hasCard = true;
            }
        }
        return hasCard;
    }

//----------------------------------------------------------------------------------------------------------------//
// COLLISION CHECKER
//----------------------------------------------------------------------------------------------------------------//
    //called in various methods, to check collision with tiles
    //check player collision with tiles (fuck you, collision logic)
    @Override
    protected boolean checkCollisionWithTile(int playerX, int playerY) {  //playerX and playerY represent the current position of the player
        //get the array representing the current level
        int[][] roomData = levelManager.getCurrentRoomData();

        // calculate the hit box positions
        int left = playerX + 10;                     //the top left, ex : x = 0
        int right = playerX + width - 10;
        int top = playerY + 3;
        int bottom = playerY + height;

        // convert pixel coordinates into grid coordinates
        // allows to check which specific tiles the player is interacting with
        // for example:
        // if playerX is 100, then dividing this by the tilesize(48px) -> 2.08 -> player's left edge is on tile column 2
        int playerLeftCol = left / ScreenSettings.TILE_SIZE;        // (x / 48)
        int playerRightCol = (right - 1)  / ScreenSettings.TILE_SIZE;      // (x + width) / 48 -> players right edge is on tile column ...
        int playerTopRow = top / ScreenSettings.TILE_SIZE;         // (y / 48)          -> player's top edge is on tile row...
        int playerBottomRow = (bottom - 1) / ScreenSettings.TILE_SIZE;  // (y + height) / 48  -> player's bottom edge is on tile row...

        //check tile collision along the player edges
        final int collisionPointsForEachEdge = 4; // 4 points per edge, so it prevents the player from slipping inside the tiles
        return
        //there will be 4 collision points for each player edge:
        //top edge
        checkTopEdgeCollision(collisionPointsForEachEdge, left, right, playerTopRow, roomData) ||
        //bottom edge
        checkBottomEdgeCollision(collisionPointsForEachEdge, left, right, playerBottomRow, roomData) ||
        //left edge
        checkLeftEdgeCollision(collisionPointsForEachEdge, bottom, top, playerLeftCol, roomData) ||
        //right edge
        checkRightEdgeCollision(collisionPointsForEachEdge, bottom, top, playerRightCol, roomData);

    }

    @Override
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
            //death tile
            if (tileNum == 9 && !gameOver) {
                gameOver = true;        //red tile
            }

            if (tileNum == 100 && !extracted) {
                extracted = true;
            }

            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {  //check if that tile collision is true or not
                return true;
            }

        }
        return false;
    }

    @Override
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
            //death tile collision
            if (tileNum == 9 && !gameOver) {
                gameOver = true;        //red tile
            }

            //checkpoint tile
            if (tileNum == 99){
                lastCheckpointX = getX() + (ScreenSettings.TILE_SIZE / 2);
                lastCheckpointY = getY() + (ScreenSettings.TILE_SIZE / 2);
//                System.out.println(" bottomcollision -> lastcheckpoint registered: " + lastCheckpointX + " " + lastCheckpointY);
            }

            if (tileNum == 100 && !extracted) {
                extracted = true;
            }
            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {

                return true;
            }
        }
        return false;
    }

    @Override
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
            //death tile collision
            if (tileNum == 9 && !gameOver){      //red tile
                gameOver = true;
            }
            //extraction tile
            if (tileNum == 100 && !extracted) {
                extracted = true;
            }
            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {
                return true;
            }
        }
        return false;
    }

    @Override
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
            //death tile
            if (tileNum == 9 && !gameOver){
                gameOver = true;
            }
            //extraction tile
            if (tileNum == 100 && !extracted) {
                extracted = true;
            }
            //tile collision
            if (tileNum > 0 && levelManager.getTile(tileNum).collision) {
                return true;
            }
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //getters
    //----------------------------------------------------------------------------------------------------------------//

    public boolean getIsMoving(){
        return isMoving;
    }

    public boolean getIsJumping(){
        return isJumping;
    }

    public boolean getIsClimbing(){
        return isClimbing;
    }

    public boolean getIsOnGround(){
        return isOnGround;
    }

    public InteractableObject getCurrentInteractable(){
        return currentInteractable;
    }

    public int getInteractionProgress(){
        return interactionHoldTime;
    }

    public int getTotalInteractionRequiredTime(){
        return SEARCH_COMPLETE_TIME;
    }

    public boolean isInteractionCompleted(){
        return interactionCompleted;
    }

    public Inventory getInventory(){
        return inventory;
    }

    public boolean getIsShowingInteractionPrompt(){
        return showingInteractionPrompt;
    }

    public boolean getGameOver(){
        return gameOver;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }

    public boolean getIsExtracted(){
        return extracted;
    }

    public ScoreTracker getScoreTracker(){
        return scoreTracker;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //setters
    //----------------------------------------------------------------------------------------------------------------//

    //called in ScreenSettings fields, to avoid circular dependencies
    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


}
