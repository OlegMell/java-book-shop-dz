package org.home.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

public class BookValidationDto {
    private Long id;

    @NotBlank(message = "Title is required field!")
    private String title;

    @NotBlank(message = "Genre is required field")
    private String genre;

    @NotBlank(message = "Price is required field!")
    private String price;

    private List<Long> Authors;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<Long> getAuthors() {
        return Authors;
    }

    public void setAuthors(List<Long> authors) {
        Authors = authors;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
