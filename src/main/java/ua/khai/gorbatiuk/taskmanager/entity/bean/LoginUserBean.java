package ua.khai.gorbatiuk.taskmanager.entity.bean;

import java.io.Serializable;

public class LoginUserBean implements Serializable {
    private static final long serialVersionUID = -6939235565557861435L;

    private String email;
    private String password;

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
