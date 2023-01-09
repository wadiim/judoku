package org.example;

import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Solved {
    public static void display() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.titleProperty().bind(ViewLang.createStringBinding("message.solved"));
        Scene scene = new Scene(FXMLLoader.load(
                Objects.requireNonNull(Solved.class.getResource("/Solved.fxml")),
                ResourceBundle.getBundle("Language", ViewLang.getLocale())));

        Button ok = (Button) scene.lookup("#ok");
        ok.setOnAction(e -> {
            window.close();
        });

        window.setScene(scene);
        window.showAndWait();
    }
}
