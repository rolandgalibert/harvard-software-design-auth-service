/*
 * FacilityTypeFactory
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
 * Static class for maintaining list of all FacilityTypes. Flyweight
 * pattern is implemented to conserve space required by the program.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class FacilityTypeFactory {

    /**
     * Private singleton collection of all facility type. This map is initialized
     * with the special types of "Home" and "Garage" the very first time it is
     * instantiated.
     */
    private static HashMap<String, FacilityType> facilityTypes = new HashMap<String, FacilityType>();
    static {
        facilityTypes.put("Home", new FacilityType("Home"));
        facilityTypes.put("Garage", new FacilityType("Garage"));
    }
    /**
     * Returns the FacilityType associated with the input descriptor and
     * also creates this object if it was not found in the map.
     * 
     * @param description Description of desired FacilityType.
     * @return Feature object associated with input descriptor.
     */
    public static FacilityType lookup(String description) {
        if (facilityTypes.get(description) == null) {
            facilityTypes.put(description, new FacilityType(description));
        }
        return facilityTypes.get(description);
    }
    
    /**
     * Private constructor to ensure singleton object.
     */    
    private FacilityTypeFactory() {
    }
}
