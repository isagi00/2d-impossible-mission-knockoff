package controller;

import view.GameView;
import view.PauseMenuView;
import view.TitleScreenView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseMenuController implements KeyListener{
    private final JFrame window;
    private final PauseMenuView pauseMenuView;
    private final GameView gameView;
    private final GameController gameController;

    public PauseMenuController(JFrame window, PauseMenuView pauseMenuView, GameView gameView, GameController gameController) {
        this.window = window;
        this.pauseMenuView = pauseMenuView;
        this.gameView = gameView;
        this.gameController = gameController;
    }





    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                pauseMenuView.selectPreviousOption();
                pauseMenuView.repaint();
                break;
            case KeyEvent.VK_DOWN:
                pauseMenuView.selectNextOption();
                pauseMenuView.repaint();
                break;
            case KeyEvent.VK_ENTER:
                handleEnterInput();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

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
