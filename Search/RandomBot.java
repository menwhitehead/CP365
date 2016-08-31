import java.util.Random;
import java.util.ArrayList;

// SlidingBoard has a public field called size
// A size of 3 means a 3x3 board

// SlidingBoard has a method to getLegalMoves
//   ArrayList<SlidingMove> legalMoves = board.getLegalMoves();

// You can create possible moves using SlidingMove:
// This moves the piece at (row, col) into the empty slot
//   SlidingMove move = new SlidingMove(row, col);

// SlidingBoard can check a single SlidingMove for legality:
//   boolean legal = board.isLegalMove(move);

// SlidingBoard can check if a position is a winning one:
//   boolean hasWon = board.isSolved();

// SlidingBoard can perform a SlidingMove:
//   board.doMove(move);

// You can undo a move by saying the direction of the previous move
// For example, to undo the last move that moved a piece down into
// the empty space from above use:
//   board.undoMove(m, 0);

// You can dump the board to view with toString:
//   System.out.println(board);


class RandomBot extends SlidingPlayer {
    
    // The constructor gets the initial board
    public RandomBot(SlidingBoard _sb) {
        super(_sb);
    }
    
    // Perform a single move based on the current given board state
    public SlidingMove makeMove(SlidingBoard board) {
        Random r = new Random();
        
        ArrayList<SlidingMove> legalMoves = board.getLegalMoves();
        int choice = r.nextInt(legalMoves.size());
        return legalMoves.get(choice);
    }   
}