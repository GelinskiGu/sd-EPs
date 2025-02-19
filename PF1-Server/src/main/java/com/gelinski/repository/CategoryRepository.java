package com.gelinski.repository;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.request.category.Category;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CategoryRepository {
    private final Connection conn;

    public void createCategory(String name, String description) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO category (name, description) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, description);

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }

    public Optional<Category> getCategoryById(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM category WHERE id = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category category = new Category();
                category.setId(Long.toString(rs.getLong("id")));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                return Optional.of(category);
            }
        } finally {
            DatabaseConfig.disconnect();
        }

        return Optional.empty();
    }

    public List<Category> getCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM category")) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setId(Long.valueOf(rs.getLong("id")).toString());
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
        } finally {
            DatabaseConfig.disconnect();
        }

        return categories;
    }

    public void updateCategory(String name, String description, Long id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE category SET name = ?, description = ? WHERE id = ?")) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setLong(3, id);

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }

    public void deleteCategory(Long id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM category WHERE id = ?")) {
            ps.setLong(1, id);

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }
}
