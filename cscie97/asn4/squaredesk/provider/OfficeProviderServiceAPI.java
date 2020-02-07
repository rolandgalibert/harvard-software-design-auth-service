/*
 * OfficeProviderServiceAPI
 * 
 * Version 1.0
 * 
 * Oct 5, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #2.
 *
 */
package cscie97.asn4.squaredesk.provider;

import cscie97.asn4.squaredesk.authentication.*;
import cscie97.asn4.squaredesk.renter.SearchEngine;
import java.net.URI;
import java.util.*;

/**
 * This singleton class maintains a map of Provider objects and a map of
 * OfficeSpace objects and makes available CRUD methods for processing those
 * objects. These CRUD methods are derived correspondingly from either the
 * ProviderAPI or OfficeSpaceAPI interfaces.
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
public class OfficeProviderServiceAPI implements ProviderAPI, OfficeSpaceAPI {

    /**
     * Map of active Provider objects (Provider UUID to Provider object).
     */
    private HashMap<UUID, Provider> providerUUIDMap = new HashMap<UUID, Provider>();
    /**
     * Set of active OfficeSpace objects.
     */
    private HashMap<UUID, OfficeSpace> officeSpaceMap = new HashMap<UUID, OfficeSpace>();
    /**
     * Map from Provider objects to assigned OfficeSpace objects.
     */
    private HashMap<UUID, List<UUID>> providerOfficeSpaceMap = new HashMap<UUID, List<UUID>>();
    /**
     * Map from OfficeSpace objects to assigned Provider.
     */
    private HashMap<UUID, UUID> officeSpaceProviderMap = new HashMap<UUID, UUID>();
    /**
     * Authentication service object
     */
    private static AuthServiceImpl authService;
    /**
     * Locale object for Location country code validation
     */
    private List<String> ISOCountries = Arrays.asList(Locale.getISOCountries());
    private SearchEngine searchEngine = SearchEngine.getInstance();

    /**
     * Private hidden singleton constructor.
     */
    private OfficeProviderServiceAPI() {
        authService = AuthServiceImpl.getInstance();
        registerWithAuthService();
    }

    /**
     * OfficeProviderServiceAPIHolder is loaded on the first execution of
     * OfficeProviderServiceAPI.getInstance() or the first access to
     * OfficeProviderServiceAPIHolder.OFFICE_PROVIDER_SERVICE_API_INSTANCE, not
     * before.
     */
    private static class OfficeProviderServiceAPIHolder {

        private static final OfficeProviderServiceAPI OFFICE_PROVIDER_SERVICE_API_INSTANCE = new OfficeProviderServiceAPI();
    }

    /**
     * Returns singleton object instance.
     *
     * @return Singleton OfficeProviderServiceAPI object.
     */
    public static OfficeProviderServiceAPI getInstance() {
        return OfficeProviderServiceAPIHolder.OFFICE_PROVIDER_SERVICE_API_INSTANCE;
    }

    /**
     * Returns list of active Provider objects in API.
     *
     * @param authToken Client authentication token.
     * @return ArrayList of active Provider objects in API.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public ArrayList<Provider> getProviderList(String authToken) throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_provider_list", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            return new ArrayList<Provider>(providerUUIDMap.values());
        }
    }

    /**
     * Returns list of active OfficeSpace objects in API.
     *
     * @param authToken Client authentication token.
     * @return ArrayList of active OfficeSpace objects in API.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public ArrayList<OfficeSpace> getOfficeSpaceList(String authToken) throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_list", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            return new ArrayList<OfficeSpace>(officeSpaceMap.values());
        }
    }

    /**
     * Returns a list of the OfficeSpace IDs in the map of currently active
     * OfficeSpace objects.
     *
     * @param authToken Client authentication token.
     * @return List of IDs of currently active OfficeSpace objects.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public ArrayList<UUID> getOfficeSpaceUUIDList(String authToken) throws InvalidAccessTokenException, UnauthorizedAccessException {

        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_UUID_list", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            ArrayList<UUID> officeSpaceUUIDList = new ArrayList<UUID>();
            Object[] officeSpaceKeyArray = officeSpaceMap.keySet().toArray();
            for (int i = 0; i < officeSpaceKeyArray.length; i++) {
                officeSpaceUUIDList.add((UUID) officeSpaceKeyArray[i]);
            }
            return officeSpaceUUIDList;
        }
    }

    /**
     * Assigns specified OfficeSpace object to specified Provider.
     *
     * @param authToken Client authentication token.
     * @param provider Provider object to be associated with OfficeSpace object.
     * @param officeSpace OfficeSpace object for assignment.
     * @throws ProviderNotFoundException
     * @throws OfficeSpaceNotFoundException
     * @throws OfficeSpaceAlreadyAssignedException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void assignOfficeSpace(String authToken, Provider provider,
            OfficeSpace officeSpace) throws ProviderNotFoundException, OfficeSpaceNotFoundException,
            OfficeSpaceAlreadyAssignedException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("assign_office_space", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if ((provider == null) || (provider.getID() == null) || (!providerUUIDMap.containsKey(provider.getID()))) {
                throw new ProviderNotFoundException();
            }
            if ((officeSpace == null) || (officeSpace.getID() == null) || (!officeSpaceMap.containsKey(officeSpace.getID()))) {
                throw new OfficeSpaceNotFoundException();
            }

            /*
             * Update officeSpaceProviderMap and providerOfficeSpaceMap
             */
            if (officeSpaceProviderMap.containsKey(officeSpace.getID())) {
                throw new OfficeSpaceAlreadyAssignedException();
            } else {
                officeSpaceProviderMap.put(officeSpace.getID(), provider.getID());
            }
            if (!providerOfficeSpaceMap.containsKey(provider.getID())) {
                providerOfficeSpaceMap.put(provider.getID(), new ArrayList<UUID>());
            }
            if (providerOfficeSpaceMap.get(provider.getID()).contains(officeSpace.getID())) {
                throw new OfficeSpaceAlreadyAssignedException();
            } else {
                providerOfficeSpaceMap.get(provider.getID()).add(officeSpace.getID());
            }
        }
    }

    /**
     * Removes specified OfficeSpace object from specified Provider.
     *
     * @param authToken Client authentication token.
     * @param provider Provider object associated with OfficeSpace object.
     * @param officeSpace OfficeSpace object for removal.
     * @throws ProviderNotFoundException
     * @throws OfficeSpaceNotFoundException
     * @throws OfficeSpaceNotAssignedToProviderException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void unAssignOfficeSpace(String authToken, Provider provider,
            OfficeSpace officeSpace) throws ProviderNotFoundException, OfficeSpaceNotFoundException,
            OfficeSpaceNotAssignedToProviderException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("unassign_office_space", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if ((provider == null) || (provider.getID() == null) || (!providerUUIDMap.containsKey(provider.getID()))) {
                throw new ProviderNotFoundException();
            }
            if ((officeSpace == null) || (officeSpace.getID() == null) || (!officeSpaceMap.containsKey(officeSpace.getID()))) {
                throw new OfficeSpaceNotFoundException();
            }

            /*
             * Update officeSpaceProviderMap and providerOfficeSpaceMap
             */
            if (!officeSpaceProviderMap.containsKey(officeSpace.getID())) {
                throw new OfficeSpaceNotAssignedToProviderException();
            } else {
                officeSpaceProviderMap.remove(officeSpace.getID());
            }
            if (!providerOfficeSpaceMap.containsKey(provider.getID())) {
                throw new ProviderNotFoundException();
            }
            if (!providerOfficeSpaceMap.get(provider.getID()).contains(officeSpace.getID())) {
                throw new OfficeSpaceNotAssignedToProviderException();
            } else {
                providerOfficeSpaceMap.get(provider.getID()).add(officeSpace.getID());
            }
        }
    }

    /**
     * Provider object constructor with no input parameters.
     *
     * @param authToken Client authentication token.
     * @return Newly created Provider object.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public Provider createProvider(String authToken) throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("create_provider", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            Provider newProvider = new Provider();
            providerUUIDMap.put(newProvider.getID(), newProvider);
            return newProvider;
        }
    }

    /**
     * Returns the Provider object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object. Unique
     * identifier for desired Provider object.
     * @return Desired Provider object or null.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public Provider getProvider(String authToken, UUID providerID)
            throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_provider", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (providerUUIDMap.containsKey(providerID)) {
                return providerUUIDMap.get(providerID);
            } else {
                throw new ProviderNotFoundException();
            }
        }
    }

    /**
     * Returns the Rating list for the Provider with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @return List of Rating objects for specified Provider.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public List<Rating> getProviderRatings(String authToken, UUID providerID)
            throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_provider_ratings", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            Provider provider = providerUUIDMap.get(providerID);
            if (provider != null) {
                return provider.getRatings();
            } else {
                throw new ProviderNotFoundException();
            }
        }
    }

    /**
     * Adds a Rating to the rating list of the Provider with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @param rating Provider rating (Rating object).
     * @return Updated Provider object.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public Provider addProviderRating(String authToken, UUID providerID,
            Rating rating) throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_provider_rating", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            Provider provider = providerUUIDMap.get(providerID);
            if (provider != null) {
                provider.addRatingToList(rating);
                return provider;
            } else {
                throw new ProviderNotFoundException();
            }
        }
    }

    /**
     * Removes the specified Rating from the rating list of the Provider with
     * the specified ID.
     *
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @param rating Provider rating (Rating object).
     * @throws ProviderNotFoundException
     * @throws RatingNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public void removeProviderRating(String authToken, UUID providerID,
            Rating rating) throws ProviderNotFoundException, RatingNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_provider_rating", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            Provider provider = providerUUIDMap.get(providerID);
            if (provider != null) {
                provider.removeRatingFromList(rating);
            } else {
                throw new ProviderNotFoundException();
            }
        }
    }

    /**
     * Deletes the Provider object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public void deleteProvider(String authToken, UUID providerID)
            throws ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("delete_provider", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            Provider provider = providerUUIDMap.get(providerID);
            if (provider != null) {
                providerUUIDMap.remove(providerID);
            } else {
                throw new ProviderNotFoundException();
            }
        }
    }

    /**
     * Returns true if ratings for specified Provider meet specifications, false
     * otherwise.
     *
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @return True if Provider ratings pass validation, false otherwise.
     * @throws ProviderNotFoundException
     * @throws BadRatingException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public boolean validProviderRatings(String authToken, UUID providerID)
            throws ProviderNotFoundException, BadRatingException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_provider_ratings", authToken)) {
            throw new UnauthorizedAccessException();
        } else {

            /*
             * Make sure account information exists for Provider.
             */
            Provider provider = providerUUIDMap.get(providerID);
            if (provider == null) {
                throw new ProviderNotFoundException();
            } else {
                List<Rating> providerRatings = provider.getRatings();
                for (int i = 0; i < providerRatings.size(); i++) {
                    if (!((providerRatings.get(i).getRating() >= 0)
                            && (providerRatings.get(i).getRating() <= 5))) {
                        throw new BadRatingException();
                    }
                }
            }
            return true;
        }
    }

    /**
     * Returns true if all specified Provider properties meets specifications,
     * false otherwise.
     *
     * @param authToken Client authentication token.
     * @param providerID Unique identifier for desired Provider object.
     * @return True if Provider properties pass validation, false otherwise.
     * @throws ProviderNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public boolean validProvider(String authToken, UUID providerID) throws
            ProviderNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_provider", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            Provider provider = providerUUIDMap.get(providerID);
            if (provider == null) {
                throw new ProviderNotFoundException();
            } else {
                try {
                    validProviderRatings(authToken, providerID);
                    return true;
                } catch (BadRatingException bre) {
                    System.out.println("A bad Provider rating has been entered (rating must be between 0 to 5).");
                    return false;
                }
            }
        }
    }

    /**
     * OfficeSpace object constructor with no OfficeSpace input parameters.
     *
     * @param authToken Client authentication token.
     * @return OfficeSpace object that was created.
     * @throws OfficeSpaceAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace createOfficeSpace(String authToken) throws OfficeSpaceAlreadyExistsException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("create_office_space", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace newOfficeSpace = new OfficeSpace();
            officeSpaceMap.put(newOfficeSpace.getID(), newOfficeSpace);
            return newOfficeSpace;
        }
    }

    /**
     * OfficeSpace object constructor that takes all mandatory OfficeSpace
     * parameters.
     *
     * @param authToken Client authentication token.
     * @param name Name of OfficeSpace object.
     * @param location Location (address, etc.) of OfficeSpace.
     * @param capacity Capacity (max. occupants, etc.) of OfficeSpace.
     * @param rate Rates for this OfficeSpace.
     * @param facilityType OfficeSpace facility type.
     * @param facilitySubtype OfficeSpace facility subtype (where applicable).
     * @return OfficeSpace object that was created.
     * @throws OfficeSpaceAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace createOfficeSpace(String authToken, String name,
            Location location, Capacity capacity, Rate rate, String facilityType,
            String facilitySubtype) throws OfficeSpaceAlreadyExistsException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("create_office_space", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace newOfficeSpace = new OfficeSpace();
            officeSpaceMap.put(newOfficeSpace.getID(), newOfficeSpace);
            newOfficeSpace.setName(name);
            newOfficeSpace.setLocation(location);
            newOfficeSpace.setCapacity(capacity);
            newOfficeSpace.addRateToList(rate);
            newOfficeSpace.setFacilityType(facilityType);
            newOfficeSpace.setFacilitySubtype(facilitySubtype);
            // searchEngine.addTriples(authToken, newOfficeSpace.getID());
            searchEngine.addTriples(authToken, newOfficeSpace);
            return newOfficeSpace;
        }
    }

    /**
     * Returns OfficeSpace object with the specified ID, or null if no such
     * object was found.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace object with specified ID, or null if none was found.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace getOfficeSpace(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (officeSpaceMap.containsKey(officeSpaceID)) {
                return officeSpaceMap.get(officeSpaceID);
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Deletes OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public void deleteOfficeSpace(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("delete_office_space", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (officeSpaceMap.containsKey(officeSpaceID)) {
                officeSpaceMap.remove(officeSpaceID);
                searchEngine.clearTriples(authToken, officeSpaceID);
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Returns name of OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace name.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public String getOfficeSpaceName(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_name", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getName();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates the name of the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param newName
     * @return Updated OfficeSpace.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceName(String authToken, UUID officeSpaceID,
            String newName) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_name", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.setName(newName);
                searchEngine.clearTriples(authToken, officeSpaceID);
                searchEngine.addTriples(authToken, officeSpace);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Returns list of Features associated with OfficeSpace object with the
     * specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace Feature list.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public List<Feature> getOfficeSpaceFeatures(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_features", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getFeatures();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds a Feature to the Feature list of the OfficeSpace object with the
     * specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param feature Feature to be added to specified OfficeSpace object.
     * @return Updated OfficeSpace.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace addOfficeSpaceFeature(String authToken, UUID officeSpaceID,
            String feature) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_feature", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                try {
                    if (officeSpace.featureAlreadyExistsInList(feature)) {
                        throw new FeatureAlreadyExistsException();
                    } else {
                        officeSpace.addFeature(feature);
                        searchEngine.clearTriples(authToken, officeSpaceID);
                        searchEngine.addTriples(authToken, officeSpace);
                        return officeSpace;
                    }
                } catch (FeatureAlreadyExistsException faee) {
                    System.out.println("Specified feature already exists for this office space.");
                    return officeSpace;
                }
            }
        }
    }

    /**
     * Removes the specified Feature from the Feature list of the OfficeSpace
     * object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param feature Feature to be removed from specified OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public void removeOfficeSpaceFeature(String authToken, UUID officeSpaceID,
            String feature) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_office_space_feature", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                try {
                    if (!officeSpace.featureAlreadyExistsInList(feature)) {
                        throw new FeatureNotFoundException();
                    } else {
                        officeSpace.removeFeature(feature);
                        searchEngine.clearTriples(authToken, officeSpaceID);
                        searchEngine.addTriples(authToken, officeSpace);
                    }
                } catch (FeatureNotFoundException fnfe) {
                    System.out.println("Specified feature does not exist for this office space.");
                }
            }
        }
    }

    /**
     * Returns list of CommonAccess areas associated with OfficeSpace object
     * with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace CommonAccess list.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public List<CommonAccess> getOfficeSpaceCommonAccesses(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_common_accesses", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getCommonAccessAreas();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds a CommonAccess area to the CommonAccess list of the OfficeSpace
     * object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param commonAccess CommonAccess area to be added to specified
     * OfficeSpace object.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace addOfficeSpaceCommonAccess(String authToken, UUID officeSpaceID,
            String commonAccess) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_common_access", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                try {
                    if (officeSpace.commonAccessAlreadyExistsInList(commonAccess)) {
                        throw new CommonAccessAlreadyExistsException();
                    } else {
                        officeSpace.addCommonAccess(commonAccess);
                        searchEngine.clearTriples(authToken, officeSpaceID);
                        searchEngine.addTriples(authToken, officeSpace);
                        return officeSpace;
                    }
                } catch (CommonAccessAlreadyExistsException caae) {
                    System.out.println("Specified common access area already exists for this office space.");
                    return officeSpace;
                }
            }
        }
    }

    /**
     * Removes the specified CommonAccess area from the CommonAccess list of the
     * OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param commonAccess CommonAccess area to be removed from specified
     * OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public void removeOfficeSpaceCommonAccess(String authToken, UUID officeSpaceID,
            String commonAccess) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_office_space_common_access", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                try {
                    if (!officeSpace.commonAccessAlreadyExistsInList(commonAccess)) {
                        throw new CommonAccessNotFoundException();
                    } else {
                        officeSpace.removeCommonAccess(commonAccess);
                        searchEngine.clearTriples(authToken, officeSpaceID);
                        // searchEngine.addTriples(authToken, officeSpaceID);
                        searchEngine.addTriples(authToken, officeSpace);
                    }
                } catch (CommonAccessNotFoundException canfe) {
                    System.out.println("Specified common access area does not exist for this office space.");
                }
            }
        }
    }

    /**
     * Returns Location of OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace Location.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public Location getOfficeSpaceLocation(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_location", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getLocation();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates Location information of the OfficeSpace object with the specified
     * ID, by passing in a Location object
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param location Location object with new location information for
     * OfficeSpace.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceLocation(String authToken, UUID officeSpaceID,
            Location location) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_location", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.setLocation(location);
                searchEngine.clearTriples(authToken, officeSpaceID);
                // searchEngine.addTriples(authToken, officeSpaceID);
                searchEngine.addTriples(authToken, officeSpace);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates Location information of the OfficeSpace object with the specified
     * ID, by passing specific Location properties.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param street1 Updated Location street1.
     * @param street2 Updated Location street2.
     * @param city Updated Location city.
     * @param state Updated Location state.
     * @param country Updated Location country.
     * @param zip Updated Location zip.
     * @param latitude Updated Location latitude.
     * @param longitude Updated Location longitude.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceLocation(String authToken, UUID officeSpaceID,
            String street1, String street2, String city, String state, String country,
            String zip, double latitude, double longitude)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_location", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.getLocation().setStreet1(street1);
                officeSpace.getLocation().setStreet2(street2);
                officeSpace.getLocation().setCity(city);
                officeSpace.getLocation().setState(state);
                officeSpace.getLocation().setCountry(country);
                officeSpace.getLocation().setZip(zip);
                officeSpace.getLocation().setLatitude(latitude);
                officeSpace.getLocation().setLongitude(longitude);
                searchEngine.clearTriples(authToken, officeSpaceID);
                // searchEngine.addTriples(authToken, officeSpaceID);
                searchEngine.addTriples(authToken, officeSpace);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Returns Capacity of OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace Capacity.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public Capacity getOfficeSpaceCapacity(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_capacity", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getCapacity();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates Capacity information of the OfficeSpace object with the specified
     * ID, by passing in a Capacity object with updated values.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param capacity Capacity object with new capacity information for
     * OfficeSpace.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceCapacity(String authToken, UUID officeSpaceID,
            Capacity capacity) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_capacity", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.setCapacity(capacity);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates Capacity information of the OfficeSpace object with the specified
     * ID, by passing in specific Capacity properties.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param maxOccupants Maximum occupants OfficeSpace can hold.
     * @param workspaces Maximum number of workspaces OfficeSpace has available.
     * @param squareFootage Size of OfficeSpace.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceCapacity(String authToken, UUID officeSpaceID,
            short maxOccupants, short workspaces, float squareFootage)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_capacity", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.getCapacity().setMaxOccupants(maxOccupants);
                officeSpace.getCapacity().setWorkspaces(workspaces);
                officeSpace.getCapacity().setSquareFootage(squareFootage);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Returns FacilityType of OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace FacilityType.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public String getOfficeSpaceFacilityType(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_facility_type", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getFacilityType();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates the FacilityType of the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param facilityType New OfficeSpace facility type.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceFacilityType(String authToken, UUID officeSpaceID,
            String facilityType) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_facility_type", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.setFacilityType(facilityType);
                searchEngine.clearTriples(authToken, officeSpaceID);
                // searchEngine.addTriples(authToken, officeSpaceID);
                searchEngine.addTriples(authToken, officeSpace);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Returns FacilitySubtype of OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpaceFacilitySubtype.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public String getOfficeSpaceFacilitySubtype(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_facility_subtype", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getFacilitySubtype();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates the FacilitySubtype of the OfficeSpace object with the specified
     * ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param facilitySubtype New OfficeSpace facility subtype.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceFacilitySubtype(String authToken, UUID officeSpaceID,
            String facilitySubtype) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_facility_subtype", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.setFacilitySubtype(facilitySubtype);
                searchEngine.clearTriples(authToken, officeSpaceID);
                // searchEngine.addTriples(authToken, officeSpaceID);
                searchEngine.addTriples(authToken, officeSpace);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Returns list of Rates associated with OfficeSpace object with the
     * specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace list of Rates.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public List<Rate> getOfficeSpaceRates(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_rates", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getRates();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds a rental Rate object to the Rate list of the OfficeSpace object with
     * the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rate New rate (rental period + actual rate) to be added to
     * OfficeSpace object.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws RateAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace addOfficeSpaceRate(String authToken, UUID officeSpaceID,
            Rate rate) throws OfficeSpaceNotFoundException, RateAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_rate", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                if (officeSpace.ratePeriodAlreadyExistsInList(rate.getRentalPeriod())) {
                    throw new RateAlreadyExistsException();
                } else {
                    officeSpace.addRateToList(rate);
                    return officeSpace;
                }
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds a rental rate to the Rate list of the OfficeSpace object with the
     * specified ID by processing specified Rate properties.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rentalPeriod Rental period for which the new rate will apply.
     * @param rate Actual amount charged over the rental period.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace addOfficeSpaceRate(String authToken, UUID officeSpaceID,
            String rentalPeriod, float rate) throws OfficeSpaceNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_rate", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                try {
                    if (officeSpace.ratePeriodAlreadyExistsInList(rentalPeriod)) {
                        throw new RateAlreadyExistsException();
                    } else {
                        officeSpace.addRateToList(rentalPeriod, rate);
                        return officeSpace;
                    }
                } catch (RateAlreadyExistsException raee) {
                    System.out.println("Rate with specified rental period already exists for office space.");
                    return officeSpace;
                }
            }
        }
    }

    /**
     * Updates the rental Rate with the given RentalPeriod in the OfficeSpace
     * object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rentalPeriod
     * @param newRate Updated rate amount for this rental period.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceRate(String authToken, UUID officeSpaceID,
            String rentalPeriod, float newRate)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_rate", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                try {
                    if (!officeSpace.ratePeriodAlreadyExistsInList(rentalPeriod)) {
                        throw new RateNotFoundException();
                    } else {
                        officeSpace.updateRate(rentalPeriod, newRate);
                        return officeSpace;
                    }
                } catch (RateNotFoundException rnfe) {
                    System.out.println("Rate with specified rental period does not exist for office space.");
                    return officeSpace;
                }
            }
        }
    }

    /**
     * Deletes the rate with the specified RentalPeriod from the Rate list of
     * the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rentalPeriod OfficeSpace rental period whose corresponding Rate we
     * will delete.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public void removeOfficeSpaceRate(String authToken, UUID officeSpaceID,
            String rentalPeriod) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_office_space_rate", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                try {
                    if (!officeSpace.ratePeriodAlreadyExistsInList(rentalPeriod)) {
                        throw new RateNotFoundException();
                    } else {
                        officeSpace.removeRateFromList(rentalPeriod);
                    }
                } catch (RateNotFoundException rnfe) {
                    System.out.println("Rate with specified rental period does not exist for office space.");
                }
            }
        }
    }

    /**
     * Returns list of general office space pictures associated with OfficeSpace
     * object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return OfficeSpace pictures.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public List<Image> getOfficeSpacePictures(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_pictures", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getOfficeSpacePictures();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds an office space Image to the office space Image list of the
     * OfficeSpace object with the specified ID, using an Image object.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param picture New picture of overall OfficeSpace area.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace addOfficeSpacePicture(String authToken, UUID officeSpaceID,
            Image picture) throws OfficeSpaceNotFoundException, ImageAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.addOfficeSpacePicture(picture);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds an office space Image to the office space Image list of the
     * OfficeSpace object with the specified ID, using specified Image object
     * property parameters.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureName Name of new picture of overall OfficeSpace area.
     * @param pictureDescription Description of new picture of overall
     * OfficeSpace area.
     * @param pictureURI URI of new picture of overall OfficeSpace area.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace addOfficeSpacePicture(String authToken, UUID officeSpaceID,
            String pictureName, String pictureDescription, URI pictureURI)
            throws OfficeSpaceNotFoundException, ImageAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                Image newPicture = new Image(pictureName, pictureDescription, pictureURI);
                officeSpace.addOfficeSpacePicture(newPicture);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates the properties of the office space Image, specified with
     * pictureID, in the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureID UUID of picture of overall OfficeSpace area.
     * @param updatedName Updated name for picture.
     * @param updatedDescription Updated description for picture.
     * @param updatedURI Updated URI for picture.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpacePicture(String authToken, UUID officeSpaceID,
            UUID pictureID, String updatedName, String updatedDescription, URI updatedURI)
            throws OfficeSpaceNotFoundException, ImageNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                if (!officeSpace.getOfficeSpacePictureMap().containsKey(pictureID)) {
                    throw new ImageNotFoundException();
                } else {
                    officeSpace.getOfficeSpacePictureMap().get(pictureID).setName(updatedName);
                    officeSpace.getOfficeSpacePictureMap().get(pictureID).setDescription(updatedDescription);
                    officeSpace.getOfficeSpacePictureMap().get(pictureID).setURI(updatedURI);
                    return officeSpace;
                }
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Deletes the specified office space Image from the office space image list
     * of the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureID Unique identifier of picture to be removed, of overall
     * OfficeSpace area.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public void removeOfficeSpacePicture(String authToken, UUID officeSpaceID,
            UUID pictureID) throws OfficeSpaceNotFoundException, ImageNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_office_space_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                if (!officeSpace.getOfficeSpacePictureMap().containsKey(pictureID)) {
                    throw new ImageNotFoundException();
                } else {
                    officeSpace.getOfficeSpacePictureMap().remove(pictureID);
                }
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Returns list of pictures of specific facility for OfficeSpace object with
     * the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return List of OfficeSpace facility pictures.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public List<Image> getOfficeSpaceFacilityPictures(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_facility_pictures", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getFacilityPictures();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds an office space facility Image to the office space facility Image
     * list of the OfficeSpace object with the specified ID, using an Image
     * object.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param picture New picture of OfficeSpace facility.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace addOfficeSpaceFacilityPicture(String authToken, UUID officeSpaceID,
            Image picture) throws OfficeSpaceNotFoundException, ImageAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_facility_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.addFacilityPicture(picture);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds an office space facility Image to the office space facility Image
     * list of the OfficeSpace object with the specified ID, using specified
     * Image object property parameters.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureName Name of new picture of OfficeSpace facility.
     * @param pictureDescription Description of new picture of OfficeSpace
     * facility.
     * @param pictureURI URI of new picture of OfficeSpace facility.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageAlreadyExistsException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace addOfficeSpaceFacilityPicture(String authToken, UUID officeSpaceID,
            String pictureName, String pictureDescription, URI pictureURI)
            throws OfficeSpaceNotFoundException, ImageAlreadyExistsException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_facility_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                Image newPicture = new Image(pictureName, pictureDescription, pictureURI);
                officeSpace.addFacilityPicture(newPicture);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Updates the properties of the office space facility Image, specified with
     * pictureID, in the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureID UUID of picture of OfficeSpace facility.
     * @param updatedName Updated name for picture.
     * @param updatedDescription Updated description for picture.
     * @param updatedURI Updated URI for picture.
     * @return Updated OfficeSpace object.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public OfficeSpace updateOfficeSpaceFacilityPicture(String authToken, UUID officeSpaceID,
            UUID pictureID, String updatedName, String updatedDescription, URI updatedURI)
            throws OfficeSpaceNotFoundException, ImageNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_office_space_facility_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                if (!officeSpace.getFacilityPictureMap().containsKey(pictureID)) {
                    throw new ImageNotFoundException();
                } else {
                    officeSpace.getFacilityPictureMap().get(pictureID).setName(updatedName);
                    officeSpace.getFacilityPictureMap().get(pictureID).setDescription(updatedDescription);
                    officeSpace.getFacilityPictureMap().get(pictureID).setURI(updatedURI);
                    return officeSpace;
                }
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Deletes the specified office space Image from the office space facility
     * image list of the OfficeSpace object with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param pictureID Unique identifier of picture to be removed, of
     * OfficeSpace facility.
     * @throws OfficeSpaceNotFoundException
     * @throws ImageNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public void removeOfficeSpaceFacilityPicture(String authToken, UUID officeSpaceID,
            UUID pictureID) throws OfficeSpaceNotFoundException, ImageNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_office_space_facility_picture", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                if (!officeSpace.getFacilityPictureMap().containsKey(pictureID)) {
                    throw new ImageNotFoundException();
                } else {
                    officeSpace.getFacilityPictureMap().remove(pictureID);
                }
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Returns true if Location information for specified OfficeSpace meets
     * specifications, false otherwise.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if OfficeSpace Location information passes validation, false
     * otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws BadOfficeSpaceLocationException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public boolean validOfficeSpaceLocation(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, BadOfficeSpaceLocationException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_office_space_location", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                if (officeSpace.getLocation().getStreet1().isEmpty()) {
                    System.out.println("Office space location error - Street1 cannot be blank.");
                    throw new BadOfficeSpaceLocationException("Office space location error - Street1 cannot be blank.");
                } else if (officeSpace.getLocation().getCity().isEmpty()) {
                    System.out.println("Office space location error - City cannot be blank.");
                    throw new BadOfficeSpaceLocationException("Office space location error - City cannot be blank.");
                } else if (officeSpace.getLocation().getCountry().isEmpty()) {
                    System.out.println("Office space location error - Country cannot be blank.");
                    throw new BadOfficeSpaceLocationException("Office space location error - Country cannot be blank.");
                } else if (!ISOCountries.contains(officeSpace.getLocation().getCountry())) {
                    System.out.println("Office space location error - Illegal country specified (country must be in ISO format).");
                    throw new BadOfficeSpaceLocationException("Office space location error - Illegal country specified (country must be in ISO format).");
                }
                return true;
            }
        }
    }

    /**
     * Returns true if Capacity information for specified OfficeSpace meets
     * specifications, false otherwise.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if OfficeSpace Capacity information passes validation, false
     * otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws BadOfficeSpaceCapacityException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public boolean validOfficeSpaceCapacity(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, BadOfficeSpaceCapacityException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_office_space_capacity", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                if (officeSpace.getCapacity().getMaxOccupants() < 1) {
                    System.out.println("Office space capacity error - maximum # of occupants must be greater than 0.");
                    throw new BadOfficeSpaceCapacityException("Office space capacity error - maximum # of occupants must be greater than 0.");
                } else if (officeSpace.getCapacity().getWorkspaces() < 1) {
                    System.out.println("Office space capacity error - # of available workspaces must be greater than 0.");
                    throw new BadOfficeSpaceCapacityException("Office space capacity error - # of available workspaces must be greater than 0.");
                }
                return true;
            }
        }
    }

    /**
     * Returns true if FacilityType/FacilitySubtype information for specified
     * OfficeSpace meets specifications, false otherwise.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if OfficeSpace FacilityType/FacilitySubtype information
     * passes validation, false otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws OfficeSpaceFacilityException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public boolean validOfficeSpaceFacility(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, OfficeSpaceFacilityException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_office_space_facility", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                if (officeSpace.getFacilityType().equalsIgnoreCase("Home")
                        && officeSpace.getFacilitySubtype().isEmpty()) {
                    System.out.println("Office space facility subtype cannot be blank if facility type = 'Home'.");
                    throw new OfficeSpaceFacilityException("Office space facility subtype cannot be blank if facility type = 'Home'.");
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * Returns true if Rate information for specified OfficeSpace meets
     * specifications, false otherwise.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if OfficeSpace Rate information passes validation, false
     * otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws OfficeSpaceRateException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public boolean validOfficeSpaceRates(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, OfficeSpaceRateException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_office_space_rates", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                if (officeSpace.getRateCount() < 1) {
                    System.out.println("At least one rate must be entered for an office space");
                    throw new OfficeSpaceRateException("At least one rate must be entered for an office space");
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * Returns true if overall properties for specified OfficeSpace meet
     * specifications, false otherwise.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return True if overall OfficeSpace passes validation, false otherwise.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    @Override
    public boolean validOfficeSpace(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_office_space", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace == null) {
                throw new OfficeSpaceNotFoundException();
            } else {
                try {
                    validOfficeSpaceLocation(authToken, officeSpaceID);
                    validOfficeSpaceCapacity(authToken, officeSpaceID);
                    validOfficeSpaceFacility(authToken, officeSpaceID);
                    validOfficeSpaceRates(authToken, officeSpaceID);
                    return true;
                } catch (BadOfficeSpaceLocationException bosle) {
                    return false;
                } catch (BadOfficeSpaceCapacityException bosce) {
                    return false;
                } catch (OfficeSpaceFacilityException osfe) {
                    return false;
                } catch (OfficeSpaceRateException osre) {
                    return false;
                }
            }
        }
    }

    /**
     * Returns the Rating list for the OfficeSpace with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @return List of Rating objects for specified OfficeSpace.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public List<Rating> getOfficeSpaceRatings(String authToken, UUID officeSpaceID)
            throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_office_space_ratings", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                return officeSpace.getRatings();
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Adds a Rating to the rating list of the OfficeSpace with the specified
     * ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rating OfficeSpace rating (Rating object).
     * @return Updated Provider object.
     * @throws OfficeSpaceNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public OfficeSpace addOfficeSpaceRating(String authToken, UUID officeSpaceID,
            Rating rating) throws OfficeSpaceNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_office_space_rating", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.addRatingToList(rating);
                searchEngine.clearTriples(authToken, officeSpaceID);
                searchEngine.addTriples(authToken, officeSpace);
                return officeSpace;
            } else {
                throw new OfficeSpaceNotFoundException();
            }
        }
    }

    /**
     * Removes the specified Rating from the rating list of the OfficeSpace with
     * the specified ID.
     *
     * @param authToken Client authentication token.
     * @param officeSpaceID Unique identifier for desired OfficeSpace object.
     * @param rating OfficeSpace rating (Rating object).
     * @throws OfficeSpaceNotFoundException
     * @throws RatingNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeOfficeSpaceRating(String authToken, UUID officeSpaceID,
            Rating rating) throws OfficeSpaceNotFoundException, RatingNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_office_space_rating", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            OfficeSpace officeSpace = officeSpaceMap.get(officeSpaceID);
            if (officeSpace != null) {
                officeSpace.removeRatingFromList(rating);
                searchEngine.clearTriples(authToken, officeSpaceID);
                searchEngine.addTriples(authToken, officeSpace);
            } else {
                throw new OfficeSpaceNotFoundException();
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
     * This method registers the following with the AuthService: - The Provider
     * service (represented by this class) - Restricted Provider service methods
     * This method also creates a Provider role with permission to the above
     * restricted methods, as well as a user who has that role.
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

            Service providerService = authService.createService(accessToken, "provider_service", "Office Space Provider Service", "Office space provider service");
            Role providerUserRole = authService.createRole(accessToken, "squaredesk_provider", "SquareDesk Provider", "SquareDesk provider");

            Permission permission = authService.createPermission(accessToken, "get_provider_list", "Get Provider List", "Get provider list");
            authService.addServicePermission(accessToken, providerService.getID(), "get_provider_list");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_provider_list");
            permission = authService.createPermission(accessToken, "get_office_space_list", "Get Office Space List", "Get office space list");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_list");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_list");
            permission = authService.createPermission(accessToken, "get_office_space_UUID_list", "Get Office Space UUID List", "Get office space UUID list");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_UUID_list");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_UUID_list");
            permission = authService.createPermission(accessToken, "assign_office_space", "Assign Office Space", "Assign office space");
            authService.addServicePermission(accessToken, providerService.getID(), "assign_office_space");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "assign_office_space");
            permission = authService.createPermission(accessToken, "unassign_office_space", "Unassign Office Space", "Unassign office space");
            authService.addServicePermission(accessToken, providerService.getID(), "unassign_office_space");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "unassign_office_space");
            permission = authService.createPermission(accessToken, "create_provider", "Create Provider", "Create provider");
            authService.addServicePermission(accessToken, providerService.getID(), "create_provider");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "create_provider");
            permission = authService.createPermission(accessToken, "get_provider", "Get Provider", "Get provider");
            authService.addServicePermission(accessToken, providerService.getID(), "get_provider");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_provider");
            permission = authService.createPermission(accessToken, "get_provider_ratings", "Get Provider Ratings", "Get provider ratings");
            authService.addServicePermission(accessToken, providerService.getID(), "get_provider_ratings");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_provider_ratings");
            permission = authService.createPermission(accessToken, "add_provider_rating", "Add Provider Rating", "Add provider rating");
            authService.addServicePermission(accessToken, providerService.getID(), "add_provider_rating");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "add_provider_rating");
            permission = authService.createPermission(accessToken, "remove_provider_rating", "Remove Provider Rating", "Remove provider rating");
            authService.addServicePermission(accessToken, providerService.getID(), "remove_provider_rating");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "remove_provider_rating");
            permission = authService.createPermission(accessToken, "delete_provider", "Delete Provider", "Delete provider");
            authService.addServicePermission(accessToken, providerService.getID(), "delete_provider");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "delete_provider");
            permission = authService.createPermission(accessToken, "valid_provider_ratings", "Valid Provider Ratings", "Valid provider ratings");
            authService.addServicePermission(accessToken, providerService.getID(), "valid_provider_ratings");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "valid_provider_ratings");
            permission = authService.createPermission(accessToken, "valid_provider", "Valid Provider", "Valid provider");
            authService.addServicePermission(accessToken, providerService.getID(), "valid_provider");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "valid_provider");
            permission = authService.createPermission(accessToken, "create_office_space", "Create Office Space", "Create office space");
            authService.addServicePermission(accessToken, providerService.getID(), "create_office_space");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "create_office_space");
            permission = authService.createPermission(accessToken, "get_office_space", "Get Office Space", "Get office space");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space");
            permission = authService.createPermission(accessToken, "delete_office_space", "Delete Office Space", "Delete office space");
            authService.addServicePermission(accessToken, providerService.getID(), "delete_office_space");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "delete_office_space");
            permission = authService.createPermission(accessToken, "get_office_space_name", "Get Office Space Name", "Get office space name");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_name");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_name");
            permission = authService.createPermission(accessToken, "update_office_space_name", "Update Office Space Name", "Update office space name");
            authService.addServicePermission(accessToken, providerService.getID(), "update_office_space_name");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "update_office_space_name");
            permission = authService.createPermission(accessToken, "get_office_space_features", "Get Office Space Features", "Get office space features");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_features");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_features");
            permission = authService.createPermission(accessToken, "add_office_space_feature", "Add Office Space Feature", "Add office space feature");
            authService.addServicePermission(accessToken, providerService.getID(), "add_office_space_feature");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "add_office_space_feature");
            permission = authService.createPermission(accessToken, "remove_office_space_feature", "Remove Office Space Feature", "Remove office space feature");
            authService.addServicePermission(accessToken, providerService.getID(), "remove_office_space_feature");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "remove_office_space_feature");
            permission = authService.createPermission(accessToken, "get_office_space_common_accesses", "Get Office Space Common Accesses", "Get office space common accesses");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_common_accesses");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_common_accesses");
            permission = authService.createPermission(accessToken, "add_office_space_common_access", "Add Office Space Common Access", "Add office space common access");
            authService.addServicePermission(accessToken, providerService.getID(), "add_office_space_common_access");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "add_office_space_common_access");
            permission = authService.createPermission(accessToken, "remove_office_space_common_access", "Remove Office Space Common Access", "Remove office space common access");
            authService.addServicePermission(accessToken, providerService.getID(), "remove_office_space_common_access");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "remove_office_space_common_access");
            permission = authService.createPermission(accessToken, "get_office_space_location", "Get Office Space Location", "Get office space location");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_location");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_location");
            permission = authService.createPermission(accessToken, "update_office_space_location", "Update Office Space Location", "Update office space location");
            authService.addServicePermission(accessToken, providerService.getID(), "update_office_space_location");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "update_office_space_location");
            permission = authService.createPermission(accessToken, "get_office_space_capacity", "Get Office Space Capacity", "Get office space capacity");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_capacity");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_capacity");
            permission = authService.createPermission(accessToken, "update_office_space_capacity", "Update Office Space Capacity", "Update office space capacity");
            authService.addServicePermission(accessToken, providerService.getID(), "update_office_space_capacity");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "update_office_space_capacity");
            permission = authService.createPermission(accessToken, "get_office_space_facility_type", "Get Office Space Facility Type", "Get office space facility type");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_facility_type");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_facility_type");
            permission = authService.createPermission(accessToken, "update_office_space_facility_type", "Update Office Space Facility Type", "Update office space facility type");
            authService.addServicePermission(accessToken, providerService.getID(), "update_office_space_facility_type");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "update_office_space_facility_type");
            permission = authService.createPermission(accessToken, "get_office_space_facility_subtype", "Get Office Space Facility Subtype", "Get office space facility subtype");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_facility_subtype");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_facility_subtype");
            permission = authService.createPermission(accessToken, "update_office_space_facility_subtype", "Update Office Space Facility Type", "Update office space facility subtype");
            authService.addServicePermission(accessToken, providerService.getID(), "update_office_space_facility_subtype");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "update_office_space_facility_subtype");
            permission = authService.createPermission(accessToken, "get_office_space_rates", "Get Office Space Rates", "Get office space rates");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_rates");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_rates");
            permission = authService.createPermission(accessToken, "add_office_space_rate", "Add Office Space Rate", "Add office space rate");
            authService.addServicePermission(accessToken, providerService.getID(), "add_office_space_rate");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "add_office_space_rate");
            permission = authService.createPermission(accessToken, "update_office_space_rate", "Update Office Space Rate", "Update office space rate");
            authService.addServicePermission(accessToken, providerService.getID(), "update_office_space_rate");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "update_office_space_rate");
            permission = authService.createPermission(accessToken, "remove_office_space_rate", "Remove Office Space Rate", "Remove office space rate");
            authService.addServicePermission(accessToken, providerService.getID(), "remove_office_space_rate");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "remove_office_space_rate");
            permission = authService.createPermission(accessToken, "get_office_space_pictures", "Get Office Space Pictures", "Get office space pictures");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_pictures");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_pictures");
            permission = authService.createPermission(accessToken, "add_office_space_picture", "Add Office Space Picture", "Add office space picture");
            authService.addServicePermission(accessToken, providerService.getID(), "add_office_space_picture");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "add_office_space_picture");
            permission = authService.createPermission(accessToken, "update_office_space_picture", "Update Office Space Picture", "Update office space picture");
            authService.addServicePermission(accessToken, providerService.getID(), "update_office_space_picture");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "update_office_space_picture");
            permission = authService.createPermission(accessToken, "remove_office_space_picture", "Remove Office Space Picture", "Remove office space picture");
            authService.addServicePermission(accessToken, providerService.getID(), "remove_office_space_picture");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "remove_office_space_picture");
            permission = authService.createPermission(accessToken, "get_office_space_facility_pictures", "Get Office Space Facility Pictures", "Get office space facility pictures");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_facility_pictures");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_facility_pictures");
            permission = authService.createPermission(accessToken, "add_office_space_facility_picture", "Add Office Space Facility Picture", "Add office space facility picture");
            authService.addServicePermission(accessToken, providerService.getID(), "add_office_space_facility_picture");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "add_office_space_facility_picture");
            permission = authService.createPermission(accessToken, "update_office_space_facility_picture", "Update Office Space Facility Picture", "Update office space facility picture");
            authService.addServicePermission(accessToken, providerService.getID(), "update_office_space_facility_picture");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "update_office_space_facility_picture");
            permission = authService.createPermission(accessToken, "remove_office_space_facility_picture", "Remvoe Office Space Facility Picture", "Remove office space facility picture");
            authService.addServicePermission(accessToken, providerService.getID(), "remove_office_space_facility_picture");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "remove_office_space_facility_picture");
            permission = authService.createPermission(accessToken, "valid_office_space_location", "Valid Office Space Location", "Valid office space location");
            authService.addServicePermission(accessToken, providerService.getID(), "valid_office_space_location");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "valid_office_space_location");
            permission = authService.createPermission(accessToken, "valid_office_space_capacity", "Valid Office Space Capacity", "Valid office space capacity");
            authService.addServicePermission(accessToken, providerService.getID(), "valid_office_space_capacity");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "valid_office_space_capacity");
            permission = authService.createPermission(accessToken, "valid_office_space_facility", "Valid Office Space Facility", "Valid office space facility");
            authService.addServicePermission(accessToken, providerService.getID(), "valid_office_space_facility");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "valid_office_space_facility");
            permission = authService.createPermission(accessToken, "valid_office_space_rates", "Valid Office Space Rates", "Valid office space rates");
            authService.addServicePermission(accessToken, providerService.getID(), "valid_office_space_rates");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "valid_office_space_rates");
            permission = authService.createPermission(accessToken, "valid_office_space", "Valid Office Space", "Valid office space");
            authService.addServicePermission(accessToken, providerService.getID(), "valid_office_space");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "valid_office_space");
            permission = authService.createPermission(accessToken, "get_office_space_ratings", "Get Office Space Ratings", "Get office space ratings");
            authService.addServicePermission(accessToken, providerService.getID(), "get_office_space_ratings");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "get_office_space_ratings");
            permission = authService.createPermission(accessToken, "add_office_space_rating", "Add Office Space Rating", "Add office space rating");
            authService.addServicePermission(accessToken, providerService.getID(), "add_office_space_rating");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "add_office_space_rating");
            permission = authService.createPermission(accessToken, "remove_office_space_rating", "Remove Office Space Rating", "Remove office space rating");
            authService.addServicePermission(accessToken, providerService.getID(), "remove_office_space_rating");
            authService.addRolePermission(accessToken, providerUserRole.getID(), "remove_office_space_rating");

            User providerUser = authService.createUser(accessToken, "Provider User", "provider_user");
            authService.addUserCredential(accessToken, "provider_user", "provider_user", "p4ssw0rd");
            authService.addUserRole(accessToken, providerUser.getUserID(), providerUserRole.getID());

            authService.logout(accessToken);

        } catch (InvalidAccessTokenException iate) {
            System.out.println("OfficeProviderServiceAPI.registerWithAuthService: InvalidAccessTokenException occurred.");
        } catch (UnauthorizedAccessException uae) {
            System.out.println("OfficeProviderServiceAPI.registerWithAuthService: UnauthorizedAccessException occurred.");
        } catch (InvalidUserIDException iue) {
            System.out.println("OfficeProviderServiceAPI.registerWithAuthService: InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("OfficeProviderServiceAPI.registerWithAuthService: InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("OfficeProviderServiceAPI.registerWithAuthService: AuthServiceException occurred.");
        }
    }
}
