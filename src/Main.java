import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ChessEngine chessEngine = new ChessEngine();
        while (true) {
            chessEngine.printBoard();
            Scanner scanner = new Scanner(System.in);
            System.out.println("from (x [space] y)");
            int fromx = scanner.nextInt();
            int fromy = scanner.nextInt();
            System.out.println(fromx + " kk " + fromy +"To (x [space] y)");
            int tox = scanner.nextInt();
            int toy = scanner.nextInt();
            System.out.println(chessEngine.makeMove(fromx, fromy, tox, toy));

        }
    }
}