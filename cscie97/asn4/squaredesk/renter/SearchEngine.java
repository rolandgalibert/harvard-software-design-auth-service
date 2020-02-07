/*
 * SearchEngine
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

import cscie97.asn4.squaredesk.provider.Rating;
import cscie97.asn4.squaredesk.provider.OfficeProviderServiceAPI;
import cscie97.asn4.squaredesk.provider.OfficeSpace;
import cscie97.asn4.squaredesk.provider.Location;
import cscie97.asn4.knowledge.engine.KnowledgeGraph;
import cscie97.asn4.knowledge.engine.Triple;
import cscie97.asn4.squaredesk.authentication.InvalidAccessTokenException;
import cscie97.asn4.squaredesk.authentication.UnauthorizedAccessException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

/**
 * This singleton class contains a singleton KnowledgeGraph object, as well as
 * methods that allow OfficeSpace criteria to be stored in the KnowledgeGraph
 * and that allow searches to be carried out for OfficeSpace objects, based on
 * those criteria.
 *
 * Only authenticated clients with appropriate permission may execute these
 * methods.
 *
 * SearchEngine is constructed following the "Initialization-on-demand holder
 * idiom" singleton as described in the Wikipedia article "Singleton pattern".
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class SearchEngine {

    /**
     * Singleton OfficeSpace Provider Service API object (used mainly for
     * accessing list of currently active OfficeSpace objects).
     */
    private static OfficeProviderServiceAPI officeProviderServiceAPI;
    
    /**
     * Singleton KnowledgeGraph object for OfficeSpace searches.
     */
    private static KnowledgeGraph knowledgeGraph = KnowledgeGraph.getInstance();
    
    /**
     * Constant feature predicate string - corresponding OfficeSpace triples are
     * stored in KnowledgeGraph in form
     * 
     * <OfficeSpace ID> has_feature <feature description>
     */
    private static final String FEATURE_PREDICATE = "has_feature";

    /**
     * Constant location predicate string - corresponding OfficeSpace triples are
     * stored in KnowledgeGraph in form
     * 
     * <OfficeSpace ID> has_lat_long <latitude_longitude>
     */
    private static final String LOCATION_PREDICATE = "has_lat_long";
    
    /**
     * Constant facility type predicate string - corresponding OfficeSpace triples are
     * stored in KnowledgeGraph in form
     * 
     * <OfficeSpace ID> has_facility_type_category <facility type>
     * 
     * OR (if facility type includes category)
     * 
     * <OfficeSpace ID> has_facility_type_category <facility type_category>
     */
    private static final String FACILITY_TYPE_PREDICATE = "has_facility_type_category";
    
    /**
     * Constant average rating predicate string - corresponding OfficeSpace triples are
     * stored in KnowledgeGraph in form
     * 
     * <OfficeSpace ID> has_average_rating <average rating of OfficeSpace>
     */
    private static final String AVERAGE_RATING_PREDICATE = "has_average_rating";

    /**
     * SearchEngineHolder is loaded on the first execution of
     * SearchEngine.getInstance() or the first access to
     * SearchEngineHolder.SEARCH_ENGINE_INSTANCE, not before.
     */
    private static class SearchEngineHolder {

        private static final SearchEngine SEARCH_ENGINE_INSTANCE = new SearchEngine();
    }

    /**
     * Returns singleton object instance.
     *
     * @return Singleton SearchEngine object.
     */
    public static SearchEngine getInstance() {
        officeProviderServiceAPI = OfficeProviderServiceAPI.getInstance();
        return SearchEngineHolder.SEARCH_ENGINE_INSTANCE;
    }

    /**
     * Returns a list of OfficeSpace UUIDs that match the criteria specified 
     * in the specified RenterCriteria object.
     * 
     * @param authToken Client authentication token.
     * @param criteria RenterCriteria object with Renter OfficeSpace requirements.
     * @return List of UUIDs of OfficeSpace object that match criteria.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public ArrayList<UUID> searchForOfficeSpaces(String authToken,
            RenterCriteria criteria) throws InvalidAccessTokenException, UnauthorizedAccessException {
        String queryObject;
        Triple searchTriple;

        /*
         * Initialize final results to all available office spaces
         */
        if (officeProviderServiceAPI == null) {
            System.out.println("officeProviderServiceAPI is null.");
        }
        HashSet<UUID> finalResults = new HashSet<UUID>(officeProviderServiceAPI.getOfficeSpaceUUIDList(authToken));
        HashSet<UUID> intermediateResults;

        /*
         * Search for features and use set intersection so finalResults only
         * contains OfficeSpace objects that contain given RenterCriteria features.
         */
        if (criteria.getFeatures().size() > 0) {
            for (int i = 0; i < criteria.getFeatures().size(); i++) {
                String feature = criteria.getFeatures().get(i).replaceAll(" ", "_");
                searchTriple = knowledgeGraph.getTriple(knowledgeGraph.getNode("?"),
                        knowledgeGraph.getPredicate(FEATURE_PREDICATE),
                        knowledgeGraph.getNode(feature));
                intermediateResults = tripleSetOfficeSpaces((HashSet<Triple>) knowledgeGraph.executeQuery(searchTriple));
                finalResults.retainAll(intermediateResults);
            }
        }

        /*
         * Search for location and use set intersection so finalResults only
         * contains OfficeSpace objects that contain given RenterCriteria location.
         * 
         * Format latitude, longitude per requirements.
         */
        int latitude = (int) Math.floor(criteria.getLocationLatitude());
        int longitude = (int) Math.floor(criteria.getLocationLongitude());
        queryObject = latitude + "_" + longitude;
        searchTriple = knowledgeGraph.getTriple(knowledgeGraph.getNode("?"),
                knowledgeGraph.getPredicate(LOCATION_PREDICATE),
                knowledgeGraph.getNode(queryObject));
        intermediateResults = tripleSetOfficeSpaces((HashSet<Triple>) knowledgeGraph.executeQuery(searchTriple));
        finalResults.retainAll(intermediateResults);


        /*
         * Search for facility type (and type category if applicable) and use
         * set intersection so finalResults only contains OfficeSpace objects 
         * that contain given RenterCriteria facilityType/facilitySubtype.
         */
        if (!criteria.getFacilityType().isEmpty()) {
            if (!criteria.getFacilitySubtype().isEmpty()) {
                queryObject = criteria.getFacilityType().replaceAll(" ", "_") + "_"
                        + criteria.getFacilitySubtype().replaceAll(" ", "_");
            } else {
                queryObject = criteria.getFacilityType().replaceAll(" ", "_");
            }
            searchTriple = knowledgeGraph.getTriple(knowledgeGraph.getNode("?"),
                    knowledgeGraph.getPredicate(FACILITY_TYPE_PREDICATE),
                    knowledgeGraph.getNode(queryObject));
            intermediateResults = tripleSetOfficeSpaces((HashSet<Triple>) knowledgeGraph.executeQuery(searchTriple));
            finalResults.retainAll(intermediateResults);
        }

        /*
         * Search for minimum average rating and use
         * set intersection so finalResults only contains OfficeSpace objects 
         * that contain given RenterCriteria minimum average rating.
         */
        if (criteria.getMinimumRating() > -1) {
            queryObject = Integer.toString(criteria.getMinimumRating());
            searchTriple = knowledgeGraph.getTriple(knowledgeGraph.getNode("?"),
                    knowledgeGraph.getPredicate(AVERAGE_RATING_PREDICATE),
                    knowledgeGraph.getNode(queryObject));
            intermediateResults = tripleSetOfficeSpaces((HashSet<Triple>) knowledgeGraph.executeQuery(searchTriple));
            for (int i = criteria.getMinimumRating() + 1; i <= Rating.MAXIMUM_RATING; i++) {
                queryObject = Integer.toString(i);
                searchTriple = knowledgeGraph.getTriple(knowledgeGraph.getNode("?"),
                        knowledgeGraph.getPredicate(AVERAGE_RATING_PREDICATE),
                        knowledgeGraph.getNode(queryObject));
                intermediateResults.addAll(tripleSetOfficeSpaces((HashSet<Triple>) knowledgeGraph.executeQuery(searchTriple)));
            }
            finalResults.retainAll(intermediateResults);
        }
        return new ArrayList<UUID>(finalResults);
    }

    /**
     * Extracts OfficeSpace IDs from the specified Triple set and returns
     * these IDs as a HashSet.
     * 
     * @param triples Triples in form <officeSpace ID> <has_criteria> <criteria>
     * @return Set of OfficeSpace UUIDs in specified Triple set.
     */
    private HashSet<UUID> tripleSetOfficeSpaces(HashSet<Triple> triples) {
        Iterator<Triple> iterator = triples.iterator();
        HashSet<UUID> officeSpaces = new HashSet<UUID>();
        while (iterator.hasNext()) {
            Triple currentTriple = iterator.next();
            officeSpaces.add(UUID.fromString(currentTriple.getSubject().getIdentifier()));
        }
        return officeSpaces;
    }

    /**
     * Evaluates specified OfficeSpace and enters corresponding KnowledgeGraph triples on the
     * basis of the features, etc. contained in the OfficeSpace.
     * 
     * @param authToken Client authentication token.
     * @param officeSpace
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void addTriples(String authToken, OfficeSpace officeSpace) throws InvalidAccessTokenException, UnauthorizedAccessException {
        String queryObject;

        // Add feature location, including common access areas
        for (int i = 0; i < officeSpace.getFeatures().size(); i++) {
            knowledgeGraph.importTriple(officeSpace.getID().toString(), FEATURE_PREDICATE,
                    officeSpace.getFeatures().get(i).getDescription().replaceAll(" ", "_"));
        }
        for (int i = 0; i < officeSpace.getCommonAccessAreas().size(); i++) {
            knowledgeGraph.importTriple(officeSpace.getID().toString(), FEATURE_PREDICATE,
                    officeSpace.getCommonAccessAreas().get(i).getDescription().replaceAll(" ", "_"));
        }

        // Add location information
        int latitude = (int) Math.floor(officeSpace.getLocation().getLatitude());
        int longitude = (int) Math.floor(officeSpace.getLocation().getLongitude());
        queryObject = latitude + "_" + longitude;
        knowledgeGraph.importTriple(officeSpace.getID().toString(), LOCATION_PREDICATE, queryObject);

        // Add facility type information
        if (!officeSpace.getFacilityType().isEmpty()) {
            if (!officeSpace.getFacilitySubtype().isEmpty()) {
                queryObject = officeSpace.getFacilityType().replaceAll(" ", "_")
                        + "_" + officeSpace.getFacilitySubtype().replaceAll(" ", "_");
            } else {
                queryObject = officeSpace.getFacilityType().replaceAll(" ", "_");
            }
            knowledgeGraph.importTriple(officeSpace.getID().toString(), FACILITY_TYPE_PREDICATE, queryObject);
        }

        // Add rating information.
        // Calculate average rating for this office space.
        if (officeSpace.getRatings().size() > 0) {
            float ratingTotal = 0;
            int ratingCount = 0;
            int averageRating = 0;

            for (int i = 0; i < officeSpace.getRatings().size(); i++) {
                ratingTotal = ratingTotal + officeSpace.getRatings().get(i).getRating();
                ratingCount++;
            }
            averageRating = (int) Math.floor(ratingTotal / (float) ratingCount);
            knowledgeGraph.importTriple(officeSpace.getID().toString(), AVERAGE_RATING_PREDICATE,
                    Integer.toString(averageRating));
        }
    }

    /**
     * Clears the knowledge graph of all triples that contain the given OfficeSpace
     * ID as a subject.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID OfficeSpace ID to be removed from KnowledgeGraph.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void clearTriples(String authToken, UUID officeSpaceID)
            throws InvalidAccessTokenException, UnauthorizedAccessException {
        knowledgeGraph.removeSubjectFromKnowledgeGraph(officeSpaceID.toString());
    }

    /**
     * Adds a feature triple to the KnowledgeGraph with the specified OfficeSpace
     * ID and feature.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID OfficeSpace ID to add to KnowledgeGraph
     * @param feature Feature description to be associated with OfficeSpace ID
     * in the KnowledgeGraph.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void addOfficeSpaceFeatureTriple(String authToken, OfficeSpace officeSpace,
            String feature) throws InvalidAccessTokenException, UnauthorizedAccessException {
        feature = feature.replaceAll(" ", "_");
        knowledgeGraph.importTriple(officeSpace.getID().toString(), FEATURE_PREDICATE, feature);
    }

    /**
     * Adds a common access area triple to the KnowledgeGraph with the specified 
     * OfficeSpace ID and common access area description.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID OfficeSpace ID to add to KnowledgeGraph
     * @param commonAccess Common access area description to be associated with OfficeSpace ID
     * in the KnowledgeGraph.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void addOfficeSpaceCommonAccessTriple(String authToken, OfficeSpace officeSpace,
            String commonAccess) throws InvalidAccessTokenException, UnauthorizedAccessException {
        knowledgeGraph.importTriple(officeSpace.getID().toString(), FEATURE_PREDICATE, commonAccess);
    }

    /**
     * Adds a location triple to the KnowledgeGraph with the specified 
     * OfficeSpace ID and location.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID OfficeSpace ID to add to KnowledgeGraph
     * @param location Location to be associated with OfficeSpace ID in KnowledgeGraph.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void addOfficeSpaceLocationTriple(String authToken, OfficeSpace officeSpace,
            Location location) throws InvalidAccessTokenException, UnauthorizedAccessException {
        int latitude = (int) Math.floor(location.getLatitude());
        int longitude = (int) Math.floor(location.getLongitude());
        String queryObject = latitude + "_" + longitude;
        knowledgeGraph.importTriple(officeSpace.getID().toString(), LOCATION_PREDICATE, queryObject);
    }

    /**
     * Adds a facility type triple to the KnowledgeGraph with the specified 
     * OfficeSpace ID and facility type (only facility type is passed in).
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID OfficeSpace ID to add to KnowledgeGraph
     * @param facilityType  Facility type to be associated with OfficeSpace ID in KnowledgeGraph.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void addOfficeSpaceFacilityTypeTriple(String authToken, OfficeSpace officeSpace,
            String facilityType) throws InvalidAccessTokenException, UnauthorizedAccessException {
        facilityType = facilityType.replaceAll(" ", "_");
        knowledgeGraph.importTriple(officeSpace.getID().toString(), FACILITY_TYPE_PREDICATE, facilityType);
    }

    /**
     * Adds a facility type triple to the KnowledgeGraph with the specified 
     * OfficeSpace ID and facility type (both facility type and category are passed in).
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID OfficeSpace ID to add to KnowledgeGraph
     * @param facilityType Facility type to be associated with OfficeSpace ID in KnowledgeGraph.
     * @param facilitySubtype Facility subtype to be associated with OfficeSpace ID in KnowledgeGraph.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void addOfficeSpaceFacilityTypeTriple(String authToken, OfficeSpace officeSpace,
            String facilityType, String facilitySubtype) throws InvalidAccessTokenException, UnauthorizedAccessException {
        facilityType = facilityType.replaceAll(" ", "_");
        facilitySubtype = facilitySubtype.replaceAll(" ", "_");
        knowledgeGraph.importTriple(officeSpace.getID().toString(), FACILITY_TYPE_PREDICATE,
                facilityType + "_" + facilitySubtype);
    }

    /**
     * Adds a minimum average rating triple to the KnowledgeGraph with the specified 
     * OfficeSpace ID and rating value.
     * 
     * @param authToken Client authentication token.
     * @param officeSpaceID OfficeSpace ID to add to KnowledgeGraph
     * @param minimumAverageRating Minimum average rating to be associated with
     * OfficeSpace ID in KnowledgeGraph.
     * @throws InvalidAccessTokenException, UnauthorizedAccessException
     */
    public void addOfficeSpaceMinimumAverageRatingTriple(String authToken, OfficeSpace officeSpace,
            short minimumAverageRating) throws InvalidAccessTokenException, UnauthorizedAccessException {
        if (officeSpace.getRatings().size() > 0) {
            float ratingTotal = 0;
            int ratingCount = 0;

            for (int i = 0; i < officeSpace.getRatings().size(); i++) {
                ratingTotal = ratingTotal + officeSpace.getRatings().get(i).getRating();
                ratingCount++;
            }
            int averageRating = (int) Math.floor(ratingTotal / (float) ratingCount);
            knowledgeGraph.importTriple(officeSpace.getID().toString(), AVERAGE_RATING_PREDICATE,
                    Integer.toString(averageRating));
        }
    }
}
