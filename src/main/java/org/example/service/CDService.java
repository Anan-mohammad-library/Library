package org.example.service;

import org.example.domain.CD;
import java.io.*;
import java.util.*;

public class CDService {

    private static final String FILE = "cds.txt";
    private List<CD> cds;

    public CDService() {
        cds = load();
    }

    public void addCD(String title, String artist, String id) {
        cds.add(new CD(title, artist, id));
        save();
        System.out.println("✅ CD added successfully!");
    }

    public List<CD> search(String keyword) {
        String k = keyword.toLowerCase();
        List<CD> res = new ArrayList<>();
        for (CD c : cds) {
            if (c.getTitle().toLowerCase().contains(k) || c.getArtist().toLowerCase().contains(k) || c.getId().contains(keyword))
                res.add(c);
        }
        return res;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (CD c : cds) {
                bw.write(c.getTitle() + "|" + c.getArtist() + "|" + c.getId());
                bw.newLine();
            }
        } catch (Exception ignored){}
    }

    private List<CD> load() {
        List<CD> out = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return out;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length == 3) out.add(new CD(p[0], p[1], p[2]));
            }
        } catch (Exception ignored){}
        return out;
    }

    public boolean exists(String title) {
        return cds.stream().anyMatch(c -> c.getTitle().equalsIgnoreCase(title));
    }
    public List<CD> getAllCDs() {
        return new ArrayList<>(cds); // assuming 'cds' is your in-memory list
    }
}

