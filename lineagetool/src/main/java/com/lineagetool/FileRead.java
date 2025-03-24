package com.lineagetool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileRead {
    private final LineageService lineageService;
    private final List<String> rootNodes;

    public FileRead(String filePath) {
        this.lineageService = new LineageService();
        this.rootNodes = new ArrayList<>();
        loadFromFile(filePath);
    }

    private void loadFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                processLine(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void processLine(String line) {
        boolean isRoot = line.startsWith("[ROOT]");
        String[] parts = line.replace("[ROOT]", "").trim().split("\\|");
        
        if (parts.length < 5) {
            System.err.println("Invalid line format: " + line);
            return;
        }

        String name = parts[0].trim();
        String[] description = {
            parts[1].trim(),  // Title
            parts[2].trim(),  // Lifespan
            parts[3].trim(),  // Scripture
            parts[4].trim()   // Description
        };

        if (isRoot) {
            addRootNode(name, description);
        } else {
            String[] parents = parts.length > 5 ? parts[5].split(",") : new String[0];
            addPersonToLineage(name, description, parents);
        }
    }

    private void addRootNode(String name, String[] description) {
        rootNodes.add(name);
        addPersonToLineage(name, description);
    }

    private void addPersonToLineage(String name, String[] description, String... parentNames) {
        Person person = new Person(name, description);
        Node<Person> node = new Node<>(person);
        
        if (parentNames == null || parentNames.length == 0) {
            lineageService.addFirst(node);
        } else {
            for (String parentName : parentNames) {
                if (parentName != null && !parentName.trim().isEmpty()) {
                    lineageService.addChild(node, parentName.trim());
                }
            }
        }
    }

    public LineageService getLineageService() {
        return lineageService;
    }

    public List<String> getRootNodes() {
        return rootNodes;
    }
}