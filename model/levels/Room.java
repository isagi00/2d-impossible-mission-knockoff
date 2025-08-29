package model.levels;

import model.entities.Dog;
import model.entities.Drone;
import model.interactableObjects.InteractableObject;

import java.util.ArrayList;
import java.util.List;

/**
 * model, contains an inner enum {@link RoomType},s contain utility methods, primarily a class that allows working on enemies
 * in the room.
 */
public class Room {
    /**
     * describes the type of the room
     */
    //room types
    public enum RoomType {
        TUTORIAL, LEVEL, ELEVATOR, EXTRACTION, GROUND
    }

    /**
     * this instance of room's room type.
     */
    private final RoomType roomType;

    /**
     * index of the current room
     */
    private int levelIndex;  //identifies the current room index
    /**
     * room is open or not boolean flag
     */
    protected boolean isOpen;

    /**
     * room is visited or not boolean flag
     */
    protected boolean isVisited;


    /**
     * flag that checks whether this specific instance of the room has been initialized or not
     */
    protected boolean isInitialized;

    /**
     * list of {@link InteractableObject}s in the room
     */
    private final List<InteractableObject> interactableObjects;

    /**
     * list of {@link Drone}s in the current room
     */
    //ENEMIES IN THE ROOM
    private final List<Drone> drones;
    /**
     * list of {@link Dog}s in the current room
     */
    private final List<Dog> dogs;

    /**
     * tutorial text to display if the current room is of {@link RoomType} TUTORIAL.
     */
    //tutorial text in the room
    private String tutorialText;


    /**constructor of a Room instance, also initiates the enemies in the room
     * @param roomType room type
     * @param levelIndex index of the level
     * @param isOpen room is open or not
     */
    public Room(RoomType roomType, int levelIndex, boolean isOpen) {
        this.roomType = roomType;
        this.levelIndex = levelIndex;
        this.isOpen = isOpen;
        this.isVisited = false;     //default false
        this.isInitialized = false;

        this.interactableObjects = new ArrayList<>();
        this.drones = new ArrayList<>();
        this.dogs = new ArrayList<>();
    }


    /**
     * @return room path (as string) based on the room type. ex: if the room is tutorial and its index is 0, then it builds the room path
     * string as "levels/tutorial + levelIndex + ".txt"
     */
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

    /**
     * converts a room into a string. if {@link RoomType} is TUTORIAL, then will return "T" + levelIndex
     * @return room type as string
     */
    @Override
    public String toString() {
        String roomSymbol;
        switch (roomType) {
            case TUTORIAL:
                roomSymbol = "T" + levelIndex;
                break;
            case LEVEL:
                if (isOpen) {
                    roomSymbol = "L" + levelIndex;
                }
                else {
                    roomSymbol = "X";   //if room is closed then display as X
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


    /**
     * used to deactivate all enemies in the room. (unused)
     */
    public void deactivateAllEnemies() {        //used to deactivate all enemies.
        for(Drone drone: drones) {
            drone.deactivate();
        }
        for (Dog dog: dogs) {
            dog.deactivate();
        }
    }

    /**
     * deactivates every {@link Drone} in the room
     */
    public void deactivateAllDrones(){
        for(Drone drone: drones) {
            drone.deactivate();
        }
    }

    /**
     * deactivates every {@link Dog} in the room
     */
    public void deactivateAllDogs(){
        for (Dog dog: dogs) {
            dog.deactivate();
        }
    }


    /**
     * @param interactableObject interactable object to add into {@link interactableObjects}
     */
    public void addInteractableObject(InteractableObject interactableObject) {
        interactableObjects.add(interactableObject);
//        System.out.println("[Room]" + interactableObjects);
    }

    /**
     * clears the interactable objects in the room
     */
    public void clearInteractableObjects() {
        interactableObjects.clear();
    }


    /**adds drone into the room
     * @param drone drone to add
     */
    public void addDrone(Drone drone){
        drones.add(drone);
    }

    /**
     * clears all drones in the room
     */
    public void clearDrones(){
        drones.clear();
    }

    /**
     * @return get the list of {@link #drones}
     */
    public List<Drone> getDrones() {
        return drones;
    }

    /**adds dog to the room
     * @param dog dog to add
     */
    public void addDog(Dog dog){dogs.add(dog);}

    /**
     * clear all dogs in the room
     */
    public void clearDogs(){dogs.clear();}

    /**
     * @return list of {@link #dogs}
     */
    public List<Dog> getDogs() {return dogs;}


    /**
     * @return {@link interactableObjects}
     */
    //GETTERS
    public List<InteractableObject> getInteractiveObjects() {
        return interactableObjects;
    }


    /**
     * @return level index
     */
    public int getLevelIndex(){
        return levelIndex;
    }

    /**
     * @return room type
     */
    public RoomType getRoomType(){
        return roomType;
    }


    /**
     * @return tutorial text
     */
    public String getTutorialText(){return tutorialText;}


    /**
     * @param tutorialText sets the tutorial text
     */
    public void setTutorialText(String tutorialText){this.tutorialText = tutorialText;}

    public void clearAllObjects() {
        interactableObjects.clear();
        drones.clear();
        dogs.clear();
    }

}
