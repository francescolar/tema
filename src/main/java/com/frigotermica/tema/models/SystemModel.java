package com.frigotermica.tema.models;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class SystemModel {

    @NotNull(message = "ID error")
    @PositiveOrZero
    private int id;

    @NotNull(message = "Name error")
    @Pattern(regexp = "^[A-Za-z0-9\\s'’\\-@$!%*?&.]+$", message = "Invalid characters in name")
    @Size(min = 4, max = 50)
    private String name;

    @NotNull(message = "Site ID error")
    @PositiveOrZero
    private int siteId;

    @PositiveOrZero
    private BigDecimal totalWorkHours;

    private boolean deleted;

    private boolean enabled;

    public SystemModel(@NotNull int id, @NotNull String name, @NotNull int siteId, @Min(0) BigDecimal totalWorkHours, boolean enabled) {
        this.id = id;
        this.name = name;
        this.siteId = siteId;
        this.totalWorkHours = totalWorkHours;
        this.enabled = enabled;
    }

    public SystemModel(@NotNull String name, @NotNull int siteId) {
        this.name = name;
        this.siteId = siteId;
    }

    public SystemModel(@NotNull String name, @NotNull int siteId, boolean deleted, boolean enabled) {
        this.name = name;
        this.siteId = siteId;
        this.deleted = deleted;
        this.enabled = enabled;
    }

    public SystemModel(@NotNull int id, @NotNull String name, @NotNull int siteId, boolean deleted, boolean enabled) {
        this.id = id;
        this.name = name;
        this.siteId = siteId;
        this.deleted = deleted;
        this.enabled = enabled;
    }

    public SystemModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "SystemModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", siteId=" + siteId +
                ", totalWorkHours=" + totalWorkHours +
                ", deleted=" + deleted +
                ", enabled=" + enabled +
                '}';
    }
}