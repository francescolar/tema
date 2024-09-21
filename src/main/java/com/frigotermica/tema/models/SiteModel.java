package com.frigotermica.tema.models;

import jakarta.validation.constraints.NotNull;

public class SiteModel {

    @NotNull
    private int id;
    
    @NotNull
    private String name;

    private String address;

    private boolean deleted;

    private boolean enabled;

    private int totalWorkHours;

    private int totalSystems;

    public SiteModel(@NotNull int id, @NotNull String name, String address, int totalWorkHours, int totalSystems, boolean enabled) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.totalWorkHours = totalWorkHours;
        this.totalSystems = totalSystems;
        this.enabled = enabled;
    }

    public SiteModel(@NotNull int id, @NotNull String name, String address, boolean deleted, boolean enabled) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.deleted = deleted;
        this.enabled = enabled;
    }

    public SiteModel(@NotNull String name, String address, boolean deleted, boolean enabled) {
        this.name = name;
        this.address = address;
        this.deleted = deleted;
        this.enabled = enabled;
    }

    public SiteModel(@NotNull String name, String address) {
        this.name = name;
        this.address = address;
    }

    public SiteModel() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getTotalSystems() {
        return totalSystems;
    }

    public void setTotalSystems(int totalSystems) {
        this.totalSystems = totalSystems;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
}
