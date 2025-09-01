package controller;


import main.JImpossibleMissionKnockoff;
import view.AudioManager;
import view.gamePanelViews.GameView;
import view.gamePanelViews.PauseMenuView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
/**
 * controller, extension of the input handler(handles the player key presses), this class adds the 'escape' key
 * handling, by coordinating the game controller, game view and pause menu view in the {@link #keyPressed(KeyEvent)}method.
 * {@link GameController}, {@link GameView}, {@link PauseMenuView}.
 * it is an observable for the {@link AudioManager} to play a sound when the escape key is pressed.
 */
//handles in game controls and pause game toggling
public class GameKeyListener extends Observable implements KeyListener {
    /**
     * reference to the {@link GameController}, to toggle the game state
     */
    private final GameController gameController;
    /**
     * reference to the {@link InputHandler}, which processes the player's keyboard inputs. this class is just an extension of the
     * keyhandler class, because it just handles the 'esc' key during gameplay
     */
    private final InputHandler inputHandler;
    /**
     * reference to the {@link GameView} view, used to remove the focusability of the game view during the pause menu.
     *
     */
    private final GameView gameView;
    /**
     * reference to the {@link PauseMenuView} view, used to set the pause menu visible when esc is pressed
     */
    private final PauseMenuView pauseMenuView;
    /**
     * reference to the JFrame created in {@link JImpossibleMissionKnockoff}. used to request a repaint of the game window
     */
    private final JFrame window;

    /**controller, extension of the input handler(handles the player key presses), this class adds the 'escape' key
     * handling, by coordinating the game controller, game view and pause menu view. {@link GameController}, {@link GameView}, {@link PauseMenuView}
     * @param gameController {@link GameController} controller
     * @param inputHandler {@link InputHandler} controller
     * @param gameView {@link GameView} view
     * @param pauseMenuView {@link PauseMenuView}
     * @param window JFrame created in {@link JImpossibleMissionKnockoff}
     */
    public GameKeyListener(GameController gameController, InputHandler inputHandler, GameView gameView, PauseMenuView pauseMenuView, JFrame window) {
        this.gameController = gameController;
        this.inputHandler = inputHandler;
        this.gameView = gameView;
        this.pauseMenuView = pauseMenuView;
        this.window = window;

        this.addObserver(AudioManager.getInstance());
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**processes all the key presses via {@link InputHandler} and  the coordinate controllers and views when the 'esc' key is pressed
     * to pause the game.
     * the game state is set to paused via the {@link GameController} .togglePause() method.
     * whenever the escape key is pressed, it sends a notification to {@link AudioManager} to play a sound.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && gameController.getGameState() == GameController.GameState.PLAYING) {
            gameController.togglePause();
            inputHandler.resetAllKeys();

            setChanged();
            notifyObservers("esc pressed");
            clearChanged();

            gameView.setFocusable(false);

            pauseMenuView.setVisible(true);
            pauseMenuView.setFocusable(true);
            pauseMenuView.requestFocusInWindow();

            window.revalidate();
            window.repaint();

        }
        else{   //for the other keys let the input handler handle the key presses
            inputHandler.keyPressed(e);
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameController.getGameState() == GameController.GameState.PLAYING) {
            inputHandler.keyReleased(e);
        }
    }
}
