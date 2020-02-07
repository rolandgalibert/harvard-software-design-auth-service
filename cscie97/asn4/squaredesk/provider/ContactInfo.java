/*
 * ContactInfo
 * 
 * Version 1.0
 * 
 * Oct 4, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #2.
 *
 */
package cscie97.asn4.squaredesk.provider;

/**
 * Class allowing for specification of contact information for a given Provider
 * object.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class ContactInfo {

    /**
     * Specific method of contact (e.g. "E-mail", "Home phone")
     */
    private String contactMethod;

    /**
     * Information associated with specified method of contact
     * (e.g. "joe.smith@gmail.com", "617-555-1234")
     */
    private String contactInformation;

    /**
     * Constructor, uses flyweight pattern (ContactMethodFactory) to conserve
     * system space.
     * 
     * @param contactMethod Method of contact.
     * @param contactInformation Information associated with this contact method.
     */
    public ContactInfo(String contactMethod, String contactInformation) {
        this.contactMethod = ContactMethodFactory.lookup(contactMethod);
        this.contactInformation = contactInformation;
    }

    /**
     * Returns contact method associated with this ContactInfo object.
     * 
     * @return Contact method.
     */
    public String getContactMethod() {
        return contactMethod;
    }

    /**
     * Sets contact method associated with this ContactInfo object.
     * 
     * @param contactMethod Contact method.
     */
    public void setContactMethod(String contactMethod) {
        this.contactMethod = ContactMethodFactory.lookup(contactMethod);
    }

    /**
     * Returns specific contact information in this ContactInfo object.
     * 
     * @return Contact information.
     */
    public String getContactInformation() {
        return contactInformation;
    }

    /**
     * Updates specific contact information in this ContactInfo object.
     * 
     * @param contactInformation Contact information.
     */
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}
