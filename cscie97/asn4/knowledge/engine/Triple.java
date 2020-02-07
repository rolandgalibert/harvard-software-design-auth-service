/*
 * Triple
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
 * This class represents a triple in the knowledge graph (see assignment design
 * document for more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Triple {
    
    /**
     * Triple identifier.
     */
    private String identifier;

    /**
     * Subject associated with triple.
     */
    private Node subject;

    /**
     * Predicate associated with triple.
     */
    private Predicate predicate;
    
    /**
     * Object associated with triple.
     */
    private Node object;
    
    /**
     * Constructor, takes String identifier for triple.
     * 
     * @param identifier Given triple identifier.
     * @param subject Subject associated with triple.
     * @param predicate Predicate associated with triple.
     * @param object Object associated with triple.
     */
    public Triple(String identifier, Node subject, Predicate predicate, Node object) {
        this.identifier = identifier;
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }
    
    /**
     * Returns String identifier for this triple.
     * 
     * @return Triple identifier.
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * Returns subject (Node object) associated with this triple.
     * 
     * @return Triple subject.
     */
    public Node getSubject() {
        return subject;
    }
    
    /**
     * Returns predicate (Predicate object) associated with this triple.
     * 
     * @return Triple predicate.
     */
    public Predicate getPredicate() {
        return predicate;
    }
    
    /**
     * Returns object (Node object) associated with this triple.
     * 
     * @return Triple object.
     */
    public Node getObject() {
        return object;
    }
}
