package controller;

import model.ScreenSettings;
import view.AudioManager;
import view.gamePanelViews.ResultScreenView;
import view.gamePanelViews.TitleScreenView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;


/**
 * controller of the result screen, handles enter key processing that allows the user to go back into the title screen.
 * {@link ResultScreenView}.
 */
public class ResultScreenController extends Observable implements KeyListener{

    /**
     * reference to the {@link ResultScreenView}
     */
    private final ResultScreenView resultScreenView;
    /**
     * reference to the JFrame created in the {@link main.Main}
     */
    private final JFrame window;

    /**controller that handles the enter input when the user extracts and is in the result screen. {@link ResultScreenView}.
     * @param resultScreenView {@link ResultScreenView} view
     * @param window JFrame created in the {@link main.Main}
     */
    public ResultScreenController(ResultScreenView resultScreenView, JFrame window) {
        this.resultScreenView = resultScreenView;
        this.window = window;

        this.addObserver(AudioManager.getInstance());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /** processes the keyboard inputs while the user is int the result screen. {@link ResultScreenView}.
     * the user can only press the 'enter' key, to return to the title screen. {@link TitleScreenView}.
     * notifies the {@link AudioManager} to play a sound when the enter key is pressed.
     * {@link #goToMainMenu()} handles/coordinates the views.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("[ResultScreenController][keypressed()]  enter pressed  ");

            setChanged();
            notifyObservers("enter pressed");
            clearChanged();

            goToMainMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    /**
     * triggered when the user presses the 'enter key', makes him return to the title screen.
     * {@link TitleScreenView}, {@link TitleScreenController}.
     */
    private void goToMainMenu() {
        window.getContentPane().removeAll();

        TitleScreenView titleScreenView = new TitleScreenView();
        window.add(titleScreenView);
        titleScreenView.setBounds(0, 0, ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT);

        TitleScreenController  titleScreenController = new TitleScreenController(window, titleScreenView);
        titleScreenView.addKeyListener(titleScreenController);

        titleScreenView.setFocusable(true);
        titleScreenView.requestFocus();
        window.revalidate();
        window.repaint();
    }


}
