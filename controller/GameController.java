package controller;

import model.entities.Drone;
import model.entities.Player;
import model.inventoryrelated.Inventory;
import model.levels.LevelManager;
import view.GameView;

import javax.swing.*;

/** what it should do:
 * -process input
 * -updates model based on input
 * -coordinate model and view
 *
 * what it should NOT do:
 * should not render anything
 * should not store persistent game state
 */
public class GameController implements Runnable {
    //models
    Player player;
    LevelManager levelManager;

    //view
    GameView gameView;

    //controller, handles inputs
    InputHandler inputHandler;

    //game loop
    private Thread gameThread;
    private final int FPS = 60;
    private boolean running = true;

    //game states
    public enum GameState {
        PLAYING, PAUSED, GAME_OVER, MAIN_MENU, RESULT
    }
    private GameState currentState = GameState.PLAYING;


    public GameController(Player player, InputHandler inputHandler, GameView gameView, LevelManager levelManager) {
        this.player = player;
        this.inputHandler = inputHandler;
        this.gameView = gameView;
        this.levelManager = levelManager;
    }

    public void startGameThread() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void togglePause(){
        if(currentState == GameState.PLAYING){
            currentState = GameState.PAUSED;
        }
        else if(currentState == GameState.PAUSED){
            currentState = GameState.PLAYING;
        }
        System.out.println("current game state:" + currentState);
    }

    public void stopGameThread() {
        running = false;
        currentState = GameState.GAME_OVER;

        if (Thread.currentThread() != gameThread) {
            if(gameThread != null && gameThread.isAlive()) {
                gameThread.interrupt();
            }
        }
    }


    //GAME LOOP
    //coordinates the model and the view.
    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (running) {
            if (currentState == GameState.PLAYING) {    //update game state if not paused
                //MODEL
                //update player model
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

                if(player.getIsExtracted()){
                    currentState = GameState.RESULT;
                }

                //update drones
                for (Drone drone : levelManager.getCurrentRoom().getDrones()) {
                    drone.update();
                }

                // handle level transition
                levelManager.handleLevelTransition();

                //always request repaint
                gameView.repaint(); // -> THIS CALLS PAINT COMPONENT METHOD IN GAMEVIEW

                //update the discard progress for each slot
                Inventory inventory = player.getInventory();
                inventory.updateDiscardProgress(0, inputHandler.onePressed);
                inventory.updateDiscardProgress(1, inputHandler.twoPressed);
                inventory.updateDiscardProgress(2, inputHandler.threePressed);
                inventory.updateDiscardProgress(3, inputHandler.fourPressed);
                inventory.updateDiscardProgress(4, inputHandler.fivePressed);

                if (player.getIsExtracted()) {
                    currentState = GameState.RESULT;
                }


            }

            //handle frame timing
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1_000_000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                if (!running) {
                    //break if shutdown
                    break;
                }
                Thread.currentThread().interrupt();
            }
        }
    }



    public LevelManager getLevelManager() {
        return levelManager;
    }

    public GameState getGameState(){
        return currentState;
    }



}
