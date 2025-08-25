package main;

import controller.GameController;
import controller.InputHandler;
import controller.TitleScreenController;
import model.levels.LevelManager;
import model.tiles.TileManager;
import model.entities.Player;
import view.GameView;
import view.PauseMenuView;
import view.TitleScreenView;
import view.ResultScreenView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {


    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");        //helps with the fps, enables (me on macbook) to have swing have the game run at 60+ fps instead of 30.
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
    }

}

