package model.interactableObjects;

import model.ScreenSettings;
import model.entities.Player;
import model.inventoryrelated.PokerCard;
import view.AudioManager;

public class RedBox extends InteractableObject {

    private boolean isOpened = false;


    public RedBox(int x, int y) {
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);

        this.addObserver(AudioManager.getInstance());
    }

    @Override
    public void open(Player player) {   //called once the player completes the interaction
        if(!isOpened){
            isOpened = true;
            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getBoostedAcePokerCard();        //higher chance to get aces
                player.getInventory().addItem(pokerCard);
//                System.out.println("paperbox : open() -> added new pokercard to inventory -> " + pokerCard);
                if (pokerCard.getValue() > 10){
                    setChanged();
                    notifyObservers("rare card found");
                    clearChanged();
                }
                else{
                    setChanged();
                    notifyObservers("common card found");
                    clearChanged();
                }
            }
        }
    }

    public boolean getIsOpened(){
        return isOpened;
    }


}
