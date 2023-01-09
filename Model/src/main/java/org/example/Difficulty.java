package org.example;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public enum Difficulty {
    EASY(5),
    MEDIUM(15),
    HARD(30);

    final int blanks;

    Difficulty(int blanks) {
        this.blanks = blanks;
    }

    public int getBlanks() {
        return blanks;
    }

    public void clearFields(SudokuBoard board) {
        Random random = new Random();

        Set<Integer> indexes = new HashSet<>();
        while (indexes.size() < blanks) {
            indexes.add(random.nextInt(SudokuBoard.BOARD_SIZE * SudokuBoard.BOARD_SIZE));
        }

        for (int index : indexes) {
            board.set(index % SudokuBoard.BOARD_SIZE,
                    index / SudokuBoard.BOARD_SIZE, SudokuBoard.BLANK);
        }
    }
}
