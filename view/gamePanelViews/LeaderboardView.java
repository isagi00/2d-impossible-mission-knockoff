package view.gamePanelViews;

import model.Leaderboard;
import model.ScreenSettings;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

public class LeaderboardView extends JPanel {



    private Leaderboard leaderboard;

    private Font leaderboardTitleFont;
    private Font leaderboardFont;
    private Font exitFont;

    private String fontPath = "fonts/ThaleahFat.ttf";

    private int padding = 50;

    public LeaderboardView(Leaderboard leaderboard){
        this.leaderboard = leaderboard;
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setOpaque(true);
        setFocusable(true);
        setBackground(Color.BLACK);

        this.leaderboardTitleFont = loadCustomFont(fontPath, 70f);
        this.leaderboardFont = loadCustomFont(fontPath, 60f);
        this.exitFont = loadCustomFont(fontPath, 30f);
    }

    private Font loadCustomFont(String fontPath, float size) {
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


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // enable antialiasing for smoother text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


//        // draw game paused title
//        g2d.setFont(pauseMenuTitleFont);
//        FontMetrics titleMetrics = g2d.getFontMetrics();
//        String title = "GAME PAUSED";
//        int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;
//        int titleY = getHeight() / 2;
//        g2d.setColor(Color.WHITE);
//        g2d.drawString(title, titleX, titleY);

        //draw leaderboard title
        g2d.setFont(leaderboardTitleFont);
        g2d.setColor(Color.ORANGE);
        String leaderboardTitle = "leaderboard";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(leaderboardTitle)) / 2;
        int titleY = padding;
        g2d.drawString(leaderboardTitle, titleX, titleY);

        List<Integer> scores = leaderboard.getLeaderbordEntryScores();
        int startY = titleY + fm.getHeight() + padding;
        g2d.setColor(Color.WHITE);
        g2d.setFont(leaderboardFont);
        for (Integer i = 0; i < scores.size(); i++) {
            g2d.drawString(i + 1 + ".", padding * 2, startY + (padding * i));
            g2d.drawString(Integer.toString(scores.get(i)), titleX, startY+ (padding * i));
            g2d.drawString("pts", titleX + (padding * 7), startY + (padding * i));
        }

        List<String>  names = leaderboard.getLeaderbordEntryNames();
        for (int i = 0; i < names.size(); i++) {
            g2d.drawString(names.get(i), padding * 3, startY + (padding * i));
        }


        g2d.setFont(exitFont);
        String exit = "< press enter to go back to the main menu";
        g2d.drawString(exit, padding, getHeight() - padding);




//        // draw menu options
//        g2d.setFont(pauseMenuFont);
//        FontMetrics optionMetrics = g2d.getFontMetrics();
//        int startY = titleY + 80;  // space below title
//        int verticalSpacing = 60;
//
//        for (int i = 0; i < OPTIONS.length; i++) {
//            int optionX = (getWidth() - optionMetrics.stringWidth(OPTIONS[i])) / 2;
//            int optionY = startY + i * verticalSpacing;
//
//            if (i == selectedOption) {
//                g2d.setColor(new Color(255, 215, 0)); // gold highlight
//            } else {
//                g2d.setColor(Color.LIGHT_GRAY);
//            }
//            g2d.drawString(OPTIONS[i], optionX, optionY);
//        }
    }













}
