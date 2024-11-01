package com.frigotermica.tema.models;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperationModel {

    @NotNull(message = "ID NotNull error")
    @PositiveOrZero(message = "ID PositiveOrZero error")
    private int id;

    @NotNull(message = "Date NotNull error")
    private LocalDateTime date;

    @NotNull(message = "Description NotNull error")
    @Size(min = 1, max = 3000, message = "Description size error")
    @Pattern(regexp = "^[A-Za-z0-9\\s'’\\-@$!%*?&,.;:]+$", message = "Description regex error")
    private String description;

    @NotNull(message = "Hours spent NotNull error")
    @Positive(message = "Hours spent Positive error")
    @Max(100)
    private BigDecimal hoursSpent;

    @NotNull(message = "Site ID NotNull error")
    @PositiveOrZero(message = "Site ID NotNull error")
    private int siteId;

    @NotNull(message = "System ID NotNull error")
    @PositiveOrZero(message = "System ID PositiveOrZero error")
    private int systemId;

    @NotNull(message = "User ID NotNull error")
    @PositiveOrZero(message = "User ID PositiveOrZero error")
    private int userId;

    private boolean deleted;

    public OperationModel(@NotNull int id, @NotNull LocalDateTime date, @NotNull String description,
                          @NotNull @Min(0) @Max(100) BigDecimal hoursSpent, @NotNull int siteId, @NotNull int systemId,
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
                          @NotNull @Min(0) @Max(100) BigDecimal hoursSpent, @NotNull int siteId, @NotNull int systemId,
                          @NotNull int userId) {
        this.date = date;
        this.description = description;
        this.hoursSpent = hoursSpent;
        this.siteId = siteId;
        this.systemId = systemId;
        this.userId = userId;
    }

    public OperationModel(@NotNull int id, @NotNull LocalDateTime date, @NotNull String description,
                          @NotNull @Min(0) @Max(100) BigDecimal hoursSpent, @NotNull int siteId, @NotNull int systemId,
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

    public OperationModel(int id, @Min(0) @Max(100) BigDecimal hoursSpent, int siteId, int systemId, int userId) {
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