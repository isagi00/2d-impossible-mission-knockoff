package model.interactableObjects;

import model.entities.Player;
import model.ScreenSettings;

public class Computer extends InteractableObject{

    private boolean isBeingAccessed = false;
    private boolean isMenuVisible = false;
    private String[] menuOptions = {
            "deactivate drones",
            "deactivate dogs"
    };

    private int selectedMenuOption = 0;

    public Computer(int x, int y) {
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE * 2);

    }



    @Override
    public void open(Player player) {
//       System.out.println("[Computer][open(player)] : player accessing computer ");
       isBeingAccessed = true;
       isMenuVisible = true;
       selectedMenuOption = 0;
    }


    public void navigateMenuUp(){
        if(isMenuVisible && selectedMenuOption > 0){
            selectedMenuOption = (selectedMenuOption - 1) % menuOptions.length;
        }
    }

    public void navigateMenuDown(){
        if(isMenuVisible && selectedMenuOption < menuOptions.length - 1){
            selectedMenuOption = (selectedMenuOption + 1) % menuOptions.length;
        }
    }

    public void selectMenuOption(Player player){
        if (!isMenuVisible) return;
        switch (selectedMenuOption) {
            case 0:
                System.out.println("[Computer][selectMenuOption(Player)]: selected menu option 0: deactivate drones");
                player.getLevelManager().getCurrentRoom().deactivateAllDrones();
                break;
            case 1:
                System.out.println("[Computer][selectMenuOption(Player)]: selected menu option 1: deactivate dogs");
                player.getLevelManager().getCurrentRoom().deactivateAllDogs();
                break;
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
