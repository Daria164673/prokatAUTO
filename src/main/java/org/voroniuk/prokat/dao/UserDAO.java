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
    boolean saveUser(User user);

    User findUserByLogin(String login);

    User findUserById(int id);

    List<User> findUsers(int start, int offset);

    int countUsers();

    List<User> findAllUsers();

    boolean deleteUser(User user);

    boolean changeIsBlockedValueById(int user_id, boolean isBlockedValue);

}
