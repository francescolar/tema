package com.frigotermica.tema.util;

import com.frigotermica.tema.models.FullOperationModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class DbUtilityFullOperation {

    public static List<FullOperationModel> getAll() throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT operations.*, users.username AS username, systems.name AS system_name, sites.name AS site_name FROM operations INNER JOIN users ON operations.user_id = users.id INNER JOIN systems ON operations.system_id = systems.id INNER JOIN sites ON operations.site_id = sites.id WHERE operations.deleted = FALSE ORDER BY id DESC;";
        PreparedStatement stmt = c.prepareStatement(sql);
        List<FullOperationModel> list = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
            String description = rs.getString("description");
            int hours_spent = rs.getInt("hours_spent");
            int site_id = rs.getInt("site_id");
            int system_id = rs.getInt("system_id");
            int user_id = rs.getInt("user_id");
            String system_name = rs.getString("system_name");
            String site_name = rs.getString("site_name");
            String username = rs.getString("username");
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            FullOperationModel operation = new FullOperationModel(id, date, description, hours_spent, site_id, system_id, user_id, system_name, site_name, username, createdAt);
            list.add(operation);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return list;
    }

    public static FullOperationModel findById(int id) throws SQLException, ClassNotFoundException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT operations.*, users.username AS username, systems.name AS system_name, sites.name AS site_name FROM operations INNER JOIN users ON operations.user_id = users.id INNER JOIN systems ON operations.system_id = systems.id INNER JOIN sites ON operations.site_id = sites.id WHERE operations.id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        FullOperationModel op = new FullOperationModel();
        if (rs.next()) {
            op.setId(rs.getInt("id"));
            op.setDate(rs.getTimestamp("date").toLocalDateTime());
            op.setDescription(rs.getString("description"));
            op.setHoursSpent(rs.getInt("hours_spent"));
            op.setSiteId(rs.getInt("site_id"));
            op.setSystemId(rs.getInt("system_id"));
            op.setUserId(rs.getInt("user_id"));
            op.setSystemName(rs.getString("system_name"));
            op.setSiteName(rs.getString("site_name"));
            op.setUsername(rs.getString("username"));
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return op;
    }

    public static List<FullOperationModel> findByOwned(int userId) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT operations.*, users.username AS user_username, systems.name AS system_name, sites.name AS site_name FROM operations INNER JOIN users ON operations.user_id = users.id INNER JOIN systems ON operations.system_id = systems.id INNER JOIN sites ON operations.site_id = sites.id WHERE operations.user_id = ? ORDER BY id DESC;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, userId);
        List<FullOperationModel> list = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
            String description = rs.getString("description");
            int hours_spent = rs.getInt("hours_spent");
            int site_id = rs.getInt("site_id");
            int system_id = rs.getInt("system_id");
            int user_id = rs.getInt("user_id");
            String system_name = rs.getString("system_name");
            String site_name = rs.getString("site_name");
            String user_username = rs.getString("user_username");
            FullOperationModel operation = new FullOperationModel(id, date, description, hours_spent, site_id, system_id, user_id, system_name, site_name, user_username);
            list.add(operation);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return list;
    }
}