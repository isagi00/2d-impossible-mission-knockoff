package model.interactableObjects;

import model.ScreenSettings;
import model.entities.Player;
import model.inventoryrelated.BoBo;
import model.inventoryrelated.PokerCard;
import view.GameView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WoodLocker extends InteractableObject {

    private BufferedImage lockerImage;
    public boolean isOpened = false;

    public WoodLocker(int x, int y) {
        super (x, y, ScreenSettings.TILE_SIZE * 2, ScreenSettings.TILE_SIZE * 2);

    }



    @Override
    public void interact(Player player) {
        if (!isOpened) {
            isOpened = true;
            if (!player.getInventory().isInventoryFull()){
                PokerCard pokerCard = PokerCard.getRandomPokerCard();
                player.getInventory().addItem(pokerCard);
                System.out.println("paperbor : interact() -> added new pokercard to inventory -> " + pokerCard);
            }
            System.out.println("Wood locker opened");
        }
    }

}
