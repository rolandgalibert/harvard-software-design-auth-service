/*
 * Rate
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
 * Class allowing for specification of the rate charged for an OfficeSpace
 * object. This rate is defined by the rental period (e.g. day) and actual
 * amount charged for that period.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Rate {

    /**
     * Rental period for which rate applies (e.g. day, week, etc.).
     */
    private String rentalPeriod;
    
    /**
     * Actual rate in USD.
     */
    private float rate;

    /**
     * Single constructor, requires all parameters.
     * 
     * @param rentalPeriod Rental period for which rate applies (e.g. day).
     * @param rate Actual rate.
     */
    public Rate(String rentalPeriod, float rate) {
        this.rentalPeriod = RentalPeriodFactory.lookup(rentalPeriod);
        this.rate = rate;
    }
    
    /**
     * Returns Rate object rate amount.
     * 
     * @return Rate amount.
     */
    public float getRate() {
        return rate;
    }

    /**
     * Updates Rate amount.
     * 
     * @param rate Rate amount.
     */
    public void setRate(float rate) {
        this.rate = rate;
    }

    /**
     * Returns rental period associated with this Rate object.
     * 
     * @return Rental period over which this Rate applies.
     */
    public String getRentalPeriod() {
        return rentalPeriod;
    }

    /**
     * Updates the rental period associated with this Rate object.
     * 
     * @param rentalPeriod Rental period over which this Rate applies.
     */
    public void setRentalPeriod(String rentalPeriod) {
        this.rentalPeriod = RentalPeriodFactory.lookup(rentalPeriod);
    }
}
