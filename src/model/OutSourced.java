package model;

/**
 *Outsourced class that inherits from, or extends, the Part class.
 *This class creates a Part object that is outsourced from a 3rd party and is accompanied by the 3rd party company's name.
 *
 *@author José L Dávila Montalvo
 */
public class OutSourced extends Part{

    /**
     * Name of the company where the part is sourced from.
     */
    private String companyName;

    /**
     *Constructor for Outsourced Part objects. Inherits attributes from Part parent class and adds company name attribute.
     *
     *@param id             Id number of the Outsourced part object.
     *@param name           Name of the Outsourced part object.
     *@param price          Price of the Outsourced part object.
     *@param stock          Quantity of the Outsourced part object in inventory.
     *@param min            Minimum quantity of the Outsourced part object in inventory.
     *@param max            Maximum quantity of the Outsourced part object in inventory.
     *@param companyName    Name of the company where the part is sourced from.
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);

        this.companyName = companyName;
    }

    /**
     * @param companyName the companyName to set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
