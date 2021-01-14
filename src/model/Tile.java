package model;

public abstract class Tile {
    /**
     * A boolean value that is true if this tile is Landslide Tile that will be initialized by the classes extending Tile.
     */
    protected String Type;
    protected String tile_name;
    /**
     * Getter for isLandslideTile attribute.
     * @post Must not change isLandslideTile value.
     * @return True if this Tile is a Landslide Tile, False if it's not.
     */
    public String getType(){
        return Type;
    }
    public String getTileName(){return tile_name;}

}
