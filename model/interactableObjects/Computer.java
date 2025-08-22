package model.interactableObjects;

import model.CustomObservable;
import model.CustomObserver;
import model.entities.Player;
import model.ScreenSettings;
import model.inventoryrelated.ComputerCard;
import model.inventoryrelated.Inventory;
import model.inventoryrelated.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Computer extends InteractableObject{

    private boolean isBeingAccessed = false;
    private boolean isMenuVisible = false;
    private String[] menuOptions = {
            "deactivate enemies",
            "..."
    };

    private int selectedMenuOption = 0;

    public Computer(int x, int y) {
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE * 2);

    }



    @Override
    public void interact(Player player) {
       System.out.println("Computer -> interact(player) : player accessing computer ");
       isBeingAccessed = true;
       isMenuVisible = true;
       selectedMenuOption = 0;
    }


    public void navigateMenuUp(){
        if(isMenuVisible && menuOptions.length > 0){
            selectedMenuOption = (selectedMenuOption - 1) % menuOptions.length;
        }
    }

    public void navigateMenuDown(){
        if(isMenuVisible){
            selectedMenuOption = (selectedMenuOption + 1) % menuOptions.length;
        }
    }

    public void selectMenuOption(Player player){
        if (!isMenuVisible) return;
        switch (selectedMenuOption) {
            case 0:
                System.out.println("Computer -> selectMenuOption(Player): selected menu option 0: deactivate enemies");
                player.getLevelManager().getCurrentRoom().deactivateAllEnemies();
                break;
            case 1:
                System.out.println("Computer -> selectMenuOption(Player): selected menu option 1: other ....");
//                player.getLevelManager().getCurrentRoom()...
        }
    }

    public void cancelMenu(){
        isMenuVisible = false;
        isBeingAccessed = false;
    }



    //getters
    public boolean getIsBeingAccessed(){
        return isBeingAccessed;
    }

    public boolean getIsMenuVisible(){
        return isMenuVisible;
    }

    public int getSelectedMenuOption(){
        return selectedMenuOption;
    }

    public String[] getMenuOptions(){
        return menuOptions;
    }


}
