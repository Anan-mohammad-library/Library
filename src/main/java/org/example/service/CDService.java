package org.example.service;

import org.example.domain.CD;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CDService {
    private static final String FILE_PATH = "cds.txt";
    private List<CD> cds;

    public CDService() { cds = loadCDs(); }

    public void addCD(String title, String artist, String id) {
        cds.add(new CD(title, artist, id));
        saveCDs();
        System.out.println(" CD added successfully!");
    }

    public List<CD> search(String keyword) {
        String lower = keyword.toLowerCase();
        return cds.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(lower)
                        || c.getArtist().toLowerCase().contains(lower)
                        || c.getId().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<CD> getCDs() { return cds; }

    private void saveCDs() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (CD c : cds) {
                writer.write(c.getTitle() + "|" + c.getArtist() + "|" + c.getId());
                writer.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private List<CD> loadCDs() {
        List<CD> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3)
                    list.add(new CD(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }
}