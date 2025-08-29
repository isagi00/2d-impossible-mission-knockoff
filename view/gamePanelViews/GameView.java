package view.gamePanelViews;

import model.levels.LevelManager;
import model.entities.Player;
import model.interactableObjects.InteractableObject;
import model.levels.Room;
import model.levels.TileManager;
import model.ScreenSettings;
import view.LevelView;
import view.entityViews.EnemyView;
import view.entityViews.PlayerView;
import view.interactableObjectsViews.InteractableObjectsView;
import view.itemViews.InventoryView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;


/**
 *view, this is the components that requests each view's draw() method. it is composed by all the game views, and decides when to
 * display the result screen. it also helps to draw the appropriate background image, player hud such as the fps and  the
 * information of the player's current position in the game world layouy defined in {@link LevelManager}.
 */
public class GameView extends JPanel {
    //----------------------------------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------------------------------//
    //MODELS
    /**
     *reference to the {@link Player} model, used to get the player's extraction status so that the {@link ResultScreenView} can appear
     * accordingly and the interaction prompt to appear when the player is near an interactable object.
     */
    private final Player player;
    /**
     * reference to the {@link LevelManager} model, used to get the data of the current depth, level, and room type.
     */
    private final LevelManager levelManager;


    //helpers (subviews)
    /**
     *reference to the {@link PlayerView}, used to render the player
     */
    private final PlayerView playerView;
    /**
     * reference to the {@link InteractableObjectsView}, use to render all the interactable objects in the room
     */
    private final InteractableObjectsView interactableObjectsView;
    /**
     * reference to the {@link InventoryView}, used to render the player's inventory
     */
    private final InventoryView inventoryView;
    /**
     * reference to the {@link EnemyView}, used to render all the enemies in the current room
     */
    private final EnemyView enemyView;

    /**
     * reference to the {@link ResultScreenView}, used to render the result screen
     * when the player extracts.
     */
    private ResultScreenView resultScreenView;
    /**
     * reference to the {@link LevelView}, used to render the tiles in the game.
     */
    private final LevelView levelView;
    //game window
    /**
     *reference to the JFrame window created in {@link main.Main}
     */
    private final JFrame window;

    //bg
    /**
     *background image of the game. it only gets rendered when the player is on the first layer
     * of the world layout defined in {@link LevelManager}
     */
    private static BufferedImage backgroundImage;

    //fps tracking vars
    /**
     * numbers of frames that have passed.
     */
    private int frameCount = 0;
    /**
     * used to track the fps.
     */
    private long lastFpsTime = System.nanoTime();
    /**
     * the total frames / second text. it is used the {@link #paintComponent(Graphics)} method to track the fps
     * on the top left corner
     */
    private double fps;

    // fonts
    /**
     * font used to display the fps on the top left corner
     */
    private final Font fpsFont = loadFont("fonts/ThaleahFat.ttf", 25);
    /**
     * font used to display the game text, such as the top right's world info and the interaction prompt of the player.
     */
    public  final Font gameFont = loadFont("fonts/ThaleahFat.ttf", 50);


    /**view of the game. it is composed by all the game models views, such as {@link PlayerView}, {@link EnemyView},
     * {@link InteractableObjectsView}s, {@link LevelView}, and requests the .draw() method of each component.
     * note that it also contains the {@link ResultScreenView}, set in the {@link controller.TitleScreenController},
     * so that it displays when the player successfully extracts.
     *  it calls each entity's draw method
     * @param player {@link Player} model
     * @param tileManager {@link TileManager} model
     * @param levelManager {@link LevelManager} model
     * @param window the JFrame created in {@link main.Main}
     */
    //----------------------------------------------------------------------------------------------------------------//
    // CONSTRUCTOR, ! DEPENDS PURELY ON MODELS !
    //----------------------------------------------------------------------------------------------------------------//
    public GameView(Player player, TileManager tileManager, LevelManager levelManager, JFrame window) {
        this.window = window;

        //models
        this.levelManager = levelManager;
        this.player = player;

        //subviews
        this.playerView = new PlayerView(player);
        this.inventoryView = new InventoryView(player.getInventory());
        this.interactableObjectsView = new InteractableObjectsView(levelManager);
        this.enemyView = new EnemyView(levelManager);
        this.levelView = new LevelView(levelManager, tileManager);

        //other
        window.setPreferredSize(new Dimension(ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT));
        window.setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setOpaque(true);

        //loading background image
        loadBackgroundImage();
    }

    /** this is where the game actually renders:
     * calls every single view component to draw.
     * tracks the fps and makes the {@link ResultScreenView} visible when the {@link Player} extracts.
     * this method is called every time the {@link controller.GameController}'s game loop (the .run() method) requests a repaint().
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        //paintComponent is defined in the JComponent, the parent class of JPanel.
        //it is the primary method for CUSTOM drawing in swing components.
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //enable text rendering
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

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

        //draw the tiles in the level
        levelView.draw(g2d);

        //draw the room's interactive objects
        interactableObjectsView.drawInteractableObjects(g2d);

        //draw the enemies
        enemyView.drawEnemies(g2d);

        //draw the player
        playerView.draw(g2d);

        //draw the player's inventory
        inventoryView.draw(g2d);

        //draw the game text
        drawGameText(g2d);

        if (player.getIsExtracted() && resultScreenView != null) {
            resultScreenView.setVisible(true);
            resultScreenView.requestFocusInWindow();
        }

        g2d.dispose();
    }


    /**loads the font at the provided font path, with the provided size.
     * @param fontPath path of the ttf file
     * @param size font size
     * @return font
     */
    public Font loadFont(String fontPath, float size) {
        try{
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fontPath);
            if(stream == null) {    //if the font path provided is not available
                System.out.println("[GameView]couldn't find font at " + fontPath);
                return null;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);   //create font
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); //gets the ge
            ge.registerFont(font);  //registers the font of the current ge
            System.out.println("[GameView] loaded font " + fontPath);
            return font;

        }catch(Exception e) {
            System.err.println("[GameView]couldn't load font at " + fontPath);
            return null;
        }
    }

    /**draws the game text.
     * top right: world info
     * top left: fps
     * player interaction prompt when near an interactable object.
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    private void drawGameText(Graphics2D g2d) {
        //top right, world info: depth, level index, leveltype
        g2d.setFont(gameFont);
        g2d.setColor(Color.WHITE);
        int depth = levelManager.getCurrentDepth();
        int level = levelManager.getCurrentWorldCol();
        Room.RoomType currentRoomType = levelManager.getCurrentRoom().getRoomType();
        String currentLevel = depth + " - " + level + "  " + currentRoomType.toString();
        int textWidth = g2d.getFontMetrics().stringWidth(currentLevel);
        g2d.setColor(Color.WHITE);
        g2d.drawString(currentLevel, ScreenSettings.SCREEN_WIDTH - textWidth - 30, 30);

        //DRAW TOP LEFT FPS
        g2d.setFont(fpsFont);
        String fpsText = String.format("FPS: %.1f", fps);
        g2d.setColor(Color.WHITE);
        g2d.drawString(fpsText, 20,30);

        //DRAW INTERACTION BUTTON WHEN NEAR an interactable object AND PROGRESS BAR IF INTERACTING
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


    /**
     * loads the background image, drawn in {@link #drawBackground(Graphics2D)}.
     * the background is image is only drawn on the first layer of the game world. see {@link LevelManager}
     */
    private void loadBackgroundImage(){
        try{
            this.backgroundImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("backgrounds/background.png"));
        }
        catch(Exception e) {
            System.out.println("Couldn't find background");
            e.printStackTrace();
        }
    }


    /**
     * draws the game's background.
     * it will display a background image if the player is on the first layer of {@link LevelManager} world layout,
     * and a solid color when deeper into the game world.
     * @param g2d swing's graphics 2d instance that allows rendering
     */
    private void drawBackground(Graphics2D g2d) {
        Room currentRoom = levelManager.getCurrentRoom();
        Room[][] world = levelManager.getWorldLayout();
        if (currentRoom == world[1][0] || currentRoom == world[1][1] || currentRoom == world[1][2]  ) {     //depth 1
            window.setBackground(new Color(139,171,191));   //lighter light blue
        }
        else if (currentRoom == world[2][0] || currentRoom == world[2][1] || currentRoom == world[2][2]  ) {     //depth 2
            window.setBackground(new Color(86,106,137));   //light blue
        }
        else if (currentRoom == world[3][0] || currentRoom == world[3][1] || currentRoom == world[3][2]  ) {     //depth 3
            window.setBackground(new Color(55, 68, 110)); //dark blue
        }
        else{
            g2d.drawImage(backgroundImage, 0,0, ScreenSettings.SCREEN_WIDTH, ScreenSettings.SCREEN_HEIGHT, null);
        }
    }


    /**sets the result screen view. a setter is used because the result screen instance is created in {@link controller.TitleScreenController}'s
     * .startMainGame() method.
     * @param resultScreenView {@link ResultScreenView} view.
     */
    public void setResultScreenView(ResultScreenView resultScreenView) {
        this.resultScreenView = resultScreenView;
    }


}
