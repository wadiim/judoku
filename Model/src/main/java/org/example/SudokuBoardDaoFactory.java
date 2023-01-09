package org.example;

public class SudokuBoardDaoFactory implements DaoFactory<SudokuBoard> {
    @Override
    public Dao<SudokuBoard> getFileDao(String fileName) throws SudokuFileException {
        return new FileSudokuBoardDao(fileName);
    }

    @Override
    public Dao<SudokuBoard> getJdbcDao(String name) throws SudokuJdbcException {
        return new JdbcSudokuBoardDao(name);
    }
}
