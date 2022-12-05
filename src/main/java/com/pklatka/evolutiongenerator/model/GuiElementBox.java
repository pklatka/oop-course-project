package com.pklatka.evolutiongenerator.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class GuiElementBox {
    private Label label;
    private VBox box;
    // GuiElementBox is used in one thread -> no risk of concurrent access problems
    public static HashMap<String, Image> images = new HashMap<>();

    public GuiElementBox(IMapElement element) throws FileNotFoundException {
        try {
            ImageView imageView;
            if (images.containsKey(element.getImageResource())) {
                imageView = new ImageView(images.get(element.getImageResource()));
            } else {
                String filePath = new File("").getAbsolutePath();
                filePath = filePath.concat("/src/main/resources/");
                Image image = new Image(new FileInputStream(filePath.concat(element.getImageResource())));
                images.put(element.getImageResource(), image);
                imageView = new ImageView(image);
            }
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);

            label = new Label(element.getObjectLabel());

            box = new VBox(1);
            box.getChildren().addAll(imageView, label);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(5, 0, 0, 0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public VBox getNode() {
        return box;
    }
}