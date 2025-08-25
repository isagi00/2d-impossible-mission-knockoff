package controller;

import model.ScreenSettings;
import model.entities.Player;
import model.levels.LevelManager;
import model.tiles.TileManager;
import view.GameView;
import view.PauseMenuView;
import view.ResultScreenView;
import view.TitleScreenView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TitleScreenController implements KeyListener {

    private final JFrame window;
    private final TitleScreenView titleScreenView;

    public TitleScreenController(JFrame window, TitleScreenView titleScreenView) {
        this.window = window;
        this.titleScreenView = titleScreenView;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                titleScreenView.selectPreviousOption();
                titleScreenView.repaint();
                break;
            case KeyEvent.VK_DOWN:
                titleScreenView.selectNextOption();
                titleScreenView.repaint();
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
        String selectedOption = titleScreenView.getSelectedOption();
        switch (selectedOption){
            case "new game":
                startMainGame(false);
                break;
            case "play tutorial":
                startMainGame(true);
                break;
            case "exit":
                System.exit(0);
                break;
        }
    }

    private void startMainGame(boolean playTutorial) {
        window.remove(titleScreenView);
        window.revalidate();
        window.repaint();

        //load game model components
        InputHandler inputHandler = new InputHandler();
        TileManager tileManager = new TileManager();
        Player player = new Player();
        LevelManager levelManager = new LevelManager(tileManager, player);
        player.setLevelManager(levelManager);

        //load game view components
        GameView gameView = new GameView(player, tileManager, levelManager, window);
        PauseMenuView pauseMenuView = new PauseMenuView();

        //result screen view
        ResultScreenView resultScreenView = new ResultScreenView(player);
        resultScreenView.setBounds(0, 0, window.getWidth(), window.getHeight());
        resultScreenView.setVisible(false);
        resultScreenView.addKeyListener(new ResultScreenController(resultScreenView, window));



        //load game controller component
        GameController gameController = new GameController(player, inputHandler, gameView, levelManager);

        //setup layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        window.setContentPane(layeredPane);
        window.revalidate();
        window.repaint();

        gameView.setBounds(0,0 , window.getWidth(), window.getHeight());
        pauseMenuView.setBounds(0,0 , window.getWidth(), window.getHeight());


        layeredPane.add(gameView, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pauseMenuView, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(resultScreenView, JLayeredPane.MODAL_LAYER);


        pauseMenuView.setVisible(false);


        gameView.setResultScreenView(resultScreenView);

        //attach the pause menu controller
        PauseMenuController pauseMenuController = new PauseMenuController(window, pauseMenuView, gameView, gameController);
        pauseMenuView.addKeyListener(pauseMenuController);
        GameKeyListener gameKeyListener = new GameKeyListener(gameController, inputHandler, gameView,pauseMenuView, window);
        gameView.addKeyListener(gameKeyListener);
        gameView.requestFocusInWindow();

        //load current level
        if(playTutorial) levelManager.initializeTutorialLayout();
        else levelManager.initializeWorldLayout();
        levelManager.loadCurrentRoom();

        System.out.println("[TitleScreenController][startMainGame()] window focus owner: " + window.getFocusOwner());
        gameController.startGameThread();
    }


}
