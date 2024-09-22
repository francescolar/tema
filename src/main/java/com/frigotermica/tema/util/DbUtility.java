package com.frigotermica.tema.util;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
public class DbUtility {

    private static DataSource dataSource;

    public DbUtility(DataSource dataSource) {
        DbUtility.dataSource = dataSource;
    }

    public static Connection createConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public static void softDeletePreparedStatement(int id, String tableName) throws SQLException {
        Connection c = createConnection();
        List<String> allowedTables = Arrays.asList("users", "operations", "systems", "sites");
        if (!allowedTables.contains(tableName)) {
            throw new IllegalArgumentException("Invalid tableName name provided.");
        }

        if (!tableName.equals("operations")) {
            String sql = "UPDATE " + tableName + " SET enabled = FALSE, deleted = TRUE WHERE id = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.close();
        } else {
            String sql = "UPDATE " + tableName + " SET deleted = TRUE WHERE id = ?;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.close();
        }

        if (tableName.equals("sites")) {
            DbUtilitySystem.deleteAfterSiteDeleted(id);
        }

        DbUtility.closeConnection(c);
    }

    public static void toggleEnable(String tableName, int id) throws SQLException {
        Connection c = DbUtility.createConnection();
        List<String> allowedTables = Arrays.asList("users", "operations", "systems", "sites");

        if (!allowedTables.contains(tableName)) {
            throw new IllegalArgumentException("Invalid table name provided.");
        }

        String sql = "UPDATE " + tableName + " SET enabled = NOT enabled WHERE id = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
        if (tableName.equals("sites")) {
            sql = "UPDATE systems SET enabled = NOT enabled WHERE site_id = ?;";
            stmt = c.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        stmt.close();
        DbUtility.closeConnection(c);
    }
}