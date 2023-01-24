import java.util.ArrayList;
import java.util.List;

/*
DOCUMENTATION:
Board origin is bottom left corner
id:
    idx 0-2 = Piece type
    idx 3   = Team
    idx 4   = wasMoved flag
 */
public class ChessEngine {
    final int WIDTH = 8;
    final int HEIGHT = 8;
    int turn = 1;
    int[][] board = new int[WIDTH][HEIGHT];
    List<String> moveNotation = new ArrayList<>();
    public enum Piece {NOP, KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN}
    final public int[] piece = {0, 1, 2, 3, 4, 5, 6};
    boolean whiteKingMoved = false;
    boolean whiteRookKingSMoved = false;
    boolean whiteRookQueenSMoved = false;
    boolean blackKingMoved = false;
    boolean blackRookKingSMoved = false;
    boolean blackRookQueenSMoved = false;


    public ChessEngine() {
        initBoard();
    }

    // private Tool Methods
    public int getPiece(int id){
        // gets piece type of id
        return id & 0x7;
    }
    private boolean getTeam(int id) {
        // gets team of id
        return getBit(id, 3) == 1;
    }
    private int setWasMoved(int id){
        return setBit(id, 4, 1);
    }
    private boolean getWasMoved(int id){
        // gets wasMoved flag of id
        // useless right now
        return getBit(id, 4) == 1;
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
        boolean legalMove = Math.abs(oldX-x) <= 1 && Math.abs(oldY-y) <= 1;
        boolean castleKingside = (getTeam(getBoardOn(oldX, oldY)) && getBoardOn(5, 0) == 0 &&
                getBoardOn(6, 0) == 0 && !whiteKingMoved && !whiteRookKingSMoved) ||
                (!getTeam(getBoardOn(oldX, oldY)) && getBoardOn(5, 7) == 0 &&
                        getBoardOn(6, 7) == 0 && !blackKingMoved && !blackRookKingSMoved);
        // castleKingside = castleKingside && x4 x5 x6 mit y0 oder y7 not checked TODO
        boolean castleQueenside = (getTeam(getBoardOn(oldX, oldY)) && getBoardOn(3, 0) == 0 &&
                getBoardOn(2, 0) == 0 && !whiteKingMoved && !whiteRookQueenSMoved) ||
                (!getTeam(getBoardOn(oldX, oldY)) && getBoardOn(3, 7) == 0 &&
                        getBoardOn(2, 7) == 0 && !blackKingMoved && !blackRookQueenSMoved);
        // castleQueenside = castleQueenside && x4 x3 x2 mit y0 oder y7 not checked TODO
        return legalMove || castleKingside || castleQueenside;
    }
    private boolean legalMoveQueen(int oldX, int oldY, int x, int y){
        // legal position
        boolean legalPosition = (Math.abs(x - oldX) == 0 || Math.abs(y - oldY) == 0)
                || Math.abs(x - oldX) == Math.abs(y - oldY);
        if(!legalPosition){
            return false;
        }
        // legal motion
        boolean legalMotion = false;
        if(Math.abs(x - oldX) == 0 || Math.abs(y - oldY) == 0){
            legalMotion = legalStraight(oldX, oldY, x, y);
        }else if(Math.abs(x - oldX) == Math.abs(y - oldY)){
            legalMotion = legalDiagonal(oldX, oldY, x, y);
        }
        return legalMotion;
    }
    private boolean legalMoveBishop(int oldX, int oldY, int x, int y){
        // legal position
        boolean legalPosition = Math.abs(x - oldX) == Math.abs(y - oldY);
        if(!legalPosition){
            return false;
        }
        // legal motion
        return legalDiagonal(oldX, oldY, x, y);
    }
    private boolean legalMoveKnight(int oldX, int oldY, int x, int y){
        return Math.abs(x - oldX)*Math.abs(y - oldY) == 2;
    }
    private boolean legalMoveRook(int oldX, int oldY, int x, int y){
        // legal position
        boolean legalPosition = (Math.abs(x - oldX) == 0 || Math.abs(y - oldY) == 0);
        if(!legalPosition){
            return false;
        }
        // legal motion
        return legalStraight(oldX, oldY, x, y);
    }
    private boolean legalMovePawn(int oldX, int oldY, int x, int y){
        return true;//temp
    }//TODO
    // legal motion helper Methods
    private boolean legalStraight(int oldX, int oldY, int x, int y){
        boolean legalMotion = true;
        int xmovement = x - oldX;
        int ymovement = y - oldY;
        boolean directionX = ymovement == 0;
        int tile = directionX ? oldX : oldY;
        int newTile = directionX ? x : y;
        int movement = directionX ? xmovement : ymovement;
        int incr = 1;
        if (movement < 0) {
            incr = -1;
        }
        for (int i = tile + incr; incr > 0 ? i < newTile : i > newTile; i += incr) {
            if (directionX) {
                if (getBoardOn(i, oldY) != 0) {
                    legalMotion = false;
                }
            } else {
                if (getBoardOn(oldX, i) != 0) {
                    legalMotion = false;
                }
            }
        }
        return legalMotion;
    }
    private boolean legalDiagonal(int oldX, int oldY, int x, int y){
        boolean legalMotion = true;
        int xmovement = x - oldX;
        int ymovement = y - oldY;
        boolean xup = xmovement > 0;
        boolean yup = ymovement > 0;
        int incrX, incrY;

        if (xup && yup) { // up right /
            incrX = incrY = 1;
        } else if (!xup && !yup) { // down left /
            incrX = incrY = -1;
        } else if (!xup && yup) { // up left \
            incrX = -1;
            incrY = 1;
        } else { // down right \
            incrX = 1;
            incrY = -1;
        }
        for (int i = 1; i < Math.abs(xmovement); i++) {
            if(getBoardOn(oldX + incrX*i, oldY + incrY*i)!= 0){
                legalMotion = false;
            }
        }
        return legalMotion;
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
    public boolean isCheck(){
        return false; // TODO
    }
    public boolean isMate(){
        return false; // TODO
    }
    public boolean getTurn() {
        return turn % 2 == 1;
    }

    public boolean canMove(int oldX, int oldY, int x, int y) {
        // TODO major and most important
        // check empty
        if(getBoardOn(oldX, oldY) == 0){
            return false;
        }
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
        boolean legalMovePiece = false;
        int zwOldId = getBoardOn(oldX, oldY);
        switch (Piece.values()[getPiece(zwOldId)]){
            case KING:
                System.out.println("King");
                legalMovePiece = legalMoveKing(oldX, oldY, x, y);
                break;
            case QUEEN:
                System.out.println("queen");
                legalMovePiece = legalMoveQueen(oldX, oldY, x, y);
                break;
            case BISHOP:
                System.out.println("bishop");
                legalMovePiece = legalMoveBishop(oldX, oldY, x, y);
                break;
            case KNIGHT:
                System.out.println("knight");
                legalMovePiece = legalMoveKnight(oldX, oldY, x, y);
                break;
            case ROOK:
                System.out.println("rook");
                legalMovePiece = legalMoveRook(oldX, oldY, x, y);
                break;
            case PAWN:
                System.out.println("pawn");
                legalMovePiece = legalMovePawn(oldX, oldY, x, y);
                break;
            default:
                System.out.println("ERROR: original Piece not found");
                break;
        }
        if(!legalMovePiece){
            return false;
        }
        // check if in check after move

        return true;//temp
    }

    public boolean makeMove(int oldX, int oldY, int x, int y) {
        if (canMove(oldX, oldY, x, y)) {
            int zwId = getBoardOn(oldX, oldY);
            zwId = setWasMoved(zwId);
            setBoardOn(x, y, zwId);
            clearTile(oldX, oldY);
            turn++;
            // (TODO other stuff to update when making a move)
            // setting global King and Rook moved flag
            if(getPiece(zwId) == Piece.KING.ordinal()){
                if(getTeam(zwId)){
                    whiteKingMoved = true;
                }else{
                    blackKingMoved = true;
                }
            }else if(oldX == 0 && oldY == 0){
                whiteRookQueenSMoved = true;
            }else if(oldX == 7 && oldY == 0){
                whiteRookKingSMoved = true;
            }else if(oldX == 0 && oldY == 7){
                whiteRookQueenSMoved = true;
            }else if(oldX == 7 && oldY == 7){
                whiteRookKingSMoved = true;
            }
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
