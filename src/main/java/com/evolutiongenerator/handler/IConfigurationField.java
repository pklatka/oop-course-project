package com.evolutiongenerator.handler;

import javafx.scene.control.Control;

public interface IConfigurationField {
    void writeProperty(String text);
    String readProperty();
}
