import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Scanner;


class Location {

    public int x;
    public int y;

    public Location(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public double distanceTo(Location other) {
        return Math.sqrt((x-other.x) * (x - other.x) + (y-other.y) * (y - other.y));
    }

    public boolean equals(Object other) {
        Location oth = (Location) other;
        return x == oth.x && y == oth.y;
    }

    public int hashCode() {
        return new Integer(x*1000 + y).hashCode();
    }

    public String toString() {
        return "LOCATION: " + x + ", " + y;
    }
}

class GameState {

    public Location hero_location;
    public Location zombie_location;

    public GameState(Location _hero_location, Location _zombie_location) {
        hero_location = _hero_location;
        zombie_location = _zombie_location;
    }

    public boolean equals(Object other) {
        GameState oth = (GameState) other;
        return (hero_location.equals(oth.hero_location) && zombie_location.equals(oth.zombie_location));
    }

    public int hashCode() {
        return hero_location.hashCode() * 1000 + zombie_location.hashCode();
    }

    public String toString() {
        return "HERO: " + hero_location + ", ZOMBIE: " + zombie_location;
    }
}



class ZombieBoardViewer extends JComponent {

    ZombieEscape ze = null;
    Font font;
    ArrayList<Location> path = null;

    BufferedImage treeImage;
    BufferedImage heroImage;
    BufferedImage zombieImage;
    BufferedImage graveImage;

    public ZombieBoardViewer(ZombieEscape _ze) {
        super();
        ze = _ze;
        font = new Font("Verdana", Font.BOLD, 24);

        try {
            treeImage = ImageIO.read(new File(ze.TREE_FILENAME));
            heroImage = ImageIO.read(new File(ze.HERO_FILENAME));
            zombieImage = ImageIO.read(new File(ze.ZOMBIE_FILENAME));
            graveImage = ImageIO.read(new File(ze.GRAVE_FILENAME));
        }
        catch (IOException e) {
            System.out.println("ERROR: " + e);
        }
    }


    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //int squareWidth = 100;
        int squareWidth = ze.BOXSIZE;
        //int squareHeight = 100;
        int squareHeight = ze.BOXSIZE;

        for (int x = ze.roomMinX; x < ze.roomMaxX + 1; x++) {
            for (int y = ze.roomMinY; y < ze.roomMaxY + 1; y++) {
                Location current = new Location(x, y);

                if (ze.rooms.containsKey(current)) {
                    if (ze.state.zombie_location.equals(current) && ze.state.hero_location.equals(current)) {
                        g2.drawImage(graveImage, x * squareWidth, y * squareHeight, this);
                    }
                    else if (ze.state.zombie_location.equals(current)) {
                        g2.drawImage(zombieImage, x * squareWidth, y * squareHeight, this);

                    }
                    else if (ze.state.hero_location.equals(current)) {
                        g2.drawImage(heroImage, x * squareWidth, y * squareHeight, this);
                    }
                    else {
                        g.setColor(Color.WHITE);  // it's empty
                        g.fillRect(x * squareWidth , y * squareHeight, squareWidth, squareHeight);
                    }
                }
                else {
                    // It's a tree
                    g.drawImage(treeImage, x * squareWidth, y * squareHeight, null);
                }
            }
        }

        // Draw number of moves
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("" + ze.move_number, 10, 20);
    }
}





public class ZombieEscape {

    public Hero hero;
    public HashMap<Location, Boolean> rooms;

    public int roomMinX;
    public int roomMaxX;
    public int roomMinY;
    public int roomMaxY;

    public int map_size;
    public int move_number;

    public GameState state;

    public static final int BOXSIZE = 20; // size of the images used
    //public static final int BOXSIZE = 100; // normal size

    // image filenames
    public static final String TREE_FILENAME = "tree_tiny.gif";
    public static final String GRAVE_FILENAME = "grave_tiny.gif";
    public static final String ZOMBIE_FILENAME = "zombie_tiny.gif";
    public static final String HERO_FILENAME = "hero_tiny.gif";

    public boolean hasViewWindow;
    JFrame viewer = null;
    ZombieBoardViewer zbv = null;

    public ZombieEscape(Hero h, int _map_size, boolean _hasViewWindow) {
        map_size = _map_size;
        move_number = 0;
        rooms = new HashMap<Location, Boolean>();

        hero = h;

        hasViewWindow = _hasViewWindow;
        if (hasViewWindow) {
            viewer = new JFrame();
            viewer.setTitle("Zombie Viewer");
            viewer.setSize(BOXSIZE * map_size+10, BOXSIZE * map_size+30);
            zbv = new ZombieBoardViewer(this);
            viewer.add(zbv);
            viewer.setVisible(true);
            viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        reset();
        initRoomBounds();
    }


    public void repaint() {
        viewer.repaint();
    }

    public boolean foundGoal() {
        return state.zombie_location.equals(state.hero_location);
    }

    public void initRoomBounds() {
        roomMinX = 0;
        roomMaxX = 0;
        roomMinY = 0;
        roomMaxY = 0;
    }


    public void reset() {
        state = new GameState(new Location(0, 0), new Location(map_size-1, map_size-1));
    }


    public void addRoom(int x, int y) {
        Location room_location = new Location(x, y);
        if (x < roomMinX) {
            roomMinX = x;
        }
        else if (x > roomMaxX) {
            roomMaxX = x;
        }
        if (y < roomMinY) {
            roomMinY = y;
        }
        else if (y > roomMaxY) {
            roomMaxY = y;
        }

        rooms.put(room_location, true);
    }


    public boolean isLegalMove(Location start, Location end) {
        if (start.x == end.x && Math.abs(start.y - end.y) <= 1) {
            return true;
        }
        else if (start.y == end.y && Math.abs(start.x - end.x) <= 1) {
            return true;
        }
        return false;
    }


    public ArrayList<Location> getLegalMoves(Location current_location) {
        ArrayList<Location> legal_moves = new ArrayList<Location>();

        // Can always stay put
        legal_moves.add(current_location);

        Location loc = new Location(current_location.x - 1, current_location.y);

        //if (hashMapHasLocation(rooms, loc)) {
        if (rooms.containsKey(loc)){
            legal_moves.add(loc);
        }

        loc = new Location(current_location.x + 1, current_location.y);
        if (rooms.containsKey(loc)) {
            legal_moves.add(loc);
        }

        loc = new Location(current_location.x, current_location.y - 1);
        if (rooms.containsKey(loc)) {
            legal_moves.add(loc);
        }

        loc = new Location(current_location.x, current_location.y + 1);
        if (rooms.containsKey(loc)) {
            legal_moves.add(loc);
        }
        return legal_moves;
    }


    public void drawBoard() {

        for (int x = roomMinX; x < roomMaxX + 1; x++) {
            for (int y = roomMinY; y < roomMaxY + 1; y++) {
                Location current = new Location(x, y);
                if (rooms.containsKey(current)) {
                    if (state.zombie_location.equals(current) && state.hero_location.equals(current)) {
                        System.out.print("*");
                    }
                    else if (state.hero_location.equals(current)) {
                        System.out.print("h");
                    }
                    else {
                        System.out.print("e");
                    }
                }
                else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println("\n");
    }


    public void loadWorld(String filename) {
        try {
            Scanner s = new Scanner(new File(filename));
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] tokens = line.split(" ");

                addRoom(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));

                System.out.println(line);
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("ERROR: " + e);
        }
    }


    public void goHeroGo() {
        hero.findPath(this);
    }



    public static void main(String[] args) {
        // Game parameters
        int world_size = 30;
        boolean view_game = true;
        double wall_density = 0.3;

        // GreedyHero hero = new GreedyHero();
        AStarHero hero = new AStarHero();

        ZombieEscape ze = new ZombieEscape(hero, world_size, view_game);
        //ze.loadWorld("world.dat");
        for (int x = 0; x < world_size; x++) {
            for (int y = 0; y < world_size; y++) {
                if ((x==0&&y==0) || (x==world_size-1&&y==world_size-1) || Math.random() < (1.0 - wall_density)) {
                    ze.addRoom(x, y);
                }
            }
        }

        ze.goHeroGo();
    }
}
