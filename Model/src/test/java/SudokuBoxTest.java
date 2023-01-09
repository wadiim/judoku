import org.example.SudokuBox;

public class SudokuBoxTest extends SudokuComponentTest {
    @Override
    void instantiateComponent() {
        component = new SudokuBox(fields);
    }
}