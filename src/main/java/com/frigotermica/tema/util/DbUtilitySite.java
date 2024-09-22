package com.frigotermica.tema.util;

import com.frigotermica.tema.models.SiteModel;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class DbUtilitySite {

    public static List<SiteModel> getAll() throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM sites WHERE deleted = FALSE ORDER BY name;";
        PreparedStatement stmt = c.prepareStatement(sql);
        List<SiteModel> list = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            BigDecimal totalWorkHours = rs.getBigDecimal("total_work_hours");
            int totalSystems = rs.getInt("total_systems");
            boolean enabled = rs.getBoolean("enabled");
            SiteModel site = new SiteModel(id, name, address, totalWorkHours, totalSystems, enabled);
            list.add(site);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return list;
    }

    public static List<SiteModel> getAllEnabled() throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM sites WHERE deleted = FALSE AND enabled = TRUE ORDER BY name;";
        PreparedStatement stmt = c.prepareStatement(sql);
        List<SiteModel> list = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            BigDecimal totalWorkHours = rs.getBigDecimal("total_work_hours");
            int totalSystems = rs.getInt("total_systems");
            boolean enabled = rs.getBoolean("enabled");
            SiteModel site = new SiteModel(id, name, address, totalWorkHours, totalSystems, enabled);
            list.add(site);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return list;
    }

    public static SiteModel findById(int id) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM sites WHERE ((id = ?) AND (deleted = FALSE))";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, id);
        SiteModel site = new SiteModel();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name");
            String address = rs.getString("address");
            BigDecimal totalWorkHours = rs.getBigDecimal("total_work_hours");
            int totalSystems = rs.getInt("total_systems");
            boolean enabled = rs.getBoolean("enabled");
            site = new SiteModel(id, name, address, totalWorkHours, totalSystems, enabled);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return site;
    }

    public static void insertPreparedStatement(SiteModel site) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "INSERT INTO sites (name, address) VALUES (?, ?);";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, site.getName());
        stmt.setString(2, site.getAddress());
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }

    public static void updatePreparedStatement(SiteModel site) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "UPDATE sites SET name = ?, address = ? WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, site.getName());
        stmt.setString(2, site.getAddress());
        stmt.setInt(3, site.getId());
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }
}