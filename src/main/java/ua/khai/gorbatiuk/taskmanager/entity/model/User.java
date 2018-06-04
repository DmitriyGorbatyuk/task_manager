package ua.khai.gorbatiuk.taskmanager.entity.model;

import java.io.Serializable;

public class User extends DBEntity implements Serializable {
    private static final long serialVersionUID = 6700580243323301014L;

    private String email;
    private String password;

    public User() {
    }

    public User(Integer id, String email, String password) {
        super(id);
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
