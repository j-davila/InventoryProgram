package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;


/**
 * Inventory class that depends on the Part and Product classes.
 * This class handles two separate lists, one for all part objects created in the program and another for all product objects created in the program. These lists are
 * displayed in two separate tables inside the main screen of the program.
 * <p>
 * The class has multiple methods that perform the following:
 * Retrieving all objects in a list.
 * Auto-generating an id for a part object or a product object.
 * Adding an object to a list.
 * Retrieving an object or objects from a list.
 * Updating objects in a list.
 * Deleting objects in a list.
 *</p>
 *
 * @author José L Dávila Montalvo
 */
public class Inventory {

    /**
     * Private attributes for the Inventory class
     *
     * Lists contain all the parts or all the products created in the program.
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @return allParts list.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return allProducts list.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Method automatically generates an id number for the part.
     *
     * @return the part id
     */
    public static int incrementPartId(){
        int id;

        if(allParts.size() >= 1){
            int number = allParts.size() - 1;
            Part lastPart = allParts.get(number);
            int lastPartId = lastPart.getId();
            id = lastPartId + 1;
        }else{
            id = 1;
        }
        return id;
    }

    /**
     * Method automatically generates an id number for the product.
     *
     * @return the product id
     */
    public static int incrementProdId(){
        int id;

        if(allProducts.size() >= 1){
            int number = allProducts.size() - 1;
            Product lastProd = allProducts.get(number);
            int lastProdId = lastProd.getId();
            id = lastProdId + 1;
        }else{
            id = 1;
        }
        return id;
    }

    /**
     * Method adds a part to the allParts list.
     *
     * @param part  Part object to be added
     */
    public static void addPart(Part part){
        allParts.add(part);
    }

    /**
     * Method adds a product to the allProducts list.
     *
     * @param product  Product object to be added
     */
    public static void addProduct(Product product){
        allProducts.add(product);
    }

    /**
     *Method looks for a part in the allParts list by id number.
     *
     * @param partId Id of the part being searched for.
     * @return thisPart if thisPart.getId() matches partId or null if no part is found
     * @throws NumberFormatException if a string is entered instead of an int.
     * @throws NullPointerException when null is returned.
     */
    public static Part lookupPart(int partId) throws NumberFormatException, NullPointerException{
        ObservableList<Part> tempParts = Inventory.getAllParts();
        for(int i = 0; i < tempParts.size(); i++){
            Part thisPart = tempParts.get(i);
            if(thisPart.getId() == partId){
                return thisPart;
            }
        }
        return null;
    }

    /**
     * Method returns a list of parts that match the string entered.
     *
     * @param partName Partial or complete name of the part being searched for.
     * @return returnList Observablelist of parts that match the string entered.
     */
    public static ObservableList<Part> lookupPart(String partName){

        ObservableList<Part> tempAllParts = Inventory.getAllParts();
        ObservableList<Part> returnList = FXCollections.observableArrayList();
        for(Part thisPart: tempAllParts){
            if(thisPart.getName().toLowerCase(Locale.ROOT).contains(partName)){
                returnList.add(thisPart);
            }
        }
        return FXCollections.observableArrayList(returnList);
    }

    /**
     *Method looks for a product in the allProducts list by id number.
     *
     * @param productId Id of the product being searched for.
     * @return thisProduct if thisProduct.getId() matches productId or null if no part is found.
     * @throws NumberFormatException if a string is entered instead of an int.
     * @throws NullPointerException when null is returned.
     */
    public static Product lookupProduct(int productId) throws NumberFormatException, NullPointerException{
        ObservableList<Product> tempProduct = Inventory.getAllProducts();
        for(int i = 0; i < tempProduct.size(); i++){
            Product thisProduct = tempProduct.get(i);
            if(thisProduct.getId() == productId){
                return thisProduct;
            }
        }
        return null;
    }

    /**
     * Method returns a list of parts that match the string entered.
     *
     * @param productName Partial or complete name of the product being searched for.
     * @return returnList Observablelist of products that match the string entered.
     */
    public static ObservableList<Product> lookupProduct(String productName){

        ObservableList<Product> tempAllProducts = Inventory.getAllProducts();
        ObservableList<Product> returnList = FXCollections.observableArrayList();
        for(Product thisProduct: tempAllProducts){
            if(thisProduct.getName().toLowerCase(Locale.ROOT).contains(productName)){
                returnList.add(thisProduct);
            }
        }
        return FXCollections.observableArrayList(returnList);
    }

    /**
     *Method updates an existing inhouse part and may replace data.
     *
     * @param index List index of the part being updated.
     * @param selectedPart  Part object that will be updated.
     * @param name  Name for the part.
     * @param price Price for the part.
     * @param stock Inventory number for the part.
     * @param min   Minimum amount of inventory for the part.
     * @param max   Maximum amount of inventory for the part.
     * @param machineId Id of the machine that created the part.
     */
    public static void updateInhousePart(int index, Part selectedPart, String name, Double price, int stock, int min, int max, int machineId){

        for(Part thisPart: allParts){
            if(thisPart.getId() == selectedPart.getId()  ){
                thisPart.setName(name);
                thisPart.setPrice(price);
                thisPart.setStock(stock);
                thisPart.setMin(min);
                thisPart.setMax(max);
                ((InHouse) thisPart).setMachineId(machineId);
            }
            allParts.set(index, selectedPart);
        }
    }

    /**
     *Method updates an existing outsourced part and may replace data.
     *
     * @param index List index of the part being updated.
     * @param selectedPart  Part object that will be updated.
     * @param name  Name for the part.
     * @param price Price for the part.
     * @param stock Inventory number for the part.
     * @param min   Minimum amount of inventory for the part.
     * @param max   Maximum amount of inventory for the part.
     * @param companyName Name of the company where the part comes from.
     */
    public static void updateOutsourcedPart(int index, Part selectedPart, String name, Double price, int stock, int min, int max, String companyName){

        for(Part thisPart: allParts){
            if(thisPart.getId() == selectedPart.getId()  ){
                thisPart.setName(name);
                thisPart.setPrice(price);
                thisPart.setStock(stock);
                thisPart.setMin(min);
                thisPart.setMax(max);
                ((OutSourced) thisPart).setCompanyName(companyName);
            }
            allParts.set(index, selectedPart);
        }
    }

    /**
     *Method updates an existing product and may replace data.
     *
     * @param index List index of the product being updated.
     * @param selectedProduct  Product object that will be updated.
     * @param name  Name for the product.
     * @param price Price for the product.
     * @param stock Inventory number for the product.
     * @param min   Minimum amount of inventory for the product.
     * @param max   Maximum amount of inventory for the product.
     * @param associatedPart List of parts associated with the product.
     */
    public static void updateProduct(int index, Product selectedProduct, String name, Double price, int stock, int min, int max, ObservableList associatedPart){

        for(Product thisProduct: allProducts){
            if(thisProduct.getId() == selectedProduct.getId()  ){
                thisProduct.setName(name);
                thisProduct.setPrice(price);
                thisProduct.setStock(stock);
                thisProduct.setMin(min);
                thisProduct.setMax(max);
                thisProduct.setAssociatedParts(associatedPart);
            }
            allProducts.set(index, selectedProduct);
        }
    }

    /**
     *Method deletes a selected part object from the allParts list.
     *
     * @param selectedPart Part object to be deleted.
     * @return allParts.remove(thisPart) If true, the part is deleted or false if it is not deleted.
     */
    public static boolean deletePart(Part selectedPart){
        for(Part thisPart: allParts){
            if(thisPart.getId() == selectedPart.getId()){
                return allParts.remove(thisPart);
            }
        }
        return false;
    }

    /**
     *Method deletes a selected product object from the allProducts list.
     *
     * @param selectedProduct Product object to be deleted.
     * @return allProducts.remove(thisProduct) If true, the product is deleted or false Product is not deleted.
     */
    public static boolean deleteProduct(Product selectedProduct){
        for(Product thisProduct: allProducts){
            if(thisProduct.getId() == selectedProduct.getId()){
                return allProducts.remove(thisProduct);
            }
        }
        return false;
    }
}
