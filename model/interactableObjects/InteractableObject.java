package model.interactableObjects;


import model.entities.Player;


import java.util.Observable;

/**
 * abstract class, model, interactable objects such as chests, computers(to disable the enemies), etc... extend this abstract class.
 */
public abstract class InteractableObject extends Observable {
    /**
     * interactable object x coordinate
     */
    protected int x;
    /**
     * interactable object y coordinate
     */
    protected int y;
    /**
     * interactable object width
     */
    protected int width;
    /**
     * interactable object height
     */
    protected int height;

    /**
     * interactable object interaction (search) progress
     */
    private int interactionProgress;
    /**
     * flag that indicates if the interactable object search is completed or not
     */
    private boolean interactionCompleted;
    /**
     * time to complete the interaction (search) of the interactable object
     */
    private int SEARCH_COMPLETE_TIME = 90;
    /**
     * indicates if the interactable object is searchable (interactable) or not
     */
    private boolean isInteractable = true;

    /**
     * @param x x coordinate of the interactable object
     * @param y y coordinate of the interactable object
     * @param width width of the interactable object
     * @param height height of the interactable object
     */
    public InteractableObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**opens the interactable object
     * @param player current instance of the player that is trying to interact with the object
     */
    public abstract void open(Player player);


    /**
     * starts the interactable object interaction (search). if the player stops pressing down e, the interaction (search) progress gets reset to 0.
     * @param ePressed indicates if the 'e' is pressed or not
     */
    public void startInteraction(boolean ePressed){
        if (ePressed){
            if(!interactionCompleted){
                interactionProgress++;
                if(interactionProgress >= SEARCH_COMPLETE_TIME){
                    interactionCompleted = true;
                    isInteractable = false;
                }
            }
        }
        else{
            if(!interactionCompleted){
                interactionProgress = 0;
            }
        }
    }




    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getInteractionProgress() { return interactionProgress; }
    public boolean isInteractionCompleted() { return interactionCompleted; }
    public int getSearchCompleteTime() { return SEARCH_COMPLETE_TIME; }

    public void setInteractionCompleted(boolean interactionCompleted) { this.interactionCompleted = interactionCompleted; }




}
