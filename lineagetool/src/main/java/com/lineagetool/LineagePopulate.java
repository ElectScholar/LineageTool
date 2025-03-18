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

    private final LineageService israelLineageService;
    
    public LineagePopulate() {
        //Originator of Israel Jacob
        israelLineageService = new LineageService();
        String[] defaultStringDescription = {"Patriarch of Israel", "Lifespan: ", "Most of the stories found in Genesis", "Father of Israel, given new name by God meaning 'to struggle with God'"};
        Person jacobIsrael = new Person("Jacob", defaultStringDescription);
        israelLineageService.addFirst(new Node<>(jacobIsrael));
        //used addChild() to add 12 sons of jacob
        defaultStringDescription = new String[] {"Patriarch of Reuben Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel eldest son of 12"};
        Person reuben = new Person("Reuben", defaultStringDescription);
        israelLineageService.addChild(new Node<>(reuben), "Jacob");
        //Simeon
        defaultStringDescription = new String[] {"Patriarch of Simeon Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel second born son of 12"};
        Person simeon = new Person("Simeon", defaultStringDescription);
        israelLineageService.addChild(new Node<>(simeon), "Jacob");
        //Levi
        defaultStringDescription = new String[] {"Patriarch of Levi Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel third born son of 12"};
        Person levi = new Person("Levi", defaultStringDescription);
        israelLineageService.addChild(new Node<>(levi), "Jacob");
        //Dan
        defaultStringDescription = new String[] {"Patriarch of Dan Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel 4th born son of 12 given to him by Bilhah servant of Rachel"};
        Person dan = new Person("Dan", defaultStringDescription);
        israelLineageService.addChild(new Node<>(dan), "Jacob");
        //Naphtali
        defaultStringDescription = new String[] {"Patriarch of Naphtali Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel 5th born son of 12 second given by Bilhah servant of Rachel"};
        Person naphtali = new Person("Naphtali", defaultStringDescription);
        israelLineageService.addChild(new Node<>(naphtali), "Jacob");
        //Gad
        defaultStringDescription = new String[] {"Patriarch of Gad Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel 6th born son of 12 given by Zilpah servant of Leah"};
        Person gad = new Person("Gad", defaultStringDescription);
        israelLineageService.addChild(new Node<>(gad), "Jacob");
        //Asher
        defaultStringDescription = new String[] {"Patriarch of Asher Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel 7th born son of 12 second given by Zilpah servant of Leah"};
        Person asher = new Person("Asher", defaultStringDescription);
        israelLineageService.addChild(new Node<>(asher), "Jacob");
        //Issachar
        defaultStringDescription = new String[] {"Patriarch of Issachar Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel 8th born son of 12"};
        Person issachar = new Person("Issachar", defaultStringDescription);
        israelLineageService.addChild(new Node<>(issachar), "Jacob");
        //Zebulun
        defaultStringDescription = new String[] {"Patriarch of Zebulun Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel 9th born son of 12"};
        Person zebulun = new Person("Zebulun", defaultStringDescription);
        israelLineageService.addChild(new Node<>(zebulun), "Jacob");
        //Dinah
        defaultStringDescription = new String[] {"Israelite (No Tribe)", "Lifespan: ", "Most of stories found in Genesis, daughter of Jacob/Israel"};
        Person dinah = new Person("Dinah", defaultStringDescription);
        israelLineageService.addChild(new Node<>(dinah), "Jacob");
        //Joseph
        defaultStringDescription = new String[] {"Israelite (No Tribe)", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel 10th born son of 12"};
        Person joseph = new Person("Joseph", defaultStringDescription);
        israelLineageService.addChild(new Node<>(joseph), "Jacob");
        //Benjamin
        defaultStringDescription = new String[] {"Patriarch of Benjamin Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel 11th born son of 12"};
        Person benjamin = new Person("Benjamin", defaultStringDescription);
        israelLineageService.addChild(new Node<>(benjamin), "Jacob");


        Person testSecondSonOfReuben = new Person("Second Son of Reuben", defaultStringDescription);
        israelLineageService.addChild(new Node<>(testSecondSonOfReuben), "Reuben");
        Node<Person> test = israelLineageService.getNode("Reuben");
        israelLineageService.printLineage(test);

        Person testSonOfReuben = new Person("Son of Reuben", defaultStringDescription);
        israelLineageService.addChild(new Node<>(testSonOfReuben), "Reuben");
        test = israelLineageService.getNode("Reuben");
        israelLineageService.printLineage(test);
    }
    public LineageService getLineageService() {
        return israelLineageService;
    }
    
    
}
