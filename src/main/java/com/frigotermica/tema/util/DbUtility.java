package com.frigotermica.tema.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.frigotermica.tema.models.OperationModel;
import com.frigotermica.tema.models.SiteModel;
import com.frigotermica.tema.models.SystemModel;
import com.frigotermica.tema.models.UserModel;
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

    public static void saveEditedLog(String tableName, int id) throws SQLException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonString = switch (tableName) {
            case "users" -> {
                UserModel user = DbUtilityUser.findById(id);
                yield objectMapper.writeValueAsString(user);
            }
            case "operations" -> {
                OperationModel operation = DbUtilityOperation.findById(id);
                yield objectMapper.writeValueAsString(operation);
            }
            case "sites" -> {
                SiteModel site = DbUtilitySite.findById(id);
                yield objectMapper.writeValueAsString(site);
            }
            case "systems" -> {
                SystemModel system = DbUtilitySystem.findById(id);
                yield objectMapper.writeValueAsString(system);
            }
            default -> null;
        };
        Connection c = createConnection();
        String sql = "INSERT INTO edit_log_table (table_name, item_id, serialized_data) VALUES (?, ?, ?::jsonb);";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, tableName);
        stmt.setInt(2, id);
        stmt.setString(3, jsonString);
        stmt.executeUpdate();
        stmt.close();
        c.close();
    }
}