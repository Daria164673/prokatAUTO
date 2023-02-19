package org.voroniuk.prokat.dao.impl;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * User Data Access Object implementation
 * provided methods for
 * finding, creating, deleting Users,
 * block/unblock users
 *
 * @author D. Voroniuk
 */

public class UserDAOimp implements UserDAO {
    private final DBManager dbManager;

    private static final Logger LOG = Logger.getLogger(UserDAOimp.class);

    public UserDAOimp(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Add new user into db
     * @param user - saving instance
     */
    public boolean saveUser(User user) {
        String sql = "INSERT INTO users (login, role, pass, isBlocked, email, first_name, last_name) VALUES (?,?,?, false,?,?,?)";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int k=0;
            statement.setString(++k, user.getLogin());
            statement.setString(++k, user.getRole().toString().toLowerCase());
            statement.setString(++k, user.getPassword());
            statement.setString(++k, user.getEmail());
            statement.setString(++k, user.getFirstName());
            statement.setString(++k, user.getLastName());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                }
            }

            LOG.info("User has been added");
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    public User findUserByLogin(String login) {
        String sql =    "select users.id, pass, login, role, isBlocked, email, first_name, last_name from users " +
                 "where login=?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, login);
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    return createUser(resultSet);
                } else {
                    LOG.info("Can't find user " + login);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }
        return null;
    }

    public User findUserById(int id) {
        String sql =    "select id, isBlocked, login, pass, role, email, first_name, last_name from users " +
                "where users.id=?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    return createUser(resultSet);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
            LOG.info("Can't find userby id: " + id);
        }
        return null;
    }

    @Override
    public List<User> findUsers(int start, int offset) {
        List<User> res = new LinkedList<>();
        String sql =    "SELECT id, isBlocked, login, pass, role, email, first_name, last_name " +
                "FROM users as users " +
                "LIMIT ?, ?; ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, start);
            statement.setInt(2, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    res.add(createUser(resultSet));
                }
            } catch (Exception e) {
                LOG.warn(e);
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return res;
    }

    @Override
    public int countUsers() {
        int result = 0;

        String sql = "SELECT COUNT(*) FROM users as users ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return result;
    }

    public List<User> findAllUsers() {
        List<User> res = new LinkedList<>();
        String sql =    "select id, isBlocked, login, pass, role, email, first_name, last_name from users ";

        try (Connection connection = dbManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                res.add(createUser(resultSet));
            }

        } catch (SQLException e) {
            LOG.warn(e);
        }
        return res;
    }

    public boolean deleteUser(User user) {
        String sql = "DELETE FROM users WHERE login=?";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getLogin());
            statement.executeUpdate();

            LOG.info("User has been deleted");
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    private User createUser(ResultSet resultSet)  throws SQLException{

        User.Role role = null;
        try {
            role = User.Role.valueOf(resultSet.getString("role").toUpperCase());
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
        }

        return User.builder()
                .id(resultSet.getInt("id"))
                .login(resultSet.getString("login"))
                .password(resultSet.getString("pass"))
                .blocked(resultSet.getBoolean("isBlocked"))
                .role(role)
                .email(resultSet.getString("email"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .build();
    }

    @Override
    public boolean changeIsBlockedValueById(int user_id, boolean isBlockedValue) {
        String sql = "UPDATE users SET isBlocked=? WHERE id=?; ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, isBlockedValue);
            statement.setInt(2, user_id);

            statement.executeUpdate();

            LOG.info("User "+user_id+" has been " + (isBlockedValue? "blocked": "unblocked"));
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

}
