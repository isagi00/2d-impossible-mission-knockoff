package model.inventoryrelated;

import model.entities.Player;

public abstract class Item {
    private String name;
    private String description;

    public Item() {

    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public void use(Player player) {
        //default implementation does nothing, override with specific items
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
