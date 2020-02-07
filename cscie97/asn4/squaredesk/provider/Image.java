/*
 * Image
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

import java.net.URI;
import java.util.UUID;

/**
 * This class allows for specification of an image for a Provider or OfficeSpace
 * (office space or facility) object, including related properties.
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Image {

    /*
     * Image ID, immutable value assigned by system.
     */
    private UUID ID;
    
    /*
     * Name of image.
     */
    private String name;
    
    /**
     * Description of image.
     */
    private String description;
    
    /**
     * Location where image is stored.
     */
    private URI uri;

    /**
     * Single constructor, requires all parameters.
     * 
     * @param name Name of Image object name.
     * @param description Image object description.
     * @param uri Location where actual image is stored.
     */
    public Image(String name, String description, URI uri) {
        this.ID = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.uri = uri;
    }

    /**
     * Returns unique identifier for this Image object.
     * 
     * @return Image object UUID.
     */
    public UUID getID() {
        return ID;
    }
    
    /**
     * Returns name of this Image object.
     * 
     * @return Image object name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of this Image object.
     * 
     * @param name Image object name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns description of this Image object.
     * 
     * @return Image object description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description for this Image object.
     * 
     * @param description Image object description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns URI where actual image is stored.
     * 
     * @return Image object URI.
     */
    public URI getURI() {
        return uri;
    }

    /**
     * Sets URI where actual image is stored.
     * 
     * @param uri Image object URI.
     */
    public void setURI(URI uri) {
        this.uri = uri;
    }
}
