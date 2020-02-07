/*
 * TestDriver
 * 
 * Version 1.0
 * 
 * November 20, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #4.
 */
package cscie97.asn4.test;

import cscie97.asn4.squaredesk.authentication.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class tests the authentication service using the test authentication file
 * provided. (see assignment design document for more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class TestDriver {

    /**
     * Singleton authentication service object.
     */
    private static AuthServiceImpl authService = AuthServiceImpl.getInstance();

    /**
     * @param args Command line arguments (args[0] contains name of test file.
     */
    public static void main(String[] args) {
        
        String fileName;
        if (args.length < 1) {
            fileName = "authentication2.csv";
        } else {
            fileName = args[0];
        }
        String superAdminID = "super_admin";
        String superAdminPassword = "p4ssw0rd";
        String superAdminAccessToken;
        try {
            
            /*
             * Log in as super admin
             */
            superAdminAccessToken = authService.login(superAdminID, superAdminPassword);
            
            /*
             * Loop through test file
             */
            BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
            String line = null;
            String[] parms;
            while ((line = inputStream.readLine()) != null) {
                
                /*
                 * Skip empty lines and comments
                 */
                if (!(line.isEmpty() || line.startsWith("#"))) {
                    parms = parseLine(line);
                    processLine(superAdminAccessToken, parms);
                }
            }
            
            /*
             * Generate and print out the inventory of all authentication service
             * objects
             */
            System.out.println(authService.currentConfiguration());
            
        } catch (IOException ioe) {
            System.out.println("I/O exception occurred.");
        }  catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        }  catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        }  catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred.");
        }  catch (InvalidAccessTokenException iate) {
            System.out.println("InvalidAccessTokenException occurred.");
        }  catch (UnauthorizedAccessException uae) {
            System.out.println("UnauthorizedAccessException occurred.");
        }
    }

    /**
     * This method parses an authentication2.csv input command line.
     * 
     * @param newLine Current test data line.
     * @return Array of command line parameters
     */
    private static String[] parseLine(String newLine) {
        String[] inputParms = newLine.split(",");
        return inputParms;
    }
    
    /**
     * This methods calls the appropriate authentication method on the basis
     * of the input parameters it receives.
     * 
     * @param accessToken Access token for using authentication service.
     * @param parms Input parameters.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException 
     */
    private static void processLine(String accessToken, String[] parms) 
            throws InvalidAccessTokenException, UnauthorizedAccessException, AuthServiceException {
        if (parms[0].trim().equalsIgnoreCase("define_service")) {
            authService.createService(accessToken, parms[1].trim(), parms[2].trim(), parms[3].trim());
        } else if (parms[0].trim().equalsIgnoreCase("define_permission")) {
            authService.createPermission(accessToken, parms[2].trim(), parms[3].trim(), parms[4].trim());
            authService.addServicePermission(accessToken, parms[1].trim(), parms[2].trim());
        } else if (parms[0].trim().equalsIgnoreCase("define_role")) {
            Role role = authService.createRole(accessToken, parms[1].trim(), parms[2].trim(), parms[3].trim());
        } else if (parms[0].trim().equalsIgnoreCase("add_entitlement_to_role")) {
            authService.addRoleEntitlement(accessToken, parms[1].trim(), parms[2].trim());
        } else if (parms[0].trim().equalsIgnoreCase("create_user")) {
            authService.createUser(accessToken, parms[2].trim(), parms[1].trim());
        } else if (parms[0].trim().equalsIgnoreCase("add_credential")) {
            authService.addUserCredential(accessToken, parms[1].trim(), parms[2].trim(), parms[3].trim());
        }  else if (parms[0].trim().equalsIgnoreCase("add_role_to_user")) {
            authService.addUserRole(accessToken, parms[1].trim(), parms[2].trim());
        }
    }
}
