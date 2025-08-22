package model;

import model.entities.Player;
import model.inventoryrelated.Inventory;
import model.inventoryrelated.Item;
import model.inventoryrelated.PokerCard;

import java.util.*;

//OBSERVER OF PLAYER.
//WHEN PLAYER PERFORMS A SPECIFIC ACTION, THE SCORE GETS UPDATED
//
public class ScoreTracker implements Observer {

    private int paperBoxesOpened = 0;
    private int redBoxesOpened = 0;
    private int metalLockersOpened = 0;
    private int woodLockersOpened = 0;


    @Override
    public void update(Observable o, Object arg) {
        if( o instanceof Player) {
            if (arg.equals("paper box opened")) {
                paperBoxesOpened++;
                getObjectPoints();
                System.out.println("score tracker -> paper box opened: " + paperBoxesOpened);
            }
            else if (arg.equals("red box opened")) {
                redBoxesOpened++;
                getObjectPoints();
                System.out.println("score tracker -> red box opened: " + redBoxesOpened);
            }
            else if (arg.equals("metal locker opened")) {
                metalLockersOpened++;
                getObjectPoints();
                System.out.println("score tracker -> meta lockers opened: " + metalLockersOpened);
            }
            else if ( arg.equals("wood locker opened")) {
                woodLockersOpened++;
                getObjectPoints();
                System.out.println("scoretracker -> wood lockers opened: " + woodLockersOpened);
            }
        }
    }



    public List<Integer> getCardValues(Player player) {
        Inventory inventory = player.getInventory();
        List<Integer> pokerCardValues = new ArrayList<>();
        if (inventory != null) {
            for (Item item : inventory.getItems()) {
                if (item instanceof PokerCard pokerCard) {
                    pokerCardValues.add(pokerCard.getValue());
                }
            }
//            System.out.println("score tracker -> card values: " + pokerCardValues);
        }
        return pokerCardValues;
    }

    //5 consecutive cards
    public boolean getIsStraight(List<Integer> pokerCardValues) {
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

    public boolean getIsFourOfAKind(List<Integer> pokerCardValues) {
        if(pokerCardValues.size() < 4) { return false; }
        //count frequency of each card , if it is 4 then return true
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

    public boolean getIsThreeOfAKind(List<Integer> pokerCardValues) {
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

    public boolean getIsTwoPair(List<Integer> pokerCardValues) {
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

    public boolean getIsAllFaceCards(List<Integer> pokerCardValues) {
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


    public int getPaperBoxTotalPoints(){
        return paperBoxesOpened * 100;
    }

    public int getRedBoxesTotalPoints(){
        return redBoxesOpened * 1000;
    }

    public int getMetalLockersTotalPoints(){
        return metalLockersOpened * 500;
    }

    public int getWoodLockersTotalPoints(){
        return woodLockersOpened * 250;
    }


    public int getPokerCardsTotalPoints(List<Integer> pokerCardValues){
        int sum = 0;
        for (int i = 0; i < pokerCardValues.size(); i++) {
            sum += pokerCardValues.get(i);
        }
        return sum * 1000;
    }

    public int getObjectPoints(){
        int getPaperBoxTotalPoints = getPaperBoxTotalPoints();
        int redBoxesTotalPoints = getRedBoxesTotalPoints();
        int metalLockersTotalPoints = getMetalLockersTotalPoints();
        int woodLockersTotalPoints = getWoodLockersTotalPoints();

        System.out.println("Paper boxes pts: " + getPaperBoxTotalPoints);
        System.out.println("Red boxes pts : " + redBoxesTotalPoints);
        System.out.println("Metal lockers pts : " + metalLockersTotalPoints);
        System.out.println("Wood lockers pts: " + woodLockersTotalPoints);

        return getPaperBoxTotalPoints + redBoxesTotalPoints + metalLockersTotalPoints + woodLockersTotalPoints;
    }

    public int getTotalPoints( List<Integer> pokerCardValues){
        return getObjectPoints() + getPokerCardsTotalPoints(pokerCardValues);
    }



    public int getGrandTotalPoints(List<Integer> pokerCardValues){
        int objectpoints = getObjectPoints();
        int cardpoints = getPokerCardsTotalPoints(pokerCardValues);
        int total = objectpoints + cardpoints;
        if(getIsStraight(pokerCardValues)) {
            total *= 5;
        }
        if(getIsAllFaceCards(pokerCardValues)) {
            total *= 4;
        }
        if(getIsFourOfAKind(pokerCardValues)) {
            total *= 4;
        }
        if(getIsThreeOfAKind(pokerCardValues)) {
            total *= 3;
        }
        if(getIsTwoPair(pokerCardValues)) {
            total *= 2;
        }
        return total;
    }









    public int getPaperBoxesOpened() {
        return paperBoxesOpened;
    }

    public int getRedBoxesOpened() {
        return redBoxesOpened;
    }

    public int getMetalLockersOpened() {
        return metalLockersOpened;
    }

    public int getWoodLockersOpened() {
        return woodLockersOpened;
    }


}
