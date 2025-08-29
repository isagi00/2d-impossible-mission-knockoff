package view.entityViews;

import model.entities.Dog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * view counterpart of the {@link Dog} model.
 * handles the rendering logic of a single dog instance.
 * all the dog sprites are loaded once in the {@link EnemyView} constructor.
 */
public class DogView {

    /**
     * reference of the {@link Dog} model that this view is rendering.
     */
    private final Dog dog;

    //6 disabled sprites
    /**
     *dog's disabling sprites. (right)
     */
    private static BufferedImage disabledR1, disabledR2, disabledR3, disabledR4, disabledR5, disabledR6;
    /**
     * dog's disabling sprites. (left)
     */
    private static BufferedImage disabledL1, disabledL2, disabledL3, disabledL4, disabledL5, disabledL6;


    //6 running sprites
    /**
     *dog's moving sprites. (right)
     */
    private static BufferedImage runR1, runR2, runR3, runR4, runR5, runR6;
    /**
     * dog's moving sprites. (left)
     */
    private static BufferedImage runL1, runL2, runL3, runL4, runL5, runL6;

    //4 idle sprites
    /**
     * dog idle state sprites (right)
     */
    private static BufferedImage idleR1, idleR2, idleR3, idleR4;
    /**
     * dog idle state sprites (left)
     */
    private static BufferedImage idleL1, idleL2, idleL3, idleL4;

    //chasing indicators
    /**
     * indicators of the dog sprites, if chasing or not.
     */
    private static BufferedImage chasing, notchasing;

    //used for cycling the sprites
    /**
     * sprite counter. this helps to display the dog animations.
     * it essentially is a counter tightly coupled with the game frame rate.
     * for example, if the frame rate it 60fps, then this counter will increment by 60 times per second.
     * each time this number reaches a certain threshold, the {@link #spriteNum} increments.
     */
    private int spriteCounter = 0;
    /**
     * sprite number. each dog sprite is related to this number.
     * it increments once the {@link #spriteCounter} reaches a certain threshold.
     * for example, if the sprite number is '1' when the dog is idle,
     * it will display the first idle dog sprite.
     */
    private int spriteNum = 0;

    /**
     *helper boolean field that indicates if the dog was disabled or not.
     * it helps to display the disabled animation from sprite number 1.
     */
    private boolean wasDisabled = false;

    /** creates a dog view instance, that handles the rendering / visual side of the model.
     * @param dog reference to the {@link Dog} model that his view is rendering
     */
    public DogView(Dog dog) {
        this.dog = dog;
//        loadSprites();
    }

    /**
     * loads all the dog sprites from the 'res' folder.
     * called to load all the sprites in the {@link EnemyView} constructor, so it loads all the sprites once.
     */
    public static void loadSprites() {
        try{
            disabledR1 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledR1.png"));
            disabledR2 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledR2.png"));
            disabledR3 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledR3.png"));
            disabledR4 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledR4.png"));
            disabledR5 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledR5.png"));
            disabledR6 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledR6.png"));

            disabledL1 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledL1.png"));
            disabledL2 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledL2.png"));
            disabledL3 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledL3.png"));
            disabledL4 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledL4.png"));
            disabledL5 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledL5.png"));
            disabledL6 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/disabledL6.png"));

            runR1 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runR1.png"));
            runR2 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runR2.png"));
            runR3 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runR3.png"));
            runR4 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runR4.png"));
            runR5 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runR5.png"));
            runR6 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runR6.png"));

            runL1 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runL1.png"));
            runL2 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runL2.png"));
            runL3 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runL3.png"));
            runL4 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runL4.png"));
            runL5 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runL5.png"));
            runL6 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/runL6.png"));

            idleR1 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/idleR1.png"));
            idleR2 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/idleR2.png"));
            idleR3 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/idleR3.png"));
            idleR4 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/idleR4.png"));

            idleL1 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/idleL1.png"));
            idleL2 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/idleL2.png"));
            idleL3 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/idleL3.png"));
            idleL4 = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/idleL4.png"));

            chasing = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/chasing.png"));
            notchasing = ImageIO.read(DogView.class.getClassLoader().getResourceAsStream("enemies/dog/notchasing.png"));

            System.out.println("[DogView] loaded dog sprites");
        }
        catch (Exception e){
            System.out.println("[DogView] failed to load dog sprites : ");
            e.printStackTrace();
        }

    }

    /**
     * updates the {@link #spriteCounter} depending on the dog's current state.
     * by updating the sprite counter and its sprite number, it creates an animation effect.
     * called in the {@link #draw(Graphics2D)} method.
     *
     */
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

    /** draws the dog based on its current state and position given by the {@link Dog} model.
     * the sprite counter speed is handled in {@link #updateFrameCounter()}, and the sprite
     * cycling (to create the animation) is handled by the {@link #switchDisabledSprites(String)},  {@link #switchIdleSprite(String)} and
     * {@link #switchMovingSprite(String)}.
     *
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d){
        BufferedImage image = null;
        updateFrameCounter();

        if(dog.getIsDisabled()){
            image = switchDisabledSprites(dog.getDirection());
//            System.out.println("[DogView] idle dog sprite");
        }

        else if(dog.getIsMoving()){
            image = switchMovingSprite(dog.getDirection());
//            System.out.println("[DogView] moving dog sprite");
        }

        else if(dog.getIsIdle()){
            image = switchIdleSprite(dog.getDirection());
//            System.out.println("[DogView] idle dog sprite");
        }

        //dog sprite
        g2d.drawImage(image, dog.getX(), dog.getY(), dog.getWidth(), dog.getHeight(), null);
        //alerted icon
        g2d.drawImage(dog.getIsChasing() ? chasing : notchasing, dog.getX() + 30, dog.getY() + 20, null);


        //debug info:
        //sprite size
        g2d.setColor(Color.BLACK);
        g2d.drawRect(dog.getX(), dog.getY(), dog.getWidth(), dog.getHeight());
        //actual hitbox size
        g2d.setColor(Color.GREEN);
        g2d.drawRect(dog.getX() + 20, dog.getY() + 65, dog.getWidth() - 40, dog.getHeight() - 65);
        g2d.setColor(Color.GREEN);
        g2d.drawString("dog direction: " + dog.getDirection(), dog.getX(), dog.getY() - 10);
        g2d.drawString("current patrol position: " + dog.getCurrentPatrolPosition(), dog.getX(), dog.getY() - 20);
        g2d.drawString("wall in front: " + dog.getWallInFront(), dog.getX(), dog.getY() - 30);
        g2d.drawString("is chasing: " + dog.getIsChasing(), dog.getX(), dog.getY() - 40);
        g2d.setColor(Color.WHITE);
        g2d.drawString("x: " + dog.getX() + "y:" + dog.getY(), dog.getX(), dog.getY() - 50);
        g2d.drawOval(dog.getX(), dog.getY(), 3, 3);
        g2d.drawString("dog width, height: " + dog.getWidth() + " " + dog.getHeight(), dog.getX(), dog.getY() - 60);
        g2d.drawString("[ground ahead] " + dog.getGroundAhead(), dog.getX(), dog.getY() - 70);
        g2d.drawString("[WAIT COUNTER]" + dog.getWaitCounter(), dog.getX(), dog.getY() - 80);
        g2d.drawOval(dog.getX() + dog.getWidth(), dog.getY() + dog.getHeight(), 5, 5); //bottom right corner of the dog
        g2d.drawOval(dog.getX(), dog.getY() + dog.getHeight(), 5, 5); //bottom left corner of the dog

    }


    /**switches the image based on the {@link #spriteNum} field.
     * contains the dog's idle state sprites.
     * @param direction direction of the dog
     * @return an image, current sprite of the dog.
     */
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
    /**switches the image based on the {@link #spriteNum} field.
     * contains the dog's moving state sprites.
     * @param direction direction of the dog
     * @return an image, current sprite of the dog.
     */
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

    /**switches the image based on the {@link #spriteNum} field.
     * contains the dog's disabled state sprites.
     * @param direction direction of the dog
     * @return an image, current sprite of the dog.
     */
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

}
