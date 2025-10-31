package org.example.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CD extends Media {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "cds.txt";

    private String artist;
    private String id;

    public CD(String title, String artist, String id) {
        super(title);
        this.artist = artist;
        this.id = id;
    }

    public String getArtist() { return artist;
    }
    public String getId() { return id;
    }

    @Override
    public int getLoanDays() {
        return 7;
    }
    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays * 20;
    }

    @Override
    public String toString() {
        return "CD: " + title + " by " + artist + " (ID: " + id + ")";
    }

    public static List<CD> loadCDsFromFile() {
        List<CD> cds = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return cds;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    cds.add(new CD(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading cds.txt: " + e.getMessage());
        }
        return cds;
    }

    public static void saveCDsToFile(List<CD> cds) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (CD c : cds) {
                writer.write(c.getTitle() + "|" + c.getArtist() + "|" + c.getId());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to cds.txt: " + e.getMessage());
        }
    }

    public static CD findCDByTitle(String title) {
        return loadCDsFromFile().stream()
                .filter(cd -> cd.getTitle().equalsIgnoreCase(title))
                .findFirst().orElse(null);
    }
}