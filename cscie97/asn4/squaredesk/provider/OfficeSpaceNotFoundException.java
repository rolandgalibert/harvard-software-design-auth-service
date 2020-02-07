/*
 * OfficeSpaceNotFoundException
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
 * This class is used to indicate a specified OfficeSpace does not exist in the 
 * set of active OfficeSpace objects.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class OfficeSpaceNotFoundException extends OfficeProviderServiceAPIException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public OfficeSpaceNotFoundException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public OfficeSpaceNotFoundException(String message) {
    }
}
