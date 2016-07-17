package weather.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import weather.data.WundergroundDataProvider;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private CheckMenuItem defaultApiKey;
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

    private String API_key = "5d2ff9078b329570";

    /**
     * This method gets executed every time the application runs.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Downloads all the data from the internet
        WundergroundDataProvider wunderground = new WundergroundDataProvider(API_key, "CzechRepublic", "Ostrava");
        WundergroundDataProvider w2 = new WundergroundDataProvider(API_key, "CZ", "Opava");

        // Set the current weather image
        weatherIcon.setImage(wunderground.getCurrentWeatherImage());

        // Set all the other weather data
        countryLabel.setText(wunderground.getCountryName());
        cityLabel.setText(wunderground.getCity());
        weatherStringLabel.setText(wunderground.getCurrentWeatherString());
        tempCelsiusLabel.setText(String.valueOf(wunderground.getCurrentTempCelsius()) + " Celsius");
        feelsLikeTempLabel.setText("Feels like: " + String.valueOf(wunderground.getCurrentTempFeelsLikeCelsius()) + " Celsius");
        windSpeedLabel.setText(String.valueOf(wunderground.getCurrentWindKph()) + " kph");
        windDirectionLabel.setText(wunderground.getCurrentWindDirection());

        // TODO: 17.07.2016 Add API Key managing system, to handle multiple API keys
        defaultApiKey.setText(API_key);
        defaultApiKey.setSelected(true);
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Duno Weather App - About");
        alert.setHeaderText("About this application");
        alert.setContentText("Author: Daniel Zvir\nThis is my first JavaFX application.\n\nThe Weather Underground API allows 500 calls a day and 10 calls a minute per API key.");
        alert.showAndWait();
    }


}
