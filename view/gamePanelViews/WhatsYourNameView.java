package view.gamePanelViews;

import model.ScreenSettings;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class WhatsYourNameView extends JPanel {

    private final String fontPath = "fonts/ThaleahFat.ttf";
    private Font whatsYourNameFont = loadCustomFont(fontPath, 60f);
    private Font nameTypedInFont = loadCustomFont(fontPath, 80f);


    private final int padding = 50;
    private String displayedName = "";

    public WhatsYourNameView() {
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setBackground(Color.BLACK);    //semi transparent
        setOpaque(false);               //allow   transparency
        setFocusable(true);
    }


    private Font loadCustomFont(String fontPath, float size) {
        try{
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if(stream == null) {    //if the font path provided is not available
                System.out.println("[WhatsYourNameView] cant find font " + fontPath);
                return null;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println("[WhatsYourNameView] loaded font " + fontPath);
            return font;

        }catch(Exception e) {
            System.err.println("[WhatsYourNameView] couldn't load font " + fontPath);
            return null;
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


        //whats your name? title
        g2d.setFont(whatsYourNameFont);
        g2d.setColor(Color.WHITE);
        String whatsYourName = "wass yo name? (is for the leaderboard :)";
        FontMetrics fm = g2d.getFontMetrics();
        int nameX = (getWidth() - fm.stringWidth(whatsYourName)) / 2;
        int nameY = ScreenSettings.SCREEN_HEIGHT / 2;
        g2d.drawString(whatsYourName, nameX, nameY);

        //current name typed in
        g2d.setFont(nameTypedInFont);
        g2d.drawString(displayedName, padding * 12, nameY + 100);

        int maxLength = 7;  //well shit, anyways it is 7 in the whatsyourname controller
        for (int i = 0; i < maxLength; i++) {
            g2d.drawString(".", padding * 12 + ( padding * i), nameY + 130);
        }
    }




    public void updateNameDisplay(String currentName){
        this.displayedName = currentName;       //getting the current char typed in from the controller
        repaint();  //ofc repaint
    }










}
