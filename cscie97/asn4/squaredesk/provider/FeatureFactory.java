/*
 * FeatureFactory
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
 * Static class for maintaining list of all OfficeSpace Features. Flyweight
 * pattern is implemented to conserve space required by the program.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class FeatureFactory {

    /**
     * Private collection of all features.
     */
    private static HashMap<String, Feature> features = new HashMap<String, Feature>();
    
    /**
     * Returns the Feature associated with the input descriptor and
     * also creates this object if it was not found in the map.
     * 
     * @param description Description of desired Feature.
     * @return Feature object associated with input descriptor.
     */
    public static Feature lookup(String description) {
        if (features.get(description) == null) {
            features.put(description, new Feature(description));
        }
        return features.get(description);
    }
    
    /**
     * Private constructor to ensure singleton object.
     */    
    private FeatureFactory() {
    }
}
