package controller;

import view.AudioManager;
import view.gamePanelViews.LeaderboardView;
import view.gamePanelViews.TitleScreenView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

/**
 * controller of the {@link LeaderboardView} view and {@link model.Leaderboard} model.
 * just handles the key presses (enter key) when the user is in the leaderboard panel from the title screen.
 */
public class LeaderboardViewController extends Observable implements KeyListener{

    /**
     * reference to the {@link LeaderboardView} view
     */
    private final LeaderboardView leaderboardView;
    /**
     * JFrame created in the {@link main.Main} class
     */
    private final JFrame window;

    /**the controller to navigate around the leaderboard view. handles the key presses when the user is in the leaderboard panel
     * @param window game window, the JFrame created in the {@link main.Main}
     * @param leaderboardView {@link LeaderboardView}.
     */
    public LeaderboardViewController(JFrame window, LeaderboardView leaderboardView) {
        this.window = window;
        this.leaderboardView = leaderboardView;


        this.addObserver(AudioManager.getInstance());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**checks if the enter key is pressed.
     * in the leaderboard panel, the user can only press this key to go back into the title screen.
     * once the user presses the enter key, {@link AudioManager} gets notified of the keypress and plays a sound and {@link #backToMainMenu()}
     * method gets called, which manages the views.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            backToMainMenu();

            setChanged();
            notifyObservers("menu enter");
            clearChanged();

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * triggered when the user presses the 'enter' key to go back into the title screen.
     * removes everything from the content pane, creates a new {@link TitleScreenView} and connects the {@link TitleScreenController}.
     */
    private void backToMainMenu() {
        window.getContentPane().removeAll();
        TitleScreenView titleScreenView = new TitleScreenView();
        window.add(titleScreenView);
        titleScreenView.setBounds(0, 0, window.getWidth(), window.getHeight());
        window.setLocationRelativeTo(null);

        TitleScreenController titleScreenController = new TitleScreenController(window, titleScreenView);
        titleScreenView.addKeyListener(titleScreenController);
        titleScreenView.setFocusable(true);
        titleScreenView.requestFocus();
        window.revalidate();
        window.repaint();
    }



}
