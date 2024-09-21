package com.frigotermica.tema.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserModel {

    @NotNull
    private int id;

    @NotNull
	@Size(min=4, max=30)
    private String username;

//    @NotNull
//	@Size(min=2, max=200)
    private String password;

    @NotNull
	@Size(min=2, max=100)
    private String email;

    @NotNull
	@Size(min=2, max=50)
    private String name;

	@Size(min=2, max=50)
    private String surname;

    private String role;

    private boolean enabled;
    private boolean deleted;
    
    
    public UserModel(@NotNull int id, @NotNull @Size(min = 4, max = 30) String username,
            @NotNull @Size(min = 2, max = 100) String email, @NotNull @Size(min = 2, max = 50) String name,
            @Size(min = 2, max = 50) String surname, String role, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.enabled = enabled;
    }
    public UserModel(@NotNull int id, @NotNull @Size(min = 4, max = 30) String username,
            @NotNull @Size(min = 2, max = 100) String email, @NotNull @Size(min = 2, max = 50) String name,
            @Size(min = 2, max = 50) String surname, boolean enabled, boolean deleted, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.deleted = deleted;
        this.role = role;
    }
    public UserModel(@NotNull @Size(min = 4, max = 30) String username, @NotNull @Size(min = 2, max = 200) String password,
            @NotNull @Size(min = 2, max = 100) String email, @NotNull @Size(min = 2, max = 50) String name,
            @Size(min = 2, max = 50) String surname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }
    public UserModel(@NotNull int id, @NotNull @Size(min = 4, max = 30) String username,
            @NotNull @Size(min = 2, max = 200) String password, @NotNull @Size(min = 2, max = 100) String email,
            @NotNull @Size(min = 2, max = 50) String name, @Size(min = 2, max = 50) String surname, boolean enabled,
            boolean deleted) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.enabled = enabled;
        this.deleted = deleted;
    }
    public UserModel() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    
}
