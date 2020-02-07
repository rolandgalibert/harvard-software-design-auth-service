/*
 * InvalidAccessTokenException
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
 * This class is used to indicate that a given access token is invalid for some
 * reason (e.g. it doesn't exist or has expired).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class InvalidAccessTokenException extends Exception {
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public InvalidAccessTokenException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public InvalidAccessTokenException(String message) {
    }
}
