package controller;

import view.AudioManager;
import view.gamePanelViews.GameView;
import view.gamePanelViews.WhatsYourNameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

/**
 * controller of the {@link WhatsYourNameView} view.
 * when the user is in the what's your name? screen, it builds the {@link #currentName} with the keys that are being typed in.
 * when the user pressed the enter key, it will enter the submitted name for the {@link model.Leaderboard} model.
 * it is an observable, strictly because of the {@link AudioManager}.
 */
public class WhatsYourNameController extends Observable implements KeyListener {

    /**
     * reference to the {@link WhatsYourNameView}
     */
    private final WhatsYourNameView whatsYourNameView;
    /**
     * the current typed in name
     */
    private final StringBuilder currentName = new StringBuilder();
    /**
     * the maximum allowed length of the name
     */
    private final int maxNameLength = 7;

    /**
     * the finished name that the user has typed in
     */
    private static String submittedName;

    /**
     * reference to {@link GameView} view
     */
    private final GameView gameView;


    /** controller of the what's your name? screen when the used enters in a new game.
     * when the user is in the what's your name? screen, it builds the {@link #currentName} with the keys that are being typed in.
     * when the user pressed the enter key, it will enter the submitted name.
     * @param whatsYourNameView {@link WhatsYourNameView} view
     * @param gameView {@link GameView} view
     */
    public WhatsYourNameController(WhatsYourNameView whatsYourNameView, GameView gameView) {
        this.whatsYourNameView = whatsYourNameView;
        this.gameView = gameView;

        this.addObserver(AudioManager.getInstance());
    }


    /**handles the key typed in when the user is in the what's your name? screen.
     * sends a notification to {@link AudioManager} when a key is typed in
     * @param e the event to be processed
     */
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

    /**handles the key pressed when the user is in the what's your name? screen
     * if the user presses enter, it submits the composed name.
     * if the user presses backspace, it deletes a single char from the name that is being typed it.
     * sends notifications to the {@link AudioManager}, to play a sound.
     * @param e the event to be processed
     */
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


    /**gets the submitted name . {@link #submittedName}.
     * @return the submitted name
     */
    public static String getSubmittedName() {
        return submittedName;
    }




}
