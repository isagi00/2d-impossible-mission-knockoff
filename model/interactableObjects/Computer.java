package model.interactableObjects;

import model.entities.Player;
import model.ScreenSettings;

/**
 * computer interactable object model used to control the enemies ({@link model.entities.Drone}, {@link model.entities.Dog} in the room, just like in the original impossible mission.
 */
public class Computer extends InteractableObject{

    /**
     * flag to check if the computer is being accessed or not
     */
    private boolean isBeingAccessed = false;

    /**
     *flag to check if the computer menu is visible or not.
     * becomes true only if the player finishes to interact with the computer and calls {@link #open(Player)}.
     */
    private boolean isMenuVisible = false;
    /**
     * menu options
     */
    private String[] menuOptions = {
            "deactivate drones",
            "deactivate dogs"
    };

    /**
     * currently selected menu option
     */
    private int selectedMenuOption = 0;

    /**
     * @param x x coordinate of the computer   (top left)
     * @param y y coordinate of the computer
     */
    public Computer(int x, int y) {
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE * 2);
    }


    /**if this method is being called, then it means the player has finished interacting (searching) the computer.
     * sets {@link #isBeingAccessed},{@link #isMenuVisible} to true.
     * sets the {@link #selectedMenuOption} to 0.
     * @param player current instance of the player that is trying to interact with the object
     */
    @Override
    public void open(Player player) {
//       System.out.println("[Computer][open(player)] : player accessing computer ");
       isBeingAccessed = true;
       isMenuVisible = true;
       selectedMenuOption = 0;
    }


    /**
     * can be used only if the computer {@link #isMenuVisible} is true.
     * selects the previous menu option
     */
    public void navigateMenuUp(){
        if(isMenuVisible && selectedMenuOption > 0){
            selectedMenuOption = (selectedMenuOption - 1) % menuOptions.length;
        }
    }

    /**
     * can be used only if the computer {@link #isMenuVisible} is true.
     * selects the next menu option
     */
    public void navigateMenuDown(){
        if(isMenuVisible && selectedMenuOption < menuOptions.length - 1){
            selectedMenuOption = (selectedMenuOption + 1) % menuOptions.length;
        }
    }

    /**selects the current menu option.
     * if option 0 is selected, deactivates all the drones in the room;
     * if option 1 is selected, deactivates all the dogs in the room.
     * @param player player instance
     */
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

    /**
     * resets the boolean flags {@link #isMenuVisible} and {@link #isBeingAccessed} to false
     */
    public void cancelMenu(){
        isMenuVisible = false;
        isBeingAccessed = false;
    }


    /**
     * @return {@link #isBeingAccessed}
     */
    public boolean getIsBeingAccessed(){
        return isBeingAccessed;
    }


    /**
     * @return {@link #isMenuVisible}
     */
    public boolean getIsMenuVisible(){
        return isMenuVisible;
    }

    /**
     * @return {@link #selectedMenuOption}
     */
    public int getSelectedMenuOption(){
        return selectedMenuOption;
    }

    /**
     * @return all the available menu options. {@link #menuOptions}
     * {@code menuOptions}
     */
    public String[] getMenuOptions(){
        return menuOptions;
    }


}
