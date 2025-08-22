package view.itemViews;

import model.inventoryrelated.*;
import model.ScreenSettings;

import view.itemViews.ItemView;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;


public class InventoryView implements Observer {

    BufferedImage inventorySlot;
    private int slotWidth = (int)(ScreenSettings.TILE_SIZE * 1.5);
    private int slotHeight = (int) (ScreenSettings.TILE_SIZE * 1.5);

    private ItemView[] itemViews;

    Inventory inventory;



    public InventoryView(Inventory inventory) {
        this.inventory = inventory;
        this.itemViews = new ItemView[inventory.getCapacity()];  //set the max capacity to the inventory capacity

        for(int i = 0; i < inventory.getCapacity(); i++){
            itemViews[i] = null;    //fill the invetory with nulls
        }

        loadSprites();
        updateItemViews();

        inventory.addObserver(this);
    }



    private void loadSprites() {
        try{
            inventorySlot = ImageIO.read(getClass().getClassLoader().getResourceAsStream("inventory/inventorySlot.png"));
//            System.out.println("loaded inventory slot(s)");
        }catch(Exception e){
//            System.out.println("failed to load inventory slot(s)");
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        int totalInventoryWidth = inventory.getCapacity() * slotWidth;
        int startX = (ScreenSettings.SCREEN_WIDTH - totalInventoryWidth) / 2;
        int startY = ScreenSettings.SCREEN_HEIGHT - slotHeight - 20;
        //draw slots and items

        for (int i = 0; i < inventory.getCapacity(); i++) {
            int x = startX + i * slotWidth;

            //draw slot backgrounds
            g2d.drawImage(inventorySlot, x, startY, slotWidth,slotHeight, null);


            // draw items
            ItemView itemView = itemViews[i];
            if(itemView != null){       //is not empty
                if(itemView instanceof PokerCardsView pcv){
                    pcv.draw(g2d, x + 15, startY + 10, slotWidth - 30, slotHeight - 20);
//                    System.out.println("Inventoryview -> draw() : drawing poker cards view");
                }
                else {
                    itemView.draw(g2d, x, startY, slotWidth, slotHeight);      //draw the item
//                    System.out.println("Inventoryview -> draw() : drawing other views");
                }
            }
        }


    }

    //rebuild the items views when the inventory changes
    public void updateItemViews(){
//        System.out.println("updateItemViews(): inventory capacity: " + inventory.getCapacity());

        for (int i = 0; i < inventory.getCapacity(); i++) {
//            System.out.println("updateItemViews(): slot " + i + "; item " + itemViews[i]);
            Item item = inventory.getItem(i);

            if( item instanceof ComputerCard computerCard){
                itemViews[i] =  new ComputerCardView(computerCard);   //add the view corresponding to the inventory slot
            }

            else if (item instanceof BoBo bobo){
                itemViews[i] = new BoboView(bobo);
            }

            else if (item instanceof PokerCard pokerCard){
                itemViews[i] = new PokerCardsView(pokerCard);
            }

            //OTHER ITEMS GO HERE

            else{
                itemViews[i] = null; //clear the slot
            }
        }


    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == inventory) {
            updateItemViews();
        }
    }
}
