package org.voroniuk.prokat.entity;

import java.io.Serializable;
import java.util.Locale;

/**
 * User entity
 *
 * @author D. Voroniuk
 */

public class User implements Serializable {
    private int id;
    private String login;
    private String password;
    private boolean isBlocked;
    private Role role;

    private Locale locale = Locale.getDefault();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User() {
    }

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public enum Role implements Serializable {
        CUSTOMER,
        MANAGER,
        ADMIN
    }
}
