package controller;

import view.AudioManager;
import view.gamePanelViews.GameView;
import view.gamePanelViews.WhatsYourNameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

public class WhatsYourNameController extends Observable implements KeyListener {

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

        this.addObserver(AudioManager.getInstance());
    }


    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isLetterOrDigit(c)){
            if (currentName.length() <= maxNameLength){
                currentName.append(c);
                whatsYourNameView.updateNameDisplay(currentName.toString()); //update the view at each letter typed in

                setChanged();
                notifyObservers("key typed");
                clearChanged();

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

            setChanged();
            notifyObservers("enter pressed");
            clearChanged();
        }

        if (code == KeyEvent.VK_BACK_SPACE && !currentName.isEmpty()){
            currentName.deleteCharAt(currentName.length() - 1);
            whatsYourNameView.updateNameDisplay(currentName.toString());

            setChanged();
            notifyObservers("backspace pressed");
            clearChanged();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public static String getSubmittedName() {
        return submittedName;
    }




}
