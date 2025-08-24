package model.inventoryrelated;


import model.ScoreTracker;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import static model.inventoryrelated.PokerCard.*;

public class Inventory extends Observable {
    private final Item[] items;
    private final int capacity;

    //discard progress tracker
    private int[] discardProgress;      //progress for each slot, storing as an array allows multiple cards to be discarded
    private boolean[] discardCompleted;     //completion for each slot
    private int DISCARD_COMPLETION_TIME = 60;   //frames needed to complete discard

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
        notifyObservers();
    }

    public boolean isInventoryFull(){
        for (Item item : items) {
            if (item == null){
                return false;
            }
        }
        return true;
    }


    public void removeItem(int index) {
        if (index < 0 || index >= capacity) {
            return;
        }
        items[index] = null;

        discardProgress[index] = 0;
        discardCompleted[index] = false;

        setChanged();
        notifyObservers();
        clearChanged();
    }

    public void removeComputerCard(ComputerCard computerCard) {
        for (int i = 0; i < capacity; i++) {
            if (items[i] == computerCard) {
                items[i] = null;

                setChanged();
                notifyObservers();
                clearChanged();
                break;
            }
        }
    }

    public ComputerCard getComputerCard() {
        for (int i = 0; i < capacity; i++) {
            if (items[i] instanceof ComputerCard card) {
                return card;
            }
        }
        return null;
    }

    public void updateDiscardProgress(int inventorySlotIndex, boolean keyHeld) {
        if (inventorySlotIndex < 0 || inventorySlotIndex >= capacity) return;
        if (items[inventorySlotIndex] == null) return;  //if the current inventory slot doesn't have anything return

        if(keyHeld){
//            System.out.println("[Inventory][updateDiscardProgress()] Key held:" + keyHeld);
            if (!discardCompleted[inventorySlotIndex]){
                discardProgress[inventorySlotIndex]++;
//                System.out.println("[Inventory][updateDiscardProgress()] Discard progress: " + Arrays.toString(discardProgress));
                if(discardProgress[inventorySlotIndex] >= DISCARD_COMPLETION_TIME){
                    discardCompleted[inventorySlotIndex] = true;
                    removeItem(inventorySlotIndex);

//                    setChanged();
//                    notifyObservers();
                }
            }
        }
        else{   //reset discard progress if the key held is released
//            System.out.println("[Inventory][updateDiscardProgress()] Key held:" + keyHeld);
            if (discardProgress[inventorySlotIndex] > 0 && !discardCompleted[inventorySlotIndex]){
                discardProgress[inventorySlotIndex] = 0;
//                System.out.println("[updateDiscardProgress()] resetting discard progress: " + Arrays.toString(discardProgress));
//                setChanged();
//                notifyObservers();
            }
        }
    }

    //GETTERS
    public Inventory getInventory() {
        return this;
    }

    public Item[] getItems(){
        return Arrays.copyOf(items, capacity);
    }

    public int getCapacity() {
        return capacity;
    }

    public Item getItem(int index) {
        return items[index];
    }

    public float getDiscardProgress(int inventorySlotIndex) {
        if (inventorySlotIndex < 0 || inventorySlotIndex >= capacity) return 0;
        if (items[inventorySlotIndex] == null) return 0;
//        System.out.println("[Inventory][getDiscardProgress()] Discard progress: " + Arrays.toString(discardProgress));
        return (float) discardProgress[inventorySlotIndex] / DISCARD_COMPLETION_TIME;
    }

    public boolean isDiscardInProgress(int inventorySlotIndex) {
        if (inventorySlotIndex < 0 || inventorySlotIndex >= capacity) return false;
        if (items[inventorySlotIndex] == null) return false;
        return discardProgress[inventorySlotIndex] > 0 &&!discardCompleted[inventorySlotIndex];
    }



    public boolean isStateDirty(){
        return hasChanged();
    }

    public void clearDirtyState(){
        clearChanged();
    }




}
