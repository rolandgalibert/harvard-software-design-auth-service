/*
 * RentalPeriodFactory
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

import java.util.HashMap;
import java.util.Map;

/**
 * Javadoc comments for class.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class RentalPeriodFactory {

    /**
     * Private collection of all rental period types (e.g. day, week, etc.).
     */
    private static HashMap<String, String> rentalPeriods = new HashMap<String, String>();
    
    /**
     * Returns the rental period associated with the input descriptor and
     * also creates this string descriptor if it was not found in the map.
     * 
     * @param description Description of desired rental period.
     * @return String associated with input descriptor.
     */
    public static String lookup(String description) {
        if (rentalPeriods.get(description) == null) {
            rentalPeriods.put(description, description);
        }
        return rentalPeriods.get(description);
    }
    
    /**
     * Private constructor to ensure singleton object.
     */    
    private RentalPeriodFactory() {
    }
}
