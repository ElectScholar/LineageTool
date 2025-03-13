package com.lineagetool;

import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for verifying the functionality of the {@code Node} class.
 */
public class AppTest {
    private static final Logger logger = Logger.getLogger(AppTest.class.getName());

    /**
     * Tests the {@code Node} class to ensure that the previous node 
     * correctly references its associated {@code Person} object.
     * <p>
     * This test:
     * <ul>
     *   <li>Creates a {@code Person} object named "son".</li>
     *   <li>Creates a {@code Person} object named "father".</li>
     *   <li>Creates a {@code Node} for "son" with a previous node containing "father".</li>
     *   <li>Verifies that the previous node's {@code Person} is correctly assigned.</li>
     * </ul>
     * The test logs the result and asserts that the expected condition holds true.
     */
    @Test
    public void NodeTests() {
        Person person = new Person("son", new String[]{"Description"});
        Person father = new Person("father", new String[]{"Description"});
        Node node = new Node(person, new Node(father));

        boolean flag = node.prev.val == father;
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
        lineageList.addFirst(new Node<Person>(test));
        boolean flag = lineageList.getFirst() != null;
        logger.info("add first node = " + lineageList.getFirst().val.getName());
        assertTrue(flag);
    }
}
