/*
 * SchedulingService
 * 
 * Version 1.0
 * 
 * Oct 30, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #3.
 *
 */
package cscie97.asn4.squaredesk.renter;

import cscie97.asn4.squaredesk.authentication.InvalidAccessTokenException;
import cscie97.asn4.squaredesk.authentication.UnauthorizedAccessException;
import cscie97.asn4.squaredesk.provider.OfficeProviderServiceAPI;
import cscie97.asn4.squaredesk.provider.OfficeSpace;
import cscie97.asn4.squaredesk.provider.OfficeSpaceNotFoundException;
import cscie97.asn4.squaredesk.provider.Rate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * This singleton class maintains a map of Booking objects as well as
 * methods for processing those objects, allowing Renters to book OfficeSpace
 * objects for use.
 *
 * Only authenticated users with appropriate permission may execute these
 * methods.
 *
 * OfficeProviderServiceAPI is constructed following the
 * "Initialization-on-demand holder idiom" singleton as described in the
 * Wikipedia article "Singleton pattern".
 *
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class SchedulingService {

    /**
     * Singleton OfficeSpace Provider Service API object.
     */
    private static OfficeProviderServiceAPI officeProviderServiceAPI;

    /**
     * Singleton Renter API object.
     */
    private static RenterAPI renterAPI;
    
    /**
     * Map of Booking objects (UUID to Booking object).
     */
    private HashMap<UUID, Booking> bookingIDMap = new HashMap<UUID, Booking>();
    
    /**
     * Map of Booking objects (OfficeSpace UUID to list of related 
     * Booking objects).
     */
    private HashMap<UUID, ArrayList<Booking>> bookingOfficeSpaceIDMap = new HashMap<UUID, ArrayList<Booking>>();
    
    /**
     * SchedulingServiceHolder is loaded on the first execution of
     * SchedulingService.getInstance() or the first access to
     * SchedulingService.SCHEDULING_SERVICE_INSTANCE, not before.
     */
    private static class SchedulingServiceHolder {
        private static final SchedulingService SCHEDULING_SERVICE_INSTANCE
                = new SchedulingService();
    }

    /**
     * Returns singleton object instance.
     *
     * @return Singleton SchedulingService object.
     */
    public static SchedulingService getInstance() {
        officeProviderServiceAPI = OfficeProviderServiceAPI.getInstance();
        renterAPI = RenterAPI.getInstance();
        return SchedulingServiceHolder.SCHEDULING_SERVICE_INSTANCE;
    }

    /**
     * Returns true if the specified OfficeSpace object is available over the 
     * period specified by startDate and endDate.
     * 
     * @param authToken Client authentication token.
     * @param officeSpace OfficeSpace being queried
     * @param startDate Desired booking start date.
     * @param endDate Desired booking end date.
     * @return True if office space is available over given period.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean checkAvailability(String authToken, OfficeSpace officeSpace,
            Calendar startDate, Calendar endDate) throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (bookingOfficeSpaceIDMap.containsKey(officeSpace.getID())) {
            ArrayList<Booking> bookings = bookingOfficeSpaceIDMap.get(officeSpace.getID());
            for (int i = 0; i < bookings.size(); i++) {
                if (startDate.equals(bookings.get(i).getStartDate())) {
                    return false;
                }
                if (startDate.after(bookings.get(i).getStartDate()) &&
                        (startDate.before(bookings.get(i).getEndDate()) || 
                        startDate.equals(bookings.get(i).getEndDate()))) {
                    return false;
                }
                if (endDate.equals(bookings.get(i).getEndDate())) {
                    return false;
                }
                if (endDate.before(bookings.get(i).getEndDate()) &&
                        (endDate.equals(bookings.get(i).getStartDate()) || 
                        endDate.after(bookings.get(i).getStartDate()))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * Creates a booking to reserve the specified OfficeSpace for the specified
     * Renter over the specified period (defined by start date and end date).
     * 
     * @param authToken Client authentication token.
     * @param renter Renter reserving office space.
     * @param officeSpace OfficeSpace being reserved.
     * @param startDate Booking start date.
     * @param endDate Booking end date.
     * @param rate Rate to be paid for the office space (time period/actual rate)
     * @return Newly created Booking object.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     * @throws RenterNotFoundException
     * @throws OfficeSpaceNotFoundException
     * @throws RentalPeriodNotValidException
     * @throws RateNotValidException
     */
    public Booking createBooking(String authToken, Renter renter, OfficeSpace officeSpace,
            Calendar startDate, Calendar endDate, Rate rate)
            throws InvalidAccessTokenException, UnauthorizedAccessException, RenterNotFoundException, OfficeSpaceNotFoundException,
            RentalPeriodNotValidException, RateNotValidException {
        
        if (!renterAPI.getRenterList(authToken).contains(renter)) {
            throw new RenterNotFoundException();
        }
        if (!officeProviderServiceAPI.getOfficeSpaceList(authToken).contains(officeSpace)) {
            throw new OfficeSpaceNotFoundException();
        }
        if (!officeSpace.getRateMap().containsKey(rate.getRentalPeriod())) {
            throw new RentalPeriodNotValidException();
        }
        if (startDate.after(endDate)) {
            throw new RentalPeriodNotValidException();
        }
        if (rate.getRate() < officeSpace.getRateMap().get(rate.getRentalPeriod()).getRate()) {
            throw new RateNotValidException();
        }
        Booking newBooking = new Booking(renter.getID(), officeSpace.getID(), startDate, endDate, rate);
        bookingIDMap.put(newBooking.getID(), newBooking);
        if (!bookingOfficeSpaceIDMap.containsKey(officeSpace.getID())) {
            bookingOfficeSpaceIDMap.put(officeSpace.getID(), new ArrayList<Booking>());
        }
        bookingOfficeSpaceIDMap.get(officeSpace.getID()).add(newBooking);
        return newBooking;
    }

    /**
     * Returns the Booking object with the specified ID.
     * 
     * @param authToken Client authentication token.
     * @param bookingID ID of desired Booking object.
     * @return Booking object
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     * @throws BookingNotFoundException
     */
    public Booking getBooking(String authToken, UUID bookingID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, BookingNotFoundException {
        if (bookingIDMap.containsKey(bookingID)) {
            return bookingIDMap.get(bookingID);
        } else {
            throw new BookingNotFoundException();
        }
    }

    /**
     * Deletes the Booking object with the specified ID from the set of 
     * currently active Booking objects.
     * 
     * @param authToken Client authentication token.
     * @param booking Booking to be deleted.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     * @throws BookingNotFoundException
     */
    public void deleteBooking(String authToken, Booking booking)
            throws InvalidAccessTokenException, UnauthorizedAccessException, BookingNotFoundException {
        if (bookingIDMap.containsKey(booking.getID())) {
            UUID officeSpaceID = bookingIDMap.get(booking.getID()).getOfficeSpaceID();
            bookingOfficeSpaceIDMap.get(officeSpaceID).remove(booking);
            bookingIDMap.remove(booking.getID());
        } else {
            throw new BookingNotFoundException();
        }
    }

    /**
     * Returns a list of all current Booking objects.
     * 
     * @param authToken Client authentication token.
     * @return List of all current Booking objects
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public ArrayList<Booking> listBookings(String authToken)
            throws InvalidAccessTokenException, UnauthorizedAccessException {
        return new ArrayList<Booking>(bookingIDMap.values());
    }

    /**
     * Returns a list of current Booking objects associated with the specified OfficeSpace.
     * 
     * @param authToken Client authentication token.
     * @param officeSpace OfficeSpace whose bookings are being sought.
     * @return List of current Booking objects associated with the specified OfficeSpace.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     * @throws OfficeSpaceNotFoundException
     */
    public ArrayList<Booking> listBookings(String authToken, OfficeSpace officeSpace)
            throws InvalidAccessTokenException, UnauthorizedAccessException, OfficeSpaceNotFoundException {
        if (bookingOfficeSpaceIDMap.containsKey(officeSpace.getID())) {
            return bookingOfficeSpaceIDMap.get(officeSpace.getID());
        } else {
            throw new OfficeSpaceNotFoundException();
        }
    }

    /**
     * Sets the status of the payment for the specified Booking to due.
     * 
     * @param authToken Client authentication token.
     * @param bookingID Unique identifier for Booking object to be updated.
     * @return Updated Booking object.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     * @throws BookingNotFoundException
     */
    public Booking setBookingPaymentStatusToDue(String authToken, UUID bookingID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, BookingNotFoundException {
        if (bookingIDMap.containsKey(bookingID)) {
            bookingIDMap.get(bookingID).setPaymentStatusToDue();
            return bookingIDMap.get(bookingID);
        } else {
            throw new BookingNotFoundException();
        }
    }

    /**
     * Sets the status of the payment for the specified Booking to overdue.
     * 
     * @param authToken Client authentication token.
     * @param bookingID Unique identifier for Booking object to be updated.
     * @return Updated Booking object.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     * @throws BookingNotFoundException
     */
    public Booking setBookingPaymentStatusToOverdue(String authToken, UUID bookingID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, BookingNotFoundException {
        if (bookingIDMap.containsKey(bookingID)) {
            bookingIDMap.get(bookingID).setPaymentStatusToOverdue();
            return bookingIDMap.get(bookingID);
        } else {
            throw new BookingNotFoundException();
        }
    }

    /**
     * Sets the status of the payment for the specified Booking to paid.
     * 
     * @param authToken Client authentication token.
     * @param bookingID Unique identifier for Booking object to be updated.
     * @return Updated Booking object.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     * @throws BookingNotFoundException
     */
    public Booking setBookingPaymentStatusToPaid(String authToken, UUID bookingID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, BookingNotFoundException {
        if (bookingIDMap.containsKey(bookingID)) {
            bookingIDMap.get(bookingID).setPaymentStatusToPaid();
            return bookingIDMap.get(bookingID);
        } else {
            throw new BookingNotFoundException();
        }
    }
}
