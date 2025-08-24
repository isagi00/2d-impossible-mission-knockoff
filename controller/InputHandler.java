package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * controller.
 * this particularly implements keyboard input processing for the model to use
 * should not render anything
 * should not store persistent game state
 */
public class InputHandler implements KeyListener {
    //player movement
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean upPressed;
    public boolean downPressed;
    public boolean spacePressed;
    //player interaction
    public boolean ePressed;    //interact with object
    //player menu navigation
    public boolean enterPressed;
    public boolean upArrowPressed;
    public boolean downArrowPressed;
    //player inventory discard
    public boolean onePressed;
    public boolean twoPressed;
    public boolean threePressed;
    public boolean fourPressed;
    public boolean fivePressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

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
            System.out.println("[InputHandler][keyPressed()] one pressed");
        }
        if (code == KeyEvent.VK_2){
            twoPressed = true;
            System.out.println("[InputHandler][keyPressed()] two pressed");
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
