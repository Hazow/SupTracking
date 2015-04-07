package com.supinfo.suptracking.model;

public class User{

    private int id;
    private String username;
    private String password;

    public User(){
    }

    public User(String login,String password){
        this.username = login;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUsername(String login) {
        this.username = login;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
