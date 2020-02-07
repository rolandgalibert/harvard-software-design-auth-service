/*
 * ImportException
 * 
 * Version 1.0
 * 
 * September 16, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #1.
 */
package cscie97.asn4.knowledge.engine;

/**
 * This class describes exceptions that occur when importing a triple file
 * (see assignment design document for more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class ImportException extends Exception {
    
    private static final long serialVersionUID = 1L;
    /**
     * No-argument constructor.
     */
    public ImportException() {
    }
    
    /**
     * Constructor with message describing exception.
     * 
     * @param message Exception description.
     */
    public ImportException(String message) {
        super(message);
    }
}
