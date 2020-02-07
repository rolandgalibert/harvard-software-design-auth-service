/*
 * Predicate
 * 
 * Version 1.0
 * 
 * September 16, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #1.
 */
package cscie97.asn4.knowledge.engine;

/**
 * This class represents a predicate in the knowledge graph (see assignment design
 * document for more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Predicate {

    /**
     * Regular expression describing well-formed predicate.
     */
    public static String regex = "[a-zA-Z0-9_]+";
    
    /**
     * Predicate identifier.
     */
    private String identifier;
    
    /**
     * Constructor, takes String identifier for predicate.
     * 
     * @param identifier Given predicate identifier.
     */
    public Predicate(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Returns Predicate object identifier.
     * 
     * @return Predicate identifier.
     */
    public String getIdentifier() {
        return identifier;
    }
}
