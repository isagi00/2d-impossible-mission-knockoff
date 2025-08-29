package view.gamePanelViews;

import model.ScreenSettings;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

/**
 * view, renders the pause menu.
 */
public class PauseMenuView extends JPanel{


    /**
     * the currently selected option in the pause menu.
     * it is updated via {@link #selectNextOption()} and {@link #selectPreviousOption()}
     */
    private int selectedOption;
    /**
     * the available options in the pause menu.
     * there a 2 currently available: 'resume' and 'to the main menu'
     */
    private final String[] options = {"resume", "to the main menu"};
    /**
     * font used for the options in the pause menu
     */
    private final Font pauseMenuFont = loadCustomFont("fonts/ThaleahFat.ttf", 40f);
    /**
     * font used in the title of the pause menu, the 'game paused' section.
     */
    private final Font pauseMenuTitleFont = loadCustomFont("fonts/ThaleahFat.ttf", 60f);

    /**
     * view of the pause menu, renders the pause menu.
     */
    public PauseMenuView() {
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setBackground(Color.BLACK);    //semi transparent
        setOpaque(false);               //allow transparency
        setFocusable(true);
    }
    /**
     * selects the next option in {@link #options}.
     * called in {@link controller.PauseMenuController}.
     */
    public void selectNextOption(){
        if (selectedOption < options.length - 1){
            selectedOption = (selectedOption + 1) %  options.length;
        }
    }

    /**
     * selects the previous option in {@link #options}.
     * called in {@link controller.PauseMenuController}.
     */
    public void selectPreviousOption(){
        if (selectedOption > 0){
            selectedOption = (selectedOption - 1) %  options.length;
        }
    }

    /**
     * @return the currently selected option in the pause menu
     */
    public String getSelectedOption(){
        return options[selectedOption];
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
                System.out.println("[PauseMenuView]cant find font " + fontPath);
                return null;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println("[PauseMenuView]loaded font " + fontPath);
            return font;

        }catch(Exception e) {
            System.err.println("[PauseMenuView]couldn't load font " + fontPath);
            return null;
        }
    }


    /**renders the pause menu screen, with the transparent background and the available options in {@link #options}.
     * @param g the <code>Graphics</code> object to protect
     */
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

        for (int i = 0; i < options.length; i++) {
            int optionX = (getWidth() - optionMetrics.stringWidth(options[i])) / 2;
            int optionY = startY + i * verticalSpacing;

            if (i == selectedOption) {
                g2d.setColor(new Color(255, 215, 0)); // gold highlight
            } else {
                g2d.setColor(Color.LIGHT_GRAY);
            }
            g2d.drawString(options[i], optionX, optionY);
        }
    }


}

