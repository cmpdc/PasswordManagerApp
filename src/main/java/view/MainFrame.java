package view;

import controller.PasswordsJSON;
import controller.PasswordsTXT;
import core.PasswordChecker;
import core.utils.Utils;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import core.utils.Constants;
import core.utils.TitleBar;
import core.PasswordGenerator;
import javafx.stage.StageStyle;
import core.Password;
import core.PasswordHolder;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MainFrame extends Stage implements Initializable {

    @FXML private Pane outerElem;
    @FXML private BorderPane innerElem;
    @FXML private Pane searchBar;
    @FXML private TextField searchField;
    @FXML private Button addContent;
    @FXML private Pane contentTitles;
    @FXML private TilePane titlesBody; // summaries of all passwords <<<< left side
    @FXML private Pane contentBody;
    @FXML private TilePane contentBodyContents; // display of each passwords <<<< right side
    @FXML private BorderPane innerElemShadow;

    private final PasswordsJSON passwordsFiles = new PasswordsJSON();
    // private final PasswordsJSON passwordsFiles = new PasswordsTXT();

    private final ContextMenu contextMenu = new ContextMenu();

    public MainFrame(){
        this.setWidth(Constants.APP_MAIN_SIZE[0]);
        this.setHeight(Constants.APP_MAIN_SIZE[1]);
        this.setTitle(Constants.APP_NAME);
        this.initStyle(StageStyle.TRANSPARENT);
        this.setResizable(false);
        this.centerOnScreen();
        this.getIcons().add(new Image(String.valueOf(MainFrame.class.getResource("logo.png"))));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // add toolbar
        innerElem.setTop(new TitleBar(
                this.getTitle(),
                ((Constants.APP_MAIN_SIZE[0] * -1) + 275),
                Constants.APP_MAIN_SIZE[0] - 20,
                Constants.APP_MAIN_SIZE[1])
        );

        innerElem.getTop().getStyleClass().add("mainFrame");

        Rectangle overflow = new Rectangle(Constants.APP_MAIN_SIZE[0] - 20, Constants.APP_MAIN_SIZE[1] - 20);
        overflow.setX(0);
        overflow.setY(0);
        overflow.setArcWidth(15.0); // adding arc (radius)
        overflow.setArcHeight(15.0);
        innerElem.setClip(overflow);

        innerElem.toFront();

        innerElemShadow.setDisable(false);
        innerElemShadow.setVisible(true);
        innerElemShadow.toBack();

        titlesBody.toBack();

        String magnifyingGlassContent = "M 10.34,2.96 C 8.74,3.21 7.38,4.22 6.72,5.64   6.52,6.08 6.54,6.35 6.79,6.63   6.96,6.82 7.22,6.91 7.46,6.87   7.76,6.82 7.93,6.68 8.11,6.32   8.53,5.49 9.12,4.96 9.96,4.64   10.61,4.40 11.48,4.41 12.16,4.66   12.80,4.90 13.34,4.60 13.34,3.99   13.34,3.76 13.28,3.60 13.12,3.44   12.97,3.28 12.39,3.08 11.76,2.97   11.45,2.91 10.65,2.91 10.34,2.96 Z M 9.60,0.07 C 8.02,0.31 6.65,1.01 5.53,2.12   4.13,3.54 3.40,5.27 3.40,7.22   3.40,8.45 3.65,9.47 4.22,10.60   4.36,10.86 4.46,11.10 4.46,11.13   4.46,11.17 3.57,12.09 2.49,13.18   1.40,14.28 0.45,15.24 0.39,15.34   0.09,15.78 -0.02,16.14 0.01,16.60   0.03,17.00 0.13,17.23 0.45,17.55   0.77,17.87 1.00,17.97 1.40,17.99   1.86,18.02 2.22,17.91 2.66,17.61   2.76,17.55 3.72,16.60 4.82,15.51   5.91,14.43 6.83,13.54 6.87,13.54   6.90,13.54 7.14,13.64 7.40,13.78   8.53,14.35 9.55,14.60 10.78,14.60   12.73,14.60 14.46,13.87 15.88,12.47   17.27,11.08 18.00,9.30 18.00,7.31   18.00,5.31 17.27,3.52 15.87,2.13   14.73,0.98 13.35,0.30 11.70,0.06   11.18,-0.02 10.14,-0.01 9.60,0.07 Z M 11.44,1.98 C 12.64,2.17 13.56,2.62 14.41,3.43   16.16,5.09 16.57,7.74 15.42,9.86   14.82,10.95 13.81,11.85 12.65,12.31   11.70,12.68 10.46,12.77 9.47,12.53   8.47,12.28 7.63,11.81 6.91,11.09   6.08,10.26 5.56,9.22 5.38,8.05   5.32,7.68 5.33,6.90 5.39,6.49   5.74,4.19 7.58,2.35 9.89,1.99   10.28,1.93 11.06,1.92 11.44,1.98 Z";
        String closeContent = "M 1.74,0.12 C 0.84,0.23 0.15,0.96 0.09,1.88   0.06,2.24 0.12,2.54 0.29,2.88   0.41,3.12 0.73,3.45 3.85,6.56   3.85,6.56 7.27,9.98 7.27,9.98   7.27,9.98 3.81,13.44 3.81,13.44   0.20,17.06 0.20,17.05 0.06,17.54   -0.02,17.83 -0.02,18.37 0.07,18.67   0.26,19.26 0.69,19.67 1.28,19.82   1.56,19.90 2.22,19.90 2.48,19.83   2.98,19.70 2.93,19.74 6.56,16.11   6.56,16.11 9.98,12.70 9.98,12.70   9.98,12.70 13.42,16.13 13.42,16.13   17.05,19.75 16.96,19.68 17.46,19.82   17.77,19.91 18.40,19.91 18.70,19.82   19.49,19.60 20.00,18.91 20.00,18.05   20.00,17.63 19.90,17.29 19.70,16.98   19.62,16.87 18.02,15.25 16.14,13.37   16.14,13.37 12.71,9.97 12.71,9.97   12.71,9.97 16.14,6.54 16.14,6.54   19.38,3.29 19.56,3.09 19.67,2.85   19.99,2.11 19.84,1.25 19.28,0.70   18.85,0.28 18.47,0.14 17.84,0.16   17.36,0.19 17.10,0.28 16.77,0.53   16.64,0.62 15.07,2.18 13.27,3.98   13.27,3.98 9.98,7.26 9.98,7.26   9.98,7.26 6.57,3.87 6.57,3.87   4.15,1.46 3.11,0.44 2.96,0.36   2.60,0.15 2.17,0.07 1.74,0.12 Z";

        SVGPath searchIcon = new SVGPath();
        searchIcon.setId("searchIcon");
        searchIcon.setContent(magnifyingGlassContent);
        searchIcon.setScaleX(0.8);
        searchIcon.setScaleY(0.8);
        searchBar.getChildren().add(1, searchIcon);

        // filtering/searching
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            if(!(Objects.equals(newValue, ""))){
                // change "magnifying glass" icon to "x" icon
                // and add mouse click listener to empty search field

                searchIcon.setContent(closeContent);
                searchIcon.setScaleX(0.5);
                searchIcon.setScaleY(0.5);
                searchIcon.setStyle("-fx-cursor: HAND;");

                searchIcon.setOnMouseClicked(event -> {
                    MouseButton mouseButton = event.getButton();
                    if (mouseButton == MouseButton.PRIMARY) searchField.clear();
                });
            }

            else{
                searchIcon.setContent(magnifyingGlassContent);
                searchIcon.setScaleX(0.8);
                searchIcon.setScaleY(0.8);
            }

            if(((filterList(passwordsFiles.getAll().stream().toList(), newValue)).isEmpty()) && (!(Objects.equals(newValue, "")))){

                titlesBody.getChildren().clear();
                contextMenu.getItems().clear();

                Label noSearchFound = new Label("No results found");
                noSearchFound.getStyleClass().add("emptyResults");
                noSearchFound.setLayoutX(200);
                noSearchFound.setLayoutY(300);

                titlesBody.setTileAlignment(Pos.CENTER);
                titlesBody.setAlignment(Pos.CENTER);
                titlesBody.getChildren().add(noSearchFound);

            }
            else{

                titlesBody.setTileAlignment(Pos.TOP_LEFT);
                titlesBody.setAlignment(Pos.TOP_LEFT);

                this.displaySummary(filterList(passwordsFiles.getAll().stream().toList(), newValue));
            }
        });

        SVGPath addContentIcon = new SVGPath();
        addContentIcon.setContent("M 9.14,0.04 C 8.79,0.14 8.43,0.47 8.27,0.81   8.27,0.81 8.18,1.00 8.18,1.00   8.18,1.00 8.17,4.58 8.17,4.58   8.17,4.58 8.16,8.16 8.16,8.16   8.16,8.16 4.58,8.17 4.58,8.17   4.58,8.17 1.00,8.18 1.00,8.18   1.00,8.18 0.78,8.29 0.78,8.29   0.52,8.42 0.26,8.67 0.12,8.93   0.02,9.12 0.02,9.12 0.02,10.00   0.02,10.88 0.02,10.88 0.12,11.07   0.25,11.32 0.56,11.61 0.81,11.73   0.81,11.73 1.00,11.82 1.00,11.82   1.00,11.82 4.58,11.83 4.58,11.83   4.58,11.83 8.16,11.84 8.16,11.84   8.16,11.84 8.17,15.42 8.17,15.42   8.17,15.42 8.18,19.00 8.18,19.00   8.18,19.00 8.27,19.19 8.27,19.19   8.38,19.43 8.68,19.75 8.93,19.88   9.12,19.98 9.12,19.98 10.00,19.98   10.88,19.98 10.88,19.98 11.07,19.88   11.32,19.75 11.61,19.44 11.73,19.19   11.73,19.19 11.82,19.00 11.82,19.00   11.82,19.00 11.83,15.42 11.83,15.42   11.83,15.42 11.84,11.84 11.84,11.84   11.84,11.84 15.42,11.83 15.42,11.83   15.42,11.83 19.00,11.82 19.00,11.82   19.00,11.82 19.19,11.73 19.19,11.73   19.44,11.61 19.75,11.32 19.88,11.07   19.98,10.88 19.98,10.88 19.98,10.00   19.98,9.12 19.98,9.12 19.88,8.93   19.74,8.67 19.48,8.42 19.22,8.29   19.22,8.29 19.00,8.18 19.00,8.18   19.00,8.18 15.42,8.17 15.42,8.17   15.42,8.17 11.84,8.16 11.84,8.16   11.84,8.16 11.83,4.58 11.83,4.58   11.83,4.58 11.82,1.00 11.82,1.00   11.82,1.00 11.71,0.78 11.71,0.78   11.58,0.52 11.33,0.26 11.07,0.12   10.88,0.02 10.86,0.02 10.06,0.01   9.61,0.01 9.20,0.02 9.14,0.04 Z");
        addContentIcon.setScaleX(0.8);
        addContentIcon.setScaleY(0.8);
        addContent.setGraphic(addContentIcon);

        passwordsFiles.read();
        this.displaySummary(passwordsFiles.getAll());
    }

    private void displaySummary(ArrayList<Password> passwords){

        // clear what was already added
        // this is to prevent duplicates
        titlesBody.getChildren().clear();

        // sorts password list in descending order
        passwords.sort(Comparator.comparing(Password::getDateAndTimeRaw).reversed());

        // append all passwords to "titlesBody"
        for(Password eachPassword: passwords){

            MenuItem menuView = new MenuItem("View");
            MenuItem menuDelete = new MenuItem("Delete");

            contextMenu.getItems().clear(); // clear
            contextMenu.getItems().addAll(menuView, menuDelete);

            Pane pane = new Pane();
            pane.getStyleClass().add("eachPassword");
            pane.setPrefSize(330, 80);

            Label passwordTitleLabel = new Label(eachPassword.getTitle());
            passwordTitleLabel.setPrefSize(300, 25);
            passwordTitleLabel.setWrapText(true);
            passwordTitleLabel.getStyleClass().add("passwordTitleLabel");
            passwordTitleLabel.setLayoutX(10);
            passwordTitleLabel.setLayoutY(10);
            passwordTitleLabel.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 18));

            Label passwordDateAndTimeLabel = new Label(Utils.getFormattedDateTime(eachPassword.getDateAndTimeRaw()));
            passwordDateAndTimeLabel.getStyleClass().add("passwordDateAndTimeLabel");
            passwordDateAndTimeLabel.setLayoutX(10);
            passwordDateAndTimeLabel.setFont(Font.font("", 12));
            passwordDateAndTimeLabel.setLayoutY(37);

            pane.getChildren().addAll(
                    passwordTitleLabel,
                    passwordDateAndTimeLabel
            );

            pane.setOnContextMenuRequested(e -> {
                contextMenu.show(pane, e.getScreenX(), e.getScreenY());
            });

            pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                // this is to verify and detect which mouse button is being clicked
                // -- the purpose is to separate calling functions between clicking and right clicking
                // ---- since context menu is implemented

                @Override
                public void handle(MouseEvent mouseEvent) {

                    MouseButton mouseButton = mouseEvent.getButton();
                    if(mouseButton == MouseButton.PRIMARY){
                        displayContents(eachPassword);
                    }
                }
            });

            menuView.setOnAction(e -> {
                this.displayContents(eachPassword);
            });

            menuDelete.setOnAction(e -> {
                this.deletePassword(eachPassword);
            });

            titlesBody.getChildren().add(pane);
        }
    }

    private void displayContents(Password password){

        // emptying the children beforehand
        // --- to prevent overlapping what is already being shown
        contentBodyContents.getChildren().clear();

        Pane paneFirst = new Pane();
        paneFirst.getStyleClass().add("passwordContent");

        Label iconLabel = new Label();
        iconLabel.setPrefSize(100, 90);
        SVGPath keyIcon = new SVGPath();
        keyIcon.setId("keyIcon");
        keyIcon.setContent("M 35.00,12.52 C 35.00,12.57 35.38,13.16 35.84,13.84   39.45,19.15 39.94,25.51 37.20,31.40   36.95,31.92 36.75,32.42 36.75,32.50   36.75,32.67 39.06,36.74 39.29,36.96   39.53,37.20 54.60,45.88 54.78,45.88   54.86,45.86 56.34,45.49 58.04,45.02   58.04,45.02 61.15,44.19 61.15,44.19   61.15,44.19 62.58,41.70 62.58,41.70   63.38,40.34 63.99,39.16 63.96,39.08   63.93,39.00 62.96,38.39 61.80,37.72   61.80,37.72 59.69,36.51 59.69,36.51   59.69,36.51 57.70,36.77 57.70,36.77   56.61,36.91 55.68,36.97 55.62,36.91   55.56,36.86 55.19,36.01 54.78,35.02   54.78,35.02 54.04,33.24 54.04,33.24   54.04,33.24 52.45,32.33 52.45,32.33   52.45,32.33 50.86,31.42 50.86,31.42   50.86,31.42 48.83,31.67 48.83,31.67   48.83,31.67 46.79,31.94 46.79,31.94   46.79,31.94 46.01,30.05 46.01,30.05   46.01,30.05 45.23,28.16 45.23,28.16   45.23,28.16 42.56,26.61 42.56,26.61   42.56,26.61 39.89,25.07 39.89,25.07   39.89,25.07 39.96,23.85 39.96,23.85   40.14,21.05 39.31,18.04 37.70,15.51   37.18,14.70 35.01,12.30 35.00,12.52 Z M 20.26,0.14 C 18.01,0.57 15.69,1.99 14.25,3.79   13.18,5.14 12.46,6.84 12.06,9.00   12.01,9.29 11.91,9.34 11.04,9.51   6.01,10.56 1.81,14.51 0.44,19.50   0.04,20.94 -0.12,23.84 0.11,25.38   0.79,29.79 3.68,33.74 7.66,35.70   7.66,35.70 8.62,36.17 8.62,36.17   8.62,36.17 8.62,47.81 8.62,47.81   8.62,47.81 8.62,59.44 8.62,59.44   8.62,59.44 10.91,61.73 10.91,61.73   10.91,61.73 13.19,64.00 13.19,64.00   13.19,64.00 16.23,64.00 16.23,64.00   16.23,64.00 19.25,64.00 19.25,64.00   19.25,64.00 19.25,61.49 19.25,61.49   19.25,61.49 19.25,58.98 19.25,58.98   19.25,58.98 18.06,57.44 18.06,57.44   17.41,56.59 16.88,55.84 16.88,55.76   16.88,55.70 17.41,54.94 18.06,54.08   18.06,54.08 19.25,52.51 19.25,52.51   19.25,52.51 19.25,50.67 19.25,50.67   19.25,50.67 19.25,48.84 19.25,48.84   19.25,48.84 18.03,47.20 18.03,47.20   18.03,47.20 16.79,45.55 16.79,45.55   16.79,45.55 18.03,43.95 18.03,43.95   18.03,43.95 19.25,42.35 19.25,42.35   19.25,42.35 19.25,39.26 19.25,39.26   19.25,39.26 19.25,36.17 19.25,36.17   19.25,36.17 19.98,35.85 19.98,35.85   23.84,34.06 26.86,30.15 27.69,25.82   27.91,24.65 27.95,22.17 27.76,21.00   26.85,15.40 22.68,10.91 17.20,9.60   17.20,9.60 15.99,9.31 15.99,9.31   15.99,9.31 16.07,8.81 16.07,8.81   16.20,8.07 16.64,7.16 17.29,6.31   18.07,5.26 19.24,4.46 20.61,4.05   21.36,3.82 23.16,3.82 23.95,4.06   26.11,4.70 27.86,6.52 28.44,8.71   28.62,9.44 28.70,11.80 28.54,11.72   28.48,11.70 28.05,11.50 27.59,11.27   27.11,11.04 26.70,10.89 26.65,10.92   26.61,10.97 26.80,11.24 27.08,11.54   27.78,12.27 28.26,12.91 28.96,14.06   30.69,16.89 31.51,19.86 31.51,23.20   31.51,28.91 28.89,34.06 24.23,37.47   23.83,37.77 23.50,38.05 23.50,38.10   23.50,38.15 26.05,42.60 29.16,48.00   29.16,48.00 34.83,57.80 34.83,57.80   34.83,57.80 38.04,58.66 38.04,58.66   38.04,58.66 41.26,59.52 41.26,59.52   41.26,59.52 43.81,58.06 43.81,58.06   45.23,57.25 46.38,56.58 46.38,56.55   46.38,56.52 45.81,55.52 45.12,54.33   45.12,54.33 43.86,52.15 43.86,52.15   43.86,52.15 42.00,51.38 42.00,51.38   39.83,50.46 40.03,50.83 40.36,48.21   40.36,48.21 40.58,46.49 40.58,46.49   40.58,46.49 39.69,44.91 39.69,44.91   39.69,44.91 38.79,43.34 38.79,43.34   38.79,43.34 36.96,42.58 36.96,42.58   35.96,42.15 35.10,41.77 35.06,41.72   35.01,41.69 35.10,40.75 35.24,39.66   35.24,39.66 35.50,37.67 35.50,37.67   35.50,37.67 34.00,35.08 34.00,35.08   33.18,33.65 32.50,32.44 32.50,32.36   32.50,32.30 32.69,31.99 32.93,31.65   33.16,31.32 33.61,30.52 33.93,29.88   36.04,25.46 35.75,20.36 33.16,16.31   33.16,16.31 32.52,15.31 32.52,15.31   32.52,15.31 32.46,11.81 32.46,11.81   32.40,7.96 32.35,7.62 31.55,5.90   30.23,3.02 27.61,0.90 24.53,0.20   23.56,-0.01 21.23,-0.05 20.26,0.14 Z M 15.58,16.85 C 16.20,17.17 16.90,17.91 17.28,18.62   17.68,19.40 17.68,20.67 17.26,21.60   16.90,22.41 15.96,23.27 15.16,23.55   12.78,24.36 10.33,22.62 10.33,20.12   10.31,17.42 13.20,15.62 15.58,16.85 Z");
        keyIcon.setScaleX(1);
        keyIcon.setScaleY(1);
        iconLabel.setGraphic(keyIcon);
        iconLabel.setAlignment(Pos.CENTER);
        iconLabel.setContentDisplay(ContentDisplay.CENTER);
        iconLabel.setLayoutX(67);
        iconLabel.setLayoutY(38);

        Label passwordTitleLabel = new Label(password.getTitle());
        passwordTitleLabel.setWrapText(true);
        passwordTitleLabel.setAlignment(Pos.CENTER_LEFT);
        passwordTitleLabel.setLayoutX(175);
        passwordTitleLabel.setLayoutY(40);
        passwordTitleLabel.setPrefSize(245, 40);
        passwordTitleLabel.setFont(Font.font("System", 30));
        passwordTitleLabel.setId("passwordTitle");

        Label dateAndTimeLabel = new Label(Utils.getFormattedDateTime(password.getDateAndTimeRaw()));
        dateAndTimeLabel.setAlignment(Pos.CENTER_LEFT);
        dateAndTimeLabel.setLayoutX(175);
        dateAndTimeLabel.setLayoutY(85);
        dateAndTimeLabel.setPrefSize(300, 35);
        dateAndTimeLabel.setFont(Font.font(16));

        Label passwordFieldPlaceholder = new Label("Password");
        passwordFieldPlaceholder.setAlignment(Pos.CENTER_RIGHT);
        passwordFieldPlaceholder.setLayoutX(20);
        passwordFieldPlaceholder.setLayoutY(150);
        passwordFieldPlaceholder.setPrefSize(140, 25);
        passwordFieldPlaceholder.setFont(Font.font(18));

        PasswordGenerator passwordGenerator = new PasswordGenerator();
        String decodedPassword = passwordGenerator.decryptPassword(
                password.getEncodedPassword(),
                Constants.ADMIN_MASTER_KEY,
                password.getGeneratedSalt()
        );

        TextField passwordFieldText = new TextField(decodedPassword);
        passwordFieldText.getStyleClass().add("passwordFieldText");
        passwordFieldText.setEditable(false);
        passwordFieldText.setLayoutX(170);
        passwordFieldText.setLayoutY(148);
        passwordFieldText.setPrefSize(220, 25);
        passwordFieldText.setFont(Font.font(18));
        passwordFieldText.setVisible(false);
        passwordFieldText.setDisable(true);

        TextField passwordFieldMask = new TextField(Utils.stringMultiplier((passwordFieldText.getText()).length(), "*"));
        passwordFieldMask.getStyleClass().add("passwordFieldText");
        passwordFieldMask.setEditable(false);
        passwordFieldMask.setLayoutX(172);
        passwordFieldMask.setLayoutY(152); // original 150
        passwordFieldMask.setPrefSize(220, 25);
        passwordFieldMask.setFont(Font.font(18));
        passwordFieldMask.setVisible(true);
        passwordFieldMask.setDisable(false);

        Label copyPasswordButton = new Label();
        copyPasswordButton.getStyleClass().add("passwordButtons");
        copyPasswordButton.setPrefSize(25, 25);
        copyPasswordButton.setLayoutX(425);
        copyPasswordButton.setLayoutY(155);
        SVGPath copyPasswordIcon = new SVGPath();
        copyPasswordIcon.setContent("M 2.73,0.73 C 2.46,0.82 2.29,0.97 2.14,1.26   2.14,1.26 2.07,1.39 2.07,1.39   2.07,1.39 2.07,7.84 2.07,7.84   2.07,7.84 2.07,14.28 2.07,14.28   2.07,14.28 2.14,14.43 2.14,14.43   2.23,14.62 2.43,14.82 2.62,14.91   2.62,14.91 2.77,14.99 2.77,14.99   2.77,14.99 7.46,14.99 7.46,14.99   12.15,15.00 12.16,15.00 12.32,14.94   12.54,14.85 12.77,14.64 12.88,14.41   12.88,14.41 12.96,14.22 12.96,14.22   12.96,14.22 12.96,7.84 12.96,7.84   12.96,7.84 12.96,1.45 12.96,1.45   12.96,1.45 12.87,1.25 12.87,1.25   12.76,1.04 12.59,0.87 12.37,0.76   12.24,0.69 12.20,0.69 11.57,0.68   11.57,0.68 10.90,0.67 10.90,0.67   10.90,0.67 10.89,1.31 10.89,1.31   10.88,1.93 10.88,1.95 10.80,2.12   10.66,2.42 10.39,2.63 10.05,2.70   9.83,2.74 5.19,2.74 4.97,2.69   4.62,2.63 4.34,2.41 4.20,2.10   4.12,1.92 4.12,1.91 4.11,1.29   4.11,1.29 4.10,0.67 4.10,0.67   4.10,0.67 3.49,0.67 3.49,0.67   2.99,0.68 2.86,0.69 2.73,0.73 Z M 10.67,4.82 C 10.80,4.87 10.87,4.96 10.89,5.09   10.90,5.19 10.89,5.22 10.80,5.32   10.80,5.32 10.70,5.43 10.70,5.43   10.70,5.43 7.56,5.44 7.56,5.44   4.14,5.45 4.28,5.46 4.17,5.29   4.06,5.12 4.14,4.88 4.34,4.81   4.49,4.76 10.54,4.76 10.67,4.82 Z M 10.71,6.87 C 11.01,7.02 10.92,7.44 10.57,7.49   10.50,7.49 9.07,7.50 7.40,7.49   4.50,7.49 4.34,7.48 4.27,7.43   4.22,7.40 4.17,7.33 4.14,7.27   4.10,7.18 4.10,7.15 4.14,7.06   4.16,7.00 4.22,6.93 4.27,6.89   4.27,6.89 4.37,6.83 4.37,6.83   4.37,6.83 7.49,6.83 7.49,6.83   10.25,6.83 10.62,6.83 10.71,6.87 Z M 10.72,8.92 C 10.82,8.97 10.90,9.10 10.90,9.20   10.90,9.28 10.82,9.42 10.75,9.47   10.69,9.52 10.34,9.52 7.52,9.52   4.03,9.52 4.24,9.53 4.15,9.33   4.11,9.25 4.10,9.21 4.13,9.13   4.16,9.03 4.25,8.93 4.34,8.90   4.36,8.89 5.79,8.88 7.51,8.88   10.25,8.88 10.65,8.88 10.72,8.92 Z M 10.72,10.97 C 10.82,11.02 10.90,11.15 10.90,11.25   10.90,11.29 10.88,11.35 10.85,11.40   10.76,11.58 10.95,11.57 7.50,11.57   4.00,11.57 4.25,11.59 4.14,11.37   4.07,11.22 4.12,11.09 4.28,10.98   4.34,10.93 4.58,10.93 7.49,10.93   10.25,10.93 10.65,10.93 10.72,10.97 Z M 4.88,0.10 C 4.88,0.10 4.78,0.20 4.78,0.20   4.78,0.20 4.78,1.03 4.78,1.03   4.78,1.03 4.78,1.85 4.78,1.85   4.78,1.85 4.88,1.95 4.88,1.95   4.88,1.95 4.97,2.05 4.97,2.05   4.97,2.05 7.48,2.05 7.48,2.05   10.25,2.05 10.11,2.06 10.21,1.86   10.27,1.73 10.28,0.35 10.21,0.19   10.13,-0.01 10.31,0.00 7.51,0.00   7.51,0.00 4.97,0.00 4.97,0.00   4.97,0.00 4.88,0.10 4.88,0.10 Z");
        copyPasswordIcon.setId("copyPasswordIcon");
        copyPasswordIcon.setScaleX(1.5);
        copyPasswordIcon.setScaleY(1.5);
        copyPasswordButton.setGraphic(copyPasswordIcon);

        copyPasswordButton.setOnMouseClicked(event -> {
            // this is to "temporarily" add a class to copyPasswordButton
            // equivalent to javascript's setTimeout

            MouseButton mouseButton = event.getButton();
            if(mouseButton == MouseButton.PRIMARY){
                passwordFieldText.getStyleClass().add("passwordCopied");

                // copies text from field
                StringSelection copiedGeneratedPassword = new StringSelection(passwordFieldText.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(copiedGeneratedPassword, null);

                CompletableFuture<Void> timeOut = CompletableFuture.runAsync(() -> {});

                try{
                    timeOut.get(1, TimeUnit.DAYS);
                    passwordFieldText.getStyleClass().remove("passwordCopied");
                }
                catch(Exception error){
                    System.out.println(error.toString());
                }
            }
        });

        String eyeIconContent = "M 8.81,4.54 C 5.85,4.85 3.04,6.30 1.12,8.50   0.70,8.99 0.00,9.92 0.00,10.00   0.00,10.08 0.70,11.01 1.12,11.50   3.06,13.72 5.86,15.16 8.86,15.46   9.38,15.52 10.62,15.52 11.14,15.46   14.14,15.16 16.94,13.72 18.88,11.50   19.30,11.01 20.00,10.08 20.00,10.00   20.00,9.92 19.30,8.99 18.88,8.50   16.94,6.28 14.14,4.84 11.14,4.54   10.68,4.48 9.27,4.49 8.81,4.54 Z M 6.27,7.69 C 6.16,8.00 6.05,8.62 6.05,8.95   6.05,9.06 6.08,9.33 6.11,9.53   6.39,11.32 7.85,12.70 9.63,12.84   11.82,13.02 13.66,11.53 13.91,9.36   13.98,8.76 13.88,7.96 13.65,7.45   13.62,7.36 13.64,7.37 14.16,7.62   15.16,8.08 16.33,8.95 17.01,9.70   17.01,9.70 17.27,10.00 17.27,10.00   17.27,10.00 17.07,10.23 17.07,10.23   16.31,11.11 14.96,12.06 13.73,12.58   12.52,13.09 11.34,13.33 10.00,13.33   8.66,13.33 7.48,13.09 6.27,12.58   5.04,12.06 3.69,11.11 2.93,10.23   2.93,10.23 2.73,10.00 2.73,10.00   2.73,10.00 2.99,9.70 2.99,9.70   3.50,9.13 4.39,8.43 5.20,7.95   5.54,7.75 6.33,7.37 6.35,7.39   6.36,7.40 6.32,7.53 6.27,7.69 Z M 12.34,7.59 C 12.41,7.63 12.52,7.72 12.60,7.80   13.25,8.45 12.79,9.57 11.88,9.57   10.96,9.58 10.47,8.48 11.11,7.82   11.42,7.49 11.95,7.39 12.34,7.59 Z";
        String eyeIconSlashedContent = "M 11.64,10.99 C 9.07,13.57 9.24,13.30 10.17,13.27   10.61,13.25 10.79,13.23 11.07,13.15   12.59,12.71 13.70,11.43 13.90,9.87   13.95,9.53 13.95,9.17 13.91,8.91   13.91,8.91 13.89,8.75 13.89,8.75   13.89,8.75 11.64,10.99 11.64,10.99 Z M 15.24,7.39 C 15.24,7.39 14.45,8.18 14.45,8.18   14.45,8.18 14.69,8.31 14.69,8.31   15.06,8.52 15.61,8.89 16.01,9.21   16.42,9.53 17.27,10.36 17.27,10.43   17.27,10.52 16.29,11.44 15.82,11.79   14.42,12.84 12.91,13.46 11.19,13.69   10.70,13.76 9.57,13.78 9.16,13.73   9.16,13.73 8.93,13.71 8.93,13.71   8.93,13.71 7.99,14.64 7.99,14.64   7.99,14.64 7.06,15.58 7.06,15.58   7.06,15.58 7.43,15.66 7.43,15.66   8.93,15.99 10.46,16.03 11.97,15.78   14.86,15.30 17.56,13.68 19.35,11.36   19.75,10.84 20.00,10.47 20.00,10.43   20.00,10.36 19.43,9.59 19.02,9.10   18.62,8.62 17.94,7.95 17.41,7.53   17.04,7.23 16.13,6.60 16.07,6.60   16.05,6.60 15.68,6.96 15.24,7.39 Z M 8.98,4.95 C 7.79,5.08 7.06,5.24 6.07,5.59   4.17,6.26 2.38,7.47 1.10,8.96   0.71,9.41 0.00,10.36 0.00,10.42   0.00,10.48 0.41,11.06 0.80,11.54   1.51,12.42 2.22,13.09 3.27,13.84   3.27,13.84 3.46,13.98 3.46,13.98   3.46,13.98 4.25,13.19 4.25,13.19   4.25,13.19 5.05,12.39 5.05,12.39   5.05,12.39 4.68,12.14 4.68,12.14   4.48,12.01 4.15,11.77 3.96,11.61   3.57,11.31 2.72,10.48 2.74,10.43   2.78,10.33 3.31,9.79 3.68,9.48   4.17,9.05 4.58,8.75 5.08,8.45   5.43,8.24 6.32,7.80 6.35,7.82   6.36,7.83 6.31,7.98 6.25,8.18   6.01,8.91 6.01,9.81 6.25,10.55   6.32,10.75 6.38,10.93 6.40,10.96   6.42,11.00 6.60,10.84 7.07,10.38   7.07,10.38 7.71,9.73 7.71,9.73   7.71,9.73 7.59,9.57 7.59,9.57   7.43,9.33 7.37,9.13 7.39,8.89   7.44,8.35 7.89,7.92 8.44,7.92   8.68,7.92 8.98,8.03 9.12,8.16   9.20,8.24 9.21,8.23 10.74,6.70   11.59,5.85 12.27,5.15 12.26,5.14   12.25,5.13 12.01,5.08 11.73,5.04   11.33,4.97 11.02,4.95 10.18,4.94   9.60,4.93 9.06,4.94 8.98,4.95 Z M 17.30,1.21 C 17.25,1.23 17.14,1.27 17.05,1.32   16.81,1.44 1.44,16.84 1.32,17.07   1.20,17.33 1.18,17.76 1.29,18.06   1.40,18.36 1.68,18.64 1.98,18.75   2.28,18.86 2.71,18.84 2.97,18.71   3.23,18.58 18.60,3.22 18.74,2.95   18.89,2.67 18.91,2.25 18.79,1.94   18.68,1.64 18.40,1.36 18.11,1.25   17.88,1.17 17.50,1.15 17.30,1.21 Z";

        Label revealPasswordButton = new Label();
        revealPasswordButton.getStyleClass().add("passwordButtons");
        revealPasswordButton.setPrefSize(25, 25);
        revealPasswordButton.setLayoutX(460);
        revealPasswordButton.setLayoutY(155);
        SVGPath revealPasswordIcon = new SVGPath();
        revealPasswordIcon.setId("revealPasswordIcon");
        revealPasswordIcon.setContent(eyeIconContent);
        revealPasswordIcon.setScaleX(1.3);
        revealPasswordIcon.setScaleY(1.3);
        revealPasswordButton.setGraphic(revealPasswordIcon);

        revealPasswordButton.setOnMouseClicked(event -> {

            MouseButton mouseButton = event.getButton();
            if(mouseButton == MouseButton.PRIMARY){
                if (revealPasswordIcon.getContent().equals(eyeIconContent)){
                    revealPasswordIcon.setContent(eyeIconSlashedContent);
                    revealPasswordButton.getStyleClass().add("revealed");

                    passwordFieldText.setVisible(true);
                    passwordFieldText.setDisable(false);

                    passwordFieldMask.setVisible(false);
                    passwordFieldMask.setDisable(true);

                    passwordFieldText.getStyleClass().add("passwordRevealed");

                }
                else{
                    revealPasswordIcon.setContent(eyeIconContent);
                    revealPasswordButton.getStyleClass().remove("revealed");

                    passwordFieldText.setVisible(false);
                    passwordFieldText.setDisable(true);

                    passwordFieldMask.setVisible(true);
                    passwordFieldMask.setDisable(false);

                    passwordFieldText.getStyleClass().remove("passwordRevealed");
                }
            }
        });

        Label passwordStrengthPlaceholder = new Label("Strength");
        passwordStrengthPlaceholder.setAlignment(Pos.CENTER_RIGHT);
        passwordStrengthPlaceholder.setLayoutX(20);
        passwordStrengthPlaceholder.setLayoutY(190);
        passwordStrengthPlaceholder.setPrefSize(140, 25);
        passwordStrengthPlaceholder.setFont(Font.font(18));

        Label passwordStrengthField = new Label(new PasswordChecker(decodedPassword).getPasswordStrengthScoreAsString());
        passwordStrengthField.setLayoutX(175);
        passwordStrengthField.setLayoutY(190);
        passwordStrengthField.setPrefSize(140, 25);
        passwordStrengthField.setTextAlignment(TextAlignment.RIGHT);
        passwordStrengthField.setFont(Font.font(18));

        Label notesFieldPlaceholder = new Label("Notes");
        notesFieldPlaceholder.setAlignment(Pos.CENTER_RIGHT);
        notesFieldPlaceholder.setLayoutX(20);
        notesFieldPlaceholder.setLayoutY(230);
        notesFieldPlaceholder.setPrefSize(140, 25);
        notesFieldPlaceholder.setTextAlignment(TextAlignment.RIGHT);
        notesFieldPlaceholder.setFont(Font.font(18));

        ScrollPane notesScrollPane = new ScrollPane();
        notesScrollPane.getStyleClass().add("notesScrollPane");
        notesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        notesScrollPane.setLayoutX(170); // original: 175; -5 because of padding
        notesScrollPane.setLayoutY(225); // original 230; -5 because of padding
        notesScrollPane.setPrefSize(300, 200);
        notesScrollPane.setPrefViewportHeight(200);
        notesScrollPane.setPadding(new Insets(5, 5, 5, 5));

        Label notesFieldText = new Label(password.getNotes());
        notesFieldText.setAlignment(Pos.TOP_LEFT);
        notesFieldText.setFont(Font.font(18));
        notesScrollPane.setContent(notesFieldText);

        paneFirst.getChildren().addAll(
                iconLabel,
                passwordTitleLabel,
                dateAndTimeLabel,
                passwordFieldPlaceholder,
                passwordFieldMask,
                passwordFieldText,
                copyPasswordButton,
                revealPasswordButton,
                passwordStrengthPlaceholder,
                passwordStrengthField,
                notesFieldPlaceholder,
                notesScrollPane
        );

        Pane paneSecond = new Pane();
        paneSecond.setPrefHeight(50);
        paneSecond.getStyleClass().add("passwordContent");

        VBox formsBottomOuter = new VBox();
        formsBottomOuter.setLayoutX(170);
        formsBottomOuter.setPrefSize(200, 80);
        formsBottomOuter.setAlignment(Pos.CENTER);
        formsBottomOuter.getStyleClass().add("formButtons");

        HBox formsBottomInner = new HBox(15);
        formsBottomInner.setAlignment(Pos.CENTER);

        Button editButton = new Button("EDIT");
        editButton.setWrapText(false);

        editButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            MouseButton mouseButton = event.getButton();
            if(mouseButton == MouseButton.PRIMARY){

                // creates a new window for updating
                try{
                    // pass current password to another controller to edit
                    PasswordHolder.getInstance().setPassword(password);

                    EditPasswordFrame editPasswordFrame = new EditPasswordFrame();

                    FXMLLoader loader = new FXMLLoader(MainFrame.class.getResource("editPasswordFrame.fxml"));

                    Parent editPasswordFrameRoot = loader.load();
                    editPasswordFrameRoot.getStylesheets().add(MainFrame.class.getResource("style.css").toExternalForm());
                    Platform.runLater(editPasswordFrameRoot::requestFocus);

                    EditPasswordFrame editFrameController = loader.getController();

                    Scene editPasswordScene = new Scene(editPasswordFrameRoot);
                    editPasswordScene.setFill(Color.TRANSPARENT);

                    editPasswordFrame.setScene(editPasswordScene);
                    editPasswordFrame.initOwner(((Node) event.getSource()).getScene().getWindow());

                    editPasswordFrame.getIcons().add(new Image(String.valueOf(MainFrame.class.getResource("logo.png"))));
                    editPasswordFrame.showAndWait();

                    // the following condition validates if the user closes the frame by closing the window
                    // or by clicking the save button
                    // -- this also prevent any future error...
                    if((editFrameController.isSaveButtonPressed())){
                        // updates password
                        PasswordHolder.getInstance().setPassword(editFrameController.getEditedPassword());

                        // passes both old and new password to edit
                        this.editPassword(password, PasswordHolder.getInstance().getPassword());
                    }

                }

                catch(Exception error){
                    System.out.println(error.toString());
                }
            }
        });

        Button deleteButton = new Button("DELETE");
        deleteButton.getStyleClass().add("deleteButton");
        deleteButton.setWrapText(false);
        deleteButton.setOnMouseClicked(event -> {

            MouseButton mouseButton = event.getButton();
            if(mouseButton == MouseButton.PRIMARY){
                this.deletePassword(password);
            }
        });

        formsBottomInner.getChildren().addAll(editButton, deleteButton);
        formsBottomOuter.getChildren().add(formsBottomInner);

        paneSecond.getChildren().add(formsBottomOuter);

        contentBodyContents.getChildren().addAll(paneFirst, paneSecond);

    }

    private boolean searchFindsOrder(Password password, String searchText){
        // return a boolean through searching password's title or password's notes

        return (password.getTitle().toLowerCase().contains(searchText.toLowerCase())) ||
                (password.getNotes().toLowerCase().contains(searchText.toLowerCase()));
    }

    private ArrayList<Password> filterList(List<Password> list, String searchText){
        ArrayList<Password> filteredList = new ArrayList<>();

        for(Password password: list){
            if(this.searchFindsOrder(password, searchText)) filteredList.add(password);
        }

        return filteredList;
    }

    private void editPassword(Password oldPassword, Password newPassword){

        if(newPassword != null){
            // update content in view
            this.displayContents(newPassword);

            passwordsFiles.update(oldPassword, newPassword);

            // update display summary
            this.displaySummary(passwordsFiles.getAll());
        }

        else System.out.println("ERROR... NEW PASSWORD RETURNS NULL... FIX THIS!");

    }

    private void deletePassword(Password password){
        // this will update both summary and the content
        // -- validating is necessary
        // ---- since you are using both buttons AND context menus

        if(passwordsFiles.getAll().contains(password)){

            String valueToCompareFrom = ((Label) ((Parent) contentBodyContents.getChildren().get(0)).getChildrenUnmodifiable().get(2)).getText();
            String valueToCompareTo = Utils.getFormattedDateTime(password.getDateAndTimeRaw());

            // -- this is to clear "content" view if current password being displayed is the one getting deleted
            if(valueToCompareFrom.equalsIgnoreCase(valueToCompareTo)){
                contentBodyContents.getChildren().clear();
            }

            passwordsFiles.delete(password);
            this.displaySummary(passwordsFiles.getAll());
        }
    }

    public void generatePasswordClick(MouseEvent event) throws IOException {

        MouseButton mouseButton = event.getButton();
        if(mouseButton == MouseButton.PRIMARY){
            searchField.clear(); // clears search field when generating new password

            // initialize adding new passwords
            GeneratePasswordFrame generatePasswordModal = new GeneratePasswordFrame();

            FXMLLoader loader = new FXMLLoader(MainFrame.class.getResource("generateFrame.fxml"));

            Parent generatePasswordRoot = loader.load();
            generatePasswordRoot.getStylesheets().add(MainFrame.class.getResource("style.css").toExternalForm());
            Platform.runLater(generatePasswordRoot::requestFocus);

            // initializing "new controller" to pass information between windows
            // in this case, im passing a new password to save in password list
            GeneratePasswordFrame newController = loader.getController();

            Scene generatePasswordScene = new Scene(generatePasswordRoot);
            generatePasswordScene.setFill(Color.TRANSPARENT);

            generatePasswordModal.setScene(generatePasswordScene);
            generatePasswordModal.initOwner(((Node) event.getSource()).getScene().getWindow());

            generatePasswordModal.getIcons().add(new Image(String.valueOf(MainApp.class.getResource("logo.png"))));
            generatePasswordModal.showAndWait();

            // returns password from modal then append that record to list
            // however, check if the return has value or null beforehand...
            // this is to eliminate any error(s) if the user closes the new window instead of clicking the save button
            if(newController.getGeneratedPassword() != null){
                passwordsFiles.create(newController.getGeneratedPassword());
            }

            // call displaySummary again to display new contents
            this.displaySummary(passwordsFiles.getAll());
        }
    }
}
