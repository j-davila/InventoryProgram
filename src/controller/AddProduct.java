package controller;

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
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class that allows the user to add products and its associated parts.
 *
 * @author José L Dávila Montalvo
 */
public class AddProduct implements Initializable {

    /**
     * Table that shows all the available parts.
     */
    @FXML
    private TableView<Part> prodPartsTable;

    /**
     * part id column for the parts table
     */
    @FXML
    private TableColumn<Part, Integer> partIdCol;

    /**
     * part name column for the parts table
     */
    @FXML
    private TableColumn<Part, String> partNameCol;

    /**
     * part inventory column for the parts table
     */
    @FXML
    private TableColumn<Part, Integer> partInvCol;

    /**
     * part price column for the parts table
     */
    @FXML
    private TableColumn<Part, Double> partPriceCol;

    /**
     * Table that shows all the associated parts.
     */
    @FXML
    private TableView<Part> assoPartTable;

    /**
     * part id column for the associated parts table
     */
    @FXML
    private TableColumn<Part, Integer> assoIdCol;

    /**
     * part name column for the associated parts table
     */
    @FXML
    private TableColumn<Part, String > assoNameCol;

    /**
     * part inventory column for the associated parts table
     */
    @FXML
    private TableColumn<Part,Integer> assoInvCol;

    /**
     * part price column for the associated parts table
     */
    @FXML
    private TableColumn<Part, Double> assoPriceCol;

    /**
     * Name textfield for the product
     */
    @FXML
    private TextField prodNameTxt;

    /**
     * Inventory textfield for the product
     */
    @FXML
    private TextField prodInvTxt;

    /**
     * Price textfield for the product
     */
    @FXML
    private TextField prodPriceTxt;

    /**
     * Max textfield for the product
     */
    @FXML
    private TextField prodMaxTxt;

    /**
     * Min textfield for the product
     */
    @FXML
    private TextField prodMinTxt;

    /**
     * Search field for the parts tableview
     */
    @FXML
    private TextField addProdSearch;

    /**
     * Initializes the controller. Contains logic to set the tableview and search the tableview.
     * Uses a filtered list class and a predicate to filter the list of part objects and then displays the found parts in the tableview.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        // sets the values for each column
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // sets the values for each column
        assoIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assoNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assoInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assoPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Used filtered list class to wrap the parts list returned from the Inventory class.
        FilteredList<Part> partFilteredList = new FilteredList<>(Inventory.getAllParts(), b -> true);

        // The logic inside the predicate filters the content of the tableview using ID or part name.
        addProdSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            partFilteredList.setPredicate(part -> {

                // shows all the parts available
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                try {
                    // uses the value entered in the searchfield with the lookupPart method. If the returned part object matches any of the part objects in the filtered list,
                    // it will display the part object that is a match.
                    if (Inventory.lookupPart(Integer.parseInt(newValue)).getId() == part.getId()) {
                        return true;
                    }
                    //error handler when a string is entered or when no part is found
                } catch (NumberFormatException | NullPointerException e) {
                    return false;
                } finally {
                    try{
                        String lowerNewValue = newValue.toLowerCase();
                        //searches for a part name entered
                        if (Inventory.lookupPart(lowerNewValue).contains(part)){
                            return true;
                        }
                        //error handler when no part is found
                    }catch (NullPointerException e){
                        return false;
                    }
                }
                return false;
            });
        });

        // wraps the filtered list converting it to a sorted list. Tableview is then updated in realtime based on the entered values in the searchfield.
        SortedList<Part> sortTest = new SortedList<>(partFilteredList);
        sortTest.comparatorProperty().bind(prodPartsTable.comparatorProperty());
        prodPartsTable.setItems(sortTest);

    }

    /**
     * Method adds a part object to the associated parts list and is displayed in the associated parts table.
     */
    public void addAssociatedPart() {

        try{
            Part part = prodPartsTable.getSelectionModel().getSelectedItem();

            if (part != null){
                Product.addAssociatedPart(part);
                assoPartTable.setItems(Product.getAllAssociatedParts());
            }else{
                throw new NullPointerException();
            }

            //error handler in case the button is pressed and no part is selected.
        }catch (NullPointerException e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Selection");
        alert.setContentText("Please select a part from the table");

        alert.showAndWait();
        }
    }

    /**
     * Method deletes a selected associated part object from the list and the table.
     */
    public void deleteAssociatedPart() {

        try{
            Part ascPart = assoPartTable.getSelectionModel().getSelectedItem();
            Product.deleteAssociatedPart(ascPart);

            //error handler in case the button is pressed and no part is selected.
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("Please select a part from the table");

            alert.showAndWait();
        }
    }

    /**
     * saves the product object created by the user.
     *
     * @param actionEvent save button press
     * @throws IOException Error loading the view
     */
    public void saveProduct(ActionEvent actionEvent) throws IOException {

        // automatically generates an ID for the product
        int id = Inventory.incrementProdId();

        try {

            int stock;
            Double price;
            int min;
            int max;
            String name;

            // if-else statements validate user input and throw detailed exceptions specific to the invalid entry.
            if(prodNameTxt.getText().isEmpty()){
                throw new NullPointerException("Please enter a product name in the Name field");
            }else{
                name = prodNameTxt.getText();
            }

            // if-else statements validate user input and throw detailed exceptions specific to the invalid entry.
            if (prodPriceTxt.getText().matches(".*[^0-9.].*") || prodPriceTxt.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Price/Cost field");
            } else {
                price = Double.parseDouble(prodPriceTxt.getText());
            }

            if (prodMaxTxt.getText().matches(".*\\D.*") || prodMaxTxt.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Max field");
            } else {
                max = Integer.parseInt(prodMaxTxt.getText());
            }

            if (prodMinTxt.getText().matches(".*\\D.*") || prodMinTxt.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Min field");
            }else if (Integer.parseInt(prodMinTxt.getText()) > max){
                throw new IllegalArgumentException("Min cannot be greater than max");
            } else {
                min = Integer.parseInt(prodMinTxt.getText());
            }

            // second else if statement validates correct inventory number
            if (prodInvTxt.getText().matches(".*\\D.*") || prodInvTxt.getText().isEmpty()) {
                throw new NumberFormatException("Please enter a number in the Inv field");
            }else if(Integer.parseInt(prodInvTxt.getText()) > max || Integer.parseInt(prodInvTxt.getText()) < min){
                throw new IllegalArgumentException("Inventory number entered is outside the min/max range");
            } else {
                stock = Integer.parseInt(prodInvTxt.getText());
            }

            // product is added
            Inventory.addProduct(new Product(id, name, price, stock, min, max, Product.getAllAssociatedParts()));

            // clears associated parts list for next product object
            Product.clearAssociatedParts();

            // takes user back to main screen.
            Parent root = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root,1070,400);
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

    /**
     * When the cancel button is pressed, nothing is saved and the user is taken back to the main screen.
     *
     * @param actionEvent cancel button press
     * @throws IOException Error loading the view
     */
    public void productCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1070,400);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }
}
