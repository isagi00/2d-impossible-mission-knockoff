package view;

import model.entities.Drone;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DroneView implements Observer {

    private Drone drone;


    private BufferedImage attack1, attack2, attack3, attack4, attack5, attack6;

    private BufferedImage disabledL1, disabledL2, disabledL3, disabledL4, disabledL5, disabledL6;
    private BufferedImage disabledR1, disabledR2, disabledR3, disabledR4, disabledR5, disabledR6;

    private BufferedImage enable1, enable2, enable3, enable4, enable5, enable6;

    private BufferedImage idleR1, idleR2, idleR3, idleR4;
    private BufferedImage idleL1, idleL2, idleL3, idleL4;

    private BufferedImage walkR1, walkR2, walkR3, walkR4, walkR5, walkR6;
    private BufferedImage walkL1, walkL2, walkL3, walkL4, walkL5, walkL6;

    private BufferedImage chasing, notChasing;

    private int spriteCounter = 0;
    private int spriteNum  = 0;


    public DroneView(Drone drone) {
        this.drone = drone;
        loadSprites();

        this.drone.addObserver(this);
    }




    private void loadSprites() {
        try{
            attack1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/attack1.png"));
            attack2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/attack2.png"));
            attack3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/attack3.png"));
            attack4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/attack4.png"));
            attack5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/attack5.png"));
            attack6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/attack6.png"));

            disabledR1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledR1.png"));
            disabledR2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledR2.png"));
            disabledR3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledR3.png"));
            disabledR4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledR4.png"));
            disabledR5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledR5.png"));
            disabledR6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledR6.png"));

            disabledL1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledL1.png"));
            disabledL2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledL2.png"));
            disabledL3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledL3.png"));
            disabledL4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledL4.png"));
            disabledL5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledL5.png"));
            disabledL6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/disabledL6.png"));

            enable1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/enable1.png"));
            enable2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/enable2.png"));
            enable3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/enable3.png"));
            enable4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/enable4.png"));
            enable5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/enable5.png"));
            enable6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/enable6.png"));

            idleR1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/idleR1.png"));
            idleR2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/idleR2.png"));
            idleR3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/idleR3.png"));
            idleR4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/idleR4.png"));

            idleL1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/idleL1.png"));
            idleL2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/idleL2.png"));
            idleL3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/idleL3.png"));
            idleL4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/idleL4.png"));

            walkR1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkR1.png"));
            walkR2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkR2.png"));
            walkR3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkR3.png"));
            walkR4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkR4.png"));
            walkR5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkR5.png"));
            walkR6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkR6.png"));

            walkL1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkL1.png"));
            walkL2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkL2.png"));
            walkL3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkL3.png"));
            walkL4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkL4.png"));
            walkL5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkL5.png"));
            walkL6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/walkL6.png"));

            chasing = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/chasing.png"));
            notChasing = ImageIO.read(getClass().getClassLoader().getResourceAsStream("enemies/drone/notchasing.png"));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private void updateFrameCounter(){
        spriteCounter++;
        if (drone.getIsIdle()){
            // idle animation
            spriteCounter++;
            if (spriteCounter  >= 15) {     //slower than running animation
                spriteNum = (spriteNum % 4) + 1; //cycle through 4 idle frames
                spriteCounter = 0;
            }
        }

        if(drone.getIsMoving()){
            //moving animation
            spriteCounter++;
            if (spriteCounter  >= 8) {     //slower than running animation
                spriteNum = (spriteNum % 6) + 1; //cycle through 4 idle frames
                spriteCounter = 0;
            }
        }

        if (drone.getIsDisabled()){
            spriteCounter++;
            if (spriteCounter  >= 5) {
                spriteNum = spriteNum + 1;
                spriteCounter = 0;
                if (spriteNum >= 6) {
                    spriteNum = 6;
                }
            }

        }

    }
//----------------------------------------------------------------------------------------------------------------//
// main draw method
//----------------------------------------------------------------------------------------------------------------//

    protected void draw(Graphics2D g2d){
        BufferedImage image = null;
        updateFrameCounter();

        if (drone.getIsIdle()){
            image = switchIdleSprite(drone.getDirection());
        }

        if (drone.getIsMoving()){
            image = switchMovingSprite(drone.getDirection());
        }

        if (drone.getIsDisabled()){
            image = switchDisabledSprites(drone.getDirection());
        }


        g2d.drawImage(image, drone.getX(), drone.getY(), drone.getWidth(), drone.getHeight(), null);

        //not chasing
        if (drone.getIsChasing() ?
                g2d.drawImage(chasing, drone.getX() + drone.getWidth(), drone.getY() - 20, null) :
                g2d.drawImage(notChasing, drone.getX() + drone.getWidth(), drone.getY() - 20, null))


        g2d.drawRect(drone.getX(), drone.getY(), drone.getWidth(), drone.getHeight());
        g2d.setColor(Color.GREEN);
        g2d.drawRect(drone.getX() + 5, drone.getY() + 15, drone.getWidth() - 10, drone.getHeight() - 15);

        g2d.setColor(Color.YELLOW);
        g2d.drawString("drone direction: " + drone.getDirection(), drone.getX(), drone.getY() - 10);
        g2d.drawString("current patrol position: " + drone.getCurrentPatrolPosition(), drone.getX(), drone.getY() - 20);
        g2d.drawString("wall in front: " + drone.getWallInFront(), drone.getX(), drone.getY() - 30);
        g2d.drawString("is chasing: " + drone.getIsChasing(), drone.getX(), drone.getY() - 40);


        g2d.setColor(Color.WHITE);
        g2d.drawString("x: " + drone.getX() + "y:" + drone.getY(), drone.getX(), drone.getY() - 50);

        g2d.drawOval(drone.getX(), drone.getY(), 3, 3);

        g2d.drawString("drone width, height: " + drone.getWidth() + " " + drone.getHeight(), drone.getX(), drone.getY() - 60);

    }

//----------------------------------------------------------------------------------------------------------------//
// sprites switch statements
//----------------------------------------------------------------------------------------------------------------//

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
        if (o instanceof  Drone drone) {

        }
    }
}
