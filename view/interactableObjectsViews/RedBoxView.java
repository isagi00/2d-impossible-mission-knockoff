package view.interactableObjectsViews;

import model.interactableObjects.RedBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RedBoxView {

    private BufferedImage redBox1, redBox2, redBox3, redBox4, redBox5, redBox6, redBox7, redBox8;


    private boolean isOpening = false;
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private static final int ANIMATION_SPEED = 5;

    private RedBox redBox;


    public RedBoxView(RedBox redBox) {
        this.redBox = redBox;

        loadSprites();
    }


    public void loadSprites() {
        try{
            redBox1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/redBox1.png"));
            redBox2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/redBox2.png"));
            redBox3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/redBox3.png"));
            redBox4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/redBox4.png"));
            redBox5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/redBox5.png"));
            redBox6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/redBox6.png"));
            redBox7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/redBox7.png"));
            redBox8 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/boxes/redBox8.png"));
            System.out.println("loaded red box sprites");
        }catch(IOException e){
            System.err.println("IOException while loading box images:");
            e.printStackTrace();
        }
    }



    public void update() {
        // if the box is opened then start opening it
        if (redBox.getIsOpened() && !isOpening) {
            isOpening = true;
            spriteNum = 1;
        }
        //opening
        if(isOpening){
            spriteCounter++;
            if(spriteCounter >= ANIMATION_SPEED){
                spriteCounter = 0;
                spriteNum++;

                //if reached the last frame
                if(spriteNum > 8){
                    spriteNum = 8;//stay on last frame
                }

            }
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        if(isOpening){
            switch (spriteNum) {
                case 1: image = redBox1; break;
                case 2: image = redBox2; break;
                case 3: image = redBox3; break;
                case 4: image = redBox4; break;
                case 5: image = redBox5; break;
                case 6: image = redBox6; break;
                case 7: image = redBox7; break;
                case 8: image = redBox8; break;
                default : image = redBox1; break;
                }
            }
        else{
            image = redBox1;
        }

        if (image != null) {
            g2d.drawImage(image, redBox.getX(), redBox.getY(), redBox.getWidth(), redBox.getHeight(), null);
            }
        else{//red rectangle if doesnt load
            g2d.setColor(Color.RED);
            g2d.fillRect(redBox.getX(), redBox.getY(), redBox.getWidth(), redBox.getHeight());
            }
    }

}
