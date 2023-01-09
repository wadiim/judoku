import org.example.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcSudokuBoardDaoTest {
    private DaoFactory<SudokuBoard> factory;

    @BeforeEach
    void setUp() {
        factory = new SudokuBoardDaoFactory();
    }

    @Test
    void TestReadAndWrite() throws Exception {
        String date = Long.toString(new Date().getTime());
        try (Dao<SudokuBoard> dao = factory.getJdbcDao("Test" + date)) {
            SudokuSolver ss = new BacktrackingSudokuSolver();
            SudokuBoard board = new SudokuBoard(ss);
            dao.write(board);
            SudokuBoard databaselized = dao.read();
            assertEquals(board, databaselized);
        }
    }

    @Test
    void TestWriteIfNameAlreadyExistThenThrowsSudokuJdbcException() throws Exception {
        String name = "Test" + Long.toString(new Date().getTime());
        SudokuSolver ss = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(ss);
        try (Dao<SudokuBoard> dao = factory.getJdbcDao(name)) {
            dao.write(board);
        }
        try (Dao<SudokuBoard> dao = factory.getJdbcDao(name)) {
            assertThrows(SudokuJdbcException.class, () -> {
                dao.write(board);
            });
        }
    }
}
