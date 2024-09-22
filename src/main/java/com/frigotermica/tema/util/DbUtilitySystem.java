package com.frigotermica.tema.util;

import com.frigotermica.tema.models.SystemModel;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DbUtilitySystem {

    public static List<SystemModel> getAll() throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM systems WHERE deleted = FALSE ORDER BY name;";
        PreparedStatement stmt = c.prepareStatement(sql);
        List<SystemModel> list = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int siteId = rs.getInt("site_id");
            BigDecimal totalWorkHours = rs.getBigDecimal("total_work_hours");
            boolean enabled = rs.getBoolean("enabled");
            SystemModel system = new SystemModel(id, name, siteId, totalWorkHours, enabled);
            list.add(system);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return list;
    }

    public static List<SystemModel> getAllEnabled() throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM systems WHERE deleted = FALSE AND enabled = TRUE ORDER BY name;";
        PreparedStatement stmt = c.prepareStatement(sql);
        List<SystemModel> list = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int siteId = rs.getInt("site_id");
            BigDecimal totalWorkHours = rs.getBigDecimal("total_work_hours");
            boolean enabled = rs.getBoolean("enabled");
            SystemModel system = new SystemModel(id, name, siteId, totalWorkHours, enabled);
            list.add(system);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return list;
    }

    public static SystemModel findById(int id) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM systems WHERE ((id = ?) AND (deleted = FALSE))";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, id);
        SystemModel system = new SystemModel();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            int siteId = rs.getInt("site_id");
            BigDecimal totalWorkHours = rs.getBigDecimal("total_work_hours");
            boolean enabled = rs.getBoolean("enabled");
            system = new SystemModel(id, name, siteId, totalWorkHours, enabled);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return system;
    }

    public static List<SystemModel> findBySiteId(int siteId) throws SQLException {
        List<SystemModel> systems = new ArrayList<>();
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM systems WHERE site_id = ?";

        try (PreparedStatement statement = c.prepareStatement(sql)) {
            statement.setInt(1, siteId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                SystemModel system = new SystemModel();
                system.setId(rs.getInt("id"));
                system.setName(rs.getString("name"));
                systems.add(system);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return systems;
    }


    public static void insertPreparedStatement(SystemModel system) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "INSERT INTO systems (name, site_id) VALUES (?, ?);";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, system.getName());
        stmt.setInt(2, system.getSiteId());
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }

    public static void updatePreparedStatement(SystemModel system) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "UPDATE systems SET name = ?, site_id = ? WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, system.getName());
        stmt.setInt(2, system.getSiteId());
        stmt.setInt(3, system.getId());
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }

    public static void deleteAfterSiteDeleted(int siteId) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "UPDATE systems SET deleted = TRUE, enabled = FALSE WHERE site_id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, siteId);
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }
}