/*
 * CommonAccessFactory
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
 * Static class for maintaining list of all OfficeSpace common access areas.
 * Flyweight pattern is implemented to conserve space required by the program.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class CommonAccessFactory {

    /**
     * Private collection of all common access areas.
     */
    private static HashMap<String, CommonAccess> commonAccessAreas = new HashMap<String, CommonAccess>();
    
    /**
     * Returns the CommonAccess object associated with the input descriptor and
     * also creates this object if it was not found in the map.
     * 
     * @param description Description of desired CommonAccess object.
     * @return CommonAccess object associated with input descriptor.
     */
    public static CommonAccess lookup(String description) {
        if (commonAccessAreas.get(description) == null) {
            commonAccessAreas.put(description, new CommonAccess(description));
        }
        return commonAccessAreas.get(description);
    }
 
    /**
     * Private constructor to ensure singleton object.
     */
    private CommonAccessFactory() {
    }
}
