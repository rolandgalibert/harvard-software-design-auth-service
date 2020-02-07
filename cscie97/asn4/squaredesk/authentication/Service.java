/*
 * Service
 * 
 * Version 1.0
 * 
 * November 20, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #4.
 *
 */
package cscie97.asn4.squaredesk.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * This class contains the necessary properties and methods for describing a
 * restricted AuthService service.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Service implements AuthServiceVisitorElement {
    
    /**
     * ID assigned to given Service object (immutable after initial creation).
     */    
    private String ID = new String();
    
    /**
     * Name assigned to given Service object (immutable after initial creation).
     */
    private String name = new String();
    
    /**
     * Description of given Service object.
     */
    private String description = new String();
    
    /**
     * Permissions (restricted methods) associated with Service object.
     */
    private ArrayList<String> permissions = new ArrayList<>();

    /**
     * Constructor.
     * 
     * @param ID Service object ID.
     * @param name Service object name.
     * @param description Service object description.
     */
    public Service(String ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    /**
     * Returns ID assigned to Service object.
     * 
     * @return Service object ID.
     */
    public String getID() {
        return ID;
    }

    /**
     * Returns name assigned to Service object.
     * 
     * @return Service object name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns description assigned to Service object.
     * 
     * @return Service object description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates Service object description.
     * 
     * @param description New description of Service object.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns permissions (restricted access methods) associated with
     * Service object.
     * 
     * @return List of permissions (restricted access methods).
     */
    public ArrayList<String> getPermissions() {
        return permissions;
    }
    
    /**
     * Adds a permission (restricted access method) to the list of permissions
     * associated with the Service object.
     * 
     * @param permissionID ID of permission being added.
     */
    public void addPermission(String permissionID) {
        permissions.add(permissionID);
    }

    /**
     * Removes a permission (restricted access method) from the list of
     * permissions associated with the Service object.
     * 
     * @param permissionID ID of permission being removed.
     */
    public void removePermission(String permissionID) {
        permissions.remove(permissionID);
    }
    
    /**
     * Method to accept an AuthServiceVisitor object.
     * 
     * @param visitor AuthServiceVisitor object.
     */
    @Override
    public void acceptVisitor(AuthServiceVisitor visitor) {
        visitor.visitService(this);
    }
}
