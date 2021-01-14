package view;



import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class GUI {

    private int number_of_players;
    private double board_size;
    private static GUI instance = null;
    private String[] names;
    private JPanel board;
    public BoardPanel table;
    private JFrame main_frame;
    public JButton draw_tiles;
    public JButton end_turn;
    public JButton collection;
    public final ArrayList<JButton> characters;
    public JLabel info;
    private JFrame players_collection;
    public JMenuItem save;
    public JMenuItem load;
    public JMenuBar menubar;
    public JMenu menu;
    /**
     * Constructor for GUI class, finishing the player registration process and board graphics initialization
     */
    private GUI(){
        number_of_players = registerNumberofPlayers();
        int i = 0;
        names = registerNames();
        while(names == null){
            number_of_players = registerNumberofPlayers();
            names = registerNames();
        }
        while (i != number_of_players) {
            if (names[i].equals("")) {
                i = 0;
                displayErrorMessage("Cannot register a player with empty name!");
                names = registerNames();
                while (names == null) {
                    number_of_players = registerNumberofPlayers();
                    names = registerNames();
                }
            } else if (names[i].equals("Fox the Thief")) {
                i = 0;
                displayErrorMessage("Ah, I see you found the secret name. Congrats, now you know my name, " +
                        "but if you speak to anyone of this, I will hurt Dora!");
                names = registerNames();
                while (names == null) {
                    number_of_players = registerNumberofPlayers();
                    names = registerNames();
                }
            } else {
                i++;
            }
        }
        // creating main board
        board = new JPanel();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        board_size = dim.height/1.30;
        board.setLayout(new BorderLayout((int) (board_size/28.8), 0));
        table = new BoardPanel();
        board.add("Center", table);

        // side panel for navigation and characters
        JPanel navigation = new JPanel();
        navigation.setSize((int) board_size, (int) board_size);
        navigation.setLayout(new GridBagLayout());
        // information
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.ipady = (int) (board_size/28.8);
        info = new JLabel("<html>"+ names[0] +"<br/><br/><br/>Use Character</html>", JLabel.CENTER);
        info.setFont(new Font("Verdana", Font.PLAIN, (int) board_size/48));
        info.setBounds(0,0,(int)(board_size/1.728),(int)(board_size/8.64));
        navigation.add(info, gbc);
        // NOTE THE ORDER MUST MATCH PLAYER'S ARRAYLIST ORDER
        String[] charact = {"assistant", "archaeologist", "digger", "professor"};
        characters = new ArrayList<>(charact.length);
        for(i = 0; i < charact.length; i++){
            BufferedImage hero = null;
            try {
                hero = ImageIO.read(new File("images_2020\\" + charact[i] + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image dimg1 = hero.getScaledInstance((int) (board_size/6.5), (int) board_size/4,
                    Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(dimg1);
            characters.add(new JButton(icon));
            gbc.gridy =  i/2 + 1;
            gbc.gridx = i%2;
            characters.get(i).setBorderPainted(false);
            characters.get(i).setContentAreaFilled(false);
            navigation.add(characters.get(i), gbc);
        }
        // buttons
        draw_tiles = new JButton("Draw Tiles");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = (int) board_size/10;
        gbc.weightx = 0.0;
        gbc.gridwidth = 3;
        navigation.add(draw_tiles, gbc);
        end_turn = new JButton("End Turn");
        gbc.gridx = 0;
        gbc.gridy = 4;
        navigation.add(end_turn, gbc);
        board.add("East", navigation);
        // My collection button
        BufferedImage collection_icon = null;
        try {
            collection_icon = ImageIO.read(new File("images_2020\\tile_collection.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg1 = collection_icon.getScaledInstance((int) (board_size/3), (int) (board_size/6.5),
                Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(dimg1);
        collection = new JButton(icon);
        collection.setContentAreaFilled(false);
        collection.setBorderPainted(false);
        board.add(collection, "South");
        save = new JMenuItem("Save Game");
        load = new JMenuItem("Load Game");
        menubar =  new JMenuBar();
        menu = new JMenu("Game");
        menu.add(save);
        menu.addSeparator();
        menu.add(load);
        menubar.add(menu);

        // add components to JFrame
        main_frame = new JFrame("Amphipolis");
        main_frame.add(board);
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setJMenuBar(menubar);
        main_frame.pack();
        main_frame.setLocationRelativeTo(null);
        main_frame.setSize((int)(board_size*1.48), (int)(board_size*1.24));
        main_frame.setResizable(false);
        main_frame.setLayout(null);
        main_frame.setVisible(true);
    }

    /**
     * Getter for GUI instance, since its a singleton class
     * @return GUI instance
     */
    public static GUI getInstance() {
        if(instance == null){
            instance = new GUI();
        }
        return instance;
    }

    /**
     * Getter for the number of players registered.
     * @return the number of players
     */
    public int getNumber_of_players(){
        return number_of_players;
    }

    /**
     * Getter for the names array
     * @return string array containing all names
     */
    public String[] getNames(){
        return names;
    }
    /**
     * Getting the number of players from the user.
     * @return the number of players user chose.
     */
    private int registerNumberofPlayers(){
        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setSize(new Dimension(250, 250));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(false);

        String[] players = {"1", "2", "3", "4"};

        Object selected = JOptionPane.showInputDialog(frame,
                null,
                "Choose number of players:",
                JOptionPane.PLAIN_MESSAGE,
                null, players,
                players[3]);

        frame.dispose();

        if(selected == null) {
            System.exit(0);
        }

        String selectedString = selected.toString();
        return Integer.parseInt(selectedString);
    }

    /**
     * Creates a JPanel with number_of_players JTextFields for name input.
     * @return String array containing all names of the players, or null if the player chose any "Cancel" option.
     */
    private String[] registerNames(){
        String[] names = new String[number_of_players];
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(number_of_players, 2, 2, 2));
        JTextField[] name_fields = new JTextField[number_of_players];
        CharacterColor[] color = CharacterColor.values();
        for(int i = 0; i < number_of_players; i++){
            name_fields[i] = new JTextField(10);
            name_fields[i].setText("Player" + (i + 1));
            panel.add(new JLabel(color[i].toString() + ":"));
            panel.add(name_fields[i]);
        }
        int choice = JOptionPane.showConfirmDialog(null, panel, "Register players' names.", JOptionPane.OK_CANCEL_OPTION);
        if(choice != JOptionPane.OK_OPTION){
            return null;
        } else{
            choice = JOptionPane.showConfirmDialog(null, "Confirm names?");
            while(choice == JOptionPane.NO_OPTION){
                choice = JOptionPane.showConfirmDialog(null, panel, "Register players' names.", JOptionPane.OK_CANCEL_OPTION);
                if(choice != JOptionPane.OK_OPTION){
                    return null;
                }
                choice = JOptionPane.showConfirmDialog(null, "Confirm names?");
            }
            if(choice != JOptionPane.OK_OPTION){
                return null;
            }
        }
        for(int i = 0; i < number_of_players; i++){
            names[i] = name_fields[i].getText();
        }
        return names;
    }

    /**
     * Method that simply displays an Error message.
     * @param message string of the message
     */
    private void displayErrorMessage(String message){
        JOptionPane.showMessageDialog(null, message);
    }


    /**
     * Creates a JFrame with player's possessions
     */
    public void view_collection(){
        JPanel tiles = new JPanel();
        tiles.setLayout(new GridLayout(3,4));
        Player player = Board.getInstance().getNowPlaying();

        JLabel Amphora = new JLabel("Blue Amphoras: "+ player.getAmphoraCount(AmphoraColor.BLUE));
        Amphora.setIcon(new ImageIcon("images_2020\\amphora_blue.png"));
        tiles.add(Amphora);
        Amphora = new JLabel("Red Amphoras: "+ player.getAmphoraCount(AmphoraColor.RED));
        Amphora.setIcon(new ImageIcon("images_2020\\amphora_red.png"));
        tiles.add(Amphora);
        Amphora = new JLabel("Brown Amphoras: "+ player.getAmphoraCount(AmphoraColor.BROWN));
        Amphora.setIcon(new ImageIcon("images_2020\\amphora_brown.png"));
        tiles.add(Amphora);
        Amphora = new JLabel("Green Amphoras: "+ player.getAmphoraCount(AmphoraColor.GREEN));
        Amphora.setIcon(new ImageIcon("images_2020\\amphora_green.png"));
        tiles.add(Amphora);
        Amphora = new JLabel("Purple Amphoras: "+ player.getAmphoraCount(AmphoraColor.PURPLE));
        Amphora.setIcon(new ImageIcon("images_2020\\amphora_purple.png"));
        tiles.add(Amphora);
        Amphora = new JLabel("Yellow Amphoras: "+ player.getAmphoraCount(AmphoraColor.YELLOW));
        Amphora.setIcon(new ImageIcon("images_2020\\amphora_yellow.png"));
        tiles.add(Amphora);

        JLabel mosaic = new JLabel("Red Mosaics: " + player.getMosaicCount(MosaicColor.RED));
        mosaic.setIcon(new ImageIcon("images_2020\\mosaic_red.png"));
        tiles.add(mosaic);
        mosaic = new JLabel("Green Mosaics: " + player.getMosaicCount(MosaicColor.GREEN));
        mosaic.setIcon(new ImageIcon("images_2020\\mosaic_green.png"));
        tiles.add(mosaic);
        mosaic = new JLabel("Yellow Mosaics: " + player.getMosaicCount(MosaicColor.YELLOW));
        mosaic.setIcon(new ImageIcon("images_2020\\mosaic_yellow.png"));
        tiles.add(mosaic);

        JLabel skelly = new JLabel("Upper Adult skeleton tiles: " + player.getSkeletonCount(true, true));
        skelly.setIcon(new ImageIcon("images_2020\\skeleton_big_top.png"));
        tiles.add(skelly);
        skelly = new JLabel("Lower Adult skeleton tiles: " + player.getSkeletonCount(false, true));
        skelly.setIcon(new ImageIcon("images_2020\\skeleton_big_bottom.png"));
        tiles.add(skelly);
        skelly = new JLabel("Upper Child skeleton tiles: " + player.getSkeletonCount(true, false));
        skelly.setIcon(new ImageIcon("images_2020\\skeleton_small_top.png"));
        tiles.add(skelly);
        skelly = new JLabel("Lower Child skeleton tiles: " + player.getSkeletonCount(false, false));
        skelly.setIcon(new ImageIcon("images_2020\\skeleton_small_bottom.png"));
        tiles.add(skelly);

        JLabel statues = new JLabel("Sphinx Statue tiles: " + player.getStatueCount(true));
        statues.setIcon(new ImageIcon("images_2020\\sphinx.png"));
        tiles.add(statues);
        statues = new JLabel("Caryatid Statue tiles: " + player.getStatueCount(false));
        statues.setIcon(new ImageIcon("images_2020\\caryatid.png"));
        tiles.add(statues);

        players_collection = new JFrame(Board.getInstance().getPlayerName() + "'s Collection");
        players_collection.add(tiles);
        players_collection.pack();
        players_collection.setLocationRelativeTo(board);
        players_collection.setResizable(false);
        players_collection.setLayout(null);
        players_collection.setVisible(true);
    }

    /**
     * The player who will be praised with a JFrame, and also shows the scoreboard.
     * @pre Player must not be NULL.
     * @param winner Player instance that will be praised
     */
    public void display_winner(Player winner){
        JFrame results_frame = new JFrame("Results");
        JLabel results_label, score_board;
        JPanel results =  new JPanel();
        StringBuilder board_body;
        String name;
        if(winner == null){
            name = "Draw";
        } else{
            name = winner.getName() + "!!!";
        }
        results_label = new JLabel("<html>Game Ended!<br/><br/>And the winner is...:<br/>" + name + "<br/><br/></html>", JLabel.LEFT);
        results_label.setFont(new Font("Verdana", Font.PLAIN, (int) board_size/40));
        results.add(results_label);

        board_body = new StringBuilder("<html>Score Board:<br/><br/>");
        for(Player player: Board.getInstance().getPlayers()){
            board_body.append(player.getName()).append(":").append(player.getPoints()).append("<br/>");
        }
        board_body.append("</html>");
        score_board = new JLabel(board_body.toString(), JLabel.LEFT);
        score_board.setFont(new Font("Verdana", Font.PLAIN, (int) board_size/40));
        results.add(score_board);

        results_frame.add(results);
        results_frame.setLocationRelativeTo(board);
        results_frame.setSize((int)(board_size/2.5), (int)(board_size/2));
        results_frame.setResizable(false);
        results_frame.setVisible(true);
    }
    public void setNumberOfPlayers(int number_of_players){
        this.number_of_players = number_of_players;
    }

}
