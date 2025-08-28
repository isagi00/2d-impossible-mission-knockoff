package model.interactableObjects;

import model.entities.Player;
import model.inventoryrelated.PokerCard;
import model.ScreenSettings;
import view.AudioManager;

/**
 * metal locker interactable model.
 */
public class MetalLocker extends InteractableObject {
    /**
     * indicates if this instance of the metal locker is opened or not
     */
    private boolean isOpened = false;

    /**
     * @param x x coordinate of the metal locker    (top left)
     * @param y y coordinate of the metal locker
     */
    public MetalLocker(int x, int y){
        super(x,y,ScreenSettings.TILE_SIZE * 2,ScreenSettings.TILE_SIZE * 2);

        this.addObserver(AudioManager.getInstance());
    }

    /**opens the current metal locker (sets the {@link #isOpened} boolean flag to true.
     * once opened, the player has a boosted chance of get a face card(J, Q, K)
     * it notifies the {@link AudioManager} to play a sound.
     * @param player current instance of the player that is trying to interact with the object
     */
    @Override
    public void open(Player player) {
        if (!isOpened) {
            isOpened = true;

            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getBoostedFacePokerCard();      //boosted rates to get a facecard
                player.getInventory().addItem(pokerCard);
                System.out.println("[Metallocker] open() -> added new pokercard to inventory -> " + pokerCard);

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
//            System.out.println("[MetalLocker] metal locker is opened");
        }
    }
}
