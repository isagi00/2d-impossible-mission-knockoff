package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * controller.
 * this particularly implements keyboard input processing for the model to use
 * should not render anything
 * should not store persistent game state
 */
public class KeyHandler implements KeyListener {

    public boolean leftPressed;
    public boolean rightPressed;
    public boolean upPressed;
    public boolean downPressed;
    public boolean spacePressed;
    public boolean hPressed;    //toggle tiles hitboxes
    public boolean ePressed;    //interact with object

    public boolean enterPressed;
    public boolean upArrowPressed;
    public boolean downArrowPressed;

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
        if (code == KeyEvent.VK_H) {
            hPressed = true;
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
        if (code == KeyEvent.VK_H) {
            hPressed = false;
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

    }




    public void resetAllKeys() {
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        spacePressed = false;
        hPressed = false;
        ePressed = false;
        enterPressed = false;
        upArrowPressed = false;
        downArrowPressed = false;
    }

}
