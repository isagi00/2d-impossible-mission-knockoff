package view.interactableObjectsViews;


import model.interactableObjects.Computer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * view of the {@link Computer} interactable object
 */
public class ComputerView {

    /**
     * computer sprites, static so they are shared between all computer view instances
     */
    private static BufferedImage computer1, computer2, computer3, computer4;

    /**
     * animation speed of the computer
     */
    private final int ANIMATION_SPEED = 5;

    /**
     * used to time the animation. it is incremented once per frame in the {@link #update()}, so if framerate is 60fps, then it will be incremented 60 times per second.
     */
    private int spriteCounter = 0;

    /**
     * number of the sprite currently active
     */
    private int spriteNum = 1;

    /**
     * {@link Computer} instance
     */
    private Computer computer;

    /**
     * width of the computer menu
     */
    private int MENU_WIDTH = 300;

    /**
     * height of the computer menu
     */
    private int MENU_HEIGHT = 90;

    /**
     * font used to display the menu text
     */
    private Font menuFont;


    /**view of the {@link Computer} model, loads sprite when called via {@link #loadSprites()}
     * @param computer {@link Computer} model instance
     */
    public ComputerView(Computer computer) {
        this.computer = computer;

        loadSprites();
        this.menuFont = loadFont("fonts/ThaleahFat.ttf", 30f);
    }

    /**
     * loads the computer sprites
     */
    private void loadSprites(){
        try{
            computer1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/computer/computer1.png"));
            computer2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/computer/computer2.png"));
            computer3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/computer/computer2.png"));
            computer4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("interactableObjects/computer/computer2.png"));
            System.out.println("[ComputerView]computer sprites loaded");
        }catch(Exception e){
            System.out.println("[ComputerView]error in loading computer sprites ");
            e.printStackTrace();
        }
    }


    /**
     * updates the sprite counter. increments the {@link #spriteCounter} once per call.
     */
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


    /**
     * draw the computer sprite at the {@link Computer} x and y coordinates
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d) {
        BufferedImage image = null;

        //switch the sprite nummber to draw the appropriate image. the sprite number gets updated in the update() method
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


    /**draw the {@link Computer}'s menu when it is visible
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    private void drawMenu(Graphics2D g2d) {
        Font originalFont = g2d.getFont();      //save the original font

        int menuX = computer.getX() + computer.getWidth() + 20;
        int menuY = computer.getY() - computer.getHeight() - 40;

        //menu background
        g2d.setColor(new Color(0,0,0, 120));    //semi transparent
        g2d.fillRoundRect(menuX, menuY, MENU_WIDTH, MENU_HEIGHT,10,10);

        //menu border
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(menuX, menuY, MENU_WIDTH, MENU_HEIGHT,10,10);


        //menu options
        String[] options = computer.getMenuOptions();
        String option1 = options[0];
        String option2 = options[1];
        g2d.setFont(menuFont);
        g2d.drawString(option1, menuX + 45 , menuY + 45);
        g2d.drawString(option2, menuX + 45 , menuY + 65);

        //currently selected option
        if(computer.getSelectedMenuOption() == 0){
            g2d.drawString(">>", menuX + 15 , menuY + 45);
        }
        if(computer.getSelectedMenuOption() == 1){
            g2d.drawString(">>", menuX + 15 , menuY + 65);
        }

        g2d.setFont(originalFont);  //had some issues that when the computer menu appears, the other fonts would get resized
    }


    /**
     * loads the font
     * @param fontPath path of the .ttf file
     * @param size font size
     * @return font if font path is correct, null otherwise
     */
    private Font loadFont(String fontPath, float size) {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if (stream == null) {    //if the font path provided is not available
                System.out.println("[ComputerView] couldn't find font " + fontPath);
                return null;
            }
            //create the Font object and set it to the current Graphics Environment
            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);   //ttf stands for true type font
            //note: Font object in java are immutable, derive font here just changes the size of the font.

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); //register the font with java graphics system
            ge.registerFont(font);
            System.out.println("[ComputerView]loaded font " + fontPath);
            return font;

        } catch (Exception e) {
            System.err.println("[ComputerView]couldn't load font " + fontPath);
            return null;    //return null if the font path is not correct / available
        }
    }



}
