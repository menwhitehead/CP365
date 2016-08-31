import java.util.Scanner;


class HumanPlayer extends SlidingPlayer {
    
    private Scanner s;
    
    public HumanPlayer(SlidingBoard _sb) {
        super(_sb);
        s = new Scanner(System.in);
    }
    
    public SlidingMove makeMove(SlidingBoard board) {        
        while (true) {
            System.out.println(board);
            System.out.println("Which move?");
            String line = s.nextLine();
            String tokens[] = line.split(" ");
            SlidingMove m = new SlidingMove(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
            if (board.isLegalMove(m)) {
                return m;
            }
        }
    }
    
}