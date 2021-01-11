package com.advinow.mica.model.mita;



import java.sql.Timestamp;


public class MITAUser {

    private Integer user_id;
    private int role_id;
    private String role_name;
    private String email;
    private String name;
    private String surname;
    private String auth0Id;
    private Timestamp created_on;

    public Integer getUser_id() {

        return user_id;
    }

    public void setUser_id(Integer user_id) {

        this.user_id = user_id;
    }

    public int getRole_id() {

        return role_id;
    }

    public void setRole_id(int role_id) {

        this.role_id = role_id;
    }

    public String getRole_name() {

        return role_name;
    }

    public void setRole_name(String role_name) {

        this.role_name = role_name;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getSurname() {

        return surname;
    }

    public void setSurname(String surname) {

        this.surname = surname;
    }

    public String getAuth0Id() {

        return auth0Id;
    }

    public void setAuth0Id(String auth0Id) {

        this.auth0Id = auth0Id;
    }

    public Timestamp getCreated_on() {

        return created_on;
    }

    public void setCreated_on(Timestamp created_on) {

        this.created_on = created_on;
    }

}