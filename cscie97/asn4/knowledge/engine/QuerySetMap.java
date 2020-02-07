/*
 * QuerySetMap
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
import java.util.HashSet;

/**
 * This class extends the java.util.HashMap to allow for implementation of a
 * <String, HashSet<Triple>> map which performs case-insensitive key lookups.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class QuerySetMap extends HashMap<String, HashSet<Triple>> {

    private static final long serialVersionUID = 1L;
    
    /**
     * Overriding get method.
     * 
     * @param key Node key (String)
     * @return HashSet<Triple> object associated with given key (null if none found)
     */
    public HashSet<Triple> get(String key) {
        return super.get(key.toLowerCase());
    }

    /**
     * Overriding put method.
     * 
     * @param key HashSet<Triple> key (String)
     * @param value HashSet<Triple> object to store
     * @return HashSet<Triple> object associated with given key
     */
    @Override
    public HashSet<Triple> put(String key, HashSet<Triple> value) {
        return super.put(key.toLowerCase(), value);
    }
}

