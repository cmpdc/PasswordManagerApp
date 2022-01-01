module uri.passwordmanagerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires validatorfx;
    requires org.json;
    requires json.simple;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports view;
    opens view to javafx.fxml;

    exports core.utils;
    opens core.utils to javafx.fxml;

    exports core;
    opens core to javafx.fxml;
}