package org.example;

import java.io.Serializable;

public interface SudokuSolver extends Serializable {
    void solve(SudokuBoard board);
}
