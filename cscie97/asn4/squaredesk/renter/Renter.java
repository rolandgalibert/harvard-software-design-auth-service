/*
 * Renter
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

import cscie97.asn4.squaredesk.provider.Rating;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class allowing for description of a SquareDesk Renter.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Renter {
    
    
    /**
     * Unique identifier for Renter.
     */
    private UUID ID;
    
    /**
     * Criteria that the Renter requires in an office space.
     */
    private RenterCriteria criteria;
    
    /**
     * List of ratings given to Renter.
     */
    private HashMap<UUID, Rating> ratingMap;

    /**
     * No-argument constructor.
     */
    public Renter() {
        ID = UUID.randomUUID();
        criteria = new RenterCriteria();
        ratingMap = new HashMap<UUID, Rating>();
    }
    
    /**
     * Returns unique identifier for Renter.
     *
     * @return Renter's unique ID.
     */
    public UUID getID() {
        return ID;
    }
    
    /**
     * Returns Renter's office space criteria.
     *
     * @return Renter's office space criteria.
     */
    public RenterCriteria getCriteria() {
        return criteria;
    }
    
    /**
     * Returns Ratings furnished for Renter.
     * 
     * @return List of Renter's ratings.
     */
    public ArrayList<Rating> getRatings() {
        return new ArrayList<Rating>(ratingMap.values());
    }

    /**
     * Adds a new Rating to the Renter's list of ratings.
     * 
     * @param rating New Rating to be added to Renter list.
     */
    public void addRatingToList(Rating rating) {
        if (!ratingMap.containsKey(rating.getID())) {
            ratingMap.put(rating.getID(), rating);
        }
    }

    /**
     * Removes a specified Rating from the Renter's list of ratings.
     * 
     * @param rating Rating to be removed from Renter list.
     */
    public void removeRatingFromList(Rating rating) {
        if (ratingMap.containsKey(rating.getID())) {
            ratingMap.remove(rating.getID());
        }
    }

    /**
     * Returns true if the specified Rating appears on the Renter's list.
     * 
     * @param rating Queried Rating.
     * @return true if Provider list has the specified Rating, false otherwise.
     */
    public boolean ratingExistsOnList(Rating rating) {
        return ratingMap.containsKey(rating.getID());
    }
}

