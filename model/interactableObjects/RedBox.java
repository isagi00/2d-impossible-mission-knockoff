package model.interactableObjects;

import model.ScreenSettings;
import model.entities.Player;
import model.inventoryrelated.PokerCard;

public class RedBox extends InteractableObject {

    private boolean isOpened = false;


    public RedBox(int x, int y) {
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);
    }

    @Override
    public void interact(Player player) {   //called once once the player completes the interaction
        if(!isOpened){
            isOpened = true;
//            if ( !player.getInventory().isInventoryFull()){
//                player.getInventory().addItem(new BoBo());
//            }

            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getBoostedAcePokerCard();        //higher chance to get aces
                player.getInventory().addItem(pokerCard);
                System.out.println("paperbor : interact() -> added new pokercard to inventory -> " + pokerCard);
            }
            System.out.println("redbox opened");
        }
    }

    public boolean getIsOpened(){
        return isOpened;
    }


}
