package org.example.service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final String FILE = "users.txt";
    private final Map<String, String> users = new HashMap<>();
    private final Map<String, String> emails = new HashMap<>();

    public UserService() {
        load();
    }

    public boolean register(String username, String password, String email) {
        String key = username.toLowerCase();
        if (users.containsKey(key)) return false;
        users.put(key, password);
        emails.put(key, email.toLowerCase());
        save();
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
        if (hasActiveOrFines) return;
        users.remove(key);
        emails.remove(key);
        save();
    }

    public String getEmail(String username) {
        return emails.get(username.toLowerCase());
    }

    private void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        users.put(parts[0].toLowerCase(), parts[1]);
                        emails.put(parts[0].toLowerCase(), parts[2].toLowerCase());
                    }
                }
            }
        } catch (IOException ignored) {}
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (String key : users.keySet()) {
                bw.write(key + "|" + users.get(key) + "|" + emails.get(key));
                bw.newLine();
            }
        } catch (IOException ignored) {}
    }


}
