package org.weather;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.weather.data.WundergroundProvider;

import static org.weather.utils.BasicUtils.showError;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        while (true) {
            showDialog(primaryStage);

            String state = DataManagement.getInstance().getState();
            String city = DataManagement.getInstance().getCity();
            String currentKey = DataManagement.getInstance().getCurrentKey();

            if(state == null && city == null) {
                System.exit(0);
            }

            try {
                WundergroundProvider wunderground = new WundergroundProvider(currentKey, state, city);
                DataManagement.getInstance().setWunderground(wunderground);
                break;
            } catch (IllegalArgumentException e) {
                if (e.getMessage().equals("Location not found!")) {
                    showError("location", "The location was not found.");
                }

                if (e.toString().contains("No internet connection!")) {
                    showError("internet", "No internet connection");
                    System.exit(0);
                }
            }
        }

        // Main app
        Parent root = FXMLLoader.load(getClass().getResource("view/MainApp.fxml"));
        primaryStage.setTitle("JavaFX Weather App");
        primaryStage.getIcons().add(getImageResource("/images/app.png"));
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    private void showDialog(Stage primaryStage) throws java.io.IOException {
        GridPane grid = FXMLLoader.load(getClass().getResource("view/UserInputDialog.fxml"));
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Enter your location");
        dialogStage.getIcons().add(getImageResource("/images/app.png"));
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(grid);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public static Image getImageResource(String name) {
        return new Image(Main.class.getClass().getResource(name).toString());
    }
}
