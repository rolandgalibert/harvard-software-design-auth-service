/*
 * Booking
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

import cscie97.asn4.squaredesk.provider.Rate;
import java.util.Calendar;
import java.util.UUID;

/**
 * Class allowing for description of a booking created by a Renter in order
 * to reserve an OfficeSpace for use.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Booking {
    
    /**
     * Unique identifier for Booking.
     */
    private UUID ID;
    
    /**
     * Identifier of associated Renter.
     */
    private UUID renterID;
    
    /**
     * Identifier of associated OfficeSpace.
     */
    private UUID officeSpaceID;
    
    /**
     * Booking start date.
     */
    private Calendar startDate;
    
    /**
     * Booking end date.
     */
    private Calendar endDate;
    
    /**
     * Booking rate (time period + actual rate paid for space).
     */
    private Rate rate;
    
    /**
     * Status of payment owed for booking the office space.
     */
    private String paymentStatus = new String();
    
    /**
     * Valid payment status string constants.
     */
    private static final String[] validPaymentStatuses =
    {"Due", "Overdue", "Paid"};
    
    /**
     * Constructor with required parameters.
     * 
     * @param renterID Unique identifier for Booking Renter.
     * @param officeSpaceID Unique identifier for Booking OfficeSpace.
     * @param startDate Booking start date.
     * @param endDate Booking end date.
     * @param rate Booking rate.
     */
    public Booking(UUID renterID, UUID officeSpaceID, Calendar startDate, Calendar endDate,
            Rate rate) {
        ID = UUID.randomUUID();
        this.renterID = renterID;
        this.officeSpaceID = officeSpaceID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rate = rate;
        this.paymentStatus = validPaymentStatuses[0];
    }

    /**
     * Returns status of payment owed for booking the office space.
     * 
     * @return Booking payment status.
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets Booking payment status to due.
     */
    public void setPaymentStatusToDue() {
        paymentStatus = validPaymentStatuses[0];
    }

    /**
     * Sets Booking payment status to overdue.
     */
    public void setPaymentStatusToOverdue() {
        paymentStatus = validPaymentStatuses[1];
    }
    
    /**
     * Sets Booking payment status to paid.
     */
    public void setPaymentStatusToPaid() {
        paymentStatus = validPaymentStatuses[2];
    }

    /**
     * Returns Booking ID.
     * 
     * @return Booking UUID
     */
    public UUID getID() {
        return ID;
    }
    
    /**
     * Returns Booking Renter ID.
     * 
     * @return Booking Renter ID.
     */
    public UUID getRenterID() {
        return renterID;
    }

    /**
     * Updates Booking Renter ID.
     * 
     * @param renterID Unique identifier of Booking Renter.
     */
    public void setRenterID(UUID renterID) {
        this.renterID = renterID;
    }

    /**
     * Returns Booking OfficeSpace ID.
     * 
     * @return Booking OfficeSpace ID.
     */
    public UUID getOfficeSpaceID() {
        return officeSpaceID;
    }

    /**
     * Updates Booking OfficeSpace ID.
     * 
     * @param renterID Unique identifier of new OfficeSpace Renter.
     */
    public void setOfficeSpaceID(UUID officeSpaceID) {
        this.officeSpaceID = officeSpaceID;
    }

    /**
     * Returns Booking start date.
     * 
     * @return Booking start date.
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * Updates Booking start date.
     * 
     * @param startDate New Booking start date
     */
    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns Booking end date.
     * 
     * @return Booking end date.
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * Updates Booking start date.
     * 
     * @param endDate New Booking end date
     */
    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns Rate agreed on for this Booking.
     * 
     * @return Booking rate.
     */
    public Rate getRate() {
        return rate;
    }

    /**
     * Updates Booking rate.
     * 
     * @param rate New Booking rate.
     */
    public void setRate(Rate rate) {
        this.rate = rate;
    }
}
