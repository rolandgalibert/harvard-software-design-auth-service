/*
 * AuthService
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

/**
 * This interface defines the methods to be implemented by any class that
 * needs to act as an authentication service for a given applications.
 * 
 * Please see the requirements document for more details.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public interface AuthService {
    
    /**
     * This method creates the specified general Service and registers it in the
     * authentication service.
     *
     * @param accessToken Client access token.
     * @param ID ID assigned to new Service.
     * @param name Name assigned to new Service.
     * @param description Description of new Service.
     * @return New Service object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Service createService(String accessToken, String ID, String name, String description)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method allows the description associated with a Service object to
     * be updated.
     * 
     * @param accessToken Client access token.
     * @param serviceID ID associated with Service object.
     * @param newDescription New Service object description.
     * @return Updated Service object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Service updateServiceDescription(String accessToken, String serviceID, String newDescription)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;

    /**
     * This method removes the specified Service from the set of authenticated
     * Services.
     * 
     * @param accessToken Client access token.
     * @param serviceID ID associated with Service object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void removeService(String accessToken, String serviceID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;    
    
    /**
     * 
     * @param accessToken
     * @param serviceID
     * @param permissionID
     * @return
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Service addServicePermission(String accessToken, String serviceID, String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method adds a permission (restricted method) to the specified Service
     * object.
     * 
     * @param accessToken Client access token.
     * @param serviceID Specified Service object ID.
     * @param permissionID ID of permission to be added.
     * @return Updated Service object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Service removeServicePermission(String accessToken, String serviceID,
            String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method creates a permission (describing a restricted access method).
     * 
     * @param accessToken Client access token.
     * @param ID ID to be associated with the Permission object.
     * @param name Name to be associated with the Permission object.
     * @param description Permission object description.
     * @return Newly created Permission object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Permission createPermission(String accessToken, String ID, String name, String description)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method allows the description of the specified Permission to be
     * updated.
     * 
     * @param accessToken Client access token.
     * @param permissionID ID of Permission object to be updated.
     * @param newDescription New Permission object description.
     * @return Updated Permission object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Permission updatePermissionDescription(String accessToken, String permissionID, String newDescription)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException;

    /**
     * This method creates a role using the specified parameters.
     * 
     * @param accessToken Client access token.
     * @param ID ID assigned to Role object.
     * @param name Name assigned to Role object.
     * @param description Role description
     * @return Newly created Role object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Role createRole(String accessToken, String ID, String name, String description)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method updates the description associated with a Role object.
     * 
     * @param accessToken Client access token.
     * @param roleID ID assigned to Role object.
     * @param newDescription Updated Role description
     * @return Updated Role object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Role updateRoleDescription(String accessToken, String roleID, String newDescription)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException;

    /**
     * This method removes the specified Role from the authentication service.
     * 
     * @param accessToken Client access token.
     * @param roleID ID of Role object to be removed.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void removeRole(String accessToken, String roleID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method adds the specified Permission to the specified Role.
     * 
     * @param accessToken Client access token.
     * @param roleID ID of Role to be modified.
     * @param permissionID ID of permission to be added.
     * @return Updated Role object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public Role addRolePermission(String accessToken, String roleID, String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method removes the specified Permission from the specified Role.
     * 
     * @param accessToken Client access token.
     * @param roleID ID of Role to be modified.
     * @param permissionID ID of Permission to be removed.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void removeRolePermission(String accessToken, String roleID,
            String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method creates a User object using the specified parameters.
     * 
     * @param accessToken Client access token.
     * @param userName Name of new User.
     * @param userID ID of new User.
     * @return Newly created User object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public User createUser(String accessToken, String userName, String userID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method updates the Name of the specified User.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User to be modified.
     * @param newName New User name.
     * @return Updated User object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public User updateUserName(String accessToken, String userID, String newName)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method updates the password associated with the credential specified
     * by loginID for the specified user.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User who owns the credential.
     * @param loginID LoginID of Credential whose password is to be updated.
     * @param newPassword New password.
     * @return Updated User object.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public User updateUserPassword(String accessToken, String userID, String loginID, String newPassword)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method assigns the specified User the specified Permission.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User to be modified.
     * @param permissionID ID of Permission to be added.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void addUserPermission(String accessToken, String userID, String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method removes the specified Permission from the specified User.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User to be modified.
     * @param permissionID ID of Permission to be added.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void removeUserPermission(String accessToken, String userID, String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;

    /**
     * This method assigns the specified User the specified Role.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User to be modified.
     * @param roleID ID of Role to be added.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void addUserRole(String accessToken, String userID, String roleID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;
    
    /**
     * This method removes the specified Role from the specified User.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User to be modified.
     * @param roleID ID of Role to be removed.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void removeUserRole(String accessToken, String userID, String roleID)
            throws InvalidAccessTokenException, UnauthorizedAccessException, 
            AuthServiceException;

    /**
     * This method returns true if the user associated with the given access
     * token has the given permission (to a restricted access method).
     * 
     * @param permissionID ID of permission to check.
     * @param accessToken Client access token.
     * @return True if user associated with access token has this permission.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     */
    public boolean checkAccess(String permissionID, String userAccessToken)
            throws InvalidAccessTokenException, UnauthorizedAccessException;

    /**
     * This method attempts to log the specified User into the authentication
     * service, returning a valid access token upon successful login or throwing
     * an appropriate Exception if the login is unsuccessful.
     * 
     * @param loginID User Credential loginID.
     * @param password User Credential password.
     * @return Valid access token (upon successful login).
     * @throws InvalidUserIDException
     * @throws InvalidPasswordException
     * @throws AuthServiceException
     */
    public String login(String userID, String password)
            throws InvalidUserIDException, InvalidPasswordException,
            AuthServiceException;

    /**
     * This method logs out the User associated with the specified access token.
     * 
     * @param accessToken Client access token.
     * @throws InvalidAccessTokenException
     */
    public void logout(String accessToken)
            throws InvalidAccessTokenException;
    
}
