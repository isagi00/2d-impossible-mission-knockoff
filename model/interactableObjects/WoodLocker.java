package model.interactableObjects;

import model.ScreenSettings;
import model.entities.Player;
import model.inventoryrelated.PokerCard;
import view.AudioManager;

public class WoodLocker extends InteractableObject {

    public boolean isOpened = false;

    public WoodLocker(int x, int y) {
        super (x, y, ScreenSettings.TILE_SIZE * 2, ScreenSettings.TILE_SIZE * 2);
        this.addObserver(AudioManager.getInstance());
    }



    @Override
    public void interact(Player player) {
        if (!isOpened) {
            isOpened = true;
            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getBoosted10Card();
                player.getInventory().addItem(pokerCard);
                System.out.println("WOODLOCKER : interact() -> added new pokercard to inventory -> " + pokerCard);
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

}
