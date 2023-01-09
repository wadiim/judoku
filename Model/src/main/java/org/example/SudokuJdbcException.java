package org.example;

public class SudokuJdbcException extends SudokuException {
    public SudokuJdbcException(String errorMessage) {
        super(errorMessage);
    }

    public SudokuJdbcException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
