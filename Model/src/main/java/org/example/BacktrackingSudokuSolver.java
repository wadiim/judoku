package org.example;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BacktrackingSudokuSolver implements SudokuSolver {
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return obj.getClass() == getClass();
    }

    public int hashCode() {
        return new HashCodeBuilder(29, 41).toHashCode();
    }

    @Override
    public void solve(SudokuBoard board) {
        solve(board, new SudokuBoard.Pos(0, 0));
    }

    public boolean solve(SudokuBoard board, SudokuBoard.Pos pos) {
        // Find the next empty cell position.
        pos = board.getEmptyCellPosition(pos);

        // If all gaps have been filled, return true.
        if (pos.col >= SudokuBoard.BOARD_SIZE || pos.row >= SudokuBoard.BOARD_SIZE) {
            return true;
        }

        // Find all the valid values.
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 9; ++i) {
            if (board.isValid(i, pos)) {
                values.add(i);
            }
        }

        Collections.shuffle(values);

        while (values.size() > 0) {
            // Fill the gap with a random value.
            board.set(pos.col, pos.row, values.remove(0));
            // Try to fill the other gaps.
            if (solve(board, board.getEmptyCellPosition(pos))) {
                return true;
            }
        }

        // Backtrack
        board.set(pos.col, pos.row, SudokuBoard.BLANK);
        return false;
    }
}
