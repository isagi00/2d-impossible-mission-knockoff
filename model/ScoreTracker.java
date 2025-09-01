package model;

import controller.WhatsYourNameController;
import model.entities.Player;
import model.inventoryrelated.Inventory;
import model.inventoryrelated.Item;
import model.inventoryrelated.PokerCard;

import java.util.*;

/**
 * 'observer' of the '{@link Player} model, tracks the number of {@link model.interactableObjects.InteractableObject} opened, provides card sequence checkers and has various
 * computing utility methods to get the total points.
 * used in {@link view.gamePanelViews.ResultScreenView}, so that the view layer doesn't have to compute stuff.
 */
public class ScoreTracker implements Observer {


    /**
     * number of {@link model.interactableObjects.PaperBox} opened.
     */
    private int paperBoxesOpened = 0;

    /**
     * number of {@link model.interactableObjects.RedBox} opened.
     */
    private int redBoxesOpened = 0;

    /**
     * number of {@link model.interactableObjects.MetalLocker} opened.
     */
    private int metalLockersOpened = 0;
    /**
     * number of {@link model.interactableObjects.WoodLocker} opened.
     */
    private int woodLockersOpened = 0;

    /**
     * reference to the one and only {@link Leaderboard} instance
     */
    private Leaderboard leaderBoard = Leaderboard.getInstance();

    /**
     * the ScoreTracker tracks the player's (observable) total interactable object searches through the observer pattern
     * whener the interactable object gets opened, fires an event to notify this object
     *
     * @param o   the observable object, in this case it is {@link Player}
     * @param arg an argument passed to the {@code notifyObservers}
     *            method, in this case the arguments are strings given by the interactable objects
     */
    @Override
    public void update(Observable o, Object arg) {
        if( o instanceof Player) {
            if (arg.equals("paper box opened")) {
                this.paperBoxesOpened++;
                getObjectPoints();
//                System.out.println("[ScoreTracker] paper box opened: " + paperBoxesOpened);
            }
            else if (arg.equals("red box opened")) {
                this.redBoxesOpened++;
                getObjectPoints();
                System.out.println("[ScoreTracker] red box opened: " + redBoxesOpened);
            }
            else if (arg.equals("metal locker opened")) {
                this.metalLockersOpened++;
                getObjectPoints();
                System.out.println("[ScoreTracker] metal locker opened: " + metalLockersOpened);
            }
            else if ( arg.equals("wood locker opened")) {
                this.woodLockersOpened++;
                getObjectPoints();
                System.out.println("[ScoreTracker] wood lockers opened: " + woodLockersOpened);
            }
        }
    }


    /**
     * returns a list of the values of the current player's inventory player cards
     * @param player current instance of the player
     * @return returns a list of poker cards values (integers).
     */
    public List<Integer> getCardValues(Player player) {
        Inventory inventory = player.getInventory();
        List<Integer> pokerCardValues = new ArrayList<>();
        if (inventory != null) {
            for (Item item : inventory.getItems()) {
                if (item instanceof PokerCard pokerCard) {
                    pokerCardValues.add(pokerCard.getValue());
                }
            }
//            System.out.println("[ScoreTracker] card values: " + pokerCardValues);
        }
        return pokerCardValues;
    }

    /**
     * checks if the player's cards form a 'straight'
     * @param pokerCardValues list of Integers, that represent the card values
     * @return true if the 5 cards represent a 'straight' (5 consecutive cards ex 1 2 3 4 5); also handles the ace-low and ace-high edge cases.
     */
    //5 consecutive cards
    public boolean checkIsStraight(List<Integer> pokerCardValues) {
        if (pokerCardValues.size() < 5) { return false; }
        boolean isStraight = true;

        Collections.sort(pokerCardValues);
        //special cases:
        //[ace 2 3 4 5] is an ace-low straight, so then [2, 3, 4, 5, 50] is still considered a straight
        //[10 11 12 13 ace] is ace-high straight, so then [10,11,12,13,50 is still considered a straight
        List<Integer> acelow = Arrays.asList(2, 3, 4, 5, 50);
        List<Integer> acehigh = Arrays.asList(10, 11, 12, 13, 50);
        if(pokerCardValues.equals(acelow) || pokerCardValues.equals(acehigh)) {
            return true;
        }

        //check if 5 cards are consecutive normally
        for (int i = 0; i < pokerCardValues.size() - 1; i++) {
            int val = pokerCardValues.get(i);
            if (pokerCardValues.get(i + 1 ) != val + 1) { //check if current card value + 1 is equal to the next card value :)
                isStraight = false;
                break; //exit the loop
            }
        }
        return isStraight;
    }

    /**
     * checks if the player's cards are '4 of a single kind' (ex. 1 heart, 2 heart, 3 heart, 4 heart)
     * @param pokerCardValues list of player card values
     * @return true if the cards are '4 of a kind'
     */
    public boolean checkIsFourOfAKind(List<Integer> pokerCardValues) {
        if(pokerCardValues.size() < 4) { return false; }
        //count frequency of each card , if it is 4 then return true
        //note that while it is inefficient, i am absolutely too lazy to make it more efficient... i am sorry.
        Collections.sort(pokerCardValues);

        for (int i = 0; i < pokerCardValues.size() - 1; i++) {
            int count = 1;
            int val = pokerCardValues.get(i);
            for(int j = i + 1; j < pokerCardValues.size(); j++) {
                if (pokerCardValues.get(j) == val) {
                    count++;
                }
            }
            if (count == 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if the player's cards are '3 of a kind'
     * @param pokerCardValues list of players card values
     * @return true if cards are '3 of a kind'
     */
    public boolean checkIsThreeOfAKind(List<Integer> pokerCardValues) {
        if(pokerCardValues.size() < 3) { return false; }
        Collections.sort(pokerCardValues);
        for (int i = 0; i < pokerCardValues.size() - 1; i++) {
            int count = 1;
            int val = pokerCardValues.get(i);
            for(int j = i + 1; j < pokerCardValues.size(); j++) {
                if (pokerCardValues.get(j) == val) {
                    count++;
                }
            }
            if (count == 3) {
                return true;
            }
        }
        return false;
    }

    /**checks if there is a '2 pair' sequence in the player's cards
     * @param pokerCardValues list of player card values
     * @return  true if there are 2 pairs of the same card, a single pair is true if both are the same value.
     */
    public boolean checkIsTwopair(List<Integer> pokerCardValues) {
        if(pokerCardValues.size() < 4) { return false; }
        Collections.sort(pokerCardValues);
        int pairs = 0;
        int i = 0;

        while (i < pokerCardValues.size() - 1) {
            if (pokerCardValues.get(i) == pokerCardValues.get(i + 1)) {
                pairs++;
                i += 2; //skup the next card
            } else {
                i++;
            }
        }
        return pairs == 2;
    }

    /**checks is all 5 cards are all face cards. (ex j, j, q, q, k would return true)
     * @param pokerCardValues card values
     * @return true if 'is all face cards'
     */
    public boolean checkIsAllFaceCards(List<Integer> pokerCardValues) {
        if(pokerCardValues.size() < 5) { return false; }
        Collections.sort(pokerCardValues);
        boolean allFaceCards = true;
        for (int i = 0; i < pokerCardValues.size(); i++) {
            int val = pokerCardValues.get(i);
            if (val < 11 || val > 13) {     //face cards are 11, 12, 13
                allFaceCards = false;
            }
        }
        return allFaceCards;
    }


    /**
     * @return number of {@link model.interactableObjects.PaperBox} opened * 100 (pts)
     */
    public int getPaperBoxTotalPoints(){
        return paperBoxesOpened * 100;
    }

    /**
     * @return number of {@link model.interactableObjects.RedBox} opened *1000 (pts)
     */
    public int getRedBoxesTotalPoints(){
        return redBoxesOpened * 1000;
    }

    /**
     * @return number of {@link model.interactableObjects.MetalLocker} opened *500 (pts)
     */
    public int getMetalLockersTotalPoints(){
        return metalLockersOpened * 500;
    }

    /**
     * @return number of {@link model.interactableObjects.WoodLocker} opened *250 (pts)
     */
    public int getWoodLockersTotalPoints(){
        return woodLockersOpened * 250;
    }


    /**
     * returns total points acquired through the player cards
     * @param pokerCardValues player card values
     * @return total sum of the card values * 1000
     */
    public int getPokerCardsTotalPoints(List<Integer> pokerCardValues){
        int sum = 0;
        for (int i = 0; i < pokerCardValues.size(); i++) {
            sum += pokerCardValues.get(i);
        }
        return sum * 1000;
    }

    /**
     * returns total points got from the interactable objects
     * @return total points got from the interactable objects opened
     */
    public int getObjectPoints(){
        int paperBoxTotalPoints = getPaperBoxTotalPoints();
        int redBoxesTotalPoints = getRedBoxesTotalPoints();
        int metalLockersTotalPoints = getMetalLockersTotalPoints();
        int woodLockersTotalPoints = getWoodLockersTotalPoints();

//        System.out.println("[ScoreTracker]Paper boxes pts: " + getPaperBoxTotalPoints);
//        System.out.println("[ScoreTracker]Red boxes pts : " + redBoxesTotalPoints);
//        System.out.println("[ScoreTracker]Metal lockers pts : " + metalLockersTotalPoints);
//        System.out.println("[ScoreTracker]Wood lockers pts: " + woodLockersTotalPoints);

        return paperBoxTotalPoints + redBoxesTotalPoints + metalLockersTotalPoints + woodLockersTotalPoints;
    }

    /**returns total points given by interactable objects + total poker cards points
     * @param pokerCardValues list of player card values
     * @return total points given by interactable objects + total poker cards points
     */
    public int getTotalPoints( List<Integer> pokerCardValues){
        return getObjectPoints() + getPokerCardsTotalPoints(pokerCardValues);
    }


    /**
     * computes the grand total points, and adds them to the {@link Leaderboard} .txt file.
     * the name is fetched from {@link WhatsYourNameController} class.
     * @param pokerCardValues list of card values
     * @return grand total points: the total points of the player and multiplies them if they are {@link #checkIsStraight(List)}, {@link #checkIsAllFaceCards(List)},
     * {@link #checkIsFourOfAKind(List)}, {@link #checkIsThreeOfAKind(List), {@link #checkIsTwopair(List)}
     *
     */
    public int getGrandTotalPoints(List<Integer> pokerCardValues){
        int objectpoints = getObjectPoints();
        int cardpoints = getPokerCardsTotalPoints(pokerCardValues);
        int total = objectpoints + cardpoints;
        if(checkIsStraight(pokerCardValues)) {
            total *= 5;
        }
        if(checkIsAllFaceCards(pokerCardValues)) {
            total *= 4;
        }
        if(checkIsFourOfAKind(pokerCardValues)) {
            total *= 4;
        }
        if(checkIsThreeOfAKind(pokerCardValues)) {
            total *= 3;
        }
        if(checkIsTwopair(pokerCardValues)) {
            total *= 2;
        }


        String name = WhatsYourNameController.getSubmittedName();
        //after calculating the gran total points, add the points to the leaderboard.
        if (name != null) {     // dont add to leaderboard if the tutorial was played.
            leaderBoard.addScore(name, total);        //foken beautiful, it only gets added once, thought it would add multiple times
        }
        System.out.println("[ScoreTracker][getGrandTotalPoints] added points to leaderboard:  " + name + " " + total );
        return total;
    }


    /**
     * @return number of {@link model.interactableObjects.PaperBox} opened
     */
    public int getPaperBoxesOpened() {
        return paperBoxesOpened;
    }

    /**
     * @return number of  {@link model.interactableObjects.RedBox} opened
     */
    public int getRedBoxesOpened() {
        return redBoxesOpened;
    }

    /**
     * @return  number of {@link model.interactableObjects.MetalLocker} opened
     */
    public int getMetalLockersOpened() {
        return metalLockersOpened;
    }

    /**
     * @return  number of {@link model.interactableObjects.WoodLocker} opened
     */
    public int getWoodLockersOpened() {
        return woodLockersOpened;
    }


}
