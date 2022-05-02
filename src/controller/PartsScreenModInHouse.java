package controller;

import javafx.collections.ObservableList;
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
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class that modifies a selected inhouse part object
 *
 * @author José L Dávila Montalvo
 */
public class PartsScreenModInHouse implements Initializable {

    /**
     * Part name textfield
     */
    @FXML
    private TextField inhouseName;

    /**
     * Part inventory textfield
     */
    @FXML
    private TextField inhouseInv;

    /**
     * Part price textfield
     */
    @FXML
    private TextField inhousePrice;

    /**
     * Part max textfield
     */
    @FXML
    private TextField inhouseMax;

    /**
     * Part machine id textfield
     */
    @FXML
    private TextField inhouseMachine;

    /**
     * Part min textfield
     */
    @FXML
    private TextField inhouseMin;

    /**
     * Part id textfield
     */
    @FXML
    private TextField inhouseID;

    /**
     * main pane for the inHouse scene.
     */
    @FXML
    private AnchorPane inHouse;

    /**
     * Part returned from MainScreen controller.
     */
    public static Part thisPart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Method populates the textfields using values sent by the MainScreen controller.
     *
     * @param id product id
     * @param name product name
     * @param inv   product inventory amount
     * @param price product price
     * @param max product max inventory amount
     * @param min product min inventory amount
     * @param machineId machine id that created the part
     */
    public void setFields(int id, String name, int inv, double price, int max, int min, int machineId){

        inhouseID.setText(Integer.toString(id));
        inhouseName.setText(name);
        inhouseInv.setText(Integer.toString(inv));
        inhousePrice.setText(Double.toString(price));
        inhouseMax.setText(Integer.toString(max));
        inhouseMin.setText(Integer.toString(min));
        inhouseMachine.setText(Integer.toString(machineId));

    }

    /**
     * Method gets the part object sent by the MainScreen controller.
     *
     * @param part part object returned from MainController
     */
    public void getPart(Part part){
        thisPart = part;
    }

    /**
     * Returns the user to the MainScreen and no parts are modified.
     *
     * @param actionEvent cancel button press
     * @throws IOException Error loading the view
     */
    public void inModCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1070,400);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Saves the modified part
     *
     * @param actionEvent save button press
     * @throws IOException Error loading the view
     */
    public void saveData(ActionEvent actionEvent) throws IOException {

        ObservableList<Part> testList = Inventory.getAllParts();

        int index = testList.indexOf(thisPart);

        try {

            // if-else statements validate user input and throw detailed exceptions specific to the invalid entry.
            if(inhouseName.getText().isEmpty()){
                throw new NullPointerException("Please enter a part name in the Name field");
            }

            // used regex to validate if anything other than an integer is entered.
            // since price is a double I excluded the . character to allow decimals
            if (inhousePrice.getText().matches(".*[^0-9.].*") || inhousePrice.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Price/Cost field");
            }

            if (inhouseMax.getText().matches(".*\\D.*") || inhouseMax.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Max field");
            }

            if (inhouseMin.getText().matches(".*\\D.*") || inhouseMin.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Min field");
            }else if(Integer.parseInt(inhouseMin.getText()) > Integer.parseInt(inhouseMax.getText())){
                throw new IllegalArgumentException("Min cannot be greater than max");
            }

            // second else if statement validates correct inventory number
            if (inhouseInv.getText().matches(".*\\D.*") || inhouseInv.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Inv field");
            }else if (Integer.parseInt(inhouseInv.getText()) > Integer.parseInt(inhouseMax.getText()) || Integer.parseInt(inhouseInv.getText()) < Integer.parseInt(inhouseMin.getText())){
                throw new IllegalArgumentException("Inventory number entered is outside the min/max range");
            }

            if (inhouseMachine.getText().matches(".*\\D.*") || inhouseMachine.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Machine ID field");
            }

            Inventory.updateInhousePart(index, thisPart, inhouseName.getText(), Double.parseDouble(inhousePrice.getText()),
                    Integer.parseInt(inhouseInv.getText()),Integer.parseInt(inhouseMin.getText()), Integer.parseInt(inhouseMax.getText()),
                    Integer.parseInt(inhouseMachine.getText()));

            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main_Screen.fxml"));
            Region root = loader.load();

            Scene scene = new Scene(root);
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
