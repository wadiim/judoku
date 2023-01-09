package org.example;

import java.util.ListResourceBundle;

public class Info extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"about.author", "Wadim X. Janikowski"}
        };
    }
}
