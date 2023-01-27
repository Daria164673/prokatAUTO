package org.voroniuk.prokat.dao;

import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.User;

import java.util.List;
import java.util.Locale;

/**
 * User Data Access Object interface
 *
 * @author D. Voroniuk
 */

public interface UserDAO {
    public boolean saveUser(User user);

    public User findUserByLogin(String login);

    public User findUserById(int id);

    public List<User> findUsers(int start, int offset);

    public int countUsers();

    public List<User> findAllUsers();

    public boolean deleteUser(User user);

    public boolean changeIsBlockedValueById(int user_id, boolean isBlockedValue);

    public boolean setUsersLocale(User user, Locale locale);
}
