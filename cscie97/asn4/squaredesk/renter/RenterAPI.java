/*
 * RenterAPI
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
import cscie97.asn4.squaredesk.provider.Rating;
import cscie97.asn4.squaredesk.provider.RatingNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * This singleton class maintains a map of Renter objects for the SquareDesk
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
public class RenterAPI {

    /**
     * Map of active Renter objects (Renter UUID to Renter object).
     */
    private HashMap<UUID, Renter> renterMap = new HashMap<UUID, Renter>();
    /**
     * Authentication service object
     */
    private static AuthServiceImpl authService;

    /**
     * Private hidden singleton constructor.
     */
    private RenterAPI() {
        authService = AuthServiceImpl.getInstance();
        registerWithAuthService();
    }

    /**
     * UserAPIHolder is loaded on the first execution of UserAPI.getInstance()
     * or the first access to UserAPIHolder.USER_API_INSTANCE, not before.
     */
    private static class RenterAPIHolder {

        private static final RenterAPI RENTER_API_INSTANCE = new RenterAPI();
    }

    /**
     * Returns singleton object instance.
     *
     * @return Singleton UserAPI object.
     */
    public static RenterAPI getInstance() {
        authService = AuthServiceImpl.getInstance();
        return RenterAPIHolder.RENTER_API_INSTANCE;
    }

    /**
     * Returns map of currently active Renter objects, in the form of a list.
     *
     * @param authToken Client authentication token.
     * @return Currently active Renter objects.
     */
    public ArrayList<Renter> getRenterList(String authToken)
            throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_renter_list", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            return new ArrayList<Renter>(renterMap.values());
        }
    }

    /**
     * Creates new Renter and adds it to the map of currently active Renter
     * objects.
     *
     * @param authToken Client authentication token.
     * @return Newly created Renter.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter createRenter(String authToken) throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("create_renter", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            Renter newRenter = new Renter();
            renterMap.put(newRenter.getID(), newRenter);
            return newRenter;
        }
    }

    /**
     * Returns Renter specified by given ID.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @return Specified Renter.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter getRenter(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_renter", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Removes specified Renter from map of currently active Renters.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void deleteRenter(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("delete_renter", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.remove(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Returns the Criteria object associated with given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @return Renter's office space criteria.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public RenterCriteria getRenterCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_renter_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                return renterMap.get(renterID).getCriteria();
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Clears all properties in the Criteria object associated with the given
     * Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearAllRenterCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_all_renter_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                ArrayList<String> features = renterMap.get(renterID).getCriteria().getFeatures();
                for (int i = 0; i < features.size(); i++) {
                    renterMap.get(renterID).getCriteria().removeFeature(features.get(i));
                }
                renterMap.get(renterID).getCriteria().setLocationLatitude(0.0);
                renterMap.get(renterID).getCriteria().setLocationLongitude(0.0);
                renterMap.get(renterID).getCriteria().setFacilityType("");
                renterMap.get(renterID).getCriteria().setFacilitySubtype("");
                renterMap.get(renterID).getCriteria().setMinimumRating((short) 0);
                renterMap.get(renterID).getCriteria().setStartDate(new Date());
                renterMap.get(renterID).getCriteria().setEndDate(new Date());
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Updates the Location in the Criteria object associated with the given
     * Renter, with the specified latitude and longitude.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param latitude Renter office space latitude criteria.
     * @param longitude Renter office space longitude criteria.
     * @return Updated Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter updateRenterLocationCriteria(String authToken, UUID renterID,
            double latitude, double longitude) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_renter_location_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setLocationLatitude(latitude);
                renterMap.get(renterID).getCriteria().setLocationLongitude(longitude);
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Clears the Location in the Criteria object associated with the given
     * Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearRenterLocationCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_renter_location_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setLocationLatitude(0.0);
                renterMap.get(renterID).getCriteria().setLocationLongitude(0.0);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Adds a feature to the list of Features in the Criteria object associated
     * with the given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param feature
     * @return Updated Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter addRenterFeatureCriteria(String authToken, UUID renterID,
            String feature) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_renter_feature_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().addFeature(feature);
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Remove a given feature from the list of Features in the Criteria object
     * associated with the given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param feature Feature to remove.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeRenterFeatureCriteria(String authToken, UUID renterID,
            String feature) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_renter_feature_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().removeFeature(feature);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Remove all features from the list of Features in the Criteria object
     * associated with the given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearAllRenterFeatureCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_all_renter_feature_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                ArrayList<String> features = renterMap.get(renterID).getCriteria().getFeatures();
                for (int i = 0; i < features.size(); i++) {
                    renterMap.get(renterID).getCriteria().removeFeature(features.get(i));
                }
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Updates the FacilityType in the Criteria object associated with the given
     * Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param facilityType New facility type.
     * @return Updated Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter updateRenterFacilityTypeCriteria(String authToken, UUID renterID,
            String facilityType) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_renter_facility_type_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setFacilityType(facilityType);
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Clears the FacilityType in the Criteria object associated with the given
     * Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearRenterFacilityTypeCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_renter_facility_type_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setFacilityType("");
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Updates the FacilitySubtype in the Criteria object associated with the
     * given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param facilitySubtype New facility subtype.
     * @return Updated Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter updateRenterFacilitySubtypeCriteria(String authToken, UUID renterID,
            String facilitySubtype) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_renter_facility_subtype_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setFacilitySubtype(facilitySubtype);
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Clears the FacilitySubtype in the Criteria object associated with the
     * given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearRenterFacilitySubtypeCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_renter_facility_subtype_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setFacilitySubtype("");
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Updates the minimum office space rating required by the given Renter, in
     * the Criteria object associated with the given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param newMinimumRating
     * @return Updated Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter updateRenterMinimumRatingCriteria(String authToken, UUID renterID,
            short newMinimumRating) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_renter_minimum_rating_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setMinimumRating(newMinimumRating);
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Clears the minimumRating field in the Criteria object associated with the
     * given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearRenterMinimumRatingCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_renter_minimum_rating_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setMinimumRating((short) 0);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Updates the rental start date required by the given Renter, in the
     * Criteria object associated with the given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param newStartDate New start date.
     * @return Updated Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter updateRenterStartDateCriteria(String authToken, UUID renterID,
            Date newStartDate) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_renter_start_date_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setStartDate(newStartDate);
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Clears the rental start date required by the given Renter, in the
     * Criteria object associated with the given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearRenterStartDateCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_renter_start_date_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().clearStartDate();
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Updates the rental end date required by the given Renter, in the Criteria
     * object associated with the given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param newEndDate New end date.
     * @return Updated Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter updateRenterEndDateCriteria(String authToken, UUID renterID,
            Date newEndDate) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("update_renter_end_date_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().setEndDate(newEndDate);
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Clears the rental end date required by the given Renter, in the Criteria
     * object associated with the given Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearRenterEndDateCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("clear_renter_end_date_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).getCriteria().clearEndDate();
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @return
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public ArrayList<Rating> getRenterRatings(String authToken, UUID renterID)
            throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("get_renter_ratings", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                return renterMap.get(renterID).getRatings();
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Adds a Rating to the rating list of the Renter with the specified ID.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param rating Renter rating (Rating object).
     * @return
     * @throws RenterNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public Renter addRenterRating(String authToken, UUID renterID,
            Rating rating) throws RenterNotFoundException, InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("add_renter_rating", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                renterMap.get(renterID).addRatingToList(rating);
                return renterMap.get(renterID);
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Removes the specified Rating from the rating list of the Renter with the
     * specified ID.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @param rating Renter rating (Rating object).
     * @throws RenterNotFoundException
     * @throws RatingNotFoundException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void removeRenterRating(String authToken, UUID renterID,
            Rating rating) throws RenterNotFoundException, RatingNotFoundException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("remove_renter_rating", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                if (renterMap.get(renterID).ratingExistsOnList(rating)) {
                    renterMap.get(renterID).removeRatingFromList(rating);
                } else {
                    throw new RatingNotFoundException();
                }
            } else {
                throw new RenterNotFoundException();
            }
        }
    }

    /**
     * Validates the rental period specified in the RentalCriteria associated
     * with the specified Renter.
     *
     * @param authToken Client authentication token.
     * @param renterID Unique identifier for desired Renter object.
     * @return True if rental period is valid, false otherwise.
     * @throws RenterNotFoundException
     * @throws RentalPeriodNotValidException
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public boolean validRenterRentalPeriodCriteria(String authToken, UUID renterID)
            throws RenterNotFoundException, RentalPeriodNotValidException,
            InvalidAccessTokenException, UnauthorizedAccessException {
        if (!validAccessToken(authToken)) {
            throw new InvalidAccessTokenException();
        } else if (!authService.checkAccess("valid_renter_rental_period_criteria", authToken)) {
            throw new UnauthorizedAccessException();
        } else {
            if (renterMap.containsKey(renterID)) {
                if (renterMap.get(renterID).getCriteria().getEndDate().compareTo(renterMap.get(renterID).getCriteria().getStartDate()) < 1) {
                    throw new RentalPeriodNotValidException();
                } else {
                    return true;
                }
            } else {
                throw new RenterNotFoundException();
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
     * This method registers the following with the AuthService: - The Renter
     * service (represented by this class) - Restricted Provider service methods
     * This method also creates a Renter role with permission to the above
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

            Service renterService = authService.createService(accessToken, "renter_service", "Office Space Renter Service", "Office space renter service");
            Role renterUserRole = authService.createRole(accessToken, "squaredesk_renter", "SquareDesk Renter", "SquareDesk renter");

            Permission permission = authService.createPermission(accessToken, "get_renter_list", "Get Renter List", "Get renter list");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "create_renter", "Create Renter", "Create renter");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "get_renter", "Get Renter", "Get renter");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "delete_renter", "Delete Renter", "Delete renter");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "get_renter_criteria", "Get Renter Criteria", "Get renter criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_all_renter_criteria", "Clear all renter criteria", "Clear all renter criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_renter_location_criteria", "Update Renter Location Criteria", "Update renter location criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_renter_location_criteria", "Clear Renter Location Criteria", "Clear renter location criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "add_renter_feature_criteria", "Add Renter Feature Criteria", "Add renter feature criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "remove_renter_feature_criteria", "Remove Renter Feature Criteria", "Remove renter feature criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_all_renter_feature_criteria", "Clear All Renter Feature Criteria", "Clear all renter feature criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_renter_facility_type_criteria", "Update Renter Facility Type Criteria", "Update renter facility type criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_renter_facility_type_criteria", "Clear Renter Facility Type Criteria", "Clear renter facility type criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_renter_facility_subtype_criteria", "Update Renter Facility Subtype Criteria", "Update renter facility subtype criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_renter_facility_subtype_criteria", "Clear Renter Facility Subtype Criteria", "Clear renter facility subtype criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_renter_minimum_rating_criteria", "Update Renter Minimum Rating Criteria", "Update renter minimum rating criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_renter_minimum_rating_criteria", "Clear Renter Minimum Rating Criteria", "Clear renter minimum rating criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_renter_start_date_criteria", "Update Renter Start Date Criteria", "Update renter start date criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_renter_start_date_criteria", "Clear Renter Start Date Criteria", "Clear renter start date criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "update_renter_end_date_criteria", "Update Renter End Date Criteria", "Update renter end date criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "clear_renter_end_date_criteria", "Clear Renter End Date Criteria", "Clear renter end date criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "get_renter_ratings", "Get Renter Ratings", "Get renter ratings");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "add_renter_rating", "Add Renter Rating", "Add renter rating");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "remove_renter_rating", "Remove Renter Rating", "Remove renter rating");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());
            permission = authService.createPermission(accessToken, "valid_renter_rental_period_criteria", "Valid Renter Rental Period Criteria", "Valid renter rental period criteria");
            authService.addServicePermission(accessToken, renterService.getID(), permission.getID());
            authService.addRolePermission(accessToken, renterUserRole.getID(), permission.getID());

            cscie97.asn4.squaredesk.authentication.User renterUser = authService.createUser(accessToken, "Renter User", "renter_user");
            authService.addUserCredential(accessToken, "renter_user", "renter_user", "p4ssw0rd");
            authService.addUserRole(accessToken, renterUser.getUserID(), renterUserRole.getID());
            
            authService.logout(accessToken);
        } catch (InvalidAccessTokenException iate) {
            System.out.println("RenterAPI.registerWithAuthService: InvalidAccessTokenException occurred.");
        } catch (UnauthorizedAccessException uae) {
            System.out.println("RenterAPI.registerWithAuthService: UnauthorizedAccessException occurred.");
        } catch (InvalidUserIDException iue) {
            System.out.println("RenterAPI.registerWithAuthService: InvalidUserIDException occurred.");
        } catch (InvalidPasswordException ipe) {
            System.out.println("RenterAPI.registerWithAuthService: InvalidPasswordException occurred.");
        } catch (AuthServiceException ase) {
            System.out.println("RenterAPI.registerWithAuthService: AuthServiceException occurred.");
        }
    }
}
