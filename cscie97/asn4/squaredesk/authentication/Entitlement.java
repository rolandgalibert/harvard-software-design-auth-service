/*
 * Entitlement
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
 * This interface defines the properties and methods that must be implemented 
 * by those classes used to define SquareDesk AuthService entitlements
 * (specifically Permission and Role classes). This will allow those objects
 * to be defined and implemented on the basis of a composite design pattern.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public interface Entitlement {
    
    /**
     * Returns the ID associated with the Entitlement object.
     * 
     * @return Entitlement object ID.
     */
    public String getID();
    
    /**
     * Returns the name associated with the Entitlement object.
     * 
     * @return Entitlement object name.
     */
    public String getName();
    
    /**
     * Returns the description associated with the Entitlement object.
     * 
     * @return Entitlement object description.
     */

    public String getDescription();
    
    /**
     * Updates the description of the Entitlement object.
     * @param description New Entitlement object description.
     */
    public void setDescription(String description);
}
