package view;

import model.FindingTile;
import javax.swing.*;
import java.util.ArrayList;

public class TileButton extends JButton {
    private JLabel tile_label;
    private ArrayList<FindingTile> tiles = new ArrayList<>(0);
    private int tile_count = 0;
    private final String type;
    private final String name;

    /**
     * Constructor for TileButton JButton.
     * @param imageIcon ImageIcon that will be assigned to the specific TileButton
     * @param type file name String without the .png ending.
     */
    public TileButton(ImageIcon imageIcon, String type) {
        super(imageIcon);
        tile_label = new JLabel("x" + tile_count);
        name = type;
        if(type.contains("mosaic")){
            this.type = "MosaicTile";
        } else if(type.contains("amphora")){
            this.type = "AmphoraTile";
        } else if(type.contains("skeleton")){
            this.type = "SkeletonTile";
        } else{
            this.type = "StatueTile";
        }
    }

    /**
     * Add tile to tiles ArrayList, and update its JLabel.
     */
    public void add_tile(FindingTile tile){
        tiles.add(tile);
        tile_label.setText("x" + ++tile_count);
    }

    /**
     * pop tile from tiles Arraylist, and update its JLabel.
     * @return the popped tile, or null if the arraylist is empty.
     */
    public FindingTile pop_tile(){
        if(tile_count == 0){
            return null;
        }
        tile_label.setText("x" + --tile_count);
        return tiles.remove(0);
    }

    /**
     * Change JLabel bounds.
     * @param x the new x-coordinate of this component.
     * @param y the new y-coordinate of this component.
     * @param width the new width of this component.
     * @param height the new height of this component.
     */
    public void setLabelBounds(int x, int y, int width, int height){
        tile_label.setBounds(x, y, width, height);
    }
    /**
     * Getter for tile_count attribute.
     * @return tile_count attribute.
     */
    public int getTile_count(){
        return tile_count;
    }

    /**
     * Getter for tile_label attribute
     * @return tile_label attribute
     */
    public JLabel getTile_label() {
        return tile_label;
    }

    /**
     * Get TileButton's Type
     * @return TileButton's Type.
     */
    public String getType(){
        return type;
    }

    /**
     * Getter for TileButton's name
     * @return TileButton's name.
     */
    public String getTileName() {
        return name;
    }
}
