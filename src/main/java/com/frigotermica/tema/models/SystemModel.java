package com.frigotermica.tema.models;

import jakarta.validation.constraints.NotNull;

public class SystemModel {

    @NotNull
    private int id;
    
    @NotNull
    private String name;

    @NotNull
    private int siteId;

    private int totalWorkHours;

    private boolean deleted;

    private boolean enabled;

    public SystemModel(@NotNull int id, @NotNull String name, @NotNull int siteId, int totalWorkHours, boolean enabled) {
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

    public int getTotalWorkHours() {
        return totalWorkHours;
    }

    public void setTotalWorkHours(int totalWorkHours) {
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
