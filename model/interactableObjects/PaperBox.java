package model.interactableObjects;


import model.ScreenSettings;
import model.entities.Player;
import model.inventoryrelated.PokerCard;
import view.AudioManager;





/**
 * paper box interactable object model.
 */
public class PaperBox extends InteractableObject {
    /**
     * indicates if this instance of paper box is open or not
     */
    private boolean isOpened;

    /**
     * @param x x coordinate of the paper box  (top left)
     * @param y y coordinate of the paper box (top left)
     */
    public PaperBox(int x, int y) {
        super(x,y, ScreenSettings.TILE_SIZE,ScreenSettings.TILE_SIZE);   //it is 48x48
        this.isOpened = false;

        this.addObserver(AudioManager.getInstance());
    }

    /**once the paper box is opened, the player gets a random poker card with no boosted rates.
     * it notifies the {@link AudioManager} to play a sound.
     * @param player current instance of the player that is trying to interact with the object
     */
    @Override
    public void open(Player player) {
        if(!isOpened){
            isOpened = true;

            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getRandomPokerCard();
                player.getInventory().addItem(pokerCard);

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

//                System.out.println("[paperbox] : open() -> added new pokercard to inventory -> " + pokerCard);
            }
//            System.out.println("[paperbox] -> paperbox opened");
        }
    }

    /**
     * @return boolean flag {@link #isOpened}
     */
    public boolean getIsOpened() {
        return isOpened;
    }




}
