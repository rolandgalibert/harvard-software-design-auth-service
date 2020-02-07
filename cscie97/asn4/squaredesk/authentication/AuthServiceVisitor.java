/*
 * AuthServiceVisitor
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
 * This interface implements a visitor design pattern and defines the methods
 * that must be implemented by any class that needs to visit and process
 * the entire set of AuthService objects.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public interface AuthServiceVisitor {

    /**
     * Method to be defined for visiting an AuthService object.
     * 
     * @param authService AuthService object being visited.
     */
    public void visitAuthService(AuthService authService);
    
    /**
     * Method to be defined for visiting an AuthService Service object.
     * 
     * @param authService Service object being visited.
     */
    public void visitService(Service service);
    
    /**
     * Method to be defined for visiting an AuthService Permission object.
     * 
     * @param permission Permission object being visited.
     */
    public void visitPermission(Permission permission);
    
    /**
     * Method to be defined for visiting an AuthService Role object.
     * 
     * @param role Role object being visited.
     */
    public void visitRole(Role role);
    
    /**
     * Method to be defined for visiting an AuthService User object.
     * 
     * @param user User object being visited.
     */
    public void visitUser(User user);
    
    /**
     * Method to be defined for visiting an AuthService Credential object.
     * 
     * @param credential Credential object being visited.
     */
    public void visitCredential(Credential credential);
    
    /**
     * Method to be defined for visiting an AuthService AccessToken object.
     * 
     * @param accessToken AccessToken object being visited.
     */
    public void visitAccessToken(AccessToken accessToken);
}
