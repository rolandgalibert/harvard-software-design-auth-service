/*
 * Provider
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

import java.net.URI;
import java.util.*;

/**
 * Class allowing for description of the entity who makes facilities on their
 * property available for rent as office space.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Provider {

    /**
     * Unique identifier for Provider.
     */
    private UUID ID = UUID.randomUUID();
    
    /**
     * List of ratings given to Provider.
     */
    private HashMap<UUID, Rating> ratingMap = new HashMap<UUID, Rating>();

    /**
     * No-argument constructor.
     */
    public Provider() {
    }

    /**
     * Returns unique identifier for Provider.
     *
     * @return Provider's unique ID.
     */
    public UUID getID() {
        return ID;
    }

    /**
     * Returns Ratings furnished for Provider.
     * 
     * @return List of Provider's ratings.
     */
    public List<Rating> getRatings() {
        return new ArrayList<Rating>(ratingMap.values());
    }

    /**
     * Adds a new Rating to the Provider's list of ratings.
     * 
     * @param rating New Rating to be added to Provider list.
     */
    public void addRatingToList(Rating rating) {
        if (!ratingMap.containsKey(rating.getID())) {
            ratingMap.put(rating.getID(), rating);
        }
    }

    /**
     * Removes a specified Rating from the Provider's list of ratings.
     * 
     * @param rating Rating to be removed from Provider list.
     */
    public void removeRatingFromList(Rating rating) {
        if (ratingMap.containsKey(rating.getID())) {
            ratingMap.remove(rating.getID());
        }
    }

    /**
     * Returns true if the specified Rating appears on the Provider's list.
     * 
     * @param rating Queried Rating.
     * @return true if Provider list has the specified Rating, false otherwise.
     */
    public boolean ratingExistsOnList(Rating rating) {
        return ratingMap.containsKey(rating.getID());
    }
}
