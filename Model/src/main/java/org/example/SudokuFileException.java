package org.example;

public class SudokuFileException extends SudokuException {
    public SudokuFileException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
