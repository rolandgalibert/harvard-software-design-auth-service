/*
 * Location
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
 * This class allows for specification of the physical location
 * (i.e. street address, etc.) of an object.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Location {

    /**
     * Address line 1 of object.
     */
    private String street1;

    /**
     * Address line 2 of object.
     */
    private String street2;
    
    /**
     * Object's city.
     */
    private String city;
    
    /**
     * Object's state.
     */
    private String state;
    
    /**
     * Object's zip code.
     */
    private String zip;
    
    /**
     * Object's country (ISO format).
     */
    private String country;

    /**
     * Object's latitude.
     */
    private double latitude;
    
    /**
     * Object's longitude.
     */
    private double longitude;
    
    /**
     * No-argument constructor.
     */
    public Location() {
    }
    
    /**
     * Constructor allowing for specification of all Location parameters.
     * 
     * @param street1 Address line 1 of object.
     * @param street2 Address line 2 of object.
     * @param city Object's city.
     * @param state Object's state.
     * @param zip Object's zip code.
     * @param latitude Object's latitude.
     * @param longitude Object's longitude.
     */
    public Location(String street1, String street2, String city, String state, String zip,
            String country, double latitude, double longitude) {
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    /**
     * Returns the value currently set for line 1 of the Location object's
     * address.
     * 
     * @return Address line 1 of Location object.
     */
    public String getStreet1() {
        return street1;
    }

    /**
     * Sets the value for line 1 of the Location object's address.
     * 
     * @param street1 Address line 1 of Location object.
     */
    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    /**
     * Returns the value currently set for line 2 of the Location object's
     * address.
     * 
     * @return Address line 2 of Location object.
     */
    public String getStreet2() {
        return street2;
    }

    /**
     * Sets the value for line 2 of the Location object's address.
     * 
     * @param street2 Address line 2 of Location object.
     */
    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    /**
     * Returns the value set for the Location object's city.
     * 
     * @return Location object's city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value for the Location object's city.
     * 
     * @param city Location object's city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the value set for the Location object's state.
     * 
     * @return Location object's state.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value for the Location object's state.
     * 
     * @param state Location object's state.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Returns the value set for the Location object's country.
     * 
     * @return Location object's country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value for the Location object's country.
     * 
     * @param country Location object's country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the value set for the Location object's zip code.
     * 
     * @return Location object's zip code.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the value for the Location object's zip code.
     * 
     * @param zip Location object's zip code.
     */
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    /**
     * Returns the latitude set for the Location object.
     * 
     * @return Location object's latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the Location object.
     * 
     * @param latitude Location object's latitude.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude set for the Location object.
     * 
     * @return Location object's longitude.
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Sets the longitude of the Location object.
     * 
     * @param longitude Location object's longitude.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
