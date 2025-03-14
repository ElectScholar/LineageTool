package com.lineagetool;
/**
 * LineagePopulate is a helper class in initializing lineages of desired characters.
 * @author ElectScholar
 */
public class LineagePopulate {
/**
 * defaultStringDescrtiption has fields in each index
 *  0: Tribe Origin, 1: Lifespan, 2: Scripture found on character
 */
    public LineagePopulate() {
        LineageService israelLineageService = new LineageService();
        String[] defaultStringDescription = {"Patriarch of Israel", "Lifespan: ", "Most of the stories found in Genesis", "Father of Israel, given new name by God meaning 'to struggle with God'"};
        Person jacobIsrael = new Person("Jacob | Israel", defaultStringDescription);
        israelLineageService.addFirst(new Node<Person>(jacobIsrael));
        defaultStringDescription = new String[] {"Patriarch of Reuben Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel eldest son of 12"};
        Person reuben = new Person("Reuben", defaultStringDescription);
        israelLineageService.addFirst(new Node<Person>(reuben));
    }
    
    
}
