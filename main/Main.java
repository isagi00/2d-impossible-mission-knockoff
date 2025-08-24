package main;

import controller.GameController;
import controller.InputHandler;
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

        //setup menu navigation
        KeyListener startScreenKeyListener = setupStartScreenKeyListener(window, titleScreenView);
        titleScreenView.addKeyListener(startScreenKeyListener);
        titleScreenView.setFocusable(true);
        titleScreenView.requestFocus();
    }



    private static void startMainGame(JFrame window, boolean playTutorial) {
        //load the model instances
        InputHandler inputHandler = new InputHandler();
        TileManager tileManager = new TileManager();
        Player player = new Player();
        LevelManager levelManager = new LevelManager(tileManager, player);
        player.setLevelManager(levelManager);


        //load the view instances
        GameView gameView = new GameView(player, tileManager, levelManager);      //the actual game instance
        PauseMenuView pauseMenuView = new PauseMenuView();                        //pause menu view
        ResultScreenView resultScreenView = new ResultScreenView(player);

        //load the game controller instance
        GameController gameController = new GameController(player, inputHandler, gameView, levelManager);

        //add the view to the window
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        window.setContentPane(layeredPane);

        gameView.setBounds(0,0, window.getWidth(), window.getHeight());
        pauseMenuView.setBounds(0,0, window.getWidth(), window.getHeight());
        resultScreenView.setBounds(0,0, window.getWidth(), window.getHeight());

        layeredPane.add(gameView,JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pauseMenuView,JLayeredPane.PALETTE_LAYER);
        layeredPane.add(resultScreenView,JLayeredPane.PALETTE_LAYER);

        pauseMenuView.setVisible(false);
        resultScreenView.setVisible(false);


        //setup all key handler instances
        KeyListener pauseMenuKeyListener = setupPauseMenuKeyListener(window, pauseMenuView, gameView, gameController);
        KeyListener gameKeyListener = setupGameKeyListener(window, gameView, gameController, inputHandler, pauseMenuView);

        gameView.addKeyListener(gameKeyListener);
        pauseMenuView.addKeyListener(pauseMenuKeyListener);

        gameView.setFocusable(true);        //give the gameview the focus
        SwingUtilities.invokeLater(() -> gameView.requestFocusInWindow());

        //load game if new game, load tutorial if want to play tutorial
        if(playTutorial){
            levelManager.initializeTutorialLayout();
        }
        else{
            levelManager.initializeWorldLayout();
        }
        levelManager.loadCurrentRoom();
        gameController.startGameThread();       //start game thread
    }


    //pause menu key listener
    private static KeyListener setupPauseMenuKeyListener(JFrame window, PauseMenuView pauseMenuView, GameView gameView, GameController gameController) {
        KeyListener pauseMenuKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_UP) {
                    pauseMenuView.selectPreviousOption();
                    pauseMenuView.repaint();
                }
                else if (code == KeyEvent.VK_DOWN){
                    pauseMenuView.selectNextOption();
                    pauseMenuView.repaint();
                }
                else if (code == KeyEvent.VK_ENTER){
                    String selectedOption = pauseMenuView.getSelectedOption();
                    switch (selectedOption){
                        case "resume":
                            SwingUtilities.invokeLater(() -> {
                                //remove the pause menu
                                pauseMenuView.setVisible(false);
                                pauseMenuView.setFocusable(false);
                                gameView.setFocusable(true);
                                gameView.requestFocusInWindow();
                                gameController.togglePause();
                            });
                            break;
                        case "to the main menu":
                            SwingUtilities.invokeLater(() -> {
                                window.getContentPane().removeAll();        //remove everything from the frame
                                //new title screen view
                                TitleScreenView titleScreenView = new TitleScreenView();
                                window.add(titleScreenView);            //add it to the frame
                                titleScreenView.setBounds(0, 0, window.getWidth(), window.getHeight());     //set the size
                                window.setLocationRelativeTo(null);
                                //new key listener
                                KeyListener startScreenKeyListener = setupStartScreenKeyListener(window, titleScreenView);
                                titleScreenView.addKeyListener(startScreenKeyListener);
                                titleScreenView.setFocusable(true);     //set focusable
                                titleScreenView.requestFocus();         //request focus
                                window.revalidate();
                                window.repaint();
                            });
                            break;
                        }
                    }
                }
            @Override
            public void keyReleased(KeyEvent e) {}
        };
        return pauseMenuKeyListener;
    }

    //game key listener
    private static KeyListener setupGameKeyListener(JFrame window, GameView gameView, GameController gameController, InputHandler inputHandler, PauseMenuView pauseMenuView) {
        KeyListener gameKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                //pause menu navigation
                if (keyCode == KeyEvent.VK_ESCAPE && gameController.getGameState() == GameController.GameState.PLAYING ) {//game in running -> pause it
                    SwingUtilities.invokeLater(() -> {
                        gameController.togglePause();       //pause the game
                        inputHandler.resetAllKeys();          //reset key pressed before pausing
                        gameView.setFocusable(false);       //remove focus from gameview
                        pauseMenuView.setVisible(true);

                        pauseMenuView.setFocusable(true);
                        pauseMenuView.requestFocusInWindow();

                        window.revalidate();
                        window.repaint();
                    });
                }
                else {
                    // forward other keys to the regular key handler
                    inputHandler.keyPressed(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (gameController.getGameState() == GameController.GameState.PLAYING) {
                    inputHandler.keyReleased(e);
                }
            }
        };
        return gameKeyListener;
    }



    //start screen keyhandler
    private static KeyListener setupStartScreenKeyListener(JFrame window, TitleScreenView titleScreenView) {
        KeyListener startScreenKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                //handle menu navigation
                if(keyCode == KeyEvent.VK_UP){
                    titleScreenView.selectPreviousOption();
                    titleScreenView.repaint();
                }
                else if(keyCode == KeyEvent.VK_DOWN){
                    titleScreenView.selectNextOption();
                    titleScreenView.repaint();
                }
                else if (keyCode == KeyEvent.VK_ENTER){
                    String selectedOption = titleScreenView.getSelectedOption();
                    switch (selectedOption){
                        case "new game":
                            window.remove(titleScreenView);
                            window.revalidate();
                            startMainGame(window, false );      // false -> dont play tutorial
                            window.repaint();
                            break;
                        case "play tutorial":
                            window.remove(titleScreenView);
                            window.revalidate();
                            startMainGame(window, true);        //true -> play tutorial
                            window.repaint();
                            break;
                        case "exit":
                            System.exit(0);
                            break;
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        };

        return startScreenKeyListener;
    }


    //tryount out git
    //learning the daily workflow
}

