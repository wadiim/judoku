import java.util.Arrays;
import org.example.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SudokuBoardTest {
    private SudokuBoard sb;
    private SudokuSolver ss;
    private int[][] board;
    private final int[][] oversizedBoard = new int[][]{
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 0},
            {2, 3, 4, 5, 6, 7, 8, 9, 0, 1},
            {3, 4, 5, 6, 7, 8, 9, 0, 1, 2},
            {4, 5, 6, 7, 8, 9, 0, 1, 2, 3},
            {5, 6, 7, 8, 9, 0, 1, 2, 3, 4},
            {6, 7, 8, 9, 0, 1, 2, 3, 4, 5},
            {7, 8, 9, 0, 1, 2, 3, 4, 5, 6},
            {8, 9, 0, 1, 2, 3, 4, 5, 6, 7},
            {9, 0, 1, 2, 3, 4, 5, 6, 7, 8},
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
    };
    private int[][] solvedSudokuBoard;

    void testDimensions(int[][] b) {
        assertEquals(b.length, SudokuBoard.BOARD_SIZE);
        for (int[] row : b) {
            assertEquals(SudokuBoard.BOARD_SIZE, row.length);
        }
    }

    @BeforeEach
    void setUp() {
        ss = new BacktrackingSudokuSolver();
        board = new int[SudokuBoard.BOARD_SIZE][SudokuBoard.BOARD_SIZE];
        for (int i = 0; i < SudokuBoard.BOARD_SIZE; ++i) {
            Arrays.fill(board[i], SudokuBoard.BLANK);
        }
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
    }

    @Test
    void TestSudokuBoardIfNoParametersThenInitializesBoardToEmpty() {
        sb = new SudokuBoard(ss);
        board = sb.getBoard();
        for (int row = 0; row < board.length; ++row) {
            for (int col = 0; col < board[row].length; ++col) {
                assertEquals(SudokuBoard.BLANK, board[row][col]);
            }
        }
    }

    @Test
    void TestSudokuBoardIfInputHasTooSmallDimensionsThenBoardHasValidDimensions() {
        sb = new SudokuBoard(ss, new int[][] {{1}});
        int[][] b = sb.getBoard();
        testDimensions(b);
    }

    @Test
    void TestSudokuBoardIfInputHasTooSmallDimensionsThenBoardIsPaddedWithBlanks() {
        int[][] input = new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        sb = new SudokuBoard(ss, input);
        int[][] b = sb.getBoard();
        for (int row = 0; row < SudokuBoard.BOARD_SIZE; ++row) {
            for (int col = 0; col < SudokuBoard.BOARD_SIZE; ++col) {
                if (row < input.length && col < input[row].length) {
                    assertEquals(input[row][col], b[row][col]);
                } else {
                    assertEquals(SudokuBoard.BLANK, b[row][col]);
                }
            }
        }
    }

    @Test
    void TestSudokuBoardIfInputHasTooGreatDimensionsThenBoardHasValidDimensions() {
        sb = new SudokuBoard(ss, oversizedBoard);
        int[][] b = sb.getBoard();
        testDimensions(b);
    }

    @Test
    void TestSudokuBoardIfInputHasTooGreatDimensionsThenItIsTruncated() {
        sb = new SudokuBoard(ss, oversizedBoard);
        int[][] b = sb.getBoard();
        for (int row = 0; row < SudokuBoard.BOARD_SIZE; ++row) {
            for (int col = 0; col < SudokuBoard.BOARD_SIZE; ++col) {
                    assertEquals(oversizedBoard[row][col], b[row][col]);
            }
        }
    }

    @Test
    void TestGetBoardIfReturnValueModifiedThenOriginalBoardNotModified() {
        sb = new SudokuBoard(ss, board);
        int[][] b = sb.getBoard();
        ++b[0][0];
        assertNotEquals(b[0][0], sb.getBoard()[0][0]);
    }

    @Test
    void TestIsValidIfEmptyBoardThenReturnsTrue() {
        sb = new SudokuBoard(ss, board);
        assertTrue(sb.isValid(1, new SudokuBoard.Pos(0, 0)));
    }

    @Test
    void TestIsValidIfValueIsRepeatedButStillValidThenReturnsTrue() {
        board[board.length - 1][board[0].length - 1] = 1;
        sb = new SudokuBoard(ss, board);
        assertTrue(sb.isValid(1, new SudokuBoard.Pos(0, 0)));
    }

    @Test
    void TestIsValidIfValueIsRepeatedHorizontallyThenReturnsFalse() {
        board[0][board[0].length - 1] = 1;
        sb = new SudokuBoard(ss, board);
        assertFalse(sb.isValid(1, new SudokuBoard.Pos(0, 0)));
    }

    @Test
    void TestIsValidIfValueIsRepeatedVerticallyThenReturnsFalse() {
        board[board.length - 1][0] = 1;
        sb = new SudokuBoard(ss, board);
        assertFalse(sb.isValid(1, new SudokuBoard.Pos(0, 0)));
    }

    @Test
    void TestIsValidIfValueIsRepeatedInTheSameBoxThenReturnsFalse() {
        board[SudokuBoard.BOX_SIZE - 1][SudokuBoard.BOX_SIZE - 1] = 1;
        sb = new SudokuBoard(ss, board);
        assertFalse(sb.isValid(1, new SudokuBoard.Pos(0, 0)));
    }

    @Test
    void TestPosIfInitializedThenSetsTheXAndYCoordinatesCorrectly() {
        final int x = 1;
        final int y = 2;
        SudokuBoard.Pos pos = new SudokuBoard.Pos(y, x);
        assertEquals(x, pos.col);
        assertEquals(y, pos.row);
    }

    @Test
    void TestGetNextPositionIfInnerPositionThenIncrementsX() {
        SudokuBoard.Pos pos = new SudokuBoard.Pos(4, 2);
        SudokuBoard.Pos ret = SudokuBoard.getNextPosition(pos);
        assertEquals(pos.col + 1, ret.col);
        assertEquals(pos.row, ret.row);
    }

    @Test
    void TestGetNextPositionIfLastColThenReturnsPosOfTheFirstCellOfTheNextRow() {
        SudokuBoard.Pos pos = new SudokuBoard.Pos(7, SudokuBoard.BOARD_SIZE - 1);
        SudokuBoard.Pos ret = SudokuBoard.getNextPosition(pos);
        assertEquals(0, ret.col);
        assertEquals(pos.row + 1, ret.row);
    }

    @Test
    void TestGetEmptyCellPositionIfEmptyBoardThenReturnsTheStartingPos() {
        SudokuBoard.Pos pos = new SudokuBoard.Pos(4, 2);
        SudokuBoard sb = new SudokuBoard(ss, board);
        SudokuBoard.Pos ret = sb.getEmptyCellPosition(pos);
        assertEquals(pos.col, ret.col);
        assertEquals(pos.row, ret.row);
    }

    @Test
    void TestGetEmptyCellPositionIfStartingPosOutsideBoardThenReturnsTheInputPos() {
        SudokuBoard.Pos pos = new SudokuBoard.Pos(128, 254);
        SudokuBoard sb = new SudokuBoard(ss, board);
        SudokuBoard.Pos ret = sb.getEmptyCellPosition(pos);
        assertEquals(pos.col, ret.col);
        assertEquals(pos.row, ret.row);
    }

    @Test
    void TestGetEmptyCellPositionIfNoGapsThenReturnsPosBelowTheLastCol() {
        SudokuBoard.Pos pos = new SudokuBoard.Pos(0, 0);
        SudokuBoard sb = new SudokuBoard(ss, solvedSudokuBoard);
        SudokuBoard.Pos ret = sb.getEmptyCellPosition(pos);
        assertEquals(0, ret.col);
        assertEquals(SudokuBoard.BOARD_SIZE, ret.row);
    }

    @Test
    void TestGetEmptyCellPositionIfSeveralGapsThenReturnsTheNextOne() {
        SudokuBoard.Pos pos = new SudokuBoard.Pos(0, 0);
        final int expected_x = 3, expected_y = 1;

        solvedSudokuBoard[expected_y][expected_x] = SudokuBoard.BLANK;
        solvedSudokuBoard[expected_y][expected_x + 1] = SudokuBoard.BLANK;
        solvedSudokuBoard[expected_y + 2][expected_x] = SudokuBoard.BLANK;

        SudokuBoard sb = new SudokuBoard(ss, solvedSudokuBoard);
        SudokuBoard.Pos ret = sb.getEmptyCellPosition(pos);

        assertEquals(expected_x, ret.col);
        assertEquals(expected_y, ret.row);
    }

    @Test
    void TestClearBoardIfNonEmptyThenIsCleared() {
        SudokuBoard sb = new SudokuBoard(ss, solvedSudokuBoard);
        sb.clearBoard();
        int[][] board = sb.getBoard();
        for (int[] row : board) {
            for (int val : row) {
                assertEquals(SudokuBoard.BLANK, val);
            }
        }
    }

    @Test
    void TestGetIfValidPositionThenReturnsCellValueAtThatPosition() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertEquals(solvedSudokuBoard[2][8], sb.get(8, 2));
    }

    @Test
    void TestSetIfValidPositionThenSetsTheCell() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        sb.set(1, 3, 2);
        assertEquals(2, sb.get(1, 3));
    }

    @Test
    void TestValidateIfEmptyBoardThenReturnsTrue() {
        sb = new SudokuBoard(ss);
        assertTrue(sb.checkBoard());
    }

    @Test
    void TestValidateIfValidBoardThenReturnsTrue() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertTrue(sb.checkBoard());
    }

    @Test
    void TestValidateIfNumberIsRepeatedHorizontallyThenReturnsFalse() {
        board[0][0] = 1;
        board[0][board[0].length - 1] = 1;
        sb = new SudokuBoard(ss, board);
        assertFalse(sb.checkBoard());
    }

    @Test
    void TestValidateIfNumberIsRepeatedVerticallyThenReturnsFalse() {
        board[0][0] = 1;
        board[board.length - 1][0] = 1;
        sb = new SudokuBoard(ss, board);
        assertFalse(sb.checkBoard());
    }

    @Test
    void TestValidateIfNumberIsRepeatedInTheSameBoxThenReturnsFalse() {
        board[0][0] = 1;
        board[SudokuBoard.BOX_SIZE - 1][SudokuBoard.BOX_SIZE - 1] = 1;
        sb = new SudokuBoard(ss, board);
        assertFalse(sb.checkBoard());
    }

    @Test
    void TestSolveGameIfEmptyBoardThenGeneratesCorrectlySolvedSudoku() {
        sb = new SudokuBoard(ss);
        sb.solveGame();
        assertTrue(sb.checkBoard());
    }

    @Test
    void TestGetRowIfValidIndexThenReturnsNonNull() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertNotNull(sb.getRow(2));
    }

    @Test
    void TestGetRowIfValidBoardThenReturnsValidRow() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertTrue(sb.getRow(2).verify());
    }

    @Test
    void TestGetRowIfNotValidTheGivenRowThenVerifyReturnsFalse() {
        solvedSudokuBoard[0][1] = solvedSudokuBoard[0][5] = 2;
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertFalse(sb.getRow(0).verify());
    }

    @Test
    void TestGetRowIfBoardWasAlteredThenTheRowRepresentsCurrentState() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        sb.set(1, 2, 2);
        sb.set(2, 2, 2);
        assertFalse(sb.getRow(2).verify());
    }

    @Test
    void TestGetRowIfBoardWasAlteredAfterRowWasReturnedThenTheRowStillIsUpdated() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        SudokuRow row = sb.getRow(2);
        sb.set(1, 2, 2);
        sb.set(2, 2, 2);
        assertFalse(row.verify());
    }

    @Test
    void TestGetColumnIfValidIndexThenReturnsNonNull() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertNotNull(sb.getColumn(2));
    }

    @Test
    void TestGetColumnIfValidBoardThenReturnsValidColumn() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertTrue(sb.getColumn(2).verify());
    }

    @Test
    void TestGetColumnIfNotValidTheGivenColumnThenVerifyReturnsFalse() {
        solvedSudokuBoard[1][0] = solvedSudokuBoard[5][0] = 2;
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertFalse(sb.getColumn(0).verify());
    }

    @Test
    void TestGetColumnIfBoardWasAlteredThenTheColumnRepresentsCurrentState() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        sb.set(2, 1, 2);
        sb.set(2, 2, 2);
        assertFalse(sb.getRow(2).verify());
    }

    @Test
    void TestGetColumnIfBoardWasAlteredAfterColumnWasReturnedThenTheColumnStillIsUpdated() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        SudokuColumn column = sb.getColumn(2);
        sb.set(2, 1, 2);
        sb.set(2, 2, 2);
        assertFalse(column.verify());
    }

    @Test
    void TestGetBoxIfValidIndexThenReturnsNonNull() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertNotNull(sb.getBox(0, 0));
    }

    @Test
    void TestGetBoxIfValidBoardThenReturnsValidBox() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertTrue(sb.getBox(0, 0).verify());
    }

    @Test
    void TestGetBoxIfNotValidTheGivenBoxThenVerifyReturnsFalse() {
        solvedSudokuBoard[0][0] = solvedSudokuBoard[2][2] = 2;
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertFalse(sb.getBox(0, 0).verify());
    }

    @Test
    void TestGetBoxIfBoardWasAlteredThenTheBoxRepresentsCurrentState() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        sb.set(0, 0, 2);
        sb.set(2, 2, 2);
        assertFalse(sb.getBox(0, 0).verify());
    }

    @Test
    void TestGetBoxIfBoardWasAlteredAfterBoxWasReturnedThenTheBoxStillIsUpdated() {
        sb = new SudokuBoard(ss, solvedSudokuBoard);
        SudokuBox box = sb.getBox(0, 0);
        sb.set(0, 0, 2);
        sb.set(2, 2, 2);
        assertFalse(box.verify());
    }

    @Test
    void TestToStringIfCalledThenReturnNonNull() {
        sb = new SudokuBoard(ss);
        assertNotNull(sb.toString());
    }

    @Test
    void TestEqualsIfPassedNullThenReturnsFalse() {
        sb = new SudokuBoard(ss);
        assertFalse(sb.equals(null));
    }

    @Test
    void TestEqualsIfPassedSelfThenReturnsTrue() {
        sb = new SudokuBoard(ss);
        assertTrue(sb.equals(sb));
    }

    @Test
    void TestEqualsIfPassedObjectOfNotCompatibleClassThenReturnsFalse() {
        sb = new SudokuBoard(ss);
        assertFalse(sb.equals(2));
    }

    @Test
    void TestEqualsIfBothBoardsAreEmptyThenReturnsTrue() {
        sb = new SudokuBoard(ss);
        SudokuBoard sb2 = new SudokuBoard(ss);
        assertTrue(sb.equals(sb2));
    }

    @Test
    void TestEqualsIfBoardsAreEqualThenReturnsTrue() {
        SudokuBoard sb1 = new SudokuBoard(ss, board);
        SudokuBoard sb2 = new SudokuBoard(ss, board);
        assertTrue(sb1.equals(sb2));
    }

    @Test
    void TestEqualsIfBoardsAreNotEqualThenReturnsFalse() {
        SudokuBoard sb1 = new SudokuBoard(ss, board);
        SudokuBoard sb2 = new SudokuBoard(ss, oversizedBoard);
        assertFalse(sb1.equals(sb2));
    }

    @Test
    void TestHashCodeIfCalledOnEqualObjectsThenBothCodesAreTheSame() {
        SudokuBoard sb1 = new SudokuBoard(ss, board);
        SudokuBoard sb2 = new SudokuBoard(ss, board);
        assertEquals(sb1.hashCode(), sb2.hashCode());
    }

    @Test
    void TestCloneIfNullThenThrowsNullPointerException() {
        SudokuBoard sb = null;
        try {
            SudokuBoard clone = sb.clone();
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    void TestCloneIfNonNullThenReturnsACopy() {
        SudokuBoard sb = new SudokuBoard(ss, solvedSudokuBoard);
        SudokuBoard clone = sb.clone();
        assertEquals(sb, clone);
    }

    @Test
    void TestCloneIfCloneIsModifiedThenOriginalRemainsUnmodified() {
        SudokuBoard sb = new SudokuBoard(ss, solvedSudokuBoard);
        SudokuBoard clone = sb.clone();
        clone.set(2, 2, 9 - sb.get(2, 2));
        assertNotEquals(sb.get(2, 2), clone.get(2, 2));
    }

    @Test
    void TestIsSolvedIfSolvedThenReturnsTrue() {
        SudokuBoard sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertTrue(sb.isSolved());
    }

    @Test
    void TestIsSolvedIfContainsRepeatedValuesThenReturnsFalse() {
        solvedSudokuBoard[1][1] = 2;
        solvedSudokuBoard[2][2] = 2;
        SudokuBoard sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertFalse(sb.isSolved());
    }

    @Test
    void TestIsSolvedIfValuesDoNotRepeatButContainsBlankThenReturnsFalse() {
        solvedSudokuBoard[2][2] = SudokuBoard.BLANK;
        SudokuBoard sb = new SudokuBoard(ss, solvedSudokuBoard);
        assertFalse(sb.isSolved());
    }
}
