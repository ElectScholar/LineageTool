package com.lineagetool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileRead {
    private final LineageService lineageService;
    private final List<String> rootNodes;
    private final Map<String, Integer> nameCounter;

    public FileRead(String filePath) {
        this.lineageService = new LineageService();
        this.rootNodes = new ArrayList<>();
        this.nameCounter = new HashMap<>();
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

        // Ensure unique names by adding a counter
        String uniqueName = createUniqueName(name);

        if (isRoot) {
            addRootNode(uniqueName, description);
        } else {
            String[] parents = parts.length > 5 ? parts[5].split(",") : new String[0];
            // Convert parent names to unique names
            String[] uniqueParents = convertToUniqueNames(parents);
            addPersonToLineage(uniqueName, description, uniqueParents);
        }
    }

    private String createUniqueName(String name) {
        nameCounter.put(name, nameCounter.getOrDefault(name, 0) + 1);
        int count = nameCounter.get(name);
        return count > 1 ? name + "_" + count : name;
    }

    private String[] convertToUniqueNames(String[] names) {
        if (names == null) return new String[0];
        
        String[] uniqueNames = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            String trimmedName = names[i].trim();
            if (!trimmedName.isEmpty()) {
                // Find the most recent count for this name
                int count = nameCounter.getOrDefault(trimmedName, 1);
                uniqueNames[i] = count > 1 ? trimmedName + "_" + count : trimmedName;
            }
        }
        return uniqueNames;
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