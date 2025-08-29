package controller;

import model.Leaderboard;
import model.ScreenSettings;
import model.entities.Player;
import model.levels.LevelManager;
import model.levels.TileManager;
import view.AudioManager;
import view.gamePanelViews.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * controller of the {@link TitleScreenView}, game entry point.
 * handles the keyboard inputs of the user when he is in the title screen, and starts the game via {@link #startMainGame(boolean)}
 * if the user selects 'new game' or 'play tutorial'. the method handles the initialization of the models, views and controllers of the game.
 */
public class TitleScreenController extends Observable implements KeyListener {

    /**
     * reference to the JFrame created in the {@link main.Main}
     */
    private final JFrame window;
    /**
     * reference to the {@link TitleScreenView} view
     */
    private final TitleScreenView titleScreenView;

    /**
     * a set containing the currently user pressed keys
     * had to use this because otherwise when the user presses a key, multiple notifications
     * get sent to the audio manager, so it caused multiple sounds (too many) if the user held down a key
     *
     */
    private Set<Integer> pressedKeys;       //have to use this because otherwise when the user preses a key, multiple notifications
                                            //get sent to the observer audio manager

    /**controller, handles the user keyboard inputs when he is in the title screen.
     * entry point of the game, so it handles the game startup via {@link #startMainGame(boolean)} or the leaderboard view if the user selects
     * 'leaderboard' in the title screen.
     * @param window JFrame window created in {@link main.Main}
     * @param titleScreenView {@link TitleScreenView} view
     */
    public TitleScreenController(JFrame window, TitleScreenView titleScreenView) {
        this.window = window;
        this.titleScreenView = titleScreenView;
        this.pressedKeys = new HashSet<>();

        AudioManager audioManager = AudioManager.getInstance(); //beautiful.
        this.addObserver(audioManager);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**handles key pressed when the user is in the title screen.
     * the user can press the arrow keys to navigate the title screen and enter key to select the option.
     * notifies {@link AudioManager} to play a sound when a key is pressed.
     * when the user confirms the selected option with the 'enter' key, {@link #handleEnterInput()} method is called
     * handle the keypress.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(pressedKeys.add(keyCode)){       //it returns true if a key is added to the set
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    titleScreenView.selectPreviousOption();
                    titleScreenView.repaint();

                        setChanged();
                        notifyObservers("menu up");
                        clearChanged();


                    break;
                case KeyEvent.VK_DOWN:
                    titleScreenView.selectNextOption();
                    titleScreenView.repaint();

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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        pressedKeys.remove(code);   //remove from set when key is released

    }

    /**
     * handles the enter key press.
     * if the user selected 'new game', the {@link #startMainGame(boolean)} gets called that connect every single controller / view.
     * the boolean 'play tutorial' will gets set to false, and {@link LevelManager} will load the default world layout.
     * if the user selected 'play tutorial' the {@link #startMainGame(boolean)} gets called that connect every single controller / view.
     * the boolean 'play tutorial' will gets set to true, and {@link LevelManager} will load the tutorial world layout.
     * if the user selected 'leaderboard', then the {@link LeaderboardView} and {@link LeaderboardViewController} get created to display the leaderboard.
     * if the user selected 'exit game', exits the game.
     */
    private void handleEnterInput() {
        String selectedOption = titleScreenView.getSelectedOption();
        switch (selectedOption){
            case "new game":
                startMainGame(false);
                break;
            case "play tutorial":
                startMainGame(true);
                break;
            case "leaderboard":
                displayLeaderboard();
                break;
            case "exit":
                System.exit(0);
                break;
        }
    }

    /**creates and connects the required models, controllers and views of the game.
     * asks {@link LevelManager} to initialize the tutorial layout if the playTutorial flag is set to true,
     * asks {@link LevelManager} to initialize the default game world layout if the playTutorial flag is set to false.
     * note that if the user selects the 'new game' option, he will be brought to the {@link WhatsYourNameView} to enter his name
     * for the {@link Leaderboard}.
     * @param playTutorial used to select what world layout will get initialized in the {@link LevelManager}.
     *                     if true, then the tutorial layout will get initialized.
     *                     if false, then the default game world layout will get initialized.
     */
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



//        WhatsYourNameView whatsYourNameView = new WhatsYourNameView();

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
//        whatsYourNameView.setBounds(0,0 , window.getWidth(), window.getHeight());


        layeredPane.add(gameView, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(pauseMenuView, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(resultScreenView, JLayeredPane.MODAL_LAYER);
//        layeredPane.add(whatsYourNameView, JLayeredPane.POPUP_LAYER);


        pauseMenuView.setVisible(false);
//        whatsYourNameView.setVisible(true);


        gameView.setResultScreenView(resultScreenView);

        //attach the pause menu controller
        PauseMenuController pauseMenuController = new PauseMenuController(window, pauseMenuView, gameView, gameController);
        pauseMenuView.addKeyListener(pauseMenuController);
        GameKeyListener gameKeyListener = new GameKeyListener(gameController, inputHandler, gameView,pauseMenuView, window);
        gameView.addKeyListener(gameKeyListener);
//        WhatsYourNameController whatsYourNameController = new WhatsYourNameController(whatsYourNameView, gameView);
//        whatsYourNameView.addKeyListener(whatsYourNameController);
//        whatsYourNameView.requestFocusInWindow();
//        gameView.requestFocusInWindow();

        //load current level
        if(playTutorial) {
            levelManager.initializeTutorialLayout();
            gameView.requestFocusInWindow();
        }
        else {
            levelManager.initializeWorldLayout();
            WhatsYourNameView whatsYourNameView = new WhatsYourNameView();
            whatsYourNameView.setBounds(0,0 , window.getWidth(), window.getHeight());
            layeredPane.add(whatsYourNameView, JLayeredPane.POPUP_LAYER);
            whatsYourNameView.setVisible(true);
            WhatsYourNameController whatsYourNameController = new WhatsYourNameController(whatsYourNameView, gameView);
            whatsYourNameView.addKeyListener(whatsYourNameController);
            whatsYourNameView.requestFocusInWindow();
        }
        levelManager.loadCurrentRoom();

        System.out.println("[TitleScreenController][startMainGame()] window focus owner: " + window.getFocusOwner());



        gameController.startGameThread();
    }


    /**
     * handles the creation/display of the {@link LeaderboardView} and {@link LeaderboardViewController}.
     */
    private void displayLeaderboard() {
        window.remove(titleScreenView);
        window.revalidate();
        window.repaint();

        System.out.println("[TitleScreenController][displayLeaderboard()] title screen view set to not visible ");
        //create new leaderboard view instance
        Leaderboard leaderboard = Leaderboard.getInstance();
        LeaderboardView leaderboardView = new LeaderboardView(leaderboard);
        window.add(leaderboardView);
        leaderboardView.setBounds(0, 0, ScreenSettings.SCREEN_WIDTH , ScreenSettings.SCREEN_HEIGHT);
        leaderboardView.setVisible(true);
        leaderboardView.requestFocusInWindow();
        window.repaint();
        System.out.println("[TitleScreenController][displayLeaderboard()] leaderboard view set to visible ");

        //connect the controller
        LeaderboardViewController leaderboardViewController = new LeaderboardViewController(window, leaderboardView);
        leaderboardView.addKeyListener(leaderboardViewController);




    }


}
