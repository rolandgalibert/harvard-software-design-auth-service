/*
 * FacilitySubtype
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
 * Class allowing for description of the facility subtype for a given OfficeSpace
 * facility. Currently this subtype is only used to further categorize a "Home"
 * OfficeSpace facility.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class FacilitySubtype {

    /**
     * Description of facility subtype.
     */
    private String description;
    
    /**
     * Single constructor, requires description property parameter.
     * 
     * @param description
     */
    public FacilitySubtype(String description) {
        this.description = description;
    }

    /**
     * Returns description of this FacilityType object.
     * 
     * @return FacilitySubtype description.
     */
    public String getDescription() {
        return description;
    }
}
