package com.lineagetool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DEPRECATED: This class is no longer the recommended way to initialize lineages.
 * Please use {@link FileRead} instead which provides a more maintainable solution
 * through external text file configuration.
 * 
 * LineagePopulate was a helper class for initializing lineages of desired characters
 * directly in code. It has been replaced by the FileRead class which allows for
 * easier maintenance and modification of lineage data through text files.
 * 
 * @deprecated Use {@link FileRead} with a properly formatted text file instead.
 *             See lineage.txt for the expected format:
 *             [ROOT] Name|Title|Lifespan|Scripture|Description
 *             
 * @author ElectScholar
 * @see FileRead
 */
@Deprecated
public class LineagePopulate {
    private final LineageService israelLineageService;
    private final List<String> rootNodes;

    public LineagePopulate() {
        israelLineageService = new LineageService();
        rootNodes = new ArrayList<>();
        initializeRootNodes();
        initializeDescendants();
        initializeTestCases();
    }

    private void initializeRootNodes() {
        // Add root nodes to both service and list
        addRootNode("Isaac", new String[]{
            "Patriarch of Israel",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Father of Israel, given new name by God meaning 'to struggle with God'"
        });

        addRootNode("Adah", new String[]{
            "Wife of Esau",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Mother of Eliphaz, Canaanite wife of Esau"
        });

        addRootNode("Basemath", new String[]{
            "Wife of Esau",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Mother of Reuel, daughter of Elon the Hittite"
        });

        addRootNode("Oholibamah", new String[]{
            "Wife of Esau",
            "Lifespan: ",
            "Genesis 36:2-3",
            "Mother of Jeush, Jalam, Korah. Daughter of Anah daughter of Zibeon the Hivite"
        });

        addRootNode("Timna", new String[]{
            "Concubine of Eliphaz",
            "Lifespan: ",
            "Genesis 36:12",
            "Concubine of Eliphaz, sister of Lotan"
        });

        addRootNode("Leah", new String[]{
            "Wife of Jacob",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Mother of Reuben, Simeon, Levi, Judah, Issachar, Zebulun, Dinah"
        });

        addRootNode("Bilhah", new String[]{
            "Concubine of Jacob",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Mother of Dan, Naphtali"
        });

        addRootNode("Zilpah", new String[]{
            "Concubine of Jacob",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Mother of Gad, Asher"
        });

        addRootNode("Rachel", new String[]{
            "Wife of Jacob",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Mother of Joseph, Benjamin"
        });

        addRootNode("Shua's Daughter", new String[]{
            "Wife of Judah",
            "Lifespan: ",
            "Genesis 38:2",
            "Mother of Er, Onan, Shelah"
        });

        addRootNode("Tamar", new String[]{
            "Wife of Er",
            "Lifespan: ",
            "Genesis 38:6-30",
            "Mother of Perez, Zerah, daughter-in-law of Judah. \n Tamar was married to Er and later married again to his brother Onan after Er died (this is similar to a practice called 'redemption' explained in Leviticus). \n For the full story, read Genesis 38."
        });

        addRootNode("Asenath", new String[]{
            "Wife of Joseph",
            "Lifespan: ",
            "Genesis 41:45",
            "Mother of Manasseh, Ephraim, daughter of Potiphera priest of On"
        });
    }

    private void addRootNode(String name, String[] description) {
        rootNodes.add(name);
        addPersonToLineage(name, description, (String[])null);
    }

    public List<String> getRootNodes() {
        return Collections.unmodifiableList(rootNodes);
    }

    private void initializeDescendants() {
        // Add Esau (Edomites)
        addPersonToLineage("Esau", new String[]{
            "Father of Edomites",
            "Lifespan: ",
            "Genesis 25-36",
            "Most of the stories found in Genesis",
            "Father of Edomites, gave away birthright to Jacob for a bowl of soup"
        }, "Isaac");

        // Add Esau's children
        addPersonToLineage("Eliphaz", new String[]{
            "Father of Temanites",
            "Lifespan: ",
            "Genesis 36:4-12",
            "Most of the stories found in Genesis",
            "Father of Temanites, son of Esau"
        }, "Esau", "Adah");

        // Add Eliphaz's children

        // Add Amalek (Father of Amalekites)
        addPersonToLineage("Amalek", new String[]{
            "Father of Amalekites",
            "Lifespan: ",
            "Genesis 36:12",
            "Father of Amalekites, son of Eliphaz"
        }, "Eliphaz", "Timna");

        // Add Teman (Father of Temanites)
        addPersonToLineage("Teman", new String[]{
            "Father of Temanites",
            "Lifespan: ",
            "Genesis 36:11",
            "Father of Temanites, son of Eliphaz"
        }, "Eliphaz");

        // Add Omar (Father of Omarites)
        addPersonToLineage("Omar", new String[]{
            "Father of Omarites",
            "Lifespan: ",
            "Genesis 36:11",
            "Father of Omarites, son of Eliphaz"
        }, "Eliphaz");

        // Add Zepho (Father of Zephoites)
        addPersonToLineage("Zepho", new String[]{
            "Father of Zephoites",
            "Lifespan: ",
            "Genesis 36:11",
            "Father of Zephoites, son of Eliphaz"
        }, "Eliphaz");

        // Add Gatam (Father of Gatamites)
        addPersonToLineage("Gatam", new String[]{
            "Father of Gatamites",
            "Lifespan: ",
            "Genesis 36:11",
            "Father of Gatamites, son of Eliphaz"
        }, "Eliphaz");

        // Add Kenaz (Father of Kenazites)
        addPersonToLineage("Kenaz", new String[]{
            "Father of Kenazites",
            "Lifespan: ",
            "Genesis 36:11",
            "Father of Kenazites, son of Eliphaz"
        }, "Eliphaz");

    
        //Add Reuel (Father of Reuelites)
        addPersonToLineage("Reuel", new String[]{
            "Father of Reuelites",
            "Lifespan: ",
            "Most of the stories found in Genesis",
            "Father of Reuelites, son of Esau"
        }, "Esau", "Basemath");

        // Add Reuel's children

        // Add Nahath (Father of Nahathites)
        addPersonToLineage("Nahath", new String[]{
            "Father of Nahathites",
            "Lifespan: ",
            "Genesis 36:13",
            "Father of Nahathites, son of Reuel"
        }, "Reuel");

        // Add Zerah (Father of Zerahites)
        addPersonToLineage("Zerah", new String[]{
            "Father of Zerahites",
            "Lifespan: ",
            "Genesis 36:13",
            "Father of Zerahites, son of Reuel"
        }, "Reuel");

        // Add Shammah (Father of Shammahites)
        addPersonToLineage("Shammah", new String[]{
            "Father of Shammahites",
            "Lifespan: ",
            "Genesis 36:13",
            "Father of Shammahites, son of Reuel"
        }, "Reuel");

        // Add Mizzah (Father of Mizzahites)
        addPersonToLineage("Mizzah", new String[]{
            "Father of Mizzahites",
            "Lifespan: ",
            "Genesis 36:13",
            "Father of Mizzahites, son of Reuel"
        }, "Reuel");

        
        // Add Jeush (Father of Jeushites)
        addPersonToLineage("Jeush", new String[]{
            "Father of Jeushites",
            "Lifespan: ",
            "Genesis 36:5",
            "Father of Jeushites, son of Esa and Obolibamah"
        }, "Esau", "Oholibamah");

        // Add Jalam (Father of Jalmites)
        addPersonToLineage("Jalam", new String[]{
            "Father of Jalmites",
            "Lifespan: ",
            "Genesis 36:5",
            "Father of Jalmites, son of Esa and Obolibamah"
        }, "Esau", "Oholibamah");

        // Add Korah (Father of Korahites)
        addPersonToLineage("Korah", new String[]{
            "Father of Korahites",
            "Lifespan: ",
            "Genesis 36:5",
            "Father of Korahites, son of Esa and Obolibamah"
        }, "Esau", "Oholibamah");
        
        // Add Jacob (Israel Patriarch) 
        addPersonToLineage("Jacob", new String[]{
            "Patriarch of Israel",
            "Lifespan: ",
            "Genesis 25-50",
            "Most of the stories found in Genesis",
            "Father of Israel, given new name by God meaning 'to struggle with God'"
        }, "Isaac");

        // Add Jacob's children
        addPersonToLineage("Reuben", new String[]{
            "Patriarch of Reuben Tribe",
            "Genesis 29-50",
            "Lifespan: ",
            "Most of stories found in Genesis, son of Jacob/Israel eldest son of 12"
        }, "Jacob", "Leah");

        //Add Reuben's children

        addPersonToLineage("Hanoch", new String[]{
            "Father of Hanochites",
            "Lifespan: ",
            "Genesis 46:9",
            "Father of Hanochites, son of Reuben"
        }, "Reuben");

        addPersonToLineage("Pallu", new String[]{
            "Father of Palluites",
            "Lifespan: ",
            "Genesis 46:9",
            "Father of Palluites, son of Reuben"
        }, "Reuben");

        addPersonToLineage("Hezron", new String[]{
            "Father of Hezronites",
            "Lifespan: ",
            "Genesis 46:9",
            "Father of Hezronites, son of Reuben"
        }, "Reuben");

        addPersonToLineage("Carmi", new String[]{
            "Father of Carmites",
            "Lifespan: ",
            "Genesis 46:9",
            "Father of Carmites, son of Reuben"
        }, "Reuben");

        addPersonToLineage("Simeon", new String[]{
            "Patriarch of Simeon Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel second born son of 12"
        }, "Jacob", "Leah");

        // Add Simeon's children
        addPersonToLineage("Jemuel", new String[]{
            "Father of Jemuelites",
            "Lifespan: ",
            "Genesis 46:10",
            "Father of Jemuelites, son of Simeon"
        }, "Simeon");
        
        addPersonToLineage("Jamin", new String[]{
            "Father of Jaminites",
            "Lifespan: ",
            "Genesis 46:10",
            "Father of Jaminites, son of Simeon"
        }, "Simeon");

        addPersonToLineage("Ohad", new String[]{
            "Father of Ohadites",
            "Lifespan: ",
            "Genesis 46:10",
            "Father of Ohadites, son of Simeon"
        }, "Simeon");

        addPersonToLineage("Jachin", new String[]{
            "Father of Jachinites",
            "Lifespan: ",
            "Genesis 46:10",
            "Father of Jachinites, son of Simeon"
        }, "Simeon");

        addPersonToLineage("Zohar", new String[]{
            "Father of Zoharites",
            "Lifespan: ",
            "Genesis 46:10",
            "Father of Zoharites, son of Simeon"
        }, "Simeon");

        addPersonToLineage("Shaul", new String[]{
            "Father of Shaulites",
            "Lifespan: ",
            "Genesis 46:10",
            "Father of Shaulites, son of Simeon (Seems to be a note about Shaul possibly not having same mother as siblins see Genesis 46:10)"
        }, "Simeon");

        addPersonToLineage("Levi", new String[]{
            "Patriarch of Levi Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel third born son of 12"
        }, "Jacob", "Leah");

        // Add Levi's children
        addPersonToLineage("Gershon", new String[]{
            "Father of Gershonites",
            "Lifespan: ",
            "Genesis 46:11",
            "Father of Gershonites, son of Levi"
        }, "Levi");

        addPersonToLineage("Kohath", new String[]{
            "Father of Kohathites",
            "Lifespan: ",
            "Genesis 46:11",
            "Father of Kohathites, son of Levi"
        }, "Levi");

        addPersonToLineage("Merari", new String[]{
            "Father of Merarites",
            "Lifespan: ",
            "Genesis 46:11",
            "Father of Merarites, son of Levi"
        }, "Levi");

        addPersonToLineage("Judah", new String[]{
            "Patriarch of Judah Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob"
        }, "Jacob", "Leah");

        // Add Judah's children
        addPersonToLineage("Er", new String[]{
            "Father of Erathites",
            "Lifespan: ",
            "Genesis 38:3",
            "Father of Erathites, son of Judah and Shua"
        }, "Judah", "Shua's Daughter");

        addPersonToLineage("Onan", new String[]{
            "Father of Onanites",
            "Lifespan: ",
            "Genesis 38:4",
            "Father of Onanites, son of Judah and Shua"
        }, "Judah", "Shua's Daughter");

        addPersonToLineage("Shelah", new String[]{
            "Father of Shelanites",
            "Lifespan: ",
            "Genesis 38:5",
            "Father of Shelanites, son of Judah and Shua"
        }, "Judah", "Shua's Daughter");

        addPersonToLineage("Perez", new String[]{
            "Father of Perezites",
            "Lifespan: ",
            "Genesis 38:29",
            "Father of Perezites, son of Judah and Tamar, Twin of Zerah, Line of Jesus"
        }, "Judah", "Tamar");

        // Add Perez's children
        addPersonToLineage("Hezron", new String[]{
            "Perezite",
            "Lifespan: ",
            "Genesis 46:12",
            "son of Perez"
        }, "Perez");

        addPersonToLineage("Hamul", new String[]{
            "Perezite",
            "Lifespan: ",
            "Genesis 46:12",
            "son of Perez"
        }, "Perez");

        addPersonToLineage("Zerah", new String[]{
            "Father of Zerahites",
            "Lifespan: ",
            "Genesis 38:30",
            "Father of Zerahites, son of Judah and Tamar, Twin of Perez"
        }, "Judah", "Tamar");

        addPersonToLineage("Dan", new String[]{
            "Patriarch of Dan Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel 4th born son of 12 given to him by Bilhah servant of Rachel"
        }, "Jacob", "Bilhah");

        //add Dan's children
        addPersonToLineage("Hushim", new String[]{
            "Danite",
            "Lifespan: ",
            "Genesis 46:23",
            "son of Dan"
        }, "Dan");

        addPersonToLineage("Naphtali", new String[]{
            "Patriarch of Naphtali Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel 5th born son of 12 second given by Bilhah servant of Rachel"
        }, "Jacob");

        //Add Naphtali's children
        addPersonToLineage("Jahzeel", new String[]{
            "Naphtalite",
            "Lifespan: ",
            "Genesis 46:24",
            "son of Naphtali"
        }, "Naphtali");

        addPersonToLineage("Guni", new String[]{
            "Naphtalite",
            "Lifespan: ",
            "Genesis 46:24",
            "son of Naphtali"
        }, "Naphtali");

        addPersonToLineage("Jezer", new String[]{
            "Naphtalite",
            "Lifespan: ",
            "Genesis 46:24",
            "son of Naphtali"
        }, "Naphtali");

        addPersonToLineage("Shillem", new String[]{
            "Naphtalite",
            "Lifespan: ",
            "Genesis 46:24",
            "son of Naphtali"
        }, "Naphtali");

        addPersonToLineage("Gad", new String[]{
            "Patriarch of Gad Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel 6th born son of 12 given by Zilpah servant of Leah"
        }, "Jacob");

        //Add Gad's children
        addPersonToLineage("Ziphion", new String[]{
            "Gadite",
            "Lifespan: ",
            "Genesis 46:16",
            "son of Gad"
        }, "Gad");

        addPersonToLineage("Haggi", new String[]{
            "Gadite",
            "Lifespan: ",
            "Genesis 46:16",
            "son of Gad"
        }, "Gad");

        addPersonToLineage("Shuni", new String[]{
            "Gadite",
            "Lifespan: ",
            "Genesis 46:16",
            "son of Gad"
        }, "Gad");

        addPersonToLineage("Ezbon", new String[]{
            "Gadite",
            "Lifespan: ",
            "Genesis 46:16",
            "son of Gad"
        }, "Gad");

        addPersonToLineage("Eri", new String[]{
            "Gadite",
            "Lifespan: ",
            "Genesis 46:16",
            "son of Gad"
        }, "Gad");

        addPersonToLineage("Arodi", new String[]{
            "Gadite",
            "Lifespan: ",
            "Genesis 46:16",
            "son of Gad"
        }, "Gad");

        addPersonToLineage("Areli", new String[]{
            "Gadite",
            "Lifespan: ",
            "Genesis 46:16",
            "son of Gad"
        }, "Gad");

        addPersonToLineage("Asher", new String[]{
            "Patriarch of Asher Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel 7th born son of 12 second given by Zilpah servant of Leah"
        }, "Jacob");

        //Add Asher's children
        addPersonToLineage("Imnah", new String[]{
            "Asherite",
            "Lifespan: ",
            "Genesis 46:17",
            "son of Asher"
        }, "Asher");

        addPersonToLineage("Ishvah", new String[]{
            "Asherite",
            "Lifespan: ",
            "Genesis 46:17",
            "son of Asher"
        }, "Asher");

        addPersonToLineage("Ishvi", new String[]{
            "Asherite",
            "Lifespan: ",
            "Genesis 46:17",
            "son of Asher"
        }, "Asher");

        addPersonToLineage("Beriah", new String[]{
            "Asherite",
            "Lifespan: ",
            "Genesis 46:17",
            "son of Asher"
        }, "Asher");

        // Add Beriah's children
        addPersonToLineage("Heber", new String[]{
            "Asherite",
            "Lifespan: ",
            "Genesis 46:17",
            "son of Beriah"
        }, "Beriah");

        addPersonToLineage("Malchiel", new String[]{
            "Asherite",
            "Lifespan: ",
            "Genesis 46:17",
            "son of Beriah"
        }, "Beriah");

        addPersonToLineage("Serah", new String[]{
            "Asherite",
            "Lifespan: ",
            "Genesis 46:17",
            "daughter of Asher"
        }, "Asher");

        addPersonToLineage("Issachar", new String[]{
            "Patriarch of Issachar Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel 8th born son of 12"
        }, "Jacob");

        //Add Issachar's children
        addPersonToLineage("Tola", new String[]{
            "Issacharite",
            "Lifespan: ",
            "Genesis 46:13",
            "son of Issachar"
        }, "Issachar");

        addPersonToLineage("Puvah", new String[]{
            "Issacharite",
            "Lifespan: ",
            "Genesis 46:13",
            "son of Issachar"
        }, "Issachar");

        addPersonToLineage("Yob", new String[]{
            "Issacharite",
            "Lifespan: ",
            "Genesis 46:13",
            "son of Issachar"
        }, "Issachar");

        addPersonToLineage("Shimron", new String[]{
            "Issacharite",
            "Lifespan: ",
            "Genesis 46:13",
            "son of Issachar"
        }, "Issachar");

        addPersonToLineage("Zebulun", new String[]{
            "Patriarch of Zebulun Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel 9th born son of 12"
        }, "Jacob");

        //Add Zebulun's children
        addPersonToLineage("Sered", new String[]{
            "Zebulunite",
            "Lifespan: ",
            "Genesis 46:14",
            "son of Zebulun"
        }, "Zebulun");

        addPersonToLineage("Elon", new String[]{
            "Zebulunite",
            "Lifespan: ",
            "Genesis 46:14",
            "son of Zebulun"
        }, "Zebulun");

        addPersonToLineage("Jahleel", new String[]{
            "Zebulunite",
            "Lifespan: ",
            "Genesis 46:14",
            "son of Zebulun"
        }, "Zebulun");

        addPersonToLineage("Dinah", new String[]{
            "Israelite (No Tribe)",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, daughter of Jacob/Israel"
        }, "Jacob");

        addPersonToLineage("Joseph", new String[]{
            "Israelite (No Tribe)",
            "Lifespan: 110",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel 10th born son of 12"
        }, "Jacob");

        //Add Joseph's children

        addPersonToLineage("Manasseh", new String[]{
            "Patriarch of Manasseh Tribe",
            "Lifespan: ",
            "Genesis 41:51",
            "Most of stories found in Genesis, son of Joseph"
        }, "Joseph", "Asenath");

        addPersonToLineage("Ephraim", new String[]{
            "Patriarch of Ephraim Tribe",
            "Lifespan: ",
            "Genesis 41:52",
            "Most of stories found in Genesis, son of Joseph"
        }, "Joseph", "Asenath");

        addPersonToLineage("Benjamin", new String[]{
            "Patriarch of Benjamin Tribe",
            "Lifespan: ",
            "Genesis 29-50",
            "Most of stories found in Genesis, son of Jacob/Israel 11th born son of 12"
        }, "Jacob");

        //Add Benjamin's children
        addPersonToLineage("Bela", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Becher", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Ashbel", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Gera", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Naaman", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Ehi", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Rosh", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Muppim", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Huppim", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

        addPersonToLineage("Ard", new String[]{
            "Benjaminite",
            "Lifespan: ",
            "Genesis 46:21",
            "son of Benjamin"
        }, "Benjamin");

    }

    private void initializeTestCases() {
        Node<Person> test = israelLineageService.getNode("Reuben");
        israelLineageService.printLineage(test);

        addPersonToLineage("Son of Reuben", new String[]{
            "Child of Reuben Tribe",
            "Lifespan: ",
            "No specific scripture reference"
        }, "Reuben");

        // Example test cases
        addPersonToLineage("Second Son of Reuben", new String[]{
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
