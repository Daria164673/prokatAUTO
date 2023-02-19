package org.voroniuk.prokat.entity.builders;

import org.apache.commons.codec.digest.DigestUtils;
import org.voroniuk.prokat.entity.User;

import java.util.Locale;

public class UserBuilder {
    private int id;
    private String login;
    private String password;
    private boolean isBlocked;
    private User.Role role;
    private String email;
    private String firstName;
    private String lastName;

    public UserBuilder id(int id) {
        this.id = id;
        return this;
    }

    public UserBuilder login(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder blocked(boolean blocked) {
        isBlocked = blocked;
        return this;
    }

    public UserBuilder role(User.Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User build() {
        User newUser = new User();
        newUser.setId(id);
        newUser.setIsBlocked(isBlocked);
        newUser.setLogin(login);
        newUser.setPassword(password);
        newUser.setRole(role);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);

        return newUser;
    }
}
