package view;


import model.ScoreTracker;
import model.ScreenSettings;
import model.entities.Player;

import java.awt.*;
import java.io.InputStream;
import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class ResultScreenView extends JPanel  {

    private Font titleFont = loadCustomFont("fonts/ThaleahFat.ttf", 70f);
    private Font resultFont = loadCustomFont("fonts/ThaleahFat.ttf", 40f);
    private Font totalFont = loadCustomFont("fonts/ThaleahFat.ttf", 65f);
    private Font multiplierFont = loadCustomFont("fonts/ThaleahFat.ttf", 65f);
    private Player player;
    private ScoreTracker scoreTracker;



    private int padding = 50;


    public ResultScreenView(Player player) {
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setOpaque(false);       //allow transparency
        this.player = player;
        this.scoreTracker = player.getScoreTracker();

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
        //enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        g2d.fillRect(0, 0, ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT);


        String title = "EXTRACTION COMPLETED!";
        FontMetrics titleMetrics = g2d.getFontMetrics();
        g2d.setColor(Color.GREEN);
        g2d.setFont(titleFont);
        g2d.drawString(title, (ScreenSettings.SCREEN_WIDTH / 2) - (titleMetrics.stringWidth(title) / 2) - 50 , padding);

        String pts = "pts";
        int ptsX = padding * 24;
        int xX = padding * 15;
        int totalPointsX = padding * 20;

        g2d.setFont(resultFont);
        g2d.setColor(Color.WHITE);
        String paperboxes = "paper boxes opened: ";
        String paperboxesopened = String.valueOf(scoreTracker.getPaperBoxesOpened());
        g2d.drawString(paperboxes, padding, titleMetrics.getHeight() + padding * 2);
        g2d.drawString(paperboxesopened, padding * 13, titleMetrics.getHeight() + padding * 2);
        g2d.drawString("x 100", xX, titleMetrics.getHeight() + padding * 2);
        g2d.drawString("=  " + scoreTracker.getPaperBoxTotalPoints(), totalPointsX, titleMetrics.getHeight() + padding * 2);

        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 2);


        String redboxes = "red boxes opened: ";
        String redboxesopened = String.valueOf(scoreTracker.getRedBoxesOpened());
        g2d.drawString(redboxes, padding, titleMetrics.getHeight() + padding * 3);
        g2d.drawString(redboxesopened, padding * 13, titleMetrics.getHeight() + padding * 3);
        g2d.drawString("x 1000", xX, titleMetrics.getHeight() + padding * 3);
        g2d.drawString("=  " + scoreTracker.getRedBoxesTotalPoints(), totalPointsX, titleMetrics.getHeight() + padding * 3);

        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 3);

        String metallockers = "metal lockers opened: ";
        String metallockersopened = String.valueOf(scoreTracker.getMetalLockersOpened());
        g2d.drawString(metallockers, padding, titleMetrics.getHeight() + padding * 4);
        g2d.drawString(metallockersopened, padding * 13, titleMetrics.getHeight() + padding * 4);
        g2d.drawString("x 500", xX, titleMetrics.getHeight() + padding * 4);
        g2d.drawString("=  " + scoreTracker.getMetalLockersTotalPoints(), totalPointsX, titleMetrics.getHeight() + padding * 4);

        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 4);

        String woodlockers = "wooden lockers opened: ";
        String woodlockersopened = String.valueOf(scoreTracker.getWoodLockersOpened());
        g2d.drawString(woodlockers, padding, titleMetrics.getHeight() + padding * 5);
        g2d.drawString(woodlockersopened, padding * 13, titleMetrics.getHeight() + padding * 5);
        g2d.drawString("x 250", xX, titleMetrics.getHeight() + padding * 5);
        g2d.drawString("=  " + scoreTracker.getWoodLockersTotalPoints(), totalPointsX, titleMetrics.getHeight() + padding * 5);

        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 5);


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
        g2d.drawString(values, padding * 9, titleMetrics.getHeight() + padding * 6);
        g2d.drawString(cards, padding, titleMetrics.getHeight() + padding * 6);
        g2d.drawString("x 1000", xX, titleMetrics.getHeight() + padding * 6);
        g2d.drawString( "= " + scoreTracker.getPokerCardsTotalPoints(cardValues), totalPointsX, titleMetrics.getHeight() + padding * 6);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 6);



        g2d.drawString("total:", padding, titleMetrics.getHeight() + padding * 7);
        g2d.drawString( "= " + scoreTracker.getTotalPoints(cardValues), totalPointsX, titleMetrics.getHeight() + padding * 7);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 7);


        String multiplier = "multipliers: ";
        g2d.setColor(Color.MAGENTA);
        g2d.setFont(multiplierFont);
        g2d.drawString(multiplier, padding * 13, titleMetrics.getHeight() + padding * 8);
        g2d.setFont(resultFont);


        String straight = "STRAIGHT: ";
        g2d.setColor(scoreTracker.getIsStraight(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(straight, padding, titleMetrics.getHeight() + padding * 9);
        g2d.drawString("pts x 5" , totalPointsX, titleMetrics.getHeight() + padding * 9);


        String allfacecards = "ALL FACE CARDS: ";
        g2d.setColor(scoreTracker.getIsAllFaceCards(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(allfacecards, padding, titleMetrics.getHeight() + padding * 10);
        g2d.drawString("pts x 4", totalPointsX, titleMetrics.getHeight() + padding * 10);


        String fourofakind = "FOUR OF A KIND: ";
        g2d.setColor(scoreTracker.getIsFourOfAKind(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(fourofakind, padding, titleMetrics.getHeight() + padding * 11);
        g2d.drawString("pts x 4 " , totalPointsX, titleMetrics.getHeight() + padding * 11);



        String threeofakind = "THREE OF A KIND: ";
        g2d.setColor(scoreTracker.getIsThreeOfAKind(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(threeofakind, padding, titleMetrics.getHeight() + padding * 12);
        g2d.drawString("pts x 3", totalPointsX, titleMetrics.getHeight() + padding * 12);

        String twopair = "TWO PAIR: ";
        g2d.setColor(scoreTracker.getIsTwoPair(cardValues) ? Color.YELLOW : Color.GRAY);
        g2d.drawString(twopair, padding, titleMetrics.getHeight() + padding * 13);
        g2d.drawString("pts x 2  " , totalPointsX, titleMetrics.getHeight() + padding * 13);


        g2d.setColor(Color.ORANGE);
        g2d.setFont(totalFont);
        String total = "GRAND TOTAL :       " + scoreTracker.getGrandTotalPoints(cardValues);
        g2d.drawString(total, padding, titleMetrics.getHeight() + padding * 14);
        g2d.drawString(pts, ptsX, titleMetrics.getHeight() + padding * 14);

    }


}
