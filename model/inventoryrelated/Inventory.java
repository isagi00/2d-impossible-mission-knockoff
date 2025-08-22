package model.inventoryrelated;


import model.ScoreTracker;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import static model.inventoryrelated.PokerCard.*;

public class Inventory extends Observable {
    private final Item[] items;
    private final int capacity;

    public Inventory(int capacity) {
        this.capacity = capacity;
        items = new Item[capacity];

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


    public void removeItem(Item item) {
        for (int i = 0; i < capacity; i++) {
            if (items[i] == item) {
                items[i] = null;
                setChanged();
                notifyObservers();
                return;
            }
        }
    }

    public void removeComputerCard(ComputerCard computerCard) {
        for (int i = 0; i < capacity; i++) {
            if (items[i] == computerCard) {
                items[i] = null;
                setChanged();
                notifyObservers();
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

    public boolean isStateDirty(){
        return hasChanged();
    }

    public void clearDirtyState(){
        clearChanged();
    }




}
