package model;

import org.json.simple.JSONObject;

import static model.Board.SORTING_AREAS_COUNT;
import static model.Character.TOTAL_CHARACTERS;
import static model.FindingTile.*;
import java.util.ArrayList;

public class Player {
    /**
     * Player's name.
     */
    private final String name;
    /**
     * 2D ArrayList containing all tiles the player collected from the board. 4 Rows for each category.
     */
    public final ArrayList<ArrayList<FindingTile>> collected_tiles = new ArrayList<>(0);
    /**
     * Instance of the last tile the player picked.
     */
    private FindingTile last_picked;
    /**
     * static ArrayList that counts collected sphinx for each player.
     */
    private static ArrayList<Integer> collected_sphinx =  new ArrayList<>();
    /**
     * static ArrayList that counts collected caryatids for each player.
     */
    private static ArrayList<Integer> collected_caryatid =  new ArrayList<>();
    /**
     * PlayerID, unique for each player, basically his registration number in order (1-4).
     */
    private final int playerID;
    /**
     * Array containing all different character cards.
     */
    private final Character[] characters = new Character[TOTAL_CHARACTERS];
    /**
     * Static final int that shows Assistant's position in characters array.
     */
    public static final int ASSISTANT = 0;
    /**
     * Static final int that shows Archaeologist's position in characters array.
     */
    public static final int ARCHAEOLOGIST = 1;
    /**
     * Static final int that shows Digger's position in characters array.
     */
    public static final int DIGGER = 2;
    /**
     * Static final int that shows Professor's position in characters array.
     */
    public static final int PROFESSOR = 3;
    /**
     * CharacterColor array that shows the color of the cards for each player. It is unique for every player.
     */
    private static final CharacterColor[] color = CharacterColor.values();
    /**
     * Player's points, will be initialized only when the game ends.
     */
    private int points = -1;
    /**
     * Constructor for a player.
     * @param name player's name.
     * @param playerID a playerID assigned by the board.
     */
    Player(String name, int playerID){
        this.name = name;
        this.playerID = playerID;
        for(int i = 0; i < SORTING_AREAS_COUNT; i++){
            collected_tiles.add(new ArrayList<FindingTile>(0));
        }
        collected_sphinx.add(0);
        collected_caryatid.add(0);

        characters[ASSISTANT] = new Assistant(color[playerID]);
        characters[ARCHAEOLOGIST] = new Archaeologist(color[playerID]);
        characters[DIGGER] = new Digger(color[playerID]);
        characters[PROFESSOR] = new Professor(color[playerID]);
    }

    /**
     * Getter for player's name.
     * @post must not alter player's name, but simply return it.
     * @return player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Overloaded method used to add a tile to the player's collection.
     * @pre choice != null
     * @param choice AmphoraTile object.
     */
    public void pickTile(AmphoraTile choice){
        if(choice == null){
            throw new NullPointerException();
        }
        last_picked = choice;
        collected_tiles.get(AMPHORA_TILES).add(choice);
    }
    /**
     * Overloaded method used to add a tile to the player's collection.
     * @pre choice != null
     * @param choice MosaicTile object.
     */
    public void pickTile(MosaicTile choice){
        if(choice == null){
            throw new NullPointerException();
        }
        last_picked = choice;
        collected_tiles.get(MOSAIC_TILES).add(choice);
    }
    /**
     * Overloaded method used to add a tile to the player's collection.
     * @pre choice != null
     * @param choice SkeletonTile object.
     */
    public void pickTile(SkeletonTile choice){
        if(choice == null){
            throw new NullPointerException();
        }
        last_picked = choice;
        collected_tiles.get(SKELETON_TILES).add(choice);
    }
    /**
     * Overloaded method used to add a tile to the player's collection.
     * @pre choice != null
     * @param choice SphinxTile object.
     */
    public void pickTile(SphinxTile choice){
        if(choice == null){
            throw new NullPointerException();
        }
        last_picked = choice;
        collected_tiles.get(STATUE_TILES).add(choice);
        collected_sphinx.set(playerID, collected_sphinx.get(playerID) + 1);
    }
    /**
     * Overloaded method used to add a tile to the player's collection.
     * @pre choice != null
     * @param choice CaryatidTile object.
     */
    public void pickTile(CaryatidTile choice){
        if(choice == null){
            throw new NullPointerException();
        }
        last_picked = choice;
        collected_tiles.get(STATUE_TILES).add(choice);
        collected_caryatid.set(playerID, collected_caryatid.get(playerID) + 1);
    }

    /**
     * Calculate points for this player.
     * @return his total points so far.
     */
    private void calculatePoints(){
        points = calculateStatuePoints() + calculateAmphoraPoints() + calculateMosaicPoints() + calculateSkeletonPoints();
    }

    /**
     * Calculate points granted for the statue this player has collected so far.
     * @return statue points achieved from both statue categories. Will be either 0, 3, 6, 9 or 12.
     */
    private int calculateStatuePoints(){
        int min = collected_sphinx.get(0), max = collected_sphinx.get(0), total_points = 0;
        // Ypologizoume gia collected sphinx min kai max
        for(int i = 1; i < collected_sphinx.size(); i++){
            int tmp = collected_sphinx.get(i);
            if(min > tmp){
                min = tmp;
            }
            if(max < tmp){
                max = tmp;
            }
        }
        // an einai max tote +6, an den einai min +3
        if(collected_sphinx.get(playerID) == max){
            total_points += 6;
        } else if(collected_sphinx.get(playerID) != min){
            total_points += 3;
        }

        // epanalamvanoume gia caryatid tiles.
        min = collected_caryatid.get(0);
        max = min;
        for(int i = 1; i < collected_caryatid.size(); i++){
            int tmp = collected_caryatid.get(i);
            if(min > tmp){
                min = tmp;
            }
            if(max < tmp){
                max = tmp;
            }
        }
        if(collected_caryatid.get(playerID) == max){
            total_points += 6;
        } else if(collected_caryatid.get(playerID) != min){
            total_points += 3;
        }
        return total_points;
    }
    /**
     * Calculate points granted for the AmphoraTiles this player has collected so far.
     * @return points achieved from Amphora Tiles. Increased by 1 for each unique color AmphoraTile the player possesses.
     */
    private int calculateAmphoraPoints(){
        int size, total_points = 0;
        int[] colorArray;
        colorArray = new int[AMPHORA_COLORS];
        for(int i = 0; i < AMPHORA_COLORS; i++){
            colorArray[i] = 0;
        }
        size = collected_tiles.get(AMPHORA_TILES).size();
        for(int i = 0; i < size; i ++){
            // tupika apo ton 2D array pairnoume to row 1, to i-osto stoixeio apo to row auto,
            // typecast se AmphoraTile afou outws i allws einai mosaic tiles ekei, pairnoume to color tou
            // pou einai enum object kai tsimpame to ordinal tou.
            colorArray[( (AmphoraTile) collected_tiles.get(AMPHORA_TILES).get(i)).getColor().ordinal()]++;
        }
        for(int i = 0; i < AMPHORA_COLORS; i++){
            if(colorArray[i] != 0){ // an exoume toulaxiston 1 apo to i-osto xrwma tote pairnoume 1 ponto.
                total_points++;
            }
        }
        switch (total_points){
            case 3:
                total_points = 1;
                break;
            case 4:
                total_points = 2;
                break;
            case 5:
                total_points = 4;
                break;
            case 6:
                total_points = 6;
                break;
            default:
                total_points = 0;
                break;
        }
        return total_points;
    }

    /**
     * Calculate points granted for the MosaicTiles this player has collected so far.
     * @return points achieved from Mosaic Tiles. Increased by 4 for each same color squad, 2 for each different color squad and 0 for no squads.
     */
    private int calculateMosaicPoints(){
        int size, total_points = 0, mod = 0;
        int[] colorArray;
        colorArray = new int[MOSAIC_COLORS];
        for(int i = 0; i < MOSAIC_COLORS; i++){
            colorArray[i] = 0;
        }
        size = collected_tiles.get(MOSAIC_TILES).size();
        for(int i = 0; i < size; i ++){
            // omoiws me Amphora Tiles
            colorArray[( (MosaicTile) collected_tiles.get(MOSAIC_TILES).get(i)).getColor().ordinal()]++;
        }
        for(int i = 0; i < MOSAIC_COLORS; i++){ // for every squad of same color mosaic tiles we get 4 points
            total_points += ((int) colorArray[i]/4)*4;
            mod += colorArray[i]%4;
        }
        // for the remaining mosaic tiles, we get 2 points per squad.
        total_points += ((int) mod/4)*2;
        return total_points;
    }

    /**
     * Calculate points granted for the SkeletonTiles this player has collected so far.
     * @return points achieved from Skeleton Tiles. Increased by 6 for every 2 complete adults and children, and 1 for every other skeleton.
     */
    private int calculateSkeletonPoints(){
        int size, adults = 0, children = 0, total_points = 0, upper_a = 0, lower_a = 0, upper_c = 0, lower_c = 0;
        size = collected_tiles.get(SKELETON_TILES).size();
        for(int i = 0; i < size; i++){ // calculate different bodyparts
            SkeletonTile tmp= (SkeletonTile) collected_tiles.get(SKELETON_TILES).get(i);
            if(tmp.getIsAdultSkeleton()){
                if(tmp.getIsUpperBody()){
                    upper_a++;
                } else{
                    lower_a++;
                }
            } else{
                if(tmp.getIsUpperBody()){
                    upper_c++;
                } else{
                    lower_c++;
                }
            }
        }
        // number of adults and children creatable.
        adults = Math.min(upper_a, lower_a);
        children = Math.min(upper_c, lower_c);
        while(adults > 0 && children > 0){
            if(adults - 2 >= 0){ // there is at least one children, otherwise the while loop condition would fail.
                adults -= 2;
                children--;
                total_points += 6;
            } else{
                adults = 0;
                total_points += 3;
            }
        }
        // remaining complete skeletons.
        total_points += 3*adults + 3*children;
        return total_points;
    }

    /**
     * Uses character ability.
     * @param character is the character the player chose to use its ability.
     * @throws IllegalActionException if character is not within [0, characters.length) or if the character isn't in this player's collection.
     */
    public void use_ability(int character){
        if(character > TOTAL_CHARACTERS){
            throw new IllegalActionException("There is no such character.");
        }

        characters[character].useAbility();
    }

    /**
     * Calculates and returns an ArrayList containing all characters who haven't used their abilities
     * @return an ArrayList with indexes from characters who haven't been used yet.
     */
    public ArrayList<Integer> getNotUsedAbilities(){
        ArrayList<Integer> result = new ArrayList<>();
        for(int i = 0; i < TOTAL_CHARACTERS; i++){
            if(!characters[i].getUsedAbility()){
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Counts all Amphora tiles with the given color.
     * @param color Desired Amphora color
     * @return the number of Amphora Tiles with the given color
     */
    public int getAmphoraCount(AmphoraColor color){
        int count = 0;
        for(FindingTile tile: collected_tiles.get(AMPHORA_TILES)){
            if(((AmphoraTile) tile).getColor() == color){
                count++;
            }
        }
        return count;
    }
    /**
     * Counts all Mosaic tiles with the given color.
     * @param color Desired Mosaic color
     * @return the number of Mosaic Tiles with the given color
     */
    public int getMosaicCount(MosaicColor color){
        int count = 0;
        for(FindingTile tile: collected_tiles.get(MOSAIC_TILES)){
            if(((MosaicTile) tile).getColor() == color){
                count++;
            }
        }
        return count;
    }
    /**
     * Counts all Skeleton tiles of the given type.
     * @param upper True if we want the upper body, else false
     * @param adult True if we want an adult skeleton, else false
     * @return the number of Skeleton Tiles matching the parameters.
     */
    public int getSkeletonCount(boolean upper, boolean adult){
        int count = 0;
        for(FindingTile tile: collected_tiles.get(SKELETON_TILES)){
            if(((SkeletonTile) tile).getIsAdultSkeleton() == adult && ((SkeletonTile) tile).getIsUpperBody() == upper){
                count++;
            }
        }
        return count;
    }
    /**
     * Counts all Statue tiles of the given type.
     * @param isSphinx true if we want sphinx tiles, false if we want caryatid
     * @return the number of Statue tiles of the specified type.
     */
    public int getStatueCount(boolean isSphinx){
        if(isSphinx){
            return collected_sphinx.get(playerID);
        } else{
            return collected_caryatid.get(playerID);
        }
    }

    /**
     * Returns the instance of the last tile this player picked.
     * @return last_picked attribute
     */
    public FindingTile getLast_picked() {
        return last_picked;
    }

    /**
     * Calculates player's points if the game has ended (board entrance full) and returns player's total points.
     * @pre The game must have ended, otherwise will return -1.
     * @return points attribute.
     */
    public int getPoints(){
        if(points == -1 && Board.getInstance().check_entrance()){
            calculatePoints();
        }
        return points;
    }

    /**
     * Getter for Characters array
     * @return Characters' array.
     */
    public Character[] getCharacters(){
        return characters;
    }

    /**
     * Updates player's possessions with given info
     * @param info Player JSONObject containing his tiles and used characters.
     */
    public void loadInfo(JSONObject info){
        ArrayList<FindingTile> tiles = Bag.getInstance().load_tiles((JSONObject) info.get("collection"));
        for(FindingTile tile : tiles){
            if(tile.getType().equals("AmphoraTile")){
                pickTile((AmphoraTile) tile);
            } else if(tile.getType().equals("MosaicTile")){
                pickTile((MosaicTile) tile);
            } else if(tile.getType().equals("SkeletonTile")){
                pickTile((SkeletonTile) tile);
            } else {
                if(tile.getTileName().equals("sphinx")){
                    pickTile((SphinxTile) tile);
                } else{
                    pickTile((CaryatidTile) tile);
                }
            }
        }
        if((boolean)((JSONObject) info.get("characters")).get("Professor")){
            use_ability(PROFESSOR);
        }
        if((boolean)((JSONObject) info.get("characters")).get("Assistant")){
            use_ability(ASSISTANT);
        }
        if((boolean)((JSONObject) info.get("characters")).get("Digger")){
            use_ability(DIGGER);
        }
        if((boolean)((JSONObject) info.get("characters")).get("Archaeologist")){
            use_ability(ARCHAEOLOGIST);
        }
    }

    /**
     * Getter for player's ID, assigned by the board.
     * @return playerID
     */
    public int getPlayerID(){
        return playerID;
    }
}
