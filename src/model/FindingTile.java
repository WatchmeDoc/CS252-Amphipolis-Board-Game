package model;

public abstract class FindingTile extends Tile {
    /**
     * Amphora Tiles row on ArrayLists.
     */
    public static final int AMPHORA_TILES = 0;
    /**
     * Mosaic Tiles row on ArrayLists.
     */
    public static final int MOSAIC_TILES = 1;
    /**
     * Statue Tiles row on ArrayLists.
     */
    public static final int STATUE_TILES = 2;
    /**
     * Skeleton Tiles row on ArrayLists.
     */
    public static final int SKELETON_TILES = 3;
    /**
     * Number of Amphora Colors.
     */
    public static final int AMPHORA_COLORS = AmphoraColor.values().length;
    /**
     * Number of Mosaic Colors.
     */
    public static final int MOSAIC_COLORS = MosaicColor.values().length;

}

class AmphoraTile extends FindingTile {
    /**
     * An enum attribute that matches this AmphoraTile's color.
     */
    private final AmphoraColor color;

    /**
     * Constructor of AmphoraTile class.
     * @param color can either be AmphoraColor.BLUE, AmphoraColor.BROWN, AmphoraColor.RED, AmphoraColor.GREEN, AmphoraColor.YELLOW or AmphoraColor.PURPLE
     * @pre color must be initialized and not NULL
     * @post AmphoraColor attribute must match the one given in arguments.
     */
    AmphoraTile(AmphoraColor color){
        this.Type = "AmphoraTile";
        this.tile_name = "amphora_" + color.toString().toLowerCase();
        this.color = color;
    }

    /**
     * Getter for the color attribute
     * @post must not change color attribute.
     * @return AmphoraTile color attribute.
     */
    public AmphoraColor getColor(){
        return this.color;
    }

}


class MosaicTile extends FindingTile{
    /**
     * An enum attribute that matches this MosaicTile's color.
     */
    private final MosaicColor color;

    /**
     * Constructor of AmphoraTile class.
     * @param color can either be MosaicColor.GREEN, MosaicColor.RED or MosaicColor.YELLOW
     * @pre color must be initialized and not NULL
     * @post MosaicColor attribute must match the one given in arguments.
     */
    MosaicTile(MosaicColor color){
        this.Type = "MosaicTile";
        this.color = color;
        this.tile_name = "mosaic_" + color.toString().toLowerCase();
    }
    /**
     * Getter for the color attribute
     * @post must not change color attribute.
     * @return MosaicTile color attribute.
     */
    public MosaicColor getColor(){
        return this.color;
    }

}

class SkeletonTile extends FindingTile{
    /**
     * Boolean attribute that evaluates to True if this SkeletonTile is Upper Body.
     */
    private final boolean isUpperBody;

    /**
     * Boolean attribute that evaluates to True if this SkeletonTile is from an Adult Skeleton.
     */
    private final boolean isAdultSkeleton;

    /**
     * Constructor for a SkeletonTile type of Tile.
     * @param isUpperBody is True if this SkeletonTile is from the upper body of a skeleton, else False.
     * @param isAdultSkeleton is True if this SkeletonTile is from an adult skeleton, else False.
     */
    SkeletonTile(boolean isUpperBody, boolean isAdultSkeleton){
        this.Type = "SkeletonTile";
        this.isUpperBody = isUpperBody;
        this.isAdultSkeleton = isAdultSkeleton;
        StringBuilder tile_name = new StringBuilder("skeleton_");

        if(isAdultSkeleton){
            tile_name.append("big_");
        } else{
            tile_name.append("small_");
        }
        if(isUpperBody){
            tile_name.append("top");
        } else{
            tile_name.append("bottom");
        }
        this.tile_name = tile_name.toString();
    }

    /**
     * Getter for the isUpperBody attribute.
     * @return True if this SkeletonTile is an Upper Body one, otherwise False.
     */
    public boolean getIsUpperBody(){
        return this.isUpperBody;
    }

    /**
     * Getter for the isAdultSkeleton attribute.
     * @return True if this SkeletonTile is from an Adult Skeleton, otherwise False.
     */
    public boolean getIsAdultSkeleton(){
        return this.isAdultSkeleton;
    }

}

abstract class StatueTile extends FindingTile {
    /**
     * Default constructor that tile_name attribute.
     */
    StatueTile(){
        this.Type = "StatueTile";
    }
}

class SphinxTile extends StatueTile {
    /**
     * Default constructor that initializes tile_name attribute.
     */
    SphinxTile(){
        this.tile_name = "sphinx";
    }

}

class CaryatidTile extends StatueTile {
    /**
     * Default constructor that tile_name attribute.
     */
    CaryatidTile(){
        this.tile_name = "caryatid";
    }
    
}