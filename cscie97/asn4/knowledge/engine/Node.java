/*
 * Node
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
 * This class represents a node in the knowledge graph (see assignment design
 * document for more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Node {
    
    /**
     * Regular expression describing well-formed node.
     */
    public static String regex = "[a-zA-Z0-9_]+";
    
    /**
     * Node identifier.
     */
    private String identifier;
    
    /**
     * Constructor, takes String identifier for node.
     * 
     * @param Given node identifier.
     */
    public Node(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Returns Node object identifier.
     * 
     * @return Node identifier.
     */
    public String getIdentifier() {
        return identifier;
    }
}
