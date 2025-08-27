package main;

import controller.TitleScreenController;
import view.AudioManager;
import view.gamePanelViews.TitleScreenView;
import javax.swing.*;


public class Main {


    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");        //helps with the fps, enables (me on macbook) to have swing have the game run at 60+ fps instead of 30
        //main window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("super knockoff impossible mission");

        //game start screen
        TitleScreenView titleScreenView = new TitleScreenView();        // game starts from the title screen -> from there it decides what do to do
        window.add(titleScreenView);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);


        TitleScreenController titleScreenController = new TitleScreenController(window, titleScreenView);
        titleScreenView.addKeyListener(titleScreenController);
        titleScreenView.setFocusable(true);
        titleScreenView.requestFocus();

        //load audio files on startup
        AudioManager audioManager = AudioManager.getInstance();
    }

}

