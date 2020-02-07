/*
 * OfficeSpaceAPI
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

import cscie97.asn4.squaredesk.authentication.InvalidAccessTokenException;
import cscie97.asn4.squaredesk.authentication.UnauthorizedAccessException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * This interface defines the necessary CRUD methods for processing OfficeSpace
 * objects.
 * 
 * @author Roland L. Galibert
 * @version 1.0
 */
public interface OfficeSpaceAPI {
    
    /**
     * OfficeSpace object constructor with no input parameters.
     *
     * @param authToken Client authentication token.
     * @return OfficeSpace object that was created.
     * @throws OfficeSpaceAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace createOfficeSpace(String authToken)
            throws OfficeSpaceAlreadyExistsException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * OfficeSpace object constructor that takes all mandatory OfficeSpace parameters.
     *
     * @param authToken Client authentication token.
     * @param name Name of OfficeSpace object.
     * @param location Location (address, etc.) of OfficeSpace.
     * @param capacity Capacity (max. occupants, etc.) of OfficeSpace.
     * @param rate Rates for this OfficeSpace.
     * @param facilityType OfficeSpace facility type.
     * @param facilitySubtype OfficeSpace facility subtype (where applicable).
     * @return OfficeSpace object that was created.
     * @throws OfficeSpaceAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace createOfficeSpace(String authToken, String name,
            Location location, Capacity capacity, Rate rate, String facilityType,
            String facilitySubtype)
            throws OfficeSpaceAlreadyExistsException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns OfficeSpace object with the specified ID, or null if no such object
     * was found.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace object with specified ID, or null if none was found.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace getOfficeSpace(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Deletes OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void deleteOfficeSpace(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns name of OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID
     * @return OfficeSpace name.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public String getOfficeSpaceName(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Updates the name of the OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param name New name for specified OfficeSpace object.
     * @return Updated OfficeSpace.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceName(String authToken, UUID officeSpaceID,
            String name) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns list of Features associated with OfficeSpace object with the
     * specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace Feature list.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public List<Feature> getOfficeSpaceFeatures(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Adds a Feature to the Feature list of the OfficeSpace object with the
     * specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param feature Feature to be added to specified OfficeSpace object.
     * @return Updated OfficeSpace.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpaceFeature(String authToken, UUID officeSpaceID,
            String feature) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
 
    /**
     * Removes the specified Feature from the Feature list of the OfficeSpace
     * object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param feature Feature to be removed from specified OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeOfficeSpaceFeature(String authToken, UUID officeSpaceID,
            String feature) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns list of CommonAccess areas associated with OfficeSpace object with the
     * specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace CommonAccess list.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public List<CommonAccess> getOfficeSpaceCommonAccesses(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Adds a CommonAccess area to the CommonAccess list of the OfficeSpace
     * object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param commonAccess CommonAccess area to be added to specified OfficeSpace object.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpaceCommonAccess(String authToken, UUID officeSpaceID,
            String commonAccess) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Removes the specified CommonAccess area from the CommonAccess list of the
     * OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param commonAccess CommonAccess area to be removed from specified OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeOfficeSpaceCommonAccess(String authToken, UUID officeSpaceID,
            String commonAccess) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns Location of OfficeSpace object with the specified ID.
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace Location.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Location getOfficeSpaceLocation(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Updates Location information of the OfficeSpace object with the specified
     * ID, by passing in a Location object
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param location Location object with new location information for OfficeSpace.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceLocation(String authToken, UUID officeSpaceID,
            Location location) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Updates Location information of the OfficeSpace object with the specified
     * ID, by passing specific Location properties.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param street1 Updated Location street1.
     * @param street2 Updated Location street2.
     * @param city Updated Location city.
     * @param state Updated Location state.
     * @param country Updated Location country.
     * @param zip Updated Location zip.
     * @param latitude Updated Location latitude.
     * @param longitude Updated Location longitude.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceLocation(String authToken, UUID officeSpaceID,
            String street1, String street2, String city, String state, String country,
            String zip, double latitude, double longitude)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns Capacity of OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace Capacity.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Capacity getOfficeSpaceCapacity(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Updates Capacity information of the OfficeSpace object with the specified
     * ID, by passing in a Capacity object with updated values.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param capacity Capacity object with new capacity information for OfficeSpace.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceCapacity(String authToken, UUID officeSpaceID,
            Capacity capacity) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Updates Capacity information of the OfficeSpace object with the specified
     * ID, by passing in specific Capacity properties.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param maxOccupants Maximum occupants OfficeSpace can hold.
     * @param workspaces Maximum number of workspaces OfficeSpace has available.
     * @param squareFootage Size of OfficeSpace.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceCapacity(String authToken, UUID officeSpaceID,
            short maxOccupants, short workspaces, float squareFootage)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns FacilityType of OfficeSpace object with the specified ID.
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace FacilityType.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public String getOfficeSpaceFacilityType(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Updates the FacilityType of the OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param facilityType New OfficeSpace facility type.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceFacilityType(String authToken, UUID officeSpaceID,
            String facilityType) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns FacilitySubtype of OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpaceFacilitySubtype.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public String getOfficeSpaceFacilitySubtype(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Updates the FacilitySubtype of the OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param facilitySubtype New OfficeSpace facility subtype.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceFacilitySubtype(String authToken, UUID officeSpaceID,
            String facilitySubtype) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns list of Rates associated with OfficeSpace object with the
     * specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace list of Rates.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public List<Rate> getOfficeSpaceRates(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Adds a rental Rate object to the Rate list of the OfficeSpace object with
     * the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rate New rate (rental period + actual rate) to be added to OfficeSpace object.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws RateAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpaceRate(String authToken, UUID officeSpaceID,
            Rate rate) throws OfficeSpaceNotFoundException, RateAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Adds a rental rate to the Rate list of the OfficeSpace object with
     * the specified ID by processing specified Rate properties.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rentalPeriod Rental period for which the new rate will apply.
     * @param rate Actual amount charged over the rental period.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws RateAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpaceRate(String authToken, UUID officeSpaceID,
            String rentalPeriod, float rate) throws OfficeSpaceNotFoundException, RateAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Updates the rental Rate with the given RentalPeriod in the OfficeSpace
     * object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rentralPeriod OfficeSpace rental period whose rate is to be updated.
     * @param newRate Updated rate amount for this rental period.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws RateNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceRate(String authToken, UUID officeSpaceID,
            String rentalPeriod, float newRate)
            throws OfficeSpaceNotFoundException, RateNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Deletes the rate with the specified RentalPeriod from the Rate list of
     * the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rentalPeriod OfficeSpace rental period whose corresponding Rate we will delete.
     * @throws OfficeSpaceNotFoundException
     * @throws RateNotFoundException
     * @throws RateCountFallsBelowMinimumException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeOfficeSpaceRate(String authToken, UUID officeSpaceID,
            String rentalPeriod) throws OfficeSpaceNotFoundException, RateNotFoundException,
            RateCountFallsBelowMinimumException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns list of general office space pictures associated with
     * OfficeSpace object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace pictures.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public List<Image> getOfficeSpacePictures(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Adds an office space Image to the office space Image list of the OfficeSpace
     * object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param picture New picture of overall OfficeSpace area.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpacePicture(String authToken, UUID officeSpaceID,
            Image picture) throws OfficeSpaceNotFoundException, ImageAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException; 
    
    /**
     * Adds an office space Image to the office space Image list of the
     * OfficeSpace object with the specified ID, using a specified Image object
     * property parameters.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureName Name of new picture of overall OfficeSpace area.
     * @param pictureDescription Description of new picture of overall OfficeSpace area.
     * @param pictureURI URI of new picture of overall OfficeSpace area.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpacePicture(String authToken, UUID officeSpaceID,
            String pictureName, String pictureDescription, URI pictureURI)
            throws OfficeSpaceNotFoundException, ImageAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Updates the properties of the office space Image, specified with
     * pictureID, in the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureID UUID of picture of overall OfficeSpace area.
     * @param updatedName Updated name for picture.
     * @param updatedDescription Updated description for picture.
     * @param updatedURI Updated URI for picture.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpacePicture(String authToken, UUID officeSpaceID,
            UUID pictureID, String updatedName, String updatedDescription, URI updatedURI)
            throws OfficeSpaceNotFoundException, ImageNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Deletes the specified office space Image from the office space image list
     * of the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureID Unique identifier of picture to be removed, of overall
     * OfficeSpace area.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeOfficeSpacePicture(String authToken, UUID officeSpaceID,
            UUID pictureID) throws OfficeSpaceNotFoundException, ImageNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns list of pictures of specific facility for OfficeSpace object with
     * the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return List of OfficeSpace facility pictures.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public List<Image> getOfficeSpaceFacilityPictures(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Adds an office space facility Image to the office space facility Image list
     * of the OfficeSpace object with the specified ID, using an Image object.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param picture New picture of OfficeSpace facility.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpaceFacilityPicture(String authToken, UUID officeSpaceID,
            Image picture) throws OfficeSpaceNotFoundException, ImageAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Adds an office space facility Image to the office space facility Image list
     * of the OfficeSpace object with the specified ID, using specified Image object
     * property parameters.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureName Name of new picture of OfficeSpace facility.
     * @param pictureDescription Description of new picture of OfficeSpace facility.
     * @param pictureURI URI of new picture of OfficeSpace facility.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpaceFacilityPicture(String authToken, UUID officeSpaceID,
            String pictureName, String pictureDescription, URI pictureURI)
            throws OfficeSpaceNotFoundException, ImageAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Updates the properties of the office space facility Image, specified with
     * pictureID, in the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureID UUID of picture of OfficeSpace facility.
     * @param updatedName Updated name for picture.
     * @param updatedDescription Updated description for picture.
     * @param updatedURI Updated URI for picture.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace updateOfficeSpaceFacilityPicture(String authToken, UUID officeSpaceID,
            UUID pictureID, String updatedName, String updatedDescription, URI updatedURI)
            throws OfficeSpaceNotFoundException, ImageNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Deletes the specified office space Image from the office space facility
     * image list of the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureID Unique identifier of picture to be removed, of
     * OfficeSpace facility.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeOfficeSpaceFacilityPicture(String authToken, UUID officeSpaceID,
            UUID pictureID) throws OfficeSpaceNotFoundException, ImageNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns true if Location information for specified OfficeSpace meets
     * specifications, false otherwise.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if OfficeSpace Location information passes validation, false otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws BadOfficeSpaceLocationException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validOfficeSpaceLocation(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, BadOfficeSpaceLocationException,
            InvalidAccessTokenException, UnauthorizedAccessException;
    
        /**
     * Returns true if Capacity information for specified OfficeSpace meets
     * specifications, false otherwise.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if OfficeSpace Capacity information passes validation, false otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws BadOfficeSpaceCapacityException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validOfficeSpaceCapacity(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, BadOfficeSpaceCapacityException,
            InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Returns true if FacilityType/FacilitySubtype information for specified
     *  OfficeSpace meets specifications, false otherwise.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if OfficeSpace FacilityType/FacilitySubtype information passes validation, false otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws OfficeSpaceFacilityException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validOfficeSpaceFacility(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, OfficeSpaceFacilityException, InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Returns true if Rate information for specified OfficeSpace meets
     *  OfficeSpace meets specifications, false otherwise.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if OfficeSpace Rate information passes validation, false otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws OfficeSpaceRateException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validOfficeSpaceRates(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, OfficeSpaceRateException, InvalidAccessTokenException, UnauthorizedAccessException;
    
        /**
     * Returns true if overall properties for specified OfficeSpace meet
     * specifications, false otherwise.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if overall OfficeSpace passes validation, false otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validOfficeSpace(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
}
