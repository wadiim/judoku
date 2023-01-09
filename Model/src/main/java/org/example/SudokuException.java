package org.example;

public class SudokuException extends Exception {
    public SudokuException(String errorMessage) {
        super(errorMessage);
    }

    public SudokuException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
