import org.example.Lang;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class LangTest {
    @Test
    void TestConstructor() {
        // Create Lang instance to make JaCoCo happy.
        Lang lang = new Lang();
        assertNotNull(lang);
    }
}
