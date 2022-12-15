package com.evolutiongenerator.utils;

import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


import java.io.File;

/**
 * Wrapper class for javafx.stage.FileChooser
 *
 * @author Patryk Klatka
 */
public class FileChooser
{
    private final Stage stage;

    /**
     * Constructor, sets stage
     *
     * @author Patryk Klatka
     * @param stage Stage object
     */
    public FileChooser(Stage stage){
        this.stage = stage;
 }

    /**
     * Show file chooser dialog to get file path
     *
     * @author Patryk Klatka
     * @param extensionFilter Extension filter for file chooser
     * @return File path
     */
    public String getFilePath(ExtensionFilter extensionFilter){
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        // Set extension filter for text files
        fileChooser.getExtensionFilters().add(extensionFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(stage);

        return file.getPath();
    }

    /**
     * Show file chooser dialog to get location where to save file
     *
     * @author Patryk Klatka
     * @param extensionFilter Extension filter for file chooser
     * @return File path
     */
    public String saveFilePath(ExtensionFilter extensionFilter){
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        // Set extension filter for text files
        fileChooser.getExtensionFilters().add(extensionFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        return file.getPath();
    }
}
