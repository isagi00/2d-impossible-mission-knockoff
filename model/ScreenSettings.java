package model;

/**
 * !VERY! important model, only contains field regarding the game screen size.
 * since the game depends on x and y calculations, MODIFYING THESE NUMBERS WILL BREAK THE GAME LOGIC.
 */
public class ScreenSettings {

    /**
     * original tile size (16 pixels)
     */
    public static final int ORIGINAL_TILE_SIZE = 16;   //16x16 pixels
    /**
     * scaling factor of the original tile size (3)
     */
    public static final int SCALE = 3;
    /**
     * real tile size (shown on screen) (48x48 px tiles)
     */
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;     //48x48 pixels

    /**
     * total cols allowed in width  (32 cols)
     */
    public static final int MAX_SCREEN_COL = 32;    //64a tiles wide
    /**
     * total rows allowed in height (16 rows)
     */
    public static final int MAX_SCREEN_ROW = 16;     //16 tiles high
    /**
     * total screen width (in pixels) based on the maximum screen cols * tile size. (1536 px)
     * {@link #MAX_SCREEN_COL}, {@link #TILE_SIZE}
     */
    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;  //1536

    /**
     * total screen height (in pixels) based on the maximum screen rows * tile size. (768 px)
     * {@link #MAX_SCREEN_ROW}, {@link #TILE_SIZE}
     */
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE; //768

}
