package view.gamePanelViews;

import model.ScreenSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * view of the title screen, handles the rendering/logic of the title screen.
 *
 */
public class TitleScreenView extends JPanel {
    /**
     * the title image displayed in the title screen.
     */
    private static  BufferedImage titleImage;
    /**
     * the menu font, of the {@link #options}
     */
    private final Font menuFont = loadCustomFont("fonts/ThaleahFat.ttf", 40f);
    /**
     * the font of the currently selected font. note that is slightly bigger than the {@link #menuFont},
     * so that the used has an even better clarity of the current selected option.
     */
    private final Font selectedFont = loadCustomFont("fonts/ThaleahFat.ttf", 50f);; //the currently selected option is gonna have a slightly larger font
    /**
     * the currently selected option, it can be incremented or decremented via {@link #selectNextOption()} and
     * {@link #selectPreviousOption()}.
     */
    private int selectedOption = 0;

    //menu options
    /**
     * contains the menu option that are displayed in the title screen
     */
    private final String[] options = {
            "new game",
            "play tutorial",
            "leaderboard",
            "exit"
    };


    /**
     * view of the title screen. handles the rendering of all the options in the title screen, title image.
     * the inputs are handled in the {@link controller.TitleScreenController}.
     */
    public TitleScreenView() {
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        loadTitleScreenBackgroundImage();
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
                System.out.println("[TitleScreenView]cant find font " + fontPath);
                return null;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println("[TitleScreenView]loaded font " + fontPath);
            return font;

        }catch(Exception e) {
            System.err.println("[TitleScreenView]couldn't load font " + fontPath);
            return null;
        }
    }


    /**
     * loads the title screen image from the res folder.
     */
    private void loadTitleScreenBackgroundImage() {
        try{
            titleImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("titlescreen/title.png"));
            System.out.println("Loaded title ");
        }catch (Exception e) {
            System.err.println("Couldn't load title lul ");
        }
    }

    /**
     * selects the next option in the {@link #options} field
     */
    public void selectNextOption() {
        if (selectedOption != options.length - 1) {
            selectedOption = (selectedOption + 1) % options.length;
        }
    }

    /**
     * selects the previous option in the {@link #options} field
     */
    public void selectPreviousOption() {
        if (selectedOption != 0) {
            selectedOption = (selectedOption - 1) % options.length;
        }
    }

    /**gets the currently selected option of the used via the 'enter key'.
     * see {@link controller.TitleScreenController}.
     * @return the currently selected option
     */
    public String getSelectedOption() {
        return options[selectedOption];
    }


    /**renders the entirety of the title screen: the title image, the various options, such as 'new game', 'play tutorial',
     * 'leaderboard' and 'exit game', listed in the {@link #options}.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //background image
        g2d.drawImage(titleImage, 0, 0, ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT,  null);

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
