/*
 * NodeMap
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

import java.util.HashMap;

/**
 * This class extends the java.util.HashMap to allow for implementation of a map
 * <String, Node> map which performs case-insensitive key lookups.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class NodeMap extends HashMap<String, Node> {

    private static final long serialVersionUID = 1L;
    
    /**
     * Overriding get method.
     * 
     * @param key Node key (String)
     * @return Node object associated with given key (null if none found)
     */
    public Node get(String key) {
        return super.get(key.toLowerCase());
    }

    /**
     * Overriding put method.
     * 
     * @param key Node key (String)
     * @param value Node object to store
     * @return Node object associated with given key
     */
    @Override
    public Node put(String key, Node value) {
        return super.put(key.toLowerCase(), value);
    }
}