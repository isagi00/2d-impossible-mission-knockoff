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
 * model, the class initializes the game world / tutorial layout in the {@link #worldLayout} matrix.
 * loads room data, and handles room transitions, and helps preserve room state.
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
        currentRoomData = new int[50][50];   //a room is at most 32x16 tiles. however,  i am generating the matrix to be slightly bigger because there could be
        //some unhandled edge cases when player changes room. did this to prevent index out of bounds errors...

        worldLayout[0][0] = new Room(Room.RoomType.GROUND, 0, true);     //init a default room when initializing, otherwise it gives a null room (for some reason)
    }

    /**
     * initializes the game world in the {@link #worldLayout}.
     * unimplemented mechanic: generate random rooms for different slots
     */
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
    }

    /**
     * initializes the {@link #worldLayout} matrix with the tutorial levels.
     */
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


    /**
     * loads the current room data. heavy work is done by {@link #loadRoomData(String)}, which loads tiles, objects and enemies of the room.
     */
    //----------------------------------------------------------------------------------------------------------------//
    // ROOM LOADER
    //----------------------------------------------------------------------------------------------------------------//
    public void loadCurrentRoom() {
        Room currentRoom = getCurrentRoom();
        String roomPath = currentRoom.getRoomPath();
        loadRoomData(roomPath); //load room data, also notifies observers
    }

    /**loads the room data: reads the text file contents, and based on the marker, it places the interactable object, enemies or tiles,
     * only if that specific instance of the room has not been initialized: allows persistent room state tracking, so that
     * does not reload opened interactable objects / disabled enemies
     * @param roomPath path of the room .txt file
     */
    private void loadRoomData(String roomPath) {
        try {
            //clear everything in the room
            Room currentRoom = getCurrentRoom();
            if (!currentRoom.isInitialized){    //if not been initialized then start from a clean slate
                currentRoom.clearAllObjects();
            }
            //read the file
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(roomPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            for (int row = 0; row < ScreenSettings.MAX_SCREEN_ROW; row++) { //for text file each row
                String line = bufferedReader.readLine();    //.txt line
                if (line == null) break;

                String[] numbers = line.split(" "); //text file line numbers
                for (int col = 0; col < numbers.length && col < ScreenSettings.MAX_SCREEN_COL; col++) { //text file cols
                    String number = numbers[col];   //single number
                    if (number.isEmpty()) continue;
                    //calculate where the starting x and y of each time
                    int tileX = col * ScreenSettings.TILE_SIZE;
                    int tileY = row * ScreenSettings.TILE_SIZE;

                    char marker = number.charAt(0); //get the first char of the string: if the string is an integer, then just falls down to the default case and just load the tile number
                    switch (marker) {
                        //load the interactable objects
                        case 'b':
                            if (!currentRoom.isInitialized) {   //if it has not been previously initialized, then creaate the new object, otherwise no need to create new instances
                                currentRoom.addInteractableObject(new PaperBox(tileX, tileY));
                                currentRoomData[row][col] = 0;
                                break;
                            }
                        case 'R':
                            if (!currentRoom.isInitialized) {
                                currentRoom.addInteractableObject(new RedBox(tileX, tileY));
                                currentRoomData[row][col] = 0;
                                break;
                            }
                        case 'M':
                            if (!currentRoom.isInitialized) {
                                currentRoom.addInteractableObject(new MetalLocker(tileX, tileY));
                                currentRoomData[row][col] = 0;
                                break;
                            }
                        case 'W':
                            if (!currentRoom.isInitialized) {
                                currentRoom.addInteractableObject(new WoodLocker(tileX, tileY));
                                currentRoomData[row][col] = 0;
                                break;
                            }
                        case 'c':
                            if (!currentRoom.isInitialized) {
                                currentRoom.addInteractableObject(new Card(tileX, tileY));
                                currentRoomData[row][col] = 0;
                                break;
                            }
                        case 'L':
                            if (!currentRoom.isInitialized) {
                                currentRoom.addInteractableObject(new Ladder(tileX, tileY));
                                currentRoomData[row][col] = 0;
                                break;
                            }
                        case 'C':
                            if (!currentRoom.isInitialized) {
                                currentRoom.addInteractableObject(new Computer(tileX, tileY));
                                currentRoomData[row][col] = 0;
                                break;
                            }

                        //load the enemies
                        case 'D':
                            if (!currentRoom.isInitialized) {
                                currentRoom.addDrone(new Drone(tileX, tileY, player, this));
                                currentRoomData[row][col] = 0;
                                break;
                            }
                        case 'd':
                            if (!currentRoom.isInitialized) {
                                currentRoom.addDog(new Dog(tileX, tileY, player, this));
                                currentRoomData[row][col] = 0;
                                break;
                            }
                        //load the tiles
                        default:
                            try {
                                //load the tiles regardless if the room has been initialized or not
                                int tileNum = Integer.parseInt(number);
                                currentRoomData[row][col] = tileNum;
                            }
                            catch (NumberFormatException e) {
                                currentRoomData[row][col] = 0;
                            }
                            break;
                    }
                }
            }
            bufferedReader.close();

            if (!currentRoom.isInitialized) {   //mark the room as initialized after first visit
                currentRoom.isInitialized = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setChanged();
        notifyObservers("level changed");
        clearChanged();
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
