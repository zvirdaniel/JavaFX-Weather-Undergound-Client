package weather.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import weather.data.WundergroundDataProvider;

public class Controller {
    private String defaultURL = "http://api.wunderground.com/api/5d2ff9078b329570/geolookup/conditions/forecast/q/CzechRepublic/Ostrava.json";
    private WundergroundDataProvider wunderground = new WundergroundDataProvider(defaultURL);

    private void setCurrentWeatherPicture() {
        wunderground.getCurrentWeatherIcon();
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

    /**
     * Sets an image for current weather.
     */
    @FXML
    private void imgView() {

    }
}
