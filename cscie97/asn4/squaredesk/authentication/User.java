/*
 * User
 * 
 * Version 1.0
 * 
 * November 20, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #4.
 *
 */
package cscie97.asn4.squaredesk.authentication;

import java.util.ArrayList;

/**
 * This class is used to describe an AuthService user, including his/her name,
 * user ID, credentials and entitlements (permissions and roles).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class User implements AuthServiceVisitorElement {

    /**
     * User's name.
     */
    private String userName;
    
    /**
     * User's ID (immutable after initial creation).
     */
    private String userID;
    
    /**
     * List of credentials (login ID/password) associated with given User.
     */
    private ArrayList<Credential> credentials = new ArrayList<>();
    
    /**
     * List of permissions (to restricted methods) associated with given User.
     */
    private ArrayList<String> permissions = new ArrayList<>();

    /**
     * List of roles associated with given User.
     */
    private ArrayList<String> roles = new ArrayList<>();
    
    /**
     * List of all access tokens (active as well as expired) associated with 
     * given User.
     */
    private ArrayList<AccessToken> accessTokens = new ArrayList<>();

    /**
     * Constructor.
     * 
     * @param userName User's name.
     * @param userID User's ID.
     */
    public User(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
    }

    /**
     * Returns user's name.
     * 
     * @return User name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Updates user's name
     * 
     * @param newUserName New user name.
     */
    public void setUserName(String newUserName) {
        userName = newUserName;
    }

    /**
     * Returns user's ID.
     * 
     * @return User's ID.
     */
    public String getUserID() {
        return userID;
    }
    
    /**
     * Returns the User's credential associated with the specified login ID
     * (null if no such credential exists).
     * 
     * @param loginID Login ID of the desired credential.
     * @return Credential associated with loginID input (null if none exists).
     */
    public Credential getCredential(String loginID) {
        boolean credentialFound = false;
        int i = 0;
        while (!credentialFound && i < credentials.size()) {
            if (credentials.get(i).getLoginID().equalsIgnoreCase(loginID)) {
                credentialFound = true;
            } else {
                i++;
            }
        }
        if (credentialFound) {
            return credentials.get(i);
        } else {
            return null;
        }
    }
    
    /**
     * Returns list of all credentials (login ID/password) associated with
     * this User object.
     * 
     * @return List of User's credentials.
     */
    public ArrayList<Credential> getCredentials() {
        return credentials;
    }

    /**
     * Creates and adds the specified credential to the User's list of credentials.
     * 
     * @param loginID Login ID for new credential.
     * @param password Password for new credential
     */
    public void addCredential(String loginID, String password) {
        if (!credentialExists(loginID)) {
            credentials.add(new Credential(loginID, password));
        }
    }

    /**
     * Removes the credential with the specified login ID from the User's list of credentials.
     * 
     * @param loginID Login ID of credential to remove.
     */
    public void removeCredential(String loginID) {
        boolean credentialFound = false;
        int i = 0;
        while (!credentialFound && i < credentials.size()) {
            if (credentials.get(i).getLoginID().equalsIgnoreCase(loginID)) {
                credentials.remove(i);
                credentialFound = true;
            } else {
                i++;
            }
        }
    }

    /**
     * Returns true if the User owns a credential with the specified login ID.
     * 
     * @param loginID Login ID of desired credential.
     * @return True if such a credential exists, false otherwise.
     */
    public boolean credentialExists(String loginID) {
        boolean credentialFound = false;
        int i = 0;
        while (!credentialFound && i < credentials.size()) {
            if (credentials.get(i).getLoginID().equalsIgnoreCase(loginID)) {
                credentialFound = true;
            } else {
                i++;
            }
        }
        return credentialFound;
    }
    
    /**
     * Updates the credential associated with the specified login ID with a new password.
     * 
     * @param loginID Login ID of desired credential.
     * @param newPassword New password for this credential.
     */
    public void changePassword(String loginID, String newPassword) {
        boolean credentialFound = false;
        int i = 0;
        while (!credentialFound && i < credentials.size()) {
            if (credentials.get(i).getLoginID().equalsIgnoreCase(loginID)) {
                credentialFound = true;
            } else {
                i++;
            }
        }
        if (credentialFound) {
            credentials.get(i).setPasswordMessageDigest(newPassword);
        }
    }

    /**
     * Returns permissions associated with this User.
     * 
     * @return List of permissions.
     */
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    /**
     * Adds a permission to the list of permissions associated with this User.
     * 
     * @param permissionID ID of permission being added.
     */
    public void addPermission(String permissionID) {
        permissions.add(permissionID);
    }

    /**
     * Removes a permission from the list of permissions associated with this User.
     * 
     * @param permissionID ID of permission being removed.
     */
    public void removePermission(String permissionID) {
        permissions.remove(permissionID);
    }
    
    /**
     * Returns roles associated with this User.
     * 
     * @return List of User roles.
     */
    public ArrayList<String> getRoles() {
        return roles;
    }

    /**
     * Adds a role to the list of roles associated with this User.
     * 
     * @param roleID ID of role being added.
     */
    public void addRole(String roleID) {
        roles.add(roleID);
    }

    /**
     * Removes a role from the list of roles associated with this User.
     * 
     * @param roleID ID of role being removed.
     */
    public void removeRole(String roleID) {
        roles.remove(roleID);
    }

    /**
     * Returns access tokens associated with this User.
     * 
     * @return List of User's access tokens.
     */
    public ArrayList<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    /**
     * Adds the specified access token to the User's list of access tokens.
     * 
     * @param accessToken New User access token.
     */
    public void addAccessToken(AccessToken accessToken) {
        accessTokens.add(accessToken);
    }

    /**
     * Method to accept an AuthServiceVisitor object.
     * 
     * @param visitor AuthServiceVisitor object.
     */
    @Override
    public void acceptVisitor(AuthServiceVisitor visitor) {
        visitor.visitUser(this);
    }
}
