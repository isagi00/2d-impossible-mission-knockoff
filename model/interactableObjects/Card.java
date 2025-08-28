package model.interactableObjects;

import model.entities.Player;
import model.ScreenSettings;
import model.inventoryrelated.ComputerCard;
import model.inventoryrelated.Inventory;

/**
 * card interactable object used to access {@link Computer} object.
 * if this object is not in the player inventory, the player wont be able to interact with the computer.
 */
public class Card extends InteractableObject {

    /**
     * flag to track if the card is taken or not
     */
    private boolean isTaken = false;


    /**
     * @param x x coordinate of the card
     * @param y y coordinate of the card
     */
    public Card(int x, int y) {
        super(x,y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);
    }


    /**if this method gets called, then it means the player has finished interacting with this object,
     * and gets added into the {@link Player} {@link Inventory}, only if the inventory is not full.
     * @param player current instance of the player that is trying to interact with the object
     */
    @Override
    public void open(Player player) {
        if(player == null) {
//            System.out.println("[Card] open(): player is null");
            return;
        }

        Inventory inventory = player.getInventory();
        if(inventory == null) {
            System.out.println("[Card] open(): inventory is null");
            return;
        }

        if (!inventory.isInventoryFull()){
            inventory.addItem(new ComputerCard());
            isTaken = true;
            System.out.println("[Card] open(): added computer card to inventory");
        }
    }

    /**
     * @return the {@link #isTaken} flag
     */
    public boolean getIsTaken(){
        return isTaken;
    }



}
