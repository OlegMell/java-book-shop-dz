package org.home.dto;


import java.util.List;

public class AddBookDto {
    private List<String> firstame;
    private String title;

    public List<String> getFirstame() {
        return firstame;
    }

    public void setFirstame(List<String> firstame) {
        this.firstame = firstame;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
