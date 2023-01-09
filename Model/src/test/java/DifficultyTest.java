import org.example.BacktrackingSudokuSolver;
import org.example.Difficulty;
import org.example.SudokuBoard;
import org.example.SudokuSolver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DifficultyTest {
    @Test
    void TestGetBlanksIfEasyThenReturns5() {
        Difficulty easy = Difficulty.EASY;
        assertEquals(5, easy.getBlanks());
    }

    @Test
    void TestGetBlanksIfMediumThenReturns15() {
        Difficulty medium = Difficulty.MEDIUM;
        assertEquals(15, medium.getBlanks());
    }

    @Test
    void TestGetBlanksIfHardThenReturns30() {
        Difficulty hard = Difficulty.HARD;
        assertEquals(30, hard.getBlanks());
    }

    @Test
    void TestClearFields() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();

        for (Difficulty difficulty : Difficulty.values()) {
            SudokuBoard sb = board.clone();
            difficulty.clearFields(sb);

            int numClearedFields = 0;
            for (int row = 0; row < SudokuBoard.BOARD_SIZE; ++row) {
                for (int col = 0; col < SudokuBoard.BOARD_SIZE; ++col) {
                    if (sb.get(row, col) == SudokuBoard.BLANK) {
                        ++numClearedFields;
                    }
                }
            }

            assertEquals(difficulty.getBlanks(), numClearedFields);
        }
    }
}
