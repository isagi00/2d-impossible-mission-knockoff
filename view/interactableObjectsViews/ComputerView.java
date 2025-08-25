package view.interactableObjectsViews;


import model.interactableObjects.Computer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ComputerView {
    BufferedImage computer1, computer2, computer3, computer4;
    int ANIMATION_SPEED = 10;
    int spriteCounter = 0;
    int spriteNum = 1;
    Computer computer;

    private int MENU_WIDTH = 300;
    private int MENU_HEIGHT = 150;

    private Font menuFont;


    public ComputerView(Computer computer) {
        this.computer = computer;

        loadSprites();
        this.menuFont = loadMenuFont("fonts/ThaleahFat.ttf", 30f);
    }

    public void loadSprites(){
        try{
            computer1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/computer/computer1.png"));
            computer2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/computer/computer2.png"));
            computer3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/computer/computer2.png"));
            computer4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/computer/computer2.png"));
            System.out.println("computer sprites loaded");
        }catch(Exception e){
            System.out.println("error in loading computer sprites : ");
            e.printStackTrace();
        }
    }


    public void update() {
        spriteCounter++;
        if (spriteCounter >= ANIMATION_SPEED){
            spriteCounter = 0;
            spriteNum++;
        }
        if (spriteNum == 4){
            spriteNum = 1;
        }
    }


    public void draw(Graphics2D g2d) {
        BufferedImage image = null;

        switch(spriteNum){
            case 1: image = computer1; break;
            case 2: image = computer2; break;
            case 3: image = computer3; break;
            case 4: image = computer4; break;
            default : image = computer1; break;
        }
        if(image != null){
            g2d.drawImage(image,computer.getX(), computer.getY(), computer.getWidth(), computer.getHeight(), null);
        }

        if(computer.getIsBeingAccessed() && computer.getIsMenuVisible()){
            drawMenu(g2d);
        }
    }


    private void drawMenu(Graphics2D g2d) {
        Font originalFont = g2d.getFont();

        int menuX = computer.getX() + computer.getWidth() + 20;
        int menuY = computer.getY() - computer.getHeight() - 40;

        //menu background
        g2d.setColor(new Color(0,0,0, 220));    //semi transparent
        g2d.fillRoundRect(menuX, menuY, MENU_WIDTH, MENU_HEIGHT,10,10);

        //BORDER?
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(menuX, menuY, MENU_WIDTH, MENU_HEIGHT,10,10);


        //menu options
        String[] options = computer.getMenuOptions();
        String deactivateEnemies = options[0];
        String other = options[1];

        g2d.setFont(menuFont);
        g2d.drawString(deactivateEnemies, menuX + 45 , menuY + 45);
        g2d.drawString(other, menuX + 45 , menuY + 65);
        //currently selected option
        if(computer.getSelectedMenuOption() == 0){
            g2d.drawString(">", menuX + 15 , menuY + 45);
        }
        if(computer.getSelectedMenuOption() == 1){
            g2d.drawString(">", menuX + 15 , menuY + 65);
        }

        g2d.setFont(originalFont);


    }


    private Font loadMenuFont(String fontPath, float size){
        try{
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if(stream == null) {    //if the font path provided is not available
                System.out.println("Couldn't find font " + fontPath);
                return new Font("Arial", Font.BOLD, (int) size);
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println("Loaded font " + fontPath);
            return font;

        }catch(Exception e) {
            System.err.println("Couldn't load font " + fontPath);
            return new Font("Arial", Font.BOLD, (int) size);
        }

    }



}
