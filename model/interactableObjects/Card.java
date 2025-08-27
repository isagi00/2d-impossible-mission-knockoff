package model.interactableObjects;

import model.entities.Player;
import model.ScreenSettings;
import model.inventoryrelated.ComputerCard;
import model.inventoryrelated.Inventory;

public class Card extends InteractableObject {

    private boolean isTaken = false;


    public Card(int x, int y) {
        super(x,y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);

    }


    @Override
    public void open(Player player) {
        //safety checks
        if(player == null) {
            System.out.println("Card open(): player is null || card is taken");
            return;
        }

        Inventory inventory = player.getInventory();
        if(inventory == null) {
            System.out.println("Card open(): inventory is null");
            return;
        }

        if (!inventory.isInventoryFull()){
            inventory.addItem(new ComputerCard());
            isTaken = true;
            System.out.println("Card open(): added computer card to inventory");
        }
    }

    public boolean getIsTaken(){
        return isTaken;
    }



}
