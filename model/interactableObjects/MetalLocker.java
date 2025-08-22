package model.interactableObjects;

import model.entities.Player;
import model.inventoryrelated.BoBo;
import model.inventoryrelated.PokerCard;
import view.GameView;
import model.ScreenSettings;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MetalLocker extends InteractableObject {
    public boolean isOpened = false;

    public MetalLocker(int x, int y){
        super(x,y,ScreenSettings.TILE_SIZE * 2,ScreenSettings.TILE_SIZE * 2);
    }

    @Override
    public void interact(Player player) {
        if (!isOpened) {
            isOpened = true;

            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getBoostedFacePokerCard();      //boosted rates to get a facecard
                player.getInventory().addItem(pokerCard);
                System.out.println("metallocker : interact() -> added new pokercard to inventory -> " + pokerCard);
            }
            System.out.println("metal locker is opened");
        }
    }
}
