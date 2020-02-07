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
import java.net.URISyntaxException;
import java.rmi.AccessException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * This class specifically tests the OfficeSpace CRUD methods provided in the
 * ShareDesk Office Space Provider API (see assignment and design document 
 * for more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class OfficeSpaceTestDriver {

    private static OfficeProviderServiceAPI serviceAPI;
    private static AuthServiceImpl authService;
    
    public static void main(String[] args) {
        serviceAPI = OfficeProviderServiceAPI.getInstance();
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
        
        try {
            
            /*
             * Log on as super admin, create provider user and credential
             * and assign it the provider role.
             */
            System.out.println("Logging in as super_admin and creating provider user with provider role:");
            superAdminAccessToken = authService.login(superAdminID, superAdminPassword);
            authService.createUser(superAdminAccessToken, providerUserName, providerUserID);
            authService.addUserCredential(superAdminAccessToken, providerUserID, providerUserLoginID, providerUserPassword);
            authService.addUserRole(superAdminAccessToken, providerUserID, "squaredesk_provider");
            authService.logout(superAdminAccessToken);
            System.out.println();
            
            /*
             * Log on as provider user to carry out test operations.
             */
            System.out.println("Logging in as provider user and carrying out original assignment 2 office space tests:");
            providerUserAccessToken = authService.login(providerUserLoginID, providerUserPassword);
            
            /*
             * Input test data.
             * 
             * Use API to create new Provider and add data.
             */
            Location osLocation = new Location("New street 1", "New street 2", "Williamstown",
                    "MA", "01267", "US", 4, 5);
            Capacity osCapacity = new Capacity((short) 4, (short) 4, (float) 100.0);
            Rate osRate = new Rate("Daily", 50);
            String osFacilityType = "Home";
            String osFacilitySubtype = "Living room";
            
                        OfficeSpace newOS = serviceAPI.createOfficeSpace(providerUserAccessToken, "Test office space name",
                    osLocation, osCapacity, osRate, osFacilityType, osFacilitySubtype);
            
           // Validate and print out new office space 
           String updatedOSName = serviceAPI.getOfficeSpaceName(providerUserAccessToken, newOS.getID());
           Location updatedOSLocation = serviceAPI.getOfficeSpaceLocation(providerUserAccessToken, newOS.getID());
           List<Rate> updatedOSRates = serviceAPI.getOfficeSpaceRates(providerUserAccessToken, newOS.getID());
           Capacity updatedOSCapacity = serviceAPI.getOfficeSpaceCapacity(providerUserAccessToken, newOS.getID());
           String updatedOSFacilityType = serviceAPI.getOfficeSpaceFacilityType(providerUserAccessToken, newOS.getID());
           String updatedOSFacilitySubtype = serviceAPI.getOfficeSpaceFacilitySubtype(providerUserAccessToken, newOS.getID());
           
           System.out.println("Validating and printing out new office space:");
           boolean validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           System.out.println("Name = '" + updatedOSName + "'");
           System.out.println("Location:");
           System.out.println("\tStreet1: " + updatedOSLocation.getStreet1());
           System.out.println("\tStreet2: " + updatedOSLocation.getStreet2());
           System.out.println("\tCity: " + updatedOSLocation.getCity());
           System.out.println("\tState: " + updatedOSLocation.getState());
           System.out.println("\tCountry: " + updatedOSLocation.getCountry());
           System.out.println("\tZip: " + updatedOSLocation.getZip());
           System.out.println("\tLatitude: " + updatedOSLocation.getLatitude());
           System.out.println("\tLongitude: " + updatedOSLocation.getLongitude());
           System.out.println("Rate: Period = '" + updatedOSRates.get(0).getRentalPeriod() 
                   + "', rate amount = " + updatedOSRates.get(0).getRate());
           System.out.println("Capacity: max occupants = " + updatedOSCapacity.getMaxOccupants()
                   + ", workspaces = " + updatedOSCapacity.getWorkspaces()
                   + ", square footage = " + updatedOSCapacity.getSquareFootage());
           System.out.println("Facility type = '" + updatedOSFacilityType + "'");
           System.out.println("Facility subtype = '" + updatedOSFacilitySubtype + "'");
           
           // Test feature exceptions
           System.out.println();
           serviceAPI.addOfficeSpaceFeature(providerUserAccessToken, newOS.getID(), "WIFI");
           System.out.println("Adding same feature to induce exception:");
           serviceAPI.addOfficeSpaceFeature(providerUserAccessToken, newOS.getID(), "WIFI");
           System.out.println("Attempting to remove non-existent feature to induce exception:");
           serviceAPI.removeOfficeSpaceFeature(providerUserAccessToken, newOS.getID(), "Pets allowed");
          
           // Test common access exceptions
           System.out.println();
           serviceAPI.addOfficeSpaceCommonAccess(providerUserAccessToken, newOS.getID(), "Kitchen");
           System.out.println("Adding same common access area to induce exception:");
           serviceAPI.addOfficeSpaceCommonAccess(providerUserAccessToken, newOS.getID(), "Kitchen");
           System.out.println("Attempting to remove non-existent common access area to induce exception:");
           serviceAPI.removeOfficeSpaceCommonAccess(providerUserAccessToken, newOS.getID(), "Conference room");
           
           // Test location exceptions
           System.out.println();
           System.out.println("Entering location without street 1 to induce exception:");
           serviceAPI.updateOfficeSpaceLocation(providerUserAccessToken, newOS.getID(), 
                   "", "", "Williamstown", "MA", "US", "01267", 4, 5);
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           System.out.println("Entering location without city to induce exception:");
           serviceAPI.updateOfficeSpaceLocation(providerUserAccessToken, newOS.getID(), 
                   "Street 1", "", "", "MA", "US", "01267", 4, 5);
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           System.out.println("Entering location without country to induce exception:");
           serviceAPI.updateOfficeSpaceLocation(providerUserAccessToken, newOS.getID(), 
                   "Street 1", "", "Williamstown", "MA", "", "01267", 4, 5);
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           System.out.println("Entering location without valid country to induce exception:");
           serviceAPI.updateOfficeSpaceLocation(providerUserAccessToken, newOS.getID(), 
                   "Street 1", "", "Williamstown", "MA", "XXXX", "01267", 4, 5);
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           
            // Re-enter valid location data
           serviceAPI.updateOfficeSpaceLocation(providerUserAccessToken, newOS.getID(), 
                   "Street 1", "", "Williamstown", "MA", "US", "01267", 4, 5);
           
           // Test capacity exceptions
           System.out.println();
           System.out.println("Changing maximum occupants to 0 to induce exception:");
           serviceAPI.updateOfficeSpaceCapacity(providerUserAccessToken, newOS.getID(), (short) 0, (short) 1, (float) 100.0);
           osCapacity.setMaxOccupants((short) 0);
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           System.out.println("Changing workspaces to 0 to induce exception:");
           serviceAPI.updateOfficeSpaceCapacity(providerUserAccessToken, newOS.getID(), (short) 1, (short) 0, (float) 100.0);
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           
           // Re-enter valid capacity data
           serviceAPI.updateOfficeSpaceCapacity(providerUserAccessToken, newOS.getID(), (short) 4, (short) 4, (float) 100.0);
           
           // Test facility type/facility subtype exceptions
           System.out.println();
           System.out.println("Changing \"Home\" facility subtype to blank to induce exception:");
           serviceAPI.updateOfficeSpaceFacilitySubtype(providerUserAccessToken, newOS.getID(), "");
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           
           // Re-enter valid facility data
           serviceAPI.updateOfficeSpaceFacilitySubtype(providerUserAccessToken, newOS.getID(), "Living room");
           
           // Test rate exceptions
           System.out.println();
           System.out.println("Adding already-existing office space rate to induce exception:");
           serviceAPI.addOfficeSpaceRate(providerUserAccessToken, newOS.getID(), "Daily", 400);
           System.out.println("Attempting to remove non-existent office space rate to induce exception:");
           serviceAPI.removeOfficeSpaceRate(providerUserAccessToken, newOS.getID(), "Monthly");
           System.out.println("Deleting only rate associated with office space to induce exception:");
           serviceAPI.removeOfficeSpaceRate(providerUserAccessToken, newOS.getID(), "Daily");
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           
           // Restore rate data
           serviceAPI.addOfficeSpaceRate(providerUserAccessToken, newOS.getID(), osRate);
           
           // Re-validate and print out final data
           System.out.println();
           System.out.println("Validating and printing out final valid office space:");
           validOS = serviceAPI.validOfficeSpace(providerUserAccessToken, newOS.getID());
           System.out.println("Name = '" + updatedOSName + "'");
           System.out.println("Location:");
           System.out.println("\tStreet1: " + updatedOSLocation.getStreet1());
           System.out.println("\tStreet2: " + updatedOSLocation.getStreet2());
           System.out.println("\tCity: " + updatedOSLocation.getCity());
           System.out.println("\tState: " + updatedOSLocation.getState());
           System.out.println("\tCountry: " + updatedOSLocation.getCountry());
           System.out.println("\tZip: " + updatedOSLocation.getZip());
           System.out.println("\tLatitude: " + updatedOSLocation.getLatitude());
           System.out.println("\tLongitude: " + updatedOSLocation.getLongitude());
           System.out.println("Rate: Period = '" + updatedOSRates.get(0).getRentalPeriod() 
                   + "', rate amount = " + updatedOSRates.get(0).getRate());
           System.out.println("Capacity: max occupants = " + updatedOSCapacity.getMaxOccupants()
                   + ", workspaces = " + updatedOSCapacity.getWorkspaces()
                   + ", square footage = " + updatedOSCapacity.getSquareFootage());
           System.out.println("Facility type = '" + updatedOSFacilityType + "'");
           System.out.println("Facility subtype = '" + updatedOSFacilitySubtype + "'");

           authService.logout(providerUserAccessToken);
        } catch (OfficeSpaceAlreadyExistsException osaee) {
            System.out.println("TestDriver main: OfficeSpaceAlreadyExistsException occurred.");
        } catch (OfficeSpaceNotFoundException osnfee) {
            System.out.println("TestDriver main: OfficeSpaceNotFoundException occurred.");
        } catch (RateAlreadyExistsException rnfe) {
            System.out.println("TestDriver main: RateNotFoundException occurred.");
        } catch (InvalidAccessTokenException iate) {
            System.out.println("TestDriver main: RateNotFoundException occurred.");
        } catch (UnauthorizedAccessException uae) {
            System.out.println("TestDriver main: RateNotFoundException occurred.");
        } catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred: " + ase.getMessage());
        } 
    }
}
