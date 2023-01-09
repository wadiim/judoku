package org.example;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SudokuField implements Serializable, Comparable<SudokuField>, Cloneable {
    private int value;

    public SudokuField() {
    }

    public SudokuField(int val) {
        setFieldValue(val);
    }

    public String toString() {
        return new ToStringBuilder(this).append(value).toString();
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
        SudokuField rhs = (SudokuField) obj;
        return new EqualsBuilder()
                .append(value, rhs.value)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(13, 37)
                .append(value)
                .toHashCode();
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int val) {
        if (val == SudokuBoard.BLANK || val >= 1 && val <= SudokuBoard.BOARD_SIZE) {
            value = val;
        }
    }

    @Override
    public int compareTo(SudokuField o) {
        if (o == null) {
            throw new NullPointerException();
        }

        return Integer.compare(value, o.value);
    }

    @Override
    public SudokuField clone() throws CloneNotSupportedException {
        return (SudokuField) super.clone();
    }
}
