package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class that adds an inhouse part object
 *
 * @author José L Dávila Montalvo
 */
public class PartsScreenInHouse implements Initializable {

    /**
     * main pane for the inHouse scene.
     */
    @FXML
    private AnchorPane inHouse;

    /**
     * Part name textfield
     */
    @FXML
    private TextField inHouseName;

    /**
     * Part inventory textfield
     */
    @FXML
    private TextField inHouseInv;

    /**
     * Part price textfield
     */
    @FXML
    private TextField inHousePrice;

    /**
     * Part max textfield
     */
    @FXML
    private TextField inHouseMax;

    /**
     * Part machien ID textfield
     */
    @FXML
    private TextField inHouseMachine;

    /**
     * Part min textfield
     */
    @FXML
    private TextField inHouseMin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * takes the user to the Outsourced part screen.
     *
     * @throws IOException Error loading the view
     */
    public void selectOutBtn() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/Parts_Screen_Outsourced.fxml"));
        inHouse.getChildren().setAll(pane);
    }

    /**
     * Takes user back to the MainScreen, no parts are added.
     *
     * @param actionEvent cancel button press
     * @throws IOException Error loading the view
     */
    public void inCancelBtn(ActionEvent actionEvent) throws IOException {
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
    public void inHouseSaveBtn(ActionEvent actionEvent) throws IOException{

        int id = Inventory.incrementPartId();

        try {

            int stock;
            Double price;
            int min;
            int max;
            int machineId;
            String name;

            // if-else statements validate user input and throw detailed exceptions specific to the invalid entry.
            if(inHouseName.getText().isEmpty()){
                throw new NullPointerException("Please enter a part name in the Name field");
            }else{
                name = inHouseName.getText();
            }

            // used regex to validate if anything other than an integer is entered.
            // since price is a double I excluded the . character to allow decimals
            if (inHousePrice.getText().matches(".*[^0-9.].*") || inHousePrice.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Price/Cost field");
            } else {
                price = Double.parseDouble(inHousePrice.getText());
            }

            if (inHouseMax.getText().matches(".*\\D.*") || inHouseMax.getText().isEmpty()) {
                throw new IllegalArgumentException("Please enter a number in the Max field");
            } else {
                max = Integer.parseInt(inHouseMax.getText());
            }

            if (inHouseMin.getText().matches(".*\\D.*") || inHouseMin.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Min field");
            }else if (Integer.parseInt(inHouseMin.getText()) > max){
                throw new IllegalArgumentException("Min cannot be greater than max");
            }else {
                min = Integer.parseInt(inHouseMin.getText());
            }

            if (inHouseMachine.getText().matches(".*\\D.*") || inHouseMachine.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Machine ID field");
            } else {
                machineId = Integer.parseInt(inHouseMachine.getText());
            }

            // second else if statement validates correct inventory number
            if (inHouseInv.getText().matches(".*\\D.*") || inHouseInv.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Inv field");
            }else if (Integer.parseInt(inHouseInv.getText()) > max || Integer.parseInt(inHouseInv.getText()) < min){
                throw new IllegalArgumentException("Inventory number entered is outside the min/max range");
            }else {
                stock = Integer.parseInt(inHouseInv.getText());
            }

            Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));

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
