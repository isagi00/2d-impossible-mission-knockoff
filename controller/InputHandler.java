package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * controller, handles keyboard input processing for the {@link model.entities.Player } and menu navigation.
 */
public class InputHandler implements KeyListener {
    //player movement
    /**
     * flag that indicates if the 'a' key is being pressed.
     * mainly used to move the player to the left
     */
    public boolean leftPressed;
    /**
     * flag that indicates if the 'd' key is being pressed.
     * mainly used to move the player to the right
     */
    public boolean rightPressed;
    /**
     * flag that indicates if the 'w' key is being pressed.
     * mainly used to move the player up a ladder
     */
    public boolean upPressed;
    /**
     * flag that indicates if the 's' key is being pressed.
     * mainly used to move the player down a ladder
     */
    public boolean downPressed;
    /**
     * flag that indicates if the 'space' key is being pressed.
     * mainly used to initiate the player's jump
     */
    public boolean spacePressed;
    /**
     * flag that indicates if the 'e' is being pressed.
     * mainly used for the player's object interaction
     */
    //player interaction
    public boolean ePressed;    //open with object
    //player menu navigation
    public boolean enterPressed;
    /**
     * flag that indicates if the up arrow is being pressed.
     * mainly used for menu navigation.
     */
    public boolean upArrowPressed;
    /**
     * flag that indicates if the down arrow is being pressed.
     * mainly used for menu navigation.
     */
    public boolean downArrowPressed;
    //player inventory discard
    /**
     * flag that indicates if '1' is pressed on the keyboard.
     * used for discarding the first inventory slot item
     */
    public boolean onePressed;
    /**
     * flag that indicates if '2' is pressed on the keyboard.
     * used for discarding the second inventory slot item
     */
    public boolean twoPressed;
    /**flag that indicates if '3' is pressed on the keyboard.
     * used for  discarding the third inventory slot item
     */
    public boolean threePressed;
    /**flag that indicates if '4' is pressed on the keyboard.
     * used for  discarding the fourth inventory slot item
     */
    public boolean fourPressed;
    /**flag that indicates if '5' is pressed on the keyboard.
     * used for discarding the fifth inventory slot item
     */
    public boolean fivePressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * processes each key press, sets each respective flag to true.
     * ex. 'a' is pressed, then the {@link #leftPressed} flag is set to true.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }

        if (code == KeyEvent.VK_E) {
            ePressed = true;
        }
        // menu navigation
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_UP) {
            upArrowPressed = true;
//            System.out.println("up arrow pressed");
        }
        if (code == KeyEvent.VK_DOWN) {
            downArrowPressed = true;
//            System.out.println("down arrow pressed");
        }

        if (code == KeyEvent.VK_1){
            onePressed = true;
//            System.out.println("[InputHandler][keyPressed()] one pressed");
        }
        if (code == KeyEvent.VK_2){
            twoPressed = true;
//            System.out.println("[InputHandler][keyPressed()] two pressed");
        }
        if (code == KeyEvent.VK_3){
            threePressed = true;
        }
        if (code == KeyEvent.VK_4){
            fourPressed = true;
        }
        if (code == KeyEvent.VK_5){
            fivePressed = true;
        }

    }

    /**
     * processes each key release.
     * if a key is release, its flag will get reset to false.
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }

        if (code == KeyEvent.VK_E) {
            ePressed = false;
        }
        //menu navigation
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (code == KeyEvent.VK_UP) {
            upArrowPressed = false;
//            System.out.println("up arrow released");
        }
        if (code == KeyEvent.VK_DOWN) {
            downArrowPressed = false;
//            System.out.println("down arrow released");
        }

        if (code == KeyEvent.VK_1) {
            onePressed = false;
        }
        if (code == KeyEvent.VK_2) {
            twoPressed = false;
        }
        if (code == KeyEvent.VK_3) {
            threePressed = false;
        }
        if (code == KeyEvent.VK_4) {
            fourPressed = false;
        }
        if (code == KeyEvent.VK_5) {
            fivePressed = false;
        }


    }


    /**
     * resets each key press flag to false.
     * it is called in {@link GameKeyListener} to reset all keys when the user pauses the game, to avoid having a key press
     * still active in the pause menu.
     */
    public void resetAllKeys() {
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        spacePressed = false;
        ePressed = false;
        enterPressed = false;
        upArrowPressed = false;
        downArrowPressed = false;
    }

}
