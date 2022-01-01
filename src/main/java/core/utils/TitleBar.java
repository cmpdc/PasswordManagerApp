package core.utils;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class TitleBar extends HBox {

    private double xPos = 0;
    private double yPos = 0;

    public TitleBar(String titleBarTitle, double titlePos, double titleBarWidth, double titleBarHeight){

        try{
            Label appTitle = new Label(titleBarTitle);
            appTitle.setTranslateX(titlePos);
            appTitle.setId("appTitle");
            appTitle.layout();

            HBox titleBarButtons = new HBox();
            titleBarButtons.setId("titleBarButtons");
            titleBarButtons.setAlignment(Pos.CENTER);

            Label minimizeButton = new Label();
            minimizeButton.setPrefSize(15, 15);
            minimizeButton.setAlignment(Pos.CENTER);
            minimizeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent e) {

                    MouseButton mouseButton = e.getButton();
                    if(mouseButton == MouseButton.PRIMARY){

                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.setIconified(true);
                    }
                }
            });

            SVGPath minimizeIcon = new SVGPath();
            minimizeIcon.setContent("M 1.14,8.46C 0.71,8.57 0.32,8.91 0.12,9.33  0.03,9.53 0.02,9.59 0.02,10.00  0.02,10.43 0.03,10.46 0.15,10.71  0.31,11.04 0.65,11.36 0.96,11.49  0.96,11.49 1.19,11.58 1.19,11.58  1.19,11.58 10.02,11.58 10.02,11.58  10.02,11.58 18.85,11.58 18.85,11.58  18.85,11.58 19.10,11.46 19.10,11.46  19.43,11.30 19.71,11.02 19.86,10.70  19.97,10.46 19.98,10.42 19.98,10.00  19.98,9.57 19.97,9.54 19.85,9.29  19.69,8.96 19.35,8.64 19.04,8.51  19.04,8.51 18.81,8.42 18.81,8.42  18.81,8.42 10.08,8.41 10.08,8.41  2.60,8.40 1.32,8.41 1.14,8.46 Z");
            minimizeIcon.setId("minimize");
            minimizeIcon.setScaleX(0.6);
            minimizeIcon.setScaleY(0.6);
            minimizeButton.setGraphic(minimizeIcon);

            Label helpButton = new Label();
            helpButton.setPrefSize(15, 15);
            helpButton.setAlignment(Pos.CENTER);
            helpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    MouseButton mouseButton = e.getButton();
                    if(mouseButton == MouseButton.PRIMARY){
                        try {
                            Desktop.getDesktop().browse(new URL(Constants.GITHUB_PAGE_URL).toURI());
                        }

                        catch (Exception ex) {
                            System.out.println(ex.toString());
                        }
                    }
                }
            });

            SVGPath helpIcon = new SVGPath();
            helpIcon.setContent("M 7.94,15.49C 7.61,15.58 7.34,15.73 7.11,15.97  6.86,16.22 6.71,16.48 6.62,16.82  6.54,17.13 6.54,18.30 6.62,18.61  6.79,19.25 7.27,19.76 7.89,19.94  8.20,20.03 10.22,20.03 10.53,19.94  10.84,19.85 11.09,19.70 11.34,19.45  11.61,19.18 11.77,18.86 11.84,18.47  11.90,18.09 11.87,16.96 11.78,16.70  11.62,16.21 11.17,15.74 10.68,15.54  10.46,15.45 10.40,15.45 9.30,15.44  8.39,15.43 8.10,15.45 7.94,15.49 ZM 8.46,0.05C 7.26,0.21 6.34,0.48 5.37,0.96  4.36,1.45 3.90,1.80 3.70,2.24  3.42,2.82 3.48,3.62 3.82,4.16  4.12,4.62 4.54,5.12 4.75,5.26  5.11,5.51 5.39,5.59 5.90,5.59  6.29,5.59 6.63,5.50 7.20,5.21  8.02,4.79 8.65,4.63 9.41,4.63  10.40,4.63 10.98,4.91 11.28,5.50  11.38,5.70 11.39,5.76 11.38,6.11  11.38,6.43 11.36,6.55 11.28,6.77  11.12,7.18 10.95,7.45 10.59,7.81  9.88,8.52 8.86,8.94 7.40,9.12  7.14,9.16 7.07,9.22 7.07,9.46  7.07,9.89 7.31,11.77 7.43,12.26  7.57,12.85 8.03,13.45 8.53,13.69  8.68,13.77 8.91,13.85 9.04,13.87  9.61,13.98 10.48,13.76 10.86,13.41  11.03,13.26 11.09,13.10 11.19,12.55  11.19,12.55 11.26,12.19 11.26,12.19  11.26,12.19 11.71,12.05 11.71,12.05  13.36,11.53 14.62,10.59 15.52,9.21  16.53,7.66 16.79,5.21 16.11,3.55  15.82,2.85 15.51,2.38 14.96,1.83  14.07,0.94 12.93,0.39 11.39,0.11  10.80,0.01 9.09,-0.03 8.46,0.05 Z");
            helpIcon.setId("help");
            helpIcon.setScaleX(0.6);
            helpIcon.setScaleY(0.6);
            helpButton.setGraphic(helpIcon);

            Label closeButton = new Label();
            closeButton.setPrefSize(15, 15);
            closeButton.setAlignment(Pos.CENTER);
            closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    MouseButton mouseButton = e.getButton();
                    if(mouseButton == MouseButton.PRIMARY){

                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.close();
                    };
                }
            });

            SVGPath closeIcon = new SVGPath();
            closeIcon.setId("close");
            closeIcon.setContent("M 1.74,0.12C 0.84,0.23 0.15,0.96 0.09,1.88  0.06,2.24 0.12,2.54 0.29,2.88  0.41,3.12 0.73,3.45 3.85,6.56  3.85,6.56 7.27,9.98 7.27,9.98  7.27,9.98 3.81,13.44 3.81,13.44  0.20,17.06 0.20,17.05 0.06,17.54  -0.02,17.83 -0.02,18.37 0.07,18.67  0.26,19.26 0.69,19.67 1.28,19.82  1.56,19.90 2.22,19.90 2.48,19.83  2.98,19.70 2.93,19.74 6.56,16.11  6.56,16.11 9.98,12.70 9.98,12.70  9.98,12.70 13.42,16.13 13.42,16.13  17.05,19.75 16.96,19.68 17.46,19.82  17.77,19.91 18.40,19.91 18.70,19.82  19.49,19.60 20.00,18.91 20.00,18.05  20.00,17.63 19.90,17.29 19.70,16.98  19.62,16.87 18.02,15.25 16.14,13.37  16.14,13.37 12.71,9.97 12.71,9.97  12.71,9.97 16.14,6.54 16.14,6.54  19.38,3.29 19.56,3.09 19.67,2.85  19.99,2.11 19.84,1.25 19.28,0.70  18.85,0.28 18.47,0.14 17.84,0.16  17.36,0.19 17.10,0.28 16.77,0.53  16.64,0.62 15.07,2.18 13.27,3.98  13.27,3.98 9.98,7.26 9.98,7.26  9.98,7.26 6.57,3.87 6.57,3.87  4.15,1.46 3.11,0.44 2.96,0.36  2.60,0.15 2.17,0.07 1.74,0.12 Z");
            closeIcon.setScaleX(0.6);
            closeIcon.setScaleY(0.6);
            closeButton.setGraphic(closeIcon);

            ArrayList<Label> buttons = new ArrayList<>();
            buttons.add(minimizeButton);
            buttons.add(helpButton);
            buttons.add(closeButton);

            // setting properties for each buttons
            for(Labeled eachButtons: buttons){
                eachButtons.setAlignment(Pos.CENTER);
                eachButtons.setPrefHeight(25);
                eachButtons.setPrefWidth(25);
            }

            // setting margins
            HBox.setMargin(minimizeButton, new Insets(3));
            HBox.setMargin(helpButton, new Insets(3));
            HBox.setMargin(closeButton, new Insets(3));

            // appending buttons to inner container
            titleBarButtons.getChildren().addAll(
                    minimizeButton,
                    helpButton,
                    closeButton
            );

            // adding mouse event listeners
            // --- dragging
            this.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    MouseButton mouseButton = e.getButton();
                    if(mouseButton == MouseButton.PRIMARY){

                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.setX(e.getScreenX() - xPos);
                        stage.setY(e.getScreenY() - yPos);
                        stage.setOpacity(0.5);
                    }
                }
            });

            // --- after dragging
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    MouseButton mouseButton = e.getButton();
                    if(mouseButton == MouseButton.PRIMARY){

                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.setOpacity(1);
                    }
                }
            });

            // -- clicking: updates position
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    MouseButton mouseButton = e.getButton();
                    if(mouseButton == MouseButton.PRIMARY){

                        xPos = e.getSceneX();
                        yPos = e.getSceneY();
                    }
                }
            });

            this.setId("titleBar");

            this.setMinWidth(titleBarWidth);
            this.setMaxWidth(titleBarWidth);
            this.setMinHeight(titleBarHeight);
            this.setMaxHeight(titleBarHeight);

            // appending appTitle and inner container to outer container
            this.getChildren().addAll(
                    appTitle,
                    titleBarButtons
            );

        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
