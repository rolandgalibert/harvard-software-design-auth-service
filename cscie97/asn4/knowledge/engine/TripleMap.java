/*
 * TripleMap
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
 * <String, Triple> map which performs case-insensitive key lookups.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class TripleMap extends HashMap<String, Triple> {

    private static final long serialVersionUID = 1L;
    
    /**
     * Overriding get method.
     * 
     * @param key Node key (String)
     * @return Triple object associated with given key (null if none found)
     */
    public Triple get(String key) {
        return super.get(key.toLowerCase());
    }

    /**
     * Overriding put method.
     * 
     * @param key Triple key (String)
     * @param value Triple object to store
     * @return Triple object associated with given key
     */
    @Override
    public Triple put(String key, Triple value) {
        return super.put(key.toLowerCase(), value);
    }
}
