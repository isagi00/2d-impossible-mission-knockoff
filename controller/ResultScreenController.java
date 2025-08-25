package controller;

import model.ScreenSettings;
import view.gamePanelViews.ResultScreenView;
import view.gamePanelViews.TitleScreenView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class ResultScreenController implements KeyListener{

    private final ResultScreenView resultScreenView;
    private final JFrame window;

    public ResultScreenController(ResultScreenView resultScreenView, JFrame window) {
        this.resultScreenView = resultScreenView;
        this.window = window;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("[ResultScreenController][keypressed()] :  "+ e.getKeyChar());
            goToMainMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


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
