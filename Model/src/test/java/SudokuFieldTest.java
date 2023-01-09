import org.apache.commons.lang3.ObjectUtils;
import org.example.SudokuBoard;
import org.example.SudokuField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldTest {
    @Test
    void TestGetFieldValueIfCorrectValuePassedToConstructorThenReturnsTheSameValue() {
        SudokuField sf = new SudokuField(2);
        assertEquals(2, sf.getFieldValue());
    }

    @Test
    void TestSetValueIfCorrectInputThenSetTheValue() {
        SudokuField sf = new SudokuField(0);
        sf.setFieldValue(2);
        assertEquals(2, sf.getFieldValue());
    }

    @Test
    void TestConstructorIfNoInputThenSetsTheValueToBlank() {
        SudokuField sf = new SudokuField();
        assertEquals(SudokuBoard.BLANK, sf.getFieldValue());
    }

    @Test
    void TestConstructorIfInputIsTooSmallThenSetsTheValueToBlank() {
        SudokuField sf = new SudokuField(-2);
        assertEquals(SudokuBoard.BLANK, sf.getFieldValue());
    }

    @Test
    void TestConstructorIfInputIsTooBigThenSetsTheValueToBlank() {
        SudokuField sf = new SudokuField(22);
        assertEquals(SudokuBoard.BLANK, sf.getFieldValue());
    }

    @Test
    void TestSetFieldValueIfInputIsTooSmallThenValueIsNotChanged() {
        SudokuField sf = new SudokuField(2);
        sf.setFieldValue(-7);
        assertEquals(2, sf.getFieldValue());
    }

    @Test
    void TestSetFieldValueIfInputIsTooBigThenValueIsNotChanged() {
        SudokuField sf = new SudokuField(1);
        sf.setFieldValue(1337);
        assertEquals(1, sf.getFieldValue());
    }

    @Test
    void TestToStringIfCalledThenReturnsNonNull() {
        SudokuField sf = new SudokuField(2);
        assertNotNull(sf.toString());
    }

    @Test
    void TestEqualsIfPassedNullThenReturnsFalse() {
        SudokuField sf = new SudokuField(2);
        assertFalse(sf.equals(null));
    }

    @Test
    void TestEqualsIfPassedSelfThenReturnsTrue() {
        SudokuField sf = new SudokuField(2);
        assertTrue(sf.equals(sf));
    }

    @Test
    void TestEqualsIfPassedObjectOfNotCompatibleClassThenReturnsFalse() {
        SudokuField sf = new SudokuField(2);
        assertFalse(sf.equals(2));
    }

    @Test
    void TestEqualsIfBothFieldsContainsTheSameValueThenReturnsTrue() {
        SudokuField sf1 = new SudokuField(2);
        SudokuField sf2 = new SudokuField(2);
        assertTrue(sf1.equals(sf2));
    }

    @Test
    void TestEqualsIfFieldsContainsDifferentValuesThenReturnsFalse() {
        SudokuField sf1 = new SudokuField(0);
        SudokuField sf2 = new SudokuField(2);
        assertFalse(sf1.equals(sf2));
    }

    @Test
    void TestHashCodeIfCalledOnEqualObjectsThenBothCodesAreTheSame() {
        SudokuField sf1 = new SudokuField(2);
        SudokuField sf2 = new SudokuField(2);
        assertEquals(sf1.hashCode(), sf2.hashCode());
    }

    @Test
    void TestCompareToIfNullThenThrowsNullPointerException() {
        SudokuField sf = new SudokuField(2);
        try {
            sf.compareTo(null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    void TestCompareToIfEqualsThenReturns0() {
        SudokuField sf1 = new SudokuField(2);
        SudokuField sf2 = new SudokuField(2);
        assertTrue(sf1.equals(sf2) && sf1.compareTo(sf2) == 0);
    }

    @Test
    void TestCompareToIfGreaterThenReturnsPositiveInteger() {
        SudokuField sf1 = new SudokuField(4);
        SudokuField sf2 = new SudokuField(2);
        assertTrue(sf1.compareTo(sf2) > 0);
    }

    @Test
    void TestCompareToIfLowerThenReturnsNegativeInteger() {
        SudokuField sf1 = new SudokuField(5);
        SudokuField sf2 = new SudokuField(7);
        assertTrue(sf1.compareTo(sf2) < 0);
    }

    @Test
    void TestCloneIfNullThenThrowsNullPointerException() {
        SudokuField sf = null;
        try {
            sf.clone();
        } catch (NullPointerException e) {
            assertTrue(true);
        } catch (CloneNotSupportedException e) {
            fail();
        }
    }

    @Test
    void TestCloneIfNonNullThenReturnsACopy() throws CloneNotSupportedException {
        SudokuField sf = new SudokuField(2);
        SudokuField copy = sf.clone();
        assertEquals(sf, copy);
    }

    @Test
    void TestCloneIfCloneIsModifiedThenOriginalRemainsUnmodified() throws CloneNotSupportedException {
        SudokuField sf = new SudokuField(2);
        SudokuField copy = sf.clone();
        copy.setFieldValue(8);
        assertEquals(2, sf.getFieldValue());
    }
}
