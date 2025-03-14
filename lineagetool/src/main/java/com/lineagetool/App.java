package com.lineagetool;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        LineageService israelLineageService = new LineageService();
        String[] defaultStringDescription = {"Patriarch of Israel", "Lifespan: ", "Most of the stories found in Genesis", "Father of Israel, given new name by God meaning 'to struggle with God'"};
        Person jacobIsrael = new Person("Jacob | Israel", defaultStringDescription);
        israelLineageService.addFirst(new Node<>(jacobIsrael));
        defaultStringDescription = new String[] {"Patriarch of Reuben Tribe", "Lifespan: ", "Most of stories found in Genesis, son of Jacob/Israel eldest son of 12"};
        Person reuben = new Person("Reuben", defaultStringDescription);
        israelLineageService.addLast(new Node<>(reuben));
        defaultStringDescription = new String[] {"Reubenite", "Lifespan: ", "Most of stories found in Genesis, this is a test node"};
        Person sonOfReuben = new Person("Son of Reuben", defaultStringDescription);
        israelLineageService.addLast(new Node<>(sonOfReuben));

        Person testSecondSonOfReuben = new Person("Second Son of Reuben", defaultStringDescription);
        israelLineageService.addChild(new Node<>(testSecondSonOfReuben), "Reuben");
        Node<Person> test = israelLineageService.getNode("Reuben");
        israelLineageService.printLineage(test);
    }
}
