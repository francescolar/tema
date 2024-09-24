package com.frigotermica.tema.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FullOperationModel {

    @NotNull
    @Min(0)
    private int id;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private String description;

    @NotNull
    @Min(0)
    @Max(100)
    private BigDecimal hoursSpent;

    @NotNull
    private int siteId;

    @NotNull
    private int systemId;

    @NotNull
    private int userId;

    private boolean deleted;

    private String systemName;

    private String siteName;

    private String username;

    private LocalDateTime createdAt;


    public FullOperationModel(@NotNull int id, @NotNull LocalDateTime date, @NotNull String description,
                              @NotNull @Size(min = 1, max = 100) BigDecimal hoursSpent, @NotNull int siteId, @NotNull int systemId,
                              @NotNull int userId, String systemName, String siteName, String username, LocalDateTime createdAt) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
        this.systemName = systemName;
        this.siteName = siteName;
        this.username = username;
        this.createdAt = createdAt;
    }

    public FullOperationModel(@NotNull int id, @NotNull LocalDateTime date, @NotNull String description,
                              @NotNull @Size(min = 1, max = 100) BigDecimal hoursSpent, @NotNull int siteId, @NotNull int systemId,
                              @NotNull int userId, String systemName, String siteName, String username) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
        this.systemName = systemName;
        this.siteName = siteName;
        this.username = username;
    }

    public FullOperationModel(@NotNull int id, @NotNull LocalDateTime date, @NotNull String description,
                              @NotNull @Size(min = 1, max = 100) BigDecimal hoursSpent, @NotNull int siteId, @NotNull int systemId,
                              @NotNull int userId) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
    }

    public FullOperationModel(@NotNull LocalDateTime date, @NotNull String description,
                              @NotNull @Size(min = 1, max = 100) BigDecimal hoursSpent, @NotNull int siteId, @NotNull int systemId,
                              @NotNull int userId) {
        this.date = date;
        this.description = description;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
    }

    public FullOperationModel(@NotNull int id, @NotNull LocalDateTime date, @NotNull String description,
                              @NotNull @Size(min = 1, max = 100) BigDecimal hoursSpent, @NotNull int siteId, @NotNull int systemId,
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

    public FullOperationModel() {
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public BigDecimal getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(BigDecimal hoursSpent) {
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
        return "OperationModel [id=" + id + ", date=" + date + ", description=" + description + ", hoursSpent="
                + hoursSpent + ", siteId=" + siteId + ", systemId=" + systemId + ", userId=" + userId + "]";
    }
}