/*
 * User
 * 
 * Version 1.0
 * 
 * October 30, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #3.
 *
 */
package cscie97.asn4.squaredesk.renter;

import cscie97.asn4.squaredesk.provider.ContactInfo;
import cscie97.asn4.squaredesk.provider.Image;
import cscie97.asn4.squaredesk.provider.Provider;
import java.net.URI;
import java.util.*;

/**
 * Class allowing for description of a SquareDesk User. A SquareDesk User
 * may be an OfficeSpace Provider, a Renter, or both.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class User {

    /**
     * Class constant for User gender.
     */
    private static final String MALE = "M";
    
    /**
     * Class constant for User gender.
     */
    private static final String FEMALE = "F";
    
    /**
     * Unique identifier for User.
     */
    private UUID ID;
    
    /**
     * User's name.
     */
    private String name = new String();
    
    /**
     * ContactInfo object with User's contact info.
     */
    private HashMap<String, ContactInfo> contactInfoMap = new HashMap<String, ContactInfo>();
    
    /**
     * Image object with User's image and related information.
     */
    private Image picture;
    
    /**
     * User's PayPal account.
     */
    private String account = new String();
    
    /**
     * User's sex.
     */
    private String gender = new String();
    
    /**
     * User's Provider object (null if User is not a Provider).
     */
    Provider providerProfile;
    
    /**
     * User's Renter object (null if User is not a Renter).
     */
    Renter renterProfile;
    
    /**
     * No-argument constructor.
     */
    public User() {
        ID = UUID.randomUUID();
    }

    /**
     * Constructor requiring all parameters.
     *
     * @param name User's name.
     * @param contactInfo ContactInfo object with User's contact info.
     * @param picture Image object with User's image and related info.
     * @param account User's PayPal account.
     */
    public User(String name, ContactInfo contactInfo, Image picture, String account) {
        this.name = name;
        this.contactInfoMap.put(contactInfo.getContactMethod(), contactInfo);
        this.picture = picture;
        this.account = account;
    }

    /**
     * Returns unique identifier for User.
     *
     * @return User's unique ID.
     */
    public UUID getID() {
        return ID;
    }

    /**
     * Returns User's name.
     *
     * @return User's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets User's name to given value.
     *
     * @param name User's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns User's account information.
     *
     * @return User's PayPal account.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Sets User's account to given value.
     *
     * @param account User's PayPal account.
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Returns list of contact information available for User.
     * 
     * @return List of contact information available for User.
     */
    public List<ContactInfo> getContactInfo() {
        return new ArrayList<ContactInfo>(contactInfoMap.values());
    }
    
    /**
     * Adds a new item to the User's list of contact information.
     * 
     * @param newContactInfo ContactInfo object with new information.
     */
    public void addContactInfo(ContactInfo newContactInfo) {
        if (!contactInfoMap.containsKey(newContactInfo.getContactMethod())) {
            contactInfoMap.put(newContactInfo.getContactMethod(), newContactInfo);
        }
    }
    
    /**
     * Adds a new item to the User's list of contact information.
     * 
     * @param contactMethod Method of contact (e.g. "E-mail", "Phone),
     * @param contactInformation Associated information
     */
    public void addContactInfo(String contactMethod, String contactInformation) {
        if (!contactInfoMap.containsKey(contactMethod)) {
            contactInfoMap.put(contactMethod, new ContactInfo(contactMethod, contactInformation));
        }
    }
    
    /**
     * Adds a new item to the User's list of contact information, given only
     * the item of contact information (default contact method will be saved),
     * 
     * @param contactInformation Contact information
     */
    public void addContactInfo(String contactInformation) {
        String contactMethod = "Contact method " + (contactInfoMap.size() + 1);
        contactInfoMap.put(contactMethod, new ContactInfo(contactMethod, contactInformation));
    }
    
    /**
     * Updates the contact information associated with a given method of contact.
     * 
     * @param contactMethod Queried method of contact.
     * @param contactInformation Updated information.
     */
    public void updateContactInfo(String contactMethod, String contactInformation) {
        if (contactInfoMap.containsKey(contactMethod)) {
            contactInfoMap.get(contactMethod).setContactInformation(contactInformation);
        }
    }

    /**
     * Removes the method of contact specified, as well as the entire associated
     * ContactInfo object, from the User's list of contact information.
     * 
     * @param contactMethod Method of contact to be removed.
     */
    public void removeContactInfo(String contactMethod) {
        if (contactInfoMap.containsKey(contactMethod)) {
            contactInfoMap.remove(contactMethod);
        }
    }

    /**
     * Returns number of contact methods User currently has available.
     * 
     * @return Number of User's contact methods/information.
     */
    public int getContactInfoCount() {
        return contactInfoMap.size();
    }
    
    /**
     * Returns Image object with User's image and related information.
     *
     * @return Image object for User.
     */
    public Image getPicture() {
        return picture;
    }

    /**
     * Sets User's Image object to Image object input.
     *
     * @param picture Image object for User.
     */
    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public String getGender() {
        return gender;
    }
    
    public void setGenderToMale() {
        gender = MALE;
    }
    
    public void setGenderToFemale() {
        gender = FEMALE;
    }

    /**
     * Returns User's Provider profile (null if User is not a Provider).
     *
     * @return Provider object for User.
     */
    public Provider getProvider() {
        return providerProfile;
    }

    /**
     * Updates User's Provider profile to given Provider object.
     *
     * @param provider Provider object for User.
     */
    public void setProvider(Provider provider) {
        this.providerProfile = provider;
    }
    
    /**
     * Clears User's Provider profile.
     */
    public void clearProvider() {
        this.providerProfile = null;
    }
    
    /**
     * Returns User's Renter profile (null if User is not a Renter).
     *
     * @return Renter object for User.
     */
    public Renter getRenter() {
        return renterProfile;
    }

    /**
     * Updates User's Renter profile to given Renter object.
     *
     * @param provider Provider object for User.
     */
    public void setRenter(Renter renter) {
        this.renterProfile = renter;
    }
    
    /**
     * Clears User's Renter profile.
     */
    public void clearRenter() {
        this.renterProfile = null;
    }
}
