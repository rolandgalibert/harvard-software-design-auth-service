/*
 * RenterAPIException
 * 
 * Version 1.0
 * 
 * Oct 29, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #3.
 *
 */
package cscie97.asn4.squaredesk.renter;

/**
 * This class is used to indicate an exception thrown by the UserAPI.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class RenterAPIException extends Exception {

    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public RenterAPIException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public RenterAPIException(String message) {
    }
}
