package org.example;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SudokuComponent implements Cloneable {
    final List<SudokuField> fields;

    public SudokuComponent(SudokuField[] f) {
        fields = Arrays.stream(f).toList();
    }

    public String toString() {
        return new ToStringBuilder(this).append(fields).toString();
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
        SudokuComponent rhs = (SudokuComponent) obj;
        return new EqualsBuilder().append(fields, rhs.fields).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 77)
                .append(fields)
                .toHashCode();
    }

    public boolean verify() {
        int[] hist = new int[SudokuBoard.BOARD_SIZE + 1];
        for (SudokuField f : fields) {
            int val = f.getFieldValue();
            if (val != SudokuBoard.BLANK && hist[val] > 0) {
                return false;
            }
            ++hist[val];
        }
        return true;
    }

    @Override
    public SudokuComponent clone() throws CloneNotSupportedException {
        SudokuField[] clonedFields = new SudokuField[SudokuBoard.BOARD_SIZE];
        for (int i = 0; i < SudokuBoard.BOARD_SIZE; ++i) {
            clonedFields[i] = fields.get(i).clone();
        }
        return new SudokuComponent(clonedFields);
    }
}
