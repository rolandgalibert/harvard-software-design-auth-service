/*
 * FacilitySubtypeFactory
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
 * Static class for maintaining list of all FacilitySubtypes. Flyweight
 * pattern is implemented to conserve space required by the program.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class FacilitySubtypeFactory {

    /**
     * Private singleton collection of all facility subtypes.
     */
    private static HashMap<String, FacilitySubtype> facilitySubtypes = new HashMap<String, FacilitySubtype>();
    
    /**
     * Returns the FacilityType associated with the input descriptor and
     * also creates this object if it was not found in the map.
     * 
     * @param description Description of desired FacilitySubtype.
     * @return Feature object associated with input descriptor.
     */
    public static FacilitySubtype lookup(String description) {
        if (facilitySubtypes.get(description) == null) {
            facilitySubtypes.put(description, new FacilitySubtype(description));
        }
        return facilitySubtypes.get(description);
    }
    
    /**
     * Private constructor to ensure singleton object.
     */    
    private FacilitySubtypeFactory() {
    }
}
