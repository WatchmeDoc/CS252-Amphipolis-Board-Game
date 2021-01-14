package model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;


public class ModelTest extends TestCase {
    ArrayList<Player> players;
    Bag bag = Bag.getInstance();
    Board board = Board.getInstance();
    public void defaultModelTest(){
        board.addPlayer("Player1");
        board.addPlayer("Player2");
        board.addPlayer("Player3");
        board.addPlayer("Player4");
        players = board.getPlayers();
    }
    @Test
    public void testCollections(){
        defaultModelTest();
        for(Player player: players){
            assertEquals(player.getAmphoraCount(AmphoraColor.BLUE), 0);
            assertEquals(player.getAmphoraCount(AmphoraColor.RED), 0);
            assertEquals(player.getAmphoraCount(AmphoraColor.GREEN), 0);
            assertEquals(player.getAmphoraCount(AmphoraColor.YELLOW), 0);
            assertEquals(player.getAmphoraCount(AmphoraColor.BROWN), 0);
            assertEquals(player.getAmphoraCount(AmphoraColor.PURPLE), 0);

            assertEquals(player.getMosaicCount(MosaicColor.GREEN), 0);
            assertEquals(player.getMosaicCount(MosaicColor.RED), 0);
            assertEquals(player.getMosaicCount(MosaicColor.YELLOW), 0);

            assertEquals(player.getSkeletonCount(true,true), 0);
            assertEquals(player.getSkeletonCount(true,false), 0);
            assertEquals(player.getSkeletonCount(false,true), 0);
            assertEquals(player.getSkeletonCount(false,false), 0);

            assertEquals(player.getStatueCount(true), 0);
            assertEquals(player.getStatueCount(false), 0);
        }
        players.get(0).pickTile(new CaryatidTile());
        players.get(0).pickTile(new SphinxTile());
        players.get(0).pickTile(new AmphoraTile(AmphoraColor.BLUE));
        players.get(0).pickTile(new MosaicTile(MosaicColor.GREEN));
        players.get(0).pickTile(new SkeletonTile(true, false));

        assertEquals(players.get(0).getStatueCount(false), 1);
        assertEquals(players.get(0).getStatueCount(true), 1);
        assertEquals(players.get(0).getSkeletonCount(true,false), 1);
        assertEquals(players.get(0).getMosaicCount(MosaicColor.GREEN), 1);
        assertEquals(players.get(0).getAmphoraCount(AmphoraColor.BLUE), 1);

        assertEquals(players.get(0).collected_tiles.get(FindingTile.AMPHORA_TILES).size(), 1);
        assertEquals(players.get(0).collected_tiles.get(FindingTile.MOSAIC_TILES).size(), 1);
        assertEquals(players.get(0).collected_tiles.get(FindingTile.SKELETON_TILES).size(), 1);
        assertEquals(players.get(0).collected_tiles.get(FindingTile.STATUE_TILES).size(), 2);
    }
    @Test
    public void testCharacters(){
        defaultModelTest();
        assertEquals((int) players.get(0).getNotUsedAbilities().stream().reduce(Integer::sum).get(), 6);
        players.get(0).use_ability(Player.PROFESSOR);
        assertEquals((int) players.get(0).getNotUsedAbilities().stream().reduce(Integer::sum).get(), 6 - Player.PROFESSOR);
        players.get(0).use_ability(Player.ARCHAEOLOGIST);
        assertEquals((int) players.get(0).getNotUsedAbilities().stream().reduce(Integer::sum).get(), 6 - Player.PROFESSOR -  Player.ARCHAEOLOGIST);
        players.get(1).use_ability(Player.DIGGER);
        assertEquals((int) players.get(1).getNotUsedAbilities().stream().reduce(Integer::sum).get(), 6 - Player.DIGGER);
        assertEquals((int) players.get(0).getNotUsedAbilities().stream().reduce(Integer::sum).get(), 6 - Player.PROFESSOR -  Player.ARCHAEOLOGIST);
        assertEquals((int) players.get(2).getNotUsedAbilities().stream().reduce(Integer::sum).get(), 6);
        assertEquals((int) players.get(3).getNotUsedAbilities().stream().reduce(Integer::sum).get(), 6);
    }

    @Test
    public void testBag(){
        defaultModelTest();
        assertEquals(bag.getFirstTiles().length, 4);
        assertTrue(Arrays.stream(bag.getFirstTiles()).anyMatch(x -> x.getType().equals("AmphoraTile")));
        assertTrue(Arrays.stream(bag.getFirstTiles()).anyMatch(x -> x.getType().equals("MosaicTile")));
        assertTrue(Arrays.stream(bag.getFirstTiles()).anyMatch(x -> x.getType().equals("SkeletonTile")));
        assertTrue(Arrays.stream(bag.getFirstTiles()).anyMatch(x -> x.getType().equals("StatueTile")));
        assertEquals(bag.entrance_fillings().length, 8);
        for(int i = 0; i < 50; i++){
            assertNotNull(bag.getTile());
        }
    }
    @Test
    public void testBoard(){
        defaultModelTest();
        assertEquals(board.getPlayers().size(), 4);
        assertEquals(board.getNowPlaying().getName(), "Player1");
        board.nextPlayer();
        assertEquals(board.getNowPlaying().getName(), "Player2");
        board.nextPlayer();
        board.nextPlayer();
        board.nextPlayer();
        assertEquals(board.getNowPlaying().getName(), "Player1");
        Tile[] tiles = board.PlaceTiles();
        assertEquals(tiles.length, 4);
        assertFalse(board.check_entrance());
        int k = 16 - (int) Arrays.stream(tiles).filter(x -> x.getType().equals("LandslideTile")).count();
        for(int i = 0; i < k; i++){
            board.addTile(new LandslideTile());
        }
        assertTrue(board.check_entrance());
        players.get(0).pickTile(new SphinxTile());
        players.get(1).pickTile(new AmphoraTile(AmphoraColor.BLUE));
        assertEquals(board.check_winner(), players.get(0));
    }
}
