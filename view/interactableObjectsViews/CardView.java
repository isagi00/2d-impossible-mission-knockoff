package view.interactableObjectsViews;

import model.interactableObjects.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CardView {

    BufferedImage card1, card2, card3, card4, card5, card6, card7, card8;
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private static final int ANIMATION_SPEED = 5;

    Card card;


    public CardView(Card card) {
        this.card = card;

        loadSprites();
    }


    public void loadSprites(){
        try{
            card1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card1.png"));
            card2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card2.png"));
            card3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card3.png"));
            card4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card4.png"));
            card5 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card5.png"));
            card6 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card6.png"));
            card7 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card7.png"));
            card8 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/card/card8.png"));
            System.out.println("loaded card sprites");
        }catch (Exception e){
            System.out.println("failed to load card sprites");
            e.printStackTrace();
        }
    }


    public void update() {
        spriteCounter++;
        if(spriteCounter >= ANIMATION_SPEED){
            spriteCounter = 0;
            spriteNum++;
            if(spriteNum == 9){
                spriteNum = 1;
            }
        }
    }

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
