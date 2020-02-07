/*
 * AuthServiceInventory
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

import java.util.Iterator;
import java.util.Set;

/**
 * This class implements AuthServiceVisitor and provides methods to create a
 * complete inventory of all AuthService objects in use. This class is based on
 * the visitor software design pattern.
 *
 * The class maintains a StringBuilder object called inventoryDescription,
 * in a YAML-like format, to which it adds descriptions of each AuthService 
 * object it visits. Another class-wide StringBuilder object called indent
 * provides for proper indentation of each line in the description
 * 
 * @author Roland L. Galibert
 * @version 1.0
 */
public class AuthServiceInventory implements AuthServiceVisitor {

    private StringBuilder inventoryDescription = new StringBuilder();
    private StringBuilder indent = new StringBuilder();
    private AuthServiceImpl authService;

    /**
     * Constructor
     */
    public AuthServiceInventory() {
    }
    
    /**
     * Visitor of the actual AuthService object; the method then visits each
     * Service, each Role and each User in the AuthService.
     * 
     * @param authService1 AuthService object.
     */
    @Override
    public void visitAuthService(AuthService authService1) {
        authService = (AuthServiceImpl) authService1;
        Set<String> keySet = authService.getServiceMap().keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Service service = authService.getServiceMap().get(key);
            visitService(service);
        }
        resetIndent();
        keySet = authService.getRoleMap().keySet();
        iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Role role = authService.getRoleMap().get(key);
            visitRole(role);
        }
        resetIndent();
        keySet = authService.getUserMap().keySet();
        iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            User user = authService.getUserMap().get(key);
            visitUser(user);
        }
    }

    /**
     * AuthService Service object visitor, prints out a description of the
     * service then visits each permission (restricted method) in the service.
     * 
     * @param service Service object being visited.
     */
    @Override
    public void visitService(Service service) {
        /*
         * Print particulars of this service
         */
        inventoryDescription.append(indent.toString()).append("service:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("ID: ").append(service.getID()).append("\n");
        inventoryDescription.append(indent.toString()).append("name: ").append(service.getName()).append("\n");
        inventoryDescription.append(indent.toString()).append("description: ").append(service.getDescription()).append("\n");

        /*
         * Print particulars of this service's permissions
         */
        inventoryDescription.append(indent.toString()).append("permissions:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("-\n");
        for (int i = 0; i < service.getPermissions().size(); i++) {
            Permission permission = authService.getPermissionMap().get(service.getPermissions().get(i));
            visitPermission(permission);
        }
        decreaseIndent();
        decreaseIndent();
    }

    /**
     * AuthService Permission object visitor, prints out a description of the
     * permission then returns.
     * 
     * @param permission Permission object being visited.
     */
    @Override
    public void visitPermission(Permission permission) {
        inventoryDescription.append(indent.toString()).append("permission:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("ID: ").append(permission.getID()).append("\n");
        inventoryDescription.append(indent.toString()).append("name: ").append(permission.getName()).append("\n");
        inventoryDescription.append(indent.toString()).append("description: ").append(permission.getDescription()).append("\n");
        decreaseIndent();
    }

    /**
     * AuthService Role object visitor, prints out a description of the
     * role then visits each subrole in the Role and each permission in the
     * role.
     * 
     * @param role Role object being visited.
     */
    public void visitRole(Role role) {

        /*
         * Print particulars of this role
         */
        inventoryDescription.append(indent.toString()).append("role:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("ID: ").append(role.getID()).append("\n");
        inventoryDescription.append(indent.toString()).append("name: ").append(role.getName()).append("\n");
        inventoryDescription.append(indent.toString()).append("description: ").append(role.getDescription()).append("\n");

        /*
         * Print particulars of this roles's subroles
         */
        inventoryDescription.append(indent.toString()).append("subroles:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("-\n");
        for (int i = 0; i < role.getSubroles().size(); i++) {
            Role subrole = authService.getRoleMap().get(role.getSubroles().get(i));
            visitRole(subrole);
        }
        decreaseIndent();

        /*
         * Print particulars of this roles's permissions
         */
        inventoryDescription.append(indent.toString()).append("permissions:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("-\n");
        for (int i = 0; i < role.getPermissions().size(); i++) {
            Permission permission = authService.getPermissionMap().get(role.getPermissions().get(i));
            visitPermission(permission);
        }
        decreaseIndent();
        decreaseIndent();
    }

    /**
     * AuthService User object visitor, prints out a description of the
     * user then visits each credential, each role, each permission and each
     * access token owned by the user.
     * 
     * @param user User object being visited.
     */
    @Override
    public void visitUser(User user) {

        /*
         * Print particulars of this user
         */
        inventoryDescription.append(indent.toString()).append("user:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("name: ").append(user.getUserName()).append("\n");

        /*
         * Print user's credentials
         */
        inventoryDescription.append(indent.toString()).append("credentials:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("-\n");
        for (int i = 0; i < user.getCredentials().size(); i++) {
            visitCredential(user.getCredentials().get(i));
        }
        decreaseIndent();

        /*
         * Print particulars of this user's roles
         */
        inventoryDescription.append(indent.toString()).append("roles:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("-\n");
        for (int i = 0; i < user.getRoles().size(); i++) {
            Role role = authService.getRoleMap().get(user.getRoles().get(i));
            visitRole(role);
        }
        decreaseIndent();

        /*
         * Print particulars of this user's permissions
         */
        inventoryDescription.append(indent.toString()).append("permissions:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("-\n");
        for (int i = 0; i < user.getPermissions().size(); i++) {
            Permission permission = authService.getPermissionMap().get(user.getPermissions().get(i));
            visitPermission(permission);
        }
        decreaseIndent();

        /*
         * Print this user's access tokens
         */
        inventoryDescription.append(indent.toString()).append("accessTokens:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("-\n");
        for (int i = 0; i < user.getAccessTokens().size(); i++) {
            visitAccessToken(user.getAccessTokens().get(i));
        }
        decreaseIndent();
        decreaseIndent();
    }

    /**
     * AuthService Credential object visitor, prints out a description of the
     * Credential object then returns.
     * 
     * @param credential Credential object being visited.
     */
    @Override
    public void visitCredential(Credential credential) {

        /*
         * Print credential particulars
         */
        inventoryDescription.append(indent.toString()).append("credential:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("loginID: ").append(credential.getLoginID()).append("\n");
        inventoryDescription.append(indent.toString()).append("passwordMessageDigest: ").append(credential.getPasswordMessageDigest()).append("\n");
        decreaseIndent();
    }

    /**
     * AuthService AccessToken object visitor, prints out a description of the
     * AccessToken object then returns.
     * 
     * @param accessToken AccessToken object being visited.
     */
    @Override
    public void visitAccessToken(AccessToken accessToken) {

        /*
         * Print accessToken particulars
         */
        inventoryDescription.append(indent.toString()).append("accessToken:\n");
        increaseIndent();
        inventoryDescription.append(indent.toString()).append("ID: ").append(accessToken.getID().toString()).append("\n");
        inventoryDescription.append(indent.toString()).append("state: ").append(accessToken.getState()).append("\n");
        inventoryDescription.append(indent.toString()).append("lastAccessTime: ").append(accessToken.getLastAccessTime().toString()).append("\n");
        decreaseIndent();
    }

    /**
     * Returns the current inventoryDescription string.
     * 
     * @return inventoryDescription string.
     */
    public String getInventoryDescription() {
        return inventoryDescription.toString();
    }

    /**
     * Clears the inventoryDescription string.
     */
    public void clearInventoryDescription() {
        inventoryDescription = new StringBuilder();
    }
    
    /*
     * Resets (clears) the indent string.
     */
    public void resetIndent() {
        indent = new StringBuilder();        
    }

    /*
     * Increases the indent of (adds a tab character to) the indent string.
     */
    private void increaseIndent() {
        indent.append("\t");
    }

    /*
     * Decreases the indent of (removes a tab character from) the indent string.
     */
    private void decreaseIndent() {
        if (indent.length() > 0) {
            indent.deleteCharAt(0);
        }
    }
}
