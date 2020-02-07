/*
 * FacilityType
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
 * Class allowing for description of the type of facility a given OfficeSpace
 * object is.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class FacilityType {

    /**
     * Description of facility type.
     */
    private String description;
    
    /**
     * Single constructor, requires description property parameter.
     * 
     * @param description
     */
    public FacilityType(String description) {
        this.description = description;
    }

    /**
     * Returns description of this FacilityType object.
     * 
     * @return FacilityType description.
     */
    public String getDescription() {
        return description;
    }
}
