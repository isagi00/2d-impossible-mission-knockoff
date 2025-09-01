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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * player model, handles everything that a player does: movement, object interaction, computer screen navigation.
 * provides multiple getter methods to access the player instance's current state.
 */
public class Player extends Entity {

    //----------------------------------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------------------------------//
    //other models
    /**
     *reference to {@link LevelManager}.
     */
    private LevelManager levelManager;
    /**
     * current room that the player is in. provides various useful methods that allow the player to interact with its current room.
     * see {@link Room}.
     */
    private Room currentRoom;

    //movement status bools
    /**
     * flag that tracks if the player is moving or not
     */
    private boolean isMoving = false;
    /**
     * flag that tracks if the player is on a tile or not
     */
    private boolean isOnGround = false;
    /**
     * flag that tracks if the player is jumping or not
     */
    private boolean isJumping = false;

    //interaction related
    /**
     *flag that tracks if the player has completed a interactable object search
     */
    private boolean interactionCompleted = false;
    /**
     * time that the player has held down the interaction/search button (e).
     */
    private int interactionHoldTime = 0;
    /**
     * time needed to finish a search. once this time is up, it will trigger the {@link #interact()} method.
     */
    private final int SEARCH_COMPLETE_TIME = 180;    //3 seconds at 60 fps
    /**
     * flag that checks if the interaction prompt should show up or not
     */
    private boolean showingInteractionPrompt = false;
    /**
     * player's current interactable. if the player is near an interactable object, then this will change into that interactable object.
     */
    private InteractableObject currentInteractable = null;
    /**
     * max allowed distance from the player and an interactable object. the interaction prompt won't show if the player
     * is outside of this range. this number was chosen because provided to be comfortable, so that the player would not access
     * interactable objects that are too distant.
     */
    private final int MAX_ALLOWED_OBJECT_DISTANCE = 40; //40 pixels near
    //jump related
    /**
     * represents the player's vertical speed.
     */
    private int verticalSpeed = 0;
    /**
     * gravity constant. it is always applied to the player's {@link #verticalSpeed} when the player is jumping or falling down.
     */
    private final int GRAVITY = 1;
    /**
     * speed of the player jump. when the player presses space, its {@link #verticalSpeed} will spike to
     * this number, moving the player vertically
     */
    private final int MAX_JUMP_SPEED = -17;
    /**
     * flag that indicates if the player is climbing or not.
     */
    //ladder climbing related
    private boolean isClimbing = false;
    /**
     * player's closest and climbable ladder.
     */
    private Ladder currentLadder = null;
    /**
     * player's ladder climbing speed.
     */
    private static final int CLIMB_SPEED = 4;

    //inventory
    /**
     * player's {@link Inventory}
     */
    private final Inventory inventory;
    /**
     * game over flag. if the player dies, then this flag gets set to true and it respawns at the last checkpoint({@link #lastCheckpointX}, {@link #lastCheckpointY}
     * via {@link #resetToCheckpoint()}.
     * it does not end the game, it indicates whether the player died or not.
     */
    private boolean gameOver;

    /**
     * tracks the last checkpoint's triggered x coordinate
     */
    private int lastCheckpointX = 0;
    /**
     * tracks the last checkpoint's triggered y coordinate
     */
    private int lastCheckpointY = 0;

    //extraction
    /**
     * extraction flag. if this flag is set to true, then the {@link view.gamePanelViews.ResultScreenView} (result screen) gets
     * set to visible, and the game ends.
     *
     */
    private boolean extracted = false;

    //score tracker
    /**
     * reference to the {@link ScoreTracker} instance
     */
    private final ScoreTracker scoreTracker;

    //sound related
    /**
     * flag that tracks if the player died or not.
     * used to send a single notification, otherwise it would send multiple notifications.
     */
    private boolean wasGameOver = false; //otherwise the audio replays multiple times
    /**
     * flag that tracks if the player was moving or not.
     * used to send a single notification, otherwise it would send multiple notifications.
     */
    private boolean wasMoving = false;
    /**
     * last footstep time, used to calculate the footstep sound timing in the {@link #update(boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)}
     * method.
     */
    private long lastFootStepTime = 0;
    /**
     * interval between footsteps
     */
    private final long FOOTSTEP_SOUND_INTERVAL = 250; //250 ms between footsteps


    //----------------------------------------------------------------------------------------------------------------//
    // PLAYER INITIALIZATION
    //----------------------------------------------------------------------------------------------------------------//
    /** player constructor: sets some default values, initializes its {@link #inventory} and {@link #scoreTracker} fields.
     * sets as observers: {@link ScoreTracker} and {@link AudioManager}.
     * the score tracker tracks the player's actions, such as opening an interactable object, which gives some points.
     * the audio manager gets notified of when the player is moving, jumping, if the player died.
     * note: {@link view.entityViews.PlayerView} is not registered as an observer,  because during the initial part of the game development
     * the observer pattern was not implemented... and because of that, the player view just gets updated via getters methods.
     */
    public Player() {
        setDefaultValues();

        this.inventory = new Inventory(5);
        this.scoreTracker = new ScoreTracker();

        this.addObserver(scoreTracker);
        this.addObserver(AudioManager.getInstance());
    }

    /**
     * sets some of the player default values, to not crown the constructor too much.
     * sets its starting x position, y position, width, height, speed, initial direction,
     * {@link #isOnGround} flag, {@link #isJumping} flag, {@link #gameOver} flag.
     */
    //called in Player() constructor
    //sets player default values
    private void setDefaultValues() {
        //spawn location
        setX(100);
        setY( getHeight() + ScreenSettings.TILE_SIZE);
        setWidth(ScreenSettings.TILE_SIZE);
        setHeight(ScreenSettings.TILE_SIZE);
        setSpeed(6);
        setDirection("right");
        isOnGround = false;
        isJumping = false;
        gameOver = false;
        this.levelManager = getLevelManager();
    }





//----------------------------------------------------------------------------------------------------------------//
// UPDATE METHOD
//----------------------------------------------------------------------------------------------------------------//

    /**core of the player logic, processes gravity, horizontal movement, jump movement, ladder movement, object interaction, computer menu navigation,
     * sound logic (notifies the {@link AudioManager}.
     * note: it is very long, and it is not being broken down into smaller methods because it has been WAY easier to debug and
     * implement logic when needed. having little methods scattered into the player class had been a nightmare for me navigate around
     * and debug.
     * each section has a inline comment to highlight its function.
     * @param upPressed w pressed
     * @param downPressed s pressed
     * @param leftPressed a pressed
     * @param rightPressed d pressed
     * @param spacePressed space pressed
     * @param ePressed e, interaction button pressed
     * @param upArrowPressed up arrow pressed, for computer menu navigation
     * @param downArrowPressed down arrow pressed, for computer menu navigation
     * @param enterPressed enter pressed, for confirming the current computer option selected
     */
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
            else{
                isMoving = false;
                setX(playerX);
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
                    interact();                                     //actually open with the object
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

    /**scans through the interactable objects (because {@link Ladder} is one) in the current room,
     * and sets the {@link #currentLadder} to the ladder closest to the player.
     *
     * @return true if the player is near a ladder
     */
    private boolean isNearLadder() {
        List<InteractableObject> objects = currentRoom.getInteractiveObjects();
        for (InteractableObject obj : objects) {
            if (obj instanceof Ladder ladder) {
//                System.out.println("ladder : " + ladder);
                //check if the player is within the ladder's horizontal bounds
                boolean horizontalBounds = getX() + getWidth() >= ladder.getX() && getX() <= ladder.getX() + ladder.getWidth();
                //placeholder ladder info
                int playerBottom = getY() + getHeight();
                int ladderBottom =ladder.getY() + ladder.getHeight();
                //check if the player's in the ladder vertical bounds
                boolean verticalBounds = playerBottom >= ladder.getY() - CLIMB_SPEED - 5 && getY() <= ladderBottom + 5;
                //set the current ladder if the player's is in ladder's both horizontal and vertical bounds
                if(horizontalBounds && verticalBounds){
                    currentLadder = ladder;
                    return true;
                }
            }
        }
        return false;
    }



    /**compute if player is near to an object or not using the pythagorean theorem.
     * depends on the {@link #MAX_ALLOWED_OBJECT_DISTANCE} field.
     * @param obj   current interactable object
     * @return true if the player is near enough
     */
    private boolean isWithinDistance(InteractableObject obj){
        //get player and object centers
        int playerCenterX = getX() + getWidth()/2;
        int playerCenterY = getY() + getHeight()/2;
        int objectCenterX = obj.getX() + obj.getWidth()/2;
        int objectCenterY = obj.getY() + obj.getHeight()/2;
        //find the difference in x and y coordinates
        int distanceX = playerCenterX - objectCenterX;
        int distanceY = playerCenterY - objectCenterY;
        //pythagorean theorem : straight line distance between 2 points = sqrt(dx^2 + dy^2) -> then check with the allowed max distance to an object
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY) < MAX_ALLOWED_OBJECT_DISTANCE;
    }

    /**
     * check if player is near an interactable object, sets the {@link #currentInteractable} field if it is near.
     * handles some edge cases: if the player's inventory's full, it will return false, so the {@link #showingInteractionPrompt} is set to
     * false in the {@link #update(boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)} method.
     * the inventory is not considered 'full' if the player has a {@link ComputerCard} and the {@link Computer} can still be accessed.
     * if the computer's menu is visible, then the {@link #currentInteractable} field is set to be the computer.
     * @return true the interactable object is {@link #isWithinDistance(InteractableObject)}.
     */
    private boolean updateCurrentInteractable() {
        List<InteractableObject> objects = currentRoom.getInteractiveObjects();
        //if there's an active computer menu, keep it as current interactable
        for (InteractableObject obj : objects) {
            if (obj instanceof Computer computer && computer.getIsMenuVisible()) {
                currentInteractable = computer;
                return true;
            }
        }
        //proximity check for other objects
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


            //for other interactable objects check if the object is within distance and the inventory is not full
            if (isWithinDistance(obj) && !inventory.isInventoryFull()) {
                currentInteractable = obj;
                return true;
            }
        }
        currentInteractable = null;
        return false;
    }


    /**handles the interaction of the current interactable object
     * interacts with the {@link #currentInteractable}, sends an appropriate notification to the respective observers.
     * calls the interactable object's .open() method, the interaction logic is handled in the respective interactable object's
     * open() method.
     */
    private void interact(){
        //if the player has a card in his inventory and interacts with the computer, the remove the card
        if ( currentInteractable instanceof Computer computer && checkPlayerHasCard()){
            ComputerCard card = inventory.getComputerCard();
            inventory.removeComputerCard(card);
            computer.setInteractionCompleted(false);        //once the computer gets used, reset it to allow the player
            //to access it again if he has another card in inventory.. edge case if the player collects more computer cards
        }
        //open the object
        if( currentInteractable != null ) {
            currentInteractable.open(this); //player INITIATES THE INTERACTION, object interacts with player
            //COMMAND PATTERN
            /**
             * the player calls open on the object
             * the objects executes its own open logic
             */
            if(currentInteractable.isInteractionCompleted() && !interactionCompleted) {
                interactionCompleted = true;
                if (currentInteractable instanceof PaperBox) {
                    setChanged();
                    notifyObservers("paper box opened");
//                    System.out.println("notifiying observers paper box opened");
                }
                else if (currentInteractable instanceof RedBox) {
                    setChanged();
                    notifyObservers("red box opened");
//                    System.out.println("notifiying observers red box opened");
                }
                else if (currentInteractable instanceof MetalLocker){
                    setChanged();
                    notifyObservers("metal locker opened");
//                    System.out.println("notifiying observers metal locker opened");
                }
                else if (currentInteractable instanceof WoodLocker){
                    setChanged();
                    notifyObservers("wood locker opened");
//                    System.out.println("notifiying observers wood locker opened");
                }
            }
        }
    }

    /**
     * resets the player to the last checkpoint ({@link model.levels.TileManager} tile number 99)
     */
    private void resetToCheckpoint(){
        if (lastCheckpointX != 0 && lastCheckpointY != 0 && gameOver) {
            setX(lastCheckpointX);
            setY(lastCheckpointY - 40);
        }
        gameOver = false;   //reset the flag
    }

    /**checks if the player has a card
     * @return true if the player has a {@link ComputerCard} in his {@link #inventory}
     */
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
    /**checks if the player is colliding against some tile.
     * each player edge has 4 collision points, so it prevents the player from slipping through tiles.
     * it is divided in 4 other helper methods to check each edge:
     * {@link #checkTopEdgeCollision(int, int, int, int, int[][])},
     * {@link #checkBottomEdgeCollision(int, int, int, int, int[][])}
     * {@link #checkLeftEdgeCollision(int, int, int, int, int[][])}
     * {@link #checkRightEdgeCollision(int, int, int, int, int[][])}.
     * it overrides the {@link Entity} check collision system with some minor tweaks: it checks if the current tile is a death tile
     * or the extraction tile.
     * @param playerX player's current x coordinate
     * @param playerY player's current y coordinate
     * @return true if player is colliding with some tile
     */
    //called in various methods, to check collision with tiles
    //check player collision with tiles (fuck you, collision logic)
    @Override
    protected boolean checkCollisionWithTile(int playerX, int playerY) {  //playerX and playerY represent the current position of the player
        //get the array representing the current level
        int[][] roomData = levelManager.getCurrentRoomData();

        // calculate the hit box positions
        int left = playerX + 10;                     //the top left, ex : x = 0
        int right = playerX + getWidth() - 10;
        int top = playerY + 3;
        int bottom = playerY + getHeight() - 3;

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

    /**checks the player's top edge collision points. returns true if there's a collision.
     * @param collisionPointsForEachEdge number of collision points for this edge
     * @param left player's leftmost point
     * @param right player's rightmost point
     * @param entityTopRow player's top row in grid coordinates
     * @param roomData current room's data
     * @return true if there's a collision with a tile
     */
    @Override
    protected boolean checkTopEdgeCollision(int collisionPointsForEachEdge, int left, int right, int entityTopRow, int[][] roomData) {
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

            int tileNum = roomData[entityTopRow][playerTopCollisionPointCol];    //look up what tile type is at that grid position
            //death tile
            if (tileNum == 9 && !gameOver) {
                gameOver = true;        //red tile
            }
            //extraction tile
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

    /**checks the player's bottom edge collision points. returns true if there's a collision.
     * @param collisionPointsForEachEdge number of collision points for this edge
     * @param left player's leftmost point
     * @param right player's rightmost point
     * @param entityBottomRow player's bottom row in grid coordinates
     * @param roomData current room's data
     * @return true if there's a collision with a tile
     */
    @Override
    //called in checkCollisionWithTile()
    //BOTTOM COLLISION
    protected boolean checkBottomEdgeCollision(int collisionPointsForEachEdge, int left, int right, int entityBottomRow, int[][] roomData) {
        for( int i = 0; i < collisionPointsForEachEdge; i++ ) {
            int width = right - left;     //width of the player
            int totalIntervals = collisionPointsForEachEdge - 1;    //how many spaces between the points
            int currentInterval = i;            //the current point
            int distanceFromLeft = (width * currentInterval) / totalIntervals;  //distance from the left point

            int x = left + distanceFromLeft;        //add the left edge to the distances from the left

            //find the current collision point tile grid position
            int playerBottomCollisionPointCol = x / ScreenSettings.TILE_SIZE;       //convert pixel coordinate to the collision point's current column

            int tileNum = roomData[entityBottomRow][playerBottomCollisionPointCol];    //look up what tile type is at that grid position
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

    /**checks the player's left edge collision points. returns true if there's a collision.
     * @param collisionPointsForEachEdge number of collision points for this edge
     * @param bottom player's bottommost point
     * @param top player's topmost point
     * @param playerLeftCol player's left col in grid coordinates
     * @param roomData current room's data
     * @return true if there's a collision with a tile
     */
    @Override
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
    /**checks the player's right edge collision points. returns true if there's a collision.
     * @param collisionPointsForEachEdge number of collision points for this edge
     * @param bottom player's bottommost point
     * @param top player's topmost point
     * @param playerRightCol player's left col in grid coordinates
     * @param roomData current room's data
     * @return true if there's a collision with a tile
     */
    @Override
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

    /**
     * @return the {@link #isMoving} flag
     */
    public boolean getIsMoving(){
        return isMoving;
    }

    /**
     * @return the {@link #isJumping} flag
     */
    public boolean getIsJumping(){
        return isJumping;
    }

    /**
     * @return the {@link #isClimbing} flag
     */
    public boolean getIsClimbing(){
        return isClimbing;
    }

    /**
     * @return the {@link #isOnGround} flag
     */
    public boolean getIsOnGround(){
        return isOnGround;
    }

    /**
     * @return the {@link #currentInteractable}
     */
    public InteractableObject getCurrentInteractable(){
        return currentInteractable;
    }

    /**
     * @return the {@link #interactionHoldTime}
     */
    public int getInteractionProgress(){
        return interactionHoldTime;
    }

    /**
     * @return the {@link #SEARCH_COMPLETE_TIME}
     */
    public int getTotalInteractionRequiredTime(){
        return SEARCH_COMPLETE_TIME;
    }

    /**
     * @return the {@link #interactionCompleted}
     */
    public boolean isInteractionCompleted(){
        return interactionCompleted;
    }

    /**
     * @return the {@link #inventory} instance
     */
    public Inventory getInventory(){
        return inventory;
    }

    /**
     * @return the {@link #showingInteractionPrompt} flag
     */
    public boolean getIsShowingInteractionPrompt(){
        return showingInteractionPrompt;
    }

    /**
     * @return the {@link #gameOver} player flag
     */
    public boolean getGameOver(){
        return gameOver;
    }

    /**
     * @return the {@link #scoreTracker} instance
     */
    public LevelManager getLevelManager(){
        return levelManager;
    }

    /**
     * @return the {@link #extracted} flag
     */
    public boolean getIsExtracted(){
        return extracted;
    }

    /**
     * @return the {@link #scoreTracker}
     */
    public ScoreTracker getScoreTracker(){
        return scoreTracker;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //setters
    //----------------------------------------------------------------------------------------------------------------//

    /**sets the current player'{@link #levelManager}. this was to avoid circular dependencies
     * @param levelManager {@link LevelManager} model instance
     */
    public void setLevelManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    /**
     * sets the player's game over to true or false, depending on the input parameter
     * @param gameOver player's game over flag
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


}
