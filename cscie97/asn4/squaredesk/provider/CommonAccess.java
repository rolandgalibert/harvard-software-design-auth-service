/*
 * CommonAccess
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
 * Class allowing for description of an area within a given OfficeSpace
 * facility that can be commonly accessed by renters of the facility.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class CommonAccess {

    /**
     * Description of common access area.
     */
    private String description;
    
    /**
     * Single constructor, requires description property parameter.
     * 
     * @param description
     */
    public CommonAccess(String description) {
        this.description = description;
    }

    /**
     * Returns description of this CommonAccess object.
     * 
     * @return Feature description.
     */
    public String getDescription() {
        return description;
    }
}
