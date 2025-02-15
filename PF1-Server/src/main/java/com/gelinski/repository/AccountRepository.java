package com.gelinski.repository;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.entity.Account;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountRepository {
    private final Connection conn;

    public void createAccount(String name, String username, String password) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO account (name, username, password) VALUES (?, ?, ?)")) {


            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }

    public Optional<Account> getByUser(String user) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM account WHERE username = ?");
            ps.setString(1, user);

            rs = ps.executeQuery();

            if (rs.next())
                return Optional.of(new Account(rs.getLong("id"), rs.getString("name"), rs.getString("username"), rs.getString("password"), rs.getString("type")));

            return Optional.empty();
        } finally {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
            DatabaseConfig.disconnect();
        }
    }
}