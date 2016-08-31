import java.util.Random;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.PriorityQueue;
       
       
class AStarNode implements Comparable {
    
    public Location loc = null;
    public double cost = 0.0;
    public int move_number = 0;
    
    public AStarNode(Location curr_loc, Location start, Location end, int _move_number) {
        move_number = _move_number;
        loc = curr_loc;
        double startDist = loc.distanceTo(start);
        double endDist = loc.distanceTo(end);
        cost = endDist + move_number;
    }
    
    public int compareTo(Object otherNode) {
        AStarNode other = (AStarNode) otherNode;
        return new Double(cost).compareTo(new Double(other.cost));
    }
    
    public String toString() {
        return "ASTARNODE: " + loc + "\t" + cost + "\n";
    }
    
       
}
public class AStarHero extends Hero {
   
    public AStarHero() {
        // init junk here
    }
    
    public void findPath(ZombieEscape game) {
        
        PriorityQueue<AStarNode> q = new PriorityQueue<>();
        HashMap<Location, Boolean> visited = new HashMap<>();
        
        Location start = game.state.hero_location;
        Location end = game.state.zombie_location;
        
        AStarNode first = new AStarNode(start, start, end, 0);
        q.add(first);
        
        while (!game.foundGoal()) {
            
            AStarNode curr = q.poll();
            visited.put(curr.loc, true);
            game.state.hero_location = curr.loc;
            
            ArrayList<Location> legal_moves = game.getLegalMoves(curr.loc);
            
            for (Location loc : legal_moves) {
                if (!visited.containsKey(loc)) {
                    AStarNode currA = new AStarNode(loc, start, end, curr.move_number + 1);
                    q.add(currA);
                    visited.put(loc, true);
                }
            }
            
            System.out.println(q);
            
            try {
                Thread.sleep(50);
                game.repaint();
                System.out.println("TICK");
            } catch (InterruptedException e) {
                System.out.println("Execution interrupted!");
            }
            game.move_number++;
        }   
    }
}
