package model.inventoryrelated;

import java.util.Arrays;
import java.util.Observable;
import static model.inventoryrelated.PokerCard.*;

/**
 * model of player's inventory. it handles the discard process and updates the {@link view.itemViews.InventoryView} accordingly via the
 * observer pattern. base implementation has the inventory size of capacity 5.
 * it has 3 arrays of size {@link #capacity}:
 * -{@link #items} where the Items are stored
 * -{@link #discardProgress} where each inventory slot's discard progress is stored
 * -{@link #discardCompleted} where each inventory slot's discard state is stored
 */
public class Inventory extends Observable {
    /**
     * array that contains the inventory's items.
     */
    private Item[] items;
    /**
     * the inventory's capacity.
     */
    private final int capacity;
    //discard progress tracker
    /**
     * array of size {@link #capacity} that tracks the discard progress of each item
     */
    private int[] discardProgress;      //progress for each slot, storing as an array allows multiple cards to be discarded
    /**
     * array of size {@link #capacity} that tracks which item has completed the discard process
     */
    private boolean[] discardCompleted;     //completion for each slot
    /**
     * frames needed to completed an item discard
     */
    private int DISCARD_COMPLETION_TIME = 60;   //frames needed to complete discard

    /**initializes the {@link #items} array, {@link #discardProgress} array, {@link #discardCompleted} array
     * based on the specified capacity.
     * an inventory can contain {@link Item}s, such as {@link ComputerCard} and {@link PokerCard} items.
     * @param capacity capacity of the inventory
     */
    public Inventory(int capacity) {
        this.capacity = capacity;
        items = new Item[capacity];
        discardProgress = new int[capacity];
        discardCompleted = new boolean[capacity];

        for (int i = 0; i < capacity; i++) {
            items[i] = null;
            discardProgress[i] = 0;
            discardCompleted[i] = false;
        }

//        //for testing:
//        this.addItem(aceheart);
//        this.addItem(twodiamond);
//        this.addItem(threespade);
//        this.addItem(fourclub);
//        this.addItem(fivespade);
    }

    /**adds the specified item into {@link #items}.
     * searches the first available slot, and notifies {@link view.itemViews.InventoryView} to update its view.
     * @param item item to add into {@link #items}
     */
    public void addItem(Item item) {
        //check for an available slot
        int availableSlot = 0;
        while (availableSlot < capacity && items[availableSlot] != null){
            availableSlot++;
        }
        if(availableSlot >= capacity){
            throw new IllegalStateException("inventory is full");
        }
        //add the item to the available slot
        items[availableSlot] = item;

        setChanged();
        notifyObservers("item added to inventory");
        clearChanged();
    }

    /**checks if the inventory is full
     * @return true if inventory is full
     */
    public boolean isInventoryFull(){
        for (Item item : items) {
            if (item == null){
                return false;
            }
        }
        return true;
    }


    /**removes the item from the inventory {@link #items} at the specified index.
     * resets the {@link #discardProgress} of the item once an item is removed from {@link #items},
     * and notifies the {@link view.itemViews.InventoryView} to remove the item's view.
     * @param index index of the item to remove
     */
    public void removeItem(int index) {
        if (index < 0 || index >= capacity) {
            return;
        }
        //remove the item
        items[index] = null;
        //reset the progress for the next item to remove
        discardProgress[index] = 0;
        discardCompleted[index] = false;
        //notify inventory view
        setChanged();
        notifyObservers("removed item from inventory");
        clearChanged();

    }

    /**removes the computer card from the inventory {@link #items} and notifies
     * {@link view.itemViews.InventoryView} of the change.
     * @param computerCard {@link ComputerCard} item
     */
    public void removeComputerCard(ComputerCard computerCard) {
        for (int i = 0; i < capacity; i++) {
            if (items[i] == computerCard) {
                items[i] = null;
                break;
            }
        }
        setChanged();
        notifyObservers("removed computer card from inventory");
        clearChanged();
    }

    /**
     * @return the {@link ComputerCard} item in the inventory if there is one
     */
    public ComputerCard getComputerCard() {
        for (int i = 0; i < capacity; i++) {
            if (items[i] instanceof ComputerCard card) {
                return card;
            }
        }
        return null;
    }

    /**updates the progress of the specified inventory slot if the number key is pressed.
     * if the key is held until {@link #DISCARD_COMPLETION_TIME} finishes, then the item will get discarded.
     * otherwise, the discard progress will reset to 0.
     * @param inventorySlotIndex item slot that is being discarded
     * @param keyHeld discard key held or not
     */
    public void updateDiscardProgress(int inventorySlotIndex, boolean keyHeld) {
        if (inventorySlotIndex < 0 || inventorySlotIndex >= capacity) return;
        if (items[inventorySlotIndex] == null) return;  //if the current inventory slot doesn't have anything return

        if(keyHeld){
//            System.out.println("[Inventory][updateDiscardProgress()] key held:" + keyHeld);
            if (!discardCompleted[inventorySlotIndex]){
                discardProgress[inventorySlotIndex]++;
//                System.out.println("[Inventory][updateDiscardProgress()] discard progress: " + Arrays.toString(discardProgress));
                if(discardProgress[inventorySlotIndex] >= DISCARD_COMPLETION_TIME){
                    discardCompleted[inventorySlotIndex] = true;
                    removeItem(inventorySlotIndex);

                }
            }
        }
        else{   //reset discard progress if the key held is released
//            System.out.println("[Inventory][updateDiscardProgress()] key held:" + keyHeld);
            if (discardProgress[inventorySlotIndex] > 0 && !discardCompleted[inventorySlotIndex]){
                discardProgress[inventorySlotIndex] = 0;

            }
        }
    }

    /**
     * @return this instance of the inventory
     */
    //GETTERS
    public Inventory getInventory() {
        return this;
    }

    /**
     * @return a copy of the inventory's items array
     */
    public Item[] getItems(){
        return Arrays.copyOf(items, capacity);
    }

    /**
     * @return the inventory's capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**returns an item at the specified index
     * @param index index of the item in the inventory
     * @return the Item at the specified inded
     */
    public Item getItem(int index) {
        return items[index];
    }

    /**returns the progress of the discard progress at the specified inventory slot index
     * @param inventorySlotIndex index of inventory slot
     * @return the progress of the discard process
     */
    public float getDiscardProgress(int inventorySlotIndex) {
        if (inventorySlotIndex < 0 || inventorySlotIndex >= capacity) return 0;
        if (items[inventorySlotIndex] == null) return 0;
//        System.out.println("[Inventory][getDiscardProgress()] Discard progress: " + Arrays.toString(discardProgress));
        return (float) discardProgress[inventorySlotIndex] / DISCARD_COMPLETION_TIME;
    }




}
