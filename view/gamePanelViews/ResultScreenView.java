package view.gamePanelViews;


import model.ScoreTracker;
import model.ScreenSettings;
import model.entities.Player;

import java.awt.*;
import java.io.InputStream;
import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * view of the result screen, renders the result screen based on the {@link Player}'s {@link ScoreTracker}.
 */
public class ResultScreenView extends JPanel  {

    /**
     * font used to display the 'extraction completed' title on top of the screen.
     */
    private final Font titleFont = loadCustomFont("fonts/ThaleahFat.ttf", 70f);
    /**
     * default font used to display the results of the player.
     * the font is used to display the total interactable objects opened, card points, the total points got from the interactable
     * objects + card points and the multipliers.
     */
    private final Font resultFont = loadCustomFont("fonts/ThaleahFat.ttf", 40f);
    /**
     * font that is used to display the 'grand total points' at the end.
     */
    private final Font grandTotalFont = loadCustomFont("fonts/ThaleahFat.ttf", 65f);
    /**
     * font that is used to display the 'multipliers' title before the multipliers.
     */
    private final Font multiplierFont = loadCustomFont("fonts/ThaleahFat.ttf", 65f);
    /**
     * reference to the {@link Player} model. to get the that player's instance's {@link ScoreTracker} model.
     *
     */
    private final Player player;
    /**
     * reference to the {@link ScoreTracker} model. this is where all the score info is taken from to display the
     * result screen.
     */
    private final ScoreTracker scoreTracker;

    /**
     * visual padding integer that helps with the spacing consistency of the result screen.
     */
    private final int padding = 50;


    /**view, handles the rendering of the result screen, with the data provided by the {@link ScoreTracker} model.
     * @param player {@link Player} model
     */
    public ResultScreenView(Player player) {
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setOpaque(false);       //allow transparency
        setFocusable(true);

        this.player = player;
        this.scoreTracker = player.getScoreTracker();
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
                System.out.println("[ResultScreenView]cant find font " + fontPath);
                return null;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println(" [ResultScreenView]loaded font " + fontPath);
            return font;

        }catch(Exception e) {
            System.err.println("[ResultScreenView]Couldn't load font " + fontPath);
            return null;
        }
    }


    /**renders the result screen.
     * renders all the interactable objects opened during gameplay, total points, and the multipliers.
     * the scores are tracked in the {@link ScoreTracker} model, so the view knows what to display.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        g2d.fillRect(0, 0, ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT);


        g2d.setFont(titleFont);
        g2d.setColor(Color.GREEN);
        String title = "EXTRACTION COMPLETED!";
        FontMetrics titleMetrics = g2d.getFontMetrics();
        g2d.drawString(title, (ScreenSettings.SCREEN_WIDTH / 2) - (titleMetrics.stringWidth(title) / 2) , padding);

        String pts = "pts";
        int ptsX = padding * 24;
        int xX = padding * 15;
        int totalPointsX = padding * 20;

        g2d.setFont(resultFont);
        g2d.setColor(Color.WHITE);
        //paper boxes opened score
        String paperboxes = "paper boxes opened: ";
        String paperboxesopened = String.valueOf(scoreTracker.getPaperBoxesOpened());
        g2d.drawString(paperboxes, padding, titleMetrics.getHeight() + padding );
        g2d.drawString(paperboxesopened, padding * 13, titleMetrics.getHeight() + padding);
        g2d.drawString("x 100", xX, titleMetrics.getHeight() + padding);
        g2d.drawString("=  " + scoreTracker.getPaperBoxTotalPoints(), totalPointsX, titleMetrics.getHeight() + padding);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding);

        //red boxes opened score
        String redboxes = "red boxes opened: ";
        String redboxesopened = String.valueOf(scoreTracker.getRedBoxesOpened());
        g2d.drawString(redboxes, padding, titleMetrics.getHeight() + padding * 2);
        g2d.drawString(redboxesopened, padding * 13, titleMetrics.getHeight() + padding * 2);
        g2d.drawString("x 1000", xX, titleMetrics.getHeight() + padding * 2);
        g2d.drawString("=  " + scoreTracker.getRedBoxesTotalPoints(), totalPointsX, titleMetrics.getHeight() + padding * 2);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 2);

        //metal lockers opened score
        String metallockers = "metal lockers opened: ";
        String metallockersopened = String.valueOf(scoreTracker.getMetalLockersOpened());
        g2d.drawString(metallockers, padding, titleMetrics.getHeight() + padding * 3);
        g2d.drawString(metallockersopened, padding * 13, titleMetrics.getHeight() + padding * 3);
        g2d.drawString("x 500", xX, titleMetrics.getHeight() + padding * 3);
        g2d.drawString("=  " + scoreTracker.getMetalLockersTotalPoints(), totalPointsX, titleMetrics.getHeight() + padding * 3);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 3);

        //wood lockers opened score
        String woodlockers = "wooden lockers opened: ";
        String woodlockersopened = String.valueOf(scoreTracker.getWoodLockersOpened());
        g2d.drawString(woodlockers, padding, titleMetrics.getHeight() + padding * 4);
        g2d.drawString(woodlockersopened, padding * 13, titleMetrics.getHeight() + padding * 4);
        g2d.drawString("x 250", xX, titleMetrics.getHeight() + padding * 4);
        g2d.drawString("=  " + scoreTracker.getWoodLockersTotalPoints(), totalPointsX, titleMetrics.getHeight() + padding * 4);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 4);

        //score from cards
        String cards = "card values: ";
        List<Integer> cardValues = scoreTracker.getCardValues(player);  //move this outside later
        String values;
        if (cardValues.isEmpty()) {         //if the player has no cards just display 0
            values = "0";
        }
        else {
            values = cardValues.stream()
                    .map(cardValue -> String.valueOf(cardValue))
                    .collect(Collectors.joining(" + "));
        }
        g2d.drawString(values, padding * 7, titleMetrics.getHeight() + padding * 5);
        g2d.drawString(cards, padding, titleMetrics.getHeight() + padding * 5);
        g2d.drawString("x 1000", xX, titleMetrics.getHeight() + padding * 5);
        g2d.drawString( "=  " + scoreTracker.getPokerCardsTotalPoints(cardValues), totalPointsX, titleMetrics.getHeight() + padding * 5);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 5);

        // total: interactable object pts + card pts
        g2d.drawString("total:", padding, titleMetrics.getHeight() + padding * 6);
        g2d.drawString( "=  " + scoreTracker.getTotalPoints(cardValues), totalPointsX, titleMetrics.getHeight() + padding * 6);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 6);

        //multipliers (title)
        String multiplier = "multipliers: ";
        g2d.setColor(Color.MAGENTA);
        g2d.setFont(multiplierFont);
        g2d.drawString(multiplier, padding * 13, titleMetrics.getHeight() + padding * 7);
        g2d.setFont(resultFont);

        //straight set
        String straight = "STRAIGHT: ";
        g2d.setColor(scoreTracker.checkIsStraight(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(straight, padding, titleMetrics.getHeight() + padding * 8);
        g2d.drawString("pts x 5" , totalPointsX, titleMetrics.getHeight() + padding * 8);

        //all face cards set
        String allfacecards = "ALL FACE CARDS: ";
        g2d.setColor(scoreTracker.checkIsAllFaceCards(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(allfacecards, padding, titleMetrics.getHeight() + padding * 9);
        g2d.drawString("pts x 4", totalPointsX, titleMetrics.getHeight() + padding * 9);

        //four of a kind set
        String fourofakind = "FOUR OF A KIND: ";
        g2d.setColor(scoreTracker.checkIsFourOfAKind(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(fourofakind, padding, titleMetrics.getHeight() + padding * 10);
        g2d.drawString("pts x 4 " , totalPointsX, titleMetrics.getHeight() + padding * 10);

        //three of a kind set
        String threeofakind = "THREE OF A KIND: ";
        g2d.setColor(scoreTracker.checkIsThreeOfAKind(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(threeofakind, padding, titleMetrics.getHeight() + padding * 11);
        g2d.drawString("pts x 3", totalPointsX, titleMetrics.getHeight() + padding * 11);

        //two pair set
        String twopair = "TWO PAIR: ";
        g2d.setColor(scoreTracker.checkIsTwopair(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(twopair, padding, titleMetrics.getHeight() + padding * 12);
        g2d.drawString("pts x 2  " , totalPointsX, titleMetrics.getHeight() + padding * 12);

        //grand total (with multipliers)
        g2d.setColor(Color.ORANGE);
        g2d.setFont(grandTotalFont);
        String total = "GRAND TOTAL :       " + scoreTracker.getGrandTotalPoints(cardValues);
        g2d.drawString(total, padding, titleMetrics.getHeight() + padding * 13);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 13);

        //to the main menu text
        g2d.setFont(resultFont);
        g2d.setColor(Color.WHITE);
        g2d.drawString("press enter to go to the main menu", padding, titleMetrics.getHeight() + padding * 14 - 15);
    }


}
