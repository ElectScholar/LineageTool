package com.lineagetool;

/**
 * Represents a person with a name and additional details.
 */
public class Person {

    private String name;
    private String[] details;

/**
* Default constructor.
*/
    public Person() {
    }

    /**
     * Parameterized constructor.
     *
     * @param name    the name of the person
     * @param details additional details about the person
     */
    public Person(String name, String[] details) {
        this.name = name;
        this.details = details;
    }

    /**
     * Gets the name of the person.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the person.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the details of the person.
     *
     * @return the details array
     */
    public String[] getDetails() {
        return details;
    }

    /**
     * Sets the details of the person.
     *
     * @param details the details to set
     */
    public void setDetails(String[] details) {
        this.details = details;
    }
}
