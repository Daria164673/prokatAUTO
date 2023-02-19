package org.voroniuk.prokat.entity;

import org.voroniuk.prokat.entity.builders.UserBuilder;

import java.io.Serializable;

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
    private String email;
    private String firstName;
    private String lastName;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public enum Role implements Serializable {
        CUSTOMER,
        MANAGER,
        ADMIN
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

}
