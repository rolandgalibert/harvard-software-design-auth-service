/*
 * UnauthorizedAccessException
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

/**
 * This class is used to indicate that the user associated with a given access
 * token does not have permission to access a given operation.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class UnauthorizedAccessException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public UnauthorizedAccessException() {

    }
}
