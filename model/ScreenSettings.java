package model;

public class ScreenSettings {

    public static final int ORIGINAL_TILE_SIZE = 16;   //16x16 pixels
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;     //48x48 pixels

    public static final int MAX_SCREEN_COL = 32;    //64a tiles wide
    public static final int MAX_SCREEN_ROW = 16;     //16 tiles high
    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;      //32 * 48 =
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

}
