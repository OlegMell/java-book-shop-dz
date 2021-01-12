package org.home.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserUnblockDateDto {
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate date;

    private boolean isBlocked;

    public UserUnblockDateDto(String id, LocalDate date, boolean isBlocked) {
        this.id = id;
        this.date = date;
        this.isBlocked = isBlocked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
