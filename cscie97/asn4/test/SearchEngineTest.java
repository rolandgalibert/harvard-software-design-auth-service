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
public class SearchEngineTest {

    private static OfficeProviderServiceAPI officeProviderServiceAPI;
    private static UserAPI userAPI;
    private static RenterAPI renterAPI;
    private static SearchEngine searchEngine;
    private static AuthServiceImpl authService;

    public static void main(String[] args) {
        officeProviderServiceAPI = OfficeProviderServiceAPI.getInstance();
        userAPI = UserAPI.getInstance();
        renterAPI = RenterAPI.getInstance();
        searchEngine = SearchEngine.getInstance();
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

            // Create provider1 user + provider profile
            System.out.println("Creating user 'provider_1' and related provider profile");
            ContactInfo provider1ContactInfo = new ContactInfo("E-mail", "provider_1@gmail.com");
            Image provider1Picture = new Image("Provider1 picture", "provider_1 picture description", new URI(sampleURI));
            cscie97.asn4.squaredesk.renter.User provider1 = userAPI.createUser(providerUserAccessToken, "provider_1", provider1ContactInfo,
                    provider1Picture, "PayPal account provider_1-XXXX-XXXX");
            Provider newProvider1 = officeProviderServiceAPI.createProvider(providerUserAccessToken);
            userAPI.updateUserProvider(providerUserAccessToken, provider1.getID(), newProvider1);

            // Create provider1 office spaces
            System.out.println();
            System.out.println("Creating office space 'office_space_1' for provider_1");
            System.out.println("\tlat/long = 10.1/10.1; facilityType = 'Home', category = 'Dining room', features=WIFI, Coffee; average rating = 5");
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
            officeProviderServiceAPI.addOfficeSpaceFeature(providerUserAccessToken, p1os1.getID(), "WIFI");
            officeProviderServiceAPI.addOfficeSpaceFeature(providerUserAccessToken, p1os1.getID(), "Coffee");
            Rating os1Rating = new Rating("joe_schmoe", (short) 5, "Great place");
            officeProviderServiceAPI.addOfficeSpaceRating(providerUserAccessToken, p1os1.getID(), os1Rating);

            System.out.println();
            System.out.println("Creating office space 'office_space_2' for provider_1");
            System.out.println("\tlat/long = 10.2/10.2; facilityType = 'Garage', features=Allows pets; average rating = 5");
            OfficeSpace p1os2 = officeProviderServiceAPI.createOfficeSpace(providerUserAccessToken);
            officeProviderServiceAPI.updateOfficeSpaceName(providerUserAccessToken, p1os2.getID(), "office_space_2");
            officeProviderServiceAPI.updateOfficeSpaceCapacity(providerUserAccessToken, p1os2.getID(),
                    (short) 1, (short) 1, (float) 1.0);
            officeProviderServiceAPI.updateOfficeSpaceLocation(providerUserAccessToken, p1os2.getID(),
                    "P1OS2 Street1", "P1OS2 Street2", "Cambridge", "MA", "USA", "12345",
                    10.2, 10.2);
            officeProviderServiceAPI.addOfficeSpaceRate(providerUserAccessToken, p1os2.getID(), "Daily", 10);
            officeProviderServiceAPI.updateOfficeSpaceFacilityType(providerUserAccessToken, p1os2.getID(), "Garage");
            officeProviderServiceAPI.addOfficeSpaceFeature(providerUserAccessToken, p1os2.getID(), "Allows pets");
            Rating os2Rating = new Rating("joe_schmoe", (short) 5, "Great place");
            officeProviderServiceAPI.addOfficeSpaceRating(providerUserAccessToken, p1os2.getID(), os2Rating);

            System.out.println();
            System.out.println("Creating office space 'office_space_3' for provider_1");
            System.out.println("\tlat/long = 20.1/20.1; facilityType = 'Home', category = 'Dining room', features=WIFI, Coffee; average rating = 5");
            OfficeSpace p1os3 = officeProviderServiceAPI.createOfficeSpace(providerUserAccessToken);
            officeProviderServiceAPI.updateOfficeSpaceName(providerUserAccessToken, p1os3.getID(), "office_space_3");
            officeProviderServiceAPI.updateOfficeSpaceCapacity(providerUserAccessToken, p1os3.getID(),
                    (short) 1, (short) 1, (float) 1.0);
            officeProviderServiceAPI.updateOfficeSpaceLocation(providerUserAccessToken, p1os3.getID(),
                    "P1OS3 Street1", "P1OS3 Street2", "Cambridge", "MA", "USA", "12345",
                    20.1, 20.1);
            officeProviderServiceAPI.addOfficeSpaceRate(providerUserAccessToken, p1os3.getID(), "Daily", 10);
            officeProviderServiceAPI.updateOfficeSpaceFacilityType(providerUserAccessToken, p1os3.getID(), "Home");
            officeProviderServiceAPI.updateOfficeSpaceFacilitySubtype(providerUserAccessToken, p1os3.getID(), "Dining room");
            officeProviderServiceAPI.addOfficeSpaceFeature(providerUserAccessToken, p1os3.getID(), "WIFI");
            officeProviderServiceAPI.addOfficeSpaceFeature(providerUserAccessToken, p1os3.getID(), "Coffee");
            Rating os3Rating = new Rating("joe_schmoe", (short) 5, "Great place");
            officeProviderServiceAPI.addOfficeSpaceRating(providerUserAccessToken, p1os3.getID(), os3Rating);

            System.out.println();
            System.out.println("Creating office space 'office_space_4' for provider_1");
            System.out.println("\tlat/long = 20.2/20.2; facilityType = 'Garage', features=WIFI; average rating = 5");
            OfficeSpace p1os4 = officeProviderServiceAPI.createOfficeSpace(providerUserAccessToken);
            officeProviderServiceAPI.updateOfficeSpaceName(providerUserAccessToken, p1os4.getID(), "office_space_4");
            officeProviderServiceAPI.updateOfficeSpaceCapacity(providerUserAccessToken, p1os4.getID(),
                    (short) 1, (short) 1, (float) 1.0);
            officeProviderServiceAPI.updateOfficeSpaceLocation(providerUserAccessToken, p1os4.getID(),
                    "P1OS4 Street1", "P1OS4 Street2", "Cambridge", "MA", "USA", "12345",
                    20.2, 20.2);
            officeProviderServiceAPI.addOfficeSpaceRate(providerUserAccessToken, p1os4.getID(), "Daily", 10);
            officeProviderServiceAPI.updateOfficeSpaceFacilityType(providerUserAccessToken, p1os4.getID(), "Garage");
            officeProviderServiceAPI.addOfficeSpaceFeature(providerUserAccessToken, p1os4.getID(), "WIFI");
            Rating os4Rating = new Rating("joe_schmoe", (short) 5, "Great place");
            officeProviderServiceAPI.addOfficeSpaceRating(providerUserAccessToken, p1os4.getID(), os4Rating);

            System.out.println();
            System.out.println("Logging out as provider user, logging in as renter user and carrying out original assignment 3 tests:");
            authService.logout(providerUserAccessToken);
            renterUserAccessToken = authService.login(renterUserLoginID, renterUserPassword);

            System.out.println();
            System.out.println("Creating user 'renter_1' and associated renter profile");
            System.out.println("\tLocated at lat/long = 10/10; requires average rating = 5");
            ContactInfo renter1ContactInfo = new ContactInfo("E-mail", "renter_1@gmail.com");
            Image renter1Picture = new Image("renter_1 picture", "renter_1 picture description", new URI(sampleURI));
            cscie97.asn4.squaredesk.renter.User renter1 = userAPI.createUser(renterUserAccessToken, "renter_1", renter1ContactInfo,
                    renter1Picture, "PayPal account renter_1-XXXX-XXXX");
            Renter newRenter1 = renterAPI.createRenter(renterUserAccessToken);
            userAPI.updateUserRenter(renterUserAccessToken, renter1.getID(), newRenter1);

            // Add renter 1 search criteria
            renterAPI.updateRenterLocationCriteria(renterUserAccessToken, renter1.getRenter().getID(), 10.0, 10.0);
            renterAPI.updateRenterMinimumRatingCriteria(renterUserAccessToken, renter1.getRenter().getID(), (short) 5);

            // Create renter 2 user + renter profile
            System.out.println();
            System.out.println("Creating user 'renter_2' and associated renter profile");
            System.out.println("\tLocated at lat/long = 20/20; requires average rating = 5");

            ContactInfo renter2ContactInfo = new ContactInfo("E-mail", "renter_2@gmail.com");
            Image renter2Picture = new Image("Renter2 picture", "renter_2 picture description", new URI(sampleURI));
            cscie97.asn4.squaredesk.renter.User renter2 = userAPI.createUser(renterUserAccessToken, "renter_2", renter2ContactInfo,
                    renter2Picture, "PayPal account renter_2-XXXX-XXXX");
            Renter newRenter2 = renterAPI.createRenter(renterUserAccessToken);
            userAPI.updateUserRenter(renterUserAccessToken, renter2.getID(), newRenter2);

            // Add renter 2 search criteria
            renterAPI.updateRenterLocationCriteria(renterUserAccessToken, renter2.getRenter().getID(), 20.0, 20.0);
            renterAPI.updateRenterMinimumRatingCriteria(renterUserAccessToken, renter2.getRenter().getID(), (short) 5);

            // Print out search results
            System.out.println();
            System.out.println("Office spaces corresponding to renter 1's search criteria:");
            RenterCriteria renter1Criteria = renterAPI.getRenterCriteria(renterUserAccessToken, renter1.getRenter().getID());
            ArrayList<UUID> renter1OS = searchEngine.searchForOfficeSpaces(renterUserAccessToken, renter1Criteria);
            for (int i = 0; i < renter1OS.size(); i++) {
                OfficeSpace officeSpace = officeProviderServiceAPI.getOfficeSpace(renterUserAccessToken, renter1OS.get(i));
                System.out.println("\t- " + officeSpace.getName());
            }

            System.out.println();
            System.out.println("Office spaces corresponding to renter 2's search criteria:");
            RenterCriteria renter2Criteria = renterAPI.getRenterCriteria(renterUserAccessToken, renter2.getRenter().getID());
            ArrayList<UUID> renter2OS = searchEngine.searchForOfficeSpaces(renterUserAccessToken, renter2Criteria);
            for (int i = 0; i < renter2OS.size(); i++) {
                OfficeSpace officeSpace = officeProviderServiceAPI.getOfficeSpace(renterUserAccessToken, renter2OS.get(i));
                System.out.println("\t- " + officeSpace.getName());
            }

            System.out.println();
            System.out.println("Adding features WIFI to renter 1's requirements.");
            renterAPI.addRenterFeatureCriteria(renterUserAccessToken, renter1.getRenter().getID(), "WIFI");
            System.out.println("Office spaces now corresponding to renter 1's search criteria:");
            renter1Criteria = renterAPI.getRenterCriteria(renterUserAccessToken, renter1.getRenter().getID());
            renter1OS = searchEngine.searchForOfficeSpaces(renterUserAccessToken, renter1Criteria);
            for (int i = 0; i < renter1OS.size(); i++) {
                OfficeSpace officeSpace = officeProviderServiceAPI.getOfficeSpace(renterUserAccessToken, renter1OS.get(i));
                System.out.println("\t- " + officeSpace.getName());
            }

            System.out.println();
            System.out.println("Adding bad rating to office_space_1 to create average rating of 3.");
            os1Rating = new Rating("jane_doe", (short) 1, "Terrible!");
            officeProviderServiceAPI.addOfficeSpaceRating(renterUserAccessToken, p1os1.getID(), os1Rating);
            System.out.println("Office spaces now corresponding to renter 1's search criteria:");
            renter1Criteria = renterAPI.getRenterCriteria(renterUserAccessToken, renter1.getRenter().getID());
            renter1OS = searchEngine.searchForOfficeSpaces(renterUserAccessToken, renter1Criteria);
            for (int i = 0; i < renter1OS.size(); i++) {
                OfficeSpace officeSpace = officeProviderServiceAPI.getOfficeSpace(renterUserAccessToken, renter1OS.get(i));
                System.out.println("\t- " + officeSpace.getName());
            }

            System.out.println();
            System.out.println("Adding facility type 'Home'/category 'Dining room' to renter 2's requirements.");
            renterAPI.updateRenterFacilityTypeCriteria(renterUserAccessToken, renter2.getRenter().getID(), "Home");
            renterAPI.updateRenterFacilitySubtypeCriteria(renterUserAccessToken, renter2.getRenter().getID(), "Dining room");
            System.out.println("Office spaces corresponding to renter 2's search criteria:");
            renter2Criteria = renterAPI.getRenterCriteria(renterUserAccessToken, renter2.getRenter().getID());
            renter2OS = searchEngine.searchForOfficeSpaces(renterUserAccessToken, renter2Criteria);
            for (int i = 0; i < renter2OS.size(); i++) {
                OfficeSpace officeSpace = officeProviderServiceAPI.getOfficeSpace(renterUserAccessToken, renter2OS.get(i));
                System.out.println("\t- " + officeSpace.getName());
            }

            System.out.println();
            System.out.println("Deleting office_space_3.");
            officeProviderServiceAPI.deleteOfficeSpace(renterUserAccessToken, p1os3.getID());
            System.out.println("Office spaces corresponding to renter 2's search criteria:");
            renter2Criteria = renterAPI.getRenterCriteria(renterUserAccessToken, renter2.getRenter().getID());
            renter2OS = searchEngine.searchForOfficeSpaces(renterUserAccessToken, renter2Criteria);
            for (int i = 0; i < renter2OS.size(); i++) {
                OfficeSpace officeSpace = officeProviderServiceAPI.getOfficeSpace(renterUserAccessToken, renter2OS.get(i));
                System.out.println("\t- " + officeSpace.getName());
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
        }
    }
}
