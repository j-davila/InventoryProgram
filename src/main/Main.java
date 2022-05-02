package main;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

// Javadoc folder located in the project folder.

/**
 * Main entry point of the program before passing onto the MainScreen controller.
 * Creates an inventory system app where the user can manage parts and products.
 *
 * <p><b>
 * Future Enhancement
 * </b></p>
 * <p>
 * Allowing the user to add multiple parts or products at the same time. Allowing the user to search the Associated Parts table.
 * </p>
 *
 * <p><b>
 * Runtime Error
 * </b></p>
 * <p>
 *  I received a runtime error in the MainScreen controller when I selected an outsourced part object from the table and I tried to access the Inhouse modification screen.
 *  I also received the same error selecting an Inhouse part and trying to access the outsourced part screen.
 *  I fixed this error using the "instance of" operator to identify if the part is Inhouse or Outsourced. The code Identifies the type of
 *  part and opens up the appropriate screen.
 * </p>
 *
 * @author José L Dávila Montalvo
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main_Screen.fxml"));
        stage.setTitle("");
        stage.setScene(new Scene(root, 1070, 400));
        stage.show();
    }

    /**
     * Main method used to manually add part objects and product objects for testing
     */
    public static void main(String[] args) {

        InHouse part1 = new InHouse(Inventory.incrementPartId(), "Garret Turbo", 500.00, 5, 1, 10, 44567);
        Inventory.addPart(part1);
        InHouse part2 = new InHouse(Inventory.incrementPartId(), "1JZ", 10000, 3, 1, 4, 44568);
        Inventory.addPart(part2);
        InHouse part3 = new InHouse(Inventory.incrementPartId(), "HKS Springs", 1500.00, 6, 1, 10, 44569);
        Inventory.addPart(part3);
        OutSourced part4 = new OutSourced(Inventory.incrementPartId(), "AWE Catback", 700.00, 9, 1, 10, "AWE");
        Inventory.addPart(part4);
        OutSourced part5 = new OutSourced(Inventory.incrementPartId(), "S20", 15000.00, 1, 1, 10, "Nissan");
        Inventory.addPart(part5);

        ObservableList<Part> testList1;

        Product.addAssociatedPart(part1);

        testList1 = Product.getAllAssociatedParts();

        Product product1 = new Product(Inventory.incrementProdId(), "S15", 45000, 2, 1, 4, testList1);
        Inventory.addProduct(product1);
        Product product2 = new Product(Inventory.incrementProdId(), "GTI", 29000, 5, 1, 8, testList1);
        Inventory.addProduct(product2);
        Product product3 = new Product(Inventory.incrementProdId(), "R32", 60000, 1, 1, 3, testList1);
        Inventory.addProduct(product3);

        Product.clearAssociatedParts();

        launch(args);
    }
}


