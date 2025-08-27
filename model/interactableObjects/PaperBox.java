package model.interactableObjects;

import model.ScoreTracker;
import model.ScreenSettings;
import model.entities.Player;
import model.inventoryrelated.BoBo;
import model.inventoryrelated.Item;
import model.inventoryrelated.PokerCard;

import java.awt.image.BufferedImage;




/**
 * model.
 * should contain box data/state
 * implements box state
 * provides box accessors
 *
 * should not know about view or input
 */

public class PaperBox extends InteractableObject {
    private boolean isOpened;

    public PaperBox(int x, int y) {
        super(x,y, ScreenSettings.TILE_SIZE,ScreenSettings.TILE_SIZE);   //it is 48x48
        this.isOpened = false;

    }

    @Override
    public void interact(Player player) {
        if(!isOpened){
            isOpened = true;

//            if ( !player.getInventory().isInventoryFull()){
//                player.getInventory().addItem(new BoBo());
//            }

            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getRandomPokerCard();
                player.getInventory().addItem(pokerCard);


                System.out.println("paperbor : interact() -> added new pokercard to inventory -> " + pokerCard);
            }




            System.out.println("paperbox -> paperbox opened!");
        }
    }

    public boolean getIsOpened() {
        return isOpened;
    }




}
