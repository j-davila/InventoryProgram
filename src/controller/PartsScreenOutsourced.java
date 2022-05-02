package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import model.Inventory;
import model.OutSourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class that adds an inhouse part object
 *
 * @author José L Dávila Montalvo
 */
public class PartsScreenOutsourced implements Initializable {

    /**
     * main pane for the outsourced scene.
     */
    @FXML
    private AnchorPane outsourced;

    /**
     * Part name textfield
     */
    @FXML
    private TextField outsourcedName;

    /**
     * Part inventory textfield
     */
    @FXML
    private TextField outsourcedInv;

    /**
     * Part price textfield
     */
    @FXML
    private TextField outsourcedPrice;

    /**
     * Part max textfield
     */
    @FXML
    private TextField outsourcedMax;

    /**
     * Part company name textfield
     */
    @FXML
    private TextField outsourcedCompany;

    /**
     * Part min textfield
     */
    @FXML
    private TextField outsourcedMin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * takes the user to the Inhouse part screen.
     *
     * @throws IOException Error loading the view
     */
    public void selectIn() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/Parts_Screen_InHouse.fxml"));
        outsourced.getChildren().setAll(pane);
    }

    /**
     * Takes user back to the MainScreen, no parts are added.
     *
     * @param actionEvent cancel button press
     * @throws IOException Error loading the view
     */
    public void outCancelBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1070,400);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Saves part object entered by the user.
     *
     * @param actionEvent save button press
     * @throws IOException Error loading the view
     */
    public void outSaveBtn(ActionEvent actionEvent) throws IOException {

        int id = Inventory.incrementPartId();

        try {
            int stock;
            Double price;
            int min;
            int max;
            String companyName;
            String name;

            // if-else statements validate user input and throw detailed exceptions specific to the invalid entry.
            if(outsourcedName.getText().isEmpty()){
                throw new NullPointerException("Please enter a part name in the Name field");
            }else{
                name = outsourcedName.getText();
            }

            // used regex to validate if anything other than an integer is entered.
            // since price is a double I excluded the . character to allow decimals
            if (outsourcedPrice.getText().matches(".*[^0-9.].*") || outsourcedPrice.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Price/Cost field");
            } else {
                price = Double.parseDouble(outsourcedPrice.getText());
            }

            if (outsourcedMax.getText().matches(".*\\D.*") || outsourcedMax.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Max field");
            } else {
                max = Integer.parseInt(outsourcedMax.getText());
            }

            if (outsourcedMin.getText().matches(".*\\D.*") || outsourcedMin.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Min field");
            }else if(Integer.parseInt(outsourcedMin.getText()) > max){
                throw new IllegalArgumentException("Min cannot be greater than max");
            } else {
                min = Integer.parseInt(outsourcedMin.getText());
            }

            // second else if statement validates correct inventory number
            if (outsourcedInv.getText().matches(".*\\D.*") || outsourcedInv.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Inv field");
            }else if(Integer.parseInt(outsourcedInv.getText()) > max || Integer.parseInt(outsourcedInv.getText()) < min){
                throw new IllegalArgumentException("Inventory number entered is outside the min/max range");
            } else {
                stock = Integer.parseInt(outsourcedInv.getText());
            }

            if (outsourcedCompany.getText().isEmpty()) {
                throw new NullPointerException("Please enter a name in the Company Name field");
            } else {
                companyName = outsourcedCompany.getText();
            }

            Inventory.addPart(new OutSourced(id, name, price, stock, min, max, companyName));

            Parent root = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1070, 400);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();

        } catch(NullPointerException | IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }
}
