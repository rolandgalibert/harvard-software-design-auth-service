/*
 * AuthServiceVisitorElement
 * 
 * Version 1.0
 * 
 * November 20, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #4.
 *
 */
package cscie97.asn4.squaredesk.authentication;

/**
 * This interface defines the methods to be implemented by any class that is
 * to be an element visited by a class implementing AuthServiceVisitor.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public interface AuthServiceVisitorElement {
    
    /**
     * Visitor pattern acceptVisitor method to be defined by the given
     * AuthServiceVisitorElement (visitable) class.
     * 
     * @param visitor AuthServiceVisitor object to be accepted.
     */
    public void acceptVisitor(AuthServiceVisitor visitor);
}
