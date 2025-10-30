package org.example.service;

import org.example.domain.Admin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    private final String FILE_NAME = "admins.dat";
    private List<Admin> admins = new ArrayList<>();
    private boolean loggedIn = false;
    private Admin currentAdmin;

    public AdminService() {
        loadAdmins();


        if (admins.isEmpty()) {
            admins.add(new Admin("anan", "1234"));
            admins.add(new Admin("mohammad", "123"));
            saveAdmins(); // حفظهم فوراً
        }
    }

    public boolean login(String username, String password) {
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
        return admins;
    }

    public void addAdmin(String username, String password) {
        admins.add(new Admin(username, password));
        saveAdmins();
    }


    private void saveAdmins() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(admins);
        } catch (IOException e) {
            System.out.println("Error saving admins: " + e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    private void loadAdmins() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                admins = (List<Admin>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading admins: " + e.getMessage());
            }
        }
    }
}
