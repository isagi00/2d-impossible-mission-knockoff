package model.inventoryrelated;

import model.entities.Player;

/**
 *model, abstract class of an 'Item' that can be collected and put in the {@link Player} 's {@link Inventory}
 */
public abstract class Item {
    /**
     * name of the item
     */
    private String name;

    /**
     * empty constructor for an 'Item'
     */
    public Item() {
    }

    /**
     * @return the name of the item
     */
    public String getName() {
        return name;
    }


    /**
     * @param name sets the item name
     */
    public void setName(String name) {
        this.name = name;
    }




}
