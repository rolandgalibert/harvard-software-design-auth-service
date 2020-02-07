/*
 * Permission
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

import java.util.UUID;

/**
 * This class implements the Entitlement interface and provides properties and 
 * methods to describe permission for accessing a restricted SquareDesk
 * method.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Permission implements Entitlement, AuthServiceVisitorElement {

    /**
     * ID assigned to given Permission object (immutable after initial creation).
     */
    private String ID = new String();
    
    /**
     * Name assigned to given Permission object (immutable after initial creation).
     */
    private String name = new String();
    
    /**
     * Description of given Permission object.
     */
    private String description = new String();

    /**
     * Constructor.
     * 
     * @param ID Permission object ID.
     * @param name Permission object name.
     * @param description Permission object description.
     */
    public Permission(String ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
    }

    /**
     * Returns ID assigned to Permission object.
     * 
     * @return Permission object ID.
     */
    @Override
    public String getID() {
        return ID;
    }

    /**
     * Returns name assigned to Permission object.
     * 
     * @return Permission object name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns description assigned to Permission object.
     * 
     * @return Permission object description.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Updates Permission object description.
     * 
     * @param description New description of Permission object.
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Method to accept an AuthServiceVisitor object.
     * 
     * @param visitor AuthServiceVisitor object.
     */
    @Override
    public void acceptVisitor(AuthServiceVisitor visitor) {
        visitor.visitPermission(this);
    }
}
