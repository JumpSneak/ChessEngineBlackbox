import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessEngineTest {
    ChessEngine engine = new ChessEngine();
    @Test
    void getPiece() {
        int[][] cases = {
                {0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {9, 1}, {10, 2}, {14, 6}
        };
        for (int i = 0; i < cases.length; i++) {
            assertEquals(cases[i][1], engine.getPiece(cases[i][0]));
        }
    }

    @Test
    void getTeam() {
        int[][] cases = {
                {0, 0}, {8, 1}, {15, 1}, {6, 0}, {18, 0}, {26, 1}, {14, 1}
        };
        for (int i = 0; i < cases.length; i++) {
            assertEquals(cases[i][1] == 1, engine.getTeam(cases[i][0]));
        }
    }
}