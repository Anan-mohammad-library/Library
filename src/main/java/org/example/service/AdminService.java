package org.example.service;

import org.example.domain.Admin;
public class AdminService {


    private final Admin admin = new Admin("admin", "1234");


    private boolean loggedIn = false;

    public boolean login(String username, String password) {
        boolean success = username.equals(admin.getUsername()) &&
                password.equals(admin.getPassword());
        loggedIn = success;
        return success;
    }


    public void logout() {
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
