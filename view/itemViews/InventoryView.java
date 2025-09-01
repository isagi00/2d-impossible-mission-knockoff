package view.itemViews;

import model.ScreenSettings;
import model.inventoryrelated.ComputerCard;
import model.inventoryrelated.Inventory;
import model.inventoryrelated.Item;
import model.inventoryrelated.PokerCard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;


/**
 * view of the {@link Inventory} model,  renders the inventory slots and the items contained in it.
 * it gets updated via the observer pattern.
 * whenever the inventory(observable) gets updated and sends a notification, this view (inventory view) gets that information and
 * updates accordingly, via {@link #update(Observable, Object)} and the {@link #updateItemViews()}.
 */
public class InventoryView implements Observer {

    /**
     * the inventory slot sprite
     */
    private static BufferedImage inventorySlotImage;
    /**
     * width of a single inventory width
     */
    private final int slotWidth = (int)(ScreenSettings.TILE_SIZE * 1.5);
    /**
     * height of a single inventory slot
     */
    private final int slotHeight = (int) (ScreenSettings.TILE_SIZE * 1.5);
    /**
     * array of {@link ItemView} views. it contains all the views of the items contained in the {@link Inventory} model.
     */
    private ItemView[] itemViews;

    /**
     * reference to the {@link Inventory}, the observable object. this view displays the data
     * got from the inventory model.
     */
    private final Inventory inventory;
    /**
     * reference to the {@link PokerCardsView} view, used mainly to load all the poker cards sprite once,
     * in the constructor.
     */
    private final PokerCardsView pokerCardsView;


    /**view of the inventory.
     * renders the inventory slots and the items contained in it.
     * it gets updated via the observer pattern.
     * whenever the inventory(observable) gets updated and sends a notification, this view (inventory view) gets that information and
     * updates accordingly, via {@link #update(Observable, Object)} and the {@link #updateItemViews()}.
     * @param inventory {@link Inventory} model
     */
    public InventoryView(Inventory inventory) {
        this.inventory = inventory;
        this.itemViews = new ItemView[inventory.getCapacity()];  //set the max capacity to the inventory capacity

        this.pokerCardsView = new PokerCardsView(); //initialize the poker card sprites

        for(int i = 0; i < inventory.getCapacity(); i++) {  //initialize all the item views to null
            itemViews[i] = null;
        }

        loadSprites();
        updateItemViews();

        inventory.addObserver(this);
    }


    /**
     * loads the inventory's slot image sprite. {@link #inventorySlotImage}
     */
    private void loadSprites() {
        try{
            inventorySlotImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("inventory/inventorySlot.png"));
//            System.out.println("loaded inventory slot(s)");
        }catch(Exception e){
//            System.out.println("failed to load inventory slot(s)");
            e.printStackTrace();
        }
    }

    /**renders the player's inventory's inventory frames({@link #inventorySlotImage}, the {@link ItemView} contained in the {@link #itemViews} array.
     * it handles the drawing of the red transparent progress bar of when an item is starting to get discarded, based on the
     * progress given by the {@link Inventory}.
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void draw(Graphics2D g2d) {
        int totalInventoryWidth = inventory.getCapacity() * slotWidth;
        int startX = (ScreenSettings.SCREEN_WIDTH - totalInventoryWidth) / 2;
        int startY = ScreenSettings.SCREEN_HEIGHT - slotHeight - 20;

        //draw slots and items
        for (int i = 0; i < inventory.getCapacity(); i++) {
            int x = startX + i * slotWidth;
            //draw slot backgrounds
            g2d.drawImage(inventorySlotImage, x, startY, slotWidth,slotHeight, null);

            // draw items
            ItemView itemView = itemViews[i];
            if(itemView != null){       //is not empty
                if(itemView instanceof PokerCardView pokerCardView){
                    pokerCardView.draw(g2d, x + 15, startY + 10, slotWidth - 30, slotHeight - 20);
//                    System.out.println("[InventoryView] drawing pokerCardView");
                }
                else {
                    itemView.draw(g2d, x, startY, slotWidth, slotHeight);      //draw the item
//                    System.out.println("[InventoryView] -> draw() : drawing other views");
                }
            }

            //draw the item discard progress if active
            float discardProgress = inventory.getDiscardProgress(i);
//            System.out.println("[InventoryView][draw()] discard progress: " + progress);
            if (discardProgress > 0) {
                int progressBarHeight = (int) (slotHeight * discardProgress);
                int progressBarY = startY + slotHeight - progressBarHeight;
                //draw semi transparent discard progress bar on the slot
                g2d.setColor(new Color(255, 0, 0, 100));
                g2d.fillRect(x, progressBarY, slotWidth, progressBarHeight);
            }
        }
    }

    /**
     * refreshes the inventory view.
     * checks all the {@link Inventory}'s items and creates the appropriate views for the {@link Item} object.
     * if an item gets added to the inventory, then that item's view (for example, {@link ComputerCardView} gets created and rendered in {@link #draw(Graphics2D)} .
     * if an item gets removed from the inventory, the that item's view gets discarded.
     */
    //rebuild the items views when the inventory changes
    public void updateItemViews(){
//        System.out.println("[InventoryView]updateItemViews(): inventory capacity: " + inventory.getCapacity());
        for (int i = 0; i < inventory.getCapacity(); i++) {
//            System.out.println("[InventoryView]updateItemViews(): slot " + i + "; item " + itemViews[i]);
            Item item = inventory.getItem(i);

            if( item instanceof ComputerCard computerCard){
                itemViews[i] =  new ComputerCardView(computerCard);   //add the view corresponding to the inventory slot
            }
            else if (item instanceof PokerCard pokerCard){
                pokerCardsView.setCurrentSprite(pokerCard);     //I am the goat. (fixed major performance issue with pokercardsview)
                BufferedImage currentPokerCardSprite = pokerCardsView.getCurrentSprite();
                itemViews[i] = new PokerCardView(currentPokerCardSprite);
            }
            //OTHER ITEMS GO HERE
            else{
                itemViews[i] = null; //clear the slot
            }
        }
    }

    /**updates the inventory view based on the {@link Inventory} notifications, refreshes the item views via {@link #updateItemViews()} method.
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Inventory) {
            switch((String) arg){
                case "item added to inventory":
                    updateItemViews();
//                    System.out.println("[InventoryView]item added to inventory: " + inventory.getItems());
                    break;
                case "removed item from inventory":
                    updateItemViews();
                    break;
                case "removed computer card from inventory":
                    updateItemViews();
                    break;

            }
        }
    }
}
