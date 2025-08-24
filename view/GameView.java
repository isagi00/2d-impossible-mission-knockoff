package view;

import model.inventoryrelated.ComputerCard;
import model.inventoryrelated.Inventory;
import model.inventoryrelated.Item;
import model.levels.LevelManager;
import model.entities.Player;
import model.interactableObjects.InteractableObject;
import model.levels.Room;
import model.tiles.TileManager;
import model.ScreenSettings;
import view.interactableObjectsViews.InteractableObjectsView;
import view.itemViews.InventoryView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;


/** MAIN RENDERING, TAKES ALL THE VIEWS AND RENDERS
 * should:
 * -render current model state  !SHOULD ONLY DEPEND ON MODELs STATE
 * -be notified of model changes
 *
 * should NOT:
 * -modify model state
 * -contain game logic
 * -know about input
 */
public class GameView extends JPanel {
    //----------------------------------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------------------------------//
    //MODELS
    Player player;
    LevelManager levelManager;
    TileManager tileManager;

    //helpers (subviews)
    PlayerView playerView;
    InteractableObjectsView interactableObjectsView;
    InventoryView inventoryView;
    EnemyView enemyView;
    ResultScreenView resultScreenView;

    //bg
    BufferedImage background;

    //fps tracking vars
    private int frameCount = 0;
    private long lastFpsTime = System.nanoTime();
    private double fps = 0;

    // fonts
    private final Font fpsFont = loadCustomFont("fonts/ThaleahFat.ttf", 25);
    public  final Font gameFont = loadCustomFont("fonts/ThaleahFat.ttf", 50);
    private final Font tutorialTextFont = loadCustomFont("fonts/ThaleahFat.ttf", 40);


    //----------------------------------------------------------------------------------------------------------------//
    // CONSTRUCTOR, ! DEPENDS PURELY ON MODELS !
    //----------------------------------------------------------------------------------------------------------------//
    public GameView(Player player, TileManager tileManager, LevelManager levelManager) {
        //models
        this.tileManager = tileManager;
        this.levelManager = levelManager;
        this.player = player;

        //subviews
        this.playerView = new PlayerView(player);
        this.inventoryView = new InventoryView(player.getInventory());
        this.interactableObjectsView = new InteractableObjectsView(levelManager);
        this.enemyView = new EnemyView(levelManager);
        this.resultScreenView = new ResultScreenView(player);

        //other
        setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setOpaque(true);
        loadBackgroundImage();

        //registering observer/observers
        player.getInventory().addObserver((obs,obj) -> {
            inventoryView.updateItemViews();
            player.getInventory().clearDirtyState();});
            repaint();  //request repaint when inventory changes
    }

    @Override
    public void paintComponent(Graphics g) {
        //paintComponent is defined in the JComponent, the parent class of JPanel.
        //it is the primary method for CUSTOM drawing in swing components.
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //enable text rendering
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

         //fps tracking
        frameCount++;
        long currentTime = System.nanoTime();
        double elapsedSec = (currentTime - lastFpsTime) / 1_000_000_000.0; //convert to seconds
        if (elapsedSec >= 1.0) {
            fps = frameCount / elapsedSec;
            frameCount = 0;
            lastFpsTime = currentTime;
        }

        //load background first
        drawBackground(g2d);

        //draw tiles, get the current room data from level manager
        tileManager.draw(g2d, levelManager.getCurrentRoomData());

        //draw the room's interactive objects
        interactableObjectsView.drawInteractableObjects(g2d);

        //draw the enemies
        enemyView.drawEnemies(g2d);

        //draw the player
        playerView.draw(g2d);

        //draw the player's inventory
        inventoryView.draw(g2d);

        //draw the tutorial level texts
        if(levelManager.getCurrentRoom().getTutorialText() != null){       //load the text only if there is one, prevents null pointer exception
            String text = levelManager.getCurrentRoom().getTutorialText();
            g2d.setFont(tutorialTextFont);
            g2d.drawString(text,  50, 100 );
        }

        //draw the game text
        drawGameText(g2d);

        //if the players extracts then draw the result screen
        if (player.getIsExtracted()) {  // or player.getExtracted()
            resultScreenView.setVisible(true);
            resultScreenView.paintComponent(g2d);  // draw result screen overlay
        }

        g2d.dispose();
    }


    public Font loadCustomFont(String fontPath, float size) {
        try{
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if(stream == null) {    //if the font path provided is not available
                System.out.println("Couldn't find font " + fontPath);
                return new Font("Arial", Font.BOLD, (int) size);
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println("Loaded font " + fontPath);
            return font;

        }catch(Exception e) {
            System.err.println("Couldn't load font " + fontPath);
            return new Font("Arial", Font.BOLD, (int) size);
        }
    }

    private void drawGameText(Graphics2D g2d) {
        //TOP RIGHT: DEPTH - LEVEL - LEVEL TYPE
        g2d.setFont(gameFont);
        g2d.setColor(Color.WHITE);
        int depth = levelManager.getCurrentDepth();
        int level = levelManager.getCurrentCol();
        String currentRoomType = levelManager.getCurrentRoom().getRoomType();
        String currentLevel = depth + " - " + level + "  " + currentRoomType;
        int textWidth = g2d.getFontMetrics().stringWidth(currentLevel);
        g2d.setColor(Color.WHITE);
        g2d.drawString(currentLevel, ScreenSettings.SCREEN_WIDTH - textWidth - 30, 30);

        //DRAW TOP LEFT FPS
        g2d.setFont(fpsFont);
        String fpsText = String.format("FPS: %.1f", fps);
        g2d.setColor(Color.WHITE);
        g2d.drawString(fpsText, 20,30);

        //DRAW INTERACTION BUTTONS WHEN NEAR AND PROGRESS BAR IF INTERACTING
        g2d.setFont(gameFont);
        if (player.getIsShowingInteractionPrompt() && player.getCurrentInteractable() != null){
            InteractableObject obj = player.getCurrentInteractable();
            //background
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillRoundRect(obj.getX() + ScreenSettings.TILE_SIZE, obj.getY() - ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE, ScreenSettings.TILE_SIZE, 10, 10);
            //text
            g2d.setColor(Color.WHITE);
            String interactionPrompt = "e";
            g2d.drawString(interactionPrompt, obj.getX() + ScreenSettings.TILE_SIZE + 13, obj.getY() - 13 );
            //progress bar
            int progressBarWidth = ScreenSettings.TILE_SIZE - 10;
            int filledWidth = (int)((double)obj.getInteractionProgress() / obj.getSearchCompleteTime() * progressBarWidth);
            //remaining interaction progress
            g2d.setColor(Color.GRAY);
            g2d.fillRect(obj.getX() + ScreenSettings.TILE_SIZE + 5, obj.getY() - 10, progressBarWidth, 5);
            //current interaction progress
            g2d.setColor(Color.WHITE);
            g2d.fillRect(obj.getX() + ScreenSettings.TILE_SIZE + 5, obj.getY() - 10, filledWidth, 5);
        }
    }


    private void loadBackgroundImage(){
        try{
            this.background = ImageIO.read(getClass().getClassLoader().getResourceAsStream("backgrounds/background.png"));
        }
        catch(Exception e) {
            System.out.println("Couldn't find background");
            e.printStackTrace();
        }
    }


    private void drawBackground(Graphics2D g2d) {
        Room currentRoom = levelManager.getCurrentRoom();
        Room[][] world = levelManager.getWorldLayout();
        if (currentRoom == world[1][0] || currentRoom == world[1][1] || currentRoom == world[1][2]  ) {     //depth 1
            setBackground(new Color(139,171,191));   //lighter light blue
        }
        else if (currentRoom == world[2][0] || currentRoom == world[2][1] || currentRoom == world[2][2]  ) {     //depth 2
            setBackground(new Color(86,106,137));   //light blue
        }
        else if (currentRoom == world[3][0] || currentRoom == world[3][1] || currentRoom == world[3][2]  ) {     //depth 3
            setBackground(new Color(55, 68, 110)); //dark blue
        }
        else{
            g2d.drawImage(background, 0,0, ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT, null);
        }
    }


}
