/*
 * RenterNotFoundException
 * 
 * Version 1.0
 * 
 * October 29, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #3.
 *
 */
package cscie97.asn4.squaredesk.renter;

/**
 * This class is used to indicate a specified Renter does not exist in the
 * set of active Renter objects.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class RenterNotFoundException extends RenterAPIException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public RenterNotFoundException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public RenterNotFoundException(String message) {
    }
}
