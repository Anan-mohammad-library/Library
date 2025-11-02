package org.example.service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final String FILE = "users.txt";
    private final Map<String, String> users = new HashMap<>();

    public UserService() {
        load();
    }


    public boolean register(String username, String password) {
        String key = username.toLowerCase();
        if (users.containsKey(key)) {
            System.out.println(" User already exists!");
            return false;
        }
        users.put(key, password);
        save();
        System.out.println(" User registered: " + username);
        return true;
    }


    public boolean login(String username, String password) {
        String key = username.toLowerCase();
        return users.containsKey(key) && users.get(key).equals(password);
    }
    public void unregister(String username, LoanService loanService) {
        String key = username.toLowerCase();
        boolean hasActiveOrFines = loanService.getAllLoans().stream()
                .anyMatch(l -> l.getBorrower().equalsIgnoreCase(username) && (!l.isReturned() || l.getFine() > 0));

        if (hasActiveOrFines) {
            System.out.println("❌ Cannot unregister: User has active loans or unpaid fines.");
            return;
        }

        if (users.remove(key) != null) {
            save();
            System.out.println("✅ User removed: " + username);
        } else {
            System.out.println("ℹ User not found: " + username);
        }
    }
    private void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 2)
                        users.put(parts[0].toLowerCase(), parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                bw.write(entry.getKey() + "|" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}
