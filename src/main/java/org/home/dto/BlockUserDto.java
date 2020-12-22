package org.home.dto;

public class BlockUserDto {
    private long id;
    private boolean isBlocked;

    public BlockUserDto(long id, boolean isBlocked) {
        this.id = id;
        this.isBlocked = isBlocked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
