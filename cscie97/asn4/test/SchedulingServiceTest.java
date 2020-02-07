/*
 * OfficeSpaceTestDriver
 * 
 * Version 1.0
 * 
 * October 4, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #2.
 */
package cscie97.asn4.test;

import cscie97.asn4.squaredesk.authentication.*;
import cscie97.asn4.squaredesk.provider.*;
import cscie97.asn4.squaredesk.renter.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.AccessException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * This class specifically tests the OfficeSpace CRUD methods provided in the
 * ShareDesk Office Space Provider API (see assignment and design document for
 * more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class SchedulingServiceTest {

    private static OfficeProviderServiceAPI officeProviderServiceAPI;
    private static UserAPI userAPI;
    private static RenterAPI renterAPI;
    private static SearchEngine searchEngine;
    private static SchedulingService schedulingService;
    private static AuthServiceImpl authService;
    
    public static void main(String[] args) {
        officeProviderServiceAPI = OfficeProviderServiceAPI.getInstance();
        userAPI = UserAPI.getInstance();
        renterAPI = RenterAPI.getInstance();
        searchEngine = SearchEngine.getInstance();
        schedulingService = SchedulingService.getInstance();
        authService = AuthServiceImpl.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        String superAdminID = "super_admin";
        String superAdminPassword = "p4ssw0rd";
        String superAdminAccessToken;

        String providerUserName = "Provider User 1";
        String providerUserID = "provider_user1";
        String providerUserLoginID = "provider_user1_loginID";
        String providerUserPassword = "provider_user1_password";
        String providerUserAccessToken;

        String renterUserName = "Renter User 1";
        String renterUserID = "renter_user1";
        String renterUserLoginID = "renter_user1_loginID";
        String renterUserPassword = "renter_user1_password";
        String renterUserAccessToken;

        try {

            /*
             * Log on as super admin, create provider user and credential and
             * assign it the provider role.
             */
            System.out.println("Logging in as super_admin:");
            System.out.println("Creating provider user with SquareDesk user role and provider role:");
            superAdminAccessToken = authService.login(superAdminID, superAdminPassword);
            authService.createUser(superAdminAccessToken, providerUserName, providerUserID);
            authService.addUserCredential(superAdminAccessToken, providerUserID, providerUserLoginID, providerUserPassword);
            authService.addUserRole(superAdminAccessToken, providerUserID, "squaredesk_user");
            authService.addUserRole(superAdminAccessToken, providerUserID, "squaredesk_provider");

            System.out.println("Creating renter user with SquareDesk user role and renter role:");
            authService.createUser(superAdminAccessToken, renterUserName, renterUserID);
            authService.addUserCredential(superAdminAccessToken, renterUserID, renterUserLoginID, renterUserPassword);
            authService.addUserRole(superAdminAccessToken, renterUserID, "squaredesk_user");
            authService.addUserRole(superAdminAccessToken, renterUserID, "squaredesk_renter");
            authService.addUserRole(superAdminAccessToken, renterUserID, "squaredesk_provider");

            authService.logout(superAdminAccessToken);
            System.out.println();

            /*
             * Log on as provider user to carry out test operations.
             */
            System.out.println("Logging in as provider user and carrying out original assignment 3 tests:");
            providerUserAccessToken = authService.login(providerUserLoginID, providerUserPassword);

            String sampleURI = "http://static.giantbomb.com/uploads/square_small/15/155745/2179159agent_smith.jpg";

            System.out.println("Creating user 'provider_1' and related provider profile");
            // Create provider1 user + provider profile
            ContactInfo provider1ContactInfo = new ContactInfo("E-mail", "provider1@gmail.com");
            Image provider1Picture = new Image("provider_1 picture", "provider_1 picture description", new URI(sampleURI));
            cscie97.asn4.squaredesk.renter.User provider1 = userAPI.createUser(providerUserAccessToken, "provider_1", provider1ContactInfo,
                    provider1Picture, "PayPal account provider_1-XXXX-XXXX");
            Provider newProvider1 = officeProviderServiceAPI.createProvider(providerUserAccessToken);
            userAPI.updateUserProvider(providerUserAccessToken, provider1.getID(), newProvider1);

            // Create provider1 office spaces
            System.out.println("Creating office space 'office_space_1' for provider_1");
            OfficeSpace p1os1 = officeProviderServiceAPI.createOfficeSpace(providerUserAccessToken);
            officeProviderServiceAPI.updateOfficeSpaceName(providerUserAccessToken, p1os1.getID(), "office_space_1");
            officeProviderServiceAPI.updateOfficeSpaceCapacity(providerUserAccessToken, p1os1.getID(),
                    (short) 1, (short) 1, (float) 1.0);
            officeProviderServiceAPI.updateOfficeSpaceLocation(providerUserAccessToken, p1os1.getID(),
                    "P1OS1 Street1", "P1OS1 Street2", "Cambridge", "MA", "USA", "12345",
                    10.1, 10.1);
            officeProviderServiceAPI.addOfficeSpaceRate(providerUserAccessToken, p1os1.getID(), "Daily", 10);
            officeProviderServiceAPI.updateOfficeSpaceFacilityType(providerUserAccessToken, p1os1.getID(), "Home");
            officeProviderServiceAPI.updateOfficeSpaceFacilityType(providerUserAccessToken, p1os1.getID(), "Dining room");
            officeProviderServiceAPI.addOfficeSpaceFeature(providerUserAccessToken, p1os1.getID(), "Feature1");
            Rating os1Rating = new Rating("joe_schmoe", (short) 5, "Great place");
            officeProviderServiceAPI.addOfficeSpaceRating(providerUserAccessToken, p1os1.getID(), os1Rating);

            System.out.println();
            System.out.println("Logging out as provider user, logging in as renter user and carrying out original assignment 3 tests:");
            authService.logout(providerUserAccessToken);
            renterUserAccessToken = authService.login(renterUserLoginID, renterUserPassword);

                        System.out.println("Creating user 'renter_1' and related renter profile");
            ContactInfo renter1ContactInfo = new ContactInfo("E-mail", "renter1@gmail.com");
            Image renter1Picture = new Image("renter_1 picture", "renter_1 picture description", new URI(sampleURI));
            cscie97.asn4.squaredesk.renter.User renter1 = userAPI.createUser(renterUserAccessToken, "renter_1", renter1ContactInfo,
                    renter1Picture, "PayPal account renter_1-XXXX-XXXX");
            Renter newRenter1 = renterAPI.createRenter(renterUserAccessToken);
            userAPI.updateUserRenter(renterUserAccessToken, renter1.getID(), newRenter1);

            System.out.println();
            System.out.println("Checking availability of office_space_1 for 4 November to 7 November 2014.");
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            startDate.set(2014, 11, 4);
            endDate.set(2014, 11, 7);
            boolean officeSpace1Available = schedulingService.checkAvailability(renterUserAccessToken,
                    p1os1, startDate, endDate);
            if (officeSpace1Available) {
                System.out.println("office_space_1 is available.");
            } else {
                System.out.println("office_space_1 is not available.");
            }

            System.out.println();
            System.out.println("Booking office_space_1 for renter_1 for 4 November to 7 November 2014.");
            Rate rate = new Rate("Daily", 40);
            Booking booking1 = schedulingService.createBooking(renterUserAccessToken, newRenter1,
                    p1os1, startDate, endDate, rate);

            System.out.println();
            System.out.println("Checking availability of office_space_1 for 6 November to 11 November 2014 (overlaps already booked period).");
            startDate.set(2014, 11, 6);
            endDate.set(2014, 11, 11);
            officeSpace1Available = schedulingService.checkAvailability(renterUserAccessToken,
                    p1os1, startDate, endDate);
            if (officeSpace1Available) {
                System.out.println("office_space_1 is available");
            } else {
                System.out.println("office_space_1 is not available");
            }

            System.out.println();
            System.out.println("Deleting original booking for 4 November to 7 November 2014.");
            schedulingService.deleteBooking(renterUserAccessToken, booking1);
            System.out.println("Re-checking availability of office_space_1 for 6 November to 11 November 2014 (should now be available).");
            officeSpace1Available = schedulingService.checkAvailability(renterUserAccessToken,
                    p1os1, startDate, endDate);
            if (officeSpace1Available) {
                System.out.println("office_space_1 is available.");
            } else {
                System.out.println("office_space_1 is not available.");
            }

            System.out.println();
            System.out.println("Booking office_space_1 for renter_1 for 11 November to 6 November 2014 (bad rental period; start date after end date).");
            try {
                startDate.set(2014, 11, 11);
                endDate.set(2014, 11, 6);
                booking1 = schedulingService.createBooking(renterUserAccessToken, newRenter1,
                        p1os1, startDate, endDate, rate);
            } catch (RentalPeriodNotValidException rpnve) {
                System.out.println("RentalPeriodNotValidException thrown.");
            }

            System.out.println();
            System.out.println("Booking office_space_1 for renter_1 for $5/day (office space charges $10/day).");
            try {
                startDate.set(2014, 11, 6);
                endDate.set(2014, 11, 11);
                rate.setRate(5);
                booking1 = schedulingService.createBooking(renterUserAccessToken, newRenter1,
                        p1os1, startDate, endDate, rate);
            } catch (RateNotValidException rnve) {
                System.out.println("RateNotValidException thrown.");
            }


            authService.logout(renterUserAccessToken);
        } catch (OfficeSpaceAlreadyExistsException osaee) {
            System.out.println("TestDriver main: OfficeSpaceAlreadyExistsException occurred.");
        } catch (OfficeSpaceNotFoundException osnfee) {
            System.out.println("TestDriver main: OfficeSpaceNotFoundException occurred.");
        } catch (InvalidAccessTokenException iate) {
            System.out.println("TestDriver main: InvalidAccessTokenException occurred.");
        } catch (UnauthorizedAccessException uae) {
            System.out.println("TestDriver main: UnauthorizedAccessException occurred.");
        } catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred: " + ase.getMessage());
        } catch (URISyntaxException use) {
        } catch (UserAlreadyExistsException uaee) {
        } catch (UserNotFoundException unfe) {
        } catch (RenterNotFoundException rnfe) {
        } catch (RentalPeriodNotValidException rpnve) {
        } catch (RateNotValidException rnve) {
        } catch (BookingNotFoundException bnfe) {
        }
    }
}
