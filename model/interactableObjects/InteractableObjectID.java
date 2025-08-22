package model.interactableObjects;


//objects are special values (negative numbers), in the .txt files.
public class InteractableObjectID {

    public static final int OBJECT_LADDER = -1;
    public static final int OBJECT_BOX = -2;
    public static final int OBJECT_REDBOX = -3;
    public static final int OBJECT_COMPUTER = -4;
    public static final int OBJECT_CARD = -5;
    public static final int OBJECT_METALLOCKER = -6;
    public static final int OBJECT_WOODLOCKER = -7;

    public static final int OBJECT_SPIKE = -8;


    public static boolean isInteractableObject(int value){
        if (value < 0){
            return true;
        }
        else{
            return false;
        }
    }

}
