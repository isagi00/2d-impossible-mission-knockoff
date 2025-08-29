package controller;

import view.AudioManager;
import view.gamePanelViews.GameView;
import view.gamePanelViews.PauseMenuView;
import view.gamePanelViews.TitleScreenView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

/**
 * controller of the pause menu, helps to process the pause menu navigation and coordinates the {@link TitleScreenView} and {@link GameView}.
 */
public class PauseMenuController extends Observable implements KeyListener{
    /**
     * reference to the JFrame created during startup in {@link main.Main}
     */
    private final JFrame window;
    /**
     * reference to the {@link PauseMenuView}
     */
    private final PauseMenuView pauseMenuView;
    /**
     * reference to the {@link GameView}
     */
    private final GameView gameView;
    /**
     * reference to the {@link GameController}
     */
    private final GameController gameController;

    /**controller, helps out processing the user keyboard inputs when he is in the pause menu.
     * {@link PauseMenuView}.
     * @param window JFrame created in {@link main.Main}
     * @param pauseMenuView {@link PauseMenuView} view
     * @param gameView {@link GameView} view
     * @param gameController {@link GameController} controller
     */
    public PauseMenuController(JFrame window, PauseMenuView pauseMenuView, GameView gameView, GameController gameController) {
        this.window = window;
        this.pauseMenuView = pauseMenuView;
        this.gameView = gameView;
        this.gameController = gameController;

        this.addObserver(AudioManager.getInstance());
    }





    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**processes the user key presses when in the pause menu.
     * the user can press the up arrow key to navigate up, down arrow key to navigate down,
     * and the enter key to select and confirm the selected option.
     * notifies {@link AudioManager} to play the menu sounds.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                pauseMenuView.selectPreviousOption();
                pauseMenuView.repaint();

                setChanged();
                notifyObservers("menu up");
                clearChanged();

                break;
            case KeyEvent.VK_DOWN:
                pauseMenuView.selectNextOption();
                pauseMenuView.repaint();

                setChanged();
                notifyObservers("menu down");
                clearChanged();

                break;
            case KeyEvent.VK_ENTER:
                handleEnterInput();

                setChanged();
                notifyObservers("menu enter");
                clearChanged();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * handles the currently selected option logic.
     * 1. if it is 'resume', then the pause menu will be set to not be visible, and game controller state will be set to 'playing',
     * and all models will resume to update.
     * {@link GameController}, {@link controller.GameController.GameState}.
     * 2. if it is 'to the main menu', then the content pane will get cleared and the used will be brought to the title screen.
     * {@link TitleScreenView}, {@link TitleScreenController}.
     */
    private void handleEnterInput() {
        String option = pauseMenuView.getSelectedOption();
        switch (option){
            case "resume":
                pauseMenuView.setVisible(false);
                pauseMenuView.setFocusable(false);

                gameView.setFocusable(true);
                gameView.requestFocusInWindow();

                gameController.togglePause();
                break;

            case "to the main menu":
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
                break;
        }
    }

}
