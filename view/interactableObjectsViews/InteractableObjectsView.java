package view.interactableObjectsViews;

import model.interactableObjects.*;
import model.levels.LevelManager;
import model.levels.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class InteractableObjectsView implements Observer {
    LevelManager levelManager;


    private List<PaperBoxView> paperBoxViews = new ArrayList<>();
    private List<CardView> cardViews = new ArrayList<>();   // array
    private List<ComputerView> computerViews = new ArrayList<>();
    private List<LadderView> ladderViews = new ArrayList<>();
    private List<MetalLockerView> metalLockerViews = new ArrayList<>();
    private List<RedBoxView> redBoxViews = new ArrayList<>();
    private List<WoodLockerView> woodLockerViews = new ArrayList<>();


    private Room currentRoom;

    public InteractableObjectsView(LevelManager levelManager) {
        this.levelManager = levelManager;
        this.currentRoom = levelManager.getCurrentRoom();

        levelManager.addObserver(this);
        initializeViews();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LevelManager){
            if (arg == "level changed"){           //when changin rooms, in level manager notifies observers with getCurrentRoom()
                this.currentRoom = levelManager.getCurrentRoom();             //update the current room
                refreshViews();
            }
        }

    }




    private void initializeViews() {
        paperBoxViews.clear();
        cardViews.clear();
        computerViews.clear();
        ladderViews.clear();
        metalLockerViews.clear();
        redBoxViews.clear();
        woodLockerViews.clear();


        for( InteractableObject obj : levelManager.getCurrentRoom().getInteractiveObjects()) {

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
        System.out.println("InteractiveObjectsView -> initializeViews(): initializing views");
    }

    //refresh the object views if the room changes
    private void refreshViews() {
        paperBoxViews.clear();
        cardViews.clear();
        computerViews.clear();
        ladderViews.clear();
        redBoxViews.clear();
        woodLockerViews.clear();


        initializeViews();
        System.out.println("InteractiveObjectsView -> refreshViews(): refreshing views");
    }

    //get the list of the objects in the current room
    public void drawInteractableObjects(Graphics2D g2d) {
        for (PaperBoxView paperBoxView : paperBoxViews){
            paperBoxView.draw(g2d);
        }

        for (CardView cardView : cardViews){
            cardView.draw(g2d);
            cardView.update();
        }

        for (LadderView ladderView : ladderViews){
            ladderView.draw(g2d);
        }

        for (MetalLockerView metalLockerView : metalLockerViews){
            metalLockerView.draw(g2d);
        }

        for (RedBoxView redBoxView : redBoxViews){
            redBoxView.draw(g2d);
            redBoxView.update();
        }

        for (WoodLockerView woodLockerView : woodLockerViews){
            woodLockerView.draw(g2d);
        }

        for (ComputerView computerView : computerViews){
            computerView.draw(g2d);
            computerView.update();
        }


    }

}
