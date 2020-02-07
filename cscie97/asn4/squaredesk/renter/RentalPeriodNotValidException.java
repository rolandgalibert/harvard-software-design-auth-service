/*
 * RentalPeriodNotValidException
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
 * This class is used to indicate that the rental period specified in a given
 * Booking object is invalid for some reason (start date greater than end date, etc.)
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class RentalPeriodNotValidException extends RenterAPIException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public RentalPeriodNotValidException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public RentalPeriodNotValidException(String message) {
    }
}
