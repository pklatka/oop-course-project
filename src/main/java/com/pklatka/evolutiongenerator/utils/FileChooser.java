package com.pklatka.evolutiongenerator.utils;

import javafx.stage.Stage;

import java.io.File;

public class FileChooser
{
    private final Stage stage;

    public FileChooser(Stage stage){
        this.stage = stage;
    }

    public String getFilePath(){
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        // Set extension filter for text files
        javafx.stage.FileChooser.ExtensionFilter extFilter = new javafx.stage.FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file.getPath();
        }
        return "";
    }

    public String saveFilePath(){
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();

        // Set extension filter for text files
        javafx.stage.FileChooser.ExtensionFilter extFilter = new javafx.stage.FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            return file.getPath();
        }
        return "";
    }

}
