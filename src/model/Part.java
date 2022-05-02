package model;

/**
* Supplied class Part.java
 */

/**
 *This is the super class that contains the constructor, getters, and setters for Part objects.
 * A Part object cannot be created from this class and can only be created by the classes that inherit from Part. The classes that inherit
 * from Part are Inhouse and Outsourced.
 *
 * @author José L Dávila Montalvo
 */
public abstract class Part {

    /**
     *Private properties of a Part object.
     */
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     *This is the constructor for Part objects. Each part has a id, name, price, stock, min, and max.
     *
     * @param id    Id number of the part object.
     * @param name  Name of the part object.
     * @param price Price of the part object.
     * @param stock Quantity of the part object in inventory.
     * @param min   Minimum quantity of the part object in inventory.
     * @param max   Maximum quantity of the part object in inventory.
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
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