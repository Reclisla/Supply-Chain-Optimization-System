package com.management;

import java.sql.*;

public class UserManagement {

    public boolean register(String username, String password, String role) throws Exception {
        try (Connection conn = DBConnectionManager.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();
            return true;
        }
    }

    public String login(String username, String password) throws Exception {
        try (Connection conn = DBConnectionManager.getConnection()) {
            String sql = "SELECT role FROM users WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            } else {
                return null;
            }
        }
    }
}
