package ua.khai.gorbatiuk.taskmanager.entity.bean;

import java.io.Serializable;

public class RegistrationUserBean implements Serializable {
    private static final long serialVersionUID = -6939235565557861435L;

    private Integer id;
    private String email;
    private String password;
    private String confirmedPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }
}
