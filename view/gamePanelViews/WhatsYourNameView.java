package view.gamePanelViews;

import model.ScreenSettings;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

/**
 * view that user sees when he presses the 'new game' in the title screen.
 * works in tandem with {@link controller.WhatsYourNameController}, which handles player's keyboard inputs.
 * this view displays the user's name currently typed in, used later for logging the score if the user
 * successfully extracts, in the {@link model.Leaderboard}.
 */
public class WhatsYourNameView extends JPanel {

    /**
     * the path of the .ttf file in res folder
     */
    private final String fontPath = "fonts/ThaleahFat.ttf";
    /**
     * font used to display the title that says 'what's your name?'. the font is loaded via {@link #loadFont(String, float)} method.
     */
    private final Font whatsYourNameFont = loadFont(fontPath, 60f);
    /**
     * font used to display the currently typed in name. the font is loaded via {@link #loadFont(String, float)} method.
     */
    private final Font nameTypedInFont = loadFont(fontPath, 80f);


    /**
     * a fixed integer, that is the 'padding' to allow a consistent alignment of the text
     */
    private final int padding = 50;
    /**
     * represents the currently displayed name.
     */
    private String displayedName = "";

    /**
     * view, handles the rendering the of the 'what's your name?' panel that is visible right after
     * the user pressed 'new game' on the title screen.
     * the name entered is used to track the player's score in the leaderboard.
     */
    public WhatsYourNameView() {
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setBackground(Color.BLACK);    //semi transparent
        setOpaque(false);               //allow   transparency
        setFocusable(true);
    }


    /**loads the font at the provided font path, with the provided size.
     * @param fontPath path of the .ttf file in the res folder
     * @param size size of the font
     * @return the font if it finds it, null otherwise
     */
    private Font loadFont(String fontPath, float size) {
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


    /**renders this view's contents. sets the transparent background, the title, renders the
     * currently name being typed in, and the dashed under the name.
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
        //dashed under the name that helps the user know what's the limit
        int maxLength = 7;  //hard coded, but it is 7 in the what's your name controller
        for (int i = 0; i < maxLength; i++) {
            g2d.drawString("-", padding * 12 + ( padding * i), nameY + 130);
        }
    }


    /**updates the {@link #displayedName} field and requests a repaint of the window
     * @param currentName the string that is currently typed in. it is handled/called in the {@link controller.WhatsYourNameController}
     */
    public void updateNameDisplay(String currentName){
        this.displayedName = currentName;       //getting the current char typed in from the controller
        repaint();  //ofc repaint
    }










}
