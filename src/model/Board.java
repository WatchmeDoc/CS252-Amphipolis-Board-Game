package model;


import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static model.FindingTile.*;


public class Board {
    /**
     * Singleton class, therefore its single instance is an attribute.
     */
    private static Board instance = null;
    /**
     * Entrance array that contains all placed Landslide tiles.
     */
    private LandslideTile[] Entrance;
    /**
     * Arraylist of 4 ArrayLists for each Finding tile category.
     * For each category there is a specified TYPE_TILES final static variable.
     */
    private static ArrayList<ArrayList<FindingTile>> SortingAreas;
    /**
     * Maximum allowed players.
     * @inv MAX_PLAYERS == CharacterColor.values().length;
     */
    public static final int MAX_PLAYERS = CharacterColor.values().length;
    /**
     * Maximum entrance tiles.
     * @inv MAX_ENTRANCE_TILES less or equal to Bag.LANDSLIDE_TILES.
     */
    public static final int MAX_ENTRANCE_TILES = 16;
    /**
     * Tiles placed in the sorting areas per player move.
     */
    public static final int TILES_PER_ROUND = 4;
    /**
     * int that counts entrance's placed tiles.
     */
    private int EntranceTiles;
    /**
     * Sorting Areas count
     */
    public final static int SORTING_AREAS_COUNT = 4;
    /**
     * int that shows which player is currently playing from the players ArrayList.
     */
    private int NowPlaying = 0;
    /**
     * ArrayList with registered players. The play priority is the same with their registration order.
     * @inv players.size() less or equal to MAX_PLAYERS.
     */
    private final ArrayList<Player> players = new ArrayList<Player>(0);
    /**
     * Bag Instance connected with the board.
     */
    private final Bag bag = Bag.getInstance();
    /**
     * Another player, a more suspicious one... But what's his purpose? Why is he doing all those... things?
     * How can someone summon thee?
     */
    private Player Thief;
    /**
     * Constructor for singleton class, therefore is private.
     */
    private Board(){
        Entrance = new LandslideTile[MAX_ENTRANCE_TILES];
        SortingAreas = new ArrayList<> (0);
        for(int i = 0; i < SORTING_AREAS_COUNT; i++){
            SortingAreas.add(new ArrayList<>(0));
        }
        EntranceTiles = 0;
    }

    /**
     * Getter for Board instance.
     * @return Board Instance.
     */
    public static Board getInstance(){
        if(instance == null){
            instance = new Board();
        }
        return instance;
    }

    /**
     * Overloaded method that adds a tile to the proper area.
     * @pre input is not NULL.
     * @post input will be saved on SortingAreas ArrayList
     * @param input an AmphoraTile that will be placed on board.
     * @throws NullPointerException if input is NULL.
     */
    public void addTile(AmphoraTile input) throws NullPointerException{
        if(input == null){
            throw new NullPointerException("Null Tile given.");
        }
        SortingAreas.get(AMPHORA_TILES).add(input);
    }
    /**
     * Overloaded method that adds a tile to the proper area.
     * @pre input is not NULL.
     * @post input will be saved on SortingAreas ArrayList
     * @param input a MosaicTile that will be placed on board.
     * @throws NullPointerException if input is NULL.
     */
    public void addTile(MosaicTile input) throws NullPointerException{
        if(input == null){
            throw new NullPointerException("Null Tile given.");
        }
        SortingAreas.get(MOSAIC_TILES).add(input);
    }
    /**
     * Overloaded method that adds a tile to the proper area.
     * @pre input is not NULL.
     * @post input will be saved on SortingAreas ArrayList
     * @param input a StatueTile that will be placed on board.
     * @throws NullPointerException if input is NULL.
     */
    public void addTile(StatueTile input) throws NullPointerException{
        if(input == null){
            throw new NullPointerException("Null Tile given.");
        }
        SortingAreas.get(STATUE_TILES).add(input);
    }
    /**
     * Overloaded method that adds a tile to the proper area.
     * @pre input is not NULL.
     * @post input will be saved on SortingAreas ArrayList
     * @param input a SkeletonTile that will be placed on board.
     * @throws NullPointerException if input is NULL.
     */
    public void addTile(SkeletonTile input) throws NullPointerException{
        if(input == null){
            throw new NullPointerException("Null Tile given.");
        }
        SortingAreas.get(SKELETON_TILES).add(input);
    }
    /**
     * Overloaded method that adds a Landslide tile to the entrance area.
     * @pre input is not NULL AND EntranceTiles less than MAX_ENTRANCE_TILES
     * @post input will be saved on Entrance Array.
     * @param input an EntranceTile that will be placed on the Entrance.
     * @throws NullPointerException if input is NULL.
     */
    public boolean addTile(LandslideTile input) throws NullPointerException{
        if(input == null){
            throw new NullPointerException("Null Tile given.");
        }
        if(EntranceTiles == MAX_ENTRANCE_TILES){
            return false;
        }
        Entrance[EntranceTiles++] = input;
        return true;
    }

    /**
     * Overloaded method that takes a tile off the board.
     * @pre AmphoraTile must not be null.
     * @post must remove the chosen FindingTile from the ArrayList.
     * @param chosen instance of the tile we want to remove from the board.
     * @throws NullPointerException if chosen == NULL.
     * @throws TileNotFoundException if chosen Tile is not on board.
     */
    public void removeTile(AmphoraTile chosen){
        if(chosen == null){
            throw new NullPointerException("Null Tile given.");
        }
        if(!SortingAreas.get(AMPHORA_TILES).remove(chosen)){
            throw new TileNotFoundException();
        }
    }
    /**
     * Overloaded method that takes a tile off the board.
     * @pre MosaicTile must not be null.
     * @post must remove the chosen FindingTile from the ArrayList.
     * @param chosen instance of the tile we want to remove from the board.
     * @throws NullPointerException if chosen == NULL.
     * @throws TileNotFoundException if chosen Tile is not on board.
     */
    public void removeTile(MosaicTile chosen){
        if(chosen == null){
            throw new NullPointerException("Null Tile given.");
        }
        if(!SortingAreas.get(MOSAIC_TILES).remove(chosen)){
            throw new TileNotFoundException();
        }
    }
    /**
     * Overloaded method that takes a tile off the board.
     * @pre StatueTile must not be null.
     * @post must remove the chosen FindingTile from the ArrayList.
     * @param chosen instance of the tile we want to remove from the board.
     * @throws NullPointerException if chosen == NULL.
     * @throws TileNotFoundException if chosen Tile is not on board.
     */
    public void removeTile(StatueTile chosen){
        if(chosen == null){
            throw new NullPointerException("Null Tile given.");
        }
        if(!SortingAreas.get(STATUE_TILES).remove(chosen)){
            throw new TileNotFoundException();
        }
    }
    /**
     * Overloaded method that takes a tile off the board.
     * @pre SkeletonTile must not be null.
     * @post must remove the chosen FindingTile from the ArrayList.
     * @param chosen instance of the tile we want to remove from the board.
     * @throws NullPointerException if chosen == NULL.
     * @throws TileNotFoundException if chosen Tile is not on board.
     */
    public void removeTile(SkeletonTile chosen){
        if(chosen == null){
            throw new NullPointerException("Null Tile given.");
        }
        if(!SortingAreas.get(SKELETON_TILES).remove(chosen)){
            throw new TileNotFoundException();
        }
    }

    /**
     * Add a player to the board with name "name" and assigns player id the number of this player's registration (1-4).
     * @pre players.size less than MAX_PLAYERS and the game must not have started or ended.
     * @inv players.size less or equal to MAX_PLAYERS.
     * @post insert player to ArrayList or throw exception.
     * @param name name of the new player that will be registered. Cannot be null or empty.
     * @throws NullPointerException if name == null.
     * @throws IllegalActionException if there are already MAX_PLAYERS players on board or name.equals("").
     */
    public Player addPlayer(String name){
        Player player;
        if(name == null){
            throw new NullPointerException();
        } else if(name.equals("")){
            throw new IllegalActionException("Player's name cannot be empty!");
        }
        player = new Player(name, players.size());
        if(players.size() == MAX_PLAYERS){
            throw new IllegalActionException("Player list already full!");
        }
        players.add(player);
        if(name.equals("Fox the Thief")){
            Thief = player;
        }
        return player;
    }

    /**
     * Basic move whenever NowPlaying player is playing - picking TILES_PER_ROUND from the bag and placing them on board.
     */
    public Tile[] PlaceTiles(){
        Tile[] new_tiles = new Tile[TILES_PER_ROUND];
        for(int i = 0; i < TILES_PER_ROUND; i++){
            new_tiles[i] = null;
        }
        for(int i = 0; i < TILES_PER_ROUND; i++){
            Tile chosen;
            try{
                chosen = bag.getTile();
            } catch(IllegalActionException e){
                return new_tiles;
            }
            if(chosen.getType().equals("LandslideTile")){
                if(!addTile((LandslideTile) chosen)){
                    return new_tiles;
                }
            } else if(chosen.getType().equals("AmphoraTile")){
                addTile((AmphoraTile) chosen);
            } else if(chosen.getType().equals("MosaicTile")){
                addTile((MosaicTile) chosen);
            } else if(chosen.getType().equals("SkeletonTile")){
                addTile((SkeletonTile) chosen);
            } else {
                addTile((StatueTile) chosen);
            }
            new_tiles[i] = chosen;
        }
        return new_tiles;
    }

    /**
     * NowPlaying player takes his chosen tiles.
     * @param chosen_tile finding tiles the player chose to add to his collection.
     */
    public void pickTile(FindingTile chosen_tile){
        if (chosen_tile.getType().equals("AmphoraTile")) {
            players.get(NowPlaying).pickTile((AmphoraTile) chosen_tile);
            removeTile((AmphoraTile) chosen_tile);
        } else if (chosen_tile.getType().equals("MosaicTile")) {
            players.get(NowPlaying).pickTile((MosaicTile) chosen_tile);
            removeTile((MosaicTile) chosen_tile);
        } else if (chosen_tile.getType().equals("SkeletonTile")) {
            players.get(NowPlaying).pickTile((SkeletonTile) chosen_tile);
            removeTile((SkeletonTile) chosen_tile);
        } else {
            if (chosen_tile.getTileName().equals("sphinx")) {
                players.get(NowPlaying).pickTile((SphinxTile) chosen_tile);
            } else {
                players.get(NowPlaying).pickTile((CaryatidTile) chosen_tile);
            }
            removeTile((StatueTile) chosen_tile);
        }
    }


    /**
     * Next player in queue. Goes back to 0 if the last player finished his turn.
     */
    public void nextPlayer(){
        NowPlaying++;
        if(NowPlaying >= players.size()){
            NowPlaying = 0;
        }
    }

    /**
     * Getter for NowPlaying attribute.
     * @return NowPlaying attribute
     */
    public Player getNowPlaying(){
        return players.get(NowPlaying);
    }

    /**
     * Getter for NowPlaying player's name.
     * @return NowPlaying player's name.
     */
    public String getPlayerName(){
        return players.get(NowPlaying).getName();
    }
    /**
     * Checks if the game is over or not.
     * @return True if the Entrance is full, otherwise false.
     */
    public boolean check_entrance(){
        return EntranceTiles == MAX_ENTRANCE_TILES;
    }

    /**
     * Returns the winner of the game.
     * @pre The game must have ended.
     * @return null if its a tie, or player instance of the winner.
     * @throws IllegalActionException if the game hasn't ended.
     */
    public Player check_winner(){
        if(!check_entrance()){
            throw new IllegalActionException("Cannot find a winner yet, the game hasn't ended!");
        }
        Player max = players.get(0);
        int max_points = -1;
        for (Player player : players) {
            int tmp = player.getPoints();
            if (tmp > max_points) {
                max_points = tmp;
                max = player;
            }
        }
        for(Player player : players){
            if(player.getPoints() == max_points && player != max){
                return null;
            }
        }
        return max;
    }

    /**
     * Getter for Players' ArrayList
     * @return Players' ArrayList.
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Creates a new player, a vicious one, your childhood's greatest foe. Just tell him a few
     * times "Alepoy mi klevis" and he will probably leave, idk Dora the adventurer did it and worked. Also initializes entrance
     * with a few entrance tiles, given from the bag.
     */
    public void create_thief(){
        addPlayer("Fox the Thief");
        Thief = players.get(players.size() - 1);
        Arrays.stream(bag.entrance_fillings()).forEach(this::addTile);
    }

    /**
     * Adds a finding tile to the thief's collection.
     * @param chosen_tile A finding tile that will be added to the thief's collection.
     */
    public void stealTile(FindingTile chosen_tile){
        if(chosen_tile == null){
            return;
        }
        if (chosen_tile.getType().equals("AmphoraTile")) {
            Thief.pickTile((AmphoraTile) chosen_tile);
            removeTile((AmphoraTile) chosen_tile);
        } else if (chosen_tile.getType().equals("MosaicTile")) {
            Thief.pickTile((MosaicTile) chosen_tile);
            removeTile((MosaicTile) chosen_tile);
        } else if (chosen_tile.getType().equals("SkeletonTile")) {
            Thief.pickTile((SkeletonTile) chosen_tile);
            removeTile((SkeletonTile) chosen_tile);
        } else {
            if (chosen_tile.getTileName().equals("sphinx")) {
                Thief.pickTile((SphinxTile) chosen_tile);
            } else {
                Thief.pickTile((CaryatidTile) chosen_tile);
            }
            removeTile((StatueTile) chosen_tile);
        }
    }

    /**
     * Initializes sorting areas by adding 1 tile to each of them.
     * @return FindingTile array with the first tiles on each sorting area.
     */
    public FindingTile[] initialize_sorting_areas(){
        FindingTile[] new_tiles = bag.getFirstTiles();
        for(FindingTile chosen: new_tiles){
            if(chosen.getType().equals("AmphoraTile")){
                addTile((AmphoraTile) chosen);
            } else if(chosen.getType().equals("MosaicTile")){
                addTile((MosaicTile) chosen);
            } else if(chosen.getType().equals("SkeletonTile")){
                addTile((SkeletonTile) chosen);
            } else {
                addTile((StatueTile) chosen);
            }
        }
        return new_tiles;
    }

    /**
     * Getter for sorting areas arraylist
     * @return SortingAreas ArrayList
     */
    public ArrayList<ArrayList<FindingTile>> getSortingAreas(){
        return SortingAreas;
    }

    /**
     * Getter for the number of placed LandslideTiles on the entrance
     * @return the number of placed LandslideTiles on board.
     */
    public int getEntranceTiles(){
        return EntranceTiles;
    }
    public void loadTiles(JSONObject sorting_areas){
        ArrayList<FindingTile> tiles = bag.load_tiles(sorting_areas);
        for(FindingTile tile: tiles){
            if(tile.getType().equals("AmphoraTile")){
                addTile((AmphoraTile) tile);
            } else if(tile.getType().equals("MosaicTile")){
                addTile((MosaicTile) tile);
            } else if(tile.getType().equals("SkeletonTile")){
                addTile((SkeletonTile) tile);
            } else {
                addTile((StatueTile) tile);
            }
        }
    }

    /**
     * Re-initializes Board and removes all of its saved info
     * @post board must be like new after this operation, as well as the bag.
     */
    public void reset(){
        Entrance = new LandslideTile[MAX_ENTRANCE_TILES];
        EntranceTiles = 0;
        SortingAreas = new ArrayList<> (0);
        for(int i = 0; i < SORTING_AREAS_COUNT; i++){
            SortingAreas.add(new ArrayList<>(0));
        }
        EntranceTiles = 0;
        players.clear();
        bag.reset();
    }

    /**
     * Sets turn to a specific player with the given name.
     * @param name string with the player's name who will now be playing.
     */
    public void giveTurn(String name){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getName().equals(name)){
                NowPlaying = i;
                break;
            }
        }
    }


}
