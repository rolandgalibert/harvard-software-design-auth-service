/*
 * OfficeSpaceNotAssignedToProviderException
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
 * This class is used to indicate a specified OfficeSpace is not assigned to a
 * certain Provider.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class OfficeSpaceNotAssignedToProviderException extends OfficeProviderServiceAPIException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public OfficeSpaceNotAssignedToProviderException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public OfficeSpaceNotAssignedToProviderException(String message) {
    }
}
