package com.vms.dao;

import com.vms.db.DBConnection;
import com.vms.models.User;
import com.vms.models.Admin;
import com.vms.models.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO class for performing CRUD operations on the user table.
 */
public class UserDAO {

    public User login(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    if ("ADMIN".equals(role)) {
                        return new Admin(rs.getInt("userId"), rs.getString("username"), rs.getString("password"));
                    } else {
                        return new Staff(rs.getInt("userId"), rs.getString("username"), rs.getString("password"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Login error: " + e.getMessage());
        }
        return null;
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] User added successfully: " + user.getUsername());
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error adding user: " + e.getMessage());
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        Connection conn = DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String role = rs.getString("role");
                if ("ADMIN".equals(role)) {
                    users.add(new Admin(rs.getInt("userId"), rs.getString("username"), rs.getString("password")));
                } else {
                    users.add(new Staff(rs.getInt("userId"), rs.getString("username"), rs.getString("password")));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Error retrieving users: " + e.getMessage());
        }
        return users;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE user SET username = ?, password = ?, role = ? WHERE userId = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setInt(4, user.getUserId());

            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] User updated successfully: ID " + user.getUserId());
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error updating user: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM user WHERE userId = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            boolean success = pstmt.executeUpdate() > 0;
            if (success)
                System.out.println("[DAO] User deleted successfully: ID " + userId);
            return success;
        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting user: " + e.getMessage());
            return false;
        }
    }
}
