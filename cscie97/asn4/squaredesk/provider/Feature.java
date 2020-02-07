/*
 * Feature
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
 * Class allowing for description of an special feature provided by a given
 * OfficeSpace object.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Feature {

    /**
     * Description of feature.
     */
    private String description;
    
    /**
     * Single constructor, requires description property parameter.
     * 
     * @param description
     */
    public Feature(String description) {
        this.description = description;
    }

    /**
     * Returns description of this Feature object.
     * 
     * @return Feature description.
     */
    public String getDescription() {
        return description;
    }
}
