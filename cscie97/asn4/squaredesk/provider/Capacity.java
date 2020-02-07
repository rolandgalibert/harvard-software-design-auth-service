/*
 * Capacity
 * 
 * Version 1.0
 * 
 * Oct 4, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #2.
 *
 */
package cscie97.asn4.squaredesk.provider;

/**
 * This class allows for specification of the physical capacity of an
 * OfficeSpace object.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Capacity {

    /**
     * Maximum number of occupants object can contain.
     */
    private short maxOccupants = 1;

    /**
     * Maximum number of workspaces object has available.
     */
    private short workspaces = 1;
    
    /**
    * Physical size of object.
    */
    private float squareFootage;

    /**
     * No-argument constructor.
     */
    public Capacity() {
    }

    /**
     * Constructor allowing for specification of all Capacity parameters.
     * 
     * @param maxOccupants Maximum number of occupants object can contain.
     * @param workspaces Maximum number of workspaces object has available.
     * @param squareFootage Physical size of object.
     */
    public Capacity(short maxOccupants, short workspaces, float squareFootage) {
        this.maxOccupants = maxOccupants;
        this.workspaces = workspaces;
        this.squareFootage = squareFootage;
    }

    /**
     * Returns the maximum number of occupants set for the Capacity object.
     * 
     * @return Maximum number of occupants object can contain.
     */
    public short getMaxOccupants() {
        return maxOccupants;
    }

    /**
     * Sets the maximum number of occupants for the Capacity object.
     * 
     * @param maxOccupants Maximum number of occupants object can contain.
     */
    public void setMaxOccupants(short maxOccupants) {
        this.maxOccupants = maxOccupants;
    }

    /**
     * Returns the maximum number of workspaces set for the Capacity object.
     * 
     * @return Maximum number of workspaces object has available.
     */
    public short getWorkspaces() {
        return workspaces;
    }

    /**
     * Sets the maximum number of workspaces for the Capacity object.
     * 
     * @param workspaces Maximum number of workspaces object has available.
     */
    public void setWorkspaces(short workspaces) {
        this.workspaces = workspaces;
    }

    /**
     * Returns the amount of square footage set for the Capacity object.
     * 
     * @return Physical size of object.
     */
    public float getSquareFootage() {
        return squareFootage;
    }

    /**
     * Sets the amount of square footage for the Capacity object.
     * 
     * @param squareFootage Physical size of object.
     */
    public void setSquareFootage(float squareFootage) {
        this.squareFootage = squareFootage;
    }
}
