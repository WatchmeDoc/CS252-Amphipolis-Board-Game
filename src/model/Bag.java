package model;

import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Bag {
    /**
     * Bag is a singleton class, therefore there is only 1 instance of it.
     */
    private static Bag instance = null;

    /**
     * Indicates the number of Landslide Tiles in the bag.
     */
    static final int LANDSLIDE_TILES = 24;
    /**
     * Indicates the number of Amphora Tiles per color in the bag.
     */
    static final int AMPHORA_TILES_PER_COLOR = 5;
    /**
     * Indicates the number of Mosaic Tiles per color in the bag.
     */
    static final int MOSAICS_PER_COLOR = 9;
    /**
     * Indicates the number of Statue Tiles per category in the bag.
     */
    static final int STATUES_PER_CATEGORY = 12;
    /**
     * Indicates the number of Adult Skeleton Tiles of both upper and lower parts in the bag.
     */
    static final int ADULT_SKELETON_PARTS = 10;
    /**
     * Indicates the number of Child Skeleton Tiles of both upper and lower parts in the bag.
     */
    static final int CHILD_SKELETON_PARTS = 5;
    /**
     * ArrayList containing all tiles currently in the bag.
     */
    private final ArrayList<Tile> tiles = new ArrayList<>();
    /**
     * Indicates the number of Landslide Tiles that will be pre placed in single player mode.
     */
    public static final int SINGLE_PLAYER_ENTRANCE_TILES = 8;
    /**
     * First tiles that the board will be initialized with.
     */
    private final FindingTile[] first_tiles = new FindingTile[Board.SORTING_AREAS_COUNT];
    /**
     * Private constructor for singleton class Bag
     * @post LANDSLIDE TILES MUST BE AT THE VERY FIRST POSITIONS OF ARRAYLIST tiles
     */
    private Bag(){
        for(int i  = 0; i < LANDSLIDE_TILES; i++){
            tiles.add(new LandslideTile());
        }

        for(AmphoraColor color: AmphoraColor.values()){
            for(int i = 0; i < AMPHORA_TILES_PER_COLOR; i++){
                tiles.add(new AmphoraTile(color));
            }
        }
        first_tiles[0] = (FindingTile) tiles.get(tiles.size() - 1);
        tiles.remove(first_tiles[0]);
        for(MosaicColor color: MosaicColor.values()){
            for(int i = 0; i < MOSAICS_PER_COLOR; i++){
                tiles.add(new MosaicTile(color));
            }
        }
        first_tiles[1] = (FindingTile) tiles.get(tiles.size() - 1);
        tiles.remove(first_tiles[1]);
        int index = tiles.size();
        for(int i = 0; i < STATUES_PER_CATEGORY; i++){
            tiles.add(index, new SphinxTile());
            tiles.add(new CaryatidTile());
        }
        first_tiles[2] = (FindingTile) tiles.get(index);
        tiles.remove(first_tiles[2]);
        for(int i = 0; i < ADULT_SKELETON_PARTS; i++){
            tiles.add(index, new SkeletonTile(false, true));
            tiles.add(index, new SkeletonTile(true, true));
        }
        for(int i = 0; i < CHILD_SKELETON_PARTS; i++){
            tiles.add(index, new SkeletonTile(false, false));
            tiles.add(index, new SkeletonTile(true, false));
        }
        first_tiles[3] = (FindingTile) tiles.get(index);
        tiles.remove(first_tiles[3]);
    }

    /**
     * Gets a random tile from the bag.
     * @pre tiles.size() != 0
     * @return a random tile from the bag.
     * @throws IllegalActionException if the bag is empty.
     */
    public Tile getTile(){
        if(tiles.size() == 0){
            throw new IllegalActionException("Cannot pull a tile off from an Empty Bag!");
        }
        Tile chosen;
        Random rand = new Random();
        chosen = tiles.get(rand.nextInt(tiles.size()));
        tiles.remove(chosen);
        return chosen;
    }
    /**
     * getter for Bag instance
     * @return Single Bag instance
     */
    public static Bag getInstance() {
        if(instance == null){
            instance = new Bag();
        }
        return instance;
    }

    /**
     * Fills the entrance with single player entrance tiles count.
     * @return LandslideTile Array with the placed LandslideTiles.
     */
    public LandslideTile[] entrance_fillings(){
        LandslideTile[] rocks = new LandslideTile[SINGLE_PLAYER_ENTRANCE_TILES];
        for(int i = 0; i < SINGLE_PLAYER_ENTRANCE_TILES; i++){
            rocks[i] = (LandslideTile) tiles.get(0);
            tiles.remove(0);
        }
        return rocks;
    }

    /**
     * Getter for the first tiles which initialize the board.
     * @return
     */
    public FindingTile[] getFirstTiles(){
        return first_tiles;
    }

    /**
     * Loads given tiles from a JSON Object.
     * @param sorting_areas JSONObject containing every FindingTile and an Integer number, the amount of the specific tile type
     *                      that will be returned from the bag.
     * @return ArrayList with FindingTiles, removed from the bag
     */
    public ArrayList<FindingTile> load_tiles(JSONObject sorting_areas){
        ArrayList<FindingTile> early_tiles = new ArrayList<>(0);
        String[] tile_names = {"mosaic_green", "mosaic_red", "mosaic_yellow","caryatid", "sphinx" ,"amphora_blue", "amphora_brown",
                "amphora_green", "amphora_purple", "amphora_red", "amphora_yellow",  "skeleton_big_top", "skeleton_small_top",
                "skeleton_big_bottom", "skeleton_small_bottom"};
        for(String name: tile_names){
            tiles.stream().filter(tile -> tile.getTileName().equals(name))
            .findFirst().ifPresent(tile -> {
                int N = ((Long) sorting_areas.get(tile.getTileName())).intValue();
                for(int i = 0; i < N; i++){
                    early_tiles.add((FindingTile) tiles.stream().filter(tile1 -> tile1.getTileName().equals(name)).findFirst().get());
                    tiles.remove(early_tiles.get(early_tiles.size() - 1));
                }
            });

        }

        return early_tiles;
    }

    /**
     * Resets the bag.
     */
    public void reset(){
        tiles.clear();
        for(int i  = 0; i < LANDSLIDE_TILES; i++){
            tiles.add(new LandslideTile());
        }

        for(AmphoraColor color: AmphoraColor.values()){
            for(int i = 0; i < AMPHORA_TILES_PER_COLOR; i++){
                tiles.add(new AmphoraTile(color));
            }
        }
        first_tiles[0] = (FindingTile) tiles.get(tiles.size() - 1);
        tiles.remove(first_tiles[0]);
        for(MosaicColor color: MosaicColor.values()){
            for(int i = 0; i < MOSAICS_PER_COLOR; i++){
                tiles.add(new MosaicTile(color));
            }
        }
        first_tiles[1] = (FindingTile) tiles.get(tiles.size() - 1);
        tiles.remove(first_tiles[1]);
        int index = tiles.size();
        for(int i = 0; i < STATUES_PER_CATEGORY; i++){
            tiles.add(index, new SphinxTile());
            tiles.add(new CaryatidTile());
        }
        first_tiles[2] = (FindingTile) tiles.get(index);
        tiles.remove(first_tiles[2]);
        for(int i = 0; i < ADULT_SKELETON_PARTS; i++){
            tiles.add(index, new SkeletonTile(false, true));
            tiles.add(index, new SkeletonTile(true, true));
        }
        for(int i = 0; i < CHILD_SKELETON_PARTS; i++){
            tiles.add(index, new SkeletonTile(false, false));
            tiles.add(index, new SkeletonTile(true, false));
        }
        first_tiles[3] = (FindingTile) tiles.get(index);
        tiles.remove(first_tiles[3]);
    }
}
