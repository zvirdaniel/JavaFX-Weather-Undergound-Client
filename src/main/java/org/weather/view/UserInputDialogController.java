package org.weather.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.weather.DataManagement;

/**
 * Created by zvird on 19.9.16.
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

        DataManagement.getInstance().setCity(null);
        DataManagement.getInstance().setState(null);

        isActive = true;
        Thread t = new Thread(this);
        t.start();
    }

    @FXML
    private void handleOkButton() {
        String state = stateInput.getCharacters().toString().trim();
        String city = cityInput.getCharacters().toString().trim();

        DataManagement.getInstance().setState(state);
        DataManagement.getInstance().setCity(city);

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
                Thread.sleep(250);

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
