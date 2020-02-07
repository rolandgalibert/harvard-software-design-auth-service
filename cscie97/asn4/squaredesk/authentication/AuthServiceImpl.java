/*
 * AuthServiceImpl
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
import java.util.HashMap;

/**
 * This class implements the AuthService interface to provide methods for
 * authenticating users and creating a hierarchy-based set of permissions for
 * applications.
 *
 * Please see the requirements document for more details.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class AuthServiceImpl implements AuthService, AuthServiceVisitorElement {

    /**
     * Active access token ID-to-User map.
     */
    private HashMap<String, User> activeAccessTokenUserMap = new HashMap<>();
    /**
     * Active access token ID-to-login ID map.
     */
    private HashMap<String, String> activeAccessTokenLoginIDMap = new HashMap<>();
    /**
     * Login ID-to-User ID map.
     */
    private HashMap<String, String> loginIDUserIDMap = new HashMap<>();
    /**
     * Map of permission IDs to list of Users authorized with that permission.
     */
    private HashMap<String, ArrayList<User>> permissionUserMap = new HashMap<>();
    /**
     * Service ID-to-Service map.
     */
    private HashMap<String, Service> serviceMap = new HashMap<>();
    /**
     * Permission ID-to-Permission map.
     */
    private HashMap<String, Permission> permissionMap = new HashMap<>();
    /**
     * Role ID-to-Role map.
     */
    private HashMap<String, Role> roleMap = new HashMap<>();
    /**
     * User ID-to-User map.
     */
    private HashMap<String, User> userMap = new HashMap<>();
    /**
     * Access token ID-to-Access Token map (both active and expired).
     */
    private HashMap<String, AccessToken> accessTokenMap = new HashMap<>();

    /**
     * Private hidden singleton constructor.
     */
    private AuthServiceImpl() {
        bootstrap();
    }

    /**
     * AuthServiceImplHolder is loaded on the first execution of
     * AuthServiceImpl.getInstance() or the first access to
     * AuthServiceImplHolder.AUTH_SERVICE_INSTANCE, not before.
     */
    private static class AuthServiceImplHolder {

        private static final AuthServiceImpl AUTH_SERVICE_INSTANCE = new AuthServiceImpl();
    }

    /**
     * Returns singleton object instance.
     *
     * @return Singleton AuthServiceImpl object.
     */
    public static AuthServiceImpl getInstance() {
        return AuthServiceImplHolder.AUTH_SERVICE_INSTANCE;
    }

    /**
     * This protected method returns the AuthServiceImpl's map of Service objects.
     * 
     * @return Service object map.
     */
    protected HashMap<String, Service> getServiceMap() {
        return serviceMap;
    }

    /**
     * This protected method returns the AuthServiceImpl's map of Permission objects.
     * 
     * @return Permission object map.
     */
    protected HashMap<String, Permission> getPermissionMap() {
        return permissionMap;
    }

    /**
     * This protected method returns the AuthServiceImpl's map of Role objects.
     * 
     * @return Role object map.
     */
    protected HashMap<String, Role> getRoleMap() {
        return roleMap;
    }

    /**
     * This protected method returns the AuthServiceImpl's map of User objects.
     * 
     * @return User object map.
     */
    protected HashMap<String, User> getUserMap() {
        return userMap;
    }

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
    @Override
    public Service createService(String accessToken, String ID, String name, String description)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("create_service", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (serviceMap.containsKey(ID)) {
            throw new AuthServiceException("Service ID already exists.");
        } else {
            Service newService = new Service(ID, name, description);
            serviceMap.put(ID, newService);
            return newService;
        }
    }

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
    @Override
    public Service updateServiceDescription(String accessToken, String serviceID, String newDescription)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("update_service_description", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!serviceMap.containsKey(serviceID)) {
            throw new AuthServiceException("Service with specified ID does not exist.");
        } else {
            Service service = serviceMap.get(serviceID);
            service.setDescription(newDescription);
            return service;
        }
    }

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
    @Override
    public void removeService(String accessToken, String serviceID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("remove_service", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!serviceMap.containsKey(serviceID)) {
            throw new AuthServiceException("Service with specified ID does not exist.");
        } else {
            serviceMap.remove(serviceID);
        }
    }

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
    @Override
    public Service addServicePermission(String accessToken, String serviceID, String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("add_service_permission", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!serviceMap.containsKey(serviceID)) {
            throw new AuthServiceException("Service with specified ID does not exist.");
        } else {
            Service service = serviceMap.get(serviceID);
            if (service.getPermissions().contains(permissionID)) {
                throw new AuthServiceException("Service already includes specified permission.");
            } else {
                service.getPermissions().add(permissionID);
                return service;
            }
        }
    }

    /**
     * This method removes a permission (restricted method) from the specified Service
     * object.
     * 
     * @param accessToken Client access token.
     * @param serviceID Specified Service object ID.
     * @param permissionID ID of permission to be added.
     * @return
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    @Override
    public Service removeServicePermission(String accessToken, String serviceID,
            String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("remove_service_permission", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!serviceMap.containsKey(serviceID)) {
            throw new AuthServiceException("Service with specified ID does not exist.");
        } else {
            Service service = serviceMap.get(serviceID);
            if (!service.getPermissions().contains(permissionID)) {
                throw new AuthServiceException("Service does not include specified permission.");
            } else {
                service.getPermissions().remove(permissionID);
                permissionMap.remove(permissionID);
                return service;
            }
        }
    }

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
    @Override
    public Permission createPermission(String accessToken, String ID, String name, String description)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("create_permission", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (permissionMap.containsKey(ID)) {
            throw new AuthServiceException("Permission with specified ID already exists.");
        } else {
            Permission newPermission = new Permission(ID, name, description);
            permissionMap.put(ID, newPermission);
            return newPermission;
        }
    }

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
    @Override
    public Permission updatePermissionDescription(String accessToken,
            String permissionID, String newDescription)
            throws InvalidAccessTokenException, UnauthorizedAccessException, AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("update_permission_description", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!permissionMap.containsKey(permissionID)) {
            throw new AuthServiceException("Permission with specified ID does not exist.");
        } else {
            Permission permission = permissionMap.get(permissionID);
            permission.setDescription(newDescription);
            return permission;
        }
    }

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
    @Override
    public Role createRole(String accessToken, String ID, String name, String description)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("create_role", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (roleMap.containsKey(ID)) {
            throw new AuthServiceException("Role with specified ID already exists.");
        } else {
            Role newRole = new Role(ID, name, description);
            roleMap.put(ID, newRole);
            return newRole;
        }
    }

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
    @Override
    public Role updateRoleDescription(String accessToken, String roleID, String newDescription)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("update_role_description", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!roleMap.containsKey(roleID)) {
            throw new AuthServiceException("Role with specified ID does not exist.");
        } else {
            Role role = roleMap.get(roleID);
            role.setDescription(newDescription);
            return role;
        }
    }

    /**
     * This method removes the specified Role from the authentication service.
     * 
     * @param accessToken Client access token.
     * @param roleID ID of Role object to be removed.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    @Override
    public void removeRole(String accessToken, String roleID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("remove_role", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!roleMap.containsKey(roleID)) {
            throw new AuthServiceException("Role with specified ID does not exist.");
        } else {
            Role role = roleMap.remove(roleID);
        }
    }

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
    @Override
    public Role addRolePermission(String accessToken, String roleID, String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("add_role_permission", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!roleMap.containsKey(roleID)) {
            throw new AuthServiceException("Role with specified ID does not exist.");
        } else {
            Role role = roleMap.get(roleID);
            if (role.getPermissions().contains(permissionID)) {
                throw new AuthServiceException("Role already includes specified permission.");
            } else {
                role.getPermissions().add(permissionID);
                return role;
            }
        }
    }

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
    @Override
    public void removeRolePermission(String accessToken, String roleID,
            String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("remove_role_permission", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!roleMap.containsKey(roleID)) {
            throw new AuthServiceException("Role with specified ID does not exist.");
        } else {
            Role role = roleMap.get(roleID);
            if (!role.getPermissions().contains(permissionID)) {
                throw new AuthServiceException("Role does not include specified permission.");
            } else {
                role.getPermissions().remove(permissionID);
            }
        }
    }

    /**
     * This method adds the specified entitlement to the specified Role.
     * 
     * @param accessToken Client access token.
     * @param roleID ID of Role object to be modified.
     * @param entitlementID ID of entitlement to be added.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void addRoleEntitlement(String accessToken, String roleID, String entitlementID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("add_role_entitlement", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!roleMap.containsKey(roleID)) {
            throw new AuthServiceException("Role with specified ID does not exist.");
        } else {
            Role role = roleMap.get(roleID);
            if (permissionMap.containsKey(entitlementID)) {
                role.addPermission(entitlementID);
            } else if (roleMap.containsKey(entitlementID)) {
                role.addSubrole(entitlementID);
            }
        }
    }

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
    @Override
    public User createUser(String accessToken, String userName, String userID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("create_user", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID already exists.");
        } else {
            User newUser = new User(userName, userID);
            userMap.put(userID, newUser);
            addUserRole(accessToken, userID, "user_role");
            return newUser;
        }
    }

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
    @Override
    public User updateUserName(String accessToken, String userID, String newName)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("update_user_name", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID does not exist.");
        } else {
            User user = userMap.get(userID);
            user.setUserName(newName);
            return user;
        }
    }

    /**
     * This method adds a credential (consisting of a login ID and password)
     * to the specified User.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User to be modified.
     * @param loginID New Credential login ID.
     * @param password New Credential password.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void addUserCredential(String accessToken, String userID, String loginID,
            String password)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("add_user_credential", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID does not exist.");
        } else {
            User user = userMap.get(userID);
            user.addCredential(loginID, password);
            loginIDUserIDMap.put(loginID, userID);
        }
    }

    /**
     * This method removes a credential (specified by loginID) from the specified
     * User.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User object to be modified.
     * @param loginID ID specifying Credential to be removed.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    public void removeUserCredential(String accessToken, String userID, String loginID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("remove_user_credential", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID does not exist.");
        } else {
            User user = userMap.get(userID);
            if (user.credentialExists(loginID)) {
                user.removeCredential(loginID);
            }
        }
    }

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
    @Override
    public User updateUserPassword(String accessToken, String userID, String loginID, String newPassword)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("update_user_password", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID does not exist.");
        } else {
            User user = userMap.get(userID);
            if (user.credentialExists(loginID)) {
                user.changePassword(loginID, newPassword);
            }
            return user;
        }
    }

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
    @Override
    public void addUserPermission(String accessToken, String userID, String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("add_user_permission", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID does not exist.");
        } else {
            User user = userMap.get(userID);
            if (user.getPermissions().contains(permissionID)) {
                throw new AuthServiceException("User already has specified permission.");
            } else {
                user.getPermissions().add(permissionID);
                if (!permissionUserMap.containsKey(permissionID)) {
                    ArrayList<User> newUserList = new ArrayList<>();
                    permissionUserMap.put(permissionID, newUserList);
                }
                permissionUserMap.get(permissionID).add(user);
            }
        }
    }

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
    @Override
    public void removeUserPermission(String accessToken, String userID, String permissionID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("remove_user_permission", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID does not exist.");
        } else {
            User user = userMap.get(userID);
            if (!user.getPermissions().contains(permissionID)) {
                throw new AuthServiceException("User does not own specified permission.");
            } else {
                user.getPermissions().remove(permissionID);
                permissionUserMap.get(permissionID).remove(user);
            }
        }
    }

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
    @Override
    public void addUserRole(String accessToken, String userID, String roleID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("add_user_role", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID does not exist.");
        } else {
            User user = userMap.get(userID);
            if (user.getRoles().contains(roleID)) {
                throw new AuthServiceException("User already has specified role.");
            } else {
                user.getRoles().add(roleID);
                Role role = roleMap.get(roleID);
                addRoleToPermissionUserMap(role, user);
            }
        }
    }

    /**
     * This recursive method updates the permissionUserMap by adding a 
     * permission-to-user ID element to the map, associating each Permission in the Role
     * to the specified User.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User to whom a Role is to be added.
     * @param roleID ID of Role to be removed.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    private void addRoleToPermissionUserMap(Role role, User user) {
        for (int i = 0; i < role.getPermissions().size(); i++) {
            if (!permissionUserMap.containsKey(role.getPermissions().get(i))) {

                ArrayList<User> newUserList = new ArrayList<>();
                permissionUserMap.put(role.getPermissions().get(i), newUserList);
                permissionUserMap.get(role.getPermissions().get(i)).add(user);

            } else {
                if (!permissionUserMap.get(role.getPermissions().get(i)).contains(user)) {
                    permissionUserMap.get(role.getPermissions().get(i)).add(user);
                }
            }
        }
        for (int i = 0; i < role.getSubroles().size(); i++) {
            Role subrole = roleMap.get(role.getSubroles().get(i));
            addRoleToPermissionUserMap(subrole, user);
        }
    }

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
    @Override
    public void removeUserRole(String accessToken, String userID, String roleID)
            throws InvalidAccessTokenException, UnauthorizedAccessException,
            AuthServiceException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!checkAccess("remove_user_role", accessToken)) {
            throw new UnauthorizedAccessException();
        } else if (!userMap.containsKey(userID)) {
            throw new AuthServiceException("User with specified ID does not exist.");
        } else {
            User user = userMap.get(userID);
            if (!user.getRoles().contains(roleID)) {
                throw new AuthServiceException("User does not have specified role.");
            } else {
                user.getRoles().remove(roleID);
                Role role = roleMap.get(roleID);
                removeRoleFromPermissionUserMap(role, user);
            }
        }
    }

    /**
     * This recursive method updates the permissionUserMap by deleting all 
     * permission-to-user ID elements in the map, where the permission is a
     * Permission in the Role and the user is the specified User.
     * to the specified User.
     * 
     * @param accessToken Client access token.
     * @param userID ID of User whose role is to be removed.
     * @param roleID ID of Role to be removed.
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     */
    private void removeRoleFromPermissionUserMap(Role role, User user) {
        for (int i = 0; i < role.getPermissions().size(); i++) {
            if (permissionUserMap.get(role.getPermissions().get(i)).contains(user)) {
                permissionUserMap.get(role.getPermissions().get(i)).remove(user);
            }
        }
        for (int i = 0; i < role.getSubroles().size(); i++) {
            Role subrole = roleMap.get(role.getSubroles().get(i));
            removeRoleFromPermissionUserMap(subrole, user);
        }
    }

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
    @Override
    public boolean checkAccess(String permissionID, String accessToken)
            throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(accessToken)) {
            throw new InvalidAccessTokenException();
        } else if (!activeAccessTokenUserMap.containsKey(accessToken)) {
            throw new InvalidAccessTokenException();
        } else {
            AccessToken accessTokenObject = accessTokenMap.get(accessToken);
            if (accessTokenObject.accessTokenExpired()) {
                accessTokenObject.setStateToExpired();
                throw new InvalidAccessTokenException();
            } else {
                User user = activeAccessTokenUserMap.get(accessToken);
                int i = 0;
                boolean userFound = false;
                while (!userFound && (i < permissionUserMap.get(permissionID).size())) {
                    if (permissionUserMap.get(permissionID).get(i).getUserID().equals(user.getUserID())) {
                        userFound = true;
                    }
                    i++;
                }
                if (!userFound) {
                    throw new UnauthorizedAccessException();
                } else {
                    accessTokenMap.get(accessToken).updateLastAccessTime();
                    return true;
                }
            }
        }
    }

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
    @Override
    public String login(String loginID, String password)
            throws InvalidUserIDException, InvalidPasswordException,
            AuthServiceException {
        if (!loginIDUserIDMap.containsKey(loginID)) {
            throw new InvalidUserIDException();
        } else {
            String userID = loginIDUserIDMap.get(loginID);
            User user = userMap.get(userID);
            if (!user.getCredential(loginID).validPassword(password)) {
                throw new InvalidPasswordException();
            } else {
                if (activeAccessTokenLoginIDMap.containsValue(loginID)) {
                    throw new AuthServiceException("User is already logged in under login ID");
                } else {
                    AccessToken accessToken = new AccessToken();
                    user.addAccessToken(accessToken);
                    activeAccessTokenUserMap.put(accessToken.getID().toString(), user);
                    accessTokenMap.put(accessToken.getID().toString(), accessToken);
                    activeAccessTokenLoginIDMap.put(accessToken.getID().toString(), loginID);
                    return accessToken.getID().toString();
                }
            }
        }
    }

    /**
     * This method logs out the User associated with the specified access token.
     * 
     * @param accessToken Client access token.
     * @throws InvalidAccessTokenException
     */
    @Override
    public void logout(String accessToken)
            throws InvalidAccessTokenException {
        if (!activeAccessTokenUserMap.containsKey(accessToken)) {
            throw new InvalidAccessTokenException();
        } else {
            AccessToken accessTokenObject = accessTokenMap.get(accessToken);
            if (accessTokenObject.accessTokenExpired()) {
                accessTokenObject.setStateToExpired();
                throw new InvalidAccessTokenException();
            } else {
                activeAccessTokenUserMap.remove(accessToken);
                activeAccessTokenLoginIDMap.remove(accessToken);
                accessTokenMap.get(accessToken).setStateToExpired();
            }
        }
    }

    /**
     * This method runs a simple check of the given access token and returns
     * true if it is valid (non-null and non-empty).
     * 
     * @param accessToken Client access token.
     * @return True if access token is valid.
     */
    private boolean validAccessToken(String accessToken) {
        if ((accessToken != null) && (accessToken.length() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to accept an AuthServiceVisitor object.
     * 
     * @param visitor AuthServiceVisitor object.
     */
    @Override
    public void acceptVisitor(AuthServiceVisitor visitor) {
        visitor.visitAuthService(this);
    }

    /**
     * This method registers the (present) authentication service as well as its
     * restricted methods (i.e. the methods in this class) in the corresponding
     * maps. It also creates a SuperAdmin role with permissions for all these
     * methods, and also creates a superadmin user to which this role is
     * assigned.
     *
     * All this is done while circumventing the normal access token checks for
     * these methods.
     */
    private void bootstrap() {

        /*
         * Create the service and add it to the service map
         */
        Service authService = new Service("auth_service", "Authentication Service", "Authentication Service");
        serviceMap.put("auth_service", authService);

        /*
         * Create a superadmin role and add the above permissions to it
         */
        Role superAdminRole = new Role("super_admin", "Super Admin", "Super admin");
        roleMap.put(superAdminRole.getID(), superAdminRole);
        Role userRole = new Role("user_role", "User Role", "User role");
        roleMap.put(userRole.getID(), userRole);

        /*
         * Create restricted method permissions and add these to the service, to
         * the super admin role and to the permission map
         */
        Permission permission = new Permission("create_service", "Create Service", "Create Service");
        authService.addPermission("create_service");
        superAdminRole.getPermissions().add("create_service");
        permissionMap.put("create_service", permission);

        permission = new Permission("update_service_description", "Update Service Description", "Update service description");
        authService.addPermission("update_service_description");
        superAdminRole.getPermissions().add("update_service_description");
        permissionMap.put("update_service_description", permission);

        permission = new Permission("remove_service", "Remove Service", "Remove service");
        authService.addPermission("remove_service");
        superAdminRole.getPermissions().add("remove_service");
        permissionMap.put("remove_service", permission);

        permission = new Permission("add_service_permission", "Add Service Permission", "Add service permission");
        authService.addPermission("add_service_permission");
        superAdminRole.getPermissions().add("add_service_permission");
        permissionMap.put("add_service_permission", permission);

        permission = new Permission("remove_service_permission", "Remove Service Permission", "Remove service permission");
        authService.addPermission("remove_service_permission");
        superAdminRole.getPermissions().add("remove_service_permission");
        permissionMap.put("remove_service_permission", permission);

        permission = new Permission("create_permission", "Create Permission", "Create permission");
        authService.addPermission("create_permission");
        superAdminRole.getPermissions().add("create_permission");
        permissionMap.put("create_permission", permission);

        permission = new Permission("update_permission_description", "Update Permission Description", "Update permission description");
        authService.addPermission("update_permission_description");
        superAdminRole.getPermissions().add("update_permission_description");
        permissionMap.put("update_permission_description", permission);

        permission = new Permission("create_role", "Create Role", "Create role");
        authService.addPermission("create_role");
        superAdminRole.getPermissions().add("create_role");
        permissionMap.put("create_role", permission);

        permission = new Permission("update_role_description", "Update Role Description", "Update role description");
        authService.addPermission("update_role_description");
        superAdminRole.getPermissions().add("update_role_description");
        permissionMap.put("update_role_description", permission);

        permission = new Permission("remove_role", "Remove Role", "Remove role");
        authService.addPermission("remove_role");
        superAdminRole.getPermissions().add("remove_role");
        permissionMap.put("remove_role", permission);

        permission = new Permission("add_role_permission", "Add Role Permission", "Add role permission");
        authService.addPermission("add_role_permission");
        superAdminRole.getPermissions().add("add_role_permission");
        permissionMap.put("add_role_permission", permission);

        permission = new Permission("remove_role_permission", "Remove Role Permission", "Remove role permission");
        authService.addPermission("remove_role_permission");
        superAdminRole.getPermissions().add("remove_role_permission");
        permissionMap.put("remove_role_permission", permission);

        permission = new Permission("add_role_entitlement", "Add Role Entitlement", "Add role entitlement");
        authService.addPermission("add_role_entitlement");
        superAdminRole.getPermissions().add("add_role_entitlement");
        permissionMap.put("add_role_entitlement", permission);

        permission = new Permission("create_user", "Create User", "Create user");
        authService.addPermission("create_user");
        superAdminRole.getPermissions().add("create_user");
        permissionMap.put("create_user", permission);

        permission = new Permission("update_user_name", "Update User Name", "Update user name");
        authService.addPermission("update_user_name");
        superAdminRole.getPermissions().add("update_user_name");
        userRole.getPermissions().add("update_user_name");
        permissionMap.put("update_user_name", permission);

        permission = new Permission("add_user_credential", "Add User Credential", "Add user credential");
        authService.addPermission("add_user_credential");
        superAdminRole.getPermissions().add("add_user_credential");
        userRole.getPermissions().add("add_user_credential");
        permissionMap.put("add_user_credential", permission);

        permission = new Permission("remove_user_credential", "Remove User Credential", "Remove user credential");
        authService.addPermission("remove_user_credential");
        superAdminRole.getPermissions().add("remove_user_credential");
        userRole.getPermissions().add("remove_user_credential");
        permissionMap.put("remove_user_credential", permission);

        permission = new Permission("update_user_password", "Update User Password", "Update user password");
        authService.addPermission("update_user_password");
        superAdminRole.getPermissions().add("update_user_password");
        userRole.getPermissions().add("update_user_password");
        permissionMap.put("update_user_password", permission);

        permission = new Permission("add_user_permission", "Add User Permission", "Add user permission");
        authService.addPermission("add_user_permission");
        superAdminRole.getPermissions().add("add_user_permission");
        permissionMap.put("add_user_permission", permission);

        permission = new Permission("remove_user_permission", "Remove User Permission", "Remove user permission");
        authService.addPermission("remove_user_permission");
        superAdminRole.getPermissions().add("remove_user_permission");
        permissionMap.put("remove_user_permission", permission);

        permission = new Permission("add_user_role", "Add User Role", "Add user role");
        authService.addPermission("add_user_role");
        superAdminRole.getPermissions().add("add_user_role");
        permissionMap.put("add_user_role", permission);

        permission = new Permission("remove_user_role", "Remove User Role", "Remove user role");
        authService.addPermission("remove_user_role");
        superAdminRole.getPermissions().add("remove_user_role");
        permissionMap.put("remove_user_role", permission);

        /*
         * Create super admin user and assign it the super admin role
         */
        User superAdminUser = new User("Super Admin", "super_admin");
        superAdminUser.addCredential("super_admin", "p4ssw0rd");
        userMap.put("super_admin", superAdminUser);
        loginIDUserIDMap.put("super_admin", "super_admin");
        superAdminUser.addRole(superAdminRole.getID());
        for (int i = 0; i < superAdminRole.getPermissions().size(); i++) {
            permissionUserMap.put(superAdminRole.getPermissions().get(i), new ArrayList<User>());
            permissionUserMap.get(superAdminRole.getPermissions().get(i)).add(superAdminUser);
        }
    }

    /**
     *
     * @return
     */
    public String currentConfiguration() {
        AuthServiceInventory authServiceInventory = new AuthServiceInventory();
        authServiceInventory.visitAuthService(this);
        return authServiceInventory.getInventoryDescription();
    }
}
