package org.weather.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.weather.data.WundergroundProvider;

import java.net.URL;
import java.util.ResourceBundle;

import static org.weather.DataProvider.INSTANCE;
import static org.weather.Main.getImageResource;
import static org.weather.utils.BasicUtils.showError;

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

    /**
     * This method gets executed every time the application runs.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData();
    }

    @FXML
    private void refresh() {
        String currentKey = INSTANCE.getCurrentKey();
        String city = INSTANCE.getCity();
        String state = INSTANCE.getState();

        try {
            INSTANCE.setWunderground(new WundergroundProvider(currentKey, state, city));
        } catch (IllegalArgumentException e) {
            if (e.toString().contains("No internet connection!")) {
                showError("internet", "No internet connection");
                System.exit(0);
            }
        }

        showData();
    }

    private void showData() {
        WundergroundProvider wunderground = INSTANCE.getWunderground();

        if (wunderground != null) {
            weatherIcon.setImage(wunderground.getCurrentWeatherImage());
            countryLabel.setText(wunderground.getCountryName());
            cityLabel.setText(wunderground.getCity());
            weatherStringLabel.setText(wunderground.getCurrentWeatherString());
            tempCelsiusLabel.setText(String.valueOf(wunderground.getCurrentTempCelsius()) + " Celsius");
            feelsLikeTempLabel.setText("Feels like: " + String.valueOf(wunderground.getCurrentTempFeelsLikeCelsius()) + " Celsius");
            windSpeedLabel.setText(String.valueOf(wunderground.getCurrentWindKph()) + " kph");
            windDirectionLabel.setText(wunderground.getCurrentWindDirection());
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(getImageResource("/images/info.png"));
        alert.setTitle("Duno Weather App - About");
        alert.setHeaderText("About this application");
        alert.setContentText("Author: Daniel Zvir\nThis is my first JavaFX application.\n\n");
        alert.showAndWait();
    }
}

