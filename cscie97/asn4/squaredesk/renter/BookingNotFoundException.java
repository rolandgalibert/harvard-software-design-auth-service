/*
 * BookingNotFoundException
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
 * This class is used to indicate a specified Booking does not exist in the
 * set of active Booking objects.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class BookingNotFoundException extends SchedulingServiceException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public BookingNotFoundException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public BookingNotFoundException(String message) {
    }
}
