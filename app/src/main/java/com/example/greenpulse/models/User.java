package com.example.greenpulse.models;

public class User {
    private String username;
    private String email;
    private String password;
    private String role;
    private String dp;

    public User(String username, String email, String password, String role, String dp) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.dp = dp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }


}
