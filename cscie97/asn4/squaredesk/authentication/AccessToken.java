/*
 * AccessToken
 * 
 * Version 1.0
 * 
 * November 20, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #2.
 *
 */
package cscie97.asn4.squaredesk.authentication;

import java.util.Date;
import java.util.UUID;

/**
 * This class allows for the creation and use of an access token, which allows a
 * SquareDesk user to execute the operations for which he/she has permission. 
 * This token is associated with a single SquareDesk user, is created when
 * the user logs in and invalidated when the user logs out.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class AccessToken implements AuthServiceVisitorElement {
    
    /**
     * Possible access token states.
     */
    public static final String[] VALID_STATES = {"ACTIVE", "EXPIRED"};
    
    /**
     * Timeout period in milliseconds (currently small value is for testing purposes).
     */
    public static final long TIMEOUT_PERIOD = 2000;
            
    /**
     * Unique identifier for access token.
     */
    private UUID ID;
    
    /**
     * Actual access token state.
     */
    private String state;
    
    /**
     * Time access token was last used.
     */
    private Date lastAccessTime;
    
    /**
     * Constructor.
     */
    public AccessToken() {
        ID = UUID.randomUUID();
        lastAccessTime = new Date();
        state = VALID_STATES[0];
    }

    /**
     * Returns true if the access token has expired (i.e. the amount of time
     * between the current time and the time it was previously used exceeds
     * the timeout period). This method does not actually reset the access
     * token's state.
     * 
     * @return True if access token has expired.
     */
    public boolean accessTokenExpired() {
        Date currentTime = new Date();
        if ((currentTime.getTime() - lastAccessTime.getTime()) > TIMEOUT_PERIOD) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns unique identifier for the access token.
     * 
     * @return Unique access token identifier.
     */
    public UUID getID() {
        return ID;
    }
    
    /**
     * 
     */
    public void updateLastAccessTime() {
        lastAccessTime = new Date();
    }
    
    /**
     * Returns time user last used access token.
     * 
     * @return Last time access token was used.
     */
    public Date getLastAccessTime() {
        return lastAccessTime;
    }
    
    /**
     * Returns current state of access token.
     * 
     * @return Current access token state.
     */
    public String getState() {
        return state;
    }
    
    /**
     * Sets state of access token to active.
     */
    public void setStateToActive() {
        state = VALID_STATES[0];
    }
    
    /**
     * Sets state of access token to expired.
     */
    public void setStateToExpired() {
        state = VALID_STATES[1];
    }

    /**
     * Method to accept an AuthServiceVisitor object.
     * 
     * @param visitor AuthServiceVisitor object.
     */
    @Override
    public void acceptVisitor(AuthServiceVisitor visitor) {
        visitor.visitAccessToken(this);
    }
}