package org.example;

import java.io.Serializable;

public interface Dao<T extends Serializable> extends AutoCloseable {

    T read() throws SudokuException;

    void write(T obj) throws SudokuException;
}
