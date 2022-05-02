package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class that creates product objects and handles the parts associated to the product.
 * Product objects have a list of parts that are associated to it.
 *
 * @author José L Dávila Montalvo
 */
public class Product {

    /**
     * Private properties for the Product class.
     */
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> ascParts;

    /**
     * Public attribute for the product class
     * The list, associatedParts, is the list used to temporarily store associated parts that are added to ascParts.
     */
    public static ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Constructor for Product objects. Each product has a id, name, price, stock, min, max, and a parts list.
     *
     * @param id    Id number of the product object.
     * @param name  Name of the product object.
     * @param price Price of the product object.
     * @param stock Quantity of the product object in inventory.
     * @param min   Minimum quantity of the product object in inventory.
     * @param max   Maximum quantity of the product object in inventory.
     * @param ascParts  List of parts that are associated with the product
     */
    public Product(int id, String name, double price, int stock, int min, int max, ObservableList ascParts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.ascParts = FXCollections.observableArrayList(ascParts);
    }

    /**
     * Method that will add a part to the associatedParts list.
     *
     * @param part Part object that will be added to the list.
     */
    public static void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Method adds a part object to an existing product object's associated parts list ascParts.
     *
     * @param product Selected product
     * @param part Selected part to be added to the associated parts list.
     */
    public static void addToExistingProduct(Product product, Part part){
        product.ascParts.add(part);
    }

    /**
     * Method clears the associatedParts list to prepare it for new part object input.
     */
    public static void clearAssociatedParts(){
            associatedParts.clear();
    }

    /**
     * @return the list of associatedParts
     */
    public static ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Method deletes a part object from the list.
     *
     * @param part Part object to be deleted.
     * @return associatedParts.remove(part) If true, the part is deleted from the list or false Part is not deleted.
     * */
    public static boolean deleteAssociatedPart(Part part){
        for(Part thisPart: associatedParts){
            if(thisPart.getId() == part.getId()) {
                return associatedParts.remove(part);
            }
        }
        return false;
    }

    /**
     * Method deletes a part object from the list for the specified product.
     *
     * @param product Selected product object.
     * @param part Part object to be deleted.
     * @return product.ascParts.remove(part) If true, the part is deleted from the list false Part is not deleted.
     * */
    public static boolean deleteAssociatedPart(Product product, Part part){
        for(Part thisPart: product.ascParts){
            if(thisPart.getId() == part.getId()) {
                return product.ascParts.remove(part);
            }
        }
        return false;
    }

    /**
     * @return the list of ascParts
     */
    public ObservableList<Part> getAssociatedParts() {
        return ascParts;
    }

    /**
     * @param ascParts the ascParts list to set
     */
    public void setAssociatedParts(ObservableList<Part> ascParts) {
        this.ascParts = ascParts;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }
}

