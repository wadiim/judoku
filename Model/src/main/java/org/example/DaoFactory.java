package org.example;

import java.io.Serializable;

public interface DaoFactory<T extends Serializable> {
    Dao<T> getFileDao(String fileName) throws SudokuFileException;

    Dao<T> getJdbcDao(String name) throws SudokuException;
}
