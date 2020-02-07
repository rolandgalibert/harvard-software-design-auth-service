/*
 * ContactMethodFactory
 * 
 * Version 1.0
 * 
 * Oct 5, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #2.
 *
 */
package cscie97.asn4.squaredesk.provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Static class for maintaining list of all contact methods. Flyweight
 * pattern is implemented to conserve space required by the program.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class ContactMethodFactory {

    /**
     * Private collection of all features.
     */
    private static HashMap<String, String> contactMethods = new HashMap<String, String>();
    
    /**
     * Returns the contact method string associated with the input descriptor,
     * and also creates this object if it was not found in the map.
     * 
     * @param description Contact method description.
     * @return Contact method string associated with input descriptor.
     */
    public static String lookup(String description) {
        if (contactMethods.get(description) == null) {
            contactMethods.put(description, description);
        }
        return contactMethods.get(description);
    }
    
    /**
     * Private constructor to ensure singleton object.
     */    
    private ContactMethodFactory() {
    }
}
