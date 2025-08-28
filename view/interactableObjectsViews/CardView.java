package view.interactableObjectsViews;

import model.interactableObjects.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * view of the {@link Card} used to access the computer.
 */
public class CardView {

    /**
     * card sprites. static, so shared across all card view instances
     */
    private static BufferedImage card1, card2, card3, card4, card5, card6, card7, card8;

    /**
     * used to time the sprites animation
     */
    private int spriteCounter = 0;

    /**
     * number of the sprite currently active
     */
    private int spriteNum = 1;

    /**
     * animation speed of the sprite
     */
    private static final int ANIMATION_SPEED = 5;

    /**
     * {@link Card} instance
     */
    private final Card card;


    /**
     * when the card is picked up, a CardView is created to display the card animation / sprites.
     * when created, it loads all the card related sprites via {@link #loadSprites()}
     * @param card card interactable object
     */
    public CardView(Card card) {
        this.card = card;

        loadSprites();
    }


    /**
     * loads the card sprites from the resources folder
     */
    private void loadSprites(){
        try{
            card1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card1.png"));
            card2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card2.png"));
            card3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card3.png"));
            card4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card4.png"));
            card5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card5.png"));
            card6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card6.png"));
            card7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card7.png"));
            card8 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card8.png"));
            System.out.println("[CardView]loaded card sprites");
        }catch (Exception e){
            System.out.println("[CardView]failed to load card sprites");
            e.printStackTrace();
        }
    }


    /**
     * updates the sprite counter and makes the sprite number change accordingly
     */
    public void update() {
        spriteCounter++;
        if(spriteCounter >= ANIMATION_SPEED){
            spriteCounter = 0;
            spriteNum++;
            if(spriteNum == 9){ //reset to the first sprite number once reached the end
                spriteNum = 1;
            }
        }
    }

    /**
     * if the card is not taken yet, then draws the card sprite at {@link Card} x and y position
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d) {
        BufferedImage image;
        if(!card.getIsTaken()) {
            switch(spriteNum){
                case 1: image = card1; break;
                case 2: image = card2; break;
                case 3: image = card3; break;
                case 4: image = card4; break;
                case 5: image = card5; break;
                case 6: image = card6; break;
                case 7: image = card7; break;
                case 8: image = card8; break;
                default: image = card1; break;
            }
            if (image != null) {
                g2d.drawImage(image, card.getX(), card.getY(), card.getWidth(), card.getHeight(), null);
            }
        }
    }






}
