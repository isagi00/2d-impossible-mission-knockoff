package view.entityViews;

import model.entities.Dog;
import model.entities.Drone;
import model.levels.LevelManager;
import model.levels.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * view, it contains all the views of all enemies in the {@link model.entities.Player}'s current {@link Room}.
 * gets updated via the observer pattern by {@link LevelManager}.
 */
public class EnemyView implements Observer {
    /**
     * list of all the drone views in the current room.
     */
    private List<DroneView> droneViews = new ArrayList<>();
    /**
     * list of all the dog views in the current room.
     */
    private List<DogView> dogViews = new ArrayList<>();

    /**
     * the current {@link Room} where the {@link model.entities.Player} is
     */
    private Room currentRoom;
    /**
     * reference to the {@link LevelManager} instance
     */
    private final LevelManager levelManager;

    public EnemyView(LevelManager levelManager) {
        this.levelManager = levelManager;
        this.currentRoom = levelManager.getCurrentRoom();
        //initialize the views in the constructor (once)
        initializeViews();
        //load all the enemies sprites
        DogView.loadSprites();
        DroneView.loadSprites();
        //register enemy view as an observer of level manager
        this.levelManager.addObserver(this);
    }

    /**updates the {@link #currentRoom} whenever it changes.
     * see {@link LevelManager}.
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LevelManager levelManager){
            if (arg.equals("level changed")){           //when changin rooms, in level manager notifies observers with getCurrentRoom()
                this.currentRoom = levelManager.getCurrentRoom();             //update the current room
                refreshViews();
            }
        }
    }


    /**
     * clears and re-populates the {@link #droneViews} and the {@link #dogViews} array lists with new {@link DogView} or {@link DroneView},
     * by checking the {@link Room}'s enemies.
     */
    private void initializeViews() {
        droneViews.clear();
        dogViews.clear();

        for( Drone drone : currentRoom.getDrones()) {
            droneViews.add(new DroneView(drone));
        }
        for(Dog dog : currentRoom.getDogs() ) {
            dogViews.add(new DogView(dog));
        }

        System.out.println("[EnemyView][initializeViews()] initializing enemy views");
    }

    /**
     * refreshed the enemy views.
     * clears all existing views in {@link #droneViews} and {@link #dogViews} and re-initializes them based on the current room.
     */
    private void refreshViews() {
        droneViews.clear();
        dogViews.clear();

        initializeViews();
        System.out.println("[EnemyView] -> refreshViews(): refreshing enemy views");
    }


    /**calls every single view's .draw() method.
     * the view instances are stored in {@link #droneViews}, {@link #dogViews}
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    //get the list of the objects in the current room
    public void drawEnemies(Graphics2D g2d) {
        for (DroneView droneView : droneViews) {
            droneView.draw(g2d);
        }
        for (DogView dogView : dogViews) {
            dogView.draw(g2d);
        }

    }


}
