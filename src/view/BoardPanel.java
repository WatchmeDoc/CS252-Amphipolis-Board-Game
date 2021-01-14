package view;

import model.Board;
import model.FindingTile;
import model.IllegalActionException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BoardPanel  extends JPanel {

    private JLayeredPane layeredPane;
    private JLabel imageContainer = new JLabel();
    public ArrayList<TileButton> tiles;
    private ArrayList<JLabel> entrance = new ArrayList<>(Board.MAX_ENTRANCE_TILES);
    private final double board_size;
    private final int button_size;

    /**
     * Constructor for Board Panel class
     */
    BoardPanel(){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        board_size = dim.height/1.30;
        button_size = (int) board_size/12;
        try {
            this.imageContainer.setIcon(new ImageIcon(
                    ImageIO.read(new File(
                            "images_2020\\background.png")).getScaledInstance(
                                    (int) board_size, (int) board_size, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension((int) board_size, (int) board_size));
        layeredPane.add(imageContainer, new Integer(50));
        String[] tile_names = {"mosaic_green", "mosaic_red", "mosaic_yellow","caryatid", "sphinx" ,"amphora_blue", "amphora_brown",
                "amphora_green", "amphora_purple", "amphora_red", "amphora_yellow",  "skeleton_big_top", "skeleton_small_top",
                "skeleton_big_bottom", "skeleton_small_bottom"};
        int N = tile_names.length;
        tiles = new ArrayList<>(N);

        for(int i = 0; i < N; i++){
            try {
                tiles.add(new TileButton(new ImageIcon(
                        ImageIO.read(new File(
                                "images_2020\\" + tile_names[i] + ".png")).getScaledInstance(
                                (int) board_size/12, (int) board_size/12, Image.SCALE_SMOOTH)), tile_names[i]));
                layeredPane.add(tiles.get(i), new Integer(100));
                layeredPane.add(tiles.get(i).getTile_label(), new Integer(100));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.add(layeredPane);
        imageContainer.setBounds( 0, 0,
                (int) board_size, (int) board_size );
        int[] buttons = {3, 2, 6, 4};
        int k = 0;
        for(int i = 0; i < 4; i++){
            int x = (int) (board_size/20 + i%2*board_size/1.54), y = (int) (board_size/20 + i/2*board_size/1.55 + i%2*i/2*board_size/30);
            for(int j = 0; j < buttons[i]; j++){
                int x_offset = (int) (x + j%2*board_size/(1.8*3.4));
                int y_offset = (int) (y + j/2*board_size/(3*3.4));
                tiles.get(k).setBounds(x_offset, y_offset,button_size, button_size);
                tiles.get(k++).setLabelBounds((int) (x_offset + board_size/11), y_offset, button_size, button_size);
            }
        }
    }

    /**
     * Adds tile to the proper ButtonTile button.
     * @pre tile must not be null
     * @param tile FindingTile instance that will be inserted to the proper ButtonTile button
     */
    public void add_tile(FindingTile tile){
        tiles.stream().filter(button -> button.getTileName().equals(tile.getTileName())).findAny()
                .ifPresent(button -> button.add_tile(tile));
    }

    /**
     * Adds an entrance tile JLabel to the board's entrance.
     */
    public void add_entrance_tile(){
        if(entrance.size() == Board.MAX_ENTRANCE_TILES){
            throw new IllegalActionException("Entrance already full!");
        }
        int tile_size = (int) (button_size/1.15);
        int x_offset = (int) (board_size/2.8 + entrance.size()%4*tile_size);
        int y_offset = (int) (board_size/2.29 + entrance.size()/4*tile_size);
        entrance.add(new JLabel(new ImageIcon("images_2020\\landslide.png")));
        entrance.get(entrance.size() - 1).setBounds(x_offset, y_offset,tile_size,tile_size);
        layeredPane.add(entrance.get(entrance.size() - 1), new Integer(100));
    }
    public void reset(){
        tiles.stream().filter(tile -> tile.getTile_count() != 0).forEach(tile -> {
            int N = tile.getTile_count();
            for(int i = 0; i < N; i++){
                tile.pop_tile();
            }
        });
        entrance.clear();
    }
}
