package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game extends Application {

    Stage stage;
    @FXML
    public Button start;
    @FXML
    public RadioButton easy;
    @FXML
    public RadioButton medium;
    @FXML
    public RadioButton hard;
    @FXML
    public RadioMenuItem english;
    @FXML
    public RadioMenuItem polish;
    private SudokuBoard sudokuBoard;
    private SudokuBoard originalSudokuBoard;
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    public static void main(String[] args) {
        launch();
    }

    @FXML
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        logger.info(ViewLang.get("message.initializing_menu_scene"));
        Scene menu = new Scene(FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/Menu.fxml")),
                ResourceBundle.getBundle("Language", ViewLang.getLocale())));

        Label label = (Label) menu.lookup("#difficulty");
        RadioButton easy = (RadioButton) menu.lookup("#easy");
        RadioButton medium = (RadioButton) menu.lookup("#medium");
        RadioButton hard = (RadioButton) menu.lookup("#hard");
        Button start = (Button) menu.lookup("#start");
        MenuBar menuBar = (MenuBar) menu.lookup("#menuBar");
        Menu fileMenu = menuBar.getMenus().get(0);
        Menu load = (Menu) fileMenu.getItems().get(0);
        MenuItem loadFromFile = load.getItems().get(0);
        MenuItem loadFromDB = load.getItems().get(1);
        Menu settings = (Menu) fileMenu.getItems().get(1);
        Menu language = (Menu) settings.getItems().get(0);
        MenuItem english = language.getItems().get(0);
        MenuItem polish = language.getItems().get(1);
        Menu helpMenu = menuBar.getMenus().get(1);
        MenuItem about = helpMenu.getItems().get(0);

        label.textProperty().bind(ViewLang.createStringBinding("menu.difficulty"));
        easy.textProperty().bind(ViewLang.createStringBinding("menu.easy"));
        medium.textProperty().bind(ViewLang.createStringBinding("menu.medium"));
        hard.textProperty().bind(ViewLang.createStringBinding("menu.hard"));
        start.textProperty().bind(ViewLang.createStringBinding("menu.start"));
        fileMenu.textProperty().bind(ViewLang.createStringBinding("menu.file"));
        load.textProperty().bind(ViewLang.createStringBinding("menu.load"));
        loadFromFile.textProperty().bind(ViewLang.createStringBinding("menu.load_from_file"));
        loadFromDB.textProperty().bind(ViewLang.createStringBinding("menu.load_from_db"));
        settings.textProperty().bind(ViewLang.createStringBinding("menu.settings"));
        language.textProperty().bind(ViewLang.createStringBinding("menu.language"));
        english.textProperty().bind(ViewLang.createStringBinding("menu.english"));
        polish.textProperty().bind(ViewLang.createStringBinding("menu.polish"));
        helpMenu.textProperty().bind(ViewLang.createStringBinding("menu.help"));
        about.textProperty().bind(ViewLang.createStringBinding("menu.about"));

        stage.titleProperty().bind(ViewLang.createStringBinding("window.title"));
        stage.setScene(menu);
        stage.show();
    }

    public void showInfo() {
        logger.info(ViewLang.get("message.initializing_about_window"));
        About.display();
    }

    @FXML
    public void newGame() {
        logger.info(ViewLang.get("message.initializing_new_game"));
        sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        sudokuBoard.solveGame();

        Difficulty difficulty;
        if (easy.isSelected()) {
            difficulty = Difficulty.EASY;
        } else if (medium.isSelected()) {
            difficulty = Difficulty.MEDIUM;
        } else {
            difficulty = Difficulty.HARD;
        }
        difficulty.clearFields(sudokuBoard);
        originalSudokuBoard = sudokuBoard.clone();

        run();
    }

    @FXML
    public void loadGame() {
        logger.info(ViewLang.get("message.loading_game"));
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            logger.info(ViewLang.get("message.file_selected") + ": " + file.getPath());
            try (FileSudokuBoardDao serializer = new FileSudokuBoardDao(file.getPath())) {
                originalSudokuBoard = serializer.read();
                sudokuBoard = serializer.read();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            run();
        }
    }

    @FXML
    public void run() {
        logger.info(ViewLang.get("message.initializing_run_scene"));
        ToggleGroup languageGroup = new ToggleGroup();
        RadioMenuItem english = new RadioMenuItem();
        RadioMenuItem polish = new RadioMenuItem();

        english.setToggleGroup(languageGroup);
        polish.setToggleGroup(languageGroup);

        if (ViewLang.getLocale() == Locale.ENGLISH) {
            english.setSelected(true);
        } else {
            polish.setSelected(true);
        }

        english.setOnAction(e -> {
            logger.info(ViewLang.get("message.switching_language_to_english"));
            ViewLang.setLocale(Locale.ENGLISH);
        });
        polish.setOnAction(e -> {
            logger.info(ViewLang.get("message.switching_language_to_polish"));
            ViewLang.setLocale(Locale.forLanguageTag("pl"));
        });

        Menu fileMenu = new Menu();
        Menu save = new Menu();
        MenuItem saveToFile = new MenuItem();
        MenuItem saveToDB = new MenuItem();
        Menu settings = new Menu();
        Menu language = new Menu();
        MenuItem backToMenu = new MenuItem();

        fileMenu.textProperty().bind(ViewLang.createStringBinding("menu.file"));
        save.textProperty().bind(ViewLang.createStringBinding("menu.save_as"));
        saveToFile.textProperty().bind(ViewLang.createStringBinding("menu.save_to_file"));
        saveToDB.textProperty().bind(ViewLang.createStringBinding("menu.save_to_db"));
        settings.textProperty().bind(ViewLang.createStringBinding("menu.settings"));
        language.textProperty().bind(ViewLang.createStringBinding("menu.language"));
        english.textProperty().bind(ViewLang.createStringBinding("menu.english"));
        polish.textProperty().bind(ViewLang.createStringBinding("menu.polish"));
        backToMenu.textProperty().bind(ViewLang.createStringBinding("menu.back_to_menu"));

        language.getItems().addAll(english, polish);
        settings.getItems().add(language);
        save.getItems().addAll(saveToFile, saveToDB);
        fileMenu.getItems().addAll(save, settings, backToMenu);
        MenuBar bar = new MenuBar();
        bar.getMenus().add(fileMenu);

        GridPane board = new GridPane();
        board.getStyleClass().add("grid");
        VBox vbox = new VBox(bar, board);

        PseudoClass right = PseudoClass.getPseudoClass("right");
        PseudoClass bottom = PseudoClass.getPseudoClass("bottom");

        logger.info(ViewLang.get("message.constructing_board"));
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                StackPane cell = new StackPane();
                cell.getStyleClass().add("cell");
                cell.pseudoClassStateChanged(right, col == 2 || col == 5);
                cell.pseudoClassStateChanged(bottom, row == 2 || row == 5);
                TextField tf = createTextField();
                tf.setText(Integer.toString(sudokuBoard.get(row, col)));
                if (originalSudokuBoard.get(row, col) != SudokuBoard.BLANK) {
                    tf.setDisable(true);
                } else {
                    final int x = row;
                    final int y = col;
                    tf.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.length() == 1) {
                            sudokuBoard.set(x, y, newValue.charAt(0) - '0');
                            if (sudokuBoard.isSolved()) {
                                logger.info(ViewLang.get("message.solved"));
                                try {
                                    Solved.display();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                }
                cell.getChildren().add(tf);
                board.add(cell, col, row);
            }
        }

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("/Board.css");
        Stage window = (Stage) start.getScene().getWindow();
        window.setScene(scene);
        window.show();

        saveToFile.setOnAction(c -> {
            logger.info(ViewLang.get("message.initializing_board_save"));
                FileChooser fc = new FileChooser();
                File file = fc.showSaveDialog(window);
                if (file != null) {
                    logger.info(ViewLang.get("message.file_selected") + ": " + file.getPath());
                    try (FileSudokuBoardDao serializer = new FileSudokuBoardDao(file.getPath())) {
                        serializer.write(originalSudokuBoard);
                        serializer.write(sudokuBoard);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        });

        saveToDB.setOnAction(c -> {
            logger.info(ViewLang.get("message.initializing_board_save"));
            try {
                String boardName = BoardSave.display();
                logger.info(ViewLang.get("message.selected_board") + ": " + boardName);
                if (boardName.isEmpty()) {
                    return;
                }
                try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao(boardName + "-ORIGINAL")) {
                    dao.write(originalSudokuBoard);
                }
                try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao(boardName)) {
                    dao.write(sudokuBoard);
                }
            } catch (IOException | SudokuJdbcException e) {
                throw new RuntimeException(e);
            }
        });

        backToMenu.setOnAction(c -> {
            try {
                logger.info(ViewLang.get("message.going_back_to_menu"));
                start(window);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private TextField createTextField() {
        TextField textField = new TextField();

        // restrict input to integers:
        textField.setTextFormatter(new TextFormatter<Integer>(c -> {
            if (c.getControlNewText().matches("[1-9]?")) {
                return c;
            } else {
                return null;
            }
        }));
        return textField;
    }

    public void switchLanguage() {
        if (english.isSelected()) {
            logger.info(ViewLang.get("message.switching_language_to_english"));
            ViewLang.setLocale(Locale.ENGLISH);
        } else {
            logger.info(ViewLang.get("message.switching_language_to_polish"));
            ViewLang.setLocale(Locale.forLanguageTag("pl"));
        }
    }

    public void loadBoardFromDB() {
        logger.info(ViewLang.get("message.initializing_board_selection"));
        try {
            String boardName = BoardSelect.display();
            logger.info(ViewLang.get("message.selected_board") + ": " + boardName);
            if (boardName.isEmpty()) {
                return;
            }
            try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao(boardName + "-ORIGINAL")) {
                originalSudokuBoard = dao.read();
            }
            try (JdbcSudokuBoardDao dao = new JdbcSudokuBoardDao(boardName)) {
                sudokuBoard = dao.read();
            }
        } catch (SudokuJdbcException | IOException e) {
            throw new RuntimeException(e);
        }

        run();
    }
}