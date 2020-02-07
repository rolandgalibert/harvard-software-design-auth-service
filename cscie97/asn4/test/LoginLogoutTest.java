/*
 * LoginLogoutTest
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

/**
 * This class tests the login/logout functionality of the authentication service
 * (see assignment design document for more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class LoginLogoutTest {

    /**
     * Singleton authentication service object.
     */
    private static AuthServiceImpl authService = AuthServiceImpl.getInstance();

    public static void main(String[] args) {

        String superAdminID = "super_admin";
        String superAdminPassword = "p4ssw0rd";
        String superAdminAccessToken;

        String user1Name = "User1 Name";
        String user1ID = "user1";
        String user1LoginID = "user1loginID";
        String user1Password = "user1password";
        String user1AccessToken;

        /*
         * Login as superadmin and create user
         */

        try {
            System.out.println("Logging onto AuthService as super_admin and creating user1 user and credentials:");
            superAdminAccessToken = authService.login(superAdminID, superAdminPassword);
            authService.createUser(superAdminAccessToken, user1Name, user1ID);
            authService.addUserCredential(superAdminAccessToken, user1ID, user1LoginID, user1Password);
            authService.logout(superAdminAccessToken);
            System.out.println("Logging out as super_admin and logging in and out as user1 (no exceptions expected):");
            user1AccessToken = authService.login(user1LoginID, user1Password);
            authService.logout(user1AccessToken);
        } catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred.");
        } catch (InvalidAccessTokenException iate) {
            System.out.println("InvalidAccessTokenException occurred.");
        } catch (UnauthorizedAccessException uae) {
            System.out.println("UnauthorizedAccessException occurred.");
        }
        System.out.println();

        /*
         * Log in with non-existent user name
         */
        try {
            System.out.println("Logging onto AuthService with non-existent userID:");
            String newAccessToken = authService.login("non_existent_user", "password");
        } catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred.");
        }
        System.out.println();

        /*
         * Log in with invalid password
         */
        try {
            System.out.println("Logging onto AuthService with user1 loginID and bad password:");
            String newAccessToken = authService.login(user1LoginID, "badPassword");
        } catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred.");
        }
        System.out.println();

        /*
         * Log in and change password
         */
        try {
            System.out.println("Logging onto AuthService with user1 loginID, changing password, logging out and logging back in (no exceptions expected):");
            user1AccessToken = authService.login(user1LoginID, user1Password);
            user1Password = "newPassword";
            authService.updateUserPassword(user1AccessToken, user1ID, user1LoginID, user1Password);
            authService.logout(user1AccessToken);
            user1AccessToken = authService.login(user1LoginID, user1Password);
            authService.logout(user1AccessToken);
        } catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred.");
        } catch (InvalidAccessTokenException iate) {
            System.out.println("InvalidAccessTokenException occurred.");
        } catch (UnauthorizedAccessException uae) {
            System.out.println("UnauthorizedAccessException occurred.");
        }
        System.out.println();

        /*
         * Log out again (use of expired access token)
         */
        try {
            System.out.println("Logging in then logging out twice (use of expired access token):");
            user1AccessToken = authService.login(user1LoginID, user1Password);
            authService.logout(user1AccessToken);
            authService.logout(user1AccessToken);
        } catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred.");
        } catch (InvalidAccessTokenException iate) {
            System.out.println("InvalidAccessTokenException occurred.");
        }
        System.out.println();

        /*
         * Log in and attempt to log out after expiration period has expired
         */
        try {
            System.out.println("Logging in, sleeping past access token timeout period then attempting to change password (use of expired access token):");
            user1AccessToken = authService.login(user1LoginID, user1Password);
            Thread.sleep(2001);
            authService.updateUserPassword(user1AccessToken, user1ID, user1LoginID, user1Password);
        } catch (InvalidUserIDException iue) {
            System.out.println("InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("AuthServiceException occurred.");
        } catch (InvalidAccessTokenException iate) {
            System.out.println("InvalidAccessTokenException occurred.");
        } catch (UnauthorizedAccessException uae) {
            System.out.println("UnauthorizedAccessException occurred.");
        } catch (InterruptedException ie) {
            System.out.println("InterruptedException occurred.");
        }
        System.out.println();

    }
}
