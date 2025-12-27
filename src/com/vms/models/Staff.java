package com.vms.models;

/**
 * Staff user model.
 */
public class Staff extends User {
    public Staff(int userId, String username, String password) {
        super(userId, username, password, "STAFF");
    }
}
