package org.voroniuk.prokat.dao.impl;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * User Data Access Object implementation
 * provided methods for
 * finding, creating, deleting Users,
 * block/unblock users,
 * set user's locale
 *
 * @author D. Voroniuk
 */

public class UserDAOimp implements UserDAO {
    private static final Logger LOG = Logger.getLogger(UserDAOimp.class);

    /**
     * Add new user into db
     * @param user
     */
    public boolean saveUser(User user) {
        String sql = "INSERT INTO users (login, role, pass, isBlocked, locale) VALUES (?,?,?, false, ?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getRole().toString().toLowerCase());
            statement.setString(3, user.getPassword());
            statement.setString(4, (user.getLocale()==null? Locale.getDefault() : user.getLocale()).getLanguage().toUpperCase());
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
        String sql =    "select users.id, pass, login, role, isBlocked, locale from users " +
                 "where login=?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

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
        String sql =    "select id, isBlocked, login, pass, role, locale from users " +
                "where users.id=?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

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
        String sql =    "SELECT id, isBlocked, login, pass, role, locale " +
                "FROM users as users " +
                "LIMIT ?, ?; ";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

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

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

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
        String sql =    "select id, isBlocked, login, pass, role, locale from users ";

        try (Connection connection = DBManager.getInstance().getConnection();
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

        try (Connection connection = DBManager.getInstance().getConnection();
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

        String login = resultSet.getString("login");
        String password = resultSet.getString("pass");
        User.Role role = null;
        try {
            role = User.Role.valueOf(resultSet.getString("role").toUpperCase());
        } catch (IllegalArgumentException e) {
        }

        Locale locale = Locale.getDefault();
        try {
            locale = SiteLocale.valueOf(resultSet.getString("locale")).getLocale();
        } catch (Exception e) {
        }

        User user = new User(login, password, role);
        user.setId(resultSet.getInt("id"));
        user.setIsBlocked(resultSet.getBoolean("isBlocked"));
        user.setLocale(locale);

        return user;
    }

    @Override
    public boolean changeIsBlockedValueById(int user_id, boolean isBlockedValue) {
        String sql = "UPDATE users SET isBlocked=? WHERE id=?; ";

        try (Connection connection = DBManager.getInstance().getConnection();
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

    @Override
    public boolean setUsersLocale(User user, Locale locale) {
        String sql = "UPDATE users SET locale=? WHERE id=?; ";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, locale.getLanguage().toUpperCase());
            statement.setInt(2, user.getId());

            statement.executeUpdate();

            LOG.info("Save locale " + locale.toString() + " fo User " + user.getId());
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }
}
