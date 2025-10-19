package org.example.service;

import org.example.domain.Admin;

public class AdminService {

    private final Admin[] admins = {
            new Admin("anan", "1234"),
            new Admin("mohammad", "123"),

    };

    private boolean loggedIn = false;
    private Admin currentAdmin;

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
}
