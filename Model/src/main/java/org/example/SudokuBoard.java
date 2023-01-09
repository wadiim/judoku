package org.example;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SudokuBoard implements Serializable, Cloneable {
    public static final int BLANK = 0;
    public static final int BOX_SIZE = 3;
    public static final int BOARD_SIZE = 9;

    private final SudokuField[][] board;
    private final SudokuSolver solver;

    public static class Pos {
        public int row;
        public int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public SudokuBoard(SudokuSolver ss) {
        solver = ss;
        board = new SudokuField[BOARD_SIZE][BOARD_SIZE];
        for (int y = 0; y < BOARD_SIZE; ++y) {
            for (int x = 0; x < BOARD_SIZE; ++x) {
                board[y][x] = new SudokuField();
            }
        }
    }

    public SudokuBoard(SudokuSolver ss, final int[][] b) {
        this(ss);
        // Copy values manually in order to handle incorrect array sizes.
        for (int row = 0; row < b.length && row < board.length; ++row) {
            for (int col = 0; col < b[row].length && col < board[row].length; ++col) {
                board[row][col].setFieldValue(b[row][col]);
            }
        }
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append(board)
                .append(solver)
                .toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SudokuBoard rhs = (SudokuBoard) obj;
        return new EqualsBuilder()
                .append(board, rhs.board)
                .append(solver, rhs.solver)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(13, 37)
                .append(board)
                .append(solver)
                .toHashCode();
    }

    public void solveGame() {
        solver.solve(this);
    }

    public int get(int x, int y) {
        return board[y][x].getFieldValue();
    }

    public void set(int x, int y, int val) {
        board[y][x].setFieldValue(val);
    }

    /*
     * Returns true if the board is correct, i.e. does not contain repeated values in any row,
     * column, or box. The board does not have to be solved, though.
     * Returns false otherwise.
     */
    public boolean checkBoard() {
        // Check horizontally and vertically.
        for (int i = 0; i < BOARD_SIZE; ++i) {
            if (!(getRow(i).verify() && getColumn(i).verify())) {
                return false;
            }
        }
        // Check boxes.
        for (int x = 0; x < BOX_SIZE; ++x) {
            for (int y = 0; y < BOX_SIZE; ++y) {
                if (!getBox(x, y).verify()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSolved() {
        for (int row = 0; row < BOARD_SIZE; ++row) {
            for (int col = 0; col < BOARD_SIZE; ++col) {
                if (board[row][col].getFieldValue() == BLANK) {
                    return false;
                }
            }
        }
        return checkBoard();
    }

    public int[][] getBoard() {
        int[][] ret = new int[BOARD_SIZE][BOARD_SIZE];
        for (int x = 0; x < BOARD_SIZE; ++x) {
            for (int y = 0; y < BOARD_SIZE; ++y) {
                ret[x][y] = board[x][y].getFieldValue();
            }
        }
        return ret;
    }

    public SudokuRow getRow(int y) {
        return new SudokuRow(board[y]);
    }

    public SudokuColumn getColumn(int x) {
        SudokuField[] values = new SudokuField[BOARD_SIZE];
        for (int y = 0; y < BOARD_SIZE; ++y) {
            values[y] = board[y][x];
        }
        return new SudokuColumn(values);
    }

    public SudokuBox getBox(int x, int y) {
        SudokuField[] values = new SudokuField[BOARD_SIZE];
        for (int row = 0; row < BOX_SIZE; ++row) {
            for (int col = 0; col < BOX_SIZE; ++col) {
                values[col + (row * BOX_SIZE)] = board[row + (y * BOX_SIZE)][col + (x * BOX_SIZE)];
            }
        }
        return new SudokuBox(values);
    }

    public boolean isValid(int val, Pos pos) {
        // Check for repeats horizontally and vertically.
        for (SudokuField[] fields : board) {
            for (int col = 0; col < fields.length; ++col) {
                if (board[pos.row][col].getFieldValue() == val
                        || fields[pos.col].getFieldValue() == val) {
                    return false;
                }
            }
        }

        // Calculate the coordinates of the top-left corner of the box.
        int bx = (pos.col / BOX_SIZE) * BOX_SIZE;
        int by = (pos.row / BOX_SIZE) * BOX_SIZE;

        // Check for repeats in the box containing (x, y).
        for (int row = by; row < by + BOX_SIZE; ++row) {
            for (int col = bx; col < bx + BOX_SIZE; ++col) {
                if (board[row][col].getFieldValue() == val) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Pos getNextPosition(Pos pos) {
        Pos ret = new Pos(pos.row, pos.col);

        if (ret.col + 1 < BOARD_SIZE) {
            ++ret.col;
        } else {
            ret.col = 0;
            ++ret.row;
        }

        return ret;
    }

    public Pos getEmptyCellPosition(Pos start) {
        Pos ret = new Pos(start.row, start.col);

        while (ret.col < BOARD_SIZE && ret.row < BOARD_SIZE
                && board[ret.row][ret.col].getFieldValue() != BLANK) {
            if (ret.col + 1 < BOARD_SIZE) {
                ++ret.col;
            } else {
                ret.col = 0;
                ++ret.row;
            }
        }

        return ret;
    }

    public void clearBoard() {
        for (SudokuField[] row : board) {
            for (SudokuField field : row) {
                field.setFieldValue(BLANK);
            }
        }
    }

    @Override
    public SudokuBoard clone() {
        int[][] values = new int[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; ++row) {
            for (int col = 0; col < BOARD_SIZE; ++col) {
                values[row][col] = board[row][col].getFieldValue();
            }
        }
        return new SudokuBoard(solver, values);
    }
}
