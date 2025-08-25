package view.entityViews;

import model.entities.Dog;
import model.entities.Drone;
import model.entities.Entity;
import model.levels.LevelManager;
import model.levels.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class EnemyView implements Observer {

    private List<DroneView> droneViews = new ArrayList<>();
    private List<DogView> dogViews = new ArrayList<>();

    private Room currentRoom;
    private LevelManager levelManager;

    public EnemyView(LevelManager levelManager) {
        this.levelManager = levelManager;
        this.currentRoom = levelManager.getCurrentRoom();
        initializeViews();

        this.levelManager.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LevelManager levelManager){
            if (arg instanceof Room newRoom){           //when changin rooms, in level manager notifies observers with getCurrentRoom()
                this.currentRoom = newRoom;             //update the current room
                refreshViews();
            }
        }
    }




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

    private void refreshViews() {
        droneViews.clear();
        dogViews.clear();

        initializeViews();
        System.out.println("EnemyView -> refreshViews(): refreshing enemy views");
    }


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
