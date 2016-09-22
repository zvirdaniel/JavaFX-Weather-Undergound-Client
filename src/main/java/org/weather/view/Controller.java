package org.weather.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.weather.DataManagement;
import org.weather.data.WundergroundProvider;

import java.net.URL;
import java.util.ResourceBundle;

import static org.weather.Main.getImageResource;

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
    private void refresh() throws IllegalArgumentException {
        String currentKey = DataManagement.getInstance().getCurrentKey();
        String city = DataManagement.getInstance().getCity();
        String state = DataManagement.getInstance().getState();

        DataManagement.getInstance().setWunderground(new WundergroundProvider(currentKey, state, city));
        showData();
    }

    private void showData() {
        WundergroundProvider wunderground = DataManagement.getInstance().getWunderground();

        weatherIcon.setImage(wunderground.getCurrentWeatherImage());
        countryLabel.setText(wunderground.getCountryName());
        cityLabel.setText(wunderground.getCity());
        weatherStringLabel.setText(wunderground.getCurrentWeatherString());
        tempCelsiusLabel.setText(String.valueOf(wunderground.getCurrentTempCelsius()) + " Celsius");
        feelsLikeTempLabel.setText("Feels like: " + String.valueOf(wunderground.getCurrentTempFeelsLikeCelsius()) + " Celsius");
        windSpeedLabel.setText(String.valueOf(wunderground.getCurrentWindKph()) + " kph");
        windDirectionLabel.setText(wunderground.getCurrentWindDirection());
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

