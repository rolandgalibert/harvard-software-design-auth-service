/*
 * ProviderAPI
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * This interface defines the necessary CRUD methods for processing Provider
 * objects.
 * 
 * @author Roland L. Galibert
 * @version 1.0
 */
public interface ProviderAPI {

    /**
     * Provider object constructor with no input parameters.
     *
     * @param authToken Client authentication token.
     * @return Newly created Provider object.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Provider createProvider(String authToken) throws InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Returns the Provider object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @return Desired Provider object or null.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Provider getProvider(String authToken, UUID providerID)
            throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Returns the Rating list for the Provider with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @return List of Rating objects for specified Provider.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public List<Rating> getProviderRatings(String authToken, UUID providerID)
            throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
    
    /**
     * Adds a Rating to the rating list of the Provider with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @param rating Provider rating (Rating object).
     * @return Updated Provider object.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Provider addProviderRating(String authToken, UUID providerID,
            Rating rating) throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Removes the specified Rating from the rating list of the Provider with
     * the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @param rating Provider rating (Rating object).
     * @throws ProviderNotFoundException
     * @throws RatingNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeProviderRating(String authToken, UUID providerID,
        Rating rating) throws ProviderNotFoundException, RatingNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Deletes the Provider object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void deleteProvider(String authToken, UUID providerID)
            throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * Returns true if ratings for specified Provider meet specifications,
     * false otherwise.
     * 
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @return True if Provider ratings pass validation, false otherwise.
     * @throws ProviderNotFoundException
     * @throws BadRatingException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validProviderRatings(String authToken, UUID providerID)
            throws ProviderNotFoundException, BadRatingException,
            InvalidAccessTokenException, UnauthorizedAccessException;

        /**
     * Returns true if all specified Provider properties meets specifications,
     * false otherwise.
     * 
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @return True if Provider properties pass validation, false otherwise.
     * @throws ProviderNotFoundException
     * @throws ProviderContactInfoNotFoundException
     * @throws ProviderAccountNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validProvider(String authToken, UUID providerID)
            throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException;
}
