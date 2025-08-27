package model.interactableObjects;


import model.entities.Player;


import java.util.Observable;

//interactable objects such as chests, computers(to disable the enemies), etc... extend this abstract class.
public abstract class InteractableObject extends Observable {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    private int interactionProgress;
    private boolean interactionCompleted;
    private int SEARCH_COMPLETE_TIME = 90;
    private boolean isInteractable = true;

    public InteractableObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void open(Player player);


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
    public boolean getIsInteractable() { return isInteractable; }
    public int getSearchCompleteTime() { return SEARCH_COMPLETE_TIME; }

    public void setInteractionCompleted(boolean interactionCompleted) { this.interactionCompleted = interactionCompleted; }




}
