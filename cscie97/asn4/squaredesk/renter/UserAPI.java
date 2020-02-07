/*
 * UserAPI
 * 
 * Version 1.0
 * October 30, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #3.
 *
 */
package cscie97.asn4.squaredesk.renter;

import cscie97.asn4.squaredesk.authentication.*;
import cscie97.asn4.squaredesk.provider.ContactInfo;
import cscie97.asn4.squaredesk.provider.Image;
import cscie97.asn4.squaredesk.provider.Provider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This singleton class maintains a map of User objects for the SquareDesk
 * solution as well as corresponding CRUD methods for processing those objects.
 *
 * Only authenticated clients with appropriate permission may execute these
 * methods.
 *
 * UserAPI is constructed following the "Initialization-on-demand holder idiom"
 * singleton as described in the Wikipedia article "Singleton pattern".
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class UserAPI {

    /**
     * Map of active User objects (User UUID to User object).
     */
    private HashMap<UUID, User> userUUIDMap = new HashMap<UUID, User>();
    /**
     * Map of active User objects (User UUID to User name).
     */
    private HashMap<String, User> userNameMap = new HashMap<String, User>();
    /**
     * Authentication service object
     */
    private static AuthServiceImpl authService;

    /**
     * Private hidden singleton constructor.
     */
    private UserAPI() {
        authService = AuthServiceImpl.getInstance();
        registerWithAuthService();
    }

    /**
     * UserAPIHolder is loaded on the first execution of UserAPI.getInstance()
     * or the first access to UserAPIHolder.USER_API_INSTANCE, not before.
     */
    private static class UserAPIHolder {

        private static final UserAPI USER_API_INSTANCE = new UserAPI();
    }

    /**
     * Returns singleton object instance.
     *
     * @return Singleton UserAPI object.
     */
    public static UserAPI getInstance() {
        authService = AuthServiceImpl.getInstance();
        return UserAPIHolder.USER_API_INSTANCE;
    }

    /**
     * User object constructor with no input parameters.
     *
     * @param authToken Client authentication token.
     * @return Newly created User object.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User createUser(String authToken) throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("create_squaredesk_user", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User newUser = new User();
            userUUIDMap.put(newUser.getID(), newUser);
            return newUser;
        }
    }

    /**
     * User object constructor with only mandatory parameters input.
     *
     * @param authToken Client authentication token.
     * @param name New User name.
     * @param contactInfo Contact information for new User.
     * @param account New User account.
     * @return Newly created User object.
     * @throws UserAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User createUser(String authToken, String name,
            ContactInfo contactInfo, String account)
            throws UserAlreadyExistsException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("create_squaredesk_user", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (userNameMap.containsKey(name)) {
                throw new UserAlreadyExistsException();
            } else {
                User newUser = new User();
                newUser.setName(name);
                newUser.addContactInfo(contactInfo);
                newUser.setAccount(account);
                userUUIDMap.put(newUser.getID(), newUser);
                userNameMap.put(name, newUser);
                return newUser;
            }
        }
    }

    /**
     * User object constructor with all available parameters input.
     *
     * @param authToken Client authentication token.
     * @param name New User name.
     * @param contactInfo Contact information for new User.
     * @param picture Picture of new User.
     * @param account New User account.
     * @return Newly created User object.
     * @throws UserAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User createUser(String authToken, String name,
            ContactInfo contactInfo, Image picture, String account)
            throws UserAlreadyExistsException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("create_squaredesk_user", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (userNameMap.containsKey(name)) {
                throw new UserAlreadyExistsException();
            } else {
                User newUser = new User(name, contactInfo, picture, account);
                userUUIDMap.put(newUser.getID(), newUser);
                userNameMap.put(name, newUser);
                return newUser;
            }
        }
    }

    /**
     * Returns the User object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object. Unique
     * identifier for desired User object.
     * @return Desired User object or null.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User getUser(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_squaredesk_user", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (userUUIDMap.containsKey(userID)) {
                return userUUIDMap.get(userID);
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Deletes the User object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void deleteUser(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("delete_squaredesk_user", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                userUUIDMap.remove(userID);
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Returns name of User object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return User Name.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public String getUserName(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_squaredesk_user_name", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                return user.getName();
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the name for the User object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param newName
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws UserAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User updateUserName(String authToken, UUID userID, String newName)
            throws UserNotFoundException, UserAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_name", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (userNameMap.containsKey(newName)) {
                throw new UserAlreadyExistsException();
            } else {
                User user = userUUIDMap.get(userID);
                if (user != null) {
                    user.setName(newName);
                    return user;
                } else {
                    throw new UserNotFoundException();
                }
            }
        }
    }

    /**
     * Returns list of ContactInfo for User object with specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return User ContactInfo list.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public List<ContactInfo> getUserContactInfo(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_squaredesk_user_contact_info", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                return user.getContactInfo();
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Adds contact information for the User object with the specified ID, using
     * a ContactInfo object.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param contactInfo ContactInfo object with new contact information.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User addUserContactInfo(String authToken, UUID userID,
            ContactInfo contactInfo) throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_squaredesk_user_contact_info", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.addContactInfo(contactInfo);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Adds contact information for the User object with the specified ID, using
     * a specified contact method and contact information.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param contactMethod New contact method.
     * @param contactInformation Contact information associated with contact
     * method.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User addUserContactInfo(String authToken, UUID userID,
            String contactMethod, String contactInformation) throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_squaredesk_user_contact_info", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.addContactInfo(contactMethod, contactInformation);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Adds contact information for the User object with the specified ID, using
     * contact information only.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param contactInformation New contact information.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User addUserContactInfo(String authToken, UUID userID,
            String contactInformation) throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_squaredesk_user_contact_info", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.addContactInfo(contactInformation);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the contact information associated with the specified contact
     * method, for the User object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param contactMethod Contact method whose information is to be updated.
     * @param contactInformation New contact information.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User updateUserContactInfo(String authToken, UUID userID,
            String contactMethod, String contactInformation) throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_contact_info", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.updateContactInfo(contactMethod, contactInformation);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Removes the specified contact method from the User object with the
     * specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param contactMethod Contact method to be removed.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeUserContactInfo(String authToken, UUID userID,
            String contactMethod) throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_squaredesk_user_contact_info", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.removeContactInfo(contactMethod);
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Returns Image object associated with User with specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return User's Image object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Image getUserPicture(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_squaredesk_user_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                return user.getPicture();
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the Image object for the User object with the specified ID, by
     * passing in an Image object.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param picture Updated Image object.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User updateUserPicture(String authToken, UUID userID,
            Image picture) throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.setPicture(picture);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the Image object for the User object with the specified ID, by
     * passing in specific Image object property values.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param name Updated picture name.
     * @param description Updated picture description.
     * @param uri Updated picture URI.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     * @throws URISyntaxException
     */
    public User updateUserPicture(String authToken, UUID userID,
            String name, String description, URI uri)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException, URISyntaxException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                Image newPicture = new Image(name, description, uri);
                user.setPicture(newPicture);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Returns PayPal account associated with User with specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return User's PayPal account.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public String getUserAccount(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_squaredesk_user_account", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                return user.getAccount();
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the account for the User object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param account
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User updateUserAccount(String authToken, UUID userID,
            String account) throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_account", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.setAccount(account);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the sex of the given User object to male.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User updateUserGenderToMale(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_gender_to_male", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.setGenderToMale();
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the sex of the given User object to female.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User updateUserGenderToFemale(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_gender_to_female", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.setGenderToFemale();
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the Provider object associated with the given User.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param newProvider New Provider object for User.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User updateUserProvider(String authToken, UUID userID, Provider newProvider)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_provider", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.setProvider(newProvider);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Clears the Provider object associated with the given User.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User clearUserProvider(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_squaredesk_user_provider", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.clearProvider();
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Updates the Renter object associated with the given User.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @param newRenter New Renter object for User.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User updateUserRenter(String authToken, UUID userID, Renter newRenter)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_squaredesk_user_renter", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.setRenter(newRenter);
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Clears the Renter object associated with the given User.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return Updated User object.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public User clearUserRenter(String authToken, UUID userID)
            throws UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_squaredesk_user_renter", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user != null) {
                user.clearRenter();
                return user;
            } else {
                throw new UserNotFoundException();
            }
        }
    }

    /**
     * Returns true if contact info for specified User meets specifications,
     * false otherwise.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return True if User contact info passes validation, false otherwise.
     * @throws UserNotFoundException
     * @throws UserContactInfoNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validUserContactInfo(String authToken, UUID userID)
            throws UserNotFoundException, UserContactInfoNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException {

        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_squaredesk_user_contact_info", authToken)) {
            throw new UnauthorizedAccessException();
        } else {

            /*
             * Make sure at least one means exists for contacting User.
             */
            User user = userUUIDMap.get(userID);
            if (user == null) {
                throw new UserNotFoundException();
            } else {
                if (user.getContactInfoCount() < 1) {
                    throw new UserContactInfoNotFoundException();
                }
            }
            return true;
        }
    }

    /**
     * Returns true if account information for specified User meets
     * specifications, false otherwise.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return True if User account information passes validation, false
     * otherwise.
     * @throws UserNotFoundException
     * @throws UserAccountNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validUserAccount(String authToken, UUID userID)
            throws UserNotFoundException, UserAccountNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_squaredesk_user_account", authToken)) {
            throw new UnauthorizedAccessException();
        } else {

            /*
             * Make sure account information exists for User.
             */
            User user = userUUIDMap.get(userID);
            if (user == null) {
                throw new UserNotFoundException();
            } else {
                if (user.getAccount().isEmpty()) {
                    throw new UserAccountNotFoundException();
                }
            }
            return true;
        }
    }

    /**
     * Returns true if all specified User properties meets specifications, false
     * otherwise.
     *
     * @param authToken Client authentication token.
     * @param userID Unique identifier for desired User object.
     * @return True if User properties pass validation, false otherwise.
     * @throws UserNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validUser(String authToken, UUID userID) throws
            UserNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_squaredesk_user", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            User user = userUUIDMap.get(userID);
            if (user == null) {
                throw new UserNotFoundException();
            } else {
                try {
                    validUserContactInfo(authToken, userID);
                    validUserAccount(authToken, userID);
                    return true;
                } catch (UserContactInfoNotFoundException ucinfe) {
                    System.out.println("Specified User requires at least one method of contact.");
                    return false;
                } catch (UserAccountNotFoundException uanfe) {
                    System.out.println("Specified User account cannot be blank.");
                    return false;
                }
            }
        }
    }

    private boolean validAccessToken(String accessToken) {
        if ((accessToken != null) && (accessToken.length() > 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method registers the following with the AuthService: - The User API
     * service (represented by this class) - Restricted User API service methods
     * This method also creates a User role with permission to the above
     * restricted methods, as well as a user who has that role. Please note that
     * a SquareDesk user object is related to, but different from, a general
     * AuthService user object.
     *
     * @throws InvalidAccessTokenException
     * @throws UnauthorizedAccessException
     * @throws AuthServiceException
     * @throws InvalidUserIDException
     * @throws InvalidPasswordException
     */
    private void registerWithAuthService() {

        try {
            String accessToken = authService.login("super_admin", "p4ssw0rd");

            Service userService = authService.createService(accessToken, "squaredesk_user_service", "SquareDesk User Service", "SquareDesk user service");
            Role squareDeskUserRole = authService.createRole(accessToken, "squaredesk_user", "SquareDesk User", "SquareDesk user");

            Permission permission = authService.createPermission(accessToken, "create_squaredesk_user", "Create SquareDesk User", "Create SquareDesk user");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "get_squaredesk_user", "Get SquareDesk User", "Get SquareDesk user");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "delete_squaredesk_user", "Delete SquareDesk User", "Delete SquareDesk user");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "get_squaredesk_user_name", "Get SquareDesk User Name", "Get SquareDesk user name");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_squaredesk_user_name", "Update SquareDesk User Name", "Update SquareDesk user name");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "get_squaredesk_user_contact_info", "Get SquareDesk User Contact Info", "Get SquareDesk user contact info");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "add_squaredesk_user_contact_info", "Add SquareDesk User Contact Info", "Add SquareDesk user contact info");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_squaredesk_user_contact_info", "Update SquareDesk User Contact Info", "Update SquareDesk user contact info");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "remove_squaredesk_user_contact_info", "Remove SquareDesk User Contact Info", "Remove SquareDesk user contact info");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "get_squaredesk_user_picture", "Get SquareDesk User Picture", "Get SquareDesk user picture");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_squaredesk_user_picture", "Update SquareDesk User Picture", "Update SquareDesk user picture");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "get_squaredesk_user_account", "Get SquareDesk User Account", "Get SquareDesk user account");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_squaredesk_user_account", "Update SquareDesk User Account", "Update SquareDesk user account");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());

            permission = authService.createPermission(accessToken, "update_squaredesk_user_gender_to_male", "Update SquareDesk User Gender To Male", "Update SquareDesk user gender to male");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_squaredesk_user_gender_to_female", "Update SquareDesk User Gender To Female", "Update SquareDesk user gender to female");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());

            permission = authService.createPermission(accessToken, "update_squaredesk_user_provider", "Update SquareDesk User Provider", "Update SquareDesk user provider");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_squaredesk_user_provider", "Clear SquareDesk User Provider", "Clear SquareDesk user provider");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_squaredesk_user_renter", "Update SquareDesk User Renter", "Update SquareDesk user renter");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_squaredesk_user_renter", "Clear SquareDesk User Renter", "Clear SquareDesk user renter");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());

            permission = authService.createPermission(accessToken, "valid_squaredesk_user_account", "Valid SquareDesk User Account", "Valid SquareDesk user account");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "valid_squaredesk_user_contact_info", "Valid SquareDesk User Contact Info", "Valid SquareDesk user contact info");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "valid_squaredesk_user", "Valid SquareDesk User", "Valid SquareDesk user");
            authService.addServicePermission(accessToken, userService.getID(), permission.getID());
            authService.addRolePermission(accessToken, squareDeskUserRole.getID(), permission.getID());

            cscie97.asn4.squaredesk.authentication.User squareDeskUser = authService.createUser(accessToken, "SquareDesk User", "squaredesk_user");
            authService.addUserCredential(accessToken, "squaredesk_user", "squaredesk_user", "p4ssw0rd");
            authService.addUserRole(accessToken, squareDeskUser.getUserID(), squareDeskUserRole.getID());
            
            authService.logout(accessToken);
            
        } catch (InvalidAccessTokenException iate) {
            System.out.println("UserAPI.registerWithAuthService: InvalidAccessTokenException occurred.");
        } catch (UnauthorizedAccessException uae) {
            System.out.println("UserAPI.registerWithAuthService: UnauthorizedAccessException occurred.");
        } catch (InvalidUserIDException iue) {
            System.out.println("UserAPI.registerWithAuthService: InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("UserAPI.registerWithAuthService: InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("UserAPI.registerWithAuthService: AuthServiceException occurred.");
        }
    }
}