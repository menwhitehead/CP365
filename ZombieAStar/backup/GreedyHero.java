import java.util.Random;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
       
       
public class GreedyHero extends Hero {
   
    public GreedyHero() {
        // init junk here
    }

    public void findPath(ZombieEscape game) {
        
         while (!game.foundGoal()) {
            ArrayList<Location> legal_moves = game.getLegalMoves(game.state.hero_location);
            
            Location best = legal_moves.get(0);
            double min_dist = best.distanceTo(game.state.zombie_location);
            
            for (Location loc : legal_moves) {
                double dist = loc.distanceTo(game.state.zombie_location);
                if (dist < min_dist) {
                    min_dist = dist;
                    best = loc;
                }
            }
            
            System.out.println("MOVING: " + best);
            game.state.hero_location = best;
            try {
                Thread.sleep(1000);
                game.repaint();
                System.out.println("TICK");
            } catch (InterruptedException e) {
                System.out.println("Execution interrupted!");
            }
            game.move_number++;
        }   
    }
}
