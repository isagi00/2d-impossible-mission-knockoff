package model.interactableObjects;

import model.entities.Player;
import model.ScreenSettings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Ladder extends InteractableObject {


    public Ladder(int x, int y){
        super(x, y, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE);
    }


    @Override
    public void open(Player player) {
        //dont have a one time interaction like other objects
    }

}
