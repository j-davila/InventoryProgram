package model;

/**
 *InHouse class that inherits from, or extends, the Part class.
 *This class creates a Part object that is sourced inhouse and is accompanied by a machine ID number.
 *
 *@author José L Dávila Montalvo
 */
public class InHouse extends Part{

    /**
     * Private property of an Inhouse Part object.
     */
    private int machineId;

    /**
     *Constructor for Inhouse Part objects. Inherits attributes from Part parent class and adds machine ID attribute.
     *
     *@param id         Id number of the Inhouse part object.
     *@param name       Name of the Inhouse part object.
     *@param price      Price of the Inhouse part object.
     *@param stock      Quantity of the Inhouse part object in inventory.
     *@param min        Minimum quantity of the Inhouse part object in inventory.
     *@param max        Maximum quantity of the Inhouse part object in inventory.
     *@param machineId  Id of the machine that created the part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @param machineId The machineId to set.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the machineId
     */
    public int getMachineId(){
        return machineId;
    }
}
