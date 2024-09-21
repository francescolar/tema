package com.frigotermica.tema.util;

import com.frigotermica.tema.models.OperationModel;

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
            int hours_spent = rs.getInt("hours_spent");
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

    public static OperationModel findById(int id) throws SQLException, ClassNotFoundException {
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
            op.setHoursSpent(rs.getInt("hours_spent"));
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
            int hours_spent = rs.getInt("hours_spent");
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
        stmt.setInt(3, operation.getHoursSpent());
        stmt.setInt(4, operation.getSiteId());
        stmt.setInt(5, operation.getSystemId());
        stmt.setInt(6, userId);
        stmt.executeUpdate();
        updateTotalWorkHours(c, operation.getHoursSpent(), userId, operation.getSystemId(), operation.getSiteId());
        stmt.close();
        DbUtility.closeConnection(c);
    }

    public static void update(OperationModel op) throws SQLException {
        Connection c = DbUtility.createConnection();
        updateWorkHours(op.getId());
        String sql = "UPDATE operations SET date = ?, description = ?, hours_spent = ?, site_id = ?, system_id = ?, user_id = ? WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setTimestamp(1, Timestamp.valueOf(op.getDate()));
        stmt.setString(2, op.getDescription());
        stmt.setInt(3, op.getHoursSpent());
        stmt.setInt(4, op.getSiteId());
        stmt.setInt(5, op.getSystemId());
        stmt.setInt(6, op.getUserId());
        stmt.setInt(7, op.getId());
        stmt.executeUpdate();
        updateTotalWorkHours(c, op.getHoursSpent(), op.getUserId(), op.getSystemId(), op.getSiteId());
        stmt.close();
        DbUtility.closeConnection(c);
    }

    public static void updateTotalWorkHours(Connection c, int hoursSpent, int user_id, int system_id, int site_id) throws SQLException {
        String sql = "UPDATE users SET total_work_hours = total_work_hours + ? WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, hoursSpent);
        stmt.setInt(2, user_id);
        stmt.executeUpdate();

        sql = "UPDATE systems SET total_work_hours = total_work_hours + ? WHERE id = ?;";
        stmt = c.prepareStatement(sql);
        stmt.setInt(1, hoursSpent);
        stmt.setInt(2, system_id);
        stmt.executeUpdate();

        sql = "UPDATE sites SET total_work_hours = total_work_hours + ? WHERE id = ?;";
        stmt = c.prepareStatement(sql);
        stmt.setInt(1, hoursSpent);
        stmt.setInt(2, site_id);
        stmt.executeUpdate();

        stmt.close();
    }

    public static void updateWorkHours(int operation_id) throws SQLException {
        OperationModel op = new OperationModel();
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM operations WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, operation_id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int hoursSpent = rs.getInt("hours_spent");
            int siteId = rs.getInt("site_id");
            int systemId = rs.getInt("system_id");
            int userId = rs.getInt("user_id");
            op = new OperationModel(id, hoursSpent, siteId, systemId, userId);
        }
        sql = "UPDATE sites SET total_work_hours = total_work_hours - ? WHERE id = ?;";
        stmt = c.prepareStatement(sql);
        stmt.setInt(1, op.getHoursSpent());
        stmt.setInt(2, op.getSiteId());
        stmt.executeUpdate();
        sql = "UPDATE systems SET total_work_hours = total_work_hours - ? WHERE id = ?;";
        stmt = c.prepareStatement(sql);
        stmt.setInt(1, op.getHoursSpent());
        stmt.setInt(2, op.getSystemId());
        stmt.executeUpdate();
        sql = "UPDATE users SET total_work_hours = total_work_hours - ? WHERE id = ?;";
        stmt = c.prepareStatement(sql);
        stmt.setInt(1, op.getHoursSpent());
        stmt.setInt(2, op.getUserId());
        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }

    /* implementazione modifica manutenzione (possibilmente in un altra tabella) */
}