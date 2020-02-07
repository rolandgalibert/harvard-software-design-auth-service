/*
 * BadOfficeSpaceCapacityException
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
 * This class is used to indicate that a given OfficeSpace object has errors
 * in the capacity information contained in its Capacity object.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class BadOfficeSpaceCapacityException extends OfficeSpaceAPIException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public BadOfficeSpaceCapacityException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public BadOfficeSpaceCapacityException(String message) {
    }
}
