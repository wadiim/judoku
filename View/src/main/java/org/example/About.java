package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class About {
    public static void display() {
        Info info = new Info();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.titleProperty().bind(ViewLang.createStringBinding("menu.about"));

        Label label = new Label();
        label.textProperty().bind(ViewLang.createStringBinding(info.getContents()[0][0].toString(),
                " " + info.getContents()[0][1]));

        Button closeButton = new Button();
        closeButton.textProperty().bind(ViewLang.createStringBinding("button.close"));
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(24, 24, 24, 24));
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
