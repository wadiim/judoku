import org.example.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileSudokuBoardDaoTest {
    private DaoFactory<SudokuBoard> factory;

    @BeforeEach
    void setUp() {
        factory = new SudokuBoardDaoFactory();
    }

    @Test
    void TestReadAndWrite() throws Exception {
        try (Dao<SudokuBoard> dao = factory.getFileDao("src/test/java/board.ser")) {
            SudokuSolver ss = new BacktrackingSudokuSolver();
            SudokuBoard board = new SudokuBoard(ss);
            dao.write(board);
            SudokuBoard serialized = dao.read();
            assertEquals(board, serialized);
        }
    }

    @Test
    void TestReadAndWriteIfMultipleObjects() throws Exception {
        try (Dao<SudokuBoard> dao = factory.getFileDao("src/test/java/boards.ser")) {
            int[][] cells1 = new int[SudokuBoard.BOARD_SIZE][SudokuBoard.BOARD_SIZE];
            int[][] cells2 = new int[SudokuBoard.BOARD_SIZE][SudokuBoard.BOARD_SIZE];
            for (int i = 0; i < SudokuBoard.BOARD_SIZE; ++i) {
                for (int j = 0; j < SudokuBoard.BOARD_SIZE; ++j) {
                    cells1[i][j] = i;
                    cells2[i][j] = j;
                }
            }
            SudokuSolver ss = new BacktrackingSudokuSolver();
            SudokuBoard board1 = new SudokuBoard(ss, cells1);
            SudokuBoard board2 = new SudokuBoard(ss, cells2);
            dao.write(board1);
            dao.write(board2);
            SudokuBoard serialized1 = dao.read();
            SudokuBoard serialized2 = dao.read();
            assertNotEquals(board1, board2);
            assertEquals(board1, serialized1);
            assertEquals(board2, serialized2);
        }
    }

    @Test
    void TestReadingUsingOtherDaoInstanceThanForWriting() throws Exception {
        SudokuSolver ss = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(ss);
        try (Dao<SudokuBoard> dao = factory.getFileDao("src/test/java/board.ser")) {
            dao.write(board);
        }

        try (Dao<SudokuBoard> dao = factory.getFileDao("src/test/java/board.ser")) {
            SudokuBoard serialized = dao.read();
            assertEquals(board, serialized);
        }
    }

    @Test
    void TestConstructorIfInvalidFileNameThenThrowsSudokuFileException() {
        assertThrows(SudokuFileException.class, () -> {factory.getFileDao("2:/"); });
    }

    @Test
    void TestReadIfFileIsEmptyThenThrowsSudokuFileException() throws Exception {
        try (Dao<SudokuBoard> dao = factory.getFileDao("src/test/java/empty.ser"))
        {
            assertThrows(SudokuFileException.class, dao::read);
        }
    }

    @Test
    void TestWriteIfFileHasBeenDeletedThenThrowsSudokuFileException() throws Exception {
        try (Dao<SudokuBoard> dao = factory.getFileDao("src/test/java/fill-me.ser")) {
            assertDoesNotThrow(() -> {
                dao.write(new SudokuBoard(new BacktrackingSudokuSolver()));
            });
        }
    }
}
