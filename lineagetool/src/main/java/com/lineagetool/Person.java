package com.lineagetool;

/**
 * Represents a person with a name and additional details.
 * This class is used to store information about individuals in the lineage.
 */
public class Person {

    private String name;
    private String[] details;

    /**
     * Default constructor.
     * Creates an empty person object with no name or details.
     */
    public Person() {
    }

    /**
     * Parameterized constructor.
     * Creates a person object with the specified name and details.
     *
     * @param name    The name of the person.
     * @param details An array of strings containing additional details about the person.
     *                Each string in the array represents a specific piece of information,
     *                such as their role, lifespan, or notable stories.
     */
    public Person(String name, String[] details) {
        this.name = name;
        this.details = details;
    }

    /**
     * Gets the name of the person.
     *
     * @return The name of the person as a string.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the person.
     *
     * @param name The name to set for the person. This should be a non-null, descriptive name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the details of the person.
     *
     * @return An array of strings containing additional details about the person.
     *         This may include information such as their role, lifespan, or notable stories.
     */
    public String[] getDetails() {
        return details;
    }

    /**
     * Sets the details of the person.
     *
     * @param details An array of strings containing additional details about the person.
     *                Each string should represent a specific piece of information.
     */
    public void setDetails(String[] details) {
        this.details = details;
    }
}
