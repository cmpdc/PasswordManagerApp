package view;

import core.PasswordChecker;
import core.PasswordGenerator;
import core.utils.Constants;
import core.utils.TitleBar;
import core.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import core.Password;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ResourceBundle;

import static core.utils.Utils.generateRandomStrings;

public class GeneratePasswordFrame extends Stage implements Initializable {

    @FXML private Pane outerElem;
    @FXML private BorderPane innerElem;
    @FXML private BorderPane innerElemShadow;
    @FXML private TextField passwordTitle;
    @FXML private Label dateAndTime;
    @FXML private Pane firstPane;
    @FXML private TextField generatedPasswordField;
    @FXML private Button copyButton;
    @FXML private Button regeneratePasswordButton;
    @FXML private ProgressBar passwordStrengthBar;
    @FXML private Slider passwordLengthSlider;
    @FXML private TextField passwordLengthField;
    @FXML private CheckBox useDigitsCheckBox;
    @FXML private CheckBox useSymbolsCheckBox;
    @FXML private CheckBox useUppercaseCheckBox;
    @FXML private CheckBox useLowercaseCheckBox;
    @FXML private TextArea notes;
    @FXML private Button saveButton;

    private final double width = 575;
    private final double height = 500;

    private final int passwordLengthMin = 5;
    private final int passwordLengthMax = 60;

    private OffsetDateTime currentDateTime = OffsetDateTime.now();

    private final ObservableSet<CheckBox> selectedCheckBoxes = FXCollections.observableSet();
    private final ObservableSet<CheckBox> unselectedCheckBoxes = FXCollections.observableSet();

    private int passwordLength = 25;
    private final String generatedSalt = generateRandomStrings(passwordLength);

    private final PasswordGenerator passwordGenerator = new PasswordGenerator(
            passwordLength,
            true,
            true,
            true,
            true
    );

    private final String masterKey = Constants.ADMIN_MASTER_KEY;

    private String generatedPassword = passwordGenerator.generatePassword();
    private String encodedPassword = passwordGenerator.encodePassword(generatedPassword, masterKey, generatedSalt);
    private PasswordChecker passwordChecker = new PasswordChecker(generatedPassword);

    private Password newPassword;

    public GeneratePasswordFrame(){
        this.setWidth(width + 20);
        this.setHeight(height + 20);
        this.setTitle(Constants.APP_NAME + " - NEW PASSWORD");
        this.initModality(Modality.APPLICATION_MODAL);
        this.initStyle(StageStyle.TRANSPARENT);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // add toolbar
        innerElem.setTop(
                new TitleBar(
                        this.getTitle(),
                        -215,
                        width,
                        height
                )
        );

        Rectangle overflowFrame = new Rectangle(width, height);
        overflowFrame.setX(0);
        overflowFrame.setY(0);
        overflowFrame.setArcWidth(15.0); // adding arc (radius)
        overflowFrame.setArcHeight(15.0);
        innerElem.setClip(overflowFrame);

        innerElem.toFront();

        innerElemShadow.setDisable(false);
        innerElemShadow.setVisible(true);
        innerElemShadow.toBack();

        dateAndTime.setText(Utils.getFormattedDateTime(currentDateTime));

        SVGPath keyIcon = new SVGPath();
        keyIcon.setId("keyIcon");
        keyIcon.setContent("M 35.00,12.52 C 35.00,12.57 35.38,13.16 35.84,13.84   39.45,19.15 39.94,25.51 37.20,31.40   36.95,31.92 36.75,32.42 36.75,32.50   36.75,32.67 39.06,36.74 39.29,36.96   39.53,37.20 54.60,45.88 54.78,45.88   54.86,45.86 56.34,45.49 58.04,45.02   58.04,45.02 61.15,44.19 61.15,44.19   61.15,44.19 62.58,41.70 62.58,41.70   63.38,40.34 63.99,39.16 63.96,39.08   63.93,39.00 62.96,38.39 61.80,37.72   61.80,37.72 59.69,36.51 59.69,36.51   59.69,36.51 57.70,36.77 57.70,36.77   56.61,36.91 55.68,36.97 55.62,36.91   55.56,36.86 55.19,36.01 54.78,35.02   54.78,35.02 54.04,33.24 54.04,33.24   54.04,33.24 52.45,32.33 52.45,32.33   52.45,32.33 50.86,31.42 50.86,31.42   50.86,31.42 48.83,31.67 48.83,31.67   48.83,31.67 46.79,31.94 46.79,31.94   46.79,31.94 46.01,30.05 46.01,30.05   46.01,30.05 45.23,28.16 45.23,28.16   45.23,28.16 42.56,26.61 42.56,26.61   42.56,26.61 39.89,25.07 39.89,25.07   39.89,25.07 39.96,23.85 39.96,23.85   40.14,21.05 39.31,18.04 37.70,15.51   37.18,14.70 35.01,12.30 35.00,12.52 Z M 20.26,0.14 C 18.01,0.57 15.69,1.99 14.25,3.79   13.18,5.14 12.46,6.84 12.06,9.00   12.01,9.29 11.91,9.34 11.04,9.51   6.01,10.56 1.81,14.51 0.44,19.50   0.04,20.94 -0.12,23.84 0.11,25.38   0.79,29.79 3.68,33.74 7.66,35.70   7.66,35.70 8.62,36.17 8.62,36.17   8.62,36.17 8.62,47.81 8.62,47.81   8.62,47.81 8.62,59.44 8.62,59.44   8.62,59.44 10.91,61.73 10.91,61.73   10.91,61.73 13.19,64.00 13.19,64.00   13.19,64.00 16.23,64.00 16.23,64.00   16.23,64.00 19.25,64.00 19.25,64.00   19.25,64.00 19.25,61.49 19.25,61.49   19.25,61.49 19.25,58.98 19.25,58.98   19.25,58.98 18.06,57.44 18.06,57.44   17.41,56.59 16.88,55.84 16.88,55.76   16.88,55.70 17.41,54.94 18.06,54.08   18.06,54.08 19.25,52.51 19.25,52.51   19.25,52.51 19.25,50.67 19.25,50.67   19.25,50.67 19.25,48.84 19.25,48.84   19.25,48.84 18.03,47.20 18.03,47.20   18.03,47.20 16.79,45.55 16.79,45.55   16.79,45.55 18.03,43.95 18.03,43.95   18.03,43.95 19.25,42.35 19.25,42.35   19.25,42.35 19.25,39.26 19.25,39.26   19.25,39.26 19.25,36.17 19.25,36.17   19.25,36.17 19.98,35.85 19.98,35.85   23.84,34.06 26.86,30.15 27.69,25.82   27.91,24.65 27.95,22.17 27.76,21.00   26.85,15.40 22.68,10.91 17.20,9.60   17.20,9.60 15.99,9.31 15.99,9.31   15.99,9.31 16.07,8.81 16.07,8.81   16.20,8.07 16.64,7.16 17.29,6.31   18.07,5.26 19.24,4.46 20.61,4.05   21.36,3.82 23.16,3.82 23.95,4.06   26.11,4.70 27.86,6.52 28.44,8.71   28.62,9.44 28.70,11.80 28.54,11.72   28.48,11.70 28.05,11.50 27.59,11.27   27.11,11.04 26.70,10.89 26.65,10.92   26.61,10.97 26.80,11.24 27.08,11.54   27.78,12.27 28.26,12.91 28.96,14.06   30.69,16.89 31.51,19.86 31.51,23.20   31.51,28.91 28.89,34.06 24.23,37.47   23.83,37.77 23.50,38.05 23.50,38.10   23.50,38.15 26.05,42.60 29.16,48.00   29.16,48.00 34.83,57.80 34.83,57.80   34.83,57.80 38.04,58.66 38.04,58.66   38.04,58.66 41.26,59.52 41.26,59.52   41.26,59.52 43.81,58.06 43.81,58.06   45.23,57.25 46.38,56.58 46.38,56.55   46.38,56.52 45.81,55.52 45.12,54.33   45.12,54.33 43.86,52.15 43.86,52.15   43.86,52.15 42.00,51.38 42.00,51.38   39.83,50.46 40.03,50.83 40.36,48.21   40.36,48.21 40.58,46.49 40.58,46.49   40.58,46.49 39.69,44.91 39.69,44.91   39.69,44.91 38.79,43.34 38.79,43.34   38.79,43.34 36.96,42.58 36.96,42.58   35.96,42.15 35.10,41.77 35.06,41.72   35.01,41.69 35.10,40.75 35.24,39.66   35.24,39.66 35.50,37.67 35.50,37.67   35.50,37.67 34.00,35.08 34.00,35.08   33.18,33.65 32.50,32.44 32.50,32.36   32.50,32.30 32.69,31.99 32.93,31.65   33.16,31.32 33.61,30.52 33.93,29.88   36.04,25.46 35.75,20.36 33.16,16.31   33.16,16.31 32.52,15.31 32.52,15.31   32.52,15.31 32.46,11.81 32.46,11.81   32.40,7.96 32.35,7.62 31.55,5.90   30.23,3.02 27.61,0.90 24.53,0.20   23.56,-0.01 21.23,-0.05 20.26,0.14 Z M 15.58,16.85 C 16.20,17.17 16.90,17.91 17.28,18.62   17.68,19.40 17.68,20.67 17.26,21.60   16.90,22.41 15.96,23.27 15.16,23.55   12.78,24.36 10.33,22.62 10.33,20.12   10.31,17.42 13.20,15.62 15.58,16.85 Z");
        keyIcon.setScaleX(1);
        keyIcon.setScaleY(1);
        keyIcon.setTranslateX(20);
        keyIcon.setTranslateY(15);
        firstPane.getChildren().add(2, keyIcon);

        SVGPath copyPasswordIcon = new SVGPath();
        copyPasswordIcon.setContent("M 2.73,0.73 C 2.46,0.82 2.29,0.97 2.14,1.26   2.14,1.26 2.07,1.39 2.07,1.39   2.07,1.39 2.07,7.84 2.07,7.84   2.07,7.84 2.07,14.28 2.07,14.28   2.07,14.28 2.14,14.43 2.14,14.43   2.23,14.62 2.43,14.82 2.62,14.91   2.62,14.91 2.77,14.99 2.77,14.99   2.77,14.99 7.46,14.99 7.46,14.99   12.15,15.00 12.16,15.00 12.32,14.94   12.54,14.85 12.77,14.64 12.88,14.41   12.88,14.41 12.96,14.22 12.96,14.22   12.96,14.22 12.96,7.84 12.96,7.84   12.96,7.84 12.96,1.45 12.96,1.45   12.96,1.45 12.87,1.25 12.87,1.25   12.76,1.04 12.59,0.87 12.37,0.76   12.24,0.69 12.20,0.69 11.57,0.68   11.57,0.68 10.90,0.67 10.90,0.67   10.90,0.67 10.89,1.31 10.89,1.31   10.88,1.93 10.88,1.95 10.80,2.12   10.66,2.42 10.39,2.63 10.05,2.70   9.83,2.74 5.19,2.74 4.97,2.69   4.62,2.63 4.34,2.41 4.20,2.10   4.12,1.92 4.12,1.91 4.11,1.29   4.11,1.29 4.10,0.67 4.10,0.67   4.10,0.67 3.49,0.67 3.49,0.67   2.99,0.68 2.86,0.69 2.73,0.73 Z M 10.67,4.82 C 10.80,4.87 10.87,4.96 10.89,5.09   10.90,5.19 10.89,5.22 10.80,5.32   10.80,5.32 10.70,5.43 10.70,5.43   10.70,5.43 7.56,5.44 7.56,5.44   4.14,5.45 4.28,5.46 4.17,5.29   4.06,5.12 4.14,4.88 4.34,4.81   4.49,4.76 10.54,4.76 10.67,4.82 Z M 10.71,6.87 C 11.01,7.02 10.92,7.44 10.57,7.49   10.50,7.49 9.07,7.50 7.40,7.49   4.50,7.49 4.34,7.48 4.27,7.43   4.22,7.40 4.17,7.33 4.14,7.27   4.10,7.18 4.10,7.15 4.14,7.06   4.16,7.00 4.22,6.93 4.27,6.89   4.27,6.89 4.37,6.83 4.37,6.83   4.37,6.83 7.49,6.83 7.49,6.83   10.25,6.83 10.62,6.83 10.71,6.87 Z M 10.72,8.92 C 10.82,8.97 10.90,9.10 10.90,9.20   10.90,9.28 10.82,9.42 10.75,9.47   10.69,9.52 10.34,9.52 7.52,9.52   4.03,9.52 4.24,9.53 4.15,9.33   4.11,9.25 4.10,9.21 4.13,9.13   4.16,9.03 4.25,8.93 4.34,8.90   4.36,8.89 5.79,8.88 7.51,8.88   10.25,8.88 10.65,8.88 10.72,8.92 Z M 10.72,10.97 C 10.82,11.02 10.90,11.15 10.90,11.25   10.90,11.29 10.88,11.35 10.85,11.40   10.76,11.58 10.95,11.57 7.50,11.57   4.00,11.57 4.25,11.59 4.14,11.37   4.07,11.22 4.12,11.09 4.28,10.98   4.34,10.93 4.58,10.93 7.49,10.93   10.25,10.93 10.65,10.93 10.72,10.97 Z M 4.88,0.10 C 4.88,0.10 4.78,0.20 4.78,0.20   4.78,0.20 4.78,1.03 4.78,1.03   4.78,1.03 4.78,1.85 4.78,1.85   4.78,1.85 4.88,1.95 4.88,1.95   4.88,1.95 4.97,2.05 4.97,2.05   4.97,2.05 7.48,2.05 7.48,2.05   10.25,2.05 10.11,2.06 10.21,1.86   10.27,1.73 10.28,0.35 10.21,0.19   10.13,-0.01 10.31,0.00 7.51,0.00   7.51,0.00 4.97,0.00 4.97,0.00   4.97,0.00 4.88,0.10 4.88,0.10 Z");
        copyPasswordIcon.setId("copyPasswordIcon");
        copyPasswordIcon.setScaleX(1.5);
        copyPasswordIcon.setScaleY(1.5);
        copyButton.setGraphic(copyPasswordIcon);

        SVGPath regeneratePasswordIcon = new SVGPath();
        regeneratePasswordIcon.setContent("M 16.84,9.46 C 16.57,9.59 16.50,9.74 16.47,10.15   16.35,11.99 15.75,13.40 14.57,14.57   13.35,15.79 11.77,16.45 10.05,16.45   9.25,16.45 8.64,16.34 7.92,16.09   7.92,16.09 7.57,15.96 7.57,15.96   7.57,15.96 8.16,15.38 8.16,15.38   8.48,15.05 8.76,14.75 8.79,14.70   8.81,14.65 8.83,14.53 8.83,14.42   8.83,14.19 8.71,14.00 8.52,13.90   8.34,13.82 2.60,12.99 2.36,13.02   2.13,13.04 1.97,13.17 1.89,13.38   1.89,13.38 1.82,13.56 1.82,13.56   1.82,13.56 2.25,16.54 2.25,16.54   2.54,18.64 2.69,19.56 2.74,19.67   2.86,19.94 3.27,20.09 3.53,19.96   3.57,19.93 3.91,19.62 4.27,19.26   4.27,19.26 4.92,18.61 4.92,18.61   4.92,18.61 5.05,18.69 5.05,18.69   6.03,19.25 7.18,19.67 8.43,19.89   9.08,20.00 10.52,20.04 11.25,19.95   13.11,19.72 14.89,18.97 16.33,17.82   18.49,16.09 19.88,13.36 19.99,10.68   20.01,10.16 20.00,10.13 19.92,10.00   19.75,9.77 19.68,9.75 18.34,9.55   17.65,9.45 17.07,9.38 17.05,9.38   17.03,9.38 16.93,9.41 16.84,9.46 Z M 8.67,0.06 C 5.18,0.51 2.18,2.75 0.82,5.92   0.30,7.11 0.00,8.48 0.00,9.58   0.00,9.94 0.00,9.95 0.14,10.11   0.24,10.23 0.32,10.28 0.46,10.31   0.84,10.39 2.91,10.67 3.04,10.65   3.20,10.62 3.38,10.49 3.46,10.35   3.50,10.27 3.53,10.03 3.55,9.65   3.57,9.33 3.63,8.89 3.67,8.68   4.16,6.30 5.92,4.43 8.27,3.77   9.50,3.43 10.96,3.49 12.15,3.93   12.15,3.93 12.43,4.04 12.43,4.04   12.43,4.04 11.82,4.65 11.82,4.65   11.49,4.98 11.20,5.31 11.17,5.37   11.07,5.65 11.20,6.01 11.45,6.14   11.59,6.20 17.23,7.03 17.56,7.03   17.86,7.03 18.12,6.77 18.12,6.47   18.12,6.38 17.94,5.00 17.71,3.40   17.42,1.34 17.28,0.45 17.23,0.36   17.14,0.18 17.01,0.08 16.83,0.05   16.50,0.00 16.44,0.04 15.73,0.74   15.73,0.74 15.06,1.39 15.06,1.39   15.06,1.39 14.90,1.30 14.90,1.30   14.63,1.13 13.82,0.75 13.43,0.61   12.68,0.33 11.95,0.15 11.13,0.05   10.57,-0.01 9.22,-0.01 8.67,0.06 Z");
        regeneratePasswordIcon.setScaleX(1.5);
        regeneratePasswordIcon.setScaleY(1.5);
        regeneratePasswordButton.setGraphic(regeneratePasswordIcon);

        // initialize controller input values
        passwordLengthSlider.setValue(passwordLength);
        passwordLengthSlider.setMin(passwordLengthMin);
        passwordLengthSlider.setMax(passwordLengthMax);

        passwordLengthField.setText(String.valueOf(passwordLength));

        // listener for passwordLengthField so that it only accept numbers
        // removal of the ability to delete
        passwordLengthField.setTextFormatter(new TextFormatter<Integer>(change -> {
            String txt = change.getControlNewText();

            // null if user enters 0 beforehand.. Also, WHY?!
            if (txt.matches("0\\d+")) return null;

            // parsing to check if the result is in [5, 60].
            try {
                int n = Integer.parseInt(txt);
                return (passwordLengthMin <= n && n <= passwordLengthMax) ? change : null;
            }

            catch (Exception e) {
                return null;
            }
        }));

        generatedPasswordField.setText(generatedPassword);
        passwordStrengthBar.setProgress(passwordChecker.getPasswordStrengthScore());

        this.validateCheckBox(useDigitsCheckBox);
        this.validateCheckBox(useSymbolsCheckBox);
        this.validateCheckBox(useLowercaseCheckBox);
        this.validateCheckBox(useUppercaseCheckBox);
    }

    private void validateCheckBox(CheckBox checkBox){

        if(checkBox.isSelected()) this.selectedCheckBoxes.add(checkBox);
        else this.unselectedCheckBoxes.add(checkBox);

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
            }
            else{
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
            }

        });

    }

    public void passwordSliderValueChange(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            int sliderValueInt = (int) passwordLengthSlider.getValue();
            passwordLength = sliderValueInt;
            passwordLengthField.setText(String.valueOf(sliderValueInt));

            passwordGenerator.setPassLength(sliderValueInt);
            generatedPassword = passwordGenerator.generatePassword();
            encodedPassword = passwordGenerator.encodePassword(generatedPassword, masterKey, generatedSalt);

            passwordChecker = new PasswordChecker(generatedPassword);
            generatedPasswordField.setText(generatedPassword);
            passwordStrengthBar.setProgress(passwordChecker.getPasswordStrengthScore());
        }
    }

    public void passwordFieldValueChange(KeyEvent event) {
        int fieldValueInt = Integer.parseInt(passwordLengthField.getText());

        if(event.getCode() == KeyCode.DOWN){
            BigDecimal value = BigDecimal.valueOf(Long.parseLong(passwordLengthField.getText()) - 1);
            passwordLengthField.setText(value.toString());
            event.consume();
        }

        if(event.getCode() == KeyCode.UP){
            BigDecimal value = BigDecimal.valueOf(Long.parseLong(passwordLengthField.getText()) + 1);
            passwordLengthField.setText(value.toString());
            event.consume();
        }

        passwordLength = fieldValueInt;
        passwordLengthSlider.setValue(fieldValueInt);

        passwordGenerator.setPassLength(fieldValueInt);
        generatedPassword = passwordGenerator.generatePassword();
        encodedPassword = passwordGenerator.encodePassword(generatedPassword, masterKey, generatedSalt);

        passwordChecker = new PasswordChecker(generatedPassword);
        generatedPasswordField.setText(generatedPassword);
        passwordStrengthBar.setProgress(passwordChecker.getPasswordStrengthScore());
    }

    public void hasDigitsCheckboxValueChanged(MouseEvent mouseEvent) {

        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            if(selectedCheckBoxes.isEmpty()) useDigitsCheckBox.setSelected(true);
            else selectedCheckBoxes.forEach(cb -> cb.setDisable(false));

            boolean isNums = useDigitsCheckBox.isSelected();

            passwordGenerator.setNumIncluded(isNums);
            generatedPassword = passwordGenerator.generatePassword();
            encodedPassword = passwordGenerator.encodePassword(generatedPassword, masterKey, generatedSalt);

            passwordChecker = new PasswordChecker(generatedPassword);
            generatedPasswordField.setText(generatedPassword);
            passwordStrengthBar.setProgress(passwordChecker.getPasswordStrengthScore());
        }
    }

    public void hasSymbolsCheckboxValueChanged(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            if(selectedCheckBoxes.isEmpty()) useSymbolsCheckBox.setSelected(true);
            else selectedCheckBoxes.forEach(cb -> cb.setDisable(false));

            boolean isChars = useSymbolsCheckBox.isSelected();

            passwordGenerator.setSpecialCharsIncluded(isChars);
            generatedPassword = passwordGenerator.generatePassword();
            encodedPassword = passwordGenerator.encodePassword(generatedPassword, masterKey, generatedSalt);

            passwordChecker = new PasswordChecker(generatedPassword);
            generatedPasswordField.setText(generatedPassword);
            passwordStrengthBar.setProgress(passwordChecker.getPasswordStrengthScore());

        }
    }

    public void hasUppercaseCheckboxValueChanged(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            if(selectedCheckBoxes.isEmpty()) useUppercaseCheckBox.setSelected(true);
            else selectedCheckBoxes.forEach(cb -> cb.setDisable(false));

            boolean isUpper = useUppercaseCheckBox.isSelected();

            passwordGenerator.setUpperIncluded(isUpper);
            generatedPassword = passwordGenerator.generatePassword();
            encodedPassword = passwordGenerator.encodePassword(generatedPassword, masterKey, generatedSalt);

            passwordChecker = new PasswordChecker(generatedPassword);
            generatedPasswordField.setText(generatedPassword);
            passwordStrengthBar.setProgress(passwordChecker.getPasswordStrengthScore());
        }
    }

    public void hasLowercaseCheckboxValueChanged(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            if(selectedCheckBoxes.isEmpty()) useLowercaseCheckBox.setSelected(true);
            else selectedCheckBoxes.forEach(cb -> cb.setDisable(false));

            boolean isLower = useLowercaseCheckBox.isSelected();

            passwordGenerator.setLowerIncluded(isLower);
            generatedPassword = passwordGenerator.generatePassword();
            encodedPassword = passwordGenerator.encodePassword(generatedPassword, masterKey, generatedSalt);

            passwordChecker = new PasswordChecker(generatedPassword);
            generatedPasswordField.setText(generatedPassword);
            passwordStrengthBar.setProgress(passwordChecker.getPasswordStrengthScore());
        }
    }

    public void generatedPasswordFieldHovered(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            generatedPasswordField.getStyleClass().add("isHovered");
        }
    }

    public void generatedPasswordFieldMouseExited(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            generatedPasswordField.getStyleClass().remove("isHovered");
            generatedPasswordField.getStyleClass().remove("isClicked");
        }
    }

    public void generatedPasswordFieldNotClicked(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            generatedPasswordField.getStyleClass().remove("isClicked");
        }
    }

    public void copyButtonClick(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            generatedPasswordField.getStyleClass().add("isClicked");

            // programmatically copy contents into system's clipboard
            StringSelection copiedGeneratedPassword = new StringSelection(generatedPasswordField.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(copiedGeneratedPassword, null);
        }
    }

    public void generateButtonClick(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            generatedPasswordField.getStyleClass().add("isClicked");

            // re-generate password
            generatedPassword = passwordGenerator.generatePassword();
            encodedPassword = passwordGenerator.encodePassword(generatedPassword, masterKey, generatedSalt);

            passwordChecker = new PasswordChecker(generatedPassword);
            generatedPasswordField.setText(generatedPassword);
            passwordStrengthBar.setProgress(passwordChecker.getPasswordStrengthScore());
        }
    }

    public Password getGeneratedPassword(){return this.newPassword;}

    public void saveButtonFunc(MouseEvent mouseEvent) {
        MouseButton mouseButton = mouseEvent.getButton();
        if(mouseButton == MouseButton.PRIMARY){

            currentDateTime = OffsetDateTime.now();

            this.newPassword = new Password(
                    passwordTitle.getText(),
                    notes.getText(),
                    generatedSalt,
                    encodedPassword,
                    currentDateTime
            );

            // closing "this" window and going back to the main window
            // NOTE: you're using "outer" node as the "parent" of all the nodes in this frame's window
            // therefore, you are using said node in order to close "this" window's stage
            ((Stage) (outerElem.getScene().getWindow())).close();
        }
    }
}
