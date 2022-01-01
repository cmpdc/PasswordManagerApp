package view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import core.utils.Constants;
import core.utils.ShakeWindow;
import core.utils.TitleBar;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginFrame implements Initializable {

    @FXML private Pane outerElem;
    @FXML private BorderPane innerElem;
    @FXML private BorderPane innerElemShadow;
    @FXML private Pane panels;
    @FXML private VBox box2;
    @FXML private Pane formRegister;
    @FXML private TextField inputUserRegister;
    @FXML private PasswordField inputPassword1Register;
    @FXML private PasswordField inputPassword2Register;
    @FXML private Button registerButton;
    @FXML private Pane formLogin;
    @FXML private TextField inputUserLogin;
    @FXML private PasswordField inputPasswordLogin;
    @FXML private Button loginButton;
    @FXML private Button forgotPasswordButton;
    @FXML private Pane formForgotPassword;
    @FXML private Label loginFailedMessage;
    @FXML private Label loginFailed;

    @FXML private Pane login_usernamePane;
    @FXML private Pane login_passwordPane;
    @FXML private Pane register_usernamePane;
    @FXML private Pane register_passwordPane1;
    @FXML private Pane register_passwordPane2;

    private int failedAttemptCounter = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // adding custom title bar
        innerElem.setTop(new TitleBar(
                Constants.APP_NAME + " - LOGIN",
                -200,
                Constants.APP_LOGIN_SIZE[0] - 20,
                Constants.APP_LOGIN_SIZE[1])
        );

        // equivalent to css' "overflow: hidden;"
        // using rectangle as a way to "clip" edges to make the edges of the window rounded
        Rectangle overflow = new Rectangle(Constants.APP_LOGIN_SIZE[0] - 20, Constants.APP_LOGIN_SIZE[1] - 20);
        overflow.setX(0);
        overflow.setY(0);
        overflow.setArcWidth(15.0); // adding arc (radius)
        overflow.setArcHeight(15.0);
        innerElem.setClip(overflow);

        // "innerElemShadow" is the same as "innerElem" but at the back to add shadow
        innerElem.toFront();
        innerElemShadow.toBack();

        // z-indexing - JavaFx's CSS has no "z-index" :(
        panels.toBack();
        box2.toBack();

        formRegister.toFront();
        formLogin.toFront();
        formForgotPassword.toFront();

        SVGPath userIcon = new SVGPath();
        userIcon.getStyleClass().add("fieldIcon");
        userIcon.setLayoutX(4);
        userIcon.setLayoutY(4);
        userIcon.setContent("M 5.70,10.82 C 4.43,11.09 3.35,11.66 2.46,12.54   1.87,13.12 1.24,14.03 1.07,14.54   0.99,14.79 1.03,14.90 1.39,15.44   2.75,17.45 4.82,18.96 7.09,19.61   8.71,20.06 10.46,20.13 12.05,19.80   13.65,19.48 15.22,18.74 16.47,17.73   17.25,17.11 18.25,15.98 18.76,15.15   19.03,14.72 19.03,14.70 18.77,14.20   17.88,12.44 16.12,11.14 14.17,10.80   13.68,10.71 13.51,10.75 13.07,11.03   12.11,11.66 11.13,11.96 10.00,11.96   8.84,11.96 7.91,11.67 6.84,10.98   6.55,10.79 6.49,10.77 6.27,10.76   6.14,10.75 5.89,10.78 5.70,10.82 Z M 9.32,0.02 C 9.27,0.04 9.10,0.07 8.93,0.10   6.89,0.49 5.20,2.25 4.86,4.34   4.79,4.80 4.79,5.58 4.86,6.04   5.22,8.23 6.96,9.96 9.18,10.33   9.61,10.41 10.39,10.41 10.85,10.33   12.95,9.99 14.65,8.36 15.11,6.25   15.21,5.78 15.21,4.61 15.11,4.14   14.67,2.12 13.11,0.55 11.09,0.10   10.81,0.03 9.50,-0.02 9.32,0.02 Z");
        userIcon.setScaleX(0.6);
        userIcon.setScaleY(0.6);

        SVGPath lockIcon = new SVGPath();
        lockIcon.getStyleClass().add("fieldIcon");
        lockIcon.setLayoutX(4);
        lockIcon.setLayoutY(3);
        lockIcon.setContent("M 8.97,0.06 C 6.82,0.42 5.04,1.87 4.22,3.93   3.87,4.82 3.79,5.36 3.79,6.87   3.79,6.87 3.79,8.04 3.79,8.04   3.79,8.04 3.48,8.06 3.48,8.06   3.09,8.08 2.87,8.18 2.66,8.43   2.57,8.52 2.46,8.70 2.41,8.82   2.41,8.82 2.32,9.04 2.32,9.04   2.32,9.04 2.31,13.96 2.31,13.96   2.30,18.86 2.30,18.89 2.38,19.14   2.49,19.48 2.68,19.73 2.94,19.88   2.94,19.88 3.14,20.00 3.14,20.00   3.14,20.00 10.00,20.00 10.00,20.00   10.00,20.00 16.86,20.00 16.86,20.00   16.86,20.00 17.06,19.88 17.06,19.88   17.31,19.74 17.51,19.48 17.61,19.15   17.70,18.91 17.70,18.85 17.70,14.05   17.70,9.29 17.70,9.18 17.62,8.94   17.43,8.34 17.03,8.05 16.41,8.05   16.41,8.05 16.21,8.05 16.21,8.05   16.21,8.05 16.21,6.88 16.21,6.88   16.21,5.36 16.13,4.82 15.78,3.93   14.96,1.86 13.17,0.41 11.01,0.06   10.56,-0.02 9.40,-0.01 8.97,0.06 Z M 10.93,2.73 C 12.17,3.09 13.14,4.06 13.48,5.28   13.59,5.70 13.63,6.20 13.63,7.18   13.63,7.18 13.63,8.05 13.63,8.05   13.63,8.05 10.00,8.05 10.00,8.05   10.00,8.05 6.37,8.05 6.37,8.05   6.37,8.05 6.37,7.18 6.37,7.18   6.37,6.20 6.41,5.70 6.52,5.28   6.88,3.98 7.95,2.98 9.30,2.68   9.67,2.59 10.55,2.62 10.93,2.73 Z M 10.55,11.27 C 11.06,11.43 11.50,11.88 11.64,12.42   11.70,12.66 11.70,13.07 11.63,13.31   11.57,13.51 11.34,13.91 11.25,13.96   11.22,13.98 11.21,14.45 11.21,15.46   11.21,16.86 11.21,16.94 11.13,17.06   10.97,17.32 10.89,17.34 10.00,17.34   9.11,17.34 9.03,17.32 8.87,17.06   8.79,16.94 8.79,16.86 8.79,15.46   8.79,14.45 8.78,13.98 8.75,13.96   8.66,13.91 8.43,13.51 8.37,13.31   8.30,13.07 8.30,12.66 8.36,12.42   8.50,11.89 8.94,11.43 9.43,11.28   9.70,11.20 10.28,11.20 10.55,11.27 Z");
        lockIcon.setScaleX(0.6);
        lockIcon.setScaleY(0.6);

        // LOGIN
        login_usernamePane.getChildren().add(0, userIcon);
        login_passwordPane.getChildren().add(0, lockIcon);

        // REGISTER
//        register_usernamePane.getChildren().add(0, userIcon);
//        register_passwordPane1.getChildren().add(0, lockIcon);
//        register_passwordPane2.getChildren().add(0, lockIcon);

        // searches for "fieldIcon" style class and increase their z-index
        // this is equivalent to "querySelectorAll" in vanilla JavaScript
        for(Node icons: panels.lookupAll(".fieldIcon")) icons.toFront();

        // disable buttons when fields are empty in each forms
        loginButton.disableProperty().bind(
                Bindings.isEmpty(inputUserLogin.textProperty())
                        .or(Bindings.isEmpty(inputPasswordLogin.textProperty()))
        );

        registerButton.disableProperty().bind(
                Bindings.isEmpty(inputUserRegister.textProperty())
                        .or(Bindings.isEmpty(inputPassword1Register.textProperty()))
                        .or(Bindings.isEmpty(inputPassword2Register.textProperty()))
        );

        // programmatically "fire" (click) login button when clicking ENTER on user and password text fields
        // this also prevent if other field is empty
        inputUserLogin.setOnKeyPressed((event) -> {
            if((event.getCode() == KeyCode.ENTER) && (!(inputPasswordLogin.getText().equals("")))){
                loginButton.fire();
            }
        });

        inputPasswordLogin.setOnKeyPressed((event) -> {
            if((event.getCode() == KeyCode.ENTER) && (!(inputUserLogin.getText().equals("")))){
                loginButton.fire();
            }
        });

        forgotPasswordButton.setVisible(false);

//        formLogin.setVisible(false);
//        formLogin.setDisable(true);

        formLogin.setVisible(true);
        formLogin.setDisable(false);
        formRegister.setVisible(false);
        formRegister.setDisable(true);
    }

    @FXML
    public void onHoverMouseInput(MouseEvent mouseEvent){
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            Node node = (Node) mouseEvent.getSource();
            node.getParent().getStyleClass().add("inputMouseHovered");
        }
    }

    @FXML
    public void onExitedMouseInput(MouseEvent mouseEvent){
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            Node node = (Node) mouseEvent.getSource();
            node.getParent().getStyleClass().remove("inputMouseHovered");
        }
    }

    @FXML
    public void loginEvent(ActionEvent e) throws IOException {
        e.consume();

        String inputUserText = inputUserLogin.getText();
        String inputPassText = inputPasswordLogin.getText();
        Stage loginStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // login success
        if (inputUserText.equals("admin") && inputPassText.equals("pass")){
            System.out.println("LOGIN SUCCESS");
            loginStage.close();

            // move to MainFrame screen by creating new window
            FXMLLoader loader = new FXMLLoader(LoginFrame.class.getResource("mainFrame.fxml"));
            Parent root = loader.load();
            root.getStylesheets().add(LoginFrame.class.getResource("style.css").toExternalForm());
            Platform.runLater(root::requestFocus);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            MainFrame mainFrame = new MainFrame();
            mainFrame.setScene(scene);
            mainFrame.show();
        }

        // login fail
        else{
            // call shake window class if user enters wrong login info combination
            ShakeWindow anim = new ShakeWindow(outerElem, t->{});
            anim.playFromStart();

            // show forgot password button if user fails to log-in consecutively
            this.failedAttemptCounter++;
            if(failedAttemptCounter > 1){
                forgotPasswordButton.setVisible(true);
                loginFailedMessage.setText("You failed " + failedAttemptCounter + " times.");
            }

            formLogin.getStyleClass().add("form_error");
            loginFailed.setText("Username or password combination is invalid.");
            inputUserLogin.clear();
            inputPasswordLogin.clear();
        }
    }

    @FXML
    public void registerEvent(ActionEvent e) {
        e.consume();
        String inputUserRegText = inputUserRegister.getText();
        String inputPasswordRegText1 = inputPassword1Register.getText();
        String inputPasswordRegText2 = inputPassword2Register.getText();
        boolean hasFailed = false;


        // checks if username meets the minimum length requirement
        if (inputUserRegText.length() < Constants.USER_NAME_MIN_LENGTH){
            inputUserRegister.getParent().getStyleClass().add("inputError");
            hasFailed = true;
        }

        else{
            inputUserRegister.getParent().getStyleClass().remove("inputError");
        }

        // checks if both password fields' text match
        if(!(inputPasswordRegText1.equalsIgnoreCase(inputPasswordRegText2)) ||
                !(inputPasswordRegText2.equalsIgnoreCase(inputPasswordRegText1))){
            inputPassword1Register.getParent().getStyleClass().add("inputError");
            inputPassword2Register.getParent().getStyleClass().add("inputError");
            hasFailed = true;
        }
        else{
            inputPassword1Register.getParent().getStyleClass().remove("inputError");
            inputPassword2Register.getParent().getStyleClass().remove("inputError");
        }


        if (hasFailed){
            ShakeWindow anim = new ShakeWindow(outerElem, t->{});
            anim.playFromStart();
        }

    }
}