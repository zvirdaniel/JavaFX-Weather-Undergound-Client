package weather.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import weather.data.WundergroundDataProvider;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private WundergroundDataProvider wunderground;

    @FXML
    private ImageView weatherIcon;

    /**
     * This method gets executed every time the application runs.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize wunderground object, fetch all weather data from default URL.
        String defaultURL = "http://api.wunderground.com/api/5d2ff9078b329570/geolookup/conditions/forecast/q/CzechRepublic/Ostrava.json";
        this.wunderground = new WundergroundDataProvider(defaultURL);

        // Set the current weather image
        try {
            weatherIcon.setImage(wunderground.getCurrentWeatherImage());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
