import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.example.BacktrackingSudokuSolver;
import org.example.SudokuBoard;
import org.example.SudokuSolver;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BacktrackingSudokuSolverTest {
    private SudokuBoard sb;
    private SudokuSolver ss;
    private int[][] solvedSudokuBoard;

    @BeforeEach
    void setUp() {
        ss = new BacktrackingSudokuSolver();
        solvedSudokuBoard = new int[][] {
                {8, 2, 7, 1, 5, 4, 3, 9, 6},
                {9, 6, 5, 3, 2, 7, 1, 4, 8},
                {3, 4, 1, 6, 8, 9, 7, 5, 2},
                {5, 9, 3, 4, 6, 8, 2, 7, 1},
                {4, 7, 2, 5, 1, 3, 6, 8, 9},
                {6, 1, 8, 9, 7, 2, 4, 3, 5},
                {7, 8, 6, 2, 3, 5, 9, 1, 4},
                {1, 5, 4, 7, 9, 6, 8, 2, 3},
                {2, 3, 9, 8, 4, 1, 5, 6, 7}
        };
        sb = new SudokuBoard(ss);
    }

    @Test
    void TestSolveIfAlreadySolvedThenNothingChanges() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        int[][] expected = sb.getBoard();
        ss.solve(sb);
        int[][] result = sb.getBoard();
        for (int row = 0; row < result.length; ++row) {
            for (int col = 0; col < result[row].length; ++col) {
                assertEquals(expected[row][col], result[row][col]);
            }
        }
    }

    @Test
    void TestSolveIfSingleGapThenFillsTheGap() {
        int expected = solvedSudokuBoard[1][2];
        solvedSudokuBoard[1][2] = 0;
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        ss.solve(sb);
        assertEquals(expected, sb.getBoard()[1][2]);
    }

    @Test
    void TestSolveIfMultipleGapsThenFillsTheGaps() {
        int[][] expected = new int[solvedSudokuBoard.length][];
        for (int i = 0; i < expected.length; ++i) {
            expected[i] = solvedSudokuBoard[i].clone();
        }

        solvedSudokuBoard[1][3] = 0;
        solvedSudokuBoard[5][2] = 0;
        solvedSudokuBoard[7][6] = 0;
        solvedSudokuBoard[4][8] = 0;

        sb = new SudokuBoard(ss, solvedSudokuBoard);
        ss.solve(sb);

        int[][] board = sb.getBoard();
        for (int row = 0; row < board.length; ++row) {
            for (int col = 0; col < board[row].length; ++col) {
                assertEquals(expected[row][col], board[row][col]);
            }
        }
    }

    @Test
    void TestSolveIfEmptyThenFillsAllTheGaps() {
        ss.solve(sb);
        int[][] board = sb.getBoard();
        for (int[] row : board) {
            for (int val : row) {
                assertNotEquals(SudokuBoard.BLANK, val);
            }
        }
    }

    @Test
    void TestSolveIfTwoConsecutiveCallsThenBoardsAreFilledDifferently() {
        ss.solve(sb);
        int[][] board1 = sb.getBoard();
        sb.clearBoard();
        ss.solve(sb);
        int[][] board2 = sb.getBoard();
        assertFalse(Arrays.deepEquals(board1, board2));
    }

    @Test
    void TestSolveIfEmptyThenNoValuesAreRepeatedHorizontally() {
        ss.solve(sb);
        int[][] board = sb.getBoard();
        int[] row;
        for (int[] r : board) {
            row = r.clone();
            Arrays.sort(row);
            for (int i = 0; i < row.length - 1; ++i) {
                assertNotEquals(row[i], row[i + 1]);
            }
        }
    }

    @Test
    void TestSolveIfEmptyThenNoValuesAreRepeatedVertically() {
        ss.solve(sb);
        int[][] board = sb.getBoard();
        ArrayList<Integer> col;
        for (int c = 0; c < SudokuBoard.BOARD_SIZE; ++c) {
            col = new ArrayList<>();
            for (int r = 0; r < SudokuBoard.BOARD_SIZE; ++r) {
                col.add(board[r][c]);
            }
            Collections.sort(col);
            for (int i = 0; i < col.size() - 1; ++i) {
                assertNotEquals(col.get(i), col.get(i + 1));
            }
        }
    }

    @Test
    void TestSolveIfEmptyThenNoValuesAreRepeatedInAnyBox() {
        ss.solve(sb);
        int[][] board = sb.getBoard();
        ArrayList<Integer> box;
        for (int boxY = 0; boxY < SudokuBoard.BOARD_SIZE / SudokuBoard.BOX_SIZE; ++boxY) {
            for (int boxX = 0; boxX < SudokuBoard.BOARD_SIZE / SudokuBoard.BOX_SIZE; ++boxX) {
                box = new ArrayList<>();
                for (int y = boxY * SudokuBoard.BOX_SIZE; y < (boxY + 1) * SudokuBoard.BOX_SIZE; ++y) {
                    for (int x = boxX * SudokuBoard.BOX_SIZE; x < (boxX + 1) * SudokuBoard.BOX_SIZE; ++x) {
                        box.add(board[y][x]);
                    }
                }
                Collections.sort(box);
                for (int i = 0; i < box.size() - 1; ++i) {
                    assertNotEquals(box.get(i), box.get(i + 1));
                }
            }
        }
    }

    @Test
    void TestSolveIfStartingPosOutsideBoardThenReturnsTrue() {
        BacktrackingSudokuSolver bss = new BacktrackingSudokuSolver();
        assertTrue(bss.solve(sb, new SudokuBoard.Pos(13, 37)));
    }

    @Test
    void TestToStringIfCalledThenReturnsNonNull() {
        BacktrackingSudokuSolver bss = new BacktrackingSudokuSolver();
        assertNotNull(bss.toString());
    }

    @Test
    void TestEqualsIfPassedNullThenReturnsFalse() {
        BacktrackingSudokuSolver bss = new BacktrackingSudokuSolver();
        assertFalse(bss.equals(null));
    }

    @Test
    void TestEqualsIfPassedSelfThenReturnsTrue() {
        BacktrackingSudokuSolver bss = new BacktrackingSudokuSolver();
        assertTrue(bss.equals(bss));
    }

    @Test
    void TestEqualsIfPassedObjectOfNotCompatibleClassThenReturnsFalse() {
        BacktrackingSudokuSolver bss = new BacktrackingSudokuSolver();
        assertFalse(bss.equals(2));
    }

    @Test
    void TestEqualsIfPassedObjectOfTheSameTypeThenReturnsTrue() {
        BacktrackingSudokuSolver bss1 = new BacktrackingSudokuSolver();
        BacktrackingSudokuSolver bss2 = new BacktrackingSudokuSolver();
        assertTrue(bss1.equals(bss2));
    }

    @Test
    void TestHashCodeIfCalledOnEqualObjectsThenBothCodesAreTheSame() {
        BacktrackingSudokuSolver bss1 = new BacktrackingSudokuSolver();
        BacktrackingSudokuSolver bss2 = new BacktrackingSudokuSolver();
        assertEquals(bss1.hashCode(), bss2.hashCode());
    }
}
