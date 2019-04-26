package me.duncte123.lavaplayerplayer.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainGui extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        initUI(primaryStage);
    }

    private void initUI(Stage stage) {
        final StackPane root = new StackPane();
        final Scene scene = new Scene(root, 300, 250);

        final Label lbl = new Label("Simple JavaFX application.");
        lbl.setFont(Font.font("Serif", FontWeight.NORMAL, 20));
        root.getChildren().add(lbl);

        stage.setTitle("Simple application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
