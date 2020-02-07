/*
 * ProviderAccountNotFoundException
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
 * This class is used to indicate that no Account information has been provided
 * for a specified Provider.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class ProviderAccountNotFoundException extends ProviderAPIException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * No-argument constructor.
     */
    public ProviderAccountNotFoundException() {
    }

    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public ProviderAccountNotFoundException(String message) {
    }
}
