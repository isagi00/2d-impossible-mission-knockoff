package view.interactableObjectsViews;

import model.interactableObjects.*;
import model.levels.LevelManager;
import model.levels.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/** view , every single interactable object views is collected in here.
 * it is created once in {@link view.gamePanelViews.GameView}, and changes the room via the observer pattern.
 * it is the observer of {@link LevelManager}, so when it gets notified of the room change, it will reload the interactable object views
 * via {@link #refreshViews()}
 */
public class InteractableObjectsView implements Observer {
    /**
     * {@link LevelManager} model instance
     */
    private static LevelManager levelManager;
    /**
     * list of {@link PaperBoxView}s
     */
    private List<PaperBoxView> paperBoxViews;
    /**
     * list of {@link CardView}s
     */
    private List<CardView> cardViews;   // array
    /**
     * list of {@link ComputerView}s
     */
    private List<ComputerView> computerViews;
    /**
     * list of {@link LadderView}s
     */
    private List<LadderView> ladderViews;
    /**
     * list of {@link MetalLockerView}s
     */
    private List<MetalLockerView> metalLockerViews;
    /**
     * list of {@link RedBoxView}s
     */
    private List<RedBoxView> redBoxViews;
    /**
     * list of {@link WoodLockerView}s
     */
    private List<WoodLockerView> woodLockerViews;

    /**
     * the current room the player is in.
     * updated via observe: {@link #update(Observable, Object)}
     */
    private Room currentRoom;

    /**created once in the {@link view.gamePanelViews.GameView}. it is a collection of each
     * {@link InteractableObject} in the {@link #currentRoom}.
     * @param levelManager {@link LevelManager} model
     */
    public InteractableObjectsView(LevelManager levelManager) {
        this.levelManager = levelManager;
        this.currentRoom = levelManager.getCurrentRoom();

        this.paperBoxViews = new ArrayList<>();
        this.cardViews = new ArrayList<>();
        this.computerViews = new ArrayList<>();
        this.ladderViews = new ArrayList<>();
        this.metalLockerViews = new ArrayList<>();
        this.redBoxViews = new ArrayList<>();
        this.woodLockerViews = new ArrayList<>();

        levelManager.addObserver(this);
        initializeViews();
    }

    /**updates the {@link #currentRoom} via the observer pattern, whenever the player enters a new room and
     * changes {@link LevelManager}.
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LevelManager){
            if (arg == "level changed"){           //when changin rooms, in level manager notifies observers with getCurrentRoom()
                this.currentRoom = levelManager.getCurrentRoom();             //update the current room
                refreshViews();
            }
        }
    }


    /**
     * scans the {@link #currentRoom}'s interactive objects, and creates each view.
     */
    private void initializeViews() {
        //clear everything
        paperBoxViews.clear();
        cardViews.clear();
        computerViews.clear();
        ladderViews.clear();
        metalLockerViews.clear();
        redBoxViews.clear();
        woodLockerViews.clear();

        //for each interactable object in the room
        for( InteractableObject obj : currentRoom.getInteractiveObjects()) {
            //create its 'view'
            if (obj instanceof PaperBox paperBox) {
                paperBoxViews.add(new PaperBoxView(paperBox));
            }
            else if (obj instanceof Card card) {
                cardViews.add(new CardView(card));
            }
            else if (obj instanceof Computer computer){
                computerViews.add(new ComputerView(computer));
            }
            else if (obj instanceof Ladder ladder){
                ladderViews.add(new LadderView(ladder));
            }
            else if (obj instanceof MetalLocker metalLocker){
                metalLockerViews.add(new MetalLockerView(metalLocker));
            }
            else if (obj instanceof RedBox redBox){
                redBoxViews.add(new RedBoxView(redBox));
            }
            else if (obj instanceof WoodLocker woodLocker){
                woodLockerViews.add(new WoodLockerView(woodLocker));
            }
        }
        System.out.println("[InteractiveObjectsView] -> initializeViews(): initializing views");
    }

    /**
     * refreshes the interactable object views.
     * clears the object view lists ex.{@link #paperBoxViews}
     * and re-initializes them via {@link #initializeViews()}
     */
    private void refreshViews() {
        paperBoxViews.clear();
        cardViews.clear();
        computerViews.clear();
        ladderViews.clear();
        redBoxViews.clear();
        woodLockerViews.clear();


        initializeViews();
        System.out.println("[InteractiveObjectsView] -> refreshViews(): refreshing views");
    }

    /**draws every single interactable object view
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    public void drawInteractableObjects(Graphics2D g2d) {
        //pp box
        for (PaperBoxView paperBoxView : paperBoxViews){
            paperBoxView.draw(g2d);
        }
        //computer card
        for (CardView cardView : cardViews){
            cardView.draw(g2d);
            cardView.update();
        }
        //ladders
        for (LadderView ladderView : ladderViews){
            ladderView.draw(g2d);
        }
        //metal lockers
        for (MetalLockerView metalLockerView : metalLockerViews){
            metalLockerView.draw(g2d);
        }
        //red boxes
        for (RedBoxView redBoxView : redBoxViews){
            redBoxView.draw(g2d);
            redBoxView.update();
        }
        //wood lockers
        for (WoodLockerView woodLockerView : woodLockerViews){
            woodLockerView.draw(g2d);
        }
        //computers
        for (ComputerView computerView : computerViews){
            computerView.draw(g2d);
            computerView.update();
        }
    }

}
