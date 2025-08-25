package controller;


import view.gamePanelViews.GameView;
import view.gamePanelViews.PauseMenuView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//handles in game controls and pause game toggling
public class GameKeyListener implements KeyListener {
    private final GameController gameController;
    private final InputHandler inputHandler;
    private final GameView gameView;
    private final PauseMenuView pauseMenuView;
    private final JFrame window;

    public GameKeyListener(GameController gameController, InputHandler inputHandler, GameView gameView, PauseMenuView pauseMenuView, JFrame window) {
        this.gameController = gameController;
        this.inputHandler = inputHandler;
        this.gameView = gameView;
        this.pauseMenuView = pauseMenuView;
        this.window = window;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && gameController.getGameState() == GameController.GameState.PLAYING) {
            gameController.togglePause();
            inputHandler.resetAllKeys();

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
