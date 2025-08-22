package view;

import model.ScreenSettings;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class PauseMenuView extends JPanel{


    private int selectedOption;
    private String[] OPTIONS = {"resume", "to the main menu"};
    private Font pauseMenuFont = loadCustomFont("fonts/ThaleahFat.ttf", 40f);
    private Font pauseMenuTitleFont = loadCustomFont("fonts/ThaleahFat.ttf", 60f);

    public PauseMenuView() {
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setBackground(Color.BLACK);    //semi transparent
        setOpaque(false);               //allow   transparency
        setFocusable(true);
    }

    public void selectNextOption(){
        selectedOption = (selectedOption + 1) %  OPTIONS.length;
    }

    public void selectPreviousOption(){
        selectedOption = (selectedOption - 1) %  OPTIONS.length;
    }

    public String getSelectedOption(){
        return OPTIONS[selectedOption];
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


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // enable antialiasing for smoother text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // transparent background
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // draw game paused title
        g2d.setFont(pauseMenuTitleFont);
        FontMetrics titleMetrics = g2d.getFontMetrics();
        String title = "GAME PAUSED";
        int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;
        int titleY = getHeight() / 2;
        g2d.setColor(Color.WHITE);
        g2d.drawString(title, titleX, titleY);

        // draw menu options
        g2d.setFont(pauseMenuFont);
        FontMetrics optionMetrics = g2d.getFontMetrics();
        int startY = titleY + 80;  // space below title
        int verticalSpacing = 60;

        for (int i = 0; i < OPTIONS.length; i++) {
            int optionX = (getWidth() - optionMetrics.stringWidth(OPTIONS[i])) / 2;
            int optionY = startY + i * verticalSpacing;

            if (i == selectedOption) {
                g2d.setColor(new Color(255, 215, 0)); // gold highlight
            } else {
                g2d.setColor(Color.LIGHT_GRAY);
            }
            g2d.drawString(OPTIONS[i], optionX, optionY);
        }
    }


}

