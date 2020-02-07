/*
 * RenterCriteria
 * 
 * Version 1.0
 * 
 * October 30, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #3.
 *
 */

package cscie97.asn4.squaredesk.renter;

import cscie97.asn4.squaredesk.provider.Location;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class allowing for description of the criteria a SquareDesk Renter requires
 * in an office space.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class RenterCriteria {
    
    /**
     * List of features the given Renter requires in an office space.
     */
    private ArrayList<String> features = new ArrayList<String>();
    
    /**
     * OfficeSpace location required by Renter.
     */
    private Location location = new Location();

    /**
     * OfficeSpace facility type required by Renter.
     */
    private String facilityType = new String();
    
    /**
     * OfficeSpace facility subtype required by Renter.
     */
    private String facilitySubtype = new String();
    
    /**
     * Minimum acceptable OfficeSpace rating required by Renter.
     */
    private short minimumRating = -1;

    /**
     * Starting date of Renter's desired rental period.
     */
    private Date startDate = new Date();
    
    /**
     * End date of Renter's desired rental period.
     */
    private Date endDate = new Date();
    
    /**
     * No-argument constructor.
     */
    public RenterCriteria() {
    }

    /**
     * Returns list of features specified in Renter's search criteria.
     * 
     * @return List of RentalCriteria features.
     */
    public ArrayList<String> getFeatures() {
        return features;
    }

    /**
     * Adds a feature to the list of features specified in Renter's search criteria.
     * 
     * @param feature New RentalCriteria feature.
     */
    public void addFeature(String feature) {
        if (!features.contains(feature)) {
            features.add(feature);
        }
    }

    /**
     * Removes a feature from the list of features specified in Renter's search criteria.
     * 
     * @param feature RentalCriteria feature to be removed.
     */
    public void removeFeature(String feature) {
        if (features.contains(feature)) {
            features.remove(feature);
        }
    }

    /**
     * Returns the latitude of the Renter's desired office space location.
     * 
     * @return RentalCriteria location latitude.
     */
    public double getLocationLatitude() {
        return this.location.getLatitude();
    }

    /**
     * Set the latitude of the Renter's desired office space location.
     * 
     * @param latitude Updated location latitude.
     */
    public void setLocationLatitude(double latitude) {
        this.location.setLatitude(latitude);
    }

    /**
     * Returns the longitude of the Renter's desired office space location.
     * 
     * @return RentalCriteria location longitude.
     */
    public double getLocationLongitude() {
        return this.location.getLongitude();
    }

    /**
     * Set the longitude of the Renter's desired office space location.
     * 
     * @param longitude Updated location longitude.
     */
    public void setLocationLongitude(double longitude) {
        this.location.setLongitude(longitude);
    }

    /**
     * Returns the office space facility type required by the Renter.
     * 
     * @return RenterCriteria facility type.
     */
    public String getFacilityType() {
        return facilityType;
    }

    /**
     * Updates the office space facility type required by the Renter.
     * 
     * @param facilityType New RenterCriteria facility type.
     */
    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    /**
     * Returns the office space facility subtype required by the Renter.
     * 
     * @return RenterCriteria facility subtype.
     */
    public String getFacilitySubtype() {
        return facilitySubtype;
    }

    /**
     * Updates the office space facility type required by the Renter.
     * 
     * @param facilitySubtype New RenterCriteria facility subtype.
     */
    public void setFacilitySubtype(String facilitySubtype) {
        this.facilitySubtype = facilitySubtype;
    }

    /**
     * Returns the minimum acceptable OfficeSpace rating required by the Renter.
     * 
     * @return RenterCriteria minimum acceptable rating.
     */
    public short getMinimumRating() {
        return minimumRating;
    }

    /**
     * Updates the minimum acceptable OfficeSpace rating required by the Renter.
     * 
     * @param minimumRating New RenterCriteria minimum acceptable rating.
     */
    public void setMinimumRating(short minimumRating) {
        this.minimumRating = minimumRating;
    }

    /**
     * Clears the RenterCriteria minimum acceptable rating field.
     */
    public void clearMinimumRating() {
        this.minimumRating = -1;
    }

    /**
     * Returns the starting date of Renter's desired rental period.
     * 
     * @return RenterCriteria start date.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Updates the starting date of Renter's desired rental period.
     * 
     * @param startDate New RenterCriteria start date.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Clears the starting date of Renter's desired rental period.
     */
    public void clearStartDate() {
        this.startDate = new Date();
    }

    /**
     * Returns the end date of Renter's desired rental period.
     * 
     * @return RenterCriteria start date.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Updates the end date of Renter's desired rental period.
     * 
     * @param endDate New RenterCriteria end date.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Clears the end date of Renter's desired rental period.
     */
    public void clearEndDate() {
        this.endDate = new Date();
    }
}
