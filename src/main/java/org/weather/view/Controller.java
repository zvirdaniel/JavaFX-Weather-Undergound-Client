package org.weather.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.weather.data.WundergroundProvider;
import org.weather.data.XLSXCreator;
import org.weather.utils.BasicUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private CheckMenuItem defaultApiKey;
    @FXML
    private CheckMenuItem userApiKey;
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
        boolean hasConnected = false;
        userInputDialog();
        setDefaultApiKey();

        // Downloads all the data from the internet
        WundergroundProvider wunderground = null;
        try {
            wunderground = new WundergroundProvider(currentKey, "CzechRepublic", "Ostrava");
            hasConnected = true;
        } catch (IOException e) {
            if (e.toString().contains("java.net.UnknownHostException: api.wunderground.com")) {
                System.err.println("Cannot resolve \"api.wunderground.com\"");
            } else {
                e.printStackTrace();
            }
        }

        if (hasConnected) {
            showWeatherData(wunderground);
        }

    }

    private void userInputDialog() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(getClass().getResource("/images/MainAppIcon.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(username::requestFocus);

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });
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

    @FXML
    public void handleDirectoryToXLSX() throws IOException {
        // Get current stage via outerAnchorPane
        Stage currentStage = (Stage) outerAnchorPane.getScene().getWindow();

        File selectedDirectory = basicUtils.directorySelector(currentStage);

        if (selectedDirectory != null) {
            File selectedFile = basicUtils.fileSaver(currentStage, "MS Excel Spreadsheet (*.xlsx)", "*.xlsx", "Duno Apache POI.xlsx");

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
}
