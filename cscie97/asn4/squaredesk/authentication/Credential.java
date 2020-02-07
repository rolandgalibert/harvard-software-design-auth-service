/*
 * Credential
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * This class provides properties and methods allowing for description of a
 * SquareDesk user's credentials to the system (user name and password).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Credential implements AuthServiceVisitorElement {

    /**
     * Login ID (immutable after initial creation).
     */
    private String loginID = new String();
    
    /**
     * Hashed password.
     */
    private String passwordMessageDigest = new String();
    
    /**
     * "Salt" for creating password message digest.
     */
    private byte[] salt = new byte[16];

    /**
     * Constructor
     * 
     * @param loginID User's login ID.
     * @param password User's password.
     */
    public Credential(String loginID, String password) {
        this.loginID = loginID;

        /*
         * Create salt
         */
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        /*
         * Create message digest
         */
        passwordMessageDigest = messageDigest(password);
    }

    /**
     * Returns login ID.
     * @return Login ID.
     */
    public String getLoginID() {
        return loginID;
    }

    /**
     * Returns hashed password.
     * 
     * @return Hashed password.
     */
    public String getPasswordMessageDigest() {
        return passwordMessageDigest;
    }

    /**
     * 
     * @param password
     */
    public void setPasswordMessageDigest(String password) {
        passwordMessageDigest = messageDigest(password);
    }
    
    /**
     * Returns true if message digest of input password matches the message
     * digest that was stored for the present password.
     * 
     * @param password Input password.
     * @return True if password is valid.
     */
    public boolean validPassword(String password) {
        String currentMessageDigest = messageDigest(password);
        if (currentMessageDigest.equals(passwordMessageDigest)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method for creating a message digest is based on the code presented in the article "How to
     * generate secure password hash : MD5, SHA, PBKDF2, BCrypt examples" by
     * Lokesh Gupta on the "How To Do It In Java" website.
     *
     * @param input String to hash.
     * @return Hash of input string.
     */
    private String messageDigest(String input) {
        String passwordHash = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt);
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            passwordHash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return passwordHash;
    }

    /**
     * Method to accept an AuthServiceVisitor object.
     * 
     * @param visitor AuthServiceVisitor object.
     */
    @Override
    public void acceptVisitor(AuthServiceVisitor visitor) {
        visitor.visitCredential(this);
    }
}
