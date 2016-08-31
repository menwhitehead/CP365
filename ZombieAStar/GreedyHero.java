import java.util.Random;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;


public class GreedyHero extends Hero {

    public void findPath(ZombieEscape game) {

         while (!game.foundGoal()) {

            ArrayList<Location> legal = game.getLegalMoves(game.state.hero_location);

            double shortest = Double.MAX_VALUE;
            Location best_move = legal.get(0);
            for (Location move : legal) {
              double dist = move.distanceTo(game.state.zombie_location);
              if (dist < shortest) {
                shortest = dist;
                best_move = move;
              }
            }

            System.out.println("MOVING: " + best_move);
            game.state.hero_location = best_move;
            try {
                Thread.sleep(250);
                game.repaint();
                System.out.println("TICK");
            } catch (InterruptedException e) {
                System.out.println("Execution interrupted!");
            }
            game.move_number++;
        }
    }
}
