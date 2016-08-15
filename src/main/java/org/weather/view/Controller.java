package org.weather.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.weather.data.WundergroundProvider;
import org.weather.utils.BasicUtils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ImageView weatherIcon;
    @FXML
    private Label countryLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label weatherStringLabel;
    @FXML
    private Label tempCelsiusLabel;
    @FXML
    private Label feelsLikeTempLabel;
    @FXML
    private Label windSpeedLabel;
    @FXML
    private Label windDirectionLabel;
    @FXML
    private AnchorPane outerAnchorPane;

    private BasicUtils basicUtils = new BasicUtils();
    private String currentKey;

    /**
     * This method gets executed every time the application runs.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String state = null;
        String city = null;

        setDefaultApiKey();

        Optional<Pair<String, String>> locationData;
        WundergroundProvider wunderground;

        while (true) {
            locationData = userInputDialog();

            if (locationData.isPresent()) {
                state = locationData.get().getKey().trim();
                city = locationData.get().getValue().trim();
            } else {
                System.exit(0);
            }

            try {
                wunderground = new WundergroundProvider(currentKey, state, city);
                showWeatherData(wunderground);
                break;
            } catch (IllegalArgumentException e) {
                basicUtils.showError("location", "The location was not found.");
            }
        }
    }

    private Optional<Pair<String, String>> userInputDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Select location");
        dialog.setHeaderText("Please, enter your location:");


        ButtonType loginButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField state = new TextField();
        state.setPromptText("GB / FL / CZ");

        TextField city = new TextField();
        city.setPromptText("London / Miami / Prague");

        grid.add(new Label("State:"), 0, 0);
        grid.add(state, 1, 0);

        grid.add(new Label("City:"), 0, 1);
        grid.add(city, 1, 1);

        Node confirmButton = dialog.getDialogPane().lookupButton(loginButtonType);
        confirmButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        state.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(state.getText(), city.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void showWeatherData(WundergroundProvider wunderground) {
        // Set the current org.weather image
        weatherIcon.setImage(wunderground.getCurrentWeatherImage());

        // Set all the other org.weather data
        countryLabel.setText(wunderground.getCountryName());
        cityLabel.setText(wunderground.getCity());
        weatherStringLabel.setText(wunderground.getCurrentWeatherString());
        tempCelsiusLabel.setText(String.valueOf(wunderground.getCurrentTempCelsius()) + " Celsius");
        feelsLikeTempLabel.setText("Feels like: " + String.valueOf(wunderground.getCurrentTempFeelsLikeCelsius()) + " Celsius");
        windSpeedLabel.setText(String.valueOf(wunderground.getCurrentWindKph()) + " kph");
        windDirectionLabel.setText(wunderground.getCurrentWindDirection());
    }

    private void setDefaultApiKey() {
        currentKey = "5d2ff9078b329570";
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Duno Weather App - About");
        alert.setHeaderText("About this application");
        alert.setContentText("Author: Daniel Zvir\nThis is my first JavaFX application.\n\n");
        alert.showAndWait();
    }
}

