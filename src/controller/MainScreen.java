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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main controller class, point of entry for user.
 * User can:
 * search part and product tables
 * add a part or product
 * modify a part or product
 * delete a part or product
 *
 * @author José L Dávila Montalvo
 */
public class MainScreen implements Initializable {

    /**
     * Table that shows all the available products.
     */
    @FXML
    private TableView<Product> prodTableView;

    /**
     * product ID column for the product table.
     */
    @FXML
    private TableColumn<Product, Integer> prodIdCol;

    /**
     * product name column for the product table.
     */
    @FXML
    private TableColumn<Product, String> prodNameCol;

    /**
     * product inventory column for the product table.
     */
    @FXML
    private TableColumn<Product, Integer> prodInvCol;

    /**
     * product price column for the product table.
     */
    @FXML
    private TableColumn<Product, Double> prodPriceCol;

    /**
     * searchfield for the product table.
     */
    @FXML
    private TextField productSearch;

    /**
     * searchfield for the part table.
     */
    @FXML
    private TextField partsSearch;

    /**
     * Table that shows all the available parts.
     */
    @FXML
    private TableView<Part> partsTableView;

    /**
     * part ID column for the part table.
     */
    @FXML
    private TableColumn<Part, Integer> partIdCol;

    /**
     * part name column for the part table.
     */
    @FXML
    private TableColumn<Part, String> partNameCol;

    /**
     * part inventory column for the part table.
     */
    @FXML
    private TableColumn<Part, Integer> partInventoryCol;

    /**
     * part price column for the part table.
     */
    @FXML
    private TableColumn<Part, Double> partPriceCol;

    /**
     * Initializes the controller. Contains logic to set the tableview and search the tableview.
     * Uses a filtered list class and a predicate to filter the list of part objects and then displays the found parts in the tableview.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // sets the values for each column
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // sets the values for each column
        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Used filtered list class to wrap the parts list returned from the Inventory class.
        FilteredList<Part> filterTest = new FilteredList<>(Inventory.getAllParts(), b -> true);

        // The logic inside the predicate filters the content of the tableview using ID or part name.
        partsSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTest.setPredicate(part -> {
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
        SortedList<Part> sortTest = new SortedList<>(filterTest);
        sortTest.comparatorProperty().bind(partsTableView.comparatorProperty());
        partsTableView.setItems(sortTest);

        // Used filtered list class to wrap the products list returned from the Inventory class.
        FilteredList<Product> prodFilterTest = new FilteredList<>(Inventory.getAllProducts(), b -> true);

        // The logic inside the predicate filters the content of the tableview using ID or part name.
        productSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            prodFilterTest.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                try {
                    if (Inventory.lookupProduct(Integer.parseInt(newValue)).getId() == product.getId()) {
                        return true;
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    return false;
                } finally {
                    try{
                        String lowerNewValue = newValue.toLowerCase();
                        if (Inventory.lookupProduct(lowerNewValue).contains(product)){
                            return true;
                        }
                    }catch (NullPointerException e){
                        return false;
                    }
                }
                return false;
            });
        });
        SortedList<Product> prodSortTest = new SortedList<>(prodFilterTest);
        prodSortTest.comparatorProperty().bind(prodTableView.comparatorProperty());
        prodTableView.setItems(prodSortTest);
    }

    /**
     * Takes the user to the add part scene, default is Inhouse.
     *
     * @param actionEvent add button press
     * @throws IOException Error loading the view
     */
    public void partsAddButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Parts_Screen_InHouse.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,549,653);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * User selects a part to modify and it takes the use to the part modification screen.
     * The information from the selected part gets sent to the other controller.
     * If the user selects an Inhouse part, they will be sent to the Inhouse screen. If the user selects an Outsourced part,
     * they will be sent to the outsourced screen.
     *
     * @param actionEvent modify button press
     * @throws IOException Error loading the view
     */
    public void partsModifyBtn(ActionEvent actionEvent) throws IOException {

        try{
            // Every attribute of the part, including the part itself, is stored in a variable.
            Part part = partsTableView.getSelectionModel().getSelectedItem();
            int id = partsTableView.getSelectionModel().getSelectedItem().getId();
            String name = partsTableView.getSelectionModel().getSelectedItem().getName();
            int inv = partsTableView.getSelectionModel().getSelectedItem().getStock();
            Double price = partsTableView.getSelectionModel().getSelectedItem().getPrice();
            int max = partsTableView.getSelectionModel().getSelectedItem().getMax();
            int min = partsTableView.getSelectionModel().getSelectedItem().getMin();

            // selection structure to check if the part is Inhouse or Outsourced
            if(part instanceof InHouse){

                Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Parts_ScreenMod_InHouse.fxml"));
                Region root = loader.load();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                InHouse mach = (InHouse) partsTableView.getSelectionModel().getSelectedItem();
                int machId = mach.getMachineId();
                PartsScreenModInHouse partsScreenModInHouse = loader.getController();
                partsScreenModInHouse.setFields(id,name,inv, price, max, min, machId);
                partsScreenModInHouse.getPart(part);

                stage.show();

            } else{

                Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Parts_ScreenMod_Outsourced.fxml"));
                Region root = loader.load();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                OutSourced thisCompanyName = (OutSourced) partsTableView.getSelectionModel().getSelectedItem();
                String companyName = thisCompanyName.getCompanyName();

                PartsScreenModOutsourced partsScreenModOutsourced = loader.getController();
                partsScreenModOutsourced.setFields(id,name,inv, price, max, min, companyName);
                partsScreenModOutsourced.getPart(part);

                stage.show();
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
     * Button deletes the selected part.
     * User is prompted for confirmation before the part is deleted.
     */
    public void partsDeleteBtn() {

        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("The selected part will be deleted.");
            alert.setContentText("Would you like to delete this part?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Part testPart = partsTableView.getSelectionModel().getSelectedItem();
                Inventory.deletePart(testPart);
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

    /**
     * takes the user to the add product screen
     *
     * @param actionEvent add button press
     * @throws IOException Error loading the view
     */
    public void prdAddBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Add_Product.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1114,566);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * User selects a product to modify and it takes the use to the product modification screen.
     * The information from the selected product gets sent to the other controller.
     *
     * @param actionEvent modify button press
     * @throws IOException Error loading the view
     */
    public void prdModifyBtn(ActionEvent actionEvent) throws IOException {

        try{
            Product product = prodTableView.getSelectionModel().getSelectedItem();
            int id = prodTableView.getSelectionModel().getSelectedItem().getId();
            String name = prodTableView.getSelectionModel().getSelectedItem().getName();
            int inv = prodTableView.getSelectionModel().getSelectedItem().getStock();
            Double price = prodTableView.getSelectionModel().getSelectedItem().getPrice();
            int max = prodTableView.getSelectionModel().getSelectedItem().getMax();
            int min = prodTableView.getSelectionModel().getSelectedItem().getMin();
            ObservableList prodList = prodTableView.getSelectionModel().getSelectedItem().getAssociatedParts();

            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Modify_Product.fxml"));
            Region root = loader.load();

            ModifyProduct modifyProduct = loader.getController();
            modifyProduct.setFields(id,name,inv, price, max, min, prodList);
            modifyProduct.getProduct(product);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();

        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("Please select a product from the table");

            alert.showAndWait();
        }
    }

    /**
     * Prompts the user to delete the selected product. If the product has associated parts, the user will be advised the product cannot be deleted.
     */
    public void prdDeletBtn() {

        try{
            Product product = prodTableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("The selected product will be deleted.");
            alert.setContentText("Would you like to delete this product?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                if(product.getAssociatedParts().isEmpty()){
                    Inventory.deleteProduct(product);
                }else{
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Error");
                    alert1.setHeaderText("Deletion Error");
                    alert1.setContentText("Product has parts associated to it and cannot be deleted");

                    alert1.showAndWait();
                }
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

    /**
     * Exits the program
     */
    public void exitBtn() {
        System.exit(0);
    }
}
