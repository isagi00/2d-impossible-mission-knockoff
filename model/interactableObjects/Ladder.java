package model.interactableObjects;

import model.ScreenSettings;
import model.entities.Player;


/**
 * ladder model, it is considered an {@link InteractableObject} so that it is easier to implement in the .txt based level editor.
 */
public class Ladder extends InteractableObject {


    /**
     * @param x x coordinate of the ladder (top left)
     * @param y y coordinate of the ladder  (top left)
     */
    public Ladder(int x, int y){
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);
    }


    /**doesn't really have a 'open' function, a ladder cannot be opened
     * @param player current instance of the player that is trying to interact with the object
     */
    @Override
    public void open(Player player) {
        //dont have a one time interaction like other objects
    }

}
