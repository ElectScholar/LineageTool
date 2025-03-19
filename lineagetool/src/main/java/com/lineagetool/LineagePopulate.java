package com.lineagetool;
/**
 * LineagePopulate is a helper class in initializing lineages of desired characters.
 * @author ElectScholar
 */
public class LineagePopulate {

    private final LineageService israelLineageService;

    public LineagePopulate() {
        israelLineageService = new LineageService();

        // Add Jacob (Israel) as the root
        addPersonToLineage("Jacob", new String[]{
            "Patriarch of Israel",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Father of Israel, given new name by God meaning 'to struggle with God'"
        }, null);

        // Add Jacob's children
        addPersonToLineage("Reuben", new String[]{
            "Patriarch of Reuben Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel eldest son of 12"
        }, "Jacob");

        addPersonToLineage("Simeon", new String[]{
            "Patriarch of Simeon Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel second born son of 12"
        }, "Jacob");

        addPersonToLineage("Levi", new String[]{
            "Patriarch of Levi Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel third born son of 12"
        }, "Jacob");

        addPersonToLineage("Dan", new String[]{
            "Patriarch of Dan Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel 4th born son of 12 given to him by Bilhah servant of Rachel"
        }, "Jacob");

        addPersonToLineage("Naphtali", new String[]{
            "Patriarch of Naphtali Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel 5th born son of 12 second given by Bilhah servant of Rachel"
        }, "Jacob");

        addPersonToLineage("Gad", new String[]{
            "Patriarch of Gad Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel 6th born son of 12 given by Zilpah servant of Leah"
        }, "Jacob");

        addPersonToLineage("Asher", new String[]{
            "Patriarch of Asher Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel 7th born son of 12 second given by Zilpah servant of Leah"
        }, "Jacob");

        addPersonToLineage("Issachar", new String[]{
            "Patriarch of Issachar Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel 8th born son of 12"
        }, "Jacob");

        addPersonToLineage("Zebulun", new String[]{
            "Patriarch of Zebulun Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel 9th born son of 12"
        }, "Jacob");

        addPersonToLineage("Dinah", new String[]{
            "Israelite (No Tribe)",
            "Lifespan: ",
            "Most of stories found in Genesis, daughter of Jacob/Israel"
        }, "Jacob");

        addPersonToLineage("Joseph", new String[]{
            "Israelite (No Tribe)",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel 10th born son of 12"
        }, "Jacob");

        addPersonToLineage("Benjamin", new String[]{
            "Patriarch of Benjamin Tribe",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel 11th born son of 12"
        }, "Jacob");

        // Example test cases
        addPersonToLineage("Second Son of Reuben", new String[]{
            "Child of Reuben Tribe",
            "Lifespan: ",
            "No specific scripture reference"
        }, "Reuben");

        Node<Person> test = israelLineageService.getNode("Reuben");
        israelLineageService.printLineage(test);

        addPersonToLineage("Son of Reuben", new String[]{
            "Child of Reuben Tribe",
            "Lifespan: ",
            "No specific scripture reference"
        }, "Reuben");

        test = israelLineageService.getNode("Reuben");
        israelLineageService.printLineage(test);
    }

    /**
     * Helper method to add a person to the lineage.
     *
     * @param name        The name of the person.
     * @param description The description array for the person.
     * @param parentName  The name of the parent (null if this is the root).
     */
    private void addPersonToLineage(String name, String[] description, String parentName) {
        Person person = new Person(name, description);
        Node<Person> node = new Node<>(person);
        if (parentName == null) {
            israelLineageService.addFirst(node);
        } else {
            israelLineageService.addChild(node, parentName);
        }
    }

    public LineageService getLineageService() {
        return israelLineageService;
    }
}
