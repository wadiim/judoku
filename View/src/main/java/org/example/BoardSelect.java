package org.example;

import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BoardSelect {

    private static String name;

    public static String display() throws IOException, SudokuJdbcException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.titleProperty().bind(ViewLang.createStringBinding("title.board_selection"));
        Scene scene = new Scene(FXMLLoader.load(
                Objects.requireNonNull(BoardSelect.class.getResource("/BoardSelect.fxml")),
                ResourceBundle.getBundle("Language", ViewLang.getLocale())));

        ChoiceBox select = (ChoiceBox) scene.lookup("#select");

        // Match strings that does not end with '-ORIGINAL'
        Pattern pattern = Pattern.compile("^(?!.*-ORIGINAL$).*$");

        try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao("")) {
            select.setItems(FXCollections.observableArrayList(
                    dao.getAllNames()
                            .stream()
                            .filter(pattern.asPredicate())
                            .collect(Collectors.toList())
            ));
        }

        Button load = (Button) scene.lookup("#load");
        Button cancel = (Button) scene.lookup("#cencel");
        load.setOnAction(e -> {
            name = select.getSelectionModel().getSelectedItem().toString();
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
