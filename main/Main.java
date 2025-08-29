package main;

import controller.TitleScreenController;
import view.AudioManager;
import view.gamePanelViews.TitleScreenView;
import javax.swing.*;


/**
 * Impossible Mission knockoff, made for Sapienza's university's first year java course.
 *
 */
public class Main {


    /** game entry point.
     * it creates the JFrame, sets the title of the window, initiates the {@link TitleScreenView} and connects the {@link TitleScreenController}
     * to handle user navigation.
     * it initiates/loads all the game audio via {@link AudioManager}.
     *
     * @param args
     */
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

