package org.weather.utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Daniel Zvir on 27.07.2016.
 */
public class BasicUtils {
    /**
     * @param currentStage                which stage should show the selector
     * @param defaultExtensionDescription Example: "MS Excel Spreadsheet (*.xlsx)"
     * @param defaultExtension            Example: "*.xlsx"
     * @param defaultFileName             Example: "File.xlsx"
     * @return selected file
     */
    public File fileSaver(Stage currentStage, String defaultExtensionDescription, String defaultExtension, String defaultFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Where do you want to save your file?");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(defaultExtensionDescription, defaultExtension));
        fileChooser.setInitialFileName(defaultFileName);
        File selectedFile = fileChooser.showSaveDialog(currentStage);
        if (selectedFile != null) {
            if (!selectedFile.getName().endsWith(defaultExtension)) {
                showErrorWithSavingDialog("file", "The file cannot be saved.");
            }
        } else {
            showErrorWithSavingDialog("file", "The file cannot be saved.");
        }
        return selectedFile;
    }

    /**
     * @param currentStage which stage should show the selector
     * @return selected directory
     */
    public File directorySelector(Stage currentStage) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select directory");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = dirChooser.showDialog(currentStage);
        if (selectedDirectory == null) {
            showErrorWithSavingDialog("directory", "The directory cannot be selected.");
        }
        return selectedDirectory;
    }

    /**
     * @param type    Example: "directory"
     * @param content Example: "The directory cannot be saved."
     */
    private void showErrorWithSavingDialog(String type, String content) {
        // Create an alert dialog, set an icon to it
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResource("/images/error.png").toString()));

        alert.setTitle("Error with saving " + type);
        alert.setHeaderText("Error with saving " + type + "!");
        alert.setContentText(content);
        alert.showAndWait();
    }

}
