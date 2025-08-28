package model.interactableObjects;

import model.ScreenSettings;
import model.entities.Player;
import model.inventoryrelated.PokerCard;
import view.AudioManager;
/**
 * wood locker interactable object model.
 */
public class WoodLocker extends InteractableObject {
    /**
     * indicates if this instance of wood locker is open or not
     */
    public boolean isOpened = false;

    /**
     * @param x x coordinate of the wood locker (top left)
     * @param y y coordinate of the wood locker (top left)
     */
    public WoodLocker(int x, int y) {
        super (x, y, ScreenSettings.TILE_SIZE * 2, ScreenSettings.TILE_SIZE * 2);
        this.addObserver(AudioManager.getInstance());
    }


    /**once the wood locker is opened, the player gets a boosted chance to get a 10 card.
     * it notifies the {@link AudioManager} to play a sound.
     * @param player current instance of the player that is trying to interact with the object
     */
    @Override
    public void open(Player player) {
        if (!isOpened) {
            isOpened = true;
            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getBoosted10Card();
                player.getInventory().addItem(pokerCard);
//                System.out.println("[WoodLocker] : open() -> added new pokercard to inventory -> " + pokerCard);
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
