import org.example.SudokuField;
import org.example.SudokuBoard;
import org.example.SudokuComponent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class SudokuComponentTest {
    protected SudokuComponent component;
    protected SudokuField[] fields;

    void instantiateComponent() {
        component = new SudokuComponent(fields);
    }

    @BeforeEach
    void setUp() {
        fields = new SudokuField[SudokuBoard.BOARD_SIZE];
        for (int i = 0; i < fields.length; ++i) {
            fields[i] = new SudokuField();
        }
    }

    @Test
    void TestVerifyIfAllFieldValuesAreBlankThenReturnsTrue() {
        instantiateComponent();
        assertTrue(component.verify());
    }

    @Test
    void TestVerifyIfRepeatedValueThenReturnsFalse() {
        fields[0].setFieldValue(1);
        fields[2].setFieldValue(1);
        instantiateComponent();
        assertFalse(component.verify());
    }

    @Test
    void TestToStringIfCalledThenReturnsNonNull() {
        instantiateComponent();
        assertNotNull(component.toString());
    }

    @Test
    void TestEqualsIfPassedNullThenReturnsFalse() {
        instantiateComponent();
        assertFalse(component.equals(null));
    }

    @Test
    void TestEqualsIfPassedSelfThenReturnsTrue() {
        instantiateComponent();
        assertTrue(component.equals(component));
    }

    @Test
    void TestEqualsIfPassedObjectOfNotCompatibleClassThenReturnsFalse() {
        instantiateComponent();
        assertFalse(component.equals(2));
    }

    @Test
    void TestEqualsIfBothComponentsContainsTheSameFieldsThenReturnsTrue() {
        SudokuComponent sc1 = new SudokuComponent(fields);
        SudokuComponent sc2 = new SudokuComponent(fields);
        assertTrue(sc1.equals(sc2));
    }

    @Test
    void TestEqualsIfComponentsContainsDifferentFieldsThenReturnsFalse() {
        SudokuComponent sc1 = new SudokuComponent(fields);
        SudokuField[] sfa = new SudokuField[SudokuBoard.BOARD_SIZE];
        for (int i = 0; i < fields.length; ++i) {
            fields[i] = new SudokuField(2);
        }
        SudokuComponent sc2 = new SudokuComponent(sfa);
        assertFalse(sc1.equals(sc2));
    }

    @Test
    void TestEqualsIfComponentsContainsDifferentNumberOfFieldsThenReturnsFalse() {
        SudokuComponent sc1 = new SudokuComponent(fields);
        SudokuField[] sfa = new SudokuField[SudokuBoard.BOARD_SIZE - 2];
        for (int i = 0; i < fields.length; ++i) {
            fields[i] = new SudokuField();
        }
        SudokuComponent sc2 = new SudokuComponent(sfa);
        assertFalse(sc1.equals(sc2));
    }

    @Test
    void TestHashCodeIfCalledOnEqualObjectsThenBothCodesAreTheSame() {
        SudokuComponent sc1 = new SudokuComponent(fields);
        SudokuComponent sc2 = new SudokuComponent(fields);
        assertEquals(sc1.hashCode(), sc2.hashCode());
    }

    @Test
    void TestCloneIfNullThenThrowsNullPointerException() {
        SudokuComponent sc = null;
        try {
            sc.clone();
        } catch (NullPointerException | CloneNotSupportedException e) {
            assertTrue(true);
        }
    }

    @Test
    void TestCloneIfNonNullThenReturnsACopy() throws CloneNotSupportedException {
        SudokuComponent sc = new SudokuComponent(fields);
        SudokuComponent clone = sc.clone();
        assertEquals(sc, clone);
    }
}
