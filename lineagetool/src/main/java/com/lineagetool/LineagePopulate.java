package com.lineagetool;
/**
 * LineagePopulate is a helper class in initializing lineages of desired characters.
 * @author ElectScholar
 */
public class LineagePopulate {

    private final LineageService israelLineageService;

    public LineagePopulate() {
        israelLineageService = new LineageService();

        // Add Isaac  as the root
        addPersonToLineage("Isaac", new String[]{
            "Patriarch of Israel",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Father of Israel, given new name by God meaning 'to struggle with God'"
        }, (String[])null);

        // Adah wife of Esau
        addPersonToLineage("Adah", new String[]{
            "Wife of Esau",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Mother of Eliphaz, Canaanite wife of Esau"
        }, (String[])null);

        // Add Basemath wife of Esau
        addPersonToLineage("Basemath", new String[]{
            "Wife of Esau",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Mother of Reuel, daughter of Elon the Hittite"
        }, (String[])null);

        // Add Esau (Edomites)
        addPersonToLineage("Esau", new String[]{
            "Father of Edomites",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Father of Edomites, gave away birthright to Jacob for a bowl of soup"
        }, "Isaac");

        // Add Esau's children
        addPersonToLineage("Eliphaz", new String[]{
            "Father of Temanites",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Father of Temanites, son of Esau"
        }, "Esau", "Adah");

        //Add Reuel (Father of Reuelites)
        addPersonToLineage("Reuel", new String[]{
            "Father of Reuelites",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Father of Reuelites, son of Esau"
        }, "Esau", "Basemath");

        // Add Jacob (Israel Patriarch) 
        addPersonToLineage("Jacob", new String[]{
            "Patriarch of Israel",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Father of Israel, given new name by God meaning 'to struggle with God'"
        }, "Isaac");

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
     * Helper method to add a person to the lineage with multiple possible parents.
     *
     * @param name         The name of the person.
     * @param description  The description array for the person.
     * @param parentNames  Array of parent names (null or empty array if this is a root).
     */
    private void addPersonToLineage(String name, String[] description, String... parentNames) {
        Person person = new Person(name, description);
        Node<Person> node = new Node<>(person);
        
        if (parentNames == null || parentNames.length == 0) {
            israelLineageService.addFirst(node);
        } else {
            // Add connection to each parent
            for (String parentName : parentNames) {
                if (parentName != null) {
                    israelLineageService.addChild(node, parentName);
                }
            }
        }
    }

    public LineageService getLineageService() {
        return israelLineageService;
    }
}
