/*
 * PredicateMap
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
 * This class extends the java.util.HashMap to allow for implementation of a
 * <String, Predicate> map which performs case-insensitive key lookups.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class PredicateMap extends HashMap<String, Predicate> {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Overriding get method.
     * 
     * @param key Predicate key (String)
     * @return Predicate object associated with given key (null if none found)
     */
    public Predicate get(String key) {
        return super.get(key.toLowerCase());
    }

    /**
     * Overriding put method.
     * 
     * @param key Predicate key (String)
     * @param value Predicate object to store
     * @return Predicate object associated with given key
     */
    @Override
    public Predicate put(String key, Predicate value) {
        return super.put(key.toLowerCase(), value);
    }
}
