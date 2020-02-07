/*
 * Rating
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

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Javadoc comments for class.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Rating {
    
    public static final short MINIMUM_RATING = 0;
    
    public static final short MAXIMUM_RATING = 5;
    
    /**
     * Rating ID, immutable value assigned by system.
     */
    private UUID ID;

    /**
     * Rating author ID.
     */
    private String author;

    /**
     * Actual rating (1 to 5 stars).
     */
    private short rating;

    /**
     * Additional comments associated with rating.
     */
    private String comment;

    /**
     * Date rating was created.
     */
    private Date date;
    
    /**
     * No-argument constructor.
     */
    public Rating() {
        date = new Date();  // Date set by system to time rating object was instantiated.
    }

    /**
     * Constructor allowing for specification of all Rating parameters except
     * date (Rating date is set to current date/time).
     * 
     * @param ID Rating ID.
     * @param author Rating author ID.
     * @param rating Actual rating (1 to 5).
     * @param comment Additional comments associated with rating.
     */
    public Rating (String author, short rating, String comment) {
        this.ID = UUID.randomUUID();
        this.author = author;
        this.rating = rating;
        this.comment = comment;
        date = new Date();  // Date set by system to time rating object was instantiated.
    }

    /**
     * Constructor allowing for specification of all Rating parameters.
     * 
     * @param ID Rating ID.
     * @param author Rating author ID.
     * @param rating Actual rating (1 to 5).
     * @param comment Additional comments associated with rating.
     * @param date Date rating was created.
     */
    public Rating (String author, short rating, String comment, Date date) {
        this.ID = UUID.randomUUID();
        this.author = author;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }

    /**
     * Returns UUID that was set for this Rating object.
     * 
     * @return Rating ID.
     */
    public UUID getID() {
        return ID;
    }

    /**
     * Returns UUID of the author of this rating.
     * 
     * @return Rating author ID.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the actual rating set for this Rating object.
     * 
     * @return Actual rating (1 to 5 stars).
     */
    public short getRating() {
        return rating;
    }
    /**
     * Sets the actual rating for this Rating object.
     * 
     * @param rating Actual rating (1 to 5 stars).
     */
    public void setRating(short rating) {
        this.rating = rating;
    }

    /**
     * Returns the comment that was added for this Rating object.
     * 
     * @return Additional comments associated with rating.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment for this Rating object.
     * 
     * @param comment Additional comments associated with rating.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the date this Rating object was created.
     * 
     * @return Date rating was created.
     */
    public Date getDate() {
        return date;
    }
}
