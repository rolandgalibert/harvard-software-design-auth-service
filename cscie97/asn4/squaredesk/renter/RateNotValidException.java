/*
 * RateNotValidException
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
 * This class is used to indicate that the rate specified in a given Booking object
 * is invalid for some reason (Renter rate does not meet Provider rate, etc.)
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class RateNotValidException extends RenterAPIException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public RateNotValidException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public RateNotValidException(String message) {
    }
}
