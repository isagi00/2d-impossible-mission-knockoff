package view.entityViews;

import model.entities.Drone;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * view counterpart of the {@link Drone} model.
 * handles the rendering logic of a single drone instance.
 * all the drone sprites are loaded once in the {@link EnemyView} constructor.
 */
public class DroneView {

    /**
     * reference to the {@link Drone} model
     */
    private final Drone drone;
    /**
     * drone attacking sprites: initially wanted to implement the re-enabling of a drone attack, scrapped the idea.
     * maybe another time... @todo
     */
    private static BufferedImage attack1, attack2, attack3, attack4, attack5, attack6;
    /**
     * drone disabling sprites (left)
     */
    private static BufferedImage disabledL1, disabledL2, disabledL3, disabledL4, disabledL5, disabledL6;
    /**
     * drone disabling sprites (right)
     */
    private static BufferedImage disabledR1, disabledR2, disabledR3, disabledR4, disabledR5, disabledR6;
    /**
     * drone enabling sprites: these are in the res folder. initially wanted to implement the re-enabling of the drones
     * in the room after some time, but scrapped the idea.
     */
    private static BufferedImage enable1, enable2, enable3, enable4, enable5, enable6;
    /**
     * drone idle sprites (right, unused)
     */
    private static  BufferedImage idleR1, idleR2, idleR3, idleR4;
    /**
     * drone idle sprites (left, unused)
     */
    private static  BufferedImage idleL1, idleL2, idleL3, idleL4;
    /**
     * drone walking sprites (right)
     */
    private static BufferedImage walkR1, walkR2, walkR3, walkR4, walkR5, walkR6;
    /**
     * drone walking sprites (left)
     */
    private static BufferedImage walkL1, walkL2, walkL3, walkL4, walkL5, walkL6;
    //chasing indicators
    /**
     * indicators of the drone sprites, if chasing or not.
     */
    private static BufferedImage chasing, notChasing;
    /**
     * sprite counter. this helps to display the drone animations.
     * it essentially is a counter tightly coupled with the game frame rate.
     * for example, if the frame rate it 60fps, then this counter will increment by 60 times per second.
     * each time this number reaches a certain threshold, the {@link #spriteNum} increments.
     */
    private int spriteCounter = 0;
    /**
     * sprite number. each drone sprite is related to this number.
     * it increments once the {@link #spriteCounter} reaches a certain threshold.
     * for example, if the sprite number is '1' when the drone is idle,
     * it will display the first idle drone sprite.
     */
    private int spriteNum  = 0;
    /**
     *helper boolean field that indicates if the drone was disabled or not.
     * it helps to display the disabled animation from sprite number 1.
     */
    private boolean wasDisabled = false;

    /** creates a drone view instance, that handles the rendering / visual side of the model.
     * @param drone reference to the {@link Drone} model that his view is rendering
     */
    public DroneView(Drone drone) {
        this.drone = drone;
    }


    /**
     * loads all the drone sprites from the 'res' folder.
     * called to load all the sprites in the {@link EnemyView} constructor, so it loads all the sprites once.
     */

    public static void loadSprites() {
        try{
            attack1 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/attack1.png"));
            attack2 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/attack2.png"));
            attack3 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/attack3.png"));
            attack4 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/attack4.png"));
            attack5 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/attack5.png"));
            attack6 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/attack6.png"));

            disabledR1 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledR1.png"));
            disabledR2 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledR2.png"));
            disabledR3 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledR3.png"));
            disabledR4 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledR4.png"));
            disabledR5 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledR5.png"));
            disabledR6 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledR6.png"));

            disabledL1 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledL1.png"));
            disabledL2 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledL2.png"));
            disabledL3 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledL3.png"));
            disabledL4 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledL4.png"));
            disabledL5 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledL5.png"));
            disabledL6 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/disabledL6.png"));

            enable1 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/enable1.png"));
            enable2 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/enable2.png"));
            enable3 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/enable3.png"));
            enable4 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/enable4.png"));
            enable5 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/enable5.png"));
            enable6 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/enable6.png"));

            idleR1 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/idleR1.png"));
            idleR2 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/idleR2.png"));
            idleR3 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/idleR3.png"));
            idleR4 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/idleR4.png"));

            idleL1 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/idleL1.png"));
            idleL2 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/idleL2.png"));
            idleL3 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/idleL3.png"));
            idleL4 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/idleL4.png"));

            walkR1 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkR1.png"));
            walkR2 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkR2.png"));
            walkR3 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkR3.png"));
            walkR4 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkR4.png"));
            walkR5 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkR5.png"));
            walkR6 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkR6.png"));

            walkL1 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkL1.png"));
            walkL2 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkL2.png"));
            walkL3 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkL3.png"));
            walkL4 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkL4.png"));
            walkL5 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkL5.png"));
            walkL6 = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/walkL6.png"));

            chasing = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/chasing.png"));
            notChasing = ImageIO.read(DroneView.class.getClassLoader().getResourceAsStream("enemies/drone/notchasing.png"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * updates the {@link #spriteCounter} depending on the drone's current state.
     * by updating the sprite counter and its sprite number, it creates an animation effect.
     * called in the {@link #draw(Graphics2D)} method.
     */
    private void updateFrameCounter(){
        spriteCounter++;

        //check if drone just became disabled
        if (drone.getIsDisabled() && !wasDisabled){
            spriteNum = 1; //start from first frame
            spriteCounter = 0;
            wasDisabled = true;
        }


        if (drone.getIsDisabled()){
            if (spriteCounter  >= 15) {
                spriteNum = spriteNum + 1;
                spriteCounter = 0;
                if (spriteNum >= 6) {
                    spriteNum = 6;
                }
            }
        }

        else if (drone.getIsIdle()){
            // idle animation   (unused becuase had to implement different behaviour. it is here tho)
            if (spriteCounter  >= 30) {     //slower than running animation
                spriteNum = (spriteNum % 4) + 1; //cycle through 4 idle frames
                spriteCounter = 0;
            }
        }

        else if(drone.getIsMoving()){
            //moving animation
            if (spriteCounter  >= 6) {     //slower than running animation
                spriteNum = (spriteNum % 6) + 1; //cycle through 4 idle frames
                spriteCounter = 0;
            }
        }

    }
//----------------------------------------------------------------------------------------------------------------//
// main draw method
//----------------------------------------------------------------------------------------------------------------//
    /** draws the drone based on its current state and position given by the {@link Drone} model.
     * the sprite counter speed is handled in {@link #updateFrameCounter()}, and the sprite
     * cycling (to create the animation) is handled by the {@link #switchDisabledSprites(String)},  {@link #switchIdleSprite(String)}(unused) and
     * {@link #switchMovingSprite(String)}.
     *
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d){
        BufferedImage image = null;
        updateFrameCounter();

        if (drone.getIsDisabled()){
            image = switchDisabledSprites(drone.getDirection());
        }

        else if (drone.getIsIdle()){
            image = switchIdleSprite(drone.getDirection());
        }

        else if (drone.getIsMoving()){
            image = switchMovingSprite(drone.getDirection());
        }


        //drone images
        g2d.drawImage(image, drone.getX(), drone.getY(), drone.getWidth(), drone.getHeight(), null);
        //drone is chasing indicator
        g2d.drawImage(drone.getIsChasing() ? chasing : notChasing, drone.getX() + 10, drone.getY() - 20, null);

////        debug info:
//        //drone image size
//        g2d.drawRect(drone.getX(), drone.getY(), drone.getWidth(), drone.getHeight());
//        //drone hitbox
//        g2d.setColor(Color.GREEN);
//        g2d.drawRect(drone.getX() + 10, drone.getY() + 25, drone.getWidth() - 20, drone.getHeight() - 25);
//        //info
//        g2d.drawString("drone direction: " + drone.getDirection(), drone.getX(), drone.getY() - 10);
//        g2d.drawString("current patrol position: " + drone.getCurrentPatrolPosition(), drone.getX(), drone.getY() - 20);
//        g2d.drawString("wall in front: " + drone.getWallInFront(), drone.getX(), drone.getY() - 30);
//        g2d.drawString("is chasing: " + drone.getIsChasing(), drone.getX(), drone.getY() - 40);
//        g2d.drawString("x: " + drone.getX() + "y:" + drone.getY(), drone.getX(), drone.getY() - 50);
//        g2d.drawString("drone width, height: " + drone.getWidth() + " " + drone.getHeight(), drone.getX(), drone.getY() - 60);
//        g2d.drawOval(drone.getX(), drone.getY(), 3, 3);
//        g2d.drawOval(drone.getX() + drone.getWidth(), drone.getY() + drone.getHeight(), 5, 5); //bottom right corner of the drone
//        g2d.drawOval(drone.getX(), drone.getY() + drone.getHeight(), 5, 5); //bottom left corner of the drone

    }

//----------------------------------------------------------------------------------------------------------------//
// sprites switch statements
//----------------------------------------------------------------------------------------------------------------//
    /**switches the image based on the {@link #spriteNum} field.
     * contains the drone's idle state sprites.
     * note that it is currently unused, because the drone never enter into a 'idle' state.
     * @param direction direction of the drone
     * @return an image, current sprite of the drone.
     */
    private BufferedImage switchIdleSprite(String direction) {
        BufferedImage image = null;

        if (direction.equals("right")) {
            switch (spriteNum) {
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
        else if (direction.equals("left")) {
            switch (spriteNum) {
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
                default: image = idleL1;
            }
        }
        return image;
    }
    /**switches the image based on the {@link #spriteNum} field.
     * contains the drone's moving state sprites.
     * @param direction direction of the drone
     * @return an image, current sprite of the drone.
     */
    private BufferedImage switchMovingSprite(String direction) {
        BufferedImage image = null;
        if (direction.equals("right")) {
            switch (spriteNum) {
                case 1:
                    image = walkR1;
                    break;
                case 2:
                    image = walkR2;
                    break;
                case 3:
                    image = walkR3;
                    break;
                case 4:
                    image = walkR4;
                    break;
                case 5:
                    image = walkR5;
                    break;
                case 6:
                    image = walkR6;
                    break;
                default:
                    image = walkR1;
            }
        }
        else if (direction.equals("left")) {
            switch (spriteNum) {
                case 1:
                    image = walkL1;
                    break;
                case 2:
                    image = walkL2;
                    break;
                case 3:
                    image = walkL3;
                    break;
                case 4:
                    image = walkL4;
                    break;
                case 5:
                    image = walkL5;
                    break;
                case 6:
                    image = walkL6;
                    break;
                default:
                    image = walkL1;
                    break;
            }
        }

        return image;
    }
    /**switches the image based on the {@link #spriteNum} field.
     * contains the drone's disabled state sprites.
     * @param direction direction of the drone
     * @return an image, current sprite of the drone.
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
