package controller;

import view.GameView;
import view.WhatsYourNameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WhatsYourNameController implements KeyListener {

    private WhatsYourNameView whatsYourNameView;
    private StringBuilder currentName;
    private int maxNameLength;

    private static String submittedName;

    private GameView gameView;


    public WhatsYourNameController(WhatsYourNameView whatsYourNameView, GameView gameView) {
        this.whatsYourNameView = whatsYourNameView;
        this.gameView = gameView;
        currentName = new StringBuilder();
        maxNameLength = 7;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isLetterOrDigit(c)){
            if (currentName.length() <= maxNameLength){
                currentName.append(c);
                whatsYourNameView.updateNameDisplay(currentName.toString()); //update the view at each letter typed in
            }
        }
        e.consume();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ENTER && !currentName.isEmpty()){
            this.submittedName = currentName.toString();
            whatsYourNameView.setVisible(false);
            gameView.requestFocusInWindow();
        }

        if (code == KeyEvent.VK_BACK_SPACE && !currentName.isEmpty()){
            currentName.deleteCharAt(currentName.length() - 1);
            whatsYourNameView.updateNameDisplay(currentName.toString());
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public static String getSubmittedName() {
        return submittedName;
    }




}
