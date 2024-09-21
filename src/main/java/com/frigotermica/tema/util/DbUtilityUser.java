package com.frigotermica.tema.util;

import com.frigotermica.tema.models.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class DbUtilityUser {

    public static List<UserModel> getAll() throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT users.*, authorities.authority FROM users INNER JOIN authorities ON users.username = authorities.username WHERE users.deleted = FALSE ORDER BY users.id";
        PreparedStatement stmt = c.prepareStatement(sql);
        List<UserModel> listUser = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean enabled = rs.getBoolean("enabled");
            String role = rs.getString("authority");
            UserModel user = new UserModel(id, username, email, name, surname, role, enabled);
            listUser.add(user);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return listUser;
    }

    public static List<UserModel> getAllNoAdmin() throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT users.*, authorities.authority FROM users INNER JOIN authorities ON users.username = authorities.username WHERE ((users.deleted = FALSE) AND (authorities.authority != 'ROLE_ADMIN')) ORDER BY users.id;";
        PreparedStatement stmt = c.prepareStatement(sql);
        List<UserModel> listUser = new LinkedList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean enabled = rs.getBoolean("enabled");
            String role = rs.getString("authority");
            UserModel user = new UserModel(id, username, email, name, surname, role, enabled);
            listUser.add(user);
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return listUser;
    }

    public static UserModel findById(int user_id) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT users.*, authorities.authority FROM users INNER JOIN authorities ON users.username = authorities.username WHERE ((users.id = ?) AND (deleted = FALSE))";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, user_id);
        UserModel user = new UserModel();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            boolean enabled = rs.getBoolean("enabled");
            String role = rs.getString("authority");
            user = new UserModel(id, username, email, name, surname, role, enabled);
        }
        rs.close();
        stmt.close();
        c.close();
        return user;
    }

    public static boolean checkUsername(String username) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM users WHERE username = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            rs.close();
            stmt.close();
            DbUtility.closeConnection(c);
            return false;
        } else {
            rs.close();
            stmt.close();
            DbUtility.closeConnection(c);
            return true;
        }
    }

    public static boolean checkEmail(String email) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "SELECT * FROM users WHERE email = ?;";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            rs.close();
            stmt.close();
            DbUtility.closeConnection(c);
            return false;
        } else {
            rs.close();
            stmt.close();
            DbUtility.closeConnection(c);
            return true;
        }
    }

    public static void insertPreparedStatement(UserModel user) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "INSERT INTO users (username, email, name, surname) VALUES(?, ?, ?, ?);";
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getEmail().toLowerCase());
        stmt.setString(3, user.getName());
        stmt.setString(4, user.getSurname());
        stmt.executeUpdate();
        sql = "INSERT INTO soft_operation (recon, username) VALUES (?, ?);";
        stmt = c.prepareStatement(sql);
        String salt = BCrypt.gensalt();
        stmt.setString(1, CryptoPassword.cryptoPasswordwithSalt(user.getPassword(), salt));
        stmt.setString(2, user.getUsername());
        stmt.executeUpdate();
        sql = "INSERT INTO authorities (username, authority) VALUES(?, ?);";
        stmt = c.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, "ROLE_USER_FIRSTLOGIN");

        stmt.executeUpdate();
        stmt.close();
        DbUtility.closeConnection(c);
    }

    public static void updateExistingUser(UserModel user, String password, boolean isFirstLogin) throws SQLException {
        Connection c = DbUtility.createConnection();
        String sql = "UPDATE users SET username = ?, email = ?, name = ?, surname = ? WHERE id = ?;";
        PreparedStatement stmt =  c.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getName());
        stmt.setString(4, user.getSurname());
        stmt.setInt(5, user.getId());
        stmt.executeUpdate();
        /* non sono sicuro che servi questa parte */
        sql = "UPDATE soft_operation SET recon = ? WHERE username = ?;";
        stmt = c.prepareStatement(sql);
        stmt.setString(1, password);
        stmt.setString(2, user.getUsername());
        stmt.executeUpdate();
        if (isFirstLogin) {
            sql = "UPDATE authorities SET authority = ? WHERE username = ?;";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, "ROLE_USER");
            stmt.setString(2, user.getUsername());
            stmt.executeUpdate();
        }
        stmt.close();
        c.close();
    }

    public static int getUserIdByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT id FROM users WHERE username = ?";
        Connection c = DbUtility.createConnection();
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        int userId = -1;
        if (rs.next()) {
            userId = rs.getInt("id");
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return userId;
    }

    public static int getAuthenticatedUserId() throws SQLException, ClassNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        }

        return getUserIdByUsername(username);
    }

    public static UserModel getUserDetails(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT users.*, soft_operation.recon FROM users INNER JOIN soft_operation ON users.username = soft_operation.username WHERE users.id = ?;";
        Connection c = DbUtility.createConnection();
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        UserModel user = new UserModel();
        if (rs.next()) {
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setPassword(rs.getString("recon"));
        }
        rs.close();
        stmt.close();
        DbUtility.closeConnection(c);
        return user;
    }
}