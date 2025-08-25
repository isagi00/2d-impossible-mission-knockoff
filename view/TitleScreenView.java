package view;

import model.ScreenSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class TitleScreenView extends JPanel {
    BufferedImage title;
    private final Font menuFont;
    private final Font selectedFont; //the currently selected option is gonna have a slightly larger font
    private int selectedOption = 0;

    //menu options
    private final String[] options = {
            "new game",
            "play tutorial",
            "leaderboard",
            "quit"
    };



    public TitleScreenView() {
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setBackground(Color.BLACK);

        menuFont = loadCustomFont("fonts/ThaleahFat.ttf", 40f);
        selectedFont = loadCustomFont("fonts/ThaleahFat.ttf", 50f);
        loadTitleScreenBackgroundImage();
    }


    private Font loadCustomFont(String fontPath, float size) {
        try{
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if(stream == null) {    //if the font path provided is not available
                System.out.println("cant find font " + fontPath);
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


    private void loadTitleScreenBackgroundImage() {
        try{
            title = ImageIO.read(getClass().getClassLoader().getResourceAsStream("titlescreen/title.png"));
            System.out.println("Loaded title ");
        }catch (Exception e) {
            System.err.println("Couldn't load title lul ");
        }
    }

    public void selectNextOption() {
        if (selectedOption != options.length - 1) {
            selectedOption = (selectedOption + 1) % options.length;
        }
    }

    public void selectPreviousOption() {
        if (selectedOption != 0) {
            selectedOption = (selectedOption - 1) % options.length;
        }
    }

    public String getSelectedOption() {
        return options[selectedOption];
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //background image
        g2d.drawImage(title, 0, 0, ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT,  null);

        //draw menu options
        int startX = ScreenSettings.SCREEN_WIDTH / 2 - 100;
        int startY = ScreenSettings.SCREEN_HEIGHT / 2 + 100;
        int verticalSpacing = 60;

        for (int i = 0; i < options.length; i++) {

            Font fontToUse = i == selectedOption ? selectedFont : menuFont;

            if (i == selectedOption) {  //highlighted
                g2d.setFont(fontToUse);
                g2d.setColor(new Color(255,215, 0));
                g2d.drawString(options[i], startX, startY + (verticalSpacing * i));
            }
            else{ //not highlighted/selected -> gray
                g2d.setFont(fontToUse);
                g2d.setColor(Color.GRAY);
            }
            g2d.drawString(options[i], startX, startY + (verticalSpacing * i));
        }

        //instructions bottom
        g2d.setFont(menuFont.deriveFont(24f));
        g2d.setColor(Color.DARK_GRAY);
        String instructions = "Use arrow keys to navigate, ENTER to select";
        int instrX = (ScreenSettings.SCREEN_WIDTH - g2d.getFontMetrics().stringWidth(instructions)) / 2;
        g2d.drawString(instructions, instrX, ScreenSettings.SCREEN_HEIGHT - 50);
    }





}
