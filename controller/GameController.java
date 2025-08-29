package controller;

import model.entities.Dog;
import model.entities.Drone;
import model.entities.Player;
import model.inventoryrelated.Inventory;
import model.levels.LevelManager;
import view.gamePanelViews.GameView;

/**
 * controller component, updates all the entities states, and requests the game view to render.
 *
 *flow of the controller is:
 * 1. {@link #startGameThread()} is called in the {@link view.gamePanelViews.TitleScreenView} if the user
 * select 'new game' or 'play tutorial' and a new thread gets created, which calls the {@link #run()} method automatically.
 * 2. in the {@link #run()} method, a fixed-time-step game LOOP is implemented: for a fixed time window, the thread is going to sleep.
 *  once that time is up, it will run once more. when it runs, it will call every single .update() method of each entity,
 *  and requests a repaint of {@link GameView}.
 */
public class GameController implements Runnable {
    //models
    /**
     * reference to {@link Player} model.
     */
    private final Player player;
    /**
     * reference to the {@link LevelManager}, which manages the game world.
     */
    private final LevelManager levelManager;

    //view
    /**
     * reference to the {@link GameView} view, which handles the rendering of the game.
     */
    private final GameView gameView;

    //handles inputs
    /**
     * reference to the {@link InputHandler} controller, which handles the user's keyboard inputs
     * to move the player
     */
    private final InputHandler inputHandler;

    //game loop
    /**
     * game thread. calls {@link #run()} after being created in {@link #startGameThread()}
     */
    private Thread gameThread;
    /**
     * indicates the target fps of the game. it is used to as a denominator to get the draw interval.
     * see {@link #run()}.
     */
    private final int FPS = 60;
    /**
     * flag that indicates if the game thread is running or not
     */
    private boolean running = true;

    /**
     * state of the game.
     * playing: while in this state, the game thread will stop calling all the models update methods
     * paused: while in this state, the game thread won't call any models update methods
     * result: while in this state, the game thread won't call any models update methods
     */
    //game state
    public enum GameState {
        PLAYING, PAUSED, RESULT
    }

    /**
     * current state of the player. see {@link GameState} for all the available game states
     */
    private GameState currentState = GameState.PLAYING;


    /**controller of the game.
     * manages the game thread, and calls each model's update methods if the {@link GameState} is 'playing' and requests
     * {@link GameView} repaints.
     * @param player {@link Player} model
     * @param inputHandler {@link InputHandler} controller
     * @param gameView {@link GameView} view
     * @param levelManager {@link LevelManager} model
     */
    public GameController(Player player, InputHandler inputHandler, GameView gameView, LevelManager levelManager) {
        this.player = player;
        this.inputHandler = inputHandler;
        this.gameView = gameView;
        this.levelManager = levelManager;
    }

    /**
     * starts the game thread by creating a new Thread object.
     * the game thread automatically calls the {@link #run()} method and updates the models based on the user's inputs.
     */
    public void startGameThread() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**toggles the game state between 'playing' and paused.
     * sets the current state of the game to 'paused' if the game state is 'playing'.
     * sets the current state of the game to 'playing' if the game state is 'paused.
     */
    public void togglePause(){
        if(currentState == GameState.PLAYING){
            currentState = GameState.PAUSED;
        }
        else if(currentState == GameState.PAUSED){
            currentState = GameState.PLAYING;
        }
        System.out.println("[GameController] current game state:" + currentState);
    }


    /** automatically called when the {@link #gameThread} starts.
     * while the thread is running and if the current state of the game is 'playing', then it will call each model's
     * .update() method. for example, see {@link Player}.
     * the game uses a fixed-time-step way to manage the game loop:
     * 1. it computes the time in nanoseconds between frames
     * 2. it computes the time of the next frame that should be rendered
     * 3. computes how much time is left until the next frame, and sleeps for that amount of time to maintain a consistent frame rate
     *
     * note: during the 'paused' and 'result' states, the thread will only handle the frame timing without updating the game state.
     */
    //GAME LOOP
    @Override
    public void run() {

        double drawInterval = (double) 1_000_000_000 / FPS;     //time in nanoseconds between frames
        double nextDrawTime = System.nanoTime() + drawInterval; //time of when the next frame should be rendered

        while (running) {   //game loop. it continues while running is true.
            //update game state if not paused
            if (currentState == GameState.PLAYING) {
                //update player model based on input
                player.update(inputHandler.upPressed,
                        inputHandler.downPressed,
                        inputHandler.leftPressed,
                        inputHandler.rightPressed,
                        inputHandler.spacePressed,
                        inputHandler.ePressed,
                        inputHandler.upArrowPressed,
                        inputHandler.downArrowPressed,
                        inputHandler.enterPressed
                );
                //set the state to result if the player has extracted
                if(player.getIsExtracted()){
                    currentState = GameState.RESULT;
                }

                //update all the drones in the room
                for (Drone drone : levelManager.getCurrentRoom().getDrones()) {
                    drone.update();
                }

                //update all the dogs in the room
                for (Dog dog : levelManager.getCurrentRoom().getDogs()){
                    dog.update();
                }

                //handle level transition
                levelManager.handleLevelTransition();

                //always request repaint in game view
                gameView.repaint(); // -> THIS CALLS PAINT COMPONENT METHOD IN GAMEVIEW

                //update the discard progress for each slot
                Inventory inventory = player.getInventory();
                inventory.updateDiscardProgress(0, inputHandler.onePressed);
                inventory.updateDiscardProgress(1, inputHandler.twoPressed);
                inventory.updateDiscardProgress(2, inputHandler.threePressed);
                inventory.updateDiscardProgress(3, inputHandler.fourPressed);
                inventory.updateDiscardProgress(4, inputHandler.fivePressed);

            }


            //handle frame timing
            try {
                double remainingTime = nextDrawTime - System.nanoTime();    //how much time is left until the next frame should be rendered
                remainingTime = remainingTime / 1_000_000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);     //sleep the thread to maintain consistent fps
                nextDrawTime += drawInterval;       //adjust the next draw time for the following frame
            }
            catch (InterruptedException e) {
                if (!running) {
                    //break if shutdown
                    break;
                }
                Thread.currentThread().interrupt();
            }
        }
    }


    /**gets the {@link LevelManager} used by this game controller.
     * @return the level manager instance that this controller is using
     */
    public LevelManager getLevelManager() {
        return levelManager;
    }

    /**gets the current state of the game.
     * @see GameState
     * @return the current state of the game.
     */
    public GameState getGameState(){
        return currentState;
    }



}
