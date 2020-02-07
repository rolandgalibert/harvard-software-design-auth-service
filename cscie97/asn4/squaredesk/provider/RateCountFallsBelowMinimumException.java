/*
 * RateCountFallsBelowMinimumException
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
 * This class is used to indicate the Rate object deletion client is attempting
 * would cause the given OfficeSpace object to have no rates associated with it.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class RateCountFallsBelowMinimumException extends OfficeSpaceAPIException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public RateCountFallsBelowMinimumException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public RateCountFallsBelowMinimumException(String message) {
    }
}
