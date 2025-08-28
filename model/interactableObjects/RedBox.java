package model.interactableObjects;

import model.ScreenSettings;
import model.entities.Player;
import model.inventoryrelated.PokerCard;
import view.AudioManager;

/**
 * red box interactable object model.
 */
public class RedBox extends InteractableObject {

    /**
     * indicates if this instance of red box is open or not
     */
    private boolean isOpened = false;


    /**
     * @param x x coordinate of the red box  (top left)
     * @param y y coordinate of the red box (top left)
     */
    public RedBox(int x, int y) {
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);

        this.addObserver(AudioManager.getInstance());
    }

    /**once the red box is opened, the player gets a boosted chance to get an ace card.
     * it notifies the {@link AudioManager} to play a sound.
     * @param player current instance of the player that is trying to interact with the object
     */
    @Override
    public void open(Player player) {   //called once the player completes the interaction
        if(!isOpened){
            isOpened = true;
            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getBoostedAcePokerCard();        //higher chance to get aces
                player.getInventory().addItem(pokerCard);
//                System.out.println("[PaperBox] : open() -> added new pokercard to inventory -> " + pokerCard);
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

    /**
     * @return boolean flag {@link #isOpened}
     */
    public boolean getIsOpened(){
        return isOpened;
    }


}
