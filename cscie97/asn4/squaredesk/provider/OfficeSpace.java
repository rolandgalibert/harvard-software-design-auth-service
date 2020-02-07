/*
 * OfficeSpace
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

import java.util.*;

/**
 * Class allowing for description of the space on the Provider's property that
 * he/she is offering for rent as an office space.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class OfficeSpace {

    /**
     * Unique identifier for this OfficeSpace object.
     */
    private UUID ID = UUID.randomUUID();
    /**
     * Name of this OfficeSpace objects.
     */
    private String name = new String();
    /**
     * Location of this OfficeSpace object (address, etc.).
     */
    private Location location = new Location();
    /**
     * General OfficeSpace facility type (e.g. Home, Garage, etc.)
     */
    private FacilityType facilityType;
    /**
     * Parameter allowing for further categorization of OfficeSpace facility, if
     * applicable.
     */
    private FacilitySubtype facilitySubtype;
    /**
     * Capacity (object) of this OfficeSpace object (maximum number of
     * occupants, square area, etc.)
     */
    private Capacity capacity = new Capacity();
    /**
     * Map of rates (daily, weekly, etc.) charged by Provider for this
     * OfficeSpace.
     */
    private HashMap<String, Rate> rateMap = new HashMap<String, Rate>();
    /**
     * List of images (and associated information) of the general office space
     * area.
     */
    private HashMap<UUID, Image> officeSpacePictureMap = new HashMap<UUID, Image>();
    /**
     * List of images (and associated information) of the specific office space
     * facility.
     */
    private HashMap<UUID, Image> facilityPictureMap = new HashMap<UUID, Image>();
    /**
     * List of special features this OfficeSpace object offers.
     */
    private ArrayList<Feature> features = new ArrayList<Feature>();
    /**
     * List of areas in the Provider's property that may be accessed by Renters
     * of the office space (e.g. kitchen, conference room, etc.)
     */
    private ArrayList<CommonAccess> commonAccessAreas = new ArrayList<CommonAccess>();
    /**
     * List of ratings given to Provider.
     */
    private HashMap<UUID, Rating> ratingMap = new HashMap<UUID, Rating>();

    /**
     * No-argument constructor.
     */
    public OfficeSpace() {
    }

    /**
     * Returns the name of this OfficeSpace object.
     *
     * @return OfficeSpace name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this OfficeSpace object to the specified value.
     *
     * @param newName Updated OfficeSpace name.
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Returns unique identifier for this OfficeSpace.
     *
     * @return Unique OfficeSpace identifier.
     */
    public UUID getID() {
        return ID;
    }

    /**
     * Returns Capacity object describing capacity available for this
     * OfficeSpace object (maximum number of occupants, square area, etc.)
     *
     * @return Capacity object associated with OfficeSpace.
     */
    public Capacity getCapacity() {
        return capacity;
    }

    /**
     * Sets OfficeSpace's Capacity object to input Capacity.
     *
     * @param capacity Updated Capacity object for OfficeSpace.
     */
    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns list of features associated with this OfficeSpace.
     *
     * @return List of features associated with this OfficeSpace.
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * Returns true if specified feature already exists in this OfficeSpace
     * object's Feature list, false otherwise.
     *
     * @param description
     * @return True if OfficeSpace already has this feature, false otherwise.
     */
    public boolean featureAlreadyExistsInList(String description) {
        if (features.contains(FeatureFactory.lookup(description))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the specified Feature to this OfficeSpace object's Feature list if
     * it isn't already in this list.
     *
     * @param description
     */
    public void addFeature(String description) {
        if (!features.contains(FeatureFactory.lookup(description))) {
            features.add(FeatureFactory.lookup(description));
        }
    }

    /**
     * Removes the specified Feature from this OfficeSpace object's Feature list
     * if it exists in this list.
     *
     * @param description
     */
    public void removeFeature(String description) {
        if (features.contains(FeatureFactory.lookup(description))) {
            features.remove((FeatureFactory.lookup(description)));
        }
    }

    /**
     * Returns list of common access areas associated with this OfficeSpace.
     *
     * @return List of common access areas associated with this OfficeSpace.
     */
    public List<CommonAccess> getCommonAccessAreas() {
        return commonAccessAreas;
    }

    /**
     * Returns true if specified common access area already exists in this
     * OfficeSpace object's CommonAccess list, false otherwise.
     *
     * @param description
     * @return True if OfficeSpace already has this common access, false
     * otherwise.
     */
    public boolean commonAccessAlreadyExistsInList(String description) {
        if (commonAccessAreas.contains(CommonAccessFactory.lookup(description))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the specified common access area to this OfficeSpace object's
     * CommonAccess list if it isn't already in this list.
     *
     * @param description
     */
    public void addCommonAccess(String description) {
        if (!commonAccessAreas.contains(CommonAccessFactory.lookup(description))) {
            commonAccessAreas.add(CommonAccessFactory.lookup(description));
        }
    }

    /**
     * Removes the specified common access area from this OfficeSpace object's
     * CommonAccess list if it exists in this list.
     *
     * @param description
     */
    public void removeCommonAccess(String description) {
        if (commonAccessAreas.contains(CommonAccessFactory.lookup(description))) {
            commonAccessAreas.remove(CommonAccessFactory.lookup(description));
        }
    }

    /**
     * Returns the OfficeSpace picture map.
     *
     * @return OfficeSpace picture map.
     */
    public Map<UUID, Image> getOfficeSpacePictureMap() {
        return officeSpacePictureMap;
    }

    /**
     * Returns the OfficeSpace picture map as a list.
     *
     * @return List of general office space area pictures.
     */
    public List<Image> getOfficeSpacePictures() {
        return new ArrayList<Image>(officeSpacePictureMap.values());
    }

    /**
     * Returns true if the specified office space picture already exists in this
     * OfficeSpace object's set of office space pictures, false otherwise.
     *
     * @param officeSpacePicture Queried office space picture.
     * @return True if this picture already exists in the OfficeSpace, false
     * otherwise.
     */
    public boolean officeSpacePictureAlreadyExistsInList(Image officeSpacePicture) {
        if (officeSpacePictureMap.containsKey(officeSpacePicture.getID())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the specified picture to the set of general office space area
     * pictures for this OfficeSpaceobject.
     *
     * @param newOfficeSpacePicture Picture to be added.
     */
    public void addOfficeSpacePicture(Image newOfficeSpacePicture) {
        if (!officeSpacePictureMap.containsKey(newOfficeSpacePicture.getID())) {
            officeSpacePictureMap.put(newOfficeSpacePicture.getID(), newOfficeSpacePicture);
        }
    }

    /**
     * Removes the specified picture from the set of general office space area
     * pictures for this OfficeSpaceobject.
     *
     * @param officeSpacePicture Picture to be removed.
     */
    public void removeOfficeSpacePicture(Image officeSpacePicture) {
        if (officeSpacePictureMap.containsKey(officeSpacePicture.getID())) {
            officeSpacePictureMap.remove(officeSpacePicture.getID());
        }
    }

    /**
     * Returns the OfficeSpace facility picture map.
     *
     * @return OfficeSpace facility picture map.
     */
    public Map<UUID, Image> getFacilityPictureMap() {
        return facilityPictureMap;
    }

    /**
     * Returns the OfficeSpace facility picture map, as a list.
     *
     * @return List of office space facility pictures.
     */
    public List<Image> getFacilityPictures() {
        return new ArrayList<Image>(facilityPictureMap.values());
    }

    /**
     * Returns true if the specified office space facility picture already
     * exists in this OfficeSpace object's set of office space facility
     * pictures, false otherwise.
     *
     * @param facilityPicture Queried office space facility picture.
     * @return True if this picture already exists in the OfficeSpace, false
     * otherwise.
     */
    public boolean facilityPictureAlreadyExistsInList(Image facilityPicture) {
        if (facilityPictureMap.containsKey(facilityPicture.getID())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the specified picture to the set of office space facility pictures
     * for this OfficeSpaceobject.
     *
     * @param newFacilityPicture Picture to be added.
     */
    public void addFacilityPicture(Image newFacilityPicture) {
        if (!facilityPictureMap.containsKey(newFacilityPicture.getID())) {
            facilityPictureMap.put(newFacilityPicture.getID(), newFacilityPicture);
        }
    }

    /**
     * Removes the specified picture from the set of office space facility
     * pictures for this OfficeSpaceobject.
     *
     * @param facilityPicture Picture to be removed.
     */
    public void removeFacilityPicture(Image facilityPicture) {
        if (facilityPictureMap.containsKey(facilityPicture.getID())) {
            facilityPictureMap.remove(facilityPicture.getID());
        }
    }

    /**
     * Returns this OfficeSpace object's facility type.
     *
     * @return FacilityType of this OfficeSpace.
     */
    public String getFacilityType() {
        if (facilityType == null) {
            return "";
        } else {
            return facilityType.getDescription();
        }
    }

    /**
     * Sets this OfficeSpace object's facility type to the specified
     * FacilityType.
     *
     * @param description
     */
    public void setFacilityType(String description) {
        this.facilityType = FacilityTypeFactory.lookup(description);
    }

    /**
     * Returns this OfficeSpace object's facility subtype (if applicable).
     *
     * @return FacilitySubtype of this OfficeSpace.
     */
    public String getFacilitySubtype() {
        if (facilitySubtype == null) {
            return "";
        } else {
            return facilitySubtype.getDescription();
        }
    }

    /**
    * Sets this OfficeSpace object's facility subtype to the specified
    * FacilitySubtype.
    *
    * @param description
    */
    public void setFacilitySubtype(String description) {
        this.facilitySubtype = FacilitySubtypeFactory.lookup(description);
    }


    /**
     * Returns the Location of this OfficeSpace object.
     * 
     * @return OfficeSpace object Location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the Location of this OfficeSpace object to the specified Location.
     * 
     * @param location Updated OfficeSpace Location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns the map of OfficeSpace rates.
     * 
     * @return OfficeSpace rate map.
     */
    public Map<String, Rate> getRateMap() {
        return rateMap;
    }

    /**
     * Returns the map of OfficeSpace rates, as a list
     * 
     * @return List of OfficeSpace rates.
     */
    public List<Rate> getRates() {
        return new ArrayList<Rate>(rateMap.values());
    }

    /**
     * Returns the number of Rates specified for this OfficeSpace object.
     * 
     * @return Number of specified OfficeSpace Rates.
     */
    public int getRateCount() {
        return rateMap.size();
    }
    
    /**
     * Returns true if this OfficeSpace object already has a Rate with the
     * specified rental period.
     * 
     * @param rentalPeriod Queried rental period.
     * @return True if OfficeSpace has a rate with this rental period, false otherwise.
     */
    public boolean ratePeriodAlreadyExistsInList(String rentalPeriod) {
        return rateMap.containsKey(rentalPeriod);
    }

    /**
     * Adds the specified Rate (Rate object) to this OfficeSpace object's list
     * of Rates.
     * 
     * @param newRate Actual rate amount.
     */
    public void addRateToList(Rate newRate) {
        rateMap.put(newRate.getRentalPeriod(), newRate);
    }

    /**
     * Adds the specified Rate (rental period + actual rate) to this OfficeSpace
     * object's list of Rates.
     * 
     * @param rentalPeriod Period over which this rate applies (day, week, etc.)
     * @param newRate Actual rate amount.
     */
    public void addRateToList(String rentalPeriod, float newRate) {
        rateMap.put(RentalPeriodFactory.lookup(rentalPeriod), new Rate(rentalPeriod, newRate));
    }

    /**
     * Updates the OfficeSpace rental rate associated with the specified
     * rental period.
     * 
     * @param rentalPeriod Period over which this rate applies (day, week, etc.)
     * @param newRate Updated rate amount.
     */
    public void updateRate(String rentalPeriod, float newRate) {
        rateMap.put(RentalPeriodFactory.lookup(rentalPeriod), new Rate(rentalPeriod, newRate));
    }

    /**
     * Removes the OfficeSpace rental rate associated with the specified
     * rental period.
     * 
     * @param rentalPeriod Rental period type for Rate removal.
     */
    public void removeRateFromList(String rentalPeriod) {
        if (rateMap.containsKey(rentalPeriod)) {
            rateMap.remove(rentalPeriod);
        }
    }
    
    /**
     * Returns Ratings furnished for OfficeSpace.
     * 
     * @return List of OfficeSpace's ratings.
     */
    public List<Rating> getRatings() {
        return new ArrayList<Rating>(ratingMap.values());
    }

    /**
     * Adds a new Rating to the OfficeSpace's list of ratings.
     * 
     * @param rating New Rating to be added to OfficeSpace list.
     */
    public void addRatingToList(Rating rating) {
        if (!ratingMap.containsKey(rating.getID())) {
            ratingMap.put(rating.getID(), rating);
        }
    }

    /**
     * Removes a specified Rating from the OfficeSpace's list of ratings.
     * 
     * @param rating Rating to be removed from OfficeSpace list.
     */
    public void removeRatingFromList(Rating rating) {
        if (ratingMap.containsKey(rating.getID())) {
            ratingMap.remove(rating.getID());
        }
    }

    /**
     * Returns true if the specified Rating appears on the OfficeSpace's list.
     * 
     * @param rating Queried Rating.
     * @return true if OfficeSpace list has the specified Rating, false otherwise.
     */
    public boolean ratingExistsOnList(Rating rating) {
        return ratingMap.containsKey(rating.getID());
    }
}
