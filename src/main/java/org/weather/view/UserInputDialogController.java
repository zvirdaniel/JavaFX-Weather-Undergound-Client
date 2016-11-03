package org.weather.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static org.weather.DataProvider.INSTANCE;

/**
 * Created by Daniel Zvir on 19.9.16.
 */
public class UserInputDialogController implements Runnable {
    @FXML
    private TextField cityInput;
    @FXML
    private TextField stateInput;
    @FXML
    private Button okButton;

    private boolean isActive;

    @FXML
    private void initialize() {
        Platform.runLater(() -> cityInput.requestFocus());
        okButton.setDisable(true);

        INSTANCE.setCity(null);
        INSTANCE.setState(null);

        isActive = true;
        Thread t = new Thread(this);
        t.start();
    }

    @FXML
    private void handleOkButton() {
        String state = stateInput.getCharacters().toString().trim();
        String city = cityInput.getCharacters().toString().trim();

        INSTANCE.setState(state);
        INSTANCE.setCity(city);

        isActive = false;
        Stage dialogStage = (Stage) okButton.getScene().getWindow();
        dialogStage.close();
    }

    @FXML
    private void handleCancelButton() {
        System.exit(0);
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                Thread.sleep(200);

                if (cityInput.getCharacters().toString().trim().isEmpty()) {
                    okButton.setDisable(true);
                } else {
                    okButton.setDisable(false);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
