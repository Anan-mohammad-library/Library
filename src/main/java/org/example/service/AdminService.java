package org.example.service;

import org.example.domain.Admin;

public class AdminService {
    private Admin admin = new Admin("admin", "1234");
    private boolean loggedIn = false;

    public boolean login(String username, String password) {
        if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
            loggedIn = true;
            return true;
        }
        return false;
    }

    public void logout() {
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
