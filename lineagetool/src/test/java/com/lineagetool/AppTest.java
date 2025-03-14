package com.lineagetool;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for verifying the functionality of the {@code Node} class.
 */
public class AppTest {
    private static final Logger logger = Logger.getLogger(AppTest.class.getName());

    @Test
    public void NodeTests() {
        Person person = new Person("son", new String[]{"Description"});
        Person father = new Person("father", new String[]{"Description"});
        Node<Person> fatherNode = new Node<>(father);
        List<Node<Person>> parentList = new ArrayList<>();
        parentList.add(fatherNode);

        Node<Person> node = new Node<>(person, parentList);

        boolean flag = node.prev.get(0).val == father;
        logger.info("Node test: " + flag);
        assertTrue(flag);
    }

    @Test 
    public void LineageTest(){
        LineageService lineageList = new LineageService();
        boolean flag = lineageList.getFirst() != null;
        logger.info("Empty List test = " + flag);
        assertTrue(!flag);
    }

    @Test 
    public void add(){
        LineageService lineageList = new LineageService();
        Person test = new Person("Bartholomew", new String[]{"Description"});
        lineageList.addFirst(new Node<>(test));

        boolean flag = lineageList.getFirst() != null;
        
        if (flag) {
            logger.info("add first node = " + lineageList.getFirst().val.getName());
        } else {
            logger.warning("First node is null");
        }

        assertTrue(flag);
    }
}
