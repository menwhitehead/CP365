import java.util.Random;
import java.util.ArrayList;
import java.lang.reflect.*;

class SlidingMove {

    public int row;
    public int col;

    public SlidingMove(int _row, int _col) {
        row = _row;
        col = _col;
    }

    public String toString() {
        return "MOVE: " + row + ", " + col;
    }
}


class SlidingPlayer {

    SlidingBoard sb;

    public SlidingPlayer(SlidingBoard _sb) {
        sb = _sb;
    }

    /*
     *override this method! :P
     */
    public SlidingMove makeMove(SlidingBoard board) {
        return null;
    }
}


class SlidingBoard {

    public int size;
    public int[][] board = null;

    public SlidingBoard(int _size) {
        size = _size;
        board = new int[size][size];
        //initBoard();
        //randomizeBoard();
    }

    public void setBoard(SlidingBoard otherBoard) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = otherBoard.board[r][c];
            }
        }
    }


    public void initBoard() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = r * size + c;
            }
        }
    }


    public void randomizeBoard() {
        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            ArrayList<SlidingMove> legalMoves = getLegalMoves();
            int choice = r.nextInt(legalMoves.size());
            doMove(legalMoves.get(choice));
        }
    }


    public boolean isLegalMove(SlidingMove m) {
        if (m.row - 1 >= 0 && board[m.row-1][m.col] == 0) return true;
        if (m.row + 1 < size && board[m.row+1][m.col] == 0) return true;
        if (m.col - 1 >= 0 && board[m.row][m.col-1] == 0) return true;
        if (m.col + 1 < size && board[m.row][m.col+1] == 0) return true;
        return false;
    }


    public ArrayList<SlidingMove> getLegalMoves() {
        ArrayList<SlidingMove> legalMoves = new ArrayList<SlidingMove>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                SlidingMove m = new SlidingMove(r, c);
                if (isLegalMove(m)) {
                    legalMoves.add(m);
                }
            }
        }
        return legalMoves;
    }


    public boolean isSolved() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] != r * size + c) {
                    return false;
                }
            }
        }
        return true;
    }

    public void undoMove(SlidingMove m, int direction) {

        // had moved up
        if (direction == 0) {
            doMove(new SlidingMove(m.row-1, m.col));
        }
        else if (direction == 1) {
            doMove(new SlidingMove(m.row+1, m.col));
        }
        else if (direction == 2) {
            doMove(new SlidingMove(m.row, m.col-1));
        }
        else if (direction == 3) {
            doMove(new SlidingMove(m.row, m.col+1));
        }

    }

    public int doMove(SlidingMove m) {
        if (m.row - 1 >= 0 && board[m.row-1][m.col] == 0) {
            int tmp = board[m.row-1][m.col];
            board[m.row-1][m.col] = board[m.row][m.col];
            board[m.row][m.col] = tmp;
            return 0;
        }
        else if (m.row + 1 < size && board[m.row+1][m.col] == 0) {
            int tmp = board[m.row+1][m.col];
            board[m.row+1][m.col] = board[m.row][m.col];
            board[m.row][m.col] = tmp;
            return 1;
        }
        else if (m.col - 1 >= 0 && board[m.row][m.col-1] == 0) {
            int tmp = board[m.row][m.col-1];
            board[m.row][m.col-1] = board[m.row][m.col];
            board[m.row][m.col] = tmp;
            return 2;
        }
        else if (m.col + 1 < size && board[m.row][m.col+1] == 0) {
            int tmp = board[m.row][m.col+1];
            board[m.row][m.col+1] = board[m.row][m.col];
            board[m.row][m.col] = tmp;
            return 3;
        }
        return -1;
    }


    public String toString() {
        String result = "";
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                result += board[r][c] + " ";
            }
            result += "\n";
        }
        return result;
    }
}


class SlidingGame {

    public static int playGame(SlidingBoard sb, SlidingPlayer player, boolean viewPlayback, int playbackDelay) {
        int moves = 0;

        while (!sb.isSolved()) {
            SlidingMove m = player.makeMove(sb);
            if (sb.isLegalMove(m)) {
                sb.doMove(m);
            }

            moves++;
            /*if (moves % 1000000 == 0) {
                System.out.println("MOVES: " + moves);
            }*/

            if (viewPlayback) {
                System.out.println(sb);
                try {
                  Thread.sleep(playbackDelay);
                } catch (InterruptedException e) {
                    System.out.println("Execution interrupted!");
                }
            }


        }

        return moves;
    }


    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {


        if (args.length < 5) {
            System.out.println("USAGE:\n\t java SlidingGame BotClassName BOARD_SIZE NUMBER_GAMES PLAYBACK_DISPLAY PLAYBACK_DELAY\n");
            System.out.println("EXAMPLE with RandomBot playing 10 games with display turned off:\n\t java SlidingGame RandomBot 3 10 0 0");
            System.out.println("\nEXAMPLE with RandomBot playing 10 games with display turned on and 100ms delay:\n\t java SlidingGame RandomBot 3 10 1 100");
            System.out.println("\nPlay a game as a human:\n\t java SlidingGame HumanPlayer 3 1 0 0");

            System.exit(1);
        }

        String botClassName = args[0];
        int BOARD_SIZE = Integer.parseInt(args[1]);
        int NUMBER_GAMES = Integer.parseInt(args[2]);
        boolean viewPlayback = Integer.parseInt(args[3]) > 0;
        int PLAYBACK_DELAY = Integer.parseInt(args[4]);


        SlidingBoard sb = new SlidingBoard(BOARD_SIZE);
        sb.initBoard();
        sb.randomizeBoard();

        Class cl = Class.forName(botClassName);
        Constructor con = cl.getConstructor(SlidingBoard.class);
        SlidingPlayer player = (SlidingPlayer)con.newInstance(sb);

        int totalMoves = 0;
        for (int i = 0; i < NUMBER_GAMES; i++) {
            totalMoves += playGame(sb, player, viewPlayback, PLAYBACK_DELAY);
            sb.randomizeBoard();
        }
        System.out.println("TOTAL:" + totalMoves);
        System.out.println("Average moves required: " + totalMoves / NUMBER_GAMES);
    }

}
