package view.entityViews;

import model.entities.Dog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class DogView implements Observer {

    private Dog dog;
    //6 disabled sprites
    private BufferedImage disabledR1, disabledR2, disabledR3, disabledR4, disabledR5, disabledR6;
    private BufferedImage disabledL1, disabledL2, disabledL3, disabledL4, disabledL5, disabledL6;

    //6 running sprites
    private BufferedImage runR1, runR2, runR3, runR4, runR5, runR6;
    private BufferedImage runL1, runL2, runL3, runL4, runL5, runL6;

    //4 idle sprites
    private BufferedImage idleR1, idleR2, idleR3, idleR4;
    private BufferedImage idleL1, idleL2, idleL3, idleL4;

    //chasing indicators
    private BufferedImage chasing, notchasing;

    //used for cycling the sprites
    private int spriteCounter = 0;
    private int spriteNum = 0;

    //display the dog disabled animation from 0
    private boolean wasDisabled = false;

    public DogView(Dog dog) {
        this.dog = dog;
        loadSprites();

        this.dog.addObserver(this);
    }

    private void loadSprites() {
        try{
            disabledR1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledR1.png"));
            disabledR2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledR2.png"));
            disabledR3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledR3.png"));
            disabledR4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledR4.png"));
            disabledR5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledR5.png"));
            disabledR6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledR6.png"));

            disabledL1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledL1.png"));
            disabledL2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledL2.png"));
            disabledL3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledL3.png"));
            disabledL4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledL4.png"));
            disabledL5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledL5.png"));
            disabledL6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/disabledL6.png"));

            runR1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runR1.png"));
            runR2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runR2.png"));
            runR3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runR3.png"));
            runR4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runR4.png"));
            runR5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runR5.png"));
            runR6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runR6.png"));

            runL1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runL1.png"));
            runL2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runL2.png"));
            runL3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runL3.png"));
            runL4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runL4.png"));
            runL5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runL5.png"));
            runL6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/runL6.png"));

            idleR1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/idleR1.png"));
            idleR2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/idleR2.png"));
            idleR3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/idleR3.png"));
            idleR4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/idleR4.png"));

            idleL1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/idleL1.png"));
            idleL2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/idleL2.png"));
            idleL3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/idleL3.png"));
            idleL4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/idleL4.png"));

            chasing = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/chasing.png"));
            notchasing = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/dog/notchasing.png"));

            System.out.println("[DogView] loaded dog sprites");
        }
        catch (Exception e){
            System.out.println("[DogView] failed to load dog sprites : ");
            e.printStackTrace();
        }

    }

    private void updateFrameCounter(){
        spriteCounter++;        //increment once per frame

        //check if dog just became disabled
        if (dog.getIsDisabled() && !wasDisabled){
            spriteNum = 1; //start from first frame
            spriteCounter = 0;
            wasDisabled = true;
        }

        //dog disabled animation
        if (dog.getIsDisabled()){
            if (spriteCounter > 15){
                if (spriteNum < 6){    //keep the dog in the last disabled frame
                    spriteNum++;
                }
                spriteCounter = 0;
            }
        }


        //idle animation
        else if (dog.getIsIdle()){
            spriteCounter++;
            if (spriteCounter > 30){
                spriteNum = (spriteNum % 4) + 1;
                spriteCounter = 0;
            }
        }

        //running animation
        else if (dog.getIsMoving()){
            spriteCounter++;
            if (spriteCounter > 10){
                spriteNum = (spriteNum % 6) + 1;
                spriteCounter = 0;
            }
        }

        else{
            spriteCounter = 0;  //reset the counter if not in any animation frame
        }
    }

    public void draw(Graphics2D g2d){
        BufferedImage image = null;
        updateFrameCounter();

        if(dog.getIsIdle()){
            image = switchIdleSprite(dog.getDirection());
        }

        if(dog.getIsMoving()){
            image = switchMovingSprite(dog.getDirection());
        }

        if(dog.getIsDisabled()){
            image = switchDisabledSprites(dog.getDirection());
        }

        g2d.drawImage(image, dog.getX(), dog.getY(), dog.getWidth(), dog.getHeight(), null);
        g2d.drawImage(dog.getIsChasing() ? chasing : notchasing, dog.getX() + 30, dog.getY() + 20, null);

        //sprite size
        g2d.setColor(Color.BLACK);
        g2d.drawRect(dog.getX(), dog.getY(), dog.getWidth(), dog.getHeight());

        //actual hitbox size
        g2d.setColor(Color.GREEN);
        g2d.drawRect(dog.getX() + 20, dog.getY() + 65, dog.getWidth() - 40, dog.getHeight() - 65);

        g2d.setColor(Color.GREEN);
        //info
        g2d.drawString("dog direction: " + dog.getDirection(), dog.getX(), dog.getY() - 10);
        g2d.drawString("current patrol position: " + dog.getCurrentPatrolPosition(), dog.getX(), dog.getY() - 20);
        g2d.drawString("wall in front: " + dog.getWallInFront(), dog.getX(), dog.getY() - 30);
        g2d.drawString("is chasing: " + dog.getIsChasing(), dog.getX(), dog.getY() - 40);
        g2d.setColor(Color.WHITE);
        g2d.drawString("x: " + dog.getX() + "y:" + dog.getY(), dog.getX(), dog.getY() - 50);
        g2d.drawOval(dog.getX(), dog.getY(), 3, 3);
        g2d.drawString("dog width, height: " + dog.getWidth() + " " + dog.getHeight(), dog.getX(), dog.getY() - 60);
        g2d.drawString("[ground ahead] " + dog.getGroundAhead(), dog.getX(), dog.getY() - 70);
        g2d.drawString("[idle time]" + dog.getWaitTime(), dog.getX(), dog.getY() - 80);
        g2d.drawOval(dog.getX() + dog.getWidth(), dog.getY() + dog.getHeight(), 5, 5); //bottom right corner of the dog
        g2d.drawOval(dog.getX(), dog.getY() + dog.getHeight(), 5, 5); //bottom left corner of the dog

    }



    private BufferedImage switchIdleSprite(String direction){
        BufferedImage image = null;

        if (direction.equals("right")){
            switch (spriteNum){
                case 1:
                    image = idleR1;
                    break;
                case 2:
                    image = idleR2;
                    break;
                case 3:
                    image = idleR3;
                    break;
                case 4:
                    image = idleR4;
                    break;
                default:
                    image = idleR1;
                    break;
            }
        }
        else if (direction.equals("left"))
            switch (spriteNum){
                case 1:
                    image = idleL1;
                    break;
                case 2:
                    image = idleL2;
                    break;
                case 3:
                    image = idleL3;
                    break;
                case 4:
                    image = idleL4;
                    break;
                default:
                    image = idleL1;
                    break;
            }
        return image;
    }

    private BufferedImage switchMovingSprite(String direction){
        BufferedImage image = null;
        if (direction.equals("right")){
            switch (spriteNum){
                case 1:
                    image = runR1;
                    break;
                case 2:
                    image = runR2;
                    break;
                case 3:
                    image = runR3;
                    break;
                case 4:
                    image = runR4;
                    break;
                case 5:
                    image = runR5;
                    break;
                case 6:
                    image = runR6;
                    break;
                default:
                    image = runR1;
            }
        } else if (direction.equals("left")) {
            switch (spriteNum){
                case 1:
                    image = runL1;
                    break;
                case 2:
                    image = runL2;
                    break;
                case 3:
                    image = runL3;
                    break;
                case 4:
                    image = runL4;
                    break;
                case 5:
                    image = runL5;
                    break;
                case 6:
                    image = runL6;
                    break;
                default:
                    image = runL1;
                    break;
            }
        }
        return image;
    }

    private BufferedImage switchDisabledSprites(String direction) {
        BufferedImage image = null;
        if (direction.equals("right")) {
            switch (spriteNum) {
                case 1: image = disabledR1; break;
                case 2: image = disabledR2; break;
                case 3: image = disabledR3; break;
                case 4: image = disabledR4; break;
                case 5: image = disabledR5; break;
                case 6: image = disabledR6; break;
                default: image = disabledR1; break;
            }
        }
        else {
            switch (spriteNum) {
                case 1: image = disabledL1; break;
                case 2: image = disabledL2; break;
                case 3: image = disabledL3; break;
                case 4: image = disabledL4; break;
                case 5: image = disabledL5; break;
                case 6: image = disabledL6; break;
                default: image = disabledL1; break;
            }
        }
        return image;
    }









    @Override
    public void update(Observable o, Object arg) {

    }
}
