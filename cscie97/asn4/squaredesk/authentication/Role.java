/*
 * Role
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
import java.util.UUID;

/**
 * This class implements the Entitlement interface and provides properties and 
 * methods for describing user roles, with associated permissions, in the 
 * SquareDesk system.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Role implements Entitlement, AuthServiceVisitorElement {

    /**
     * ID assigned to given Role object (immutable after initial creation).
     */
    private String ID = new String();
    
    /**
     * Name assigned to given Role object (immutable after initial creation).
     */
    private String name = new String();
    
    /**
     * Description of given Role object.
     */
    private String description = new String();
    
    /**
     * Subroles assigned to Role object.
     */
    private ArrayList<String> subroles = new ArrayList<>();
    
    /**
     * Permissions associated with Role object.
     */
    private ArrayList<String> permissions = new ArrayList<>();

    /**
     * Constructor.
     * 
     * @param ID Permission object ID.
     * @param name Permission object name.
     * @param description Permission object description.
     */
    public Role(String ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    /**
     * Returns ID assigned to Role object.
     * 
     * @return Role object ID.
     */
    @Override
    public String getID() {
        return ID;
    }

    /**
     * Returns name assigned to Role object.
     * 
     * @return Role object name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns description assigned to Role object.
     * 
     * @return Role object description.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Updates Role object description.
     * 
     * @param description New description of Role object.
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Returns subroles associated with Role object.
     * 
     * @return List of subroles.
     */
    public ArrayList<String> getSubroles() {
        return subroles;
    }

    /**
     * Adds a subrole to the list of subroles associated with the Role
     * object.
     * 
     * @param subroleID ID of subrole being added.
     */
    public void addSubrole(String subroleID) {
        subroles.add(subroleID);
    }
    
    /**
     * Removes a subrole from the list of subroles associated with the
     * Role object.
     * 
     * @param subroleID ID of subrole being removed.
     */
    public void removeSubrole(String subroleID) {
        subroles.remove(subroleID);
    }
    
    /**
     * Returns permissions associated with Role object.
     * 
     * @return List of permissions.
     */
    public ArrayList<String> getPermissions() {
        return permissions;
    }
    
    /**
     * Adds a permission to the list of permissions associated with the Role
     * object.
     * 
     * @param permissionID ID of permission being added.
     */
    public void addPermission(String permissionID) {
        permissions.add(permissionID);
    }
    
    /**
     * Removes a permission from the list of permissions associated with the
     * Role object.
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
        visitor.visitRole(this);
    }
}
