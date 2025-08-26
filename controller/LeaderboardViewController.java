package controller;

import view.AudioManager;
import view.gamePanelViews.LeaderboardView;
import view.gamePanelViews.TitleScreenView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

public class LeaderboardViewController extends Observable implements KeyListener{

    private LeaderboardView leaderboardView;
    private JFrame window;

    public LeaderboardViewController(JFrame window, LeaderboardView leaderboardView) {
        this.window = window;
        this.leaderboardView = leaderboardView;


        this.addObserver(AudioManager.getInstance());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

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
