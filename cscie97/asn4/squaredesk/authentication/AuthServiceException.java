/*
 * AuthServiceException
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
 * This class is used to indicate a general exception thrown by the SquareDesk
 * AuthService.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class AuthServiceException extends Exception {
    
    private static final long serialVersionUID = 1L;
    /**
     * No-argument constructor.
     */
    public AuthServiceException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public AuthServiceException(String message) {
    }
}
