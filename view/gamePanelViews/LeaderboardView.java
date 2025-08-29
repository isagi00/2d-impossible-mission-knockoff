package view.gamePanelViews;

import model.Leaderboard;
import model.ScreenSettings;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

/**
 * view, renders all the leaderboard rankings and points data provided by the {@link Leaderboard} model.
 */
public class LeaderboardView extends JPanel {

    /**
     * path of the font used, in the res folder
     */
    private final String fontPath = "fonts/ThaleahFat.ttf";

    /**
     * reference to the {@link Leaderboard} model, used to get the
     * data of the leaderboard to render.
     */
    private final Leaderboard leaderboard;

    /**
     * font used to display the leaderboard title on the top.
     */
    private final Font leaderboardTitleFont = loadCustomFont(fontPath, 70f);
    /**
     * font used to display the leaderboard contents, such as placing, name and points.
     */
    private final Font leaderboardFont = loadCustomFont(fontPath, 60f);
    /**
     * font used to the display the exit prompt on the bottom right of the leaderboard screen.
     */
    private final Font exitFont = loadCustomFont(fontPath, 30f);

    /**
     * visual padding integer that helps with the spacing consistency of the leaderboard screen.
     */
    private final int padding = 50;

    /**view, renders all the data provided by the {@link Leaderboard} model.
     * @param leaderboard the {@link Leaderboard} model
     */
    public LeaderboardView(Leaderboard leaderboard){
        this.leaderboard = leaderboard;
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setOpaque(true);
        setFocusable(true);
        setBackground(Color.BLACK);
    }

    /**loads the font at the provided font path, with the provided size.
     * @param fontPath path of the .ttf file in the res folder
     * @param size size of the font
     * @return the font if it finds it, null otherwise
     */
    private Font loadCustomFont(String fontPath, float size) {
        try{
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if(stream == null) {    //if the font path provided is not available
                System.out.println("[LeaderboardView]couldn't find font " + fontPath);
                return null;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println("[LeaderboardView]loaded font " + fontPath);
            return font;

        }catch(Exception e) {
            System.err.println("[LeaderboardView] couldn't load font " + fontPath);
            return null;
        }
    }


    /**renders the leaderboard, with the data provided by {@link Leaderboard} model.
     * draws the title, the rankings from 1 to 10 with the names and points.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // enable antialiasing for smoother text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

    }













}
