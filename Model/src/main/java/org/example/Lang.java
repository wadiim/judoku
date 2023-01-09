package org.example;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {
    public static String get(final String key, final String... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("Language", Locale.getDefault());
        return bundle.getString(key) + Arrays.toString(args)
                .replace(",", "")
                .replace("[", "")
                .replace("]", "");
    }
}
