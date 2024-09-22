package com.frigotermica.tema.util;

import com.frigotermica.tema.models.OperationModel;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DbUtilityOperation {

    public static List<OperationModel> getAll() throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM operations WHERE deleted = FALSE ORDER BY id DESC";
        PreparedStatement stmt = c.prepareStatement(sql);
        List<OperationModel> list = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
            String description = rs.getString("description");
            BigDecimal hours_spent = rs.getBigDecimal("hours_spent");
            int site_id = rs.getInt("site_id");
            int system_id = rs.getInt("system_id");
            int user_id = rs.getInt("user_id");
            OperationModel operation = new OperationModel(id, date, description, hours_spent, site_id, system_id, user_id);
            list.add(operation);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return list;
    }

    public static OperationModel findById(int id) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM operations WHERE id = ?";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        OperationModel op = new OperationModel();
        if (rs.next()) {
            op.setId(rs.getInt("id"));
            op.setDate(rs.getTimestamp("date").toLocalDateTime());
            op.setDescription(rs.getString("description"));
            op.setHoursSpent(rs.getBigDecimal("hours_spent"));
            op.setSiteId(rs.getInt("site_id"));
            op.setSystemId(rs.getInt("system_id"));
            op.setUserId(rs.getInt("user_id"));
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return op;
    }

    public static List<OperationModel> findByOwned(int userId) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM operations WHERE user_id = ? ORDER BY id DESC";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, userId);
        List<OperationModel> list = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
            String description = rs.getString("description");
            BigDecimal hours_spent = rs.getBigDecimal("hours_spent");
            int site_id = rs.getInt("site_id");
            int system_id = rs.getInt("system_id");
            int user_id = rs.getInt("user_id");
            OperationModel operation = new OperationModel(id, date, description, hours_spent, site_id, system_id, user_id);
            list.add(operation);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return list;
    }

    public static void insertPreparedStatement(OperationModel operation, int userId) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "INSERT INTO operations (date, description, hours_spent, site_id, system_id, user_id) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setTimestamp(1, Timestamp.valueOf(operation.getDate()));
        stmt.setString(2, operation.getDescription());
        stmt.setBigDecimal(3, operation.getHoursSpent());
        stmt.setInt(4, operation.getSiteId());
        stmt.setInt(5, operation.getSystemId());
        stmt.setInt(6, userId);
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }

    public static void update(OperationModel op) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "UPDATE operations SET date = ?, description = ?, hours_spent = ?, site_id = ?, system_id = ?, user_id = ? WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setTimestamp(1, Timestamp.valueOf(op.getDate()));
        stmt.setString(2, op.getDescription());
        stmt.setBigDecimal(3, op.getHoursSpent());
        stmt.setInt(4, op.getSiteId());
        stmt.setInt(5, op.getSystemId());
        stmt.setInt(6, op.getUserId());
        stmt.setInt(7, op.getId());
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }
}