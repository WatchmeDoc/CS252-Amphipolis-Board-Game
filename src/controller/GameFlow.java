package controller;

import model.*;
import model.Character;
import view.GUI;
import view.TileButton;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class GameFlow {
    /**
     * Number of Max players.
     */
    private int NoOfPlayers = 0;
    /**
     * currently Registered players.
     */
    private int curr_players = 0;
    /**
     * board instance
     */
    private Board board;
    /**
     * Game Status, can be "Initialized", "Started" or "Finished".
     */
    private String gameStatus = "";
    /**
     * Instance private variable since its a singleton class
     */
    private static GameFlow instance = null;
    /**
     * GUI instance
     */
    private GUI graphics;
    /**
     * Boolean variable that shows whether an ability has been used in this round or not.
     */
    private boolean used_ability;
    /**
     * Boolean variable that shows whether the game is on single player or not.
     */
    private boolean single_player_mode = false;
    /**
     * Integer variable that shows the number of tiles a player has picked so far.
     */
    public int chosen_tiles = 0;
    /**
     * Player info level in json save file.
     */
    private final int JSON_PLAYER_INFO = 0;
    /**
     * Board Tiles level in json save file.
     */
    private final int JSON_BOARD_AREAS = 1;
    /**
     * Player name level in json save file.
     */
    private final int JSON_NOW_PLAYING_NAME = 2;
    /**
     * Private Constructor so there is no instance of the class - all its methods ares eitherway.
     */
    private GameFlow(){
        graphics = GUI.getInstance();
        initialize(graphics.getNumber_of_players());
        String[] names = graphics.getNames();
        for(String name: names){
            RegisterPlayer(name);
        }
        // Action Listeners
        // Draw Tiles Button
        graphics.draw_tiles.addActionListener(e -> {
            graphics.save.setEnabled(false);
            play();
            used_ability = false;
            graphics.draw_tiles.setEnabled(false);
            graphics.end_turn.setEnabled(graphics.table.tiles.stream()
                .noneMatch(Component::isEnabled));

            graphics.characters.forEach(character -> character.setEnabled(false));
        });
        // End Turn button
        graphics.end_turn.addActionListener(e -> {
            graphics.save.setEnabled(true);
            if(!gameStatus.equals("Finished")){
                chosen_tiles = 0;
                next();
                graphics.draw_tiles.setEnabled(true);
                graphics.end_turn.setEnabled(false);
                graphics.table.tiles.forEach(button -> button.setEnabled(false));
                graphics.characters.forEach(character -> character.setEnabled(false));
            } else{
                next();
                update_tiles();
            }
        });
        // View collection button
        graphics.collection.addActionListener(e -> graphics.view_collection());

        // TileButton
        graphics.table.tiles.forEach(tile -> tile.addActionListener(e -> {
            FindingTile tmp = tile.pop_tile();
            board.pickTile(tmp);
            update_tiles();
            graphics.characters.forEach(character -> character.setEnabled(false));
            chosen_tiles++;
            if(chosen_tiles == 0){ // Only during the professor's ability
                chosen_tiles = -1;
            }
            if(chosen_tiles == 1){
                graphics.table.tiles.stream().filter(tileButtons -> !tileButtons.getType().equals(tmp.getType()))
                        .forEach(tileButtons -> tileButtons.setEnabled(false));
            }
            if(chosen_tiles == 2 ||
                    (graphics.table.tiles.stream().noneMatch(button -> button.getTile_count() != 0
                            && button.getType().equals(tmp.getType())) && chosen_tiles == 1))
            {
                chosen_tiles = 0;
                graphics.table.tiles.forEach(button -> button.setEnabled(false));
                graphics.end_turn.setEnabled(true);
                if(!used_ability){
                    ArrayList<Integer> not_used = board.getNowPlaying().getNotUsedAbilities();
                    for(Integer i: not_used){
                        graphics.characters.get(i).setEnabled(true);
                    }
                }
            }
        }));

        // Characters
        graphics.characters.get(Player.ASSISTANT).addActionListener(e -> {
            board.getNowPlaying().use_ability(Player.ASSISTANT);
            chosen_tiles = 1;
            update_tiles();
            used_ability = true;
            graphics.characters.forEach(character -> character.setEnabled(false));
        });
        graphics.characters.get(Player.ARCHAEOLOGIST).addActionListener(e -> {
            board.getNowPlaying().use_ability(Player.ARCHAEOLOGIST);
            chosen_tiles = 0;
            update_tiles();
            graphics.table.tiles.stream().filter(tile ->
                    tile.getType().equals(board.getNowPlaying().getLast_picked().getType())
            ).forEach(tile -> tile.setEnabled(false));
            used_ability = true;
            graphics.characters.forEach(character -> character.setEnabled(false));
        });
        graphics.characters.get(Player.DIGGER).addActionListener(e -> {
            board.getNowPlaying().use_ability(Player.DIGGER);
            chosen_tiles = 0;
            update_tiles();
            graphics.table.tiles.stream().filter(tile ->
                    !tile.getType().equals(board.getNowPlaying().getLast_picked().getType())
            ).forEach(tile -> tile.setEnabled(false));
            used_ability = true;
            graphics.characters.forEach(character -> character.setEnabled(false));
        });
        graphics.characters.get(Player.PROFESSOR).addActionListener(e ->{
            chosen_tiles = -1;
            board.getNowPlaying().use_ability(Player.PROFESSOR);
            update_tiles();
            graphics.end_turn.setEnabled(false);
            Thread process = new Thread(() -> {
                ArrayList<String> used_areas = new ArrayList<>(0);
                String tiletype = board.getNowPlaying().getLast_picked().getType();
                used_areas.add(tiletype);
                while(graphics.table.tiles.stream().anyMatch(Component::isEnabled)){
                    graphics.table.tiles.stream().filter(tile ->
                            used_areas.contains(tile.getType())
                    ).forEach(tile -> tile.setEnabled(false));
                    tiletype = board.getNowPlaying().getLast_picked().getType();
                    if(!used_areas.contains(tiletype)){
                        used_areas.add(tiletype);
                    }
                }
                chosen_tiles = 0;
                graphics.end_turn.setEnabled(true);
            });
            process.start();
            used_ability = true;
            graphics.characters.forEach(character -> character.setEnabled(false));
        });
        // Save and Load game Action Listeners
        graphics.save.addActionListener(e -> save());
        for(FindingTile tile: board.initialize_sorting_areas()){
            graphics.table.add_tile(tile);
        }
        graphics.load.addActionListener(e -> {
            load();
            chosen_tiles = 0;
            graphics.draw_tiles.setEnabled(true);
            graphics.end_turn.setEnabled(false);
            graphics.table.tiles.forEach(button -> button.setEnabled(false));
            graphics.characters.forEach(character -> character.setEnabled(false));
        });
        graphics.table.tiles.forEach(tile -> tile.setEnabled(false));
        graphics.end_turn.setEnabled(false);
        graphics.characters.forEach(character -> character.setEnabled(false));
        if(NoOfPlayers == 1){
            single_player_mode = true;
            board.create_thief();
            for(int i = 0; i < Bag.SINGLE_PLAYER_ENTRANCE_TILES; i++){
                graphics.table.add_entrance_tile();
            }
        }
    }

    /**
     * Game Initialization.
     * @inv 0 less or equal to NumOfPlayers less or equal to CharacterColor.values().length.
     * @pre The game must not have been initialized yet.
     * @post Must change game_status to "Initialized".
     * @param NumOfPlayers the number of total players.
     * @throws InvalidNumberOfPlayersException if NumOfPlayers less than 0 OR NumOfPlayers more than CharacterColor.values().length
     */
    private void initialize(int NumOfPlayers){
        if(NumOfPlayers < 0 || NumOfPlayers > CharacterColor.values().length){
            throw new InvalidNumberOfPlayersException();
        }
        NoOfPlayers = NumOfPlayers;
        board = Board.getInstance();
        gameStatus = "Initialized";
    }

    /**
     * Method that registers a player to the table named with "name"
     * @pre The game is not just initialized.
     * @post Must change game_status to "Started".
     * @param name the name of the registered player.
     */
    private void RegisterPlayer(String name){
        if(!gameStatus.equals("Initialized")){
            throw new IllegalActionException("Cannot Add any more players or the game hasn't been initialized yet.");
        }
        board.addPlayer(name);
        curr_players++;
        if(curr_players == NoOfPlayers){
            gameStatus = "Started";
        }
    }

    /**
     * Taking 4 random tiles off the bag and placing them on board.
     * @pre The game must have started.
     * @throws IllegalActionException if the game is not active.
     */
    public void play(){
        if(!gameStatus.equals("Started")){
            throw new IllegalActionException("The game is not active.");
        }
        Tile[] new_tiles = board.PlaceTiles();
        for(Tile tile: new_tiles){
            if(tile == null){
                continue;
            }
            String type = tile.getTileName();
            if(type.equals("landslide")){
                graphics.table.add_entrance_tile();
                if(single_player_mode){
                    steal();
                }
            } else{
                graphics.table.add_tile((FindingTile) tile);
            }
        }
        update_tiles();
        check_game();
    }

    /**
     * Next Player's turn.
     * @pre The game must have started.
     */
    public void next(){
        if(!single_player_mode){
            board.nextPlayer();
            setInfo(board.getNowPlaying());
        }
    }
    /**
     * Checks whether the game has ended or not.
     * @pre The game must have started.
     * @post must change game_status if the entrance if full.
     * @return True if the entrance is full of landslide tiles, false if not.
     * @throws IllegalActionException if the game is not active.
     */
    public void check_game(){
        if(!gameStatus.equals("Started")){
            throw new IllegalActionException("The game is not active.");
        }
        if(board.check_entrance()){
            gameStatus = "Finished";
            update_tiles();
            graphics.display_winner(board.check_winner());
        }
    }

    /**
     * Getter for controller's instance since its a singleton class
     * @return controller single instance
     */
    public static GameFlow getInstance() {
        if(instance == null){
            instance = new GameFlow();
        }
        return instance;
    }

    /**
     * Upadtes GUI's info JLabel
     * @param player Player who's name will be shown on the JFrame.
     */
    public void setInfo(Player player){
        if(player == null){
            throw new NullPointerException();
        }
        graphics.info.setText("<html>"+ player.getName() +"<br/><br/><br/>Use Character</html>");
    }

    /**
     * Updates tiles properly, based on how many tiles of each category are on board.
     * If the game is Finished, it will only enable "end turn" button, disabling the rest of the tiles.
     */
    public void update_tiles(){
        if(gameStatus.equals("Finished")){
            graphics.draw_tiles.setEnabled(false);
            graphics.table.tiles.forEach(tile-> tile.setEnabled(false));
            graphics.characters.forEach(character -> character.setEnabled(false));
            graphics.end_turn.setEnabled(true);
        } else{
            graphics.table.tiles.parallelStream().forEach(tile -> tile.setEnabled(tile.getTile_count() != 0));
            ArrayList<Integer> not_used = board.getNowPlaying().getNotUsedAbilities();
            for(Integer i: not_used){
                graphics.characters.get(i).setEnabled(true);
            }
        }
    }
    /**
     * Removes all tiles from the board and gives them to the thief
     * @post No tiles are left on GUI board or on the actual board.
     */
    private void steal(){
        for (TileButton tile : graphics.table.tiles) {
            board.stealTile(tile.pop_tile());
        }
        update_tiles();
    }

    /**
     * Saves current game state in a .json file.
     * @pre the order of the saved items must match our variables at the top
     */
    private void save(){
        JSONArray players = new JSONArray();
        for(Player player: board.getPlayers()){
            JSONObject player_info = new JSONObject();
            // TILE COLLECTION
            JSONObject player_collection = new JSONObject();
            for(AmphoraColor amphoraColor : AmphoraColor.values()){
                player_collection.put("amphora_" + amphoraColor.toString().toLowerCase(), player.getAmphoraCount(amphoraColor));
            }
            for(MosaicColor mosaicColor : MosaicColor.values()){
                player_collection.put("mosaic_" + mosaicColor.toString().toLowerCase(), player.getMosaicCount(mosaicColor));
            }
            player_collection.put("skeleton_big_top", player.getSkeletonCount(true, true));
            player_collection.put("skeleton_small_top", player.getSkeletonCount(true, false));
            player_collection.put("skeleton_big_bottom", player.getSkeletonCount(false, true));
            player_collection.put("skeleton_small_bottom", player.getSkeletonCount(false, false));
            player_collection.put("sphinx", player.getStatueCount(true));
            player_collection.put("caryatid", player.getStatueCount(false));
            player_info.put("collection", player_collection);


            boolean[] uses = new boolean[Character.TOTAL_CHARACTERS];
            for(int i = 0; i < Character.TOTAL_CHARACTERS; i++){
                uses[i] = true;
            }
            for(Integer index : player.getNotUsedAbilities()){
                uses[index] = false;
            }
            JSONObject player_characters =  new JSONObject();
            int i = 0;
            for(Character character : player.getCharacters()){
                player_characters.put(character.name, uses[i++]);
            }

            player_info.put("characters", player_characters);
            player_info.put("name", player.getName());

            JSONObject curr_player = new JSONObject();
            curr_player.put("Player" + (player.getPlayerID() + 1), player_info);
            players.add(curr_player);
        }

        JSONObject board_info = new JSONObject();
        board_info.put("amphora_blue", board.getSortingAreas()
                .get(FindingTile.AMPHORA_TILES).stream().filter(x -> x.getTileName().equals("amphora_blue")).count());
        board_info.put("amphora_brown", board.getSortingAreas()
                .get(FindingTile.AMPHORA_TILES).stream().filter(x -> x.getTileName().equals("amphora_brown")).count());
        board_info.put("amphora_green", board.getSortingAreas()
                .get(FindingTile.AMPHORA_TILES).stream().filter(x -> x.getTileName().equals("amphora_green")).count());
        board_info.put("amphora_purple", board.getSortingAreas()
                .get(FindingTile.AMPHORA_TILES).stream().filter(x -> x.getTileName().equals("amphora_purple")).count());
        board_info.put("amphora_red", board.getSortingAreas()
                .get(FindingTile.AMPHORA_TILES).stream().filter(x -> x.getTileName().equals("amphora_red")).count());
        board_info.put("amphora_yellow", board.getSortingAreas()
                .get(FindingTile.AMPHORA_TILES).stream().filter(x -> x.getTileName().equals("amphora_yellow")).count());

        board_info.put("mosaic_green", board.getSortingAreas()
                .get(FindingTile.MOSAIC_TILES).stream().filter(x -> x.getTileName().equals("mosaic_green")).count());
        board_info.put("mosaic_red", board.getSortingAreas()
                .get(FindingTile.MOSAIC_TILES).stream().filter(x -> x.getTileName().equals("mosaic_red")).count());
        board_info.put("mosaic_yellow", board.getSortingAreas()
                .get(FindingTile.MOSAIC_TILES).stream().filter(x -> x.getTileName().equals("mosaic_yellow")).count());

        board_info.put("skeleton_big_top", board.getSortingAreas().get(FindingTile.SKELETON_TILES).stream()
                .filter(x -> x.getTileName().equals("skeleton_big_top")).count());

        board_info.put("skeleton_small_top", board.getSortingAreas().get(FindingTile.SKELETON_TILES).stream()
                .filter(x -> x.getTileName().equals("skeleton_small_top")).count());

        board_info.put("skeleton_big_bottom", board.getSortingAreas().get(FindingTile.SKELETON_TILES).stream()
                .filter(x -> x.getTileName().equals("skeleton_big_bottom")).count());

        board_info.put("skeleton_small_bottom", board.getSortingAreas().get(FindingTile.SKELETON_TILES).stream()
                .filter(x -> x.getTileName().equals("skeleton_small_bottom")).count());

        board_info.put("sphinx", board.getSortingAreas().get(FindingTile.STATUE_TILES).stream()
                .filter(x -> x.getTileName().equals("sphinx")).count());
        board_info.put("caryatid", board.getSortingAreas().get(FindingTile.STATUE_TILES).stream()
                .filter(x -> x.getTileName().equals("caryatid")).count());
        JSONArray array = new JSONArray();
        JSONObject board_tiles =  new JSONObject();
        board_tiles.put("sorting_areas",board_info);
        JSONObject landslide_tiles = new JSONObject();
        landslide_tiles.put("LandslideTile", board.getEntranceTiles());
        board_tiles.put("landslide_tiles", landslide_tiles);
        JSONObject turn = new JSONObject();
        turn.put("Playing", board.getPlayerName());
        array.add(players);
        array.add(board_tiles);
        array.add(turn);
        //Write JSON file
        try {
            FileWriter file = new FileWriter("save_file.json");
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads save file named .json
     * @pre the file must have been created by "save" method, loading does not validate it.
     * @post board must reset and be initialized again with the new data
     * @post Must update all necessary info saved in control and gui.
     */
    private void load(){

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("save_file.json"))
        {
            single_player_mode = false;
            Object obj = jsonParser.parse(reader);
            JSONArray saved_info = (JSONArray) obj;
            JSONArray players = (JSONArray) saved_info.get(JSON_PLAYER_INFO);
            board.reset();
            NoOfPlayers = 0;
            curr_players = 0;
            int k = 1;
            for(Object player: players){
                load_player((JSONObject) player, k++);
            }
            graphics.table.reset();
            graphics.setNumberOfPlayers(NoOfPlayers);
            board.loadTiles(((JSONObject)((JSONObject) saved_info.get(JSON_BOARD_AREAS)).get("sorting_areas")));
            board.getSortingAreas().stream().forEach(area ->
                area.stream().forEach(area_tile -> graphics.table.add_tile(area_tile))
            );
            board.giveTurn(((JSONObject)saved_info.get(JSON_NOW_PLAYING_NAME)).get("Playing").toString());
            setInfo(board.getNowPlaying());
            int N = ((Long) ((JSONObject) ((JSONObject) saved_info.get(JSON_BOARD_AREAS)).get("landslide_tiles")).get("LandslideTile")).intValue();
            for(int i = 0; i < N; i++){
                graphics.table.add_entrance_tile();
                board.addTile(new LandslideTile());
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads Player with given info
     * @param player JSONObject containing all player info
     * @param ID player's ID, will match with the save file's one.
     */
    private void load_player(JSONObject player, int ID){
        String name = (String) ((JSONObject) player.get("Player" + ID)).get("name");
        if(name.equals("Fox the Thief")){
            single_player_mode = true;
        }
        NoOfPlayers++;
        curr_players++;
        Player new_player = board.addPlayer(name);
        new_player.loadInfo((JSONObject) player.get("Player" + ID));
    }
}
