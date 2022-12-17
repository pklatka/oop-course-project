package com.evolutiongenerator.utils;

import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


import java.io.File;

/**
 * Wrapper class for javafx.stage.FileChooser
 *
 * @author Patryk Klatka
 */
public class FileChooser {
    private final Stage stage;

    /**
     * Constructor, sets stage
     *
     * @param stage Stage object
     */
    public FileChooser(Stage stage) {
        this.stage = stage;
    }

    /**
     * Show file chooser dialog to get file path
     *
     * @param extensionFilter Extension filter for file chooser
     * @return File path
     */
    public String getFilePath(ExtensionFilter extensionFilter) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        // Set extension filter for text files
        fileChooser.getExtensionFilters().add(extensionFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(stage);

        if(file == null){
            return null;
        }

        return file.getPath();
    }

    /**
     * Show file chooser dialog to get location where to save file
     *
     * @param extensionFilter Extension filter for file chooser
     * @return File path
     */
    public String saveFilePath(ExtensionFilter extensionFilter) {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        // Set extension filter for text files
        fileChooser.getExtensionFilters().add(extensionFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if(file == null){
            return null;
        }

        return file.getPath();
    }
}
