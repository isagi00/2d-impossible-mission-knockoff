package model.levels;

import model.ScreenSettings;
import model.entities.Dog;
import model.entities.Drone;
import model.entities.Player;
import model.interactableObjects.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;

/**
 * model.
 * should store level state / data
 * implements level rules *
 * provide this level's data accessors / accessors in general
 *
 * should not know about view or input
 */
public class LevelManager extends Observable {      // -> observers: interactableObjectsView
    //----------------------------------------------------------------------------------------------------------------//
    // FIELDS
    //----------------------------------------------------------------------------------------------------------------//
    //other models
    /**
     * tile manager instance
     */
    private TileManager tileManager;
    /**
     * player instance
     */
    private Player player;

    /**
     * world layout. the game world is represented as a matrix. each slot in the matrix contains a room.
     */
    //data
    private Room[][] worldLayout;       //general game world layout
    /**
     * current world layout row
     */
    private int currentWorldRow;
    /**
     * current world layout col
     */
    private int currentWorldCol;
    /**
     * contain current room data. it is an integer matrix, each integer represent which tile it is at that location.
     */
    private int[][] currentRoomData;    //room data: .txt files that defines the levels
    //----------------------------------------------------------------------------------------------------------------//
    // CONSTRUCTOR
    //----------------------------------------------------------------------------------------------------------------//

    /**the LevelManager constructor initializes:
     * -the current world row and col to 0,
     * -the {@link #worldLayout} Room matrix,
     * -the {@link #currentRoomData} integer matrix,
     * -the first Room {@link #worldLayout} as a Room: this was done to solve a null pointer issue.
     * and adds the players as its observer
     * @param tileManager tile manager instance
     * @param player player instance
     */
    public LevelManager(TileManager tileManager, Player player) {
        this.tileManager = tileManager;
        this.player = player;
        this.currentWorldRow = 0;
        this.currentWorldCol = 0;
        worldLayout = new Room[5][8];   //world size
        //currentRoomData = new int[GameView.MAX_SCREEN_ROW][GameView.MAX_SCREEN_COL];   //a room is at most 32x16 tiles
        currentRoomData = new int[50][50];   //a room is at most 32x16 tiles
        this.addObserver(player);

        worldLayout[0][0] = new Room(Room.RoomType.GROUND, 0, true);     //init a default room when initializing, otherwise it gives a null room (for some reason)

    }
    //----------------------------------------------------------------------------------------------------------------//
    // WORLD LAYOUT
    //----------------------------------------------------------------------------------------------------------------//

    public void initializeWorldLayout() {
        //row 0: surface
        worldLayout[0][0] = new Room(Room.RoomType.GROUND, 0, true);
        worldLayout[0][1] = new Room(Room.RoomType.ELEVATOR, 0, true);
        worldLayout[0][2] = new Room(Room.RoomType.EXTRACTION, 0, true);
        worldLayout[0][3] = null;
        worldLayout[0][4] = null;
        worldLayout[0][5] = null;
        worldLayout[0][6] = null;
        worldLayout[0][7] = null;
        //row 1: underground depth 1
        worldLayout[1][0] = new Room(Room.RoomType.LEVEL, 1, true);
        worldLayout[1][1] = new Room(Room.RoomType.ELEVATOR, 1, true);
        worldLayout[1][2] = new Room(Room.RoomType.LEVEL, 0, true);
        worldLayout[1][3] = null;
        worldLayout[1][4] = null;
        worldLayout[1][5] = null;
        worldLayout[1][6] = null;
        worldLayout[1][7] = null;

        //row 2: underground depth 2
        worldLayout[2][0] = new Room(Room.RoomType.LEVEL, 2, true);
        worldLayout[2][1] = new Room(Room.RoomType.ELEVATOR, 2, true);
        worldLayout[2][2] = new Room(Room.RoomType.LEVEL, 3, true);
        worldLayout[2][3] = null;
        worldLayout[2][4] = null;
        worldLayout[2][5] = null;
        worldLayout[2][6] = null;
        worldLayout[2][7] = null;

        //row 3: underground depth 3
        worldLayout[3][0] = new Room(Room.RoomType.LEVEL, 4, true);
        worldLayout[3][1] = new Room(Room.RoomType.ELEVATOR, 3, true);
        worldLayout[3][2] = new Room(Room.RoomType.LEVEL, 5, true);
        worldLayout[3][3] = null;
        worldLayout[3][4] = null;
        worldLayout[3][5] = null;
        worldLayout[3][6] = null;
        worldLayout[3][7] = null;

        //row 4: underground depth 4
        worldLayout[4][0] = null;
        worldLayout[4][1] = new Room(Room.RoomType.ELEVATOR, 4, true);
        worldLayout[4][2] = null;
        worldLayout[4][3] = null;
        worldLayout[4][4] = null;
        worldLayout[4][5] = null;
        worldLayout[4][6] = null;
        worldLayout[4][7] = null;


        //@todo add random levels, select from 8 different levels, shuffle them throughtout the map
    }

    public void initializeTutorialLayout(){
        //row 0: surface
        worldLayout[0][0] = new Room(Room.RoomType.TUTORIAL, 0, true);
        worldLayout[0][0].setTutorialText("welcome to impossible mission! (? kinda?) - press WASD and SPACE to move around :)");

        worldLayout[0][1] = new Room(Room.RoomType.TUTORIAL, 1, true);
        worldLayout[0][1].setTutorialText("your objective is to collect poker cards and extract: higher values = more points");

        worldLayout[0][2] = new Room(Room.RoomType.TUTORIAL, 2, true);
        worldLayout[0][2].setTutorialText("however... evil forces will try to hold you back! use the computer to deactivate them if needed.");

        worldLayout[0][3] = new Room(Room.RoomType.TUTORIAL, 3, true);
        worldLayout[0][3].setTutorialText("certain card combinations give more points! just like poker... ");

        worldLayout[0][4] = new Room(Room.RoomType.TUTORIAL, 4, true);
        worldLayout[0][5] = new Room(Room.RoomType.TUTORIAL, 5, true);
        worldLayout[0][6] = new Room(Room.RoomType.TUTORIAL, 6, true);
        worldLayout[0][7] = new Room(Room.RoomType.TUTORIAL, 7, true);
//        loadCurrentRoom();
    }






    //----------------------------------------------------------------------------------------------------------------//
    // MOVE TO LEFT / RIGHT / TOP / BOTTOM ROOM
    //----------------------------------------------------------------------------------------------------------------//

    //@todo: MAKE IT THAT ONCE A ROOM IS VIISTED ( LEVEL ) YOU CANT GO BACK IN. NO BACKTRACKING! ONLY MAKE IT POSSIBLE IF THE RIGHT ROOM IS ELEVATOR
    public void moveToLeftRoom(){
        Room currentRoom = getCurrentRoom();
        if (currentWorldCol > 0 && worldLayout[currentWorldRow][currentWorldCol - 1] != null) {
            currentWorldCol--;
            System.out.println(" moveToLeftRoom(): moving to left room, marking as visited");
            currentRoom.isVisited = true;
            loadCurrentRoom();

        }
    }

    public void moveToRightRoom(){
        Room currentRoom = getCurrentRoom();
        if (currentWorldCol < 7  && worldLayout[currentWorldRow][currentWorldCol + 1] != null ) {
            currentWorldCol++;
            System.out.println(" moveToRightRoom(): moving to right room, marking as visited");
            currentRoom.isVisited = true;
            loadCurrentRoom();

        }
    }

    public void moveToUpRoom(){
        Room currentRoom = getCurrentRoom();
        if (currentWorldRow > 0  && worldLayout[currentWorldRow - 1][currentWorldCol] != null ) {
            currentWorldRow--;

            currentRoom.isVisited = true;
            loadCurrentRoom();

        }
    }

    public void moveToDownRoom(){
        Room currentRoom = getCurrentRoom();
        if (currentWorldRow < 4  && worldLayout[currentWorldRow + 1][currentWorldCol] != null ) {
            currentWorldRow++;

            currentRoom.isVisited = true;
            loadCurrentRoom();

        }
    }


    //----------------------------------------------------------------------------------------------------------------//
    // ROOM LOADER
    //----------------------------------------------------------------------------------------------------------------//
    public void loadCurrentRoom() {
        Room currentRoom = getCurrentRoom();
        String roomPath = currentRoom.getRoomPath();

//        loadBackground(currentRoom); //load the background
        loadRoomData(roomPath); //load room data


        setChanged();
        notifyObservers(getCurrentRoom());
    }

    //----------------------------------------------------------------------------------------------------------------//
    // ROOM DATA LOADER
    //----------------------------------------------------------------------------------------------------------------//
    private void loadRoomData(String roomPath) {
        try{
            //clear enemies and interactable objects, then load, to avoid reloading more enemies/interactable objects.
            getCurrentRoom().clearDrones();
            getCurrentRoom().clearDogs();
            getCurrentRoom().clearInteractableObjects();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(roomPath);        //import the text file
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));     //reads the text file content
            for (int row = 0; row < ScreenSettings.MAX_SCREEN_ROW; row++) {
                String line = bufferedReader.readLine();
                if (line == null) { break; }
                else {processLine(line, row);}  //this is where the magic happens jesus fucking christ
            }
            bufferedReader.close();
        }
        catch(Exception e){    //
            e.printStackTrace();
        }

        setChanged();
        notifyObservers("level changed");
        clearChanged();
    }

    private void processLine(String line, int row) {
        int col = 0;

        String[] numbers = line.split(" ");
        for (String number : numbers) {
            if (col >= ScreenSettings.MAX_SCREEN_COL) {break;}

            try{
                int num = Integer.parseInt(number);
                int tileX = col * ScreenSettings.TILE_SIZE;
                int tileY = row * ScreenSettings.TILE_SIZE;
                if (InteractableObjectID.isInteractableObject(num)) {
                    placeObject(getCurrentRoom(), num, tileX, tileY);
                    //set tile to be an empty space
                    currentRoomData[row][col] = 0;

                }
                else {//if the num is > 0, then it is a regular tile
                    currentRoomData[row][col] = num;
                }

                //SECOND PART: PLACE THE OBJECTS/TILES BASED ON THE MARKER.
            } catch (NumberFormatException e) {
                //check for markers based objects (interactable objects)
                char marker = number.charAt(0);
                int tileX = col * ScreenSettings.TILE_SIZE;
                int tileY = row * ScreenSettings.TILE_SIZE;

                Room currentRoom = getCurrentRoom();
                switch (marker) {
                    //----------------------------------------------------------------------------------------------------------------//
                    // ADDING INTERACTABLE OBJECTS 1.
                    //----------------------------------------------------------------------------------------------------------------//
                    case 'b':
                        placeObject(currentRoom, InteractableObjectID.OBJECT_BOX,tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;
                    case 'R':
                        placeObject(currentRoom, InteractableObjectID.OBJECT_REDBOX,tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;
                    case 'M':
                        placeObject(currentRoom, InteractableObjectID.OBJECT_METALLOCKER,tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;
                    case 'W':
                        placeObject(currentRoom, InteractableObjectID.OBJECT_WOODLOCKER, tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;
                    case 'c':
                        placeObject(currentRoom, InteractableObjectID.OBJECT_CARD,tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;
                    case 'L':
                        placeObject(currentRoom, InteractableObjectID.OBJECT_LADDER,tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;
                    case 'C':
                        placeObject(currentRoom, InteractableObjectID.OBJECT_COMPUTER,tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;


                        //ENEMIES

                    case 'D':
                        placeDrone(currentRoom, tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;
                    case 'd':
                        placeDog(currentRoom, tileX, tileY);
                        currentRoomData[row][col] = 0;
                        break;


                    default:
                        // if not a recognized object marker treat as empty space
                        currentRoomData[row][col] = 0;
                        break;
                    }
                }
            col++;
        }
    }


    //----------------------------------------------------------------------------------------------------------------//
    // PRINT WORLD LAYOUT TO CONSOLE
    //----------------------------------------------------------------------------------------------------------------//

    public void printWorld() {
        System.out.println("\nGAME WORLD STRUCTURE:");
        for (int row = 0; row < 5; row++) {
            StringBuilder line = new StringBuilder("Row " + row + ": ");
            for (int col = 0; col < 8; col++) {
                if (worldLayout[row][col] == null) {
                    line.append(" . ");
                    continue;
                }

                // use room to string method
                String symbol = worldLayout[row][col].toString();

                // highlight the current position
                if (row == currentWorldRow && col == currentWorldCol) {
                    line.append("[").append(symbol).append("] ");
                }
                else {
                    line.append(" ").append(symbol).append("  ");
                }
            }
            System.out.println(line);
        }
    }

    //----------------------------------------------------------------------------------------------------------------//
    // ADDING INTERACTABLE OBJECTS 2.  INSTANTIATE THE NEW OBJECTS
    //----------------------------------------------------------------------------------------------------------------//
    private void placeObject(Room currentRoom, int objectType, int x, int y){
            switch (objectType) {
                case InteractableObjectID.OBJECT_BOX:
                    PaperBox box = new PaperBox(x, y);
                    currentRoom.addInteractableObject(box);
                    break;
                case InteractableObjectID.OBJECT_REDBOX:
                    RedBox redBox = new RedBox(x, y);
                    currentRoom.addInteractableObject(redBox);
                    break;
                case InteractableObjectID.OBJECT_METALLOCKER:
                    MetalLocker metalLocker = new MetalLocker(x, y);
                    currentRoom.addInteractableObject(metalLocker);
                    break;
                case InteractableObjectID.OBJECT_WOODLOCKER:
                    WoodLocker woodLocker = new WoodLocker(x, y);
                    currentRoom.addInteractableObject(woodLocker);
                    break;
                case InteractableObjectID.OBJECT_CARD:
                    Card card = new Card(x, y);
                    currentRoom.addInteractableObject(card);
                    break;
                case InteractableObjectID.OBJECT_LADDER:
                    Ladder ladder = new Ladder(x, y);
                    currentRoom.addInteractableObject(ladder);
                    break;
                case InteractableObjectID.OBJECT_COMPUTER:
                    Computer computer = new Computer(x, y);
                    currentRoom.addInteractableObject(computer);
                    break;
                }
    }

    /**
     * handles the level transition, wherever the {@link Player} is going
     */
    public void handleLevelTransition() {
        //check if player is near right edge and moving right to transition to the next level
        if (player.getX() >= ScreenSettings.SCREEN_WIDTH - player.getWidth() - 5 && player.getDirection().equals("right")) {
            moveToRightRoom();

            player.setX(0); //reset player to the left side of the room
//            printWorld();
        }
        //check if player is near left edge and moving to the left, transition to next level
        if (player.getX() <= 5 && player.getDirection().equals("left")) {
            moveToLeftRoom();

            player.setX(ScreenSettings.SCREEN_WIDTH - player.getWidth() - 10); //reset player to the right side of the room
//            printWorld();
        }

        if(player.getY() >= ScreenSettings.SCREEN_HEIGHT - player.getHeight() - 5 && player.getDirection().equals("down")) {
            moveToDownRoom();

            player.setY(player.height - 5);
//            printWorld();
        }

        if (player.getY() <= 5 && player.getDirection().equals("up")) {
            moveToUpRoom();
            player.setY(ScreenSettings.SCREEN_HEIGHT - player.getHeight() - 5);
//            printWorld();
        }
    }
    //----------------------------------------------------------------------------------------------------------------//
    // ENEMY RELATED
    //----------------------------------------------------------------------------------------------------------------//
    /**
     * @param currentRoom current room where {@link Player} is
     * @param x x coordinate to place the {@link Drone}
     * @param y y coordinate to place the {@link Drone}
     */
    private void placeDrone(Room currentRoom, int x, int y) {
        Drone drone = new Drone(x, y, player, this);
        currentRoom.addDrone(drone);
//        System.out.println("[LevelManager][placeDrone()] :  placed drone");
    }

    /**
     * @param currentRoom current room where {@link Player} is
     * @param x x coordinate to place the {@link Dog}
     * @param y y coordinate to place the {@link Dog}
     */
    private void placeDog(Room currentRoom, int x, int y) {
        Dog dog = new Dog(x, y, player, this);
        currentRoom.addDog(dog);
//        System.out.println("[LevelManager][placeDog()] :  placed dog");
    }


    /**
     * @return the current which the {@link Player} is in
     */
    public Room getCurrentRoom() {
        if (worldLayout[currentWorldRow][currentWorldCol] == null) {
//            System.out.println("[LevelManager]getCurrentRoom() : room is null ar row: " + currentWorldRow + " col: " + currentWorldCol);
        };
        return worldLayout[currentWorldRow][currentWorldCol];
    }

    /**
     * @return this instance of the LevelManager
     */
    public LevelManager getLevelManager() {
        return this;
    }

    /**
     * @return {@link #currentWorldRow} the row, which is basically the depth in the game world
     */
    public int getCurrentDepth(){
        return currentWorldRow;
    }

    /**
     * @return {@link #currentWorldCol}
     */
    public int getCurrentWorldCol(){
        return currentWorldCol;
    }

    /**
     * @return {@link #currentRoomData}
     */
    public int[][] getCurrentRoomData() {
        return currentRoomData;
    }

    /**
     * @param tileNum tile number
     * @return {@link Tile} object that has that tile number
     */
    public Tile getTile(int tileNum){
        return tileManager.getTile(tileNum);
    }

    /**
     * @return current {@link #worldLayout}
     */
    public Room[][] getWorldLayout() {
        return worldLayout;
    }





}
