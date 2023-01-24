import java.util.ArrayList;
import java.util.List;

/*
DOCUMENTATION:
Board origin is bottom left corner
 */
public class ChessEngine {
    final int WIDTH = 8;
    final int HEIGHT = 8;
    int turn = 1;
    int[][] board = new int[WIDTH][HEIGHT];
    List<String> moveNotation = new ArrayList<>();

    public enum Piece {NOP, KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN}
    final public int[] piece = {0, 1, 2, 3, 4, 5, 6};



    public ChessEngine() {
        initBoard();
    }

    // private Tool Methods
    public int getPiece(int id){
        return id & 0x7;
    }
    private boolean getTeam(int id) {
        // gets team of id
        return getBit(id, 3) == 1;
    }
    private void setBoardOn(int x, int y, int id){
        board[x][y] = id;
    }
    private void clearTile(int x, int y){
        // clears tile
        board[x][y] = 0;
    }
    private void placeNewPiece(int x, int y, int id, boolean team) {
        // creates new pieces and places
        board[x][y] = createNewPiece(id, team);
    }

    private int createNewPiece(int id, boolean team) {
        // creates new player with id
        int result = id; // set piece
        result = setBit(result, 3, team ? 1 : 0); // set team
        return result;
    }

    private void initBoard() {
        // setup new chessboard
        for (int i = 0; i < WIDTH; i++) {
            placeNewPiece(i, 1, Piece.PAWN.ordinal(), true);
            placeNewPiece(i, 6, Piece.PAWN.ordinal(), false);
        }
        // ROOK
        placeNewPiece(0, 0, Piece.ROOK.ordinal(), true);
        placeNewPiece(7, 0, Piece.ROOK.ordinal(), true);
        placeNewPiece(0, 7, Piece.ROOK.ordinal(), false);
        placeNewPiece(7, 7, Piece.ROOK.ordinal(), false);
        // KNIGHT
        placeNewPiece(1, 0, Piece.KNIGHT.ordinal(), true);
        placeNewPiece(6, 0, Piece.KNIGHT.ordinal(), true);
        placeNewPiece(1, 7, Piece.KNIGHT.ordinal(), false);
        placeNewPiece(6, 7, Piece.KNIGHT.ordinal(), false);
        // BISHOP
        placeNewPiece(2, 0, Piece.BISHOP.ordinal(), true);
        placeNewPiece(5, 0, Piece.BISHOP.ordinal(), true);
        placeNewPiece(2, 7, Piece.BISHOP.ordinal(), false);
        placeNewPiece(5, 7, Piece.BISHOP.ordinal(), false);
        // KING and QUEEN
        placeNewPiece(3, 0, Piece.QUEEN.ordinal(), true);
        placeNewPiece(4, 0, Piece.KING.ordinal(), true);
        placeNewPiece(3, 7, Piece.QUEEN.ordinal(), false);
        placeNewPiece(4, 7, Piece.KING.ordinal(), false);
    }

    // private Piece Move Validation Methods
    private boolean legalMoveKing(int oldX, int oldY, int x, int y){
        return false;
    }
    private boolean legalMoveQueen(int oldX, int oldY, int x, int y){
        return false;
    }
    private boolean legalMoveBishop(int oldX, int oldY, int x, int y){
        return false;
    }
    private boolean legalMoveKnight(int oldX, int oldY, int x, int y){
        return false;
    }
    private boolean legalMoveRook(int oldX, int oldY, int x, int y){
        return false;
    }
    private boolean legalMovePawn(int oldX, int oldY, int x, int y){
        return false;
    }


    // private Helper Methods
    private int setBit(int number, int idx, int value) {
        if (value != 0 && value != 1) {
            return -1;
        }
        return number | (value << idx);
    }

    private int getBit(int number, int idx) {
        return (number >> idx) & 1;
    }

    // public Methods -------------------
    public int[][] getBoardArr() {
        return board;
    }

    public int getBoardOn(int x, int y) {
        return board[x][y];
    }

    public boolean getTurn() {
        return turn % 2 == 1;
    }

    public boolean canMove(int oldX, int oldY, int x, int y) {
        // TODO major and most important
        // check turn
        if (getTurn() != getTeam(getBoardOn(oldX, oldY))) {
            return false;
        }
        // check if newpos empty or opponent
        int zwNewId = getBoardOn(x, y);
        if(zwNewId != 0 && getTeam(zwNewId) == getTurn()){
            return false;
        }
        // check if legal piece move
        int zwOldId = getBoardOn(oldX, oldY);
        switch (Piece.values()[getPiece(zwOldId)]){
            case KING:
                System.out.println("King");
                break;
            case QUEEN:
                System.out.println("queen");
                break;
            case BISHOP:
                System.out.println("bishop");
                break;
            case KNIGHT:
                System.out.println("knight");
                break;
            case ROOK:
                System.out.println("rook");
                break;
            case PAWN:
                System.out.println("pawn");
                break;
            default:
                System.out.println("ERROR: original Piece not found");
                break;
        }

        // check if in check after move

        return true;//temp
    }

    public boolean makeMove(int oldX, int oldY, int x, int y) {
        if (canMove(oldX, oldY, x, y)) {
            int zwId = getBoardOn(oldX, oldY);
            setBoardOn(x, y, zwId);
            clearTile(oldX, oldY);
            turn++;
            // (TODO other stuff to update when making a move)
            return true;
        } else {
            return false;
        }
    }

    public List<String> getBoardLAN() {
        return moveNotation; // TODO later later
    }

    public boolean setBoardLAN(List<String> input) {
        return false; // TODO for later later
    }

    // public testing methods
    public void printBoard() {
        for (int i = HEIGHT - 1; i >= 0; i--) {
            for (int j = 0; j < WIDTH; j++) {
                String a = Integer.toString(board[j][i], 2);
//                String a = Integer.toString(getPiece(getBoardOn(j, i)), 2);
                String res = "";
                for (int k = 0; k < 5 - a.length(); k++) {
                    res += " ";
                }
                System.out.print(res + a + "|");
            }
            System.out.println("\n-----------------------------------------------");
        }
    }
}
