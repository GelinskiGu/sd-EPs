package com.gelinski.repository;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.entity.AccountCategory;
import com.gelinski.entity.Announcement;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AnnouncementRepository {
    private final Connection conn;

    public void createAnnouncement(String title, String text, Integer categoryId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO announcement (title, text, date, categoryId) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, title);
            ps.setString(2, text);
            ps.setDate(3, Date.valueOf(LocalDate.now().toString()));
            ps.setInt(4, categoryId);

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }

    public void subscribeToCategory(String accountId, String categoryId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO account_category (accountId, categoryId) VALUES (?, ?)")) {
            ps.setInt(1, Integer.parseInt(accountId));
            ps.setInt(2, Integer.parseInt(categoryId));

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }

    public void unsubscribeToCategory(String accountId, String categoryId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM account_category WHERE accountId = ? AND categoryId = ?")) {
            ps.setInt(1, Integer.parseInt(accountId));
            ps.setInt(2, Integer.parseInt(categoryId));

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }

    public Optional<AccountCategory> getAccountCategory(String accountId, String categoryId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT account_category.* FROM account_category WHERE accountId = ? AND categoryId = ?")) {
            ps.setInt(1, Integer.parseInt(accountId));
            ps.setInt(2, Integer.parseInt(categoryId));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AccountCategory accountCategory = new AccountCategory();
                accountCategory.setAccountId(rs.getInt("accountId"));
                accountCategory.setCategoryId(rs.getInt("categoryId"));
                return Optional.of(accountCategory);
            }
        } finally {
            DatabaseConfig.disconnect();
        }
        return Optional.empty();
    }

    public Optional<Announcement> getAnnouncementById(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT announcement.* FROM announcement WHERE id = ?")) {
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Announcement announcement = getAnnouncement(rs);
                return Optional.of(announcement);
            }
        } finally {
            DatabaseConfig.disconnect();
        }
        return Optional.empty();
    }

    public List<Announcement> getAnnouncementsUserSubscribed(Integer accountId) throws SQLException {
        List<Announcement> announcements = new ArrayList<>();
        ResultSet rs = null;

        try (PreparedStatement ps = conn.prepareStatement("SELECT a.* FROM announcement a INNER JOIN account_category aa ON a.categoryId = aa.categoryId WHERE aa.accountId = ?")) {
            ps.setInt(1, accountId);

            rs = ps.executeQuery();

            while (rs.next()) {
                addAnnouncements(announcements, rs);
            }
        } finally {
            DatabaseConfig.disconnect();
        }

        return announcements;
    }

    public List<Announcement> getAnnouncements() throws SQLException {
        List<Announcement> announcements = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement("SELECT announcement.* FROM announcement")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                addAnnouncements(announcements, rs);
            }
        } finally {
            DatabaseConfig.disconnect();
        }

        return announcements;
    }

    private void addAnnouncements(List<Announcement> announcements, ResultSet rs) throws SQLException {
        Announcement announcement = getAnnouncement(rs);
        announcements.add(announcement);
    }

    private Announcement getAnnouncement(ResultSet rs) throws SQLException {
        Announcement announcement = new Announcement();
        announcement.setId(String.valueOf(rs.getInt("id")));
        announcement.setTitle(rs.getString("title"));
        announcement.setText(rs.getString("text"));
        Date sqlDate = rs.getDate("date");
        LocalDate localDate = sqlDate.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = localDate.format(formatter);
        announcement.setDate(formattedDate);
        announcement.setCategoryId(String.valueOf(rs.getInt("categoryId")));
        return announcement;
    }

    public void updateAnnouncement(String title, String text, String id, String categoryId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE announcement SET title = ?, text = ?, categoryId = ? WHERE id = ?")) {
            ps.setString(1, title);
            ps.setString(2, text);
            ps.setInt(3, Integer.parseInt(categoryId));
            ps.setInt(4, Integer.parseInt(id));

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }

    public void deleteAnnouncement(String id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM announcement WHERE id = ?")) {
            ps.setInt(1, Integer.parseInt(id));

            ps.executeUpdate();
        } finally {
            DatabaseConfig.disconnect();
        }
    }
}
