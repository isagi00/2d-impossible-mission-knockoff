package model.levels;

import model.entities.Dog;
import model.entities.Drone;
import model.entities.Entity;
import model.interactableObjects.InteractableObject;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * model.
 * should store specific room data
 * should implement room rules (dont think there are any)
 * provides room accessors
 *
 * should not know about view or keyboard input
 */
public class Room {
    //room types
    public enum RoomType {
        TUTORIAL, LEVEL, ELEVATOR, EXTRACTION, GROUND
    }

    public RoomType roomType;
    //room specific flags
    public int levelIndex;  //identifies the current room index
    public boolean isOpen;
    public boolean isVisited;
    private List<InteractableObject> interactableObjects;

    //ENEMIES IN THE ROOM
    private List<Drone> drones = new ArrayList<>();
    private List<Dog> dogs = new ArrayList<>();

    //tutorial text in the room
    private String tutorialText;



    public Room(RoomType roomType, int levelIndex, boolean isOpen) {
        this.roomType = roomType;
        this.levelIndex = levelIndex;
        this.isOpen = isOpen;
        this.isVisited = false;     //default false

        this.interactableObjects = new ArrayList<>();
    }


    public String getRoomPath(){
        switch (roomType) {
            case TUTORIAL:
                return "levels/tutorial" + levelIndex + ".txt";
            case LEVEL:
                return "levels/level" + levelIndex  + ".txt";
            case ELEVATOR:
                return "levels/elevator" + levelIndex + ".txt";
            case EXTRACTION:
                return "levels/extraction.txt";
            case GROUND:
                return "levels/ground" + levelIndex + ".txt";
            default:
                return "levels/empty.txt";

        }
    }

    @Override
    public String toString() {
        String roomSymbol;
        switch (roomType) {
            case TUTORIAL:
                roomSymbol = "T" + (levelIndex + 1);
                break;

            case LEVEL:
                if (isOpen) {
                    roomSymbol = "L" + levelIndex;
                }
                else {
                    roomSymbol = "X";
                }
                break;
            case ELEVATOR:
                roomSymbol = "EL" + levelIndex;
                break;
            case EXTRACTION:
                roomSymbol = "EX" + levelIndex;
                break;
            case GROUND:
                roomSymbol = "GR" + levelIndex;
                break;
            default:
                roomSymbol = "undefined room";
        }
        return roomSymbol;
    }





    public void deactivateAllEnemies() {
        for(Drone drone: drones) {
            drone.deactivate();
        }

        for (Dog dog: dogs) {
            dog.deactivate();
        }
    }







    public void addInteractableObject(InteractableObject interactableObject) {
        interactableObjects.add(interactableObject);
//        System.out.println(interactableObjects);
    }

    public void clearInteractableObjects() {
        interactableObjects.clear();
    }


    public void addDrone(Drone drone){
        drones.add(drone);
    }
    public void clearDrones(){
        drones.clear();
    }
    public List<Drone> getDrones() {
        return drones;
    }

    public void addDog(Dog dog){dogs.add(dog);}
    public void clearDogs(){dogs.clear();}
    public List<Dog> getDogs() {return dogs;}


    //GETTERS

    public List<InteractableObject> getInteractiveObjects() {
        return interactableObjects;
    }


    public int getLevelIndex(){
        return levelIndex;
    }

    public String getRoomType(){
        return roomType.toString();
    }


    public String getTutorialText(){return tutorialText;}



    public void setTutorialText(String tutorialText){this.tutorialText = tutorialText;}



}
