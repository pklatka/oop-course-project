package com.evolutiongenerator.model.ui;

import com.evolutiongenerator.model.mapObject.IMapElement;
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
            // Note: element.toString() was element.getImageResource(), but we won't use images in gui
            ImageView imageView;
            if (images.containsKey(element.toString())) {
                imageView = new ImageView(images.get(element.toString()));
            } else {
                String filePath = new File("").getAbsolutePath();
                filePath = filePath.concat("/src/main/resources/");
                Image image = new Image(new FileInputStream(filePath.concat(element.toString())));
                images.put(element.toString(), image);
                imageView = new ImageView(image);
            }
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);

            // Was element.getObjectLabel
//            label = new Label(element.getObjectType().toString());
            label = new Label();

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