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
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class that modifies a selected outsourced part object
 *
 * @author José L Dávila Montalvo
 */
public class PartsScreenModOutsourced implements Initializable {

    /**
     * Part id textfield
     */
    @FXML
    private TextField outsourcedID;

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

    /**
     * Part returned from MainScreen controller.
     */
    public static Part thisPart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Method gets the part object sent by the MainScreen controller.
     *
     * @param part part object returned from the MainController
     */
    public void getPart(Part part){
        thisPart = part;
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
     * @param companyName name of the company where the part comes from
     */
    public void setFields(int id, String name, int inv, double price, int max, int min, String companyName){

        outsourcedID.setText(Integer.toString(id));
        outsourcedName.setText(name);
        outsourcedInv.setText(Integer.toString(inv));
        outsourcedPrice.setText(Double.toString(price));
        outsourcedMax.setText(Integer.toString(max));
        outsourcedMin.setText(Integer.toString(min));
        outsourcedCompany.setText(companyName);
    }

    /**
     * Returns the user to the MainScreen and no parts are modified.
     *
     * @param actionEvent cancel button press
     * @throws IOException Error loading the view
     */
    public void outModCancel(ActionEvent actionEvent) throws IOException {
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
    public void savePart(ActionEvent actionEvent) throws IOException {

        ObservableList<Part> testList = Inventory.getAllParts();

        int index = testList.indexOf(thisPart);

        try {

            // if-else statements validate user input and throw detailed exceptions specific to the invalid entry.
            if(outsourcedName.getText().isEmpty()){
                throw new NullPointerException("Please enter a part name in the Name field");
            }

            // if-else statements validate user input and throw detailed exceptions specific to the invalid entry.
            if (outsourcedPrice.getText().matches(".*[^0-9.].*") || outsourcedPrice.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Price/Cost field");
            }

            if (outsourcedMax.getText().matches(".*\\D.*") || outsourcedMax.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Max field");
            }

            if (outsourcedMin.getText().matches(".*\\D.*") || outsourcedMin.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Min field");
            }else if(Integer.parseInt(outsourcedMin.getText()) > Integer.parseInt(outsourcedMax.getText())){
                throw new IllegalArgumentException("Min cannot be greater than max");
            }

            // second else if statement validates correct inventory number
            if (outsourcedInv.getText().matches(".*\\D.*") || outsourcedInv.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Inv field");
            }else if(Integer.parseInt(outsourcedInv.getText()) > Integer.parseInt(outsourcedMax.getText()) || Integer.parseInt(outsourcedInv.getText()) < Integer.parseInt(outsourcedMin.getText())){
                throw new IllegalArgumentException("Inventory number entered is outside the min/max range");
            }

            if (outsourcedCompany.getText().isEmpty()) {
                throw new NullPointerException("Please enter a name in the Company Name field");
            }

            Inventory.updateOutsourcedPart(index, thisPart, outsourcedName.getText(), Double.parseDouble(outsourcedPrice.getText()),
                    Integer.parseInt(outsourcedInv.getText()),Integer.parseInt(outsourcedMin.getText()), Integer.parseInt(outsourcedMax.getText()),
                    outsourcedCompany.getText());

            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main_Screen.fxml"));
            Region root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();

        } catch(IllegalArgumentException | NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }
}
