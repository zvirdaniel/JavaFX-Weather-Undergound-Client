package weather.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import weather.data.WundergroundDataProvider;
import weather.data.XLSXCreator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private MenuBar gg;
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
    @FXML
    private AnchorPane outerAnchorPane;
    @FXML
    private Circle circleOne;

    /**
     * This method gets executed every time the application runs.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Downloads all the data from the internet
        String API_key = "5d2ff9078b329570";
        WundergroundDataProvider wunderground = new WundergroundDataProvider(API_key, "CzechRepublic", "Ostrava");

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

    @FXML
    public void handleDirectoryToXLSX() throws IOException {
        Stage currentStage = (Stage) outerAnchorPane.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource("/images/error.png").toString()));

        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select directory to scan");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = dirChooser.showDialog(currentStage);
        if (selectedDirectory == null) {
            alert.setTitle("Error with selecting a directory");
            alert.setHeaderText("Error with selecting a directory!");
            alert.setContentText("You have not selected any directory!");
            alert.showAndWait();
        }

        if (selectedDirectory != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Where do you want to save your file?");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MS Excel Spreadsheet (*.xlsx)", "*.xlsx"));
            fileChooser.setInitialFileName("Duno Apache POI.xlsx");
            File selectedFile = fileChooser.showSaveDialog(currentStage);
            if (selectedFile != null) {
                if (!selectedFile.getName().endsWith(".xlsx")) {
                    showErrorWithSavingFileDialog(alert);
                }
            } else {
                showErrorWithSavingFileDialog(alert);
            }

            if (selectedFile != null) {
                TextInputDialog dialog = new TextInputDialog("Sheet");
                dialog.setTitle("Set sheet name");
                dialog.setHeaderText("Enter sheet name");
                dialog.setContentText("Please enter desired sheet name:");
                Stage stg = (Stage) dialog.getDialogPane().getScene().getWindow();
                stg.getIcons().add(new Image(getClass().getResource("/images/info.png").toString()));

                Optional<String> result = dialog.showAndWait();
                XLSXCreator creator = new XLSXCreator();
                if (result.isPresent()) {
                    creator.createXLSXwithDirectoryContent(selectedDirectory, result.get(), selectedFile);
                }
            }
        }
    }

    private void showErrorWithSavingFileDialog(Alert alert) {
        alert.setTitle("Error with saving file");
        alert.setHeaderText("Error with saving file!");
        alert.setContentText("For some reason, the file can not be saved.");
        alert.showAndWait();
    }


}
