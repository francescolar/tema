package com.frigotermica.tema.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class OperationModel {

    @NotNull(message = "id")
    private int id;

    @NotNull(message = "date")
    private LocalDateTime date;

    @NotNull(message = "description")
    private String description;

    @NotNull(message = "hoursSpent")
    private int hoursSpent;

    @NotNull(message = "site_id")
    private int siteId;

    @NotNull(message = "system_id")
    private int systemId;

    @NotNull(message = "user_id")
    private int userId;

    private boolean deleted;

    public OperationModel(@NotNull int id, @NotNull LocalDateTime date, @NotNull String description,
            @NotNull @Size(min = 1, max = 100) int hoursSpent, @NotNull int siteId, @NotNull int systemId,
            @NotNull int userId) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
    }

    public OperationModel(@NotNull LocalDateTime date, @NotNull String description,
            @NotNull @Size(min = 1, max = 100) int hoursSpent, @NotNull int siteId, @NotNull int systemId,
            @NotNull int userId) {
        this.date = date;
        this.description = description;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
    }

    public OperationModel(@NotNull int id, @NotNull LocalDateTime date, @NotNull String description,
            @NotNull @Size(min = 1, max = 100) int hoursSpent, @NotNull int siteId, @NotNull int systemId,
            @NotNull int userId, boolean deleted) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
        this.deleted = deleted;
    }

    public OperationModel() {
    }

    public OperationModel(int id, int hoursSpent, int siteId, int systemId, int userId) {
        this.id = id;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(int hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    @Override
    public String toString() {
        return "OperationModel{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", hoursSpent=" + hoursSpent +
                ", siteId=" + siteId +
                ", systemId=" + systemId +
                ", userId=" + userId +
                ", deleted=" + deleted +
                '}';
    }
}
