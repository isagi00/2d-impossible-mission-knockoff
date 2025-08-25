package view.entityViews;


import model.entities.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

//handles player rendering based on its state
public class PlayerView  implements Observer {
    private Player player;
    private boolean wasGameOver;


    //animation frames
    private BufferedImage runningR1, runningR2, runningR3, runningR4, runningR5, runningR6;
    private BufferedImage runningL1, runningL2, runningL3, runningL4, runningL5, runningL6;
    private BufferedImage idleR1, idleR2, idleR3, idleR4;
    private BufferedImage idleL1, idleL2, idleL3, idleL4;
    private BufferedImage jumpR1, jumpR2, jumpR3, jumpR4, jumpR5, jumpR6, jumpR7, jumpR8;
    private BufferedImage jumpL1, jumpL2, jumpL3, jumpL4, jumpL5, jumpL6, jumpL7, jumpL8;
    private BufferedImage searching;
    private BufferedImage climb1,climb2,climb3,climb4;

    private BufferedImage death1, death2, death3, death4, death5, death6, death7, death8, death9;
    private BufferedImage canJumpIcon;

    private int spriteCounter;
    private int spriteNum;


    public PlayerView(Player player) {
        this.player = player;
        this.spriteCounter = 0;
        this.spriteNum = 0;

        loadPlayerSprites();
        player.addObserver(this);
    }



//
//  MOVES FORWARD THE SPRITES
//
    private void updateFrameCounter() {

        //GAME OVER
        boolean isGameOver = player.getGameOver();
        if( isGameOver && !wasGameOver) {
            spriteCounter = 0;
            spriteNum = 1;
        }
        wasGameOver = isGameOver;
        if(isGameOver) {
            spriteCounter++;        //tracks how many frames have passed.
            if (spriteCounter >= 8) {       //every 4 frames, updates the spritenumber. (higher = lower speed)
                if(spriteNum < 9) {
                    spriteNum = spriteNum + 1;    //cycles through the sprites.       total sprites = 8
                }
                spriteCounter = 0;
            }
            return;
        }

        // climbing animation
        if(player.getIsClimbing()){
            spriteCounter++;
            if (spriteCounter >= 12) {   //animation speed
                spriteNum = (spriteNum % 4) + 1;        //total frames
                spriteCounter = 0;
            }
            return;
        }


        //jumping animation
        if (player.getIsJumping()) {
            spriteCounter++;
            if (spriteCounter >= 20) {
                spriteNum = (spriteNum % 8) + 1;
                spriteCounter = 0;
            }
        }


        // running animation
        if (player.getIsMoving()) {
            spriteCounter++;
            if (spriteCounter >= 6) {
                spriteNum = (spriteNum % 6) + 1;    //4 running frames
                spriteCounter = 0;
            }
            return;
        }

        // idle animation
        spriteCounter++;
        if (spriteCounter  >= 30) {     //slower than running animation
            spriteNum = (spriteNum % 4) + 1; //cycle through 4 idle frames
            spriteCounter = 0;
        }
    }


//----------------------------------------------------------------------------------------------------------------//
//PLAYER SPRITES LOADER
//----------------------------------------------------------------------------------------------------------------//
    private void loadPlayerSprites() {
        try {
            // load idle sprites
            loadPlayerIdleSprites();
            // load jumping sprites
            loadPlayerJumpingSprites();
            // load running sprites
            loadPlayerRunningSprites();
            // load player climbing sprites
            loadPlayerClimbingSprites();
            //load player death sprites
            loadPlayerDeathSprites();

            //load the jump icon
            loadCanJumpSprite();

            System.out.println("all player sprites successfully loaded");
        }catch (Exception e){
            System.out.println("player sprites failed to load : ");
            e.printStackTrace();
        }
    }

    //called in loadPlayerSprites() method
    private void loadPlayerIdleSprites(){
        try {
            idleR1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/idleR1.png")));
            idleR2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/idleR2.png")));
            idleR3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/idleR3.png")));
            idleR4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/idleR4.png")));

            idleL1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/idleL1.png")));
            idleL2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/idleL2.png")));
            idleL3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/idleL3.png")));
            idleL4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/idleL4.png")));
            System.out.println("player idle sprites successfully loaded");
        }catch(Exception e){
            System.out.println("player idle sprites failed to load : ");
            e.printStackTrace();
        }
    }

    //called in loadPlayerSprites() method
    private void loadPlayerJumpingSprites(){
        try{
            jumpR1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpR1.png")));
            jumpR2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpR2.png")));
            jumpR3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpR3.png")));
            jumpR4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpR4.png")));
            jumpR5 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpR5.png")));
            jumpR6 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpR6.png")));
            jumpR7 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpR7.png")));
            jumpR8 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpR8.png")));

            jumpL1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpL1.png")));
            jumpL2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpL2.png")));
            jumpL3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpL3.png")));
            jumpL4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpL4.png")));
            jumpL5 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpL5.png")));
            jumpL6 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpL6.png")));
            jumpL7 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpL7.png")));
            jumpL8 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/jumpL8.png")));
            System.out.println("player jumping sprites successfully loaded");
        }catch(Exception e){
            System.out.println("player jumping sprites failed to load : ");
            e.printStackTrace();
        }

    }

    //called in loadPlayerSpritesMethod()
    private void loadPlayerRunningSprites(){
        try {
            runningR1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningR1.png")));
            runningR2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningR2.png")));
            runningR3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningR3.png")));
            runningR4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningR4.png")));
            runningR5 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningR5.png")));
            runningR6 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningR6.png")));

            runningL1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningL1.png")));
            runningL2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningL2.png")));
            runningL3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningL3.png")));
            runningL4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningL4.png")));
            runningL5 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningL5.png")));
            runningL6 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/runningL6.png")));
            System.out.println("player running sprites successfully loaded");
        }catch (Exception e){
            System.out.println("player running sprites failed to load : ");
            e.printStackTrace();
        }
    }
    //called in loadPlayerSprites() method
    private void loadPlayerClimbingSprites(){
        try{
            climb1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/climb1.png")));
            climb2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/climb2.png")));
            climb3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/climb3.png")));
            climb4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/climb4.png")));
            System.out.println("player climbing sprites successfully loaded");
        }catch (Exception e){
            System.out.println("player climbing sprites failed to load : ");
            e.printStackTrace();
        }
    }

    private void loadPlayerDeathSprites(){
        try{
            death1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death1.png")));
            death2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death2.png")));
            death3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death3.png")));
            death4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death4.png")));
            death5 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death5.png")));
            death6 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death6.png")));
            death7 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death7.png")));
            death8 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death8.png")));
            death9 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/death9.png")));
            System.out.println("player death sprites successfully loaded");
        }catch (Exception e){
            System.out.println("player death sprites failed to load : ");
            e.printStackTrace();
        }

    }

    private void loadCanJumpSprite(){
        try{
            canJumpIcon = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("player/canjumpicon.png")));
            System.out.println("can jump sprites successfully loaded");
        }
        catch (Exception e){
            System.out.println("can jump sprites failed to load : ");
            e.printStackTrace();
        }
    }


//----------------------------------------------------------------------------------------------------------------//
// MAIN DRAW METHOD
//----------------------------------------------------------------------------------------------------------------//
    //draw player animations
    // method is called in GameView paintComponent(Graphics g) method
    // Graphics g turns into Graphics2D g2d through casting inside (itself) paintComponent method.
    public void draw(Graphics2D g2d) {

        updateFrameCounter(); //moves the frames

        BufferedImage image = null;
        String direction = player.getDirection();
        // Jumping animation
        if (player.getIsJumping()) {
            image = drawJumpingAnimation(direction);
        }
        else if (player.getGameOver()) {
            image = drawDeathAnimation();
        }
        //climbing animation
        else if (player.getIsClimbing()) {
            image = drawClimbingAnimation();
        }
        // Running animation
        else if (player.getIsMoving()) {
            image = drawRunningAnimation(direction);
        }
        // idle animation
        else if (!player.getIsMoving()) {
            image = drawIdleAnimation(direction);
        }

        // Draw the player
        if (image != null) {
            g2d.drawImage(image, player.getX(), player.getY(), player.width, player.height, null);
        } else {
            // Fallback rectangle
            g2d.setColor(Color.GREEN);
            g2d.fillRect(player.getX(), player.getY(), player.width, player.height);
        }


        if(player.getIsOnGround()){
            g2d.drawImage(canJumpIcon, player.getX() + player.getWidth(), player.getY() - 15, null);
        }



        //BELOW IS EVERYTHING IS FOR DEBUGGING
        //player hitbox
        drawPlayerInfo(g2d);
        //draw collision points
        //drawCollisionPoints(g2d);

//        System.out.println("drawing player...");
    }
//----------------------------------------------------------------------------------------------------------------//
// SINGLE ANIMATIONS
//----------------------------------------------------------------------------------------------------------------//
    // called in draw() method when player is jumping
    //the spriteNum gets updated in updateFrameCounter()
    private BufferedImage drawJumpingAnimation(String direction){
        BufferedImage image;
        if (direction.equals("left")) {
            switch (spriteNum) {
                case 1: image = jumpL1; break;
                case 2: image = jumpL2; break;
                case 3: image = jumpL3; break;
                case 4: image = jumpL4; break;
                case 5: image = jumpL5; break;
                case 6: image = jumpL6; break;
                case 7: image = jumpL7; break;
                case 8: image = jumpL8; break;
                default: image = jumpL1; break;
            }
        } else {
            switch (spriteNum) {
                case 1: image = jumpR1; break;
                case 2: image = jumpR2; break;
                case 3: image = jumpR3; break;
                case 4: image = jumpR4; break;
                case 5: image = jumpR5; break;
                case 6: image = jumpR6; break;
                case 7: image = jumpR7; break;
                case 8: image = jumpR8; break;
                default: image = jumpR1; break;
            }
        }
        return image;
    }

    //called in draw() method when player is running
    //the spriteNum gets updated in updateFrameCounter()
    private BufferedImage drawRunningAnimation(String direction){
        BufferedImage image;
        if (direction.equals("left")) {
            switch (spriteNum) {
                case 1: image = runningL1; break;
                case 2: image = runningL2; break;
                case 3: image = runningL3; break;
                case 4: image = runningL4; break;
                case 5: image = runningL5; break;
                case 6: image = runningL6; break;
                default: image = runningL1; break;
            }
        } else {
            switch (spriteNum) {
                case 1: image = runningR1; break;
                case 2: image = runningR2; break;
                case 3: image = runningR3; break;
                case 4: image = runningR4; break;
                case 5: image = runningR5; break;
                case 6: image = runningR6; break;
                default: image = runningR1; break;
            }
        }
        return image;
    }

    //called in draw() when player is idle
    //the spriteNum gets updated in updateFrameCounter()
    private BufferedImage drawIdleAnimation(String direction){
        //image = direction.equals("left") ? idleL : idleR;
        BufferedImage image;
        if (direction.equals("right")) {
            switch (spriteNum) {
                case 1: image = idleR1; break;
                case 2: image = idleR2; break;
                case 3: image = idleR3; break;
                case 4: image = idleR4; break;
                default: image = idleR1; break;
            }
        }else{
            switch (spriteNum) {
                case 1: image = idleL1; break;
                case 2: image = idleL2; break;
                case 3: image = idleL3; break;
                case 4: image = idleL4; break;
                default: image = idleL1; break;
            }
        }
        return image;
    }

    //called in draw() when player is climbing
    //the spriteNum gets updated in updateFrameCounter()
    private BufferedImage drawClimbingAnimation(){
        BufferedImage image;
        switch (spriteNum){
            case 1: image = climb1; break;
            case 2: image = climb2; break;
            case 3: image = climb3; break;
            case 4: image = climb4; break;
            default : image = climb1; break;
        }

        return image;
    }

    private BufferedImage drawDeathAnimation(){
        BufferedImage image;
        switch (spriteNum){
            case 1: image = death1; break;
            case 2: image = death2; break;
            case 3: image = death3; break;
            case 4: image = death4; break;
            case 5: image = death5; break;
            case 6: image = death6; break;
            case 7: image = death7; break;
            case 8: image = death8; break;
            case 9: image = death9; break;
            default: image = death1; break;
        }
        return image;
    }



    //DEBUG:  called in draw() if enabled
    private void drawCollisionPoints(Graphics2D g2d){
        // Collision point visualization
        final int collisionPointsForEachEdge = 4;
        int left = player.getX();
        int right = player.getX() + player.width;
        int top = player.getY();
        int bottom = player.getY() + player.height;

        // Left edge collision points (BLUE)
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < collisionPointsForEachEdge; i++) {
            int y = top + (bottom - top) * i / (collisionPointsForEachEdge - 1);
            g2d.fillOval(left - 3, y - 3, 6, 6);
            // Add point number for reference
            g2d.setColor(Color.WHITE);
        }

        // Right edge collision points (MAGENTA)
        g2d.setColor(Color.MAGENTA);
        for (int i = 0; i < collisionPointsForEachEdge; i++) {
            int y = top + (bottom - top) * i / (collisionPointsForEachEdge - 1);
            g2d.fillOval(right - 3, y - 3, 6, 6);
            // Add point number for reference
            g2d.setColor(Color.WHITE);

        }

        // Top edge collision points (YELLOW)
        g2d.setColor(Color.YELLOW);
        for (int i = 0; i < collisionPointsForEachEdge; i++) {
            int x = left + (right - left) * i / (collisionPointsForEachEdge - 1);
            g2d.fillOval(x - 3, top - 3, 6, 6);
            // Add point number for reference
            g2d.setColor(Color.WHITE);
        }

        // Bottom edge collision points (CYAN)
        g2d.setColor(Color.CYAN);
        for (int i = 0; i < collisionPointsForEachEdge; i++) {
            int x = left + (right - left) * i / (collisionPointsForEachEdge - 1);
            g2d.fillOval(x - 3, bottom - 3, 6, 6);
            // Add point number for reference
            g2d.setColor(Color.WHITE);
        }
    }

    //debug: called in draw() if enabled
    private void drawPlayerInfo(Graphics2D g2d) {
        //player hitbox
        g2d.setColor(new Color(255,0,0,100));
        g2d.drawRect(player.getX(), player.getY(), player.width, player.height);
        g2d.setColor(Color.GREEN);
        g2d.drawRect(player.getX() + 10, player.getY() + 3, player.width - 20, player.height);

        // grounded state
        g2d.setColor(player.getIsOnGround() ? Color.GREEN : Color.RED);
        g2d.drawString("Grounded: " + player.getIsOnGround(), player.getX(), player.getY() - 10);

        //is moving
        g2d.setColor(player.getIsMoving() ? Color.GREEN : Color.RED);
        g2d.drawString("isMoving: " + player.getIsMoving(), player.getX(), player.getY() - 20);

        //is jumping
        g2d.setColor(player.getIsJumping() ? Color.GREEN : Color.RED);
        g2d.drawString("isJumping: " + player.getIsJumping(), player.getX(), player.getY() - 30);
        //is climbing
        g2d.setColor(player.getIsClimbing() ? Color.GREEN : Color.RED);
        g2d.drawString("is climbing: " + player.getIsClimbing(), player.getX(), player.getY() - 40);
        //direction
        g2d.setColor(Color.WHITE);
        g2d.drawString("direction: " + player.getDirection(), player.getX(), player.getY() - 50);
        //game over status
        g2d.setColor(player.getGameOver() ? Color.GREEN : Color.RED);
        g2d.drawString("gameOver: " + player.getGameOver(), player.getX(), player.getY() - 60);
        //x and y coordinates
        g2d.setColor(Color.WHITE);
        g2d.drawString("x: " + player.getX() + "y:" + player.getY(), player.getX(), player.getY() - 70);
        //player x and y coordinates display
        g2d.drawOval(player.getX(),player.getY(), 3, 3);
        //player width and height
        g2d.drawString("player width, height : "  + player.getWidth() + ", " + player.getHeight(), player.getX(), player.getY() - 80 );

    }


    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Player player ) {

        }
    }


}

