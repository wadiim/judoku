package org.example;

import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BoardSave {

    private static String name;

    public static String display() throws IOException, SudokuJdbcException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.titleProperty().bind(ViewLang.createStringBinding("title.board_save"));
        Scene scene = new Scene(FXMLLoader.load(
                Objects.requireNonNull(BoardSave.class.getResource("/BoardSave.fxml")),
                ResourceBundle.getBundle("Language", ViewLang.getLocale())));

        TextField input = (TextField) scene.lookup("#input");

        Button save = (Button) scene.lookup("#save");
        Button cancel = (Button) scene.lookup("#cancel");

        save.setOnAction(e -> {
            name = input.getText();
            window.close();
        });
        cancel.setOnAction(e -> {
            name = "";
            window.close();
        });

        window.setScene(scene);
        window.showAndWait();

        return name;
    }
}
