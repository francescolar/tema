package com.frigotermica.tema.util;

import com.frigotermica.tema.models.SiteModel;

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
            int totalWorkHours = rs.getInt("total_work_hours");
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
            int totalWorkHours = rs.getInt("total_work_hours");
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
            int totalWorkHours = rs.getInt("total_work_hours");
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

    public static void updateTotalSystems(int site_id) throws SQLException {
        int tmp = 0;
        Connection c = DbUtility.createConnection();
        String sql = "SELECT total_systems FROM sites WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, site_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            tmp = rs.getInt("total_systems");
        }
        sql = "UPDATE sites SET total_systems = ? WHERE id = ?;";
        stmt = c.prepareStatement(sql);
        stmt.setInt(1, tmp + 1);
        stmt.setInt(2, site_id);
        stmt.executeUpdate();
        stmt.close();
        rs.close();
        DbUtility.closeConnection(c);
    }

    public static void removeTotalSystems(int system_id) throws SQLException {
        int site_id = 0;
        Connection c = DbUtility.createConnection();
        String sql = "SELECT site_id FROM systems WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, system_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            site_id = rs.getInt("site_id");
        }
        sql = "UPDATE sites SET total_systems = total_systems - 1 WHERE id = ?;";
        stmt = c.prepareStatement(sql);
        stmt.setInt(1, site_id);
        stmt.executeUpdate();
        stmt.close();
        rs.close();
        DbUtility.closeConnection(c);
    }

    public static void updateDeletedWorkHoursLog(String tableName, int id) throws SQLException {
        int total_work_hours = 0;
        Connection c = DbUtility.createConnection();
        String sql = null;
        if (tableName.equals("systems")) {
            sql = "SELECT total_work_hours FROM systems WHERE id = ?;";
        } else {
            sql = "SELECT total_work_hours FROM sites WHERE id = ?;";
        }
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            total_work_hours = rs.getInt("total_work_hours");
        }
        if (tableName.equals("systems")) {
            sql = "UPDATE deleted_log SET hours_log = hours_log + ? WHERE table_name = 'systems';";
        } else {
            sql = "UPDATE deleted_log SET hours_log = hours_log + ? WHERE table_name = 'sites';";
        }
        stmt = c.prepareStatement(sql);
        stmt.setInt(1, total_work_hours);
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }
}