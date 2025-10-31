package org.example.service;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class UserService {

    private static final String FILE = "users.txt";
    private final Set<String> users = new HashSet<>();

    public UserService() { load(); }

    public boolean exists(String username) { return users.contains(username.toLowerCase()); }

    public void ensureExists(String username) {
        String key = username.toLowerCase();
        if (users.add(key)) save();
    }

    public void register(String username) {
        ensureExists(username);
        System.out.println("User registered: " + username);
    }

    public void unregister(String username, AdminService adminService, LoanService loanService) {
        if (!adminService.isLoggedIn()) {
            System.out.println("❌ Only admins can unregister users.");
            return;
        }

        loanService.refreshOverdues();
        boolean blocked = loanService.getLoansByBorrower(username).stream()
                .anyMatch(l -> !l.isReturned() || l.getFine() > 0);
        if (blocked) {
            System.out.println("❌ Cannot unregister: user has active loans or unpaid fines.");
            return;
        }

        if (users.remove(username.toLowerCase())) {
            save();
            System.out.println("✅ User unregistered: " + username);
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
                if (!line.isBlank()) users.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (String u : users) {
                bw.write(u);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}
