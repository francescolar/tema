package com.frigotermica.tema.models;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserModel {

    @NotNull
    @PositiveOrZero
    private int id;

    @NotNull(message = "Username is required")
    @Size(min = 4, max = 25, message = "Username must be between 4 and 25 characters")
    @Pattern(regexp = "^[a-zA-Z0-9.]+$", message = "Username can contain only letters, numbers, and periods")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@$!%*?&?])[A-Za-z0-9@$!%*?&?]{8,50}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character (@$!%*?&?), and must only include Latin characters."
    )
    private String password;

    @NotNull(message = "Email is required")
    @Size(min = 2, max = 50, message = "Email must be between 2 and 50 characters")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[A-Za-z0-9\\s'’\\-@$!%*?&.]+$", message = "Email contains invalid characters")
    private String email;

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s'’\\-@$!%*?&.]+$", message = "Name contains invalid characters")
    private String name;

    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s'’\\-@$!%*?&.]+$", message = "Surname contains invalid characters")
    private String surname;

    private String role;

    @PositiveOrZero
    private BigDecimal totalWorkHours;

    private LocalDateTime createdAt;

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
                     @Size(min = 2, max = 50) String surname, @Min(0) BigDecimal totalWorkHours, LocalDateTime createdAt, String role, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.totalWorkHours = totalWorkHours;
        this.createdAt = createdAt;
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

    public BigDecimal getTotalWorkHours() {
        return totalWorkHours;
    }

    public void setTotalWorkHours(BigDecimal totalWorkHours) {
        this.totalWorkHours = totalWorkHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", deleted=" + deleted +
                '}';
    }
}