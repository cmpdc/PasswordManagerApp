package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import core.utils.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // controller
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("loginFrame.fxml"));
//        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("mainFrame.fxml"));

        Parent root = loader.load();

        // adding CSS
        root.getStylesheets().add(MainApp.class.getResource("style.css").toExternalForm());
        Platform.runLater(root::requestFocus); // removal of autofocus for fields

        // passing "root" to "scene" and setting login window's sizes
        Scene scene = new Scene(root, Constants.APP_LOGIN_SIZE[0], Constants.APP_LOGIN_SIZE[1]);
//        Scene scene = new Scene(root, Constants.APP_MAIN_SIZE[0], Constants.APP_MAIN_SIZE[1]);

        // set stage to transparent in an attempt to make the window's edges round
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        // adding application icon
        stage.getIcons().add(new Image(String.valueOf(MainApp.class.getResource("logo.png"))));

        // adding title
        stage.setTitle(Constants.APP_NAME);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {

        // sign up function is unfinished
        // to login, use "admin" as username and "pass" as password
        launch();
    }
}