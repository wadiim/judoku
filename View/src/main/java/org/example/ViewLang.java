package org.example;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ViewLang {
    private static final ObjectProperty<Locale> locale;

    static {
        locale = new SimpleObjectProperty<>(Locale.getDefault());
        locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }

    public static Locale getLocale() {
        return locale.get();
    }

    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    public static ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    public static String get(final String key, final String... args) {
        ResourceBundle bundle = ResourceBundle.getBundle("Language", getLocale());
        return bundle.getString(key) + Arrays.toString(args)
                .replace(",", "")
                .replace("[", "")
                .replace("]", "");
    }

    public static StringBinding createStringBinding(final String key, String... args) {
        return Bindings.createStringBinding(() -> get(key, args), locale);
    }
}
