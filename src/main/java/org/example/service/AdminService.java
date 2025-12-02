package org.example.service;

import org.example.domain.Admin;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.example.domain.Book.logger;

public class AdminService {

    private final String fileName;
    private List<Admin> admins = new ArrayList<>();
    private boolean loggedIn = false;
    private Admin currentAdmin;


    public AdminService() {
        this("admins.txt");
    }

    public AdminService(String fileName) {
        this.fileName = fileName;
        loadAdmins();


        if (admins.isEmpty()) {
            admins.add(new Admin("anan", "1234"));
            admins.add(new Admin("mohammad", "123"));
            saveAdmins();
        }
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) return false;

        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                loggedIn = true;
                currentAdmin = admin;
                return true;
            }
        }
        loggedIn = false;
        currentAdmin = null;
        return false;
    }


    public void logout() {
        loggedIn = false;
        currentAdmin = null;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public List<Admin> getAdmins() {
        return new ArrayList<>(admins);
    }



    public boolean addAdmin(String username, String password) {
        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;

        String hashedPassword = hashPassword(password);
        admins.add(new Admin(username, hashedPassword));
        saveAdmins();
        return true;
    }


    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Encryption error\n", e);
        }
    }


    public void removeAdmin(String username) {
        if (username == null) return;
        admins.removeIf(a -> a.getUsername().equals(username));
        saveAdmins();
    }


    private void saveAdmins() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Admin admin : admins) {
                writer.write(admin.getUsername() + "|" + admin.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            logger.severe("Error saving admins: " + e.getMessage());
        }
    }


    private void loadAdmins() {
        File file = new File(fileName);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            admins.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 2) {
                        admins.add(new Admin(parts[0], parts[1]));
                    }
                }
            }
        } catch (IOException e) {
            logger.severe("Error loading admins: " + e.getMessage());
        }
    }
}
