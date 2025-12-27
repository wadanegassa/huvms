package com.vms.models;

/**
 * Admin user model.
 */
public class Admin extends User {
    public Admin(int userId, String username, String password) {
        super(userId, username, password, "ADMIN");
    }
}
