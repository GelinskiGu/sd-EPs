package com.gelinski.repository;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.entity.Account;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class AccountRepository {
    private final Connection conn;

    public void create(String userId, String name, String password) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO user (user_id, name, password) VALUES (?, ?, ?)")) {
            ps.setString(1, userId);
            ps.setString(2, name);
            ps.setString(3, password);

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }

    public Account getByUserId(String userId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM user WHERE user_id = ?");
            ps.setString(1, userId);

            rs = ps.executeQuery();

            if (rs.next()) return new Account(rs.getLong("user_id"), rs.getString("name"), rs.getString("user"), rs.getString("password"), rs.getString("type_user"));

            return null;
        } finally {
            if(ps != null) ps.close();
            if(rs != null) rs.close();
            DatabaseConfig.disconnect();
        }
    }
}