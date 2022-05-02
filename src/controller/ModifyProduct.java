package controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class that modifies an existing product
 *
 * @author José L Dávila Montalvo
 */
public class ModifyProduct implements Initializable {

    /**
     * Table that shows all the available parts.
     */
    @FXML
    private TableView<Part> prodModTable;

    /**
     * Part ID column for the parts table.
     */
    @FXML
    private TableColumn<Part, Integer> modIdCol;

    /**
     * Part name column for the parts table.
     */
    @FXML
    private TableColumn<Part, String> modNameCol;

    /**
     * Part inventory column for the parts table.
     */
    @FXML
    private TableColumn<Part, Integer> modInvCol;

    /**
     * Part price column for the parts table.
     */
    @FXML
    private TableColumn<Part, Double> modPriceCol;

    /**
     * Table that shows all the associated parts.
     */
    @FXML
    private TableView<Part> modAscTbl;

    /**
     * Part ID column for the associated parts table.
     */
    @FXML
    private TableColumn<Part, Integer> modAscIdCol;

    /**
     * Part name column for the associated parts table.
     */
    @FXML
    private TableColumn<Part, String> modAscNameCol;

    /**
     * Part inventory column for the associated parts table.
     */
    @FXML
    private TableColumn<Part, Integer> modAscInvCol;

    /**
     * Part price column for the associated parts table.
     */
    @FXML
    private TableColumn<Part, Double> modAscPriceCol;

    /**
     * id textfield for product being modified.
     */
    @FXML
    private TextField prodModId;

    /**
     * name textfield for product being modified.
     */
    @FXML
    private TextField prodModName;

    /**
     * inventory textfield for product being modified.
     */
    @FXML
    private TextField prodModInv;

    /**
     * price textfield for product being modified.
     */
    @FXML
    private TextField prodModPrice;

    /**
     * max textfield for product being modified.
     */
    @FXML
    private TextField prodModMax;

    /**
     * min textfield for product being modified.
     */
    @FXML
    private TextField prodModMin;

    /**
     * searchfield for parts table.
     */
    @FXML
    private TextField prodModSearch;

    /**
     * product to be received from MainScreen controller
     */
    public static Product thisProduct;

    /**
     * Method populates the textfields using values sent by the MainScreen controller.
     *
     * @param id product id
     * @param name product name
     * @param inv   product inventory amount
     * @param price product price
     * @param max product max inventory amount
     * @param min product min inventory amount
     * @param associatedPart parts associated with product
     */
    public void setFields(int id, String name, int inv, double price, int max, int min, ObservableList associatedPart){

        prodModId.setText(Integer.toString(id));
        prodModName.setText(name);
        prodModInv.setText(Integer.toString(inv));
        prodModPrice.setText(Double.toString(price));
        prodModMax.setText(Integer.toString(max));
        prodModMin.setText(Integer.toString(min));
        modAscTbl.setItems(associatedPart);

    }

    /**
     * Method gets the product object sent by the MainScreen controller.
     *
     * @param product product object sent from MainScreen controller
     */
    public void getProduct(Product product){
        thisProduct = product;
    }

    /**
     * Initializes the controller. Contains logic to set the tableview and search the tableview.
     * Uses a filtered list class and a predicate to filter the list of part objects and then displays the found parts in the tableview.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // sets the values for each column
        modIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // sets the values for each column
        modAscIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modAscNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modAscInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modAscPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Used filtered list class to wrap the parts list returned from the Inventory class.
        FilteredList<Part> partFilteredList = new FilteredList<>(Inventory.getAllParts(), b -> true);

        // The logic inside the predicate filters the content of the tableview using ID or part name.
        prodModSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            partFilteredList.setPredicate(part -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                try {
                    if (Inventory.lookupPart(Integer.parseInt(newValue)).getId() == part.getId()) {
                        return true;
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    return false;
                } finally {
                    try{
                        String lowerNewValue = newValue.toLowerCase();
                        if (Inventory.lookupPart(lowerNewValue).contains(part)){
                            return true;
                        }
                    }catch (NullPointerException e){
                        return false;
                    }
                }
                return false;
            });
        });

        SortedList<Part> sortList = new SortedList<>(partFilteredList);
        sortList.comparatorProperty().bind(prodModTable.comparatorProperty());
        prodModTable.setItems(sortList);
    }

    /**
     * Cancel button takes the user back to the MainScreen, no products are modified.
     *
     * @param actionEvent cancel button press
     * @throws IOException Error loading the view
     */
    public void modCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1070,400);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Adds a selected part to the associated parts table.
     */
    public void modProdAdd() {

        try{
            modAscTbl.setItems(thisProduct.getAssociatedParts());
            Part part = prodModTable.getSelectionModel().getSelectedItem();

            if (part != null){
                Product.addToExistingProduct(thisProduct, part);
            }else{
                throw new NullPointerException();
            }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("Please select a part from the table");

            alert.showAndWait();
        }
    }

    /**
     * Saves the modified product with any associated parts.
     *
     * @param actionEvent save button press
     * @throws IOException Error loading the view
     */
    public void saveModProd(ActionEvent actionEvent) throws IOException {

        try{
            // if-else statements validate user input and throw detailed exceptions specific to the invalid entry.
            if(prodModName.getText().isEmpty()){
                throw new NullPointerException("Please enter a product name in the Name field");
            }

            // used regex to validate if anything other than an integer is entered.
            // since price is a double I excluded the . character to allow decimals
            if (prodModPrice.getText().matches(".*[^0-9.].*") || prodModPrice.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Price/Cost field");
            }

            if (prodModMax.getText().matches(".*\\D.*") || prodModMax.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Max field");
            }

            if (prodModMin.getText().matches(".*\\D.*") || prodModMin.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Min field");
            }else if(Integer.parseInt(prodModMin.getText()) > Integer.parseInt(prodModMax.getText())){
                throw new IllegalArgumentException("Min cannot be greater than max");
            }

            // second else if statement validates correct inventory number
            if (prodModInv.getText().matches(".*\\D.*") || prodModInv.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Inv field");
            }else if(Integer.parseInt(prodModInv.getText()) > Integer.parseInt(prodModMax.getText()) || Integer.parseInt(prodModInv.getText()) < Integer.parseInt(prodModMin.getText())){
                throw new IllegalArgumentException("Inventory number entered is outside the min/max range");
            }

            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main_Screen.fxml"));
            Region root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);

            ObservableList<Product> productList = Inventory.getAllProducts();

            int index = productList.indexOf(thisProduct);

            Inventory.updateProduct(index, thisProduct, prodModName.getText(), Double.parseDouble(prodModPrice.getText()),
                    Integer.parseInt(prodModInv.getText()),Integer.parseInt(prodModMin.getText()), Integer.parseInt(prodModMax.getText()), thisProduct.getAssociatedParts());
            stage.show();

            Product.clearAssociatedParts();

        } catch (NullPointerException | IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    /**
     * Removes selected part from the associated parts table.
     */
    public void removeAscPart() {
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("The selected part will be deleted.");
            alert.setContentText("Would you like to delete this part?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Part ascPart = modAscTbl.getSelectionModel().getSelectedItem();
                Product.deleteAssociatedPart(thisProduct, ascPart);
            } else {
                alert.close();
            }

        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("Please select a part from the table");

            alert.showAndWait();
        }
    }
}
